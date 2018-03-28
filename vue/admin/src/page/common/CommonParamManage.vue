<template>  
  <div style="background-color: white; overflow:scroll; height: 100%;">    
    <div class="searchHeader" style="height: 50px; background-color: gainsboro;" > 
      <span style="font-weight: bold; color: #606266; 
        line-height: 50px; ">公用配置</span>
    </div> 

    <div style="height:70px; text-align: left; background-color: white; padding: 0 20px;">
      <el-col :span="12" style="padding-top: 5px;">
        <span style="line-height: 60px; ">推荐返点</span>
        <span style=" margin-left: 35px;" >{{commonParam.rebateRate}}
        </span> 
        <span>%</span>
        <el-button type="primary" size="small" icon="el-icon-edit" 
          style="margin-left: 20px; " @click="showUpdateRebateDialog">修改</el-button>
      </el-col>

      <el-col :span="12">
        <div style="height: 60px; width: 100%; text-align: left; padding-top: 5px;">
          <div style="display: inline-block; height: 60px; margin-top: 0px;">
            <span style="line-height: 60px; ">分享图标</span>
          </div>
          <div style="display: inline-block; vertical-align:top; margin-top: 0px;
            margin-left: 35px;">
            <el-upload class="avatar-uploader" :before-upload="beforeShareImageUpload"
              :action="uploadShareImage"
              :show-file-list="false" :on-success="handleUploadShareImageSuccess"
              >
              <img :src="downloadImageHeader + 'shareIcon.jpg'" class="avatar"
                style="width: 60px; height: 60px;" :onerror='altImagePlus'>
            </el-upload> 
          </div>
        </div>
      </el-col>

      <!--
      <el-col :span="12" style="padding-top: 5px;">
        <span style="line-height: 60px; ">讲师返点</span>
        <span style=" margin-left: 35px;" >{{commonParam.teacherRebateRate}}
        </span> 
        <span>%</span>
        <el-button type="primary" size="small" icon="el-icon-edit" 
          style="margin-left: 20px; " @click="showUpdateTeacherRebateDialog">修改</el-button>
      </el-col>
      -->
    </div>
    <div class="grey-line" style="height: 1px; margin-bottom: 0px; "></div>

    <div style="background-color: white; padding: 0 20px; ">
      <el-col :span="12">
        <div style="height: 260px; width: 100%; text-align: left;">
          <div style="display: inline-block; height: 260px; margin-top: 10px;">
            <span style="line-height: 260px; ">宣传图片</span>
          </div>
          <div style="display: inline-block; vertical-align:top; margin-top: 10px;
            margin-left: 35px;">
            <el-upload class="avatar-uploader" :before-upload="beforeImageUpload"
              :action="uploadAdvertiseImage"
              :show-file-list="false" :on-success="handleUploadAdvertiseImageSuccess"
              >
              <img v-if="commonParam.advertiseImageUrl" :src="downloadImageHeader + commonParam.advertiseImageUrl" class="avatar"
                style="width: 180px; height: 260px;" :onerror='altImage'>
              <i v-else class="el-icon-plus avatar-uploader-icon"
                style="width: 180px; height: 260px; line-height: 260px;"></i>
            </el-upload> 
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div style="height: 260px; width: 100%; text-align: left;">
          <div style="display: inline-block; height: 260px; margin-top: 10px;">
            <span style="line-height: 260px; ">联系我们</span>
          </div>
          <div style="display: inline-block; vertical-align:top; margin-top: 10px;
            margin-left: 35px;">
            <el-upload class="avatar-uploader" :before-upload="beforeImageUpload"
              :action="uploadContactUsImage"
              :show-file-list="false" :on-success="handleUploadContactUsImageSuccess"
              >
              <img v-if="commonParam.contactUsImageUrl" 
                :src="downloadImageHeader + commonParam.contactUsImageUrl" class="avatar"
                style="width: 180px; height: 260px;" :onerror='altImage'>
              <i v-else class="el-icon-plus avatar-uploader-icon"
                style="width: 180px; height: 260px; line-height: 260px;"></i>
            </el-upload> 
          </div>
        </div>
      </el-col>
    </div>
    <div class="grey-line" style="height: 1px; margin-bottom: 0px; "></div>
 
    <el-dialog title="修改" :visible.sync="updateRebateDialogVisible"
      width="50%" center >
      <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
        margin-top: -20px;"></div> 
      <el-form :model="updateCommonParam" :rules="rules" ref="ruleForm" size="medium">
        <div style="height:45px; text-align: center;">
          <el-col :span="24"> 
            <el-form-item prop="rebateRate">
              <div >
                <span>推荐返点</span>
                <el-input v-model.number="updateCommonParam.rebateRate" style="width: 60px; 
                  height: 30px; margin-left: 5px;"></el-input>
                <span>%</span>
              </div>
            </el-form-item>
          </el-col>
        </div>
      </el-form>
      <div class="grey-line" style="height: 1px; margin-bottom: 5px; "></div> 
        
      <div style="margin-top: 20px; vertical-align: middle; text-align: center;">
        <el-button type="info" plain @click="cancelRebateUpdateDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary"  @click="updateRebateRate()" style="margin-left: 30px;
          width: 100px; height: 36px;">修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改" :visible.sync="updateTeacherRebateDialogVisible"
      width="50%" center >
      <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
        margin-top: -20px;"></div> 
      <el-form :model="updateCommonParam" :rules="rules" ref="teacherRuleForm" size="medium">
        <div style="height:45px; text-align: center;">
          <el-col :span="24"> 
            <el-form-item prop="teacherRebateRate">
              <div >
                <span>讲师返点</span>
                <el-input v-model.number="updateCommonParam.teacherRebateRate" style="width: 60px; 
                  height: 30px; margin-left: 5px;"></el-input>
                <span>%</span>
              </div>
            </el-form-item>
          </el-col>
        </div>
      </el-form>
      <div class="grey-line" style="height: 1px; margin-bottom: 5px; "></div> 
        
      <div style="margin-top: 20px; vertical-align: middle; text-align: center;">
        <el-button type="info" plain @click="cancelTeacherRebateUpdateDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary"  @click="updateTeacherRebateRate()" style="margin-left: 30px;
          width: 100px; height: 36px;">修改</el-button>
      </div>
    </el-dialog>

  </div>    
