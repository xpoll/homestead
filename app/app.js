//app.js
App({
  onLaunch: function () {
    // //调用API从本地缓存中获取数据
    // var logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)
    // wx.login({
    //   success: function (res) {
    //     console.info(res)
    //   }
    // });
  },
  getUserInfo:function(cb){
    var that = this
    if (this.globalData.user){
      typeof cb == "function" && cb(this.globalData.user)
    } else {
      //调用登录接口
      wx.login({
        success: function (res) {
          var code = res.code
          wx.getUserInfo({
            success: function (res) {
              var iv = res.iv
              var encryptedData = res.encryptedData
              wx.request({
                url: that.globalData.httpDomain + '/api/applet/auth',
                data: {
                  code: code,
                  iv: iv,
                  encryptedData: encryptedData
                },
                header: {
                  "Content-Type": "application/x-www-form-urlencoded"
                },
                method: 'POST',
                success: function (res) {
                  that.globalData.user = res.data.data
                  cb(that.globalData.user)
                },
                fail: function (res) { },
                complete: function (res) { }
              });
            }
          })
        }
      })
    }
  },
  globalData:{
    user: null,
    // wsDomain: 'ws://127.0.0.1:8082',
    // httpDomain: 'http://127.0.0.1:8082'
    wsDomain: 'ws://blmdz.cn',
    httpDomain: 'http://blmdz.cn'
  }
})