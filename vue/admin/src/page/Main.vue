<template>  
  <div style="height: 100%; width: 100%;  ">     
    <div id="main" style="width: 100%; height: 100%; display: flex; flex-direction: row;  " >
      <div id="main-left" style="width: 20%; background-color: dimgray;">  
        <el-menu :default-active="currentIndex" @select="selectMenu" 
          background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
          :router="true" @open="handleOpen" @close="handleClose"
          :unique-opened="true" style="text-align: left; "
          >        
          <el-submenu index="/lessonManage" default-active="/lessonManage">
            <template slot="title">课程管理</template>
            <el-menu-item index="/lessonManage" style="margin-left: 15px;">课程管理</el-menu-item>
            <el-menu-item index="/categoryManage" style="margin-left: 15px;">分类管理</el-menu-item>
            <el-menu-item index="/teacherManage" style="margin-left: 15px;">讲师管理</el-menu-item>
          </el-submenu>  

          <el-submenu index="/videoManage" default-active="/videoManage">
            <template slot="title">视频管理</template>
            <el-menu-item index="/videoManage" style="margin-left: 15px;">视频管理</el-menu-item>
          </el-submenu>  

          <el-submenu index="/userManage" default-active="/userManage">
            <template slot="title">用户管理</template>
            <el-menu-item index="/userManage" style="margin-left: 15px;">用户管理</el-menu-item>
            <el-menu-item index="/groupsManage" style="margin-left: 15px;">群组管理</el-menu-item>
            <el-menu-item index="/groupsMessageManage" style="margin-left: 15px;">群组消息</el-menu-item>
          </el-submenu>  

          <el-submenu index="/bulkBuyManage" default-active="/bulkBuyManage">
            <template slot="title">团购管理</template>
            <el-menu-item index="/bulkBuyManage" style="margin-left: 15px;">团购管理</el-menu-item>
            <el-menu-item index="/bulkBuyGroupsManage" style="margin-left: 15px;">团购群组</el-menu-item>
          </el-submenu> 

          <el-submenu index="/transactionManage" default-active="/transactionManage">
            <template slot="title">账户管理</template>
            <el-menu-item index="/transactionManage" style="margin-left: 15px;">交易记录</el-menu-item>
            <el-menu-item index="/rebateManage" style="margin-left: 15px;">用户返点</el-menu-item>
            <el-menu-item index="/rebateWithdrawManage" style="margin-left: 15px;">提现管理</el-menu-item>
            <el-menu-item index="/accountStatistic" style="margin-left: 15px;">账户总览</el-menu-item>
            <el-menu-item index="/teacherRebate" style="margin-left: 15px;">讲师返点</el-menu-item>
          </el-submenu>   

          <el-submenu index="/commonParamManage" default-active="/commonParamManage">
            <template slot="title">公用配置</template>
            <el-menu-item index="/commonParamManage" style="margin-left: 15px;">公用配置</el-menu-item>
            <el-menu-item index="/memberTypeManage" style="margin-left: 15px;">会员类型</el-menu-item>
          </el-submenu> 

          <el-submenu index="/changePassword" default-active="/changePassword">
            <template slot="title">系统设置</template>
            <el-menu-item index="/changePassword" style="margin-left: 15px;">修改密码</el-menu-item>
          </el-submenu> 

          <el-menu-item index="">
            <span slot="title">退出</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div id="main-right" style="width: 80%; background-color: gainsboro;">
        <router-view></router-view>
      </div>
    </div>
  </div>    
</template>

<script>
export default {
  name: 'main', 
  data() {
    return {
      currentIndex: '',
      memberTypeQueryParams: {
        page: {
          current: 1,
          size: 10,
        },
        memberType: {
          memberTypeName: '',
        },
        createTimeRange: []
      },
      memberTypeList: [],
    }
  },

  mounted: function() {
    // 默认打开的菜单
    var menuIndex = localStorage.getItem("menuIndex");
    if (null == menuIndex || undefined == menuIndex) {
      this.currentIndex = '/lessonManage';
    } else {
      this.currentIndex = menuIndex;
    }
    console.log("main mounted currentIndex " + this.currentIndex);
    // 会员类型列表
    this.getMemberTypeList();
  }, 

  methods: {
    getMemberTypeList() {
      this.$fetch.getMemberTypeList(this.memberTypeQueryParams).then((resp) => {
        console.log("getMemberTypeList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          // 存储到全局内存
          var memberTypeListJson = JSON.stringify(resp.data);
          localStorage.setItem("memberTypeListJson", memberTypeListJson);
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    selectMenu:function(index) {
      console.log("selectMenu index " + index);

      // 为空时，是退出按钮
      if ('' == index) {
        this.$confirm('确定退出吗?').then(() => {
          console.log("logout ");
          this.$fetch.logout().then((resp) => {
            console.log("logout resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$router.push({path: '/'});
            } else {
              this.$message.error(resp.returnMsg + "");
              this.$router.push({path: '/'});
            }
          }, (error) => {
            this.$message.error(error);
            this.$router.push({path: '/'});
          });
        }).catch(() => {
        });
      }
      // 存储当前菜单位置
      this.currentIndex = index;
      localStorage.setItem("menuIndex", index);
    },
    handleOpen(key, keyPath) {
      console.log("handleOpen key " + key + " keyPath " + keyPath); 
      // 打开第一个菜单
      this.$router.push({path: key});
      // 打开的菜单高亮
      this.currentIndex = key;
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
     
  }, 
 
}
</script>
 
<style >
  

  .el-input__inner { 
    height: 50px;
  }
</style>
