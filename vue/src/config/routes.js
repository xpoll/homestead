import index from '@/page/index.vue'
import login from '@/page/login.vue'
import register from '@/page/register.vue'

import Frame from '@/frame/subroute.vue'

import user from '@/page/user/index.vue' // 我的信息
import userNew from '@/page/user/new.vue' // 我的信息
import userInfo from '@/page/user/info.vue' // 我的资料

import talk from '@/page/talk/index.vue' // 我的TALK

import otherIndex from '@/page/o/index.vue' // 他人眼中的我
import otherTalk from '@/page/o/talk.vue' // 他人眼中的我的TALK

import file from '@/page/file/index.vue'

import draw from '@/page/draw/index.vue'

import bs from '@/page/bs/index.vue'

import s from '@/page/baiduyun/baiduyun.vue'

import w from '@/page/weekly/show.vue'

import wl from '@/page/weekly/list.vue'

import wlc from '@/page/weekly/content.vue'


export default [
  {
    path: '/',
    component: index
  },
  {
    path: '/index',
    component: index
  },
  {
    path: '/login',
    component: login
  },
  {
    path: '/register',
    component: register
  },
  {
    path: '/w',
    component: Frame,
    children: [
      {path: '', component: w},
      {path: ':id', component: wl},
      {path: 'c:id', component: wlc}
    ]
  },
  // {
  //   path: '/user',
  //   component: Frame,
  //   children: [
  //     {path: '', component: userNew},
  //     {path: 'user', component: user},
  //     {path: 'info', component: userInfo}
  //   ]
  // },
  // {
  //   path: '/talk',
  //   component: Frame,
  //   children: [
  //     {path: '', component: talk}
  //   ]
  // },
  // {
  //   path: '/o',
  //   component: Frame,
  //   children: [
  //     // {path: '', component: user},
  //     {path: ':id', component: otherIndex},
  //     {path: 't/:id', component: otherTalk}
  //   ]
  // },
  {
    path: '/file',
    component: file
  },
  {
    path: '/draw',
    component: draw
  },
  // {
  //   path: '/bs',
  //   component: bs
  // },
  {
    path: '/s',
    component: s
  },
  {
    path: '*',
    component: login
  }
]
