//draw.js
var app = getApp()
Page({
  data: {
    tools: {
      left_0: true,
      left_1: false,
      left_2: false,
      left_3: false,
      left_4: false,
      left_5: false,
      right_0: true,
      right_1: false,
      right_2: false,
      right_3: false,
      right_4: true,
      right_5: false,
    }
  },
  toolsChooseType: function (type, value) {

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


    // var query = wx.createSelectorQuery()
    // var arrDiv = query.selectAll('.tools-tool')
    

    // var aDiv = document.getElementById('tools-left').getElementsByTagName('view')
    // var bDiv = document.getElementById('tools-right').getElementsByTagName('view')
    // // 工具箱点击事件
    // aDiv[0].onclick = function () { colorToolsChoose(0); clearToolsClass(this, new Array(aDiv[1], aDiv[2], aDiv[3], aDiv[4])) }
    // aDiv[1].onclick = function () { colorToolsChoose(1); clearToolsClass(this, new Array(aDiv[0], aDiv[2], aDiv[3], aDiv[4])) }
    // aDiv[2].onclick = function () { colorToolsChoose(2); clearToolsClass(this, new Array(aDiv[0], aDiv[1], aDiv[3], aDiv[4])) }
    // aDiv[3].onclick = function () { colorToolsChoose(3); clearToolsClass(this, new Array(aDiv[0], aDiv[1], aDiv[2], aDiv[4])) }
    // aDiv[4].onclick = function () { colorToolsChoose(4); clearToolsClass(this, new Array(aDiv[0], aDiv[1], aDiv[2], aDiv[3])) }
    // aDiv[5].onclick = function () { resetdraw() }

    // bDiv[0].onclick = function () { sizeToolsChoose(0); clearToolsClass(this, new Array(bDiv[1], bDiv[2], bDiv[3])) }
    // bDiv[1].onclick = function () { sizeToolsChoose(1); clearToolsClass(this, new Array(bDiv[0], bDiv[2], bDiv[3])) }
    // bDiv[2].onclick = function () { sizeToolsChoose(2); clearToolsClass(this, new Array(bDiv[0], bDiv[1], bDiv[3])) }
    // bDiv[3].onclick = function () { sizeToolsChoose(3); clearToolsClass(this, new Array(bDiv[0], bDiv[1], bDiv[2])) }
    // bDiv[4].onclick = function () { toolsChoose(0); clearToolsClass(this, new Array(bDiv[5])) }
    // bDiv[5].onclick = function () { toolsChoose(1); clearToolsClass(this, new Array(bDiv[4])) }
    // /**
    //  * 工具箱选中样式
    //  */
    // function clearToolsClass(e, arr) {
    //   e.classList.add('tools-choose')
    //   for (var i = 0; i < arr.length; i++) {
    //     arr[i].classList.remove('tools-choose')
    //   }
    // }
    function colorToolsChoose(num) {
      curColor = colorArr[num]
    }
    function sizeToolsChoose(num) {
      curSize = sizeArr[num]
    }
    function toolsChoose(num) {
      curTool = toolArr[num]
    }
    // 数据定义
    var canvas = document.getElementById('draw')
    var canvasWidth = 400;
    var canvasHeight = 200;
    canvas.setAttribute('width', canvasWidth);
    canvas.setAttribute('height', canvasHeight);
    var drawLeft = canvas.getBoundingClientRect().left;
    var drawTop = canvas.getBoundingClientRect().top
    if (typeof G_vmlCanvasManager != 'undefined') {
      canvas = G_vmlCanvasManager.initElement(canvas);
    }
    var context = canvas.getContext("2d")

    var sizeArr = new Array(2, 4, 8, 16)
    var colorArr = new Array("#cb3594", "#659b41", "#ffcf33", "#986928", "#000000", "#ffffff")
    var toolArr = new Array(0, 1)// 0 笔芯 1 橡皮擦
    var curColor = colorArr[0]
    var curSize = sizeArr[0]
    var curTool = toolArr[0]
    var paint = false
    var drawX = [], drawY = [], drawTool = [], drawColor = [], drawSize = [], drawPaint = []

    canvas.onmousedown = function (e) {
      var x = e.pageX - this.offsetLeft;
      var y = e.pageY - this.offsetTop;
      drawmousedown(x, y);
    }
    canvas.onmousemove = function (e) {
      var x = e.pageX - this.offsetLeft;
      var y = e.pageY - this.offsetTop;
      drawmousemove(x, y);
    }
    canvas.onmouseup = function (e) {
      drawmouseup();
    }
    canvas.onmouseleave = function (e) {
      drawmouseleave();
    }

    function drawmousedown(x, y) {
      paint = true
      addClick(x, y, false)
      redraw()
    }
    function drawmousemove(x, y) {
      if (paint) {
        addClick(x, y, true)
        redraw()
      }
    }
    function drawmouseup() {
      paint = false
      redraw()
    }
    function drawmouseleave() {
      paint = false
    }
    function addClick(x, y, p) {
      console.info(x, y, p)
      drawX.push(x)
      drawY.push(y)
      drawTool.push(curTool)
      drawColor.push(curColor)
      drawSize.push(curSize)
      drawPaint.push(p)
    }
    function resetdraw() {
      drawX = [], drawY = [], drawTool = [], drawColor = [], drawSize = [], drawPaint = []
      redraw()
    }
    function redraw() {
      context.clearRect(0, 0, canvasWidth, canvasHeight)
      console.info(drawX[drawX.length - 1], drawY[drawY.length - 1])
      for (var i = 1; i < drawX.length; i++) {
        context.beginPath()
        if (drawPaint[i] && i) {
          context.moveTo(drawX[i - 1], drawY[i - 1])
          context.lineTo(drawX[i], drawY[i])
        }
        context.closePath()
        context.strokeStyle = drawTool[i] == toolArr[1] ? colorArr[5] : drawColor[i]
        context.lineJoin = "round"
        context.lineWidth = drawSize[i]
        context.stroke()
      }
      context.restore()
      context.globalAlpha = 1
    }
  }
})
