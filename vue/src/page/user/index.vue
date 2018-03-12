<template>
  <div class="user_index">
    <xpollHeader></xpollHeader>
    <div class="head">
      <div class="icon">
        <div class="rotate">
          <router-link :to="'/draw'">
            <img :src="user.avatar">
          </router-link>
        </div>
      </div>
      <div class="info">
        <div class="left">
          <span class="nick"><router-link :to="'/o/' + user.id">{{user.id}}</router-link>&nbsp;<router-link :to="'user/info'">{{user.nick}}</router-link></span>
          <span class="level">LV{{user.level}}</span>
        </div>
        <div class="right"> 
          <div class="sign">
            <div v-if="!sign">
              <span @click="signs">签到&nbsp;></span>
            </div>
            <div v-else>
              <span>已签</span>
            </div>
          </div>
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
      .right {
        margin-top: 15px;
        float: right;
        padding: 1px 2px;
        font-size: 11px;
        border-radius: 9px;
        border: 1px solid #ccc;
        color: #888;
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
        user: {},
        talkContent: null,
        sign: true
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/user')
      .then(r => {
        if (!r.data.success) {
          v.$router.push({ path: '/login' })
        } else {
          v.user = r.data.data
          v.sign = v.$util.sign(v.user.signDate)

          v.$axios.get(v.$domain + '/api/talk/first', {params: {'userId': v.user.id}})
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
        }
      })
      .catch(error => {
        console.log(error);
        v.$router.push({ path: '/login' })
      });
    },
    methods: {
      signs(e) {
        var v = this
        v.$axios.get(v.$domain + '/api/user/sign')
        .then(r => {
          console.log(r.data);
          if (r.data.success) {
            v.sign = true
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      talk(e) {
        this.$router.push({ path: '/talk' })
      }
    }
  }
</script>
