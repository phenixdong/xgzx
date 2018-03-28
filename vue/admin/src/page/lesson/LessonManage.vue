<template>  
  <div>    
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="14" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px; ">
              <span style=" ">名称</span>
              <div style=" display: inline-block; margin-left: 0px; ">
                <el-input v-model="queryParams.lesson.lessonName" 
                  style="width: 160px; " placeholder=""></el-input>
              </div>
            </div>
            <div style="float: left; line-height: 50px; margin-left: 20px; ">
              <span style=" ">分类</span>
              <div style=" display: inline-block; margin-left: 0px; ">
                <el-select v-model="queryParams.lesson.categoryId" 
                  clearable placeholder="" style="width: 160px;">
                  <el-option v-for="item in categoryList.records" :key="item.lessonCategoryId"
                    :label="item.categoryName" :value="item.lessonCategoryId">
                  </el-option>
                </el-select>
              </div>
            </div>
            <!--
            <div style="float: left; height: 50px; padding-left: 0px;">
              <span style=" ">创建时间</span>
              <div style=" display: inline-block; height: 50px; margin-left: 0px; ">
                <el-date-picker v-model="queryParams.createTimeRange" type="daterange" 
                  align="center" unlink-panels range-separator="至" start-placeholder="开始日期"
                  end-placeholder="结束日期" :picker-options="pickerOptions"
                  format="yyyy-MM-dd" value-format="yyyy-MM-dd" 
                  style="vertical-align: middle; width: 280px; ">
                </el-date-picker>
              </div>
            </div>
          -->
          </el-col>
          <el-col :span="10" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getLessonList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
              <el-button type="primary" icon="el-icon-plus" size="medium" @click="gotoAddPage">新增</el-button>
              <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteLesson">删除</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="lessonList.records" stripe style="width: 100%" 
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" min-width="5%" header-align="center" 
          align="center" >
        </el-table-column>
        <el-table-column label="课程视频" min-width="15%" align="center">
          <template slot-scope="scope">
            <img style="height: 50px; width: 90px;
              margin: auto 0;" :src='scope.row.coverUrl'
              :onerror='altImage' > 
          </template>
        </el-table-column>
        <el-table-column prop="lessonName" label="课程名称" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="teacherName" label="讲师" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="categoryName" label="分类" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="price" label="价格" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="orderNo" label="排序" min-width="15%" align="center" ></el-table-column>
        <el-table-column prop="chargeType" label="收费类型" min-width="15%" align="center">
          <template slot-scope="scope">
            {{ scope.row.chargeType | translate(static.chargeTypeList, 
              'chargeType', 'chargeTypeName') }}
          </template>
        </el-table-column>
        <el-table-column prop="lessonId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="gotoDetailPage(scope.row.lessonId)" 
              style="color: #409EFF; ">详情</a>
            <a @click="gotoUpdatePage(scope.row.lessonId)" 
              style="color: #409EFF; margin-left: 20px;">修改</a>
            <a @click="deleteLesson(scope.row.lessonId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
          </template>
        </el-table-column>
      </el-table>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
          :total="lessonList.total" :current-page="queryParams.page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
     
    </div>
  </div>    
</template>

<script>
   

