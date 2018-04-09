<template>
  <div class="qxx_baiduyun">
    <xpollHeader></xpollHeader>
    <el-alert
    title="tips:"
    type="success"
    description="tips">
    <div>
      1. key格式为https://pan.baidu.com/s/14fo5FWeNfpa8CXZk8v9EUA或https://pan.baidu.com/s/1后部分4fo5FWeNfpa8CXZk8v9EUA<br>
      2. 网盘内文件尽量不要过多避免超时<br>
      3. 请复制链接至下载工具下载，有效时间为8h<br>
      4. 因网盘限制，速度为500kb（实测400kb-500kb）互不影响<br>
      5. 征集意见<br>
    </div>
  </el-alert>
    <section class="qxx-p-0">
      <el-form :model="formVo" :rules="rulesVo" ref="formVo" label-width="50px">

        <el-form-item>
          <el-switch
            v-model="formVo.encryption"
            active-text="加密"
            inactive-text="公开">
          </el-switch>
        </el-form-item>

        <el-form-item label="key" prop="key">
          <el-input type="text" placeholder="hi,i'm key..." v-model="formVo.key" auto-complete="off" clearable :minlength="50"></el-input>
        </el-form-item>

        <el-form-item label="pwd" prop="pwd" v-if="formVo.encryption">
          <el-input type="password" placeholder="(/▽╲..." v-model="formVo.pwd" auto-complete="off" clearable :minlength="4"></el-input>
        </el-form-item>

        <el-form-item class="qxx_form_btn qxx-m-0">
          <el-button size="small" @click="getinfo">꒰╬•᷅д•᷄╬꒱</el-button>
        </el-form-item>

        <el-form-item>
          <el-alert
            :title="error"
            type="error"
            @close="errorClose"
            show-icon
            v-if="error != null"
            >
          </el-alert>
        </el-form-item>

      </el-form>

      <el-table
      v-if="tableData"
      :data="tableData"
      style="width: 100%">
        <el-table-column
          sortable
          prop="fsId"
          label="编号"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          prop="b"
          label="标签"
          width="100">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.dir ? 'primary' : 'success'"
              close-transition>{{scope.row.dir ? '文件夹' : '文件'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          prop="name"
          label="名称"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          prop="sizeShow"
          label="大小"
          width="180">
        </el-table-column>
        <el-table-column
          sortable
          prop="path"
          label="路径"
          width="360">
        </el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button
              v-if="!scope.row.dir"
              size="mini"
              type="danger"
              class="cp"
              :data-clipboard-text="scope.row.link">复制</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <xpollFooter></xpollFooter>
  </div>
</template>
<style type="text/css" lang="scss">
  .qxx_baiduyun {
    section {
      padding: 10px;
      form {
        text-align: center;
        display: flex;
        .el-input {
          margin-left: 2px;
        }
      }

    }
  }
</style>

<script type="text/javascript">
  import xpollHeader from '@/components/header'
  import xpollFooter from '@/components/footer'
  import Clipboard from 'clipboard'
  let clipboard = new Clipboard('.cp')

  export default {
    components: { xpollHeader, xpollFooter },
    data() {
      var validateName = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入key.'))
        else callback()
      };

      var validatePwd = (rule, value, callback) => {
        if (value == '' || value == null) callback(new Error('请输入密码.'))
        else callback()
      };
      return {
        tableData: null,
        formVo: {
          encryption: false,
          key: null,
          pwd: null
        },
        rulesVo: {
          key: [{ validator: validateName, trigger: 'blur' }],
          pwd: [{ validator: validatePwd, trigger: 'blur' }]
        },
        error: null
      }
    },
    created() {
    },
    methods: {
      handlecp(scope) {
        console.info(scope.row.f)
        Clipboard.setData("Text",scope.row.f);
      },
      getinfo() {
        var v = this
        v.tableData = null
        var valid = false
        v.error = null
        v.$refs.formVo.validate($valid => valid = $valid)
        if (!valid) return false

        const loading = this.$util.load(1)

        v.$axios.post(v.$domain + '/api/s', v.formVo)
        .then(r => {
          loading.close()
          console.info(r.data)
          if (!r.data.success) {
            v.error = r.data.message
          } else {
            v.tableData = r.data.data
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
