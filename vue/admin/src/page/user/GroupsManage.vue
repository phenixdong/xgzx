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
                <el-input v-model="queryParams.groups.userName" style="" placeholder=""
                  style="width: 130px; "></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 20px;">
              <span style=" ">创建时间</span>
              <div style=" display: inline-block; height: 50px;">
                <el-date-picker v-model="queryParams.createTimeRange" type="daterange" 
                  align="center" unlink-panels range-separator="至" start-placeholder="开始日期"
                  end-placeholder="结束日期" :picker-options="pickerOptions"
                  format="yyyy-MM-dd" value-format="yyyy-MM-dd" style="vertical-align: middle;">
                </el-date-picker>
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getGroupsList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="groupsList.records" stripe style="width: 100%" >
        <el-table-column prop="userName" label="群主" min-width="15%" align="center" >
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="memberNumber" label="人数" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="groupId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="showDetailDialog(scope.row.groupId)" 
              style="color: #409EFF;" >详情</a>
            <!--
            <a @click="deleteGroups(scope.row.groupId)" 
              style="color: #409EFF; ">删除</a>
            -->
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="float: right; margin-top: 5px; ">
      <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
        :total="groupsList.total" :current-page="queryParams.page.current" 
        @current-change="handleCurrentChange" ></el-pagination>
    </div>

    <el-dialog title="群成员列表" :visible.sync="detailDialogVisible" width="50%" 
      center close-on-click-modal>
      <div style="width: 100%; text-align: center; margin-top: -20px;">
        <el-table :data="groups.page.records" stripe style="width: 100%" 
        >
        <el-table-column prop="nickName" label="用户名称" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="createTime" label="入群时间" min-width="15%" align="center"></el-table-column>
      </el-table>
      </div>
    </el-dialog>
  </div>    
</template>

<script>
   
export default {
  name: 'groupsManage', 
  data() {
    return {
      detailDialogVisible: false,
      payCode: '',
      altImage: 'this.src="' + require('../../assets/imgs/user_img_default.jpg') + '"',
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl(),
      static:{
        // 1未支付，2已支付
        withdrawStatus:[
          {label:'未支付',value: 1},
          {label:'已支付',value: 2},
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
      groupsList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        groups: {
          userName: '',
        },
        createTimeRange: []
      },
      groups: {
        groupId: 0, 
        page: {}
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.groups.userName = '';
      this.queryParams.createTimeRange = [];
      this.getGroupsList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getGroupsList();
    },
    showDetailDialog(groupId) { 
      console.log("getGroupsInfo param " + JSON.stringify(groupId));
      this.$fetch.getGroupsInfo(groupId).then((resp) => {
        console.log("getGroupsInfo " + JSON.stringify(resp.data));
        if ("1" == resp.returnCode) {
          this.groups = JSON.parse(JSON.stringify(resp.data));
          this.detailDialogVisible = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    getGroupsList() {
      this.$fetch.getGroupsList(this.queryParams).then((resp) => {
        console.log("getGroupsList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.groupsList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 
  mounted: function() {
    this.getGroupsList();
  },
}
</script>
 
<style >  
.el-input__inner { 
  height: 35px;
} 

</style>
