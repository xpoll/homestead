<template>
  <div class="user_index">
    <xpollHeader></xpollHeader>
    <div class="head">
      <div class="icon">
        <div class="rotate">
          <img :src="user.avatar">
        </div>
      </div>
      <div class="info">
        <div class="left">
          <span class="nick"><router-link :to="'/o/' + user.id">{{user.id}}</router-link>&nbsp;{{user.nick}}</span>
          <span class="level">LV{{user.level}}</span>
        </div>
      </div>
      <div @click="talk" class="info talk">
        <span>TALK:</span>
        <span v-html="talkContent"></span>
      </div>
    </div>
    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
.user_index {
  .head {
    background-color: #fff;
    padding: 3%;
    position: relative;
    overflow: hidden;
    margin-bottom: 5px;
    .icon {
      width: 25%;
      float: left;
      .rotate, .rotate img {
        border: 1px solid #bcd5f6;
        box-shadow: 0 0 1px 1px #bcd5f6;
        transform: rotate(12deg);
        width: 100px;
        height: 100px;
      }
      .rotate img {
        transform: rotate(-12deg);
      }
    }
    .info {
      padding-left: 6%;
      float: left;
      width: 65%;
      .left {
        width: 50%;
        float: left;
        .nick {
          color: #000;
          display: block;
          margin: 15px auto 10px;
          a {
            text-decoration: none;
            color: black;
          }
        }
        .level {
          margin-top: 10px;
          border-radius: 9px;
          padding: 1px 2px;
          display: inline;
          box-shadow: 0 0 1px 1px #bcd5f6;
          border: 1px solid #ccc;
          font-size: 10px;
          font-weight: bold;
          color: #ccc;
        }
      }
    }
    .talk {
      padding-top: 3px;
    }
    .forward {
      border: 1px solid #ccc;
      margin: 2px;
      display: block;
      width: 100%;
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
        id: this.$route.params.id,
        user: {},
        talkContent: null
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/user/base', {params: {'userId': v.id}})
      .then(r => {
        if (r.data.success) {
          v.user = r.data.data
        }
      })
      .catch(error => {
        console.log(error);
        v.$router.push({ path: '/login' })
      });
      v.$axios.get(v.$domain + '/api/talk/first', {params: {'userId': v.id}})
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
      talk(e) {
        this.$router.push({ path: '/o/t/' + this.id })
      }
    }
  }
</script>