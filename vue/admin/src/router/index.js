import Vue from 'vue'
import Router from 'vue-router' 
import Login from '@/page/Login' 
import Main from '@/page/Main' 
import VideoManage from '@/page/video/VideoManage' 
import CategoryManage from '@/page/lesson/CategoryManage' 
import LessonManage from '@/page/lesson/LessonManage' 
import TeacherManage from '@/page/lesson/TeacherManage' 
import EditLesson from '@/page/lesson/EditLesson' 
import GroupsManage from '@/page/user/GroupsManage'
import UserManage from '@/page/user/UserManage'
import MemberTypeManage from '@/page/account/MemberTypeManage'
import TransactionManage from '@/page/account/TransactionManage'
import RebateManage from '@/page/account/RebateManage'
import RebateWithdrawManage from '@/page/account/RebateWithdrawManage'
import AccountStatistic from '@/page/account/AccountStatistic'
import CommonParamManage from '@/page/common/CommonParamManage'
import ChangePassword from '@/page/system/ChangePassword'
import GroupsMessageManage from '@/page/user/GroupsMessageManage'
import BulkBuyManage from '@/page/bulkBuy/BulkBuyManage'
import BulkBuyGroupsManage from '@/page/bulkBuy/BulkBuyGroupsManage'
import TeacherRebate from '@/page/account/TeacherRebate'

Vue.use(Router)

export default new Router({ 
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
    },
    {
      path: '/main',
      name: 'Main',
      component: Main,
      children: [
        {path: '/lessonManage', name: 'LessonManage', component: LessonManage},
        {path: '/categoryManage', name: 'CategoryManage', component: CategoryManage},
        {path: '/teacherManage', name: 'TeacherManage', component: TeacherManage},
        {path: '/editLesson', name: 'EditLesson', component: EditLesson},
        {path: '/videoManage', name: 'VideoManage', component: VideoManage},
        {path: '/userManage', name: 'UserManage', component: UserManage},
        {path: '/transactionManage', name: 'TransactionManage', component: TransactionManage},
        {path: '/rebateManage', name: 'RebateManage', component: RebateManage},
        {path: '/rebateWithdrawManage', name: 'RebateWithdrawManage', component: RebateWithdrawManage},
        {path: '/groupsManage', name: 'GroupsManage', component: GroupsManage},
        {path: '/memberTypeManage', name: 'MemberTypeManage', component: MemberTypeManage},
        {path: '/accountStatistic', name: 'AccountStatistic', component: AccountStatistic},
        {path: '/commonParamManage', name: 'CommonParamManage', component: CommonParamManage},
        {path: '/changePassword', name: 'ChangePassword', component: ChangePassword},
        {path: '/groupsMessageManage', name: 'GroupsMessageManage', component: GroupsMessageManage},
        {path: '/bulkBuyManage', name: 'bulkBuyManage', component: BulkBuyManage},
        {path: '/bulkBuyGroupsManage', name: 'bulkBuyGroupsManage', component: BulkBuyGroupsManage},
        {path: '/teacherRebate', name: 'teacherRebate', component: TeacherRebate},
      ]
    },

     
  ]
})
 
