//draw.js
var app = getApp()
Page({
  data: {
  },
  onLoad: function () {
    var socketOpen = false
    wx.connectSocket({
      url: app.globalData.wsDomain + '/endpoint/deal/applet',
      success: function (a) {
        console.info('建立连接！')
      }
    })
    wx.onSocketOpen(function (res) {
      socketOpen = true
      console.log('WebSocket连接已打开！')
      sendSocketMessage('{"type": 41, "msg": "xxxxx"}')
    })
    wx.onSocketMessage(function (res) {
      console.log('收到服务器内容：' + res.data)
    })
    function sendSocketMessage(msg) {
      if (socketOpen) {
        wx.sendSocketMessage({
          data: msg
        })
      }
    }
    function init() {

      var context = wx.createCanvasContext('canvas')

      context.setStrokeStyle("#00ff00")
      context.setLineWidth(5)
      context.rect(0, 0, 200, 200)
      context.stroke()
      context.setStrokeStyle("#ff0000")
      context.setLineWidth(2)
      context.moveTo(160, 100)
      context.arc(100, 100, 60, 0, 2 * Math.PI, true)
      context.moveTo(140, 100)
      context.arc(100, 100, 40, 0, Math.PI, false)
      context.moveTo(85, 80)
      context.arc(80, 80, 5, 0, 2 * Math.PI, true)
      context.moveTo(125, 80)
      context.arc(120, 80, 5, 0, 2 * Math.PI, true)
      context.stroke()
      context.draw()
    }
    init()
  }
})
