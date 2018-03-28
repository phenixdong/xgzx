<template>  
  <div>     
    <div>
      <el-form ref="form" label-width="90px"  >
        <div class="searchHeader" style="height: 50px;" > 
          <el-col :span="24" style="text-align: right; vertical-align: middle;
            line-height: 50px; "
            >
            <div style="float: right; line-height: 50px;">
              <el-button type="primary" icon="el-icon-search" size="medium" @click="getMemberTypeList">查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
              <el-button type="primary" icon="el-icon-plus" size="medium" @click="showAddDialog">新增</el-button>
            <el-button type="info" icon="el-icon-delete" size="medium" plain @click="batchDeleteMemberType">删除</el-button>
            </div>
          </el-col>
        </div>
      </el-form> 

      <el-table :data="memberTypeList" stripe style="width: 100%" 
        >
        <el-table-column prop="memberTypeName" label="会员名称" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="price" label="价格(元)" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="period" label="有效期（月）" min-width="10%" align="center"></el-table-column>
        <el-table-column prop="memberTypeId" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <a @click="showDetailDialog(scope.row.memberTypeId)" 
              style="color: #409EFF; ">详情</a>
            <a @click="showUpdateDialog(scope.row.memberTypeId)" 
              style="color: #409EFF; margin-left: 20px;">修改</a>
            <a @click="deleteMemberType(scope.row.memberTypeId)" 
              style="color: #409EFF; margin-left: 20px;">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="addUpdateTitle" :visible.sync="addUpdateDialogVisible"
      width="50%" center>
      <div class="grey-line" style="height: 1px; margin-bottom: 15px; 
        margin-top: -20px;"></div> 
      <el-form :model="memberType" :rules="rules" ref="ruleForm" size="medium">
        <div style="height:45px; text-align: left;">
          <el-col :span="12"> 
            <el-form-item prop="memberTypeName">
              <div >
                <span><span class="red-star">*</span>名称</span>
                <el-input v-model="memberType.memberTypeName" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12"> 
            <el-form-item>
              <div >
                <span>顺序</span>
                <el-input-number v-model="memberType.orderNo" controls-position="right" 
                  :min="1" style="width: 40%; margin-left: 5px;"
                  size="medium" 
                  >
                </el-input-number>
              </div>
            </el-form-item>
          </el-col>
        </div>
        <div class="grey-line" style="height: 1px; margin-bottom: 5px; 
          "></div> 
      
        <div style="height:55px; text-align: left;">
          <el-col :span="12"> 
            <el-form-item prop="price">
              <div >
                <span><span class="red-star">*</span>价格</span>
                <el-input v-model.number="memberType.price" style="width: 190px; height: 30px; margin-left: 5px;"></el-input>
                <span>.00(元)</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item> 
              <div >
                <span>时长</span>
                <el-input-number v-model="memberType.period" controls-position="right" 
                  :min="1" style="width: 40%; margin-left: 5px;"
                  size="medium" 
                  >
                </el-input-number>
                <span>(月)</span>
              </div>
            </el-form-item>
          </el-col>
        </div>

        <div class="grey-line" style="height: 1px; margin-bottom: 5px; 
          "></div> 
        <div style="height:55px; text-align: left; width: 100%;">
          <el-col :span="22" > 
            <el-form-item>
              <div >
                <span>描述</span>
                <el-input v-model="memberType.description" style="width: 89%; 
                  height: 30px; margin-left: 5px;" type="textarea" :rows="2" ></el-input>
              </div>
            </el-form-item>
          </el-col>
        </div>
      </el-form>
        
      <div style="margin-top: 20px; vertical-align: middle; text-align: center;">
        <el-button type="info" plain @click="cancelAddUpdateDialog" 
          style="width: 100px; height: 36px;">取消</el-button>
        <el-button type="primary" v-if="1 == dialogType"  @click="addMemberType" style="margin-left: 30px;
          width: 100px; height: 36px;">新增</el-button>
        <el-button type="primary" v-if="2 == dialogType"  @click="updateMemberType" style="margin-left: 30px;
          width: 100px; height: 36px;">修改</el-button>
      </div>
    </el-dialog>
    
  </div>    
</template>

<script>
   
