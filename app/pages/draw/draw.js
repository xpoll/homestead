//draw.js
var app = getApp()
Page({
  data: {
    canvas: null,
    context: null,
    socketOpen: false,
    socket: null,
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
      previous: null,
      current: null
    },
    coor: {
      left: 0,
      top: 0
    },
    game: {
      drawer: false,
      tips: '',
      answer: ''
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
  bindMatchGame: function (event) {
    this.sendSocketMessage(11, '')
    wx.showLoading({title: 'match...'})
  },
  bindtouchstart: function (event) {
    if (!this.data.game.drawer) return
    this.setData({ 'cur.paint': true })
    this.redraw(event.changedTouches[0].x, event.changedTouches[0].y, false)
  },
  bindtouchmove: function (event) {
    if (!this.data.game.drawer) return
    if (this.data.cur.paint) {
      this.redraw(event.changedTouches[0].x, event.changedTouches[0].y, true)
    }
  },
  bindtouchend: function (event) {
    if (!this.data.game.drawer) return
    this.setData({ 'cur.paint': false })
  },
  bindtouchcancel: function (event) {
    if (!this.data.game.drawer) return
    this.setData({ 'cur.paint': false })
  },
  resetdraw: function () {
    this.data.context.clearRect(0, 0, 300, 200)
    this.setData({'draw.previous': new Object()})
    this.setData({'draw.current': new Object()})
    this.data.context.draw()
  },
  redraw: function (x, y, p) {
    var obj = new Object()
    obj.x = x
    obj.y = y
    obj.paint = p
    obj.tool = this.data.cur.tool
    obj.color = this.data.cur.color
    obj.size = this.data.cur.size

    this.sendSocketMessage(1, obj)

    this.setData({'draw.previous': this.data.draw.current})
    this.setData({'draw.current': obj})

    var context = this.data.context
    context.beginPath()
    if (this.data.draw.current.paint && this.data.draw.previous) {
      context.moveTo(this.data.draw.previous.x, this.data.draw.previous.y)
      context.lineTo(this.data.draw.current.x, this.data.draw.current.y)
    }
    context.closePath()
    context.setStrokeStyle(this.data.draw.current.tool == this.data.arr.tool[1] ? this.data.arr.color[5] : this.data.draw.current.color)
    context.setLineJoin("round")
    context.setLineCap("round")
    context.setLineWidth(this.data.draw.current.size)
    context.stroke()
    context.restore()
    context.draw(true)
  },
  sendSocketMessage: function (type, obj) {
    if (this.data.socketOpen) {
      wx.sendSocketMessage({
        data: JSON.stringify({'type': type, 'msg': JSON.stringify(obj)})
      })
    }
  },
  onLoad: function () {
    var that = this

    if (that.data.socket == null || !that.data.socketOpen) {
      var socket = wx.connectSocket({
        url: app.globalData.wsDomain + '/endpoint/deal/applet',
        success: function (a) {
          console.info('建立连接！')
        }
      })
      that.setData({'socket': 'socket'})
      wx.onSocketOpen(function (res) {
        console.log('WebSocket连接已打开！')
        that.setData({'socketOpen': true})
      })
    }
    wx.onSocketMessage(function (res) {
      that.receive(JSON.parse(res.data))
    })
    wx.onSocketClose(function(res) {
      console.log('WebSocket 已关闭！')
      that.setData({'socketOpen': false})
    })
    // wx.createSelectorQuery().in(that).select('.draw').boundingClientRect(function (res) {
    //   that.setData({ 'coor.left': (res.left + 1) })
    //   that.setData({ 'coor.top': (res.top + 1) })
    // }).exec()
    this.data.context = wx.createCanvasContext("draw")
  },
  hideLoadingLater: function () {
    setTimeout(function(){wx.hideLoading()},2000)
  },
  receive: function(obj) {
    console.info(obj)
    var that = this
    var type = obj.type
    if (type == 21) {
      wx.hideLoading()
    } else if (type == 31) {
      that.setData({'game.drawer': false, 'game.tips': '', 'game.answer': obj.msg})
    } else if (type == 32) {
      that.setData({'game.drawer': true, 'game.tips': obj.msg, 'game.answer': ''})
    } else if (type == 33) {
      wx.showLoading({title: 'yes'})
      that.hideLoadingLater()
    } else if (type == 34) {
      wx.showLoading({title: 'no'})
      that.hideLoadingLater()
    } else if (type == 35) {
      wx.showLoading({title: 'right: ' + obj.msg})
      that.hideLoadingLater()
    }


    // game: {
    //   drawer: false,
    //   tips: null
    // }


  }
})
