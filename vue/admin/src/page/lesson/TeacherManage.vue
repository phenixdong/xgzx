<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="12" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px;">
              <span style=" ">讲师姓名</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.teacher.teacherName" 
                style="width: 130px;" placeholder=""></el-input>
              </div>
            </div>
          </el-col>
          <el-col :span="12" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getTeacherList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
              <el-button type="primary" icon="el-icon-plus" size="medium" @click="showAddDialog">新增</el-button>
              <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteTeacher">删除</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="teacherList.records" stripe style="width: 100%" 
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" min-width="5%" header-align="center" 
          align="center" >
        </el-table-column>
        <el-table-column label="头像" min-width="15%" align="center">
          <template slot-scope="scope">
            <img style="height: 40px; width: 40px;
              margin: auto 0;" :src='downloadImageHeader + scope.row.imageUrl'
              :onerror='altImage' > 
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="姓名" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="position" label="职位" min-width="15%" align="center"></el-table-column>
        <!--
        <el-table-column prop="description" label="描述" min-width="15%" align="center">
          <template slot-scope="scope">
            <span style="" >{{scope.row.description}}</span>
          </template>
        </el-table-column>
        -->
        <el-table-column prop="createTime" label="创建时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="teacherId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="showDetailDialog(scope.row.teacherId)" 
              style="color: #409EFF; ">详情</a>
            <a @click="showUpdateDialog(scope.row.teacherId)" 
              style="color: #409EFF; margin-left: 20px;">修改</a>
            <a @click="deleteTeacher(scope.row.teacherId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
          </template>
        </el-table-column>
      </el-table>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
          :total="teacherList.total" :current-page="queryParams.page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
     
      <el-dialog :title="addUpdateTitle" :visible.sync="addUpdateDialogVisible" width="50%" center>
        <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
          margin-top: -20px;"></div> 
        <el-form :model="teacher" :rules="rules" ref="ruleForm" size="medium">
          <div style="height:45px; text-align: left;">
            <el-col :span="12">
              <el-form-item prop="teacherName" >
                <div style="height:45px; text-align: left;">
                  <span><span class="red-star">*</span>姓名</span>
                  <el-input v-model="teacher.teacherName" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item>
                <div style="height:45px; text-align: left;">
                  <span>职位</span>
                  <el-input v-model="teacher.position" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
                </div>
              </el-form-item>
            </el-col>
          </div> 
          <div class="grey-line" style="height: 1px; margin-bottom: 0px;"></div> 
          <el-form-item>
            <div style="height:50px; text-align: left; width: 100%; margin-top: 10px; ">
              <span style="line-height: 50px; ">描述</span>
              <el-input type="textarea" :rows="2" v-model="teacher.description" 
                style="width: 90%; line-height: 50px;  
                margin-left: 10px; vertical-align: middle;"></el-input>
            </div>
          </el-form-item>
          <div class="grey-line" style="height: 1px; "></div> 
          <el-form-item>
            <div style="height: 140px; width: 100%; text-align: left; margin-top: 10px;">
              <div style="display: inline-block; height: 120px; margin-top: 10px;">
                <span style="line-height: 120px; ">头像</span>
              </div>
              <div style="display: inline-block; vertical-align:top; margin-top: 10px;
                margin-left: 10px;">
                <el-upload class="avatar-uploader" :before-upload="beforeImageUpload"
                  :action="uploadImageUrl"
                  :show-file-list="false" :on-success="handleUploadImageSuccess"
                  >
                  <img v-if="teacher.imageUrl" :src="downloadImageHeader + teacher.imageUrl" class="avatar"
                    style="width: 190px; height: 120px;">
                  <i v-else class="el-icon-plus avatar-uploader-icon"
                    style="width: 190px; height: 120px; line-height: 120px;"></i>
                </el-upload> 
              </div>
            </div>
          </el-form-item>
        </el-form>  
        <div class="grey-line" style="height: 1px; margin-top: 10px;"></div>  
        <div v-if="3 != dialogType" style="margin-top: 20px; vertical-align: middle; text-align: center;">
          <el-button type="info" plain @click="cancelAddUpdateDialog" 
            style="width: 100px; height: 36px;">取消</el-button>
          <el-button type="primary" v-if="1 == dialogType"  @click="addTeacher" style="margin-left: 30px;
            width: 100px; height: 36px;">新增</el-button>
          <el-button type="primary" v-else  @click="updateTeacher" style="margin-left: 30px;
            width: 100px; height: 36px;">修改</el-button>
        </div>
      </el-dialog>
    </div>
  </div>    
</template>

<script>
   
export default {
  name: 'teacherManage', 
  data() {
    return {
      rules: {
        teacherName: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
        ],
      },
      altImage: 'this.src="' + require('../../assets/imgs/user_img_default.jpg') + '"',
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
      selectIds: [],
      // 图片上传地址
      uploadImageUrl: this.$fetch.getUploadFileUrl() + 'teacher',
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl() + 'teacher/',
      teacherList: {
        records: [],
        total: 0,
        pages: 0,
      },
      teacher: {
        teacherName: '',
        position: '',
        description: '',
        imageUrl: '',
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        teacher: {
          teacherName: '',
          imageUrl: '',
        }
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      this.queryParams.teacher.teacherName = '';
      this.getTeacherList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getTeacherList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.selectIds = [];
      for (var i = 0; i < val.length; i++) {
        this.selectIds.push(val[i].teacherId);
      }
    },
    handleUploadImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      var respObj = JSON.parse(JSON.stringify(resp))
      this.teacher.imageUrl = respObj.data; 
      console.log("imageUrl " + this.teacher.imageUrl);
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
    uploadImage() {
      console.log("uploadImage");
    },
    showDetailDialog(teacherId) {
      console.log("showDetailDialog teacherId " + teacherId);
      this.$fetch.getTeacherInfo(teacherId).then((resp) => {
        console.log("getTeacherInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.teacher = JSON.parse(JSON.stringify(resp.data));
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
    showUpdateDialog(teacherId) {
      console.log("showUpdateDialog teacherId " + teacherId);
      this.$fetch.getTeacherInfo(teacherId).then((resp) => {
        console.log("getTeacherInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.teacher = JSON.parse(JSON.stringify(resp.data));
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
    deleteTeacher(teacherId) {
      this.selectIds.push(teacherId);
      this.batchDeleteTeacher();
    },
    batchDeleteTeacher() {
      console.log("batchDeleteTeacher " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteTeacher(params).then((resp) => {
          console.log("deleteTeacher " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getTeacherList();
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
    showAddDialog() {
      this.dialogType = 1;
      this.addUpdateTitle = '新增';
      this.teacher = {
        teacherName: '',
        orderNo: 0,
        imageUrl: '',
      }
      this.addUpdateDialogVisible = true;
    },
    addTeacher() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.$fetch.addTeacher(this.teacher).then((resp) => {
            console.log("addTeacher resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$message.success("新增成功");
              this.getTeacherList();
            } else {
              this.$message.error(resp.returnMsg + "");
            }
            this.addUpdateDialogVisible = false;
          }, (error) => {
            this.$message.error(error);
            this.addUpdateDialogVisible = false;
          });
        } else {
          this.$message.error('请检查输入');
          return false;
        }
      });
    },
    updateTeacher() {
      this.$fetch.updateTeacher(this.teacher).then((resp) => {
        console.log("updateTeacher resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.getTeacherList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    getTeacherList() {
      this.$fetch.getTeacherList(this.queryParams).then((resp) => {
        console.log("getTeacherList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.teacherList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getTeacherList();
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
