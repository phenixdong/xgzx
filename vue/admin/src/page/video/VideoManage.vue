<template>  
  <div>     
    <div >
      <el-form ref="form" label-width="90px" :model="form">
        <div class="searchHeader" style="height: 50px;" > 
            <el-col :span="14" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px;">
              <span style=" ">名称</span>
              <div style=" display: inline-block; margin-left: 0px; ">
                <el-input v-model="queryParams.video.title" 
                  style="width: 130px; " placeholder=""></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 15px;">
              <span style=" ">创建时间</span>
              <div style=" display: inline-block; height: 50px; margin-left:0px; ">
                <el-date-picker v-model="queryParams.createTimeRange" type="daterange" 
                  align="center" unlink-panels range-separator="至" start-placeholder="开始日期"
                  end-placeholder="结束日期" :picker-options="pickerOptions"
                  format="yyyy-MM-dd" value-format="yyyy-MM-dd" 
                  style="vertical-align: middle; width: 280px;">
                </el-date-picker>
              </div>
            </div>
          </el-col>

          <el-col :span="10" style="text-align: right; vertical-align: middle;
            line-height: 50px; ">
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getVideoList">查询</el-button>
              <!--<el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetForm">重置</el-button>-->
              <el-button type="primary" icon="el-icon-upload" size="medium" @click="uploadDialogVisible = true">上传</el-button>
              <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteVideo">删除</el-button>
            </div>
          </el-col>
        </div>
        <div>
          <el-table :data="page.records" stripe style="width: 100%" 
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" min-width="5%" header-align="center" 
              align="center" ></el-table-column>
            <el-table-column label="视频预览" min-width="20%" align="center">
              <template slot-scope="scope">
                <img style="height: 60px; width: 130px;
                  margin: auto 0;" :src=scope.row.coverUrl > 
              </template>
            </el-table-column>
            <el-table-column label="视频名称" min-width="20%" header-align="center" align="center">
                <template slot-scope="scope">
                  <span>{{scope.row.title}}</span>
                  <span>{{scope.row.destription}}</span>
                </template>
              </el-table-column>
            <el-table-column prop="status" label="视频状态" min-width="10%" align="center">
              <template slot-scope="scope">
                  {{ scope.row.status | translate(static.statusTypes, 'value', 'label') }}
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="播放时长" min-width="10%" align="center">
              <template slot-scope="scope">
                  {{ scope.row.duration | secondToTime }}
              </template>
            </el-table-column>
            <el-table-column prop="size" label="文件大小" min-width="10%" align="center">
              <template slot-scope="scope">
                  {{ scope.row.size | fileSizeFormat }}
              </template>
            </el-table-column>
            <!--
            <el-table-column prop="CateName" label="分类" min-width="10%" align="center">
              <template slot-scope="scope">
                <span v-if="undefined != scope.row.CateName">{{ scope.row.CateName }}</span>
                <span v-else>未分类</span>
              </template>
            </el-table-column>
            -->
            <el-table-column prop="createTime" label="创建时间" min-width="15%" align="center"></el-table-column>
            <el-table-column prop="videoId" label="操作" min-width="10%" align="center">
              <template slot-scope="scope">
                <a @click="updateVideoDialog(scope.row.videoId)" style="color: #409EFF">修改</a>
                <a @click="deleteVideo(scope.row.videoId)" 
                  style="color: #409EFF; margin-left: 20px;">删除</a>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>

      <div style="float: right; margin-top: 5px; ">
        <el-pagination background layout="total, prev, pager, next, jumper" :page-size="page.size"
          :total="page.total" :current-page="page.current" 
          @current-change="handleCurrentChange" ></el-pagination>
      </div>
      <el-dialog title="文件上传" :visible.sync="uploadDialogVisible" width="50%" 
        center :close-on-click-modal="false" :close-on-press-escape="false"
        :before-close="handleClose">
        <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
          margin-top: -20px;"></div>  
        <div style="width: 100%; ">
          <div style="width: 60%; height: 36px; display: inline-block; ">
            <div style="position:relative; ">
              <input type="file" style="width: 100px; height: 36px; filter:alpha(opacity=0); 
                opacity:0; position:absolute; left:0px; top:0px; z-index:10; " 
                @change="selectFile" multiple=""/>     
              <el-button type="primary" style="width: 100px; height: 36px; position: absolute; top: 0px; 
                z-index: 1; ">选取文件</el-button>  
            </div>
            <!--
            <div style="margin-left: 120px; ">
              <el-button type="success" style="width: 100px; height: 36px; "
                @click="uploadAll" >全部上传</el-button>
            </div>
            -->
          </div>
          <div v-loading="fileUploading" style="display: inline-block; width: 20px; 
            height: 36px; float: right; margin-right: 30px;  "> 
          </div>
        </div>
        <div class="grey-line" style="height: 1px;  margin-top: 10px;"></div>  
        <div style="margin-top: 10px; width: 100%; ">
          <table style="width: 100%;  ">
            <tr v-for="(item, index) in uploadFileList" style="height: 40px;">
              <td>
                <div style="width: 100%; " id="upload-item">
                  <div style="width: 92%; display: inline-block; padding: 5px 0;" >{{item.name}}</div>  
                  <div style="width: 4%; display: inline-block; float: right;  padding: 5px 0;">              
                    <i class="el-icon-circle-check" style="color: #67C23A; " 
                      v-show="item.percent == 100"></i>
                  </div>  
                  <div style="width: 4%; display: inline-block; float: right;  padding: 5px 0;">              
                    <i class="el-icon-delete" style=" " @click="removeUploadFile(index)"
                    v-show="item.percent != 100"></i>
                  </div> 
                </div>
                <el-progress :text-inside="true" :stroke-width="2" style=" "
                  :percentage="item.percent" status="success"></el-progress>
              </td>
            </tr>        
          </table>
        </div>
        <div style="width: 100%; text-align: center; margin-bottom: -15px; margin-top: 20px; ">
          <el-button type="info" plain style="width: 120px;  height: 36px;"
            @click="clearFinishedItem">清空成功记录</el-button>
            <el-button type="success" style="width: 120px; height: 36px; "
            @click="uploadAll" >全部上传</el-button>
          <el-button type="primary" style="width: 120px; height: 36px; margin-left: 25px;"
            @click="closeUploadDialog">完成</el-button>
        </div>
      </el-dialog>

      <el-dialog title="视频修改" :visible.sync="updateDialogVisible" width="50%" center>
        <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
          margin-top: -20px;"></div>  
        <div >
          <span>标题</span>
          <el-input v-model="video.Title" style="width: 80%; height: 30px; margin-left: 5px;"></el-input>
        </div>
        <!--
        <el-col :span="8">
          <div>
            <span>分类</span>
            <el-select v-model="defaultCateId" placeholder="请选择" 
              style="width: 70%; height: 30px; margin-left: 5px;">
              <el-option v-for="item in categories" :key="item.CateId"
                :label="item.CateName" :value="item.CateId">
              </el-option>
            </el-select>
          </div>
        </el-col>
        -->
        <div style="margin-top: 20px; height: 100px; vertical-align: middle;">
          <div style="line-height: 100px; height: 100px; display: inline-block;
            vertical-align: top;">
            <span style="line-height: 100px; height: 100px;" >封面</span>
          </div> 
          <!-- 注意，图片img要加vertical-align: top属性，否则会把旁边的控件挤下去，
            导致其他控件不垂直对称 -->
          <img style="height: 100px; width: 200px; margin-left: 5px;
            outline-width:0px; vertical-align:top; " 
            :src="video.CoverURL" >    
          <div style="position:relative; display: inline-block;  
            height: 70px; margin-top: 15px; margin-left: 5px;">
            <input type="file" style="width: 120px; height: 70px; filter:alpha(opacity=0); 
              opacity:0; position:absolute; left:0px; top:0px; z-index:10; " 
              @change="uploadImage"/>     
            <el-button plain icon="el-icon-plus" 
              style="width: 120px; height: 70px; position: absolute; top: 0px; 
              z-index: 1; border: 1px dashed #d9d9d9;">更换封面</el-button>  
          </div>
        </div>

        <div class="grey-line" style="height: 1px; margin-top: 15px;"></div>  
        <div style="margin-top: 20px; vertical-align: middle; text-align: center;">
          <el-button type="info" plain @click="cancelUpdateVideo" 
            style="width: 100px; height: 36px;">取消</el-button>
          <el-button type="success"   @click="updateVideo" style="margin-left: 30px;
          width: 100px; height: 36px;">提交</el-button>
        </div>
      </el-dialog>
    </div>
     
  </div>    