export default {
  name: 'memberTypeManage', 
  data() {
    return {
      rules: {
        memberTypeName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' },
          { type: 'number', message: '必须为数字', trigger: 'blur'}
        ],
      },
      selectIds: [],
      memberType: {
        memberTypeName: '',
        orderNo: 0,
        price: 0, 
        period: 0,
        description: '',
      },
      static:{
        // 商品类型，1会员，2商品
        orderProductType:[
          {label:'购买会员',value: 1},
          {label:'购买商品',value: 2},
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
      dialogType: 1, //1新增，2修改，3详情
      addUpdateTitle: '新增',
      memberTypeList: [],
      queryParams: {
        page: {
          current: 1,
          size: 10,
        },
        memberType: {
          memberTypeName: '',
        },
        createTimeRange: []
      },
      memberType: {
      },
      addUpdateDialogVisible: false,
    }
  },

  methods: {
    deleteMemberType(memberTypeId) {
      this.selectIds.push(memberTypeId);
      this.batchDeleteMemberType();
    },
    batchDeleteMemberType() {
      console.log("batchDeleteMemberType " );
      if (this.selectIds.length == 0) {
        this.$message.error('请勾选一项');
        return;
      }
      this.$confirm('确定删除吗?').then(() => {
        var params = {ids: this.selectIds};
        console.log("params " + params);
        this.$fetch.deleteMemberType(params).then((resp) => {
          console.log("deleteMemberType " + JSON.parse(JSON.stringify(resp.data)));
          if ("1" == resp.returnCode) {
            this.$message.success("删除成功");
            this.getMemberTypeList();
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
    resetQuery() {
      this.queryParams.memberType.memberTypeName = '';
      this.queryParams.createTimeRange = [];
      this.getMemberTypeList();
    },
    handleCurrentChange(val) {
      console.log("handleCurrentChange " + val);
      this.queryParams.page.current = val;
      this.getMemberTypeList();
    },
    showAddDialog() {
      this.addUpdateTitle = '新增';
      this.dialogType = 1; 
      this.memberType = {
        memberTypeName: '',
        orderNo: 0,
        price: 0, 
        period: 0,
        description: '',
      }
      this.addUpdateDialogVisible = true;
    },
    showDetailDialog(memberTypeId) {
      console.log("showDetailDialog memberTypeId " + memberTypeId);
      this.$fetch.getMemberTypeInfo(memberTypeId).then((resp) => {
        console.log("getMemberTypeInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.memberType = JSON.parse(JSON.stringify(resp.data));
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
    showUpdateDialog(memberTypeId) {
      console.log("showUpdateDialog memberTypeId " + memberTypeId);
      this.$fetch.getMemberTypeInfo(memberTypeId).then((resp) => {
        console.log("getMemberTypeInfo " + JSON.parse(JSON.stringify(resp.data)));
        if ("1" == resp.returnCode) {
          this.memberType = JSON.parse(JSON.stringify(resp.data));
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
    addMemberType() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.$fetch.addMemberType(this.memberType).then((resp) => {
            console.log("addMemberType resp " + JSON.stringify(resp));
            if ("1" == resp.returnCode) {
              this.$message.success("新增成功");
              this.getMemberTypeList();
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
    updateMemberType() {
      this.$fetch.updateMemberType(this.memberType).then((resp) => {
        console.log("updateMemberType resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.$message.success("修改成功");
          this.getMemberTypeList();
        } else {
          this.$message.error(resp.returnMsg + "");
        }
        this.addUpdateDialogVisible = false;
      }, (error) => {
        this.$message.error(error);
        this.addUpdateDialogVisible = false;
      });
    },
    cancelAddUpdateDialog() {
      this.addUpdateDialogVisible = false;
    },
    getMemberTypeList() {
      this.$fetch.getMemberTypeList(this.queryParams).then((resp) => {
        console.log("getMemberTypeList resp " + JSON.stringify(resp));
        if ("1" == resp.returnCode) {
          this.memberTypeList = JSON.parse(JSON.stringify(resp.data));
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
  }, 

  created: function() {
    this.getMemberTypeList();
  }, 

  mounted: function() {
     
  },
}
</script>
 
<style >  
.el-input__inner { 
  height: 35px;
} 

</style>
