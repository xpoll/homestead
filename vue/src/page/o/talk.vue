<template>
  <div>
    <!-- <xpollHeader></xpollHeader> -->
    <h4>现在全是提交后刷新，没有做交互</h4>
    <div v-for="item in talks">
      <span v-text="item.nick"></span>
      <time v-text="item.createTime"></time>
      <p v-text="item.content"></p>
      <div class="quote" v-if="!item.original">
        <router-link :to="'/o/' + item.talkForwardVo.userId">
          <span v-text="item.talkForwardVo.nick"></span>
        </router-link>: 
        <time v-text="item.talkForwardVo.createTime"></time>
        <p v-text="item.talkForwardVo.content"></p>
      </div>
      <div>
        <span class="alert-div-forward" @click="forward1(item.id, item.nick, item.content)">转发(<span v-text="item.forward"></span>)</span>
        <span>回复(<span v-text="item.reply"></span>)</span>
        <span @click="like(item.id)">赞(<span v-text="item.like"></span>)</span>
      </div>
      <div v-for="replys in item.replys">
        <div class="alert-div-reply" @click="reply2(item.id, replys.id, replys.userId, replys.nick)">
          <span class="alert-div-reply" v-text="replys.createTime"></span>
          <router-link :to="'/o/' + replys.userId">
            <span class="alert-div-reply" v-text="replys.nick"></span>
          </router-link>: 
          <span class="alert-div-reply" v-text="replys.content"></span>
        </div>
        <div class="alert-div-reply" v-for="reply in replys.replys"  @click="reply2(item.id, replys.id, reply.userId, reply.nick)">
          <span class="alert-div-reply" v-text="reply.createTime"></span>
          <router-link :to="'/o/' + reply.userId">
            <span class="alert-div-reply" v-text="reply.nick"></span>
          </router-link> 回复
          <router-link :to="'/o/' + reply.replyToId">
            <span class="alert-div-reply" v-text="reply.replayToNick"></span>
          </router-link>: 
          <span class="alert-div-reply" v-text="reply.content"></span>
        </div>
      </div>
      <div>
        <input type="text" name="reply" placeholder="评论" value=""><button @click="reply1($event, item.id)">评论</button>
      </div>
      <hr>
    </div>
    <div class="alert-div-reply alert-div" v-if="talkReply.flag">
      <span class="alert-div-reply">回复:</span>
      <input class="alert-div-reply" type="text" :placeholder="talkReply.placeholder" v-model="talkReply.content"><button class="alert-div-reply" @click="reply3">回复</button>
    </div>
    <div class="alert-div-forward alert-div" v-if="talkForward.flag">
      <span class="alert-div-forward">转发:</span>
      <textarea class="alert-div-forward" type="text" placeholder="转发" ref="ref_forward" v-model="talkForward.content" rows="10" cols="40"></textarea>
      <br>
      <label class="alert-div-forward">私密</label>
      <select class="alert-div-forward" v-model="talkForward.privacy">
        <option value="0">公开</option>
        <option value="1">好友可见</option>
        <option value="2">自己可见</option>
      </select>
      <br>
      <button class="alert-div-forward" @click="forward">转发</button>
    </div>
    <!-- <xpollFooter></xpollFooter> -->
  </div>
</template>

<style type="text/css" lang="scss">
.quote {
  margin: 0 5%;
  border: 1px solid #ccc;
}
.alert-div {
  position: absolute;
  top: 50%;
  width: 80%;
  left: 5%;
  z-index: 1;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
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
        talkForward: {
          flag: false,
          talkId: null,
          privacy: 0,
          content: null
        },
        talks: [],
        userId: null,
        talkReply: {
          flag: false,
          placeholder: '回复',
          talkId: null,
          superId: null,
          replyToId: null,
          content: null
        }
      }
    },
    created() {
      var v = this
      let html = document.querySelector('html')
      html.addEventListener('click', (e)=>{
        if (e.target.classList.contains('alert-div-reply') != true) v.talkReply.flag = false
        if (e.target.classList.contains('alert-div-forward') != true) v.talkForward.flag = false
      }, false)

      v.$axios.get(v.$domain + '/api/user')
      .then(r => {
        if (!r.data.success) {
          v.$router.push({ path: '/login' })
        } else {
          v.userId = r.data.data.id
          v.refresh()
        }
      })
      .catch(error => {
        console.log(error);
        v.$router.push({ path: '/login' })
      });
    },
    methods: {
      forward1(id, nick, content) {
        var v = this
        if (v.talkForward.talkId != id) {
          v.talkForward.content = '//@' + nick + ':' + content
        }
        v.talkForward.talkId = id
        v.talkForward.flag = true
      },
      forward() {
        var v = this
        var forward = new Object()
        forward.talkId = v.talkForward.talkId
        forward.privacy = v.talkForward.privacy
        forward.content = v.talkForward.content
        v.$axios.post(v.$domain + '/api/talk/forward', forward)
        .then(r => {
          if (r.data.success) {
            v.refresh()
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      reply1(e, talkId) {
        this.reply(talkId, null, null, e.target.parentNode.getElementsByTagName("input")[0].value)
      },
      reply2(talkId, superId, replyToId, nick) {
        var v = this
        v.talkReply.flag = true
        v.talkReply.placeholder = nick

        if (v.talkReply.superId != superId || v.talkReply.replyToId != replyToId) {
          v.talkReply.content = null
        }
        v.talkReply.talkId = talkId
        v.talkReply.superId = superId
        v.talkReply.replyToId = replyToId
      },
      reply3(e) {
        this.reply(this.talkReply.talkId, this.talkReply.superId, this.talkReply.replyToId, this.talkReply.content)
      },
      reply(talkId, superId, replyToId, content) {
        var v = this
        var reply = new Object()
        reply.talkId = talkId
        reply.superId = superId
        reply.replyToId = replyToId
        reply.content = content
        v.$axios.post(v.$domain + '/api/talk/reply', reply)
        .then(r => {
          if (r.data.success) {
            v.refresh()
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      refresh(e) {
        var v = this
        var page = new Object()
        var search = new Object()
        search.userId = v.id
        page.mode = search
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
      like(e) {
        var v = this
        v.$axios.get(v.$domain + '/api/talk/like', {params: {'talkId': e}})
        .then(r => {
          if (r.data.success) {
            v.refresh()
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      unlike(e) {
        var v = this
        v.$axios.get(v.$domain + '/api/talk/unlike', {'talkId': e})
        .then(r => {
          if (r.data.success) {
            v.refresh()
          }
        })
        .catch(error => {
          console.log(error);
        });
      }
    }
  }
</script>
