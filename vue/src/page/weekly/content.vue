<template>
  <div class="qxx_weekly_content">
    <xpollHeader></xpollHeader>
    <div class="content">
    </div>
    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
</style>

<script type="text/javascript">
  import xpollHeader from '@/components/header'
  import xpollFooter from '@/components/footer'

  export default {
    components: { xpollHeader, xpollFooter },
    data() {
      return {
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/weekly', {params: {'userId': v.user.id}})
      .then(r => {
        if (r.data.success) {
          if (!r.data.data.original) {
            v.talkContent = '转发: ' + r.data.data.content + '<span class="forward">' + r.data.data.talkForwardVo.content + '</span>'
          } else {
            v.talkContent = r.data.data.content
          }
        }
      })
      .catch(error => {
        console.log(error);
      });
    },
    methods: {
    }
  }
</script>
