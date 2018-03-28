import axios from 'axios';
import config from './config';
import host from './host';
import { Loading,Message} from 'element-ui';
axios.defaults.baseURL = host.hostIp;

axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
axios.defaults.withCredentials = true;


function doGet(url, params) {
    if (axios.defaults.headers.authorization == null) {
      axios.defaults.headers.authorization = sessionStorage.getItem("authorization");
    }
    return new Promise(function(resolve, reject) {
        var str = "";
        if(typeof params == 'object')
          str = Object.values(params).join("/");
        else
          str = params ? params : "";

        var u = str ? url + "/" + str : url;
        axios.get(u).then((response) => {
          var r = response.data;
          if(r.hasOwnProperty('returnCode') && r.returnCode !=='1'){
            Message.warning({
              message:r.returnMsg
            });
          }
          resolve(r);
        }).catch((error) => {
          Message.error({
            message:'服务或网络异常!!!'
          });
          reject(error);
        })
    });
}

function doPost(url, params, isLoading) {
    if (axios.defaults.headers.authorization == null) {
      axios.defaults.headers.authorization = sessionStorage.getItem("authorization");
    }
    let loadingInstance = null;
    if(isLoading)
      loadingInstance  = Loading.service({target:'.a-product',lock: true,text: '努力加载中',spinner: 'el-icon-loading',background: 'rgba(231, 231, 231, .6)'});
    return new Promise(function(resolve, reject) {
        axios.post(url, params).then((response) => {
          loadingInstance && loadingInstance.close();
          var r = response.data;
          if(r.hasOwnProperty('returnCode') && r.returnCode !=='1'){
            Message.warning({
              message:r.returnMsg
            });
          }
          resolve(r);
        }).catch((error) => {
          loadingInstance && loadingInstance.close();
          Message.error({
            message:'服务或网络异常!!!'
          });
          reject(error);
        });
    })
}
function setAuthorization(authorization) {
  if (authorization != null && authorization != '') {
    axios.defaults.headers.authorization = authorization;
    sessionStorage.setItem("authorization", authorization);
  }
  return true;
}
function getBaseUrl() {
  return axios.defaults.baseURL;
} 

export default {
  getBaseUrl() {
    return getBaseUrl();
  },
  //获取验证码
  getCheckCode() {
    // 必须加时间戳，否则有的浏览器不能发出请求
    return getBaseUrl() + config.getCheckCode +"?dataString="+(new Date()).getTime();
  }, 
  getUploadFileUrl() {
    return getBaseUrl() + config.uploadFile;
  },
  uploadAdvertiseFile() {
    return getBaseUrl() + config.uploadAdvertiseFile;
  },
  uploadContactUsFile() {
    return getBaseUrl() + config.uploadContactUsFile;
  },
  uploadShareIconFile() {
    return getBaseUrl() + config.uploadShareIconFile;
  },
  getDownloadFileUrl() {
    return getBaseUrl() + config.staticFilePath;
  }, 
  login(params) {
    return doPost(config.login, params);
  },
  logout(params) {
    return doPost(config.logout, params);
  },
  changePassword(params) {
    return doPost(config.changePassword, params);
  },
  getVideoList(params) {
    return doPost(config.getVideoList, params);
  },
  getVideoInfo(params) {
    return doGet(config.getVideoInfo, params);
  },
  getVideoUploadAuth(params) {
    return doPost(config.getVideoUploadAuth, params);
  },
  getVideoCategory() {
    return doGet(config.getVideoCategory, '');
  },
  deleteVideo(params) {
    return doPost(config.deleteVideo, params);
  },
  updateVideo(params) {
    return doPost(config.updateVideo, params);
  },
  getCategoryList(params) {
    return doPost(config.getCategoryList, params);
  },
  getCategoryInfo(params) {
    return doGet(config.getCategoryInfo, params);
  },
  deleteCategory(params) {
    return doPost(config.deleteCategory, params);
  },
  addCategory(params) {
    return doPost(config.addCategory, params);
  },
  updateCategory(params) {
    return doPost(config.updateCategory, params);
  },
  getTeacherList(params) {
    return doPost(config.getTeacherList, params);
  },
  getTeacherInfo(params) {
    return doGet(config.getTeacherInfo, params);
  },
  deleteTeacher(params) {
    return doPost(config.deleteTeacher, params);
  },
  addTeacher(params) {
    return doPost(config.addTeacher, params);
  },
  updateTeacher(params) {
    return doPost(config.updateTeacher, params);
  },
  getTeacherRebate(params) {
    return doPost(config.getTeacherRebate, params);
  },
  getLessonList(params) {
    return doPost(config.getLessonList, params);
  },
  getLessonInfo(params) {
    return doGet(config.getLessonInfo, params);
  },
  deleteLesson(params) {
    return doPost(config.deleteLesson, params);
  },
  addLesson(params) {
    return doPost(config.addLesson, params);
  },
  updateLesson(params) {
    return doPost(config.updateLesson, params);
  },
  getUserList(params) {
    return doPost(config.getUserList, params);
  },
  getUserInfo(params) {
    return doGet(config.getUserInfo, params);
  },
  deleteUser(params) {
    return doPost(config.deleteUser, params);
  },
  updateUser(params) {
    return doPost(config.updateUser, params);
  },
  lockUser(params) {
    return doPost(config.lockUser, params);
  },
  getTransactionList(params) {
    return doPost(config.getTransactionList, params);
  },
  getRebateTransactionList(params) {
    return doPost(config.getRebateTransactionList, params);
  },
  getRebateWithdrawList(params) {
    return doPost(config.getRebateWithdrawList, params);
  },
  payRebateWithdraw(params) {
    return doPost(config.payRebateWithdraw, params);
  },
  getGroupsList(params) {
    return doPost(config.getGroupsList, params);
  },
  getBulkBuyGroupsList(params) {
    return doPost(config.getBulkBuyGroupsList, params);
  },
  getGroupsInfo(params) {
    return doGet(config.getGroupsInfo, params);
  },
  getMemberTypeList(params) {
    return doPost(config.getMemberTypeList, params);
  },
  addMemberType(params) {
    return doPost(config.addMemberType, params);
  },
  getMemberTypeInfo(params) {
    return doGet(config.getMemberTypeInfo, params);
  },
  getSendMessageList(params) {
    return doPost(config.getSendMessageList, params);
  },
  deleteMemberType(params) {
    return doPost(config.deleteMemberType, params);
  },
  deleteBulkBuy(params) {
    return doPost(config.deleteBulkBuy, params);
  },
  getBulkBuyInfo(params) {
    return doGet(config.getBulkBuyInfo, params);
  },
  getBaseShareUrl() {
    return getBaseUrl() + 'admin/wx/share/';
  },
  updateMemberType(params) {
    return doPost(config.updateMemberType, params);
  },
  getAccountStatistic(params) {
    return doPost(config.getAccountStatistic, params);
  },
  getCommonParam(params) {
    return doGet(config.getCommonParam, params);
  },
  updateRebateRate(params) {
    return doPost(config.updateRebateRate, params);
  },
  updateTeacherRebateRate(params) {
    return doPost(config.updateTeacherRebateRate, params);
  },
  finishWithdraw(params) {
    return doPost(config.finishWithdraw, params);
  },
  addBulkBuy(params) {
    return doPost(config.addBulkBuy, params);
  },
  getBulkBuyList(params) {
    return doPost(config.getBulkBuyList, params);
  },
}