</template>

<script>
   
export default {
  name: 'commonParamManage', 
  data() {
    return {
      rules: {
        rebateRate: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { type: 'number', message: '必须为数字', trigger: 'blur'}
        ],
        teacherRebateRate: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { type: 'number', message: '必须为数字', trigger: 'blur'}
        ],
      },
      updateRebateDialogVisible: false,
      updateTeacherRebateDialogVisible: false,
      commonParam: {
        rebateRate: 0,
        teacherRebateRate: 0,
        advertiseImageUrl: '',
        contactUsImageUrl: '',
      },
      updateCommonParam: {
        rebateRate: 0,
        teacherRebateRate: 0,
        advertiseImageUrl: '',
        contactUsImageUrl: '',
      },
      altImage: 'this.src="' + require('../../assets/imgs/plus_large.png') + '"',
      altImagePlus: 'this.src="' + require('../../assets/imgs/plus.png') + '"',
      // 图片上传地址
      uploadAdvertiseImage: this.$fetch.uploadAdvertiseFile(),
      uploadContactUsImage: this.$fetch.uploadContactUsFile(),
      uploadShareImage: this.$fetch.uploadShareIconFile(),
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl() + 'common/',

    }
  },

  methods: {
    showUpdateRebateDialog() {
      this.updateCommonParam.rebateRate = this.commonParam.rebateRate;
      this.updateRebateDialogVisible = true;
    },
    showUpdateTeacherRebateDialog() {
      this.updateCommonParam.teacherRebateRate = this.commonParam.teacherRebateRate;
      this.updateTeacherRebateDialogVisible = true;
    },
    cancelRebateUpdateDialog() {
      this.updateRebateDialogVisible = false;
    },
    cancelTeacherRebateUpdateDialog() {
      this.updateTeacherRebateDialogVisible = false;
    },
    updateRebateRate() {
      if (this.updateCommonParam.rebateRate >= 100) {
        this.$message.error("不能超过100%");
        return;
      }
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          console.log("updateRebateRate param " + JSON.stringify(this.updateCommonParam));
          this.$fetch.updateRebateRate(this.updateCommonParam).then((resp) => {
            console.log("updateRebateRate resp " + JSON.stringify(resp.data));
            if ("1" == resp.returnCode) {
              this.commonParam.rebateRate = resp.data;
              this.updateRebateDialogVisible = false;
            } else {
              this.$message.error(resp.returnMsg + "");
            }
          }, (error) => {
            this.$message.error(error);
          });
        } else {
          this.$message.error('请检查输入');
          return false;
        }
      });
    },
    updateTeacherRebateRate() {
      if (this.updateCommonParam.teacherRebateRate >= 100) {
        this.$message.error("不能超过100%");
        return;
      }
      this.$refs['teacherRuleForm'].validate((valid) => {
        if (valid) {
          console.log("updateTeacherRebateRate param " + JSON.stringify(this.updateCommonParam));
          this.$fetch.updateTeacherRebateRate(this.updateCommonParam).then((resp) => {
            console.log("updateTeacherRebateRate resp " + JSON.stringify(resp.data));
            if ("1" == resp.returnCode) {
              this.commonParam.teacherRebateRate = resp.data;
              this.updateTeacherRebateDialogVisible = false;
            } else {
              this.$message.error(resp.returnMsg + "");
            }
          }, (error) => {
            this.$message.error(error);
          });
        } else {
          this.$message.error('请检查输入');
          return false;
        }
      });
    },
    getCommonParam() {
      this.$fetch.getCommonParam({}).then((resp) => {
        console.log("getCommonParam " + JSON.stringify(resp.data));
        if ("1" == resp.returnCode) {
          this.commonParam = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    handleUploadAdvertiseImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      var respObj = JSON.parse(JSON.stringify(resp))
      this.commonParam.advertiseImageUrl = respObj.data; 
      console.log("imageUrl " + this.commonParam.advertiseImageUrl);
    },
    handleUploadContactUsImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      var respObj = JSON.parse(JSON.stringify(resp))
      this.commonParam.contactUsImageUrl = respObj.data; 
      console.log("imageUrl " + this.commonParam.contactUsImageUrl);
    },
    handleUploadShareImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      window.location.reload();
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是JPG/PNG格式');
      } 
      if (!isLt10M) {
        this.$message.error('上传图片大小不能超过10M');
      }
      return isJPG || isPNG || isLt10M;
    },
    beforeShareImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isJPG) {
        this.$message.error('上传图片只能是JPG格式');
      } 
      if (!isLt10M) {
        this.$message.error('上传图片大小不能超过10M');
      }
      return isJPG || isPNG || isLt10M;
    },
     
     
  }, 
 
  mounted: function() {
    this.getCommonParam();
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
 
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    text-align: center;
  }
  .avatar {
    display: block;
  }

</style>