export default {
  name: 'lessonManage', 
  data() {
    return {
      static: {
        chargeTypeList: [
          {chargeType: 1, chargeTypeName: '免费'},
          {chargeType: 2, chargeTypeName: '会员免费'},
          {chargeType: 3, chargeTypeName: '单独收费'},
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
      selectTeacherId: 0,
      teacherList: {
        records: [],
        total: 0,
        pages: 0,
      },
      categoryList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryTeacherParams: {
        page: {
          current: 1,
          size: 100,
        },
        teacher: {
          teacherName: '',
          imageUrl: '',
        }
      },
      queryCategoryParams: {
        page: {
          current: 1,
          size: 100,
        },
        category: {
          imageUrl: '',
        }
      },
      altImage: 'this.src="' + require('../../assets/imgs/video_img_default_01.jpg') + '"',
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
      selectIds: [],
      lessonList: {
        records: [],
        total: 0,
        pages: 0,
      },
      lesson: {
        lessonName: '',
        description: '',
        coverUrl: '',
        teacherId: 0,
        teacher: {
          teacherName: '',
        },
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        lesson: {
          lessonName: '',
          coverUrl: '',
          categoryId: ''
        },
        createTimeRange: []
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    resetQuery() {
      console.log("resetQuery ");
      this.queryParams.lesson.lessonName = '';
      this.queryParams.lesson.categoryId = '';
      this.queryParams.createTimeRange = [];
      sessionStorage.setItem("lessonQueryParams", '');
      this.getLessonList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getLessonList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.selectIds = [];
      for (var i = 0; i < val.length; i++) {
        this.selectIds.push(val[i].lessonId);
      }
    },
    handleUploadImageSuccess(resp, file) {
      console.log("resp " + JSON.stringify(resp));
      var respObj = JSON.parse(JSON.stringify(resp))
      this.lesson.coverUrl = respObj.data; 
      console.log("coverUrl " + this.lesson.coverUrl);
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
    deleteLesson(lessonId) {
      this.selectIds.push(lessonId);
      this.batchDeleteLesson();
    },
    batchDeleteLesson() {
      console.log("batchDeleteLesson " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteLesson(params).then((resp) => {
          console.log("deleteLesson " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getLessonList();
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
      this.lesson = {
        lessonName: '',
        orderNo: 0,
        coverUrl: '',
      }
      this.addUpdateDialogVisible = true;
    },
    gotoAddPage() {
      console.log("gotoAddPage");
      sessionStorage.setItem("lessonQueryParams", JSON.stringify(this.queryParams));
      this.$router.push({path: '/editLesson', query: { editType: 1}});
    },
    gotoUpdatePage(lessonId) {
      sessionStorage.setItem("lessonQueryParams", JSON.stringify(this.queryParams));
      this.$router.push({path: '/editLesson', query: { editType: 2, lessonId: lessonId }});
    },
    gotoDetailPage(lessonId) {
      sessionStorage.setItem("lessonQueryParams", JSON.stringify(this.queryParams));
      this.$router.push({path: '/editLesson', query: { editType: 3, lessonId: lessonId }});
    },

    getLessonList() {
      console.log("getLessonList param " + JSON.stringify(this.queryParams));
      this.$fetch.getLessonList(this.queryParams).then((resp) => {
        console.log("getLessonList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.lessonList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    getCategoryList() {
      this.$fetch.getCategoryList(this.queryCategoryParams).then((resp) => {
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
    getTeacherList() {
      this.$fetch.getTeacherList(this.queryTeacherParams).then((resp) => {
        console.log("getTeacherList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.teacherList = JSON.parse(JSON.stringify(resp.data));
          if (this.teacherList.records.length > 0) {
            this.selectTeacherId = this.teacherList.records[0].teacherId;
          }
          console.log("selectTeacherId " + this.selectTeacherId);
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  beforeRouteEnter: (to, from, next) => {
    console.log("beforeRouteEnter from " + from.name);
    if (from.name == 'EditLesson') { 

    } else {
      // 不从详情页跳转，清空查询参数
      sessionStorage.setItem("lessonQueryParams", '');
    }
    next();
  },

  mounted: function() {
    // 从详情页返回时，查询参数要保持，该段代码不能放在beforeRouteEnter里面，因为界面还没加载完，
    // 会被加载完的界面清空查询参数
    var lessonQueryParamsJson = sessionStorage.getItem("lessonQueryParams");
    if (null != lessonQueryParamsJson && '' != lessonQueryParamsJson) {
      console.log("get saved queryParams " + lessonQueryParamsJson);
      var that = this;
      setTimeout(function () {
        that.queryParams = JSON.parse(lessonQueryParamsJson);
        that.handleCurrentChange(that.queryParams.page.current);
        console.log("set queryParams " + JSON.stringify(that.queryParams));
      }, 500);
    } else {
      console.log("set queryParams init ");
      this.queryParams = {
        page: {
          current: 1,
          size: 5,
        },
        lesson: {
          lessonName: '',
          coverUrl: '',
          categoryId: ''
        },
        createTimeRange: []
      }
    }
    this.getLessonList();
    this.getCategoryList();
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
