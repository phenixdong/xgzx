<template>  
  <div>     
    <div>
      <div class="searchHeader" style="height: 50px;" > 
        <el-col :span="24" style="text-align: right; vertical-align: middle;
          line-height: 50px; "
          >
          <div style="float: right; line-height: 50px;">
            <el-button type="primary" icon="el-icon-search" size="medium" @click="getCategoryList">查询</el-button>
            <el-button type="primary" icon="el-icon-plus" size="medium" @click="showAddDialog">新增</el-button>
            <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteCategory">删除</el-button>
          </div>
        </el-col>
      </div>
       
      <el-table :data="categoryList.records" stripe style="width: 100%" 
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" min-width="5%" header-align="center" 
          align="center" >
        </el-table-column>
        <el-table-column label="分类图片" min-width="15%" align="center">
          <template slot-scope="scope">
            <img style="height: 50px; width: 90px;
              margin: auto 0;" :src='downloadImageHeader + scope.row.imageUrl' > 
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类名称" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="25%" align="center"></el-table-column>
        <el-table-column prop="orderNo" label="排序" min-width="5%" align="center"></el-table-column>
        <el-table-column prop="lessonCategoryId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="showUpdateDialog(scope.row.lessonCategoryId)" 
              style="color: #409EFF">修改</a>
            <a @click="deleteCategory(scope.row.lessonCategoryId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
          </template>
        </el-table-column>
      </el-table>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
          :total="categoryList.total" :current-page="queryParams.page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
     
      <el-dialog :title="addUpdateTitle" :visible.sync="addUpdateDialogVisible"
        width="50%" center>
        <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
          margin-top: -20px;"></div> 
        <el-form :model="category" :rules="rules" ref="ruleForm" size="medium">
          <div style="height:120px; text-align: left;">
            <el-col :span="12" style="height: 45px; "> 
              <el-form-item prop="categoryName" >
                <div >
                  <span><span class="red-star">*</span>分类名称</span>
                  <el-input v-model="category.categoryName" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
                </div>
              </el-form-item>
              <div class="grey-line" style="height: 1px; margin-top: -5px; "></div>
            </el-col>
            <el-col :span="12" style="height: 45px; "> 
              <el-form-item >
                <div >
                  <span>显示顺序</span>
                  <el-input-number v-model="category.orderNo" controls-position="right" 
                    :min="1" style="width: 40%; margin-left: 5px;"
                    size="medium" 
                    >
                  </el-input-number>
                </div>
              </el-form-item>
              <div class="grey-line" style="height: 1px; margin-top: -5px; "></div>
            </el-col>

            <el-col :span="24" style="height: 45px; margin-top: 25px;"> 
              <el-form-item prop="description" >
                <div style="width: 100%;" >
                  <span><span class="red-star">*</span>分类描述</span>
                  <el-input v-model="category.description" style="width: 80%; height: 30px; margin-left: 5px;"></el-input>
                </div>
              </el-form-item>
            </el-col>
          </div>
          <div class="grey-line" style="height: 1px; margin-bottom: 5px; "></div> 

          <div style="height: 140px; width: 100%; text-align: left;">
            <div style="display: inline-block; height: 120px; margin-top: 10px;">
              <span style="line-height: 120px; ">&nbsp;分类图片</span>
            </div>
            <div style="display: inline-block; vertical-align:top; margin-top: 10px;
              margin-left: 5px;">
              <el-upload class="avatar-uploader" :before-upload="beforeImageUpload"
                :action="uploadImageUrl"
                :show-file-list="false" :on-success="handleUploadImageSuccess"
                >
                <img v-if="category.imageUrl" :src="downloadImageHeader + category.imageUrl" class="avatar"
                  style="width: 190px; height: 120px;" :onerror='altImage'>
                <i v-else class="el-icon-plus avatar-uploader-icon"
                  style="width: 190px; height: 120px; line-height: 120px;"></i>
              </el-upload> 
            </div>
          </div>
        </el-form>
        <div class="grey-line" style="height: 1px; margin-top: 15px;"></div>  
        <div style="margin-top: 20px; vertical-align: middle; text-align: center;">
          <el-button type="info" plain @click="cancelAddUpdateDialog" 
            style="width: 100px; height: 36px;">取消</el-button>
          <el-button type="primary" v-if="1 == dialogType"  @click="addCategory" style="margin-left: 30px;
            width: 100px; height: 36px;">新增</el-button>
          <el-button type="primary" v-else  @click="updateCategory" style="margin-left: 30px;
            width: 100px; height: 36px;">修改</el-button>
        </div>
      </el-dialog>
    </div>
  </div>    
</template>

<script>
   
export default {
  name: 'categoryManage', 
  data() {
    return {
      rules: {
        categoryName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' },
        ],
      },
      altImage: 'this.src="' + require('../../assets/imgs/video_img_default_01.jpg') + '"',
      dialogType: 1, //1新增，2修改
      addUpdateTitle: '新增',
      selectIds: [],
      // 图片上传地址
      uploadImageUrl: this.$fetch.getUploadFileUrl() + 'category',
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl() + 'category/',
      categoryList: {
        records: [],
        total: 0,
        pages: 0,
      },
      category: {
        categoryName: '',
        orderNo: 0,
        imageUrl: '',
        description: ''
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        category: {
          imageUrl: '',
        }
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getCategoryList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.selectIds = [];
      for (var i = 0; i < val.length; i++) {
        this.selectIds.push(val[i].lessonCategoryId);
      }
    },
    handleUploadImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      var respObj = JSON.parse(JSON.stringify(resp))
      this.category.imageUrl = respObj.data; 
      console.log("imageUrl " + this.category.imageUrl);
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!');
      } 
      return isJPG || isPNG;
    },
    uploadImage() {
      console.log("uploadImage");
    },
    showUpdateDialog(lessonCategoryId) {
      console.log("showUpdateDialog lessonCategoryId " + lessonCategoryId);
      this.$fetch.getCategoryInfo(lessonCategoryId).then((resp) => {
        console.log("getCategoryInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.category = JSON.parse(JSON.stringify(resp.data));
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
    deleteCategory(lessonCategoryId) {
      this.selectIds.push(lessonCategoryId);
      this.batchDeleteCategory();
    },
    batchDeleteCategory() {
      console.log("batchDeleteCategory " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteCategory(params).then((resp) => {
          console.log("deleteCategory " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getCategoryList();
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
      this.category = {
        categoryName: '',
        orderNo: 0,
        imageUrl: '',
        description: ''
      }
      this.addUpdateTitle = '新增';
      this.dialogType = 1; 
      this.addUpdateDialogVisible = true;
    },
    addCategory() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          console.log("addCategory param " + JSON.stringify(this.category));
          this.$fetch.addCategory(this.category).then((resp) => {
            console.log("addCategory resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$message.success("新增成功");
              this.getCategoryList();
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
    updateCategory() {
      console.log("updateCategory param " + JSON.stringify(this.category));
      this.$fetch.updateCategory(this.category).then((resp) => {
        console.log("updateCategory resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.getCategoryList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    getCategoryList() {
      this.$fetch.getCategoryList(this.queryParams).then((resp) => {
        console.log("getCategoryList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.categoryList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  created: function() {
    this.getCategoryList();
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
