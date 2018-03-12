// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import VueRouter from 'vue-router'

import App from './App.vue'

import routes from './config/routes'
import util from './config/index'

import axios from 'axios'

import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'
Vue.use(ElementUI)
// import { Form, FormItem, Input, Button, Loading, Message, Alert } from 'element-ui';
// Vue.use(Form)
// Vue.use(FormItem)
// Vue.use(Input)
// Vue.use(Button)
// Vue.use(Alert)


axios.defaults.withCredentials=true

Vue.use(VueRouter)
Vue.prototype.$axios = axios
Vue.prototype.$util = util
Vue.prototype.$domain = 'http://127.0.0.1:8082'
Vue.prototype.$domain = 'http://blmdz.cn'
Vue.prototype.$domain = ''

const router = new VueRouter({
  routes
})

new Vue({
  router,
  el: '#app',
  render: (h) => h(App)
})
