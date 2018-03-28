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
                <el-input v-model="queryParams.rebateTransaction.userName" style="" placeholder=""
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
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getRebateTransactionList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="rebateList.records" stripe style="width: 100%" 
        >
        <el-table-column prop="userName" label="用户名称" min-width="15%" align="center">
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="返点金额(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="orderProductType" label="交易类型" min-width="10%" align="center">
          <template slot-scope="scope">
            {{ scope.row.orderProductType | translate(static.orderProductType, 'value', 'label') }}
          </template>
        </el-table-column>
        <el-table-column prop="orderUserName" label="交易用户" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="orderNumber" label="交易金额(元)" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="createTime" label="交易时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="orderId" label="订单号" min-width="15%" align="center"></el-table-column>
      </el-table>
    </div>

    <div style="float: right; margin-top: 5px; ">
      <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
        :total="rebateList.total" :current-page="queryParams.page.current" 
        @current-change="handleCurrentChange" ></el-pagination>
    </div>
  </div>    
</template>

<script>
   
export default {
  name: 'rebateManage', 
  data() {
    return {
      static:{
        // 商品类型，1会员，2商品
        orderProductType:[
          {label:'购买会员',value: 1},
          {label:'购买商品',value: 2},
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
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
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
        rebateTransaction: {
          userName: '',
        },
        createTimeRange: []
      },
      rebateTransaction: {
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.rebateTransaction.userName = '';
      this.queryParams.createTimeRange = [];
      this.getRebateTransactionList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getRebateTransactionList();
    },
    showDetailDialog(rebateId) {
      console.log("showDetailDialog rebateId " + rebateId);
      this.$fetch.getRebateInfo(rebateId).then((resp) => {
        console.log("getRebateInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.rebateTransaction = JSON.parse(JSON.stringify(resp.data));
          this.dialogType = 3;// 对话框类型为“详情”
          this.addUpdateTitle = '详情';
          this.addUpdateDialogVisible = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    showUpdateDialog(rebateId) {
      console.log("showUpdateDialog rebateId " + rebateId);
      this.$fetch.getRebateInfo(rebateId).then((resp) => {
        console.log("getRebateInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.rebateTransaction = JSON.parse(JSON.stringify(resp.data));
          this.dialogType = 2;// 对话框类型为“修改”
          this.addUpdateTitle = '修改';
          this.addUpdateDialogVisible = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    cancelAddUpdateDialog() {
      this.addUpdateDialogVisible = false;
    },
    getRebateTransactionList() {
      this.$fetch.getRebateTransactionList(this.queryParams).then((resp) => {
        console.log("getRebateTransactionList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.rebateList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getRebateTransactionList();
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
