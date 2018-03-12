<template>
  <div class="qxx_login">
    <xpollHeader></xpollHeader>

    <section class="qxx-p-0">
      <el-form :model="formLogin" :rules="rulesLogin" ref="formLogin" label-width="50px">

        <el-form-item label="账号" prop="name">
          <el-input type="text" placeholder="请输入账号..." v-model="formLogin.name" auto-complete="off" clearable :minlength="20"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="pwd">
          <el-input type="password" placeholder="请输入密码..." v-model="formLogin.pwd" auto-complete="off" clearable :minlength="20"></el-input>
        </el-form-item>

        <el-form-item class="qxx_form_btn qxx-m-0">
          <el-button size="small" @click="login">登陆</el-button>
        <router-link :to="'register'">注册</router-link>
        </el-form-item>

        <el-alert
          :title="error"
          type="error"
          @close="errorClose"
          show-icon
          v-if="error != null"
          >
        </el-alert>

      </el-form>
    </section>

    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
  .qxx_login {
    form {
      width: 300px;
      .qxx_form_btn {
          div {
            margin: 0 auto !important;
            text-align: center;
          }
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
      var validateName = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入账号.'))
        else callback()
      };

      var validatePwd = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入密码.'))
        else callback()
      };

      return {
        formLogin: {
          name: 'blmdz521',
          pwd: 'blmdz520'
        },
        rulesLogin: {
          name: [{ validator: validateName, trigger: 'blur' }],
          pwd: [{ validator: validatePwd, trigger: 'blur' }]
        },
        error: null
      }
    },
    created() {
      var v = this
    },
    methods: {
      login() {
        var v = this
        var valid = false
        v.$refs.formLogin.validate($valid => valid = $valid)
        if (!valid) return false

        const loading = this.$util.load(1)

        v.$axios.post(v.$domain + '/api/user/login', v.formLogin)
        .then(r => {
          loading.close()
          if (!r.data.success) {
            v.error = r.data.message
          } else {
            v.$router.push({ path: '/user' })
          }
        })
        .catch(error => {
          loading.close()
          v.$util.msg('error', '连接超时')
        });
      },
      errorClose() {
        this.error = null
      }
    }
  }
</script>
