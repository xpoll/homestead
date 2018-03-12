import { Loading, Message } from 'element-ui';

export default {
  getTime(str) {
    if (str == null || str == '' || str == 'undefined') return ''
    return '发布于: ' + str.replace('T', ' ').split('.')[0]
  },
  sign(str) {
    var date = new Date()
    var d_s = date.getFullYear() + '-' + (date.getMonth() < 9 ? ('0' + (date.getMonth() + 1)) : date.getMonth() + 1) + '-' + (date.getDate() < 10 ? ('0' + date.getDate()) : date.getDate())
    return d_s === str
  },
  load(type) {
    const option =
      type === 1 ?
        { fullscreen: true } :
          type === 2 ?
            {
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            } : null
    return Loading.service(option);
  },
  msg(type, msg) {
    Message({
      showClose: true,
      message: msg,
      type: type
    });
  }
}
