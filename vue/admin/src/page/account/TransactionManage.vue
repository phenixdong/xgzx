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
                <el-input v-model="queryParams.transaction.nickName" style="" placeholder=""
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
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getTransactionList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="transactionList.records" stripe style="width: 100%" 
        @selection-change="handleSelectionChange"
        >
        <el-table-column prop="nickName" label="用户名称" min-width="15%" align="center">
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.nickName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="交易金额(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="createTime" label="交易时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="type" label="交易类型" min-width="10%" align="center">
          <template slot-scope="scope">
            {{ scope.row.type | translate(static.transactionTypes, 'value', 'label') }}
          </template>
        </el-table-column>
        <el-table-column prop="orderId" label="订单号" min-width="40%" align="center"></el-table-column>
      </el-table>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
          :total="transactionList.total" :current-page="queryParams.page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
    </div>
  </div>    
</template>

<script>
   
export default {
  name: 'transactionManage', 
  data() {
    return {
      static:{
        // 1购买会员，2购买商品，3返点，4充值，5提现
        transactionTypes:[
          {label:'购买会员',value: 1},
          {label:'购买商品',value: 2},
          {label:'返点',value: 3},
          {label:'充值',value: 4},
          {label:'提现',value: 5},
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
      transactionList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        transaction: {
          nickName: '',
        },
        createTimeRange: []
      },
      transaction: {
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.transaction.nickName = '';
      this.queryParams.createTimeRange = [];
      this.getTransactionList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getTransactionList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.selectIds = [];
      for (var i = 0; i < val.length; i++) {
        this.selectIds.push(val[i].transactionId);
      }
    }, 
    showDetailDialog(transactionId) {
      console.log("showDetailDialog transactionId " + transactionId);
      this.$fetch.getTransactionInfo(transactionId).then((resp) => {
        console.log("getTransactionInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.transaction = JSON.parse(JSON.stringify(resp.data));
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
    showUpdateDialog(transactionId) {
      console.log("showUpdateDialog transactionId " + transactionId);
      this.$fetch.getTransactionInfo(transactionId).then((resp) => {
        console.log("getTransactionInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.transaction = JSON.parse(JSON.stringify(resp.data));
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
    lockTransaction(transactionId, state) {
      var params = {transactionId: transactionId + '', transactionState: state};
      this.$fetch.lockTransaction(params).then((resp) => {
        console.log("lockTransaction " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.$message.success("成功");
          this.getTransactionList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    deleteTransaction(transactionId) {
      this.selectIds.push(transactionId);
      this.batchDeleteTransaction();
    },
    batchDeleteTransaction() {
      console.log("batchDeleteTransaction " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteTransaction(params).then((resp) => {
          console.log("deleteTransaction " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getTransactionList();
          } else {
            this.$message.error(resp.returnMsg + "");
          }
        }, (error) => {
          this.$message.error(error);
        });
      }).catch(() => {
        console.log("delete cancel ");
      });
    },  
    cancelAddUpdateDialog() {
      this.addUpdateDialogVisible = false;
    },
     
    addTransaction() {
      this.$fetch.addTransaction(this.transaction).then((resp) => {
        console.log("addTransaction resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("新增成功");
          this.getTransactionList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    updateTransaction() {
      this.$fetch.updateTransaction(this.transaction).then((resp) => {
        console.log("updateTransaction resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.getTransactionList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    getTransactionList() {
      console.log("getTransactionList param " + JSON.stringify(this.queryParams));
      this.$fetch.getTransactionList(this.queryParams).then((resp) => {
        console.log("getTransactionList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.transactionList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getTransactionList();
  }, 

  mounted: function() {
     
  },
}
</script>
 
<style >  
/*
 el-input-number的属性也是由el-input__inner设置，只是右边加了数字控件，
 size="medium"设置数字控件高度
*/
.el-input__inner { 
  height: 35px;
} 

</style>
