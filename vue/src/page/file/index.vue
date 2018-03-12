<template>
  <div>
    <span class="hide">
      <input type="file" ref="file" @change="fileChange">
    </span>
    <button @click="fileEvent">选择文件</button>
    <br>
    <div class="img">
      <img :src="href">
    </div>
  </div>
</template>

<style type="text/css" lang="scss">
  .hide {
    display: none;
  }
  .img {
    width: 100%;
    img {
      width: 100%;
    }
  }
</style>

<script type="text/javascript">
  export default {
    data() {
      return {
        href: null
      }
    },
    created() {
      var v = this
      // v.$axios.delete(v.$domain + '/api/file/del/21')
      // .then(r => {
      //   console.log(r);
      // })
      // .catch(error => {
      //   console.log(error);
      // });
      // var page = new Object()
      // page.mode = 8
      // v.$axios.post(v.$domain + '/api/file/page', page)
      // .then(r => {
      //   if (r.data.success) {
      //     console.info(r.data.data.data)
      //   }
      // })
      // .catch(error => {
      //   console.log(error);
      // });
    },
    methods: {
      upload(file) {
        var v = this
        var data = new FormData()
        data.append('file', file)
        v.$axios.post(v.$domain + '/api/file', data, {headers:{'Content-Type':'multipart/form-data'}})
        .then(r => {
          console.info(r.data)
          if (r.data.success) {
            v.href = r.data.data.path
          }
        })
        .catch(error => {
          console.log(error);
        });
      },
      fileChange(e) {
        var v = this
        if (v.$refs.file.files[0] == undefined) return
        v.upload(v.$refs.file.files[0])
      },
      fileEvent(e) {
        this.$refs.file.value = null
        this.$refs.file.click()
      }
    }
  }
</script>