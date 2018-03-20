//draw.js
var app = getApp()
Page({
  data: {
    canvas: null,
    context: null,
    tools: {
      color: [true, false, false, false, false],
      tool: [true, false],
      size: [true, false, false, false]
    },
    arr: {
      color: ["#cb3594", "#659b41", "#ffcf33", "#986928", "#000000", "#ffffff"],
      size: [2, 4, 8, 16],
      tool: [0, 1]
    },
    cur: {
      color: "#cb3594",
      size: 2,
      tool: 0,
      paint: false
    },
    draw: {
      x: [],
      y: [],
      color: [],
      size: [],
      tool: [],
      paint: []
    },
    coor: {
      left: 0,
      top: 0
    }
  },
  toolsChooseType: function (event) {
    // 工具箱点击事件
    var that = this
    var type = event.currentTarget.dataset.type
    var value = event.currentTarget.dataset.value
    if (type == 0) {// 涂料
      that.setData({ 'tools.color': choose(that.data.tools.color, value) })
      that.setData({ 'cur.color': that.data.arr.color[value] })
    } else if (type == 1) {// 粗细
      that.setData({ 'tools.size': choose(that.data.tools.size, value) })
      that.setData({ 'cur.size': that.data.arr.size[value] })
    } else if (type == 2) {// 笔檫
      that.setData({ 'tools.tool': choose(that.data.tools.tool, value) })
      that.setData({ 'cur.tool': that.data.arr.tool[value] })
    } else if (type == 3) {// 清除
      that.resetdraw()
    }
    function choose(arr, num) { for (var i = 0; i < arr.length; i++) arr[i] = (num == i); return arr }
  },
  bindtouchstart: function (event) {
    this.setData({ 'cur.paint': true })
    this.addClick(event.changedTouches[0].x, event.changedTouches[0].y, false)
    this.redraw()
  },
  bindtouchmove: function (event) {
    if (this.data.cur.paint) {
      this.addClick(event.changedTouches[0].x, event.changedTouches[0].y, true)
      this.redraw()
    }
  },
  bindtouchend: function (event) {
    this.setData({ 'cur.paint': false })
    this.redraw()
  },
  bindtouchcancel: function (event) {
    this.setData({ 'cur.paint': false })
  },
  addClick: function (x, y, p) {
    this.data.draw.x.push(x)
    this.data.draw.y.push(y)
    this.data.draw.color.push(this.data.cur.color)
    this.data.draw.size.push(this.data.cur.size)
    this.data.draw.tool.push(this.data.cur.tool)
    this.data.draw.paint.push(p)
    // this.setData({ 'draw.x': this.data.draw.x })
    // this.setData({ 'draw.y': this.data.draw.y })
    // this.setData({ 'draw.color': this.data.draw.color })
    // this.setData({ 'draw.size': this.data.draw.size })
    // this.setData({ 'draw.tool': this.data.draw.tool })
    // this.setData({ 'draw.paint': this.data.draw.paint })
  },
  resetdraw: function () {
    this.data.draw.x = []
    this.data.draw.y = []
    this.data.draw.color = []
    this.data.draw.size = []
    this.data.draw.tool = []
    this.data.draw.paint = []
    this.redraw()
  },
  redraw: function () {
    this.data.context.clearRect(0, 0, 300, 200)
    for (var i = 1; i < this.data.draw.x.length; i++) {
      this.data.context.beginPath()
      if (this.data.draw.paint[i] && i) {
        this.data.context.moveTo(this.data.draw.x[i - 1], this.data.draw.y[i - 1])
        this.data.context.lineTo(this.data.draw.x[i], this.data.draw.y[i])
      }
      this.data.context.closePath()
      this.data.context.setStrokeStyle(this.data.draw.tool[i] == this.data.arr.tool[1] ? this.data.arr.color[5] : this.data.draw.color[i])
      this.data.context.lineJoin = "round"

      this.data.context.setLineWidth(this.data.draw.size[i])
      this.data.context.stroke()
    }
    this.data.context.restore()
    this.data.context.draw()
  },
  onLoad: function () {
    var that = this
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
    wx.createSelectorQuery().in(that).select('.draw').boundingClientRect(function (res) {
      that.setData({ 'coor.left': (res.left + 1) })
      that.setData({ 'coor.top': (res.top + 1) })
    }).exec()
    this.data.context = wx.createCanvasContext("draw")
  }
})
