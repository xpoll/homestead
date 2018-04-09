// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import VueRouter from 'vue-router'

import App from './App.vue'

import routes from './config/routes'
import util from './config/index'

import axios from 'axios'

import VuePreview from 'vue-preview'

import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'
Vue.use(ElementUI)
// import { Form, FormItem, Input, Button, Loading, Message, Alert } from 'element-ui';
// Vue.use(Form)
// Vue.use(FormItem)
// Vue.use(Input)
// Vue.use(Button)
// Vue.use(Alert)


// Vue.use(VuePreview)

// with parameters install
Vue.use(VuePreview, {
  mainClass: 'pswp--minimal--dark',
  barsSize: {top: 0, bottom: 0},
  captionEl: false,
  fullscreenEl: false,
  shareEl: false,
  bgOpacity: 0.85,
  tapToClose: true,
  tapToToggleControls: false
})

axios.defaults.withCredentials=true

Vue.use(VueRouter)
Vue.prototype.$axios = axios
Vue.prototype.$util = util
Vue.prototype.$domain = ''
// Vue.prototype.$domain = 'http://127.0.0.1:8082'
// Vue.prototype.$domain = 'http://blmdz.cn'

const router = new VueRouter({
  routes
})

new Vue({
  router,
  el: '#app',
  render: (h) => h(App)
})
