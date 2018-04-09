<template>
  <div class="qxx_weekly_show">
    <xpollHeader></xpollHeader>
    <div class="content">
      <div v-for="item in weekly" :key="item.key">
        <router-link :to="'/w/' + item.key">{{item.value}}</router-link>
      </div>
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
        weekly: []
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/weekly')
      .then(r => {
        console.info(r.data)
        v.weekly = r.data.data
      })
      .catch(error => {
        console.log(error);
      });
    },
    methods: {
    }
  }
</script>
