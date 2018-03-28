const ADMIN = 'admin';
// 静态文件
const staticFilePath = `${ADMIN}/static/`; 
const uploadFile = `${ADMIN}/file/upload/`; 
// 登录
const login = `${ADMIN}/adminUser/login`; 
const logout = `${ADMIN}/adminUser/logout`; 
const changePassword = `${ADMIN}/adminUser/changePassword`; 
const getCheckCode = `${ADMIN}/adminUser/checkCode`; 
// 视频管理
const getVideoList = `${ADMIN}/video/selectPage`; 
const getVideoInfo = `${ADMIN}/video/selectById`; 
const getVideoUploadAuth = `${ADMIN}/video/getVideoUploadAuth`; 
const getVideoCategory = `${ADMIN}/video/getCategory`; 
const deleteVideo = `${ADMIN}/video/deleteBatch`; 
const updateVideo = `${ADMIN}/video/update`; 
// 讲师管理
const getTeacherList = `${ADMIN}/teacher/selectPage`; 
const getTeacherInfo = `${ADMIN}/teacher/select`; 
const deleteTeacher = `${ADMIN}/teacher/deleteBatch`;
const addTeacher = `${ADMIN}/teacher/insert`;
const updateTeacher = `${ADMIN}/teacher/update`;
const getTeacherRebate = `${ADMIN}/teacher/statisticRebate`;
// 课程管理
const getLessonList = `${ADMIN}/lesson/selectPage`; 
const getLessonInfo = `${ADMIN}/lesson/select`; 
const deleteLesson = `${ADMIN}/lesson/deleteBatch`;
const addLesson = `${ADMIN}/lesson/insert`;
const updateLesson = `${ADMIN}/lesson/update`;
// 课程分类
const getCategoryList = `${ADMIN}/lessonCategory/selectPage`; 
const getCategoryInfo = `${ADMIN}/lessonCategory/select`; 
const deleteCategory = `${ADMIN}/lessonCategory/deleteBatch`;
const addCategory = `${ADMIN}/lessonCategory/insert`;
const updateCategory = `${ADMIN}/lessonCategory/update`;
// 用户管理
const getUserList = `${ADMIN}/user/selectPage`; 
const getUserInfo = `${ADMIN}/user/select`; 
const deleteUser = `${ADMIN}/user/deleteBatch`;
const updateUser = `${ADMIN}/user/update`;
const lockUser = `${ADMIN}/user/lock`;
// 消费记录
const getTransactionList = `${ADMIN}/transaction/selectPage`;
// 返点
const getRebateTransactionList = `${ADMIN}/rebateTransaction/selectPage`;
const getRebateWithdrawList = `${ADMIN}/rebateWithdraw/selectPage`;
const payRebateWithdraw = `${ADMIN}/rebateWithdraw/pay`;
const finishWithdraw = `${ADMIN}/rebateWithdraw/finishWithdraw`;
// 群组
const getGroupsList = `${ADMIN}/groups/selectPage`;
const getBulkBuyGroupsList = `${ADMIN}/groups/bulkBuySelectPage`;
const getGroupsInfo = `${ADMIN}/groups/select`;
// 会员类型
const getMemberTypeList = `${ADMIN}/memberType/selectPage`;
const addMemberType = `${ADMIN}/memberType/insert`; 
const getMemberTypeInfo = `${ADMIN}/memberType/select`; 
const deleteMemberType = `${ADMIN}/memberType/deleteBatch`;
const updateMemberType = `${ADMIN}/memberType/update`;
// 统计
const getAccountStatistic = `${ADMIN}/statistic/select`;
// 配置参数
const uploadAdvertiseFile = `${ADMIN}/file/uploadAdvertiseFile`;
const uploadContactUsFile = `${ADMIN}/file/uploadContactUsFile`;
const uploadShareIconFile = `${ADMIN}/file/uploadShareIconFile`;
const getCommonParam = `${ADMIN}/commonParam/getAll`;
const updateRebateRate = `${ADMIN}/commonParam/updateRebateRate`;
const updateTeacherRebateRate = `${ADMIN}/commonParam/updateTeacherRebateRate`;
const getSendMessageList = `${ADMIN}/groups/getSendMessageList`;
const getBulkBuyList = `${ADMIN}/bulkBuy/selectList`;
const addBulkBuy = `${ADMIN}/bulkBuy/insert`;
const deleteBulkBuy = `${ADMIN}/bulkBuy/deleteBatch`;
const getBulkBuyInfo = `${ADMIN}/bulkBuy/select`;


export default {  
  getBulkBuyList,
  getBulkBuyInfo,
  addBulkBuy,
  deleteBulkBuy,
  uploadFile, 
  uploadShareIconFile,
  staticFilePath,
  login,
  logout,
  changePassword,
  getVideoList,
  getVideoInfo,
  getVideoUploadAuth,
  getSendMessageList,
  getVideoCategory,
  deleteVideo,
  updateVideo,
  getCategoryList,
  getCategoryInfo,
  deleteCategory,
  addCategory,
  updateCategory,
  getTeacherList,
  getTeacherInfo,
  deleteTeacher,
  addTeacher,
  updateTeacher,
  getLessonList,
  getLessonInfo,
  deleteLesson,
  addLesson,
  updateLesson,
  getUserList,
  getUserInfo,
  deleteUser,
  updateUser,
  lockUser,
  getTransactionList,
  getRebateTransactionList,
  getRebateWithdrawList,
  payRebateWithdraw,
  getGroupsList,
  getBulkBuyGroupsList,
  getTeacherRebate,
  getGroupsInfo,
  getMemberTypeList,
  addMemberType,
  getMemberTypeInfo,
  deleteMemberType,
  updateMemberType,
  getAccountStatistic,
  getCommonParam,
  uploadAdvertiseFile,
  updateRebateRate,
  updateTeacherRebateRate,
  uploadContactUsFile,
  getCheckCode,
  finishWithdraw,
  
}
