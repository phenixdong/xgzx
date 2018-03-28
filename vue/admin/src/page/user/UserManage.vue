<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="16" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px; ">
              <span style=" ">用户名称</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.user.userName" style="" placeholder=""
                  style="width: 130px; "></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 15px;">
              <span style=" ">创建时间</span>
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
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getUserList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
              <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteUser">删除</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="userList.records" stripe style="width: 100%" 
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" min-width="5%" header-align="center" 
          align="center" >
        </el-table-column>
        <el-table-column label="头像" min-width="15%" align="center">
          <template slot-scope="scope">
            <img style="height: 40px; width: 40px; 
              margin: auto 0;" :src='scope.row.imageUrl' v-if="scope.row.imageUrl"
              :onerror='altImage' > 
            <img style="height: 40px; width: 40px; margin: auto 0;" 
              :src='altImage'  v-else
              > 
          </template>
        </el-table-column>
        <el-table-column prop="nickName" label="名称" min-width="20%" align="center"></el-table-column>
        <el-table-column prop="memberTypeId" label="会员类型" min-width="10%" align="center">
          <template slot-scope="scope" >
            <span v-if="scope.row.memberTypeId != undefined">
              {{ scope.row.memberTypeId | translate(memberTypeList, 
                'memberTypeId', 'memberTypeName')}}
            </span>
            <span v-else>
              非会员
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="province" label="省份" min-width="5%" align="center"></el-table-column>
        <!--
        <el-table-column prop="userState" label="状态" min-width="10%" align="center">
          <template slot-scope="scope">
            {{ scope.row.userState | translate(static.stateTypes, 'value', 'label') }}
          </template>
        </el-table-column>
        -->
        <el-table-column prop="createTime" label="创建时间" min-width="20%" align="center"></el-table-column>
        <el-table-column prop="userId" label="操作" min-width="15%" align="center">
          <template slot-scope="scope">
            <a @click="showDetailDialog(scope.row.userId)" 
              style="color: #409EFF; ">详情</a>
              <!--
            <a @click="lockUser(scope.row.userId, 1)" v-if="0 == scope.row.userState"
              style="color: #409EFF; margin-left: 20px;">解锁</a>
            <a @click="lockUser(scope.row.userId, 0)" v-else
              style="color: #409EFF; margin-left: 20px;">锁定</a>
            <a @click="deleteUser(scope.row.userId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
            -->
          </template>
        </el-table-column>
      </el-table>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
          :total="userList.total" :current-page="queryParams.page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
     
      <el-dialog :title="addUpdateTitle" :visible.sync="addUpdateDialogVisible" width="50%" center>
        <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
          margin-top: -20px;"></div> 
        <div style="width: 100%; height: 120px; ">
          <el-col :span="6" style="text-align: center;">
            <img :src="user.imageUrl" :onerror='altImage' v-if="user.imageUrl"
              style="width: 100px; height: 100px; border: 1px solid #d9d9d9;
              padding: 1px; ">
            <img :src="altImage" v-else 
              style="width: 100px; height: 100px; border: 1px solid #d9d9d9;">
          </el-col>

          <el-col :span="18">
            <div style="width: 100%; display: inline-block; ">
              <div style="height:30px; text-align: left; width: 40%; display: inline-block; ">
                <span>名称:</span>
                <span style="height: 30px; margin-left: 5px;" >
                  {{user.nickName}}
                </span>
              </div>
              <div style="height:30px; text-align: left; width: 40%; display: inline-block; ">
                <span>性别:</span>
                <span style="height: 30px; margin-left: 5px;" >
                  {{ user.sex | translate(static.sexTypes, 'value', 'label') }}
                </span>
              </div>
            </div>
            <div style="width: 100%; display: inline-block; ">
              <div style="height:30px; text-align: left; width: 40%; display: inline-block; ">
                <span>省份:</span>
                <span style="height: 30px; margin-left: 5px;" >
                  {{user.province}}
                </span>
              </div>
              <div style="height:30px; text-align: left; width: 40%; display: inline-block; ">
                <span>城市:</span>
                <span style="height: 30px; margin-left: 5px;" >
                    {{user.city}}
                </span>
              </div>
            </div>
            <div style="width: 100%; display: inline-block; ">
              <div style="height:30px; text-align: left; width: 90%; display: inline-block; ">
                <span>微信openId:</span>
                <span style="height: 30px; margin-left: 5px;" >
                  {{user.openId}}
                </span>
              </div>
            </div>
          </el-col>
        </div>
        <div class="grey-line" style="height: 1px; margin-top: 10px;"></div> 
      </el-dialog>
    </div>
  </div>    
</template>

<script>
   
export default {
  name: 'userManage', 
  data() {
    return {
      memberTypeList: [],
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
      static:{
        stateTypes:[
          {label:'锁定',value: 0},
          {label:'正常',value: 1},
        ],
        sexTypes:[// 1男，2女，0未知
          {label:'未知',value: 0},
          {label:'男',value: 1},
          {label:'女',value: 2},
        ],
      },
      altImage: require('../../assets/imgs/user_img_default.jpg'),
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
      selectIds: [],
      userList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        user: {
          userName: '',
          nickName: '',
          imageUrl: '',
        },
        createTimeRange: []
      },
      user: {
        userName: '',
        nickName: '',
        imageUrl: '',
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.user.userName = '';
      this.queryParams.createTimeRange = [];
      this.getUserList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getUserList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.selectIds = [];
      for (var i = 0; i < val.length; i++) {
        this.selectIds.push(val[i].userId);
      }
    }, 
    showDetailDialog(userId) {
      console.log("showDetailDialog userId " + userId);
      this.$fetch.getUserInfo(userId).then((resp) => {
        console.log("getUserInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.user = JSON.parse(JSON.stringify(resp.data));
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
    showUpdateDialog(userId) {
      console.log("showUpdateDialog userId " + userId);
      this.$fetch.getUserInfo(userId).then((resp) => {
        console.log("getUserInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.user = JSON.parse(JSON.stringify(resp.data));
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
    lockUser(userId, state) {
      var params = {userId: userId + '', userState: state};
      this.$fetch.lockUser(params).then((resp) => {
        console.log("lockUser " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.$message.success("成功");
          this.getUserList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    deleteUser(userId) {
      this.selectIds.push(userId);
      this.batchDeleteUser();
    },
    batchDeleteUser() {
      console.log("batchDeleteUser " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteUser(params).then((resp) => {
          console.log("deleteUser " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getUserList();
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
     
    addUser() {
      this.$fetch.addUser(this.user).then((resp) => {
        console.log("addUser resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("新增成功");
          this.getUserList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    updateUser() {
      this.$fetch.updateUser(this.user).then((resp) => {
        console.log("updateUser resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.getUserList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    getUserList() {
      this.$fetch.getUserList(this.queryParams).then((resp) => {
        console.log("getUserList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.userList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  mounted: function() {
    var memberTypeListJson = localStorage.getItem("memberTypeListJson");
    this.memberTypeList = JSON.parse(memberTypeListJson);
    this.getUserList();
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
