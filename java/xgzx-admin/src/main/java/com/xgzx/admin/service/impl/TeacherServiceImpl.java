package com.xgzx.admin.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.BulkBuyRegister;
import com.xgzx.admin.entity.LessonWatch;
import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.Teacher;
import com.xgzx.admin.mapper.BulkBuyRegisterMapper;
import com.xgzx.admin.mapper.LessonWatchMapper;
import com.xgzx.admin.mapper.OrdersMapper;
import com.xgzx.admin.mapper.TeacherMapper;
import com.xgzx.admin.service.TeacherService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
	
	@Autowired
	TeacherMapper teacherMapper;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	LessonWatchMapper lessonWatchMapper;
	@Autowired
	BulkBuyRegisterMapper bulkBuyRegisterMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		Teacher teacher = teacherMapper.customQuery(id);
		return new BaseResult("1", "成功", teacher);
	}
	
	/*  
	 * 查询时间往前推一个月或者一年，再和订单创建时间比较
	 */
	@Override
	public BaseResult statisticRebate(Map<String, Object> param) throws Exception {
		String startTime = (String) param.get("startTime");
		String endTime = (String) param.get("endTime");
		String queryTeacherName = (String) param.get("teacherName");
		Integer bulkBuyPrice = Integer.valueOf((String) param.get("bulkBuyPrice"));
		Integer rebateRate = Integer.valueOf((String) param.get("rebateRate"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("startTime " + startTime + " endTime " + endTime);
		
		// 月份-1，用于包月用户统计
		Calendar monthMemberStartCalendar = Calendar.getInstance(); 
		monthMemberStartCalendar.setTime(sdf.parse(startTime));
		monthMemberStartCalendar.add(Calendar.MONTH, -1); 
		String monthMemberStart = sdf.format(monthMemberStartCalendar.getTime());
		
		Calendar monthMemberEndCalendar = Calendar.getInstance(); 
		monthMemberEndCalendar.setTime(sdf.parse(endTime));
		monthMemberEndCalendar.add(Calendar.MONTH, -1); 
		String monthMemberEnd = sdf.format(monthMemberEndCalendar.getTime());
		System.out.println("monthMemberStart " + monthMemberStart + " monthMemberEnd " + monthMemberEnd);
		
		// 年份-1，用于包年用户统计
		Calendar yearMemberStartCalendar = Calendar.getInstance(); 
		yearMemberStartCalendar.setTime(sdf.parse(startTime));
		yearMemberStartCalendar.add(Calendar.YEAR, -1); 
		String yearMemberStart = sdf.format(yearMemberStartCalendar.getTime());
		
		Calendar yearMemberEndCalendar = Calendar.getInstance(); 
		yearMemberEndCalendar.setTime(sdf.parse(endTime));
		yearMemberEndCalendar.add(Calendar.YEAR, -1); 
		String yearMemberEnd = sdf.format(yearMemberEndCalendar.getTime());
		System.out.println("yearMemberStart " + yearMemberStart + " yearMemberEnd " + yearMemberEnd);
		
		// 到期的包月订单
		Map<String, Object> monthQueryMap = new HashMap<>();
		monthQueryMap.put("startTime", monthMemberStart);
		monthQueryMap.put("endTime", monthMemberEnd);
		monthQueryMap.put("memberType", 1);
		List<Orders> monthOrdersList = ordersMapper.getExpiryOrders(monthQueryMap);
		
		// 到期的包年订单
		Map<String, Object> yearQueryMap = new HashMap<>();
		yearQueryMap.put("startTime", yearMemberStart);
		yearQueryMap.put("endTime", yearMemberEnd);
		yearQueryMap.put("memberType", 2);
		List<Orders> yearOrdersList = ordersMapper.getExpiryOrders(yearQueryMap);
		
		// 讲师列表
		Map<String, Object> teacherQueryMap = new HashMap<>();
		if (null != queryTeacherName && !"".equals(queryTeacherName)) {
			teacherQueryMap.put("teacher_name", queryTeacherName);
		}
		teacherQueryMap.put("valid_tag", 1);
		List<Teacher> teacherList = teacherMapper.selectByMap(teacherQueryMap);
		
		// 查询包月用户观看记录，将总金额分给讲师
		if (null != monthOrdersList && monthOrdersList.size() > 0) {
			for (Orders item : monthOrdersList) {
				String userId = item.getUserId();
				Date watchStartDate = item.getCreateTime();
				String watchStartTime = sdfDate.format(watchStartDate);
				Calendar monthMemberWatchCalendar = Calendar.getInstance(); 
				monthMemberWatchCalendar.setTime(watchStartDate);
				monthMemberWatchCalendar.add(Calendar.MONTH, 1); 
				String watchEndTime = sdfDate.format(monthMemberWatchCalendar.getTime());
				System.out.println("watchStartTime " + watchStartTime + " watchEndTime " + watchEndTime);
				
				Map<String, Object> watchQueryMap = new HashMap<>();
				watchQueryMap.put("userId", userId);
				watchQueryMap.put("startTime", watchStartTime);
				watchQueryMap.put("endTime", watchEndTime);
				List<Map<String, Object>> monthWatchList = lessonWatchMapper.getListByTime(watchQueryMap);
				System.out.println("monthOrders id " + item.getOrderId() + " totalFee " + item.getTotalFee());
				if (null != monthWatchList && monthWatchList.size() > 0) {
					// 观看的每个课程的返点
					float totalFee = item.getTotalFee().floatValue();
					float eachRebate = totalFee / monthWatchList.size();
					
					// 每个课程返点分给相应讲师
					for (Map<String, Object> watchItem : monthWatchList) {
						Integer teacherId = ((Long) watchItem.get("teacherId")).intValue();
						String teacherName = (String) watchItem.get("teacherName");
						for (Teacher teacherItem : teacherList) {
							if (teacherItem.getTeacherId() == teacherId) {
								System.out.println("monthOrders teacher " + teacherName + " rebate " + eachRebate);
								float monthMemberRebate = teacherItem.getMonthMemberRebate() + eachRebate * rebateRate / 100;
								teacherItem.setMonthMemberRebate((float)(Math.floor(monthMemberRebate * 100) / 100));
								float totalRebate = teacherItem.getBulkMemberRebate() + teacherItem.getMonthMemberRebate()
									+ teacherItem.getYearMemberRebate();
								teacherItem.setTotalRebate((float)(Math.floor(totalRebate * 100) / 100));
								break;
							}
						}
					}
				}
			}
		}
		
		// 查询包年用户观看记录，将总金额分给讲师
		if (null != yearOrdersList && yearOrdersList.size() > 0) {
			for (Orders yearItem : yearOrdersList) {
				String yearWatchUserId = yearItem.getUserId();
				Date yearWatchStartDate = yearItem.getCreateTime();
				String yearWatchStartTime = sdfDate.format(yearWatchStartDate);
				Calendar yearMemberWatchCalendar = Calendar.getInstance(); 
				yearMemberWatchCalendar.setTime(yearWatchStartDate);
				yearMemberWatchCalendar.add(Calendar.YEAR, 1); 
				String yearWatchEndTime = sdfDate.format(yearMemberWatchCalendar.getTime());
				System.out.println("yearWatchStartTime " + yearWatchStartTime + " yearWatchEndTime " + yearWatchEndTime);
				
				Map<String, Object> yearWatchQueryMap = new HashMap<>();
				yearWatchQueryMap.put("userId", yearWatchUserId);
				yearWatchQueryMap.put("startTime", yearWatchStartTime);
				yearWatchQueryMap.put("endTime", yearWatchEndTime);
				List<Map<String, Object>> yearWatchList = lessonWatchMapper.getListByTime(yearWatchQueryMap);
				System.out.println("yearOrders id " + yearItem.getOrderId() + " totalFee " + yearItem.getTotalFee());
				
				if (null != yearWatchList && yearWatchList.size() > 0) {
					// 观看的每个课程的返点
					float totalFee = yearItem.getTotalFee().floatValue();
					float eachRebate = totalFee / yearWatchList.size();
					
					// 每个课程返点分给相应讲师
					for (Map<String, Object> watchItem : yearWatchList) {
						Integer teacherId = ((Long) watchItem.get("teacherId")).intValue();
						String teacherName = (String) watchItem.get("teacherName");
						
						for (Teacher teacherItem : teacherList) {
							if (teacherItem.getTeacherId() == teacherId) {
								System.out.println("yearOrders teacher " + teacherName + " rebate " + eachRebate);
								float yearMemberRebate = teacherItem.getYearMemberRebate() + eachRebate * rebateRate / 100;
								teacherItem.setYearMemberRebate((float)(Math.floor(yearMemberRebate * 100) / 100));
								float totalRebate = teacherItem.getBulkMemberRebate() + teacherItem.getMonthMemberRebate()
									+ teacherItem.getYearMemberRebate();
								teacherItem.setTotalRebate((float)(Math.floor(totalRebate * 100) / 100));
								break;
							}
						}
					}
				}
			}
		}
		
		// 查询集团用户
		Map<String, Object> bulkBuyQueryMap = new HashMap<>();
		bulkBuyQueryMap.put("startTime", yearMemberStart);
		bulkBuyQueryMap.put("endTime", yearMemberEnd);
		List<BulkBuyRegister> bulkBuyList = bulkBuyRegisterMapper.selectByTime(bulkBuyQueryMap);
		
		// 集团用户观看记录，将总金额分给讲师
		if (null != bulkBuyList && bulkBuyList.size() > 0) {
			for (BulkBuyRegister bulkBuyItem : bulkBuyList) {
				String bulkWatchUserId = bulkBuyItem.getUserId();
				Date bulkWatchStartDate = bulkBuyItem.getCreateTime();
				String bulkWatchStartTime = sdfDate.format(bulkWatchStartDate);
				Calendar bulkMemberWatchCalendar = Calendar.getInstance(); 
				bulkMemberWatchCalendar.setTime(bulkWatchStartDate);
				bulkMemberWatchCalendar.add(Calendar.YEAR, 1); 
				String bulkWatchEndTime = sdfDate.format(bulkMemberWatchCalendar.getTime());
				System.out.println("bulkWatchStartTime " + bulkWatchStartTime + " bulkWatchEndTime " + bulkWatchEndTime);
				
				Map<String, Object> bulkWatchQueryMap = new HashMap<>();
				bulkWatchQueryMap.put("userId", bulkWatchUserId);
				bulkWatchQueryMap.put("startTime", bulkWatchStartTime);
				bulkWatchQueryMap.put("endTime", bulkWatchEndTime);
				List<Map<String, Object>> bulkWatchList = lessonWatchMapper.getListByTime(bulkWatchQueryMap);
				System.out.println("bulkBuy id " + bulkBuyItem.getBulkBuyRegisterId() + " totalFee " + bulkBuyPrice);
				
				if (null != bulkWatchList && bulkWatchList.size() > 0) {
					// 观看的每个课程的返点
					float totalFee = bulkBuyPrice.floatValue();
					float eachRebate = totalFee / bulkWatchList.size();
					
					// 每个课程返点分给相应讲师
					for (Map<String, Object> watchItem : bulkWatchList) {
						Integer teacherId = ((Long) watchItem.get("teacherId")).intValue();
						String teacherName = (String) watchItem.get("teacherName");
						
						for (Teacher teacherItem : teacherList) {
							if (teacherItem.getTeacherId() == teacherId) {
								System.out.println("bulkBuy teacher " + teacherName + " rebate " + eachRebate);
								float bulkMemberRebate = teacherItem.getBulkMemberRebate() + eachRebate * rebateRate / 100;
								teacherItem.setBulkMemberRebate((float)(Math.floor(bulkMemberRebate * 100) / 100));
								float totalRebate = teacherItem.getBulkMemberRebate() + teacherItem.getMonthMemberRebate()
									+ teacherItem.getYearMemberRebate();
								teacherItem.setTotalRebate((float)(Math.floor(totalRebate * 100) / 100));
								break;
							}
						}
					}
				}
			}
		}
		
		
		// 分页，只从list中取一页的数据返回
		Page<Teacher> page = JsonObjUtils.map2obj(param, "page", Page.class);
		List<Teacher> pagedList = new ArrayList<>();
		for (int i = page.getSize() * (page.getCurrent() - 1); 
				i < page.getSize() * page.getCurrent(); i++) {
			if (i >= teacherList.size()) {// 最后一页时，i值会超过总数
				break;
			}
			pagedList.add(teacherList.get(i));
		}
		page.setRecords(pagedList);
		page.setTotal(teacherList.size());		
		return new BaseResult(page);		
	}
		
	 
}
