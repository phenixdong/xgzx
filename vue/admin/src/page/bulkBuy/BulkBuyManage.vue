<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="16" style="text-align: left; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: left; line-height: 50px; ">
              <span style=" ">用户</span>
              <div style=" display: inline-block;">
                <el-input v-model="queryParams.bulkBuy.customerName" style="" placeholder=""
                  style="width: 130px; "></el-input>
              </div>
            </div>
            <div style="float: left; height: 50px; padding-left: 15px;">
              <span style=" ">交易时间</span>
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
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getBulkBuyList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
              <el-button type="primary" icon="el-icon-plus" size="medium" @click="showAddDialog">新增</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="bulkBuyList.records" stripe style="width: 100%" 
        >
        <el-table-column prop="customerName" label="用户名称" min-width="15%" align="center">
          <template slot-scope="scope">
            <span style="line-height: 50px; ">{{scope.row.customerName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="购买数量" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="createTime" label="交易时间" min-width="15%" align="center"></el-table-column>
        <el-table-column prop="memberTypeId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="showDetailDialog(scope.row.bulkBuyId)" 
              style="color: #409EFF; ">详情</a> 
            <a @click="deleteBulkBuy(scope.row.bulkBuyId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="float: right; margin-top: 5px; ">
      <el-pagination background layout="total, prev, pager, next, jumper" :page-size="5"
        :total="bulkBuyList.total" :current-page="queryParams.page.current" 
        @current-change="handleCurrentChange" ></el-pagination>
    </div>

    <el-dialog :title="addUpdateTitle" :visible.sync="addDialogVisible" width="50%" center>
      <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
        margin-top: -20px;"></div> 
      <el-form :model="bulkBuy" :rules="rules" ref="ruleForm" size="medium">
        <div style="height:45px; text-align: left;">
          <el-col :span="12"> 
            <el-form-item prop="customerName">
              <div >
                <span><span class="red-star">*</span>用户名称</span>
                <el-input v-model="bulkBuy.customerName" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12"> 
            <el-form-item>
              <div >
                <span>购买数量</span>
                <el-input-number v-model="bulkBuy.number" controls-position="right" 
                  :min="1" style="width: 40%; margin-left: 5px;"
                  size="medium" 
                  >
                </el-input-number>
              </div>
            </el-form-item>
          </el-col>
        </div>
        <div class="grey-line" style="height: 1px; margin-bottom: 5px; "></div> 
      </el-form>
      
      <div style="margin-top: 20px; vertical-align: middle; text-align: center;"
        v-show="!qrCodeVisible">
        <el-button type="info" v-if="1 == dialogType" plain @click="cancelAddDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary" v-if="1 == dialogType" @click="addBulkBuy" style="margin-left: 30px;
          width: 100px; height: 36px;">新增</el-button>  
      </div>

      <div style="text-align: center; width: 100%; " v-show="qrCodeVisible" >
        <div id="qrCodeDiv" v-show="!canvasVisible "
          style="width: 230px; text-align: center; height: 400px; 
          background-color: black; margin: 0 auto; 
          background-repeat:no-repeat; background-size:100% 100%; " 
          v-bind:style="{backgroundImage: 'url(' + qrBackImage + ')'}"
          > 
          <div style="width: 100%; text-align: center; padding-top: 255px; ">
            <qrcode-vue :value="qrShareLink" size="100" level="H"
              style=""></qrcode-vue>
          </div>
        </div>
        <div id="myCanvas" style="width: 230px; text-align: left; height: 400px; 
          display: inline-block; background-color: black; " v-show="canvasVisible" ></div>
      </div>
      
      <div style="margin-top: 5px; vertical-align: middle; text-align: center;"
        v-show="qrCodeVisible">
        <div style="font-size: 15px;  ">
          <span>密码</span>
          <span style="margin-left: 0px; border: 1px solid;  ">{{bulkBuy.password}}</span>
        </div>
        <div class="grey-line" style="height: 1px; margin-top: 3px; margin-bottom: 5px; "></div> 
        <el-button type="info" plain @click="cancelAddDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary" icon="el-icon-download"  @click="downQrCode" 
          style="margin-left: 30px; width: 100px; height: 36px;">下载</el-button>  
      </div>
    </el-dialog>
 

  </div>    
</template>

<script>
import QrcodeVue from 'qrcode.vue';
import html2canvas from 'html2canvas';
// 要在下载的canvas2image.js里面，最后面加一句"export default Canvas2Image;"，
// 否则会找不到saveAsPNG函数
import Canvas2Image from 'canvas2image';

export default {
  name: 'bulkBuyManage', 
  components: {
    QrcodeVue
  },
  data() {
    return {
      qrCodeVisible: false,
      canvasVisible: false,
      qrShareLink: '',
      qrBackImage: require('../../assets/imgs/qr_back.jpg'),
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
      selectIds: [],
      rules: {
        customerName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
        ],
        number: [
          { required: true, message: '请输入数量', trigger: 'blur' },
          { type: 'number', message: '必须为数字', trigger: 'blur'}
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
      bulkBuyList: {
        records: [],
        total: 0,
        pages: 0,
      },
      queryParams: {
        page: {
          current: 1,
          size: 5,
        },
        bulkBuy: {
          customerName: '',
        },
        createTimeRange: []
      },
      bulkBuy: {
      },
      addDialogVisible: false,
    }
  },

  methods: {
    downQrCode() {
      console.log("downQrCode");
      setTimeout(function () {
        var that = this;

        var shareContent = document.getElementById("qrCodeDiv");
        var width = shareContent.offsetWidth; 
        var height = shareContent.offsetHeight;  
        var canvas = document.createElement("canvas"); //创建一个canvas节点
        var scale = 6; //放大倍数，要放大，否则手机上模糊
        canvas.width = width * scale;  
        canvas.height = height * scale; 
        canvas.getContext("2d").scale(scale, scale); 
        var opts = {
            scale: scale,  
            canvas: canvas,  
            // logging: true,  
            width: width, 
            height: height,
            useCORS: true // 【重要】开启跨域配置
        };
        
        // 注意要刷新，修改才有效，因为每次都要canvas到img转换
        html2canvas(shareContent, opts).then(function(canvas) {
          var context = canvas.getContext('2d');
          // 【重要】关闭抗锯齿
          context.mozImageSmoothingEnabled = false;
          context.webkitImageSmoothingEnabled = false;
          context.msImageSmoothingEnabled = false;
          context.imageSmoothingEnabled = false;

          var canvasDiv = document.getElementById("myCanvas");
          var nodelist = canvasDiv.childNodes;
          for(var i = nodelist.length - 1; i >= 0; i--) {  
            var x = canvasDiv.removeChild(nodelist[i]);  
            if (x.nodeType == 1) {  
              x = null;  
            }  
          }  
        
          canvasDiv.appendChild(canvas);
          that.canvasVisible = true;
          Canvas2Image.saveAsPNG(canvas, 920, 1600);// 图片尺寸要大，否则手机上模糊
        }) 
      }, 500);
       
      
    },
    deleteBulkBuy(bulkBuyId) {
      this.$confirm('确定删除吗?').then(() => {
        console.log("deleteBulkBuy " );
        this.selectIds = [];
        this.selectIds.push(bulkBuyId);
        var params = {ids: this.selectIds};
        console.log("deleteBulkBuy param " + JSON.stringify(params));
        this.$fetch.deleteBulkBuy(params).then((resp) => {
          console.log("deleteBulkBuy resp " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getBulkBuyList();
          } else {
            this.$message.error(resp.returnMsg + "");
          }
        }, (error) => {
          this.$message.error(error);
        });
      }).catch(() => {
      });
    }, 
    resetQuery() {
      this.queryParams.bulkBuy.customerName = '';
      this.queryParams.createTimeRange = [];
      this.getBulkBuyList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getBulkBuyList();
    },
    showDetailDialog(bulkBuyId) {
      console.log("getBulkBuyInfo param " + bulkBuyId);
      this.$fetch.getBulkBuyInfo(bulkBuyId).then((resp) => {
        console.log("getBulkBuyInfo resp " + JSON.stringify(resp.data));
        if ("1" == resp.returnCode) {
          this.bulkBuy = JSON.parse(JSON.stringify(resp.data));
          this.qrShareLink = this.$fetch.getBaseShareUrl() + '?page=null' 
            + '&bulkBuyId=' + resp.data.bulkBuyId;
          console.log("qrShareLink " + this.qrShareLink);
          this.dialogType = 3;// 对话框类型为“详情”
          this.addUpdateTitle = '详情';
          this.addDialogVisible = true;
          this.qrCodeVisible = true;
        } else {
          this.$message.error(resp.returnMsg + "");
          this.qrCodeVisible = false;
        }
      }, (error) => {
        this.$message.error(error);
        this.qrCodeVisible = false;
      });
    },
    showAddDialog(bulkBuyId) {
      console.log("showAddDialog bulkBuyId " + bulkBuyId);
      this.addUpdateTitle = '新增';
      this.dialogType = 1; 
      this.bulkBuy = {};
      this.addDialogVisible = true;
      this.qrCodeVisible = false;
    },
    cancelAddDialog() {
      this.addDialogVisible = false;
    },
    getBulkBuyList() {
      console.log("getBulkBuyList param " + JSON.stringify(this.queryParams));
      this.$fetch.getBulkBuyList(this.queryParams).then((resp) => {
        console.log("getBulkBuyList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.bulkBuyList = JSON.parse(JSON.stringify(resp.data));
        } else {
          this.$message.error(resp.returnMsg + "");
        }
      }, (error) => {
        this.$message.error(error);
      });
    },
    addBulkBuy() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          console.log("addBulkBuy param " + JSON.stringify(this.bulkBuy));
          this.$fetch.addBulkBuy(this.bulkBuy).then((resp) => {
            console.log("addBulkBuy resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$message.success("新增成功");
              this.bulkBuy = resp.data;
              this.qrShareLink = this.$fetch.getBaseShareUrl() + '?page=null' 
                + '&bulkBuyId=' + resp.data.bulkBuyId;
              console.log("qrShareLink " + this.qrShareLink);
              this.qrCodeVisible = true;
              this.getBulkBuyList();
            } else {
              this.$message.error(resp.returnMsg + "");
              this.qrCodeVisible = false;
            }
          }, (error) => {
            this.$message.error(error);
            this.qrCodeVisible = false;
          });
        } else {
          this.$message.error('请检查输入');
          this.qrCodeVisible = false;
          return false;
        }
      });
    },
  }, 

  mounted: function() {
    this.getBulkBuyList();
  },
}
</script>
 
<style >  
.el-input__inner { 
  height: 35px;
} 

</style>
