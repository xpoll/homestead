<template>
  <div class="user_info">
    <xpollHeader></xpollHeader>
    <div class="head">
      <div class="icon">
        <div class="rotate">
          <img :src="user.avatar">
        </div>
      </div>
      <div class="info">
        <p>{{user.nick}}</p>
        <p>ID:&nbsp;{{user.id}}</p>
      </div>
    </div>
    <div class="material">
      <div class="banner">
        <span>个人资料</span>
        <router-link :to="'edit'">更新个人资料>></router-link>
      </div>
      <ul>
        <li class="all">ID:&nbsp;&nbsp;{{user.id}}</li>
        <li class="all">昵称:&nbsp;{{user.nick}}</li>
        <li class="all">等级:&nbsp;LV{{user.level}}</li>
        <li class="all">学校:&nbsp;{{infos.school}}</li>
        <li>性别:&nbsp;<img src="http://www.i8wan.com/images/touchScreen/xingbie_nan.png">{{infos.genderShow}}</li>
        <li>星座:&nbsp;<img src="http://www.i8wan.com/images/constellation/mojie.gif">{{infos.constellation}}</li>
        <li>年龄:&nbsp;{{infos.age}}</li>
        <li>生日:&nbsp;{{infos.birthday}}</li>
        <li>城市:&nbsp;{{infos.city}}</li>
      </ul>
    </div>
    <div class="home">
      <div class="banner">
        <span>家园资料</span>
      </div>
      <div class="level">
        <label>
          LV{{user.level}}
        </label>
        <div class="progress">
          <div class="progress-bar"></div>
        </div>
        <div class="add-level">升级还需3.2活跃天</div>
      </div>
      <div class="desc">
        <p>个人简介:&nbsp;{{infos.description}}</p>
      </div>
    </div>
    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
.user_info {
  color: #353535;
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
      color: #2893f5;
      padding-left: 6%;
      float: left;
    }
  }
  .material {
    background-color: #fff;
    margin-bottom: 5px;
    .banner {
      border-left: 6px solid #abcaf4;
      line-height: 30px;
      height: 30px;
      span {
        padding-left: 2%;
      }
      a {
        color: #2893f5;
        display: block;
        float: right;
        padding-right: 5%;
        text-decoration: none;
      }
    }
    ul {
      list-style: none;
      overflow: hidden;
      padding: 3%;
      margin: 0;
      li {
        float: left;
        width: 49%;
        padding-left: 1%;
        height: 25px;
        line-height: 25px;
        img {
          width: 19px;
          height: 19px;
        }
      }
      .all {
        width: 100%;
      }
    }
  }
  .home {
    background-color: #fff;
    margin-bottom: 5px;
    .banner {
      border-left: 6px solid #abcaf4;
      line-height: 30px;
      height: 30px;
      span {
        padding-left: 2%;
      }
    }
    .level {
      margin: 0 5%;
      overflow: hidden;
      border-bottom: 1px solid #abcaf4;
      line-height: 30px;
      height: 30px;
      label {
        display: block;
        float: left;
        color: #2893f5;
      }
      .progress {
        float: left;
        margin: 9px 0 0 9px;
        padding: 1px;
        border: 1px solid #000;
        height: 10px;
        border-radius: 20px;
        width: 50%;
        .progress-bar {
          background-color: #5bc0de;
          width: 80%;
          border-radius: 20px;
          height: 10px;
        }
      }
      .add-level {
        float: right;
        font-size: 5px;
      }
    }
    .desc {
      margin: 0 5%;
      overflow: hidden;
      line-height: 30px;
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
        infos: {}
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
          v.$axios.get(v.$domain + '/api/user/info')
          .then(r => {
            if (r.data.success) {
              console.info(r.data.data)
              v.infos = r.data.data
              v.infos.age = new Date().getFullYear() - v.infos.birthday.split('-')[0]
              v.infos.birthday = v.infos.birthday.split('-')[1] + '-' + v.infos.birthday.split('-')[2]
            } else {
              console.info(r.data.message)
            }
          })
          .catch(error => {
            console.log(error);
          });
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
