<template>
  <div class="qxx_user_new">
    <!-- <xpollHeader></xpollHeader> -->
    <section class="desc">
      <h1>音雨泪蝶之殇</h1>
      <p>牵起的那一刻，便注定了永远！</p>
    </section>

    <el-button type="text" @click="dialogVisible=true">点击打开 Dialog</el-button>
    <!-- 发布 -->
    <!-- 转发 -->
    <!-- 回复 -->
    <!-- 评论 -->
    <!-- 赞   -->
    <el-dialog
      title=""
      :visible.sync="dialogVisible"
      width="60%">
      <span>回复:</span>
      <div contenteditable="true" class="qxx_reply_div"></div>
    </el-dialog>


    <section class="head">
      <img src="" width="120" height="120">
      <div class="content">
        <span v-if="user">{{user.nick}}</span>
        <div>其他</div>
        <div class="util">
          <el-tabs tab-position="top" value="a">
            <el-tab-pane name="a" label="主页"></el-tab-pane>
            <el-tab-pane name="b" label="日志"></el-tab-pane>
            <el-tab-pane name="c" label="相册"></el-tab-pane>
            <el-tab-pane name="d" label="留言板"></el-tab-pane>
            <el-tab-pane name="e" label="说说"></el-tab-pane>
            <el-tab-pane name="f" label="个人档"></el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </section>

    <section class="body">
      <el-menu default-active="1" class="left">
        <el-menu-item index="1">
          <i class="el-icon-location"></i>
          <span slot="title">导航一</span>
        </el-menu-item>
        <el-menu-item index="2">
          <i class="el-icon-menu"></i>
          <span slot="title">导航二</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-setting"></i>
          <span slot="title">导航三</span>
        </el-menu-item>
      </el-menu>


      <div class="middle">
        <el-card v-for="item in talks" :key="item.key">
          <div slot="header" class="head">
            <img src="../../assets/lm.png" width="30px" height="30px">
            <div class="show">
              <span>{{item.nick}}</span>
              <time>{{item.createTime}}</time>
            </div>
          </div>
          <div class="font">
            {{item.content}}
          </div>
          <!--
            这里要区分几张图片分别做适应
            1图 -- 100%
            2图 -- 50%
            3图 -- 33%
            3+图 -- 33%
            9+图 -- 33% + 样式
           -->
          <div class="image">
            <!-- <img src="../../assets/eraser-background.png"> -->
          </div>
          <div class="util">
            <span @click="open">
              <el-badge :value="item.forward" class="item">
                <el-tag size="mini" type="info">转发</el-tag>
                <!-- <el-tag size="mini" type="success">转发</el-tag> -->
              </el-badge>
            </span>
            <span @click="open">
              <el-badge :value="item.reply" class="item">
                <el-tag size="mini" type="info">回复</el-tag>
                <!-- <el-tag size="mini" type="warning">回复</el-tag> -->
              </el-badge>
            </span>
            <span @click="open">
              <el-badge :value="item.like" class="item">
                <!-- <el-tag size="mini" type="info">赞</el-tag> -->
                <el-tag size="mini" type="danger">赞</el-tag>
              </el-badge>
            </span>
          </div>

          <div class="reply_content" v-if="item.replys != null">
            <div v-for="reply in item.replys" :key="reply.key">
              <div>
                {{reply.nick}} : {{reply.content}}
              </div>
              <div v-for="replyy in reply.replys" :key="replyy.key">
                &nbsp;&nbsp;&nbsp;&nbsp;{{replyy.nick}} 回复 {{replyy.replayToNick}} : {{replyy.content}}
              </div>
            </div>
          </div>

          <div class="reply">
            <el-input></el-input>
            <el-button round size="small">回复</el-button>
          </div>
        </el-card>
      </div>
      <el-menu default-active="1" class="right">
        <el-menu-item index="1">
          <i class="el-icon-location"></i>
          <span slot="title">导航一</span>
        </el-menu-item>
        <el-menu-item index="2">
          <i class="el-icon-menu"></i>
          <span slot="title">导航二</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-setting"></i>
          <span slot="title">导航三</span>
        </el-menu-item>
      </el-menu>
    </section>

    <!-- <xpollFooter></xpollFooter> -->
  </div>
</template>

<style type="text/css" lang="scss">
.qxx_user_new {
  div, h1, p, img, h2, section {
    // border: 1px solid #ccc;
  }

  .qxx_reply_div {
    width: 100%;
    height: 200px;
    padding: 10px;
    border: 1px solid #ccc;
  }

  .head {
    display: flex;

    .content {
      display: inline-grid;
      align-content: space-between;
      width: 100%;
      padding-left: 10px;
      .util {
        position: relative;
        bottom: -0.5rem;
        .el-tabs__nav-wrap::after {
          background-color: rgba(0, 0, 0, 0);
        }
      }
    }
  }
  .body {
    display: flex;
    justify-content: space-between;

    .left {
      width: 200px;
    }
    .middle {
      padding: 0 20px;
      min-width: 400px;
      width: -webkit-fill-available;
      .el-card {
        padding: 5px;
        margin-bottom: 10px;
      }

      .el-card__body, .el-card__header {
        border: 0;
        padding: 0;
      }

      .head {
        display: flex;
        padding-bottom: 3px;
        img {
          width: 40px;
          height: 40px;
          border-radius: 50%;
        }
        .show {
          display: inline-grid;
          span {
            padding-left: 5px;
          }
          time {
            padding-left: 5px;
            font-size: 0.5rem;
            color: #ccc;
          }
        }
      }
      .font {
        font-size: 0.9rem;
      }
      .image {

      }
      .util {
        text-align: right;
        padding: 10px;
        font-size: 0.8rem;
        color: #ccc;
        .item {
          margin-right: 10px;
        }
      }
      .reply_content {
        border-top: 1px solid #eee;
        font-size: 0.7rem;
        line-height: 1rem;
      }
      .reply {
        display: flex;
        padding: 5px;
        .el-input {
          width: auto;
        }
        input {
          width: 100%;
        }
        input:focus {
          width: 300px;
        }
      }
    }
    .right {
      width: 250px;
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
        talks: [],
        dialogVisible: false,
        user: null
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
          this.refresh()
        }
      })
      .catch(error => {
        console.log(error);
      });
    },
    methods: {
      refresh(e) {
        var v = this
        var page = new Object()
        var search = new Object()
        search.userId = v.user.id
        page.mode = search
        page.size = 10
        page.num = 1
        v.$axios.post(v.$domain + '/api/talk/page', page)
        .then(r => {
          if (r.data.success) {
            v.talks = r.data.data.data
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      open(e) {
        console.log(111);
      }
    }
  }
</script>
