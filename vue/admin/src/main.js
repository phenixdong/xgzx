// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import { VueEditor } from 'vue2-editor'
import 'babel-polyfill'

Vue.use(ElementUI)

Vue.config.productionTip = false

//fetch
import fetch from './fetch/index.js'
Vue.prototype.$fetch =  Vue.$fetch = fetch;

import filters from './filter/index.js'
Vue.use(filters);


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
