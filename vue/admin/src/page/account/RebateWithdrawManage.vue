<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="16" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px; ">
              <span style=" ">用户</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.rebateWithdraw.userName" style="" placeholder=""
                  style="width: 130px; "></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 15px;">
              <span style=" ">交易时间</span>
              <div style=" display: inline-block; height: 50px;">
                <el-date-picker v-model="queryParams.createTimeRange" type="daterange" 
                  align="center" unlink-panels range-separator="至" start-placeholder="开始日期"
                  end-placeholder="结束日期" :picker-options="pickerOptions"
                  format="yyyy-MM-dd" value-format="yyyy-MM-dd" 
                  style="vertical-align: middle; width: 280px;">
                </el-date-picker>
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getRebateWithdrawList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="rebateList.records" stripe style="width: 100%; " 
        >
        <el-table-column prop="user.userName" label="用户名称" min-width="10%" align="center">
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="提现金额(元)" min-width="10%" align="center"></el-table-column>
        <!--
        <el-table-column prop="rebate.number" label="余额(元)" min-width="10%" align="center"></el-table-column>
        -->
        <el-table-column prop="status" label="状态" min-width="10%" align="center">
          <template slot-scope="scope">
            <span style="color: #409EFF;" v-if="scope.row.status == 1">
              {{ scope.row.status | translate(static.withdrawStatus, 'value', 'label') }}
            </span>
            <span style=" " v-if="scope.row.status != 1" >
              {{ scope.row.status | translate(static.withdrawStatus, 'value', 'label') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="payTime" label="支付时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="qrcodeUrl" label="收款二维码" min-width="10%" align="center">
          <template slot-scope="scope">
            <img v-if="scope.row.qrcodeUrl" 
              :src="downloadImageHeader + scope.row.qrcodeUrl" 
              style="width: 50px; height: 50px;" @click="showPayCode(scope.row)" >
            <img v-else style="width: 50px; height: 50px; border: 1px solid #EBEEF5; "></img>
          </template>
        </el-table-column>  
        <el-table-column prop="rebateWithdrawId" label="操作" min-width="10%" align="center">
          <!--
          <template slot-scope="scope">
            <a @click="payWithdraw(scope.row.rebateWithdrawId)" 
              style="color: #409EFF; " v-if="scope.row.status == 1">支付</a>
          </template>
          -->
          <template slot-scope="scope">
            <a @click="finishWithdraw(scope.row.rebateWithdrawId)" 
              style="color: #409EFF; " v-if="scope.row.status == 1">完成支付</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="float: right; margin-top: 5px; ">
      <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
        :total="rebateList.total" :current-page="queryParams.page.current" 
        @current-change="handleCurrentChange" ></el-pagination>
    </div>

    <el-dialog :title='"用户:" + rebateWithdraw.userName + " 金额:￥" + rebateWithdraw.number' 
      :visible.sync="qrcodeDialogVisible" width="20%" center>
      <div style="width: 100%; text-align: center; ">
        <img v-if="rebateWithdraw.qrcodeUrl" 
          :src="downloadImageHeader + rebateWithdraw.qrcodeUrl" 
          style="width: 150px; height: 150px;" >
      </div>
    </el-dialog>

  </div>    
</template>

<script>
   
export default {
  name: 'rebateWithdrawManage', 
  data() {
    return {
      qrcodeDialogVisible: false,
      altImage: 'this.src="' + require('../../assets/imgs/user_img_default.jpg') + '"',
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl() + 'wxQrcode/',
      static:{
        // 1未支付，2待领取，3已领取，4已退款
        withdrawStatus:[
          {label:'未支付',value: 1},
          {label:'待领取',value: 2},
          {label:'已支付',value: 3},
          {label:'超时未领取',value: 4},
        ],
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      }, 
      rebateList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        rebateWithdraw: {
          userName: '',
        },
        createTimeRange: []
      },
      rebateWithdraw: {
        rebateWithdrawId: 0, 
        staus: 2,
        qrcodeUrl: '',
        number: '',
        userName: ''
      },
    }
  },

  methods: {
    showPayCode(item) {
      console.log("showPayCode " + JSON.stringify(item));
      this.rebateWithdraw.qrcodeUrl = item.qrcodeUrl;
      this.rebateWithdraw.userName = item.user.userName;
      this.rebateWithdraw.number = item.number;
      this.qrcodeDialogVisible = true;
    },
    payWithdraw(rebateWithdrawId) {
      this.$confirm('确定支付吗?').then(() => {
        console.log("payWithdraw ");
        this.rebateWithdraw.rebateWithdrawId = rebateWithdrawId;
        this.$fetch.payRebateWithdraw(this.rebateWithdraw).then((resp) => {
          console.log("payWithdraw " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("支付成功");
            this.getRebateWithdrawList();
          } else {
            this.$message.error(resp.returnMsg + "");
          }
        }, (error) => {
          this.$message.error(error);
        });
      }).catch(() => {
        console.log("payWithdraw cancel");
      });
    },
    finishWithdraw(rebateWithdrawId) {
      this.$confirm('确定完成支付吗?').then(() => {
        this.rebateWithdraw.rebateWithdrawId = rebateWithdrawId;
        console.log("finishWithdraw params " + JSON.stringify(this.rebateWithdraw));
        this.$fetch.finishWithdraw(this.rebateWithdraw).then((resp) => {
          console.log("finishWithdraw resp " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("成功");
            this.resetQuery();
          } else {
            this.$message.error(resp.returnMsg + "");
          }
        }, (error) => {
          this.$message.error(error);
        });
      }).catch(() => {
        console.log("payWithdraw cancel");
      });
    },
    resetQuery() {
      this.queryParams.rebateWithdraw.userName = '';
      this.queryParams.createTimeRange = [];
      this.getRebateWithdrawList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getRebateWithdrawList();
    },
    getRebateWithdrawList() {
      this.$fetch.getRebateWithdrawList(this.queryParams).then((resp) => {
        console.log("getRebateWithdrawList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          var dataObj = JSON.parse(JSON.stringify(resp.data));
          this.rebateList = dataObj.page;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getRebateWithdrawList();
  }, 

  mounted: function() {
     
  },
}
</script>
 
<style >  
.el-input__inner { 
  height: 35px;
} 

</style>
