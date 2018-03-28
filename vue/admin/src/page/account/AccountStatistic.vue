<template>  
  <div>   
    <div class="searchHeader" style="height: 50px;" > 
    </div>  
    <div style="background-color: white; height: 45px; text-align: left; 
      padding-left: 20px; ">
      <span style="line-height: 45px; ">总收入</span>
      <span style="margin-left: 5px; ">{{accountStatistic.totalIn}}</span>
    </div>
    <div class="grey-line" style="height: 1px; margin-top: 0px; 
      "></div> 
    <div style="background-color: white; height: 45px; text-align: left; 
      padding-left: 20px; ">
      <span style="line-height: 45px; ">总返点</span>
      <span style="margin-left: 5px; ">{{accountStatistic.totalRebate}}</span>
      
      <span style="line-height: 45px; margin-left: 10px; ">公共返点</span>
      <span v-if="accountStatistic.totalPubRebate" style="margin-left: 5px; ">{{accountStatistic.totalPubRebate}}</span>
      <span v-else style="margin-left: 5px; ">0</span>

      <span style="line-height: 45px; margin-left: 10px; ">个人返点</span>
      <span v-if="accountStatistic.totalPersonRebate" style="margin-left: 5px; ">{{accountStatistic.totalPersonRebate}}</span>
      <span v-else style="margin-left: 5px; ">0</span>
    </div>
    <div class="grey-line" style="height: 1px; margin-top: 0px; "></div> 
    <div style="background-color: white; height: 45px; text-align: left; 
      padding-left: 20px; ">
      <span style="line-height: 45px; ">总提现</span>
      <span style="margin-left: 5px; ">{{accountStatistic.totalWithdraw}}</span>
    </div>
    <div class="grey-line" style="height: 1px; margin-top: 0px; 
      "></div> 
  </div>    
</template>

<script>
   
export default {
  name: 'memberTypeManage', 
  data() {
    return {
      accountStatistic: {
        totalIn: 0,
        totalRebate: 0, 
        totalWithdraw: 0
      },
      selectIds: [],
      memberType: {
        memberTypeName: '',
        orderNo: 0,
        price: 0, 
        period: 0,
        description: '',
      },
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
      memberTypeList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 10,
        },
        memberType: {
          memberTypeName: '',
        },
        createTimeRange: []
      },
      memberType: {
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    getAccountStatistic() {
      this.$fetch.getAccountStatistic(this.queryParams).then((resp) => {
        console.log("getAccountStatistic resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.accountStatistic = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getAccountStatistic();
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
