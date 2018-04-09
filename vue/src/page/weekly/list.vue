<template>
  <div class="qxx_weekly_show">
    <xpollHeader></xpollHeader>
    <div class="content">

      <el-table
      :data="weekly"
      style="width: 100%"><!--
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
                <vue-preview :slides="props.row.slide" @close="handleClose"></vue-preview>
            </el-form>
          </template>
        </el-table-column> -->
        <el-table-column
          sortable
          fixed
          prop="name"
          label="名称"
          width="250">
        </el-table-column>
        <el-table-column
          sortable
          prop="fsid"
          label="编号"
          width="180">
        </el-table-column>
        <el-table-column
          prop="start"
          label="start"
          width="80">
        </el-table-column>
        <el-table-column
          prop="end"
          label="end"
          width="80">
        </el-table-column>
        <el-table-column
          sortable
          prop="createTime"
          label="收录时间"
          width="180">
        </el-table-column>
        <el-table-column
          label="操作"
          width="1000">
          <template slot-scope="scope">
            <vue-preview :slides="scope.row.slide" @close="handleClose"></vue-preview>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <xpollFooter></xpollFooter>
  </div>
</template>

<style type="text/css" lang="scss">
.my-gallery{
  width: 100%;
  figure {
    display: block;
    float: left;
    margin: 0;
    img {
    margin: auto 2px;
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
        weekly: null
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/weekly/list', {params: {'type': v.id}})
      .then(r => {
        console.info(r.data)
        v.weekly = r.data.data
        for(var i=0; i<v.weekly.length; i++) {
          v.weekly[i].slide = []
          for (var j = 0; j <= v.weekly[i].end; j++) {
            var obj = new Object()
            obj.src = 'http://q.img.blmdz.cn/fhzk/' + v.weekly[i].fsid + '-' + j + '.jpg'
            obj.alt = j
            obj.w = 1003
            obj.h = 1314
            v.weekly[i].slide.push(obj)
          }
        }
      })
      .catch(error => {
        console.log(error);
      });
    },
    methods: {
      handleClose () {
        console.log('close event')
      }
    }
  }
</script>