</template>

<script>  

var videoUploader;
var imageUploader;


window.onload=function(){
      console.log("videoManage onload");
    }

export default {
  name: 'videoManage', 
  data() {
    return {
      fileUploading: false,
      finishedNum: 0,
      multipleSelection: {
        ids: [] 
      },
      defaultCateId: 0,
      updateDialogVisible: false,
      static:{
        statusTypes:[
          {label:'上传中',value:'Uploading'},
          {label:'上传失败',value:'UploadFail'},
          {label:'上传完成',value:'UploadSucc'},
          {label:'转码中',value:'Transcoding'},
          {label:'转码失败',value:'TranscodeFail'},
          {label:'正常',value:'Normal'},
        ]
      },
      categories: [],
      video: {
        CoverURL: '',
        VideoId: '',
        ModifyTime: '',
        Title: '',
        CreationTime: '',
        Status: '',
        CateId: 0,
        CateName: '',
        CreateTime: '',
        Duration: 0,
        Size: 0,
      },
      // 注意IsShowWaterMark和Priority是字符串，
      // 如果格式错误，addFile时会报错First argument must be a string, Buffer
      videoUserData: {
        Vod: {
          UserData: {
            IsShowWaterMark: 'false',
            Priority: '7'
          }
        }
      },
      authParam: {
        fileName: '', 
        title: '',   
        description: '', 
        categoryId: 0,
      },
      uploadDialogVisible: false,
      // 格式[{"status":"ready","name":"01.jpg","size":24713,"percentage":0,
      // "uid":1514901045044,"raw":{"uid":1514901045044},"url":"http://xxx"}]
      fileList: [],
      queryParams: {
        createTimeRange: [],
        page: {
          pages: 1,
          size: 5,
          current: 1
        },
        video: {
          title: '',
        }
      },
      page: {
        total: 0,
        size: 5,
        pages: 0,
        current: 0,
        records: [],
      },
      
      uploadFileList: [
      ],
      form: {
      },
    }
  },

  methods: {
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
    handleClose(done) {
      done();
    },
    closeUploadDialog() {
      this.uploadDialogVisible = false;
      this.getVideoList();
    },
    clearFinishedItem() {
      console.log("clearFinishedItem " + JSON.stringify(this.uploadFileList));
      if (null != this.uploadFileList && undefined != this.uploadFileList) {
        // 从数组最尾巴开始删除已完成的item
        for (var i = this.uploadFileList.length - 1; i >= 0; i--) {
          var item = this.uploadFileList[i];
          console.log("item.percent " + item.percent);
          if (100 == item.percent) {
            console.log("splice " + i);
            this.uploadFileList.splice(i);
          }
        }
      }
      videoUploader.cleanList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getVideoList();
    },
    handleSelectionChange(val) {
      console.log("handleSelectionChange " + JSON.stringify(val));
      this.multipleSelection.ids = [];
      for (var i = 0; i < val.length; i++) {
        this.multipleSelection.ids.push(val[i].videoId);
      }
    },
    cancelUpdateVideo() {
      console.log("cancelUpdateVideo");
      this.updateDialogVisible = false;
    },
    updateVideo() {
      console.log("updateVideo title " + this.video.Title + " coverUrl " + this.video.CoverURL);
      this.$fetch.updateVideo(this.video).then((resp) => {
        console.log("updateVideo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.updateDialogVisible = false;
          this.getVideoList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    updateVideoDialog(videoId) {
      console.log("update videoId " + videoId);
      this.$fetch.getVideoInfo(videoId).then((resp) => {
        console.log("getVideoInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          var jsonStr = JSON.parse(JSON.stringify(resp.data));
          var videoRespObj = JSON.parse(jsonStr);
          this.video = videoRespObj.Video;
          // 默认选择项
          if (null == this.video.CateId || undefined == this.video.CateId) {
            // 如果没有分类，则设置默认分类“未分类”
            this.defaultCateId = 0;
          } else {
            this.defaultCateId = this.video.CateId;
          }
          console.log("video.Title " + this.video.Title + " CateId " + this.defaultCateId);
          this.updateDialogVisible = true;
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    }, 
    deleteVideo(videoId) {
      this.multipleSelection.ids.push(videoId);
      this.batchDeleteVideo();
    },
    batchDeleteVideo() {
      console.log("batchDeleteVideo " );
      this.$confirm('确定删除吗?').then(() => {
        this.$fetch.deleteVideo(this.multipleSelection).then((resp) => {
          console.log("deleteVideo " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            var jsonStr = JSON.parse(JSON.stringify(resp.data));
            var videoRespObj = JSON.parse(jsonStr);
            this.$message.success("删除成功");
            this.getVideoList();
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
    removeUploadFile(index) {
      console.log("removeUploadFile " + index);
      videoUploader.deleteFile(index);
      this.uploadFileList.splice(index, 1);
      localStorage.setItem("uploadFileList", JSON.stringify(this.uploadFileList));
      var list = videoUploader.listFiles();
      console.log("file num " + list.length);
      if (0 == list.length) {
        this.fileUploading = false;
      }
    },
    selectFile($event) {
      console.log("selectFile ");  
      var file = $event.target.files[0];
      var fileName = file.name;
      var fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      if (null == fileType || '' == fileType) {
        console.log("fileType " + fileType);
        this.$message.error('文件格式错误');
        return;
      }
      /*必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", ".dv", 
      ".flv", ".f4v", ".gif", ".m2t", ".m3u8", ".m4v", ".mj2", ".mjpeg", ".mkv", 
      ".mov", ".mp4", ".mpe", ".mpg", ".mpeg", ".mts", ".ogg", ".qt", ".rm", ".rmvb", 
      ".swf", ".ts", ".vob", ".wmv", ".webm", ".aac", ".ac3", ".acm", ".amr", ".ape", 
      ".caf", ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）*/
      if (fileType != '3gp' && fileType != 'avi' && fileType != 'dat' && fileType != 'flv'
          && fileType != 'f4v'  && fileType != 'm3u8'  && fileType != 'mjpeg'  && fileType != 'mkv'
          && fileType != 'mov'  && fileType != 'mp4'  && fileType != 'mpg'  && fileType != 'mpeg'
          && fileType != 'rm'  && fileType != 'rmvb'  && fileType != 'wmv'  && fileType != 'webm' 
          && fileType != 'aac'  && fileType != 'wav'  && fileType != 'wma') {
        console.log("fileType " + fileType);
        this.$message.error('文件格式错误');
        return;
      }
      console.log("selectFile " + file.name + " totalSize " + file.size);  
      var fileItem = {
        name: '',
        size: 0,
        percent: 0,
      };
      fileItem.name = file.name;
      fileItem.size = file.size;
      fileItem.percent = 0;
      this.uploadFileList.push(fileItem);  
      localStorage.setItem("uploadFileList", JSON.stringify(this.uploadFileList));
      videoUploader.addFile(file, null, null, null, JSON.stringify(this.videoUserData));
    },
    uploadImage($event) {
      console.log("uploadImage ");  
      var file = $event.target.files[0];
      console.log("uploadImage " + file.name + " totalSize " + file.size);  
      imageUploader.addFile(file, null, null, null, JSON.stringify(this.videoUserData));
      imageUploader.startUpload();
    },
    uploadAll() {
      console.log("uploadAll");
      if (this.uploadFileList.length > 0 && this.finishedNum != this.uploadFileList.length) {
        this.fileUploading = true;
      }
      videoUploader.startUpload();
    },
    handleRemove(file, fileList) {// 删除
      console.log("handleRemove", JSON.stringify(file));
    },
    handlePreview(file) {// 点击某行
      console.log("handlePreview", JSON.stringify(file));
    },
    handleChange(file, fileList) {// 新增
      console.log("handleChange", JSON.stringify(file));
      videoUploader.addFile(file, null, null, null, JSON.stringify(this.videoUserData));
    },
    handleSuccess(file, fileList) {
      console.log("handleSuccess", JSON.stringify(file));
    },

    getVideoList() {
      this.$fetch.getVideoList(this.queryParams).then((resp) => {
        console.log("videoList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.page = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
      this.getVideoList();
    },
    getVideoCategory() {
      this.$fetch.getVideoCategory().then((resp) => {
        console.log("getVideoCategory resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          var jsonStr = JSON.parse(JSON.stringify(resp.data));
          console.log("getVideoCategory resp " + jsonStr);
          var respObj = JSON.parse(jsonStr);
          this.categories = respObj.SubCategories.Category;
          // 增加“未分类”选项
          var nullCate = {
            "CateId": 0,
            "CateName": "未分类",
          }
          this.categories.push(nullCate);
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    }
     
  }, 

  mounted: function() {
    console.log("mounted");
    this.getVideoList();

    // 读取内存中上传文件列表
    var uploadFileListJson = localStorage.getItem("uploadFileList");
    if (null != uploadFileListJson && undefined != uploadFileListJson) {
      this.uploadFileList = JSON.parse(uploadFileListJson);
    }
    console.log("mounted uploadFileList " + JSON.stringify(this.uploadFileList));

    // 初始化上传接口
    var that = this;
    videoUploader = new AliyunUpload.Vod({
      //分片大小默认1M
      partSize: 1048576,
      //并行上传分片个数，默认5
      parallel: 5,
      //网络原因失败时，重新上传次数，默认为3
      retryCount: 3,
      //网络原因失败时，重新上传间隔时间，默认为2秒
      retryDuration: 2,
      // 开始上传
      'onUploadstarted': function (uploadInfo) {
        // 请求参数
        console.log("onUploadstarted uploadInfo " + JSON.stringify(uploadInfo));
        console.log("uploadInfo.file.name " + uploadInfo.file.name);
        var index = uploadInfo.file.name.lastIndexOf('.');
        that.authParam.title = uploadInfo.file.name.substring(0, index);
        that.authParam.fileName =  uploadInfo.file.name;
        that.authParam.description = '';
        console.log("onUploadstarted authParam " + JSON.stringify(that.authParam));

        let request = new XMLHttpRequest();
        var url = that.$fetch.getBaseUrl() + "admin/video/getVideoUploadAuth";
        request.open('POST', url, false) // 第三个参数 false 代表设置同步请求
        request.setRequestHeader('Accept', 'application/json');
        request.setRequestHeader('Content-Type', 'application/json');
        request.send(JSON.stringify(that.authParam));
        if (request.status === 200) {
          console.log('success ' + request.responseText);
          var resp = JSON.parse(request.responseText);
          if (resp.returnCode == '1') {        
            var authResp = resp.data;
            console.log("authResp.uploadAuth " + authResp.uploadAuth);
            // 设置上传地址和上传凭证
            videoUploader.setUploadAuthAndAddress(uploadInfo, authResp.uploadAuth, 
              authResp.uploadAddress); 
          } else {
            that.$message.error(resp.returnMsg + "");
            // 更新上传列表文件状态
          }
        } else {
          that.$message.error("上传凭证申请失败：" + request.status);
        }
      },
      // 文件上传成功
      'onUploadSucceed': function (uploadInfo) {
        console.log("onUploadSucceed: " + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
        that.finishedNum++;
        if (that.finishedNum == that.uploadFileList.length) {
          that.fileUploading = false;
        }
      },
      // 文件上传失败
      'onUploadFailed': function (uploadInfo, code, message) {
        console.log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message);
      },
      // 文件上传进度，单位：字节
      'onUploadProgress': function (uploadInfo, totalSize, loadedPercent) {
        console.log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + Math.ceil(loadedPercent * 100) + "%");
        for (var i = 0; i < that.uploadFileList.length; i++) {
          var item = that.uploadFileList[i];
          console.log("item fileName " + item.name + " fileSize " + item.size);
          if (uploadInfo.file.name == item.name && totalSize == item.size) {
            console.log("refresh uploadFileList");
            item.percent = Math.ceil(loadedPercent * 100); 
            // 更新这行记录
            that.uploadFileList.splice(i, 1, item);
            localStorage.setItem("uploadFileList", JSON.stringify(that.uploadFileList));
          }
        }
      },
      // 上传凭证超时
      'onUploadTokenExpired': function () {
          console.log("onUploadTokenExpired");
          // videoUploader.resumeUploadWithAuth(uploadAuth);
      }
    });

    imageUploader = new AliyunUpload.Vod({
      //分片大小默认1M
      partSize: 1048576,
      //并行上传分片个数，默认5
      parallel: 5,
      //网络原因失败时，重新上传次数，默认为3
      retryCount: 3,
      //网络原因失败时，重新上传间隔时间，默认为2秒
      retryDuration: 2,
      // 开始上传
      'onUploadstarted': function (uploadInfo) {
        let request = new XMLHttpRequest();
        var url = that.$fetch.getBaseUrl() + "admin/video/getImageUploadAuth";
        request.open('POST', url, false) // 第三个参数 false 代表设置同步请求
        request.setRequestHeader('Accept', 'application/json');
        request.setRequestHeader('Content-Type', 'application/json');
        request.send(JSON.stringify(that.authParam));
        if (request.status === 200) {
          console.log('success ' + request.responseText);
          var resp = JSON.parse(request.responseText);
          if (resp.returnCode == '1') {        
            var authResp = resp.data;
            that.uploadImageUrl = authResp.imageURL;
            console.log("authResp.uploadAuth " + authResp.uploadAuth);
            // 设置上传地址和上传凭证
            imageUploader.setUploadAuthAndAddress(uploadInfo, authResp.uploadAuth, 
              authResp.uploadAddress); 
          } else {
            that.$message.error(resp.data.returnMsg + "");
            // 更新上传列表文件状态
          }
        } else {
          that.$message.error("上传凭证申请失败：" + request.status);
        }
      },
      // 文件上传成功
      'onUploadSucceed': function (uploadInfo) {
        console.log("onUploadSucceed: " + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
        that.$message.success("图片上传成功"); 
        // 设置图片显示新的地址
        that.video.CoverURL = that.uploadImageUrl;
      },
      // 文件上传失败
      'onUploadFailed': function (uploadInfo, code, message) {
        console.log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message);
      },
      // 文件上传进度，单位：字节
      'onUploadProgress': function (uploadInfo, totalSize, loadedPercent) {
        console.log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + Math.ceil(loadedPercent * 100) + "%");
      },
      // 上传凭证超时
      'onUploadTokenExpired': function () {
          console.log("onUploadTokenExpired");
          // imageUploader.resumeUploadWithAuth(uploadAuth);
      }
    });

  },
}
</script>
 
<style > 
#upload-item:hover {background-color:#E4E7ED;} 

.el-input__inner { 
  height: 35px;
}
 

</style>
