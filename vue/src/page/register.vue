<template>
  <div class="qxx_register">
    <QxxHeader></QxxHeader>

    <section class="qxx-p-0">
      <el-form :model="formRegister" :rules="rulesRegister" ref="formRegister" label-width="70px" class="demo-ruleForm">

        <el-form-item label="账号" prop="name">
          <el-input type="text" placeholder="请输入账号." v-model="formRegister.name" auto-complete="off" clearable :maxlength="20"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="pwd">
        <el-input type="password" placeholder="请输入密码." v-model="formRegister.pwd" auto-complete="off" clearable :maxlength="20"></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="code" class="code">
          <el-input type="text" placeholder="请输入验证码." v-model="formRegister.code" auto-complete="off" clearable :maxlength="4"></el-input>
          <img :src="captcha" @click="captchas">
        </el-form-item>

        <el-form-item class="qxx_form_btn qxx-m-0">
          <el-button size="small" @click="register">注册</el-button>
        <router-link :to="'login'">登陆</router-link>
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

    <QxxFooter></QxxFooter>
  </div>
</template>

<style type="text/css" lang="scss">
  .qxx_register {
    form {
      width: 300px;

      .code {
        // 
        div {
          display: flex;
          input {
            width: 100px;
          }
        }
      }
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
  import QxxHeader from '@/components/header'
  import QxxFooter from '@/components/footer'

  export default {
    components: { QxxHeader, QxxFooter },
    data() {
      var validateName = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入账号.'))
        else callback()
      };

      var validatePwd = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入密码.'))
        else callback()
      };

      var validateCode = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入验证码.'))
        else if (value.length != 4) callback(new Error('验证码格式不正确.'))
        else callback()
      };

      return {
        formRegister: {
          name: null,
          pwd: null,
          code: null
        },
        rulesRegister: {
          name: [{ validator: validateName, trigger: 'blur' }],
          pwd: [{ validator: validatePwd, trigger: 'blur' }],
          code: [{ validator: validateCode, trigger: 'blur' }]
        },
        error: null,
        captcha: null
      }
    },
    mounted() {
    },
    created() {
      this.captchas()
    },
    methods: {
      captchas() {
        this.captcha = this.$domain + '/img?d='+Math.random()
      },
      register() {
        var v = this
        var valid = false
        v.$refs.formRegister.validate($valid => valid = $valid)
        if (!valid) return false

        const loading = this.$util.load(1)

        v.$axios.post(v.$domain + '/api/user/registered', v.formRegister)
        .then(r => {
          loading.close()
          if (!r.data.success) {
            v.captchas()
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
