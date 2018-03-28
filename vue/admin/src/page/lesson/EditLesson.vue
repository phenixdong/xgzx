<template>  
  <div style="background-color: white; overflow:scroll;">    
    <div class="searchHeader" style="height: 50px; background-color: gainsboro;" > 
      <span style="font-weight: bold; color: #606266; 
        line-height: 50px; ">{{addUpdateTitle}}</span>
        <i class="el-icon-close" style="float: right; line-height: 50px; 
          margin-right: 20px;" @click="cancel"></i>
    </div> 

    <el-form :model="lesson" :rules="rules" ref="ruleForm" size="medium">
    <div style="background-color: white; padding: 0 20px; height: 100%;">
      <el-form-item prop="lessonName" >
        <div style="height:25px; text-align: left; margin-top: 10px;">
          <span><span class="red-star">*</span>标题</span>
          <el-input v-model="lesson.lessonName" style="width: 89%; margin-left: 5px;"></el-input>
        </div>
      </el-form-item>
      <div class="grey-line" style="height: 1px;"></div> 

      <div style="height:45px; text-align: left; margin-top: 10px;">
        <el-col :span="8">
          <el-form-item prop="teacherId" >
            <div style="height:45px; text-align: left; ">
              <span><span class="red-star">*</span>讲师</span>
              <el-select v-model="lesson.teacherId" placeholder="请选择"
                size="medium" style="margin-left: 5px; width: 200px;">
                <el-option v-for="item in teacherList.records" :key="item.teacherId"
                  :label="item.teacherName" :value="item.teacherId">
                </el-option>
              </el-select>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item >
            <div style="height:45px; text-align: left;">
              <span>分类</span>
              <el-select v-model="lesson.categoryId" placeholder="请选择"
                size="medium" style="margin-left: 5px; width: 200px;">
                <el-option v-for="item in categoryList.records" :key="item.lessonCategoryId"
                  :label="item.categoryName" :value="item.lessonCategoryId">
                </el-option>
              </el-select>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="chargeType" >
            <div style="height:45px; text-align: left; ">
              <span><span class="red-star">*</span>收费类型</span>
              <el-select v-model="lesson.chargeType" placeholder="请选择"
                size="medium" style="margin-left: 5px; width: 200px;">
                <el-option v-for="item in chargeTypeList" :key="item.chargeType"
                  :label="item.chargeTypeName" :value="item.chargeType">
                </el-option>
              </el-select> 
            </div>
          </el-form-item>
        </el-col>
      </div>
      <div class="grey-line" style="height: 1px; margin-bottom: 0px; "></div>

      <div style="height:55px; text-align: left; margin-top: 10px;">
        <el-col :span="8">
          <el-form-item prop="price" >
            <div style="height:45px; text-align: left; ">
              <span><span class="red-star">*</span>价格</span>
              <el-input v-model.number="lesson.price" style="width: 200px; 
                margin-left: 5px;" >
              </el-input> 
              <span>.00</span>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="orderNo" >
            <div style="height:45px; text-align: left;">
              <span>排序</span>
              <el-input-number v-model="lesson.orderNo" controls-position="right" 
                :min="1" style="width: 200px; margin-left: 5px;"
                size="medium" 
                >
              </el-input-number>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="watchNo" >
            <div style="height:45px; text-align: left;">
              <span>播放次数</span>
              <el-input v-model.number="lesson.watchNo" style="width: 200px; margin-left: 5px;"></el-input> 
            </div>
          </el-form-item>
        </el-col>
      </div>
      <div class="grey-line" style="height: 1px; margin-bottom: 0px; "></div>

      <div style="height:125px; text-align: left; margin-top: 10px; width: 100%; ">
        <el-col :span="24">
          <el-form-item >
            <div style="height:125px; text-align: left; ">
              <span>视频</span>
              <el-button type="info" size="small" 
                plain @click="showVideoList" style="margin-left: 10px;">选择</el-button>
              <div style="display: inline-block; margin-left: 20px; vertical-align: top;" >
                <img v-if="undefined != lesson.video.coverUrl" style="height: 100px; 
                  width: 180px; border: 1px dashed #d9d9d9; margin: auto 0;" 
                  :src='lesson.video.coverUrl' :onerror='altImage'
                > 
              </div>
              <div style="display: inline-block; margin-left: 5px; vertical-align: top;" >
                <div style="height: 20px;  ">
                  <span style="line-height: 20px; ">标题:</span>
                  <span style="margin-left: 5px; ">{{lesson.video.title}}</span>
                </div>
                <div style="height: 20px; margin-top: 5px;">
                    <span >描述:</span>
                    <span style="margin-left: 5px;">{{lesson.video.description}}</span>
                  </div>
              </div>
            </div>
          </el-form-item>
        </el-col>
      </div>
      
      <!--
      <div style="height:230px; text-align: left; margin-top: 10px;">
        <el-col :span="24">
          <el-form-item >
            <div style="height:230px; text-align: left; ">
              <span>目录</span>
              <el-button type="info" size="small" 
                plain @click="showAddContentDialog" style="margin-left: 10px;">新增</el-button>
              <div style="display: inline-block; margin-left: 20px; vertical-align: top;
                border: 1px solid #d9d9d9; width: 70%; height: 220px; padding: 5px; "  >
                <el-table :data="lesson.lessonContentList" stripe style="width: 100%;  " 
                  height="220" >
                  <el-table-column min-width="30%" prop="contentTitle" label="章节名称"  
                    align="center" ></el-table-column>
                  <el-table-column min-width="50%" prop="contentDescription" label="内容"  
                    align="center" ></el-table-column>
                  <el-table-column prop="contentTitle" label="操作" min-width="30%" align="center">
                    <template slot-scope="scope">
                      <a @click="showUpdateContentDialog(scope.row)" 
                        style="color: #409EFF; margin-left: 20px;">修改</a>
                      <a @click="deleteContent(scope.row)" 
                        style="color: #409EFF; margin-left: 20px;">删除</a>
                    </template>
                  </el-table-column>  
                </el-table>
              </div>
            </div>
          </el-form-item>
        </el-col>
      </div>
      <div class="grey-line" style="height: 1px; margin-top: 10px;"></div> 

      <div style="height:680px; text-align: left; margin-top: 0px; width: 100%; ">
        <el-form-item prop="description" >
          <div style="line-height: 665px; margin-top: -10px;
            vertical-align: top; display: inline-block; ">
            <span class="red-star" style="line-height: 320px;" >*</span>描述
          </div>
          
          <div style="width: 375px; height: 665px; display: inline-block; 
            margin-left: 85px; overflow:scroll; " >
            <vue-editor v-model="lesson.description" :editorToolbar="customToolbar"
              useCustomImageHandler @imageAdded="handleImageAdded"
              style="height: 665px; width: 375px;"
            ></vue-editor>
          </div>
          
        </el-form-item>
      </div>
      <div class="grey-line" style="height: 1px; "></div> 
      -->
       
      <div style="height: 40px; width: 100%; text-align: left; float: left; "
        v-show='3 == editType'>
        <el-form-item >
          <div>分享地址<span style="margin-left: 20px; font-size: 18px; ">https://phenixdong.com/xgzxapi/admin/wx/share/?page=lessonDetail&lessonId={{lesson.lessonId}}</span></div>
        </el-form-item>
        <div class="grey-line" style="height: 1px; "></div> 
      </div>

      <div style="height: 490px; width: 100%; text-align: left; float: left; margin-top: 15px; ">
        <el-form-item >
          <div style="display: inline-block; height: 260px; margin-top: 0px;">
            <span style="line-height: 260px; ">内容描述图片</span>
          </div>
          <div style="display: inline-block; vertical-align:top; margin-top: 10px;
            margin-left: 40px;">
            <el-upload class="avatar-uploader" :before-upload="beforeImageUpload"
              :action="uploadImageUrl"
              :show-file-list="false" :on-success="handleUploadImageSuccess"
              >
              <img v-if="lesson.contentImageUrl" :src="downloadImageHeader + lesson.contentImageUrl" class="avatar"
                style="width: 280px; height: 470px;" :onerror='altImage'>
              <i v-else class="el-icon-plus avatar-uploader-icon"
                style="width: 280px; height: 470px; line-height: 470px;"></i>
            </el-upload> 
          </div>
        </el-form-item>
      </div>
      <div class="grey-line" style="height: 1px; margin-top: 10px;"></div>  

      <div v-if="3 != editType" style="margin-top: 20px; vertical-align: middle; 
        text-align: center; margin-bottom: 20px;">
        <el-button type="info" plain @click="cancel" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary" v-if="1 == editType"  @click="addLesson" style="margin-left: 30px;
          width: 100px; height: 36px;">新增</el-button>
        <el-button type="primary" v-if="2 == editType"  @click="updateLesson" style="margin-left: 30px;
           width: 100px; height: 36px;" v-bind:disabled="!updateBtnEnable">修改</el-button>
      </div>
    </div>
    </el-form>

    <el-dialog title="" :visible.sync="selectVideoDialogShow" width="60%" center
      style=" ">
      <div class="grey-line" style="height: 1px; margin-top: -20px; "></div>
      
      <div style="height: 50px; width: 100%; " > 
        <el-col :span="16" style="text-align: left; vertical-align: middle;
          line-height: 50px; ">
          <div style="float: left; line-height: 50px;">
            <span style=" ">名称</span>
            <div style=" display: inline-block; margin-left: 5px; ">
              <el-input v-model="videoQueryParams.video.title" 
                style="width: 100px; " placeholder=""></el-input>
            </div>
          </div>
          <div style="float: left; height: 50px; padding-left: 10px;">
            <span style=" ">创建时间</span>
            <div style=" display: inline-block; height: 50px; margin-left: 5px; 
              ">
              <el-date-picker v-model="videoQueryParams.createTimeRange" type="daterange" 
                align="center" unlink-panels range-separator="至" start-placeholder="开始日期"
                end-placeholder="结束日期" :picker-options="pickerOptions"
                format="yyyy-MM-dd" value-format="yyyy-MM-dd" 
                style="vertical-align: middle; width: 250px; ">
              </el-date-picker>
            </div>
          </div>
        </el-col>

        <el-col :span="8" style="text-align: right; vertical-align: middle;
          line-height: 50px; ">
          <div style="float: right; line-height: 50px;">
            <el-button type="primary" icon="el-icon-search" size="medium" @click="getVideoList">查询</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
          </div>
        </el-col>
      </div>
      <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
        margin-top: 3px;"></div> 

      <el-table :data="videoPage.records" stripe style="width: 100%; margin-top: -10px; " 
        >
        <el-table-column label="视频" min-width="40%" align="left"  >
          <template slot-scope="scope">
            <img style="height: 60px; width: 110px; 
              margin: auto 0;" :src=scope.row.coverUrl  > 
          </template>
        </el-table-column>
        <el-table-column label="视频名称" min-width="20%" header-align="center" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.title}}</span>
            <span>{{scope.row.destription}}</span>
          </template>
        </el-table-column>
        <!--
        <el-table-column prop="categoryName" label="分类" min-width="10%" align="center">
          <template slot-scope="scope">
            <span v-if="undefined != scope.row.categoryName">{{ scope.row.categoryName }}</span>
            <span v-else>未分类</span>
          </template>
        </el-table-column>
        -->
        <el-table-column prop="videoId" label="操作" min-width="40%" align="center">
          <template slot-scope="scope">
            <el-button type="info" plain size="small"
              @click="selectVideo(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="float: right; margin-top: 0px ; width: 25%; margin-right: 20px;  ">
        <el-pagination background layout="total, prev, pager, next" :page-size="videoPage.size"
          :total="videoPage.total" :current-page="videoPage.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
    </el-dialog>

    <el-dialog :title="editContentTitle" :visible.sync="editContentDialogVisible" 
      width="50%" center style=" ">
      <div class="grey-line" style="height: 1px; margin-top: -20px; "></div>

      <div style="height:45px; text-align: left; margin-top: 10px;">
        <span>标题</span>
        <el-input v-model="content.contentTitle" style="width: 89%; margin-left: 5px;"></el-input>
      </div>
      <div class="grey-line" style="height: 1px;"></div> 

      <div style="height:45px; text-align: left; margin-top: 10px;">
        <span>内容</span>
        <el-input v-model="content.contentDescription" style="width: 89%; margin-left: 5px;"></el-input>
      </div>
      <div class="grey-line" style="height: 1px;"></div> 

      <div style="margin-top: 20px; vertical-align: middle;  text-align: center; 
        margin-bottom: 20px;">
        <el-button type="info" plain @click="cancelContentDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary" v-if="1 == editContentDialogType"  @click="addContent" style="margin-left: 30px;
          width: 100px; height: 36px;">新增</el-button>
        <el-button type="primary" v-else  @click="updateContent" style="margin-left: 30px;
          width: 100px; height: 36px;">修改</el-button>
      </div>
    </el-dialog>  
  </div>    
