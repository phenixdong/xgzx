<template>  
  <div style="width: 100%; height: 100%; ">
    <div class="searchHeader" style="height: 50px;" > 
    </div>

    <div style="width: 100%; background-color: white; text-align: center; 
      padding-top: 20px;">
      <el-form 
        style="background-color: white; width: 40%; margin: 0 auto; "
        :model="adminUser" :rules="rules" ref="ruleForm" size="medium">
          <el-form-item prop="oldPassword" style=" " >
            <span>&nbsp;&nbsp;<span class="red-star">*</span>旧密码</span>
            <el-input v-model="adminUser.oldPassword" type="password"
              style=" margin-left: 0px; width: 80%;"></el-input>
          </el-form-item>
          <el-form-item prop="newPassword" >
            <span>&nbsp;&nbsp;<span class="red-star">*</span>新密码</span>
            <el-input v-model="adminUser.newPassword" type="password"
              style=" margin-left: 0px; width: 80%;"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword" >
            <span><span class="red-star">*</span>确认密码</span>
            <el-input v-model="adminUser.confirmPassword" type="password" 
              style=" margin-left: 0px; width: 80%;"></el-input>
          </el-form-item>
      </el-form>
      <div class="grey-line" style="height: 1px; margin-top: 0px;"></div> 
      <el-button type="primary" @click="changePassword" 
        style=" width: 100px; height: 36px; margin: 10px auto;" 
        >修改</el-button>
    </div>
  </div>    
</template>

<script>
export default {
  name: 'changePassword', 

  mounted: function() {
  },

  data() {
    return {
      rules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请输入确认密码', trigger: 'blur' },
        ],
      },
      adminUser: {
        loginId: '',
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
    }
  },

  methods: {
     
    changePassword: function() {
      if (this.adminUser.newPassword != this.adminUser.confirmPassword) {
        this.$message.error("确认密码不一致");
        return;
      }
      console.log("changePassword param " + JSON.stringify(this.adminUser));
      this.$fetch.changePassword(this.adminUser).then((resp) => {
        console.log("changePassword resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    }
  }, 
 
}
</script>
 
<style >
  .el-input__inner { 
    height: 40px;
  }
</style>
