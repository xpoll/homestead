<template>
  <div class="qxx_index">
    <xpollHeader></xpollHeader>
    <div class="content">
      <router-link :to="'/s'">1. 百度云真实链接解析</router-link>
      <a href="http://xn.blmdz.cn/xpoll/dy.html">2. dy2018谷歌自定义搜索</a>
      <router-link :to="'/draw'">3. 绘画</router-link>
      <router-link :to="'/w'">4. 杂志周刊</router-link>
      <a href="http://xn.blmdz.cn">5. 虚拟主机目录</a>
      <a href="http://xpoll.blmdz.cn">6. 博客地址</a>
      <router-link :to="'/file'">测试. file</router-link>
    </div>
    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
.qxx_index {
  .content {
    height: 300px;
    display: flex;
    flex-direction: column;
    a {
      display: table;
    }
  }
}
</style>

<script type="text/javascript">
  import xpollHeader from '@/components/header'
  import xpollFooter from '@/components/footer'

  export default {
    components: { xpollHeader, xpollFooter },
    data() {
      return {
        user: {}
      }
    },
    created() {
    },
    methods: {
    }
  }
</script>