</template>

<script>
// import axios from 'axios'
// import { VueEditor } from 'vue2-editor'

export default {
  name: 'editLesson', 
  components: {
    //VueEditor
  },
  data() {
    return {
      customToolbar: [
        // [{ 'font': [] }],
        [{ 'header': [false, 1, 2, 3, 4, 5, 6, ] }],
        ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
        [{'align': ''}, {'align': 'center'}, {'align': 'right'}, {'align': 'justify'}],
        //['blockquote', 'code-block'],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }, { 'list': 'check' }],
        // [{ 'script': 'sub'}, { 'script': 'super' }],      // superscript/subscript
        [{ 'indent': '-1'}, { 'indent': '+1' }],          // outdent/indent
        [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
        ['image'],
        //['link', 'image', 'video'],
        //['clean'],                                         // remove formatting button
        // [{ 'direction': 'rtl' }],                         // text direction
      ],
      editType: 1, //1新增，2修改，3详情
      editContentTitle: '',
      editContentDialogType: 1,// 1新增，2修改
      editContentDialogVisible: false,
      coverUrl: '',
      altImage: 'this.src="' + require('../../assets/imgs/video_img_default_01.jpg') + '"',
      // 收费类型，0免费，1会员免费，2单独收费
      chargeTypeList: [
        {chargeType: 1, chargeTypeName: '免费'},
        {chargeType: 2, chargeTypeName: '会员免费'},
        //{chargeType: 3, chargeTypeName: '单独收费'},
      ],
      addUpdateTitle: '新增',
      selectIds: [],
      // 图片上传地址
      uploadImageUrl: this.$fetch.getUploadFileUrl() + 'lesson',
      // 图片下载地址域名
      downloadImageHeader: this.$fetch.getDownloadFileUrl() + 'lesson/',
      content: {
        contentTitle: '',
        contentDescription: '',
      },
      updateBtnEnable: true,
      lesson: {
        lessonId: 0,
        price: 0,
        contentImageUrl: '',
        lessonContentList: [],
        lessonName: '',
        description: '',
        imageUrl: '',
        chargeType: 1,
        orderNo: 1,
        watchNo: 0,
        teacherId: 0,
        teacher: {
          teacherId: '',
          teacherName: '',
        },
        categoryId: 0,
        category: {
          lessonCategoryId: 0,
          categoryName: ''
        },
        videoId: 0,
        video: {
          coverUrl: '',
          title: '',
          description: ''
        },
      },
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
      selectVideoDialogShow: false,
      videoQueryParams: {
        createTimeRange: [],
        page: {
          pages: 1,
          size: 5,
          current: 1
        },
        video: {
          title: '',
          status: 'Normal',
        }
      },
      videoPage: {
        total: 0,
        pages: 0,
        size: 5,
        current: 1,
        records: [],
      },
      rules: {
        lessonName: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
        ],
        teacherId: [
          { required: true, message: '请选择讲师', trigger: 'blur' },
        ],
        chargeType: [
          { required: true, message: '请选择收费类型', trigger: 'blur' },
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' },
          { type: 'number', message: '必须为数字', trigger: 'blur'}
        ],
        // 数字类型的验证需要用v-model.number绑定，不是v-model，这是Vue的number类型的修饰符
        watchNo: [
          { type: 'number', message: '必须为数字', trigger: 'blur'}
        ],
      }
    }
  },

  methods: {
    /*
    handleImageAdded: function(file, Editor, cursorLocation) {
      var formData = new FormData();
      formData.append('file', file)

      axios({
        url: this.uploadImageUrl,
        method: 'POST',
        data: formData
      }).then((result) => {
        console.log("upload file resp " + JSON.stringify(result));
        var resp = result.data;
        if (resp.hasOwnProperty('returnCode')) {
          if (resp.returnCode == '1') {
            var url = this.downloadImageHeader + resp.data;
            Editor.insertEmbed(cursorLocation, 'image', url);
          } else {
            this.$message.error(resp.returnMsg);
          }
        }
      }).catch((err) => {
        console.log(err);
      })
    },*/
    updateLesson() {
      console.log("updateLesson param " + JSON.stringify(this.lesson));
      this.$fetch.updateLesson(this.lesson).then((resp) => {
        console.log("updateLesson resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.cancel();
      }, (error) => {
        this.$message.error(error);
        this.cancel();
      });
    },
    cancelContentDialog() {
      this.editContentDialogVisible = false;
    },
    showAddContentDialog() {
      this.content.contentTitle = '';
      this.content.contentDescription = '';
      this.editContentTitle = '新增章节';
      this.editContentDialogType = 1;
      this.editContentDialogVisible = true;
    },
    showUpdateContentDialog(content) {
      console.log("showUpdateContentDialog content " + JSON.stringify(content));
      this.content = content;
      this.editContentTitle = '修改章节';
      this.editContentDialogType = 2;
      this.editContentDialogVisible = true;
    },
    addContent() {
      console.log("addContent " + this.lesson.lessonContentList);
      if (undefined == this.lesson.lessonContentList) {
        this.lesson.lessonContentList = [];
      }
      // JSON.parse(JSON.stringify)用来生成新的obj，否则数据都会指向同一个地址，
      // 导致数组中内容都一样
      var copyContent = JSON.parse(JSON.stringify(this.content))
      this.lesson.lessonContentList.push(copyContent);
      console.log("lessonContentList " + JSON.stringify(this.lesson.lessonContentList));
      this.editContentDialogVisible = false;
    },
    deleteContent(content) {
      console.log("deleteContent contentList before " + JSON.stringify(this.lesson.lessonContentList));
      // title和description都相同，删除
      for (var i = 0; i < this.lesson.lessonContentList.length; i++) {
        var item = this.lesson.lessonContentList[i];
        if (item.contentTitle == content.contentTitle 
          && item.contentDescription == content.contentDescription) {
            console.log("del content " + item.contentTitle + " " + content.contentTitle);
            this.lesson.lessonContentList.splice(i, 1);
            break;
        }
      }
      console.log("deleteContent lessonContentList after " + JSON.stringify(this.lesson.lessonContentList));
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
    updateContent() {
      console.log("updateContent ");
      for (var i = 0; i < this.lesson.lessonContentList.length; i++) {
        var item = this.lesson.lessonContentList[i];
        if (item.contentTitle == this.content.contentTitle 
            && item.contentDescription == this.content.contentDescription) {
          this.lesson.lessonContentList.splice(i, 1, this.content);// 删除一行，并且插入一行content
        }
      }
      this.editContentDialogVisible = false;
    },
    resetQuery() {
      this.videoQueryParams.video.title = '';
      this.videoQueryParams.createTimeRange = [];
      this.getVideoList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.videoQueryParams.page.current = val;
      this.getVideoList();
    },
    selectVideo(video) {
      console.log("selectVideo " + video);
      this.lesson.videoId = video.videoId;
      this.coverUrl = video.coverUrl;
      this.lesson.video.videoId = video.videoId;
      this.lesson.video.coverUrl = video.coverUrl;
      this.lesson.video.title = video.title;
      this.lesson.video.description = video.description;
      console.log("Title " + this.lesson.video.title + " url " + this.lesson.video.coverUrl);
      this.selectVideoDialogShow = false;
    },
    getLessonInfo() {
      this.$fetch.getLessonInfo(this.lesson.lessonId).then((resp) => {
        console.log("getLessonInfo " + JSON.stringify(resp.data));
        if ("1" == resp.returnCode) {
          this.lesson = JSON.parse(JSON.stringify(resp.data));
          this.updateBtnEnable = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    getVideoList() {
      this.$fetch.getVideoList(this.videoQueryParams).then((resp) => {
        if ("1" == resp.returnCode) {
          this.videoPage = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    showVideoList() {
      this.$fetch.getVideoList(this.videoQueryParams).then((resp) => {
        console.log("videoList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.videoPage = JSON.parse(JSON.stringify(resp.data));
          this.selectVideoDialogShow = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    addLesson() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          console.log("addLesson param " + JSON.stringify(this.lesson));
          this.$fetch.addLesson(this.lesson).then((resp) => {
            console.log("addLesson resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$message.success("新增成功");
              this.cancel();
            } else {
              this.$message.error(resp.returnMsg + "");
            }
            this.cancel();
          }, (error) => {
            this.$message.error(error);
            this.cancel();
          });
        } else {
          this.$message.error('请检查输入');
          return false;
        }
      });
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
      this.lesson.contentImageUrl = respObj.data; 
      console.log("imageUrl " + this.lesson.contentImageUrl);
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
    cancel() {
      this.$router.push({path: '/lessonManage'});
    },
    getTeacherList() {
      this.$fetch.getTeacherList(this.queryTeacherParams).then((resp) => {
        console.log("getTeacherList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.teacherList = JSON.parse(JSON.stringify(resp.data));
          if (this.teacherList.records.length > 0) {
            this.lesson.teacherId = this.teacherList.records[0].teacherId;
          }
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
          if (this.categoryList.records.length > 0) {
            this.lesson.categoryId = this.categoryList.records[0].lessonCategoryId;
            console.log("getCategoryList set categoryId " + this.lesson.categoryId);
          }
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
  }, 

  mounted: function() {
    this.editType = this.$route.query.editType;
    this.lesson.lessonId = this.$route.query.lessonId;
    console.log("route params editType " + this.editType);
    console.log("route params lessonId " + this.lesson.lessonId);
    if (1 == this.editType) { 
      this.addUpdateTitle = '新增';
    } else if (2 == this.editType) {
      this.addUpdateTitle = '修改';
      this.updateBtnEnable = false;
      this.getLessonInfo();
    } else {
      this.addUpdateTitle = '详情';
      this.updateBtnEnable = false;
      this.getLessonInfo();
    }

    this.getTeacherList();
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
