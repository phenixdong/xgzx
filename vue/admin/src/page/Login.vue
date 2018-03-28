<template>  
  <div :style="{backgroundImage: 'url(' + backgroundImg + ')'}"
    style="height: 100%; width: 100%; background-size:cover;
    "
    >     
    <div style="width: 18%; margin: auto; height: 100%; ">
      <el-input style="margin-top: 50%; font-size: 20px; height: 40px;"  
        v-model="user.loginId" placeholder="用户名" ></el-input>
      <el-input style="margin-top: 10px; font-size: 20px; height: 40px;" 
        type="password" v-model="user.password"  placeholder="密码" 
        ></el-input>
      <div style="height: 40px; ">
        <el-col :span="16" >
          <el-input style="margin-top: 10px; font-size: 20px;" type="password"  
            v-model="user.checkCode"  placeholder="验证码" 
            @keyup.enter.native="doLogin"></el-input>
        </el-col>  
        <el-col :span="7" :offset="1" style=" height: 40px; ">
          <img :src="checkCodeUrl" @click="getCheckCode" 
            style="height:40px; width:68px; border-radius: 5px;  margin-top: 10px;  ">
        </el-col>
      </div>
      <el-button style="margin-top: 10px; width: 100%; height: 40px; 
        font-size: 23px; " type="primary" @click="login()" 
        >登录</el-button>
    </div>
  </div>    
</template>

<script>
export default {
  name: 'login', 

  mounted: function() {
    this.getCheckCode();
  },

  data() {
    return {
      backgroundImg: require('../assets/imgs/login_back_02.jpg'),
      user: {
        userName: '',
        loginId: 'admin',
        password: 'admin',
        checkCode: ''
      },
      checkCodeUrl: '',

    }
  },

  methods: {
    doLogin: function() { 
      console.log("doLogin ");
      this.login();
    },
    getCheckCode: function() {
      this.checkCodeUrl = this.$fetch.getCheckCode();
    },
    login: function() {
      this.$fetch.login(this.user).then((resp) => {
        console.log("login resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.user = JSON.parse(JSON.stringify(resp.data));
          this.$router.push({path: '/lessonManage'}); 
          localStorage.setItem("menuIndex", "/lessonManage");
        } else {
          this.$message.error(resp.returnMsg + "");
          this.getCheckCode();
        }
      }, (error) => {
        this.$message.error(error);
        this.getCheckCode();
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
