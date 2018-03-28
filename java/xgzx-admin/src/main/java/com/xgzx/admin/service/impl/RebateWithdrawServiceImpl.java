package com.xgzx.admin.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.entity.RebateTransaction;
import com.xgzx.admin.entity.RebateWithdraw;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.RebateMapper;
import com.xgzx.admin.mapper.RebateWithdrawMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.RebateTransactionService;
import com.xgzx.admin.service.RebateWithdrawService;
import com.xgzx.admin.service.WXService;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class RebateWithdrawServiceImpl extends ServiceImpl<RebateWithdrawMapper, RebateWithdraw> implements RebateWithdrawService {
	
	@Autowired
	RebateWithdrawMapper rebateWithdrawMapper;
	@Autowired
	RebateTransactionService rebateTransactionService;
	@Autowired
	RebateMapper rebateMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	WXService wxService;
	
	
	@Override
	public BaseResult customQuery(int id) {
		RebateWithdraw rebateWithdraw = rebateWithdrawMapper.customQuery(id);
		return new BaseResult("1", "成功", rebateWithdraw);
	}
	
	@Override
	public BaseResult selectRebateWithdrawList(Page<RebateWithdraw> page, 
			Map<String, Object> rebateWithdrawMap) throws Exception {
		// 更新红包领取状态，如果为超时未领取退回的，则退回到返点账户
		BaseResult statusResult = updateRedPackStatus(rebateWithdrawMap);
		if (!"1".equals(statusResult.getReturnCode())) {
			return statusResult;
		}
		
		List<RebateWithdraw> list = rebateWithdrawMapper
				.selectRebateWithdrawList(page, rebateWithdrawMap); 
		// 每条请求的用户信息、返点余额
		if (null != list && list.size() > 0) {
			for (RebateWithdraw item : list) {
				// 用户信息
				String userId = item.getUserId();
				User user = userMapper.selectById(userId);
				item.setUser(user);
				
				// 返点余额
				Rebate rebate = new Rebate();
				rebate.setUserId(userId);
				rebate = rebateMapper.selectOne(rebate);
				item.setRebate(rebate);
			}
		}
		page.setRecords(list);
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		// 如果有userId，则查询余额
		if (null != rebateWithdrawMap.get("userId")
				&& !"".equals(rebateWithdrawMap.get("userId"))) {
			String userId = (String) rebateWithdrawMap.get("userId");
			Rebate rebate = new Rebate();
			rebate.setUserId(userId);
			rebate = rebateMapper.selectOne(rebate);
			map.put("rebate", rebate);
		}
		return new BaseResult(map);
	}

	private BaseResult updateRedPackStatus(Map<String, Object> param) throws Exception {
		Map<String, Object> statusParam = new HashMap<>();
		statusParam.put("status", 2);// 查询状态为“待领取”的申请
		if (null != param.get("userId")) {
			statusParam.put("userId", param.get("userId"));
		}
		List<RebateWithdraw> list = rebateWithdrawMapper
				.selectRebateWithdrawList(new Page(1, 2000), statusParam);
		if (null == list || 0 == list.size()) {
			new BaseResult();
		}
		 
		// 逐条查询红包状态
		for (RebateWithdraw item : list)
		{
			String userId = item.getUserId();
			Integer rebateWithdrawId = item.getRebateWithdrawId();
			BigDecimal number = item.getNumber();
			String mchBillNo = item.getMchBillNo();
			// 查询微信服务器
			BaseResult result = wxService.checkRedPackStatus(mchBillNo);
			if ("1".equals(result.getReturnCode())) 
			{
				Map<String, String> map = (Map<String, String>) result.getData();
				String status = map.get("status");
				if ("RECEIVED".equals(status)) {
					// 已领取
					RebateWithdraw updateWithdraw = new RebateWithdraw();
					updateWithdraw.setRebateWithdrawId(rebateWithdrawId);
					updateWithdraw.setStatus(3);// 1未支付，2待领取，3已领取，4已退款
					int ret = rebateWithdrawMapper.updateById(updateWithdraw);
					if (ret <= 0) {
						CommonError.CommonErr(new BaseErrResult("-1", "更新领取状态失败"));
					}
				} else if ("REFUND".equals(status)) {
					// 超时未领取
					RebateWithdraw updateWithdraw = new RebateWithdraw();
					updateWithdraw.setRebateWithdrawId(rebateWithdrawId);
					updateWithdraw.setStatus(4);// 1未支付，2待领取，3已领取，4已退款
					int ret = rebateWithdrawMapper.updateById(updateWithdraw);
					if (ret <= 0) {
						CommonError.CommonErr(new BaseErrResult("-1", "更新领取状态失败"));
					}
					
					// 退款到用户返点账户
					Rebate rebate = new Rebate();
					rebate.setUserId(userId);
					rebate = rebateMapper.selectOne(rebate);
					rebate.setNumber(rebate.getNumber().add(number));
					ret = rebateMapper.updateById(rebate);
					if (ret <= 0) {
						CommonError.CommonErr(new BaseErrResult("-1", "更新返点账户失败"));
					}
					
					// 增加一条返点交易记录
					RebateTransaction rebateTransaction = new RebateTransaction();
					rebateTransaction.setUserId(userId);
					rebateTransaction.setNumber(number);
					rebateTransaction.setOperation(3);// 1收入，2支出，3红包未领取退款
					rebateTransaction.setOrderId(mchBillNo);
					rebateTransaction.setValidTag(1);
					boolean success = rebateTransactionService.insert(rebateTransaction);
					if (!success) {
						CommonError.CommonErr(new BaseErrResult("-1", "返点记录新增失败"));
					}
				}
			} else {
				return result;
			}
		}
		return new BaseResult();
	}
	
	@Override
	public BaseResult pay(Map<String, Object> param) throws Exception {
		RebateWithdraw rebateWithdraw = JsonObjUtils.map2obj(param, RebateWithdraw.class);
		// 查询提现详情
		rebateWithdraw = rebateWithdrawMapper.selectOne(rebateWithdraw);
		String userId = rebateWithdraw.getUserId();
		
		// 查询用户
		User user = userMapper.selectById(userId);
		
		// 微信商家红包支付
		Map<String, Object> payParam = new HashMap<>();
		payParam.put("openId", user.getOpenId());
		// 注意要转换成integer，再转string，否则会有xx.00的字符串
		payParam.put("number", (rebateWithdraw.getNumber().multiply(new BigDecimal("100"))).intValue() + "");
		BaseResult payResult = wxService.payWithdraw(payParam);
		if ("0".equals(payResult.getReturnCode())) {
			CommonError.CommonErr(new BaseErrResult("-1", payResult.getReturnMsg()));
		} 
			 
		// 更新申请状态为“已支付”， 存储mch_billno 
		Map<String, String> respMap = (Map<String, String>) payResult.getData();
		String mchBillNo = respMap.get("mch_billno");
		rebateWithdraw.setMchBillNo(mchBillNo);
		rebateWithdraw.setStatus(2);// 1未支付，2待领取，3已领取，4已退款
		rebateWithdraw.setPayTime(new Date());
		int ret = rebateWithdrawMapper.updateById(rebateWithdraw); 
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "提现申请更新失败"));
      	}
		
        // 新增提现记录
        RebateTransaction rebateTransaction = new RebateTransaction();
        rebateTransaction.setUserId(rebateWithdraw.getUserId());
        rebateTransaction.setNumber(rebateWithdraw.getNumber());
        rebateTransaction.setOperation(2);// 1收入，2支出
        rebateTransaction.setValidTag(1);
        boolean success = rebateTransactionService.insert(rebateTransaction);
        if (!success) {
        	CommonError.CommonErr(new BaseErrResult("-1", "提现记录新增失败"));
        }
        return new BaseResult("1", "成功", null);
	}

	@Override
	public BaseResult insertRebateWithdraw(Map<String, Object> param) throws Exception {
		RebateWithdraw rebateWithdraw =  JsonObjUtils.map2obj(param, RebateWithdraw.class);
		String userId = rebateWithdraw.getUserId();
		BigDecimal number = rebateWithdraw.getNumber();
		if (null == userId || "".equals(userId)) {
			return new BaseResult("0", "userId为空", null);
		}
		if (1 != number.compareTo(new BigDecimal(0))) {
			return new BaseResult("0", "数值要大于0", null);
		}
		// 提现值是否超过余额
		Rebate rebate = new Rebate();
		rebate.setUserId(userId);
		rebate = rebateMapper.selectOne(rebate);
		if (1 == number.compareTo(rebate.getNumber())) {
			return new BaseResult("0", "超过余额", null);
		}
		// 新增
		rebateWithdraw.setValidTag(1);
		rebateWithdraw.setStatus(1);
		rebateWithdraw.setApplyTime(new Date());
		int ret = rebateWithdrawMapper.insert(rebateWithdraw); 
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "新增失败"));
        }
        // 更新余额
        rebate.setNumber(rebate.getNumber().subtract(number));
        ret = rebateMapper.updateById(rebate);
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "更新余额失败"));
        }
        return new BaseResult();
	}
	
	@Override
	public BaseResult deleteRebateWithdraw(Map<String, Object> param) throws Exception {
		String userId = (String) param.get("userId");
		Integer id = (Integer) param.get("rebateWithdrawId");
		// 查询提现金额
		RebateWithdraw rebateWithdraw = rebateWithdrawMapper.selectById(id);
		if (null == rebateWithdraw) {
			return new BaseResult("0", "没有该记录", null);
		}
		BigDecimal number = rebateWithdraw.getNumber();
				
		// 删除
		rebateWithdraw.setRebateWithdrawId(id);
		rebateWithdraw.setUserId(userId);
		rebateWithdraw.setValidTag(0);
		int ret = rebateWithdrawMapper.updateById(rebateWithdraw);
		if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "删除失败"));
        }
		
		// 申请提现的金额返回到总金额
		Rebate rebate = new Rebate();
		rebate.setUserId(userId);
		rebate = rebateMapper.selectOne(rebate);
        rebate.setNumber(rebate.getNumber().add(number));
        ret = rebateMapper.updateById(rebate);
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "更新余额失败"));
        }
        return new BaseResult();
	}
}
