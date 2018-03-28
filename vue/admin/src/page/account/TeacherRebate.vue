<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="18" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <!--
            <div style="float: left; line-height: 50px; ">
              <span style=" ">讲师</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.teacherName" style="" placeholder=""
                  style="width: 130px; "></el-input>
              </div>
            </div>
            -->
            <div style="float: left; line-height: 50px; margin-left: 0px;  ">
              <span style=" "><span class="red-star">*</span>返点比例%</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.rebateRate" style="" placeholder=""
                  style="width: 60px; "></el-input>
              </div>
            </div>
            <div style="float: left; line-height: 50px; margin-left: 20px; ">
              <span style=" "><span class="red-star">*</span>团购会员单价(元)</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.bulkBuyPrice" style="" placeholder=""
                  style="width: 60px; "></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 0px; margin-left: 20px; ">
              <span style=" "><span class="red-star">*</span>会员到期时间</span>
              <div style=" display: inline-block; height: 50px;">
                <el-date-picker v-model="queryParams.startTime" type="month" 
                  value-format="yyyy-MM"
                  align="center" style="vertical-align: middle; width: 120px;">
                </el-date-picker>
              </div>
              至
              <div style=" display: inline-block; height: 50px;">
                <el-date-picker v-model="queryParams.endTime" type="month" 
                  value-format="yyyy-MM"
                  align="center" style="vertical-align: middle; width: 120px;">
                </el-date-picker>
              </div>
            </div>
          </el-col>
          <el-col :span="6" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getTeacherRebate">统计</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="rebateList.records" stripe style="width: 100%" 
        >
        <el-table-column prop="teacherName" label="讲师" min-width="15%" align="center">
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.teacherName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="monthMemberRebate" label="包月会员(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="yearMemberRebate" label="包年会员(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="bulkMemberRebate" label="集团会员(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="totalRebate" label="总额(元)" min-width="10%" align="center"></el-table-column>
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
        startTime: '',
        endTime: '',
        rebateRate: '40',
        bulkBuyPrice: '0',
      },
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.teacherName = '';
      this.queryParams.startTime = '';
      this.queryParams.endTime = '';
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getTeacherRebate();
    },
    getTeacherRebate() {
      if ('' == this.queryParams.rebateRate || 0 == this.queryParams.rebateRate 
        || undefined == this.queryParams.rebateRate) {
        this.$message.error('请输入返点比例');
        return;
      }
      if ('' == this.queryParams.bulkBuyPrice || 0 == this.queryParams.bulkBuyPrice 
        || undefined == this.queryParams.bulkBuyPrice) {
        this.$message.error('请输入会员单价');
        return;
      }
      if ('' == this.queryParams.startTime || undefined == this.queryParams.startTime
        || '' == this.queryParams.endTime || undefined == this.queryParams.endTime) {
        this.$message.error('请选择到期时间');
        return;
      }
      if (this.queryParams.endTime < this.queryParams.startTime) {
        this.$message.error('结束时间不能小于开始时间');
        return;
      }

      console.log("getTeacherRebate param " + JSON.stringify(this.queryParams));
      this.$fetch.getTeacherRebate(this.queryParams).then((resp) => {
        console.log("getTeacherRebate resp " + JSON.stringify(resp));
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
 
  mounted: function() {
     
  },
}
</script>
 
<style >  
.el-input__inner { 
  height: 35px;
} 

</style>
