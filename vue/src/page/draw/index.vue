<template>
  <div>
    <div style="width: 490px; margin: 10px auto;">
      <div ref="canvasDiv"></div>
    </div>
    <button>clearCanvas</button>
    <button>匹配</button>
    <br>
    <input type="text">
    <button>发送</button>
  </div>
</template>

<style type="text/css" lang="scss">
</style>

<script type="text/javascript">
  import sockjs from "../../assets/sockjs.min.js"
  import stomp from "../../assets/stomp.min.js"
  export default {
    data() {
      return {
        ws:null,
        stomp: null,
        canvas: null,
        context: null,
        canvasWidth: 490,
        canvasHeight: 220,
        padding: 25,
        lineWidth: 8,
        colorPurple: "#cb3594",
        colorGreen: "#659b41",
        colorYellow: "#ffcf33",
        colorBrown: "#986928",
        outlineImage: new Image(),
        crayonImage: new Image(),
        markerImage: new Image(),
        eraserImage: new Image(),
        crayonBackgroundImage: new Image(),
        markerBackgroundImage: new Image(),
        eraserBackgroundImage: new Image(),
        crayonTextureImage: new Image(),
        clickX: new Array(),
        clickY: new Array(),
        clickColor: new Array(),
        clickTool: new Array(),
        clickSize: new Array(),
        clickDrag: new Array(),
        paint: false,
        curColor: "#cb3594",
        curTool: "crayon",
        curSize: "normal",
        mediumStartX: 18,
        mediumStartY: 19,
        mediumImageWidth: 93,
        mediumImageHeight: 46,
        drawingAreaX: 111,
        drawingAreaY: 11,
        drawingAreaWidth: 267,
        drawingAreaHeight: 200,
        toolHotspotStartY: 23,
        toolHotspotHeight: 38,
        sizeHotspotStartY: 157,
        sizeHotspotHeight: 36,
        sizeHotspotWidthObject: {
          huge: 39,
          large: 25,
          normal: 18,
          small: 16
        },
        totalLoadResources: 8,
        curLoadResNum: 0,
      }
    },
    created() {
      var v = this
      v.$axios.get(v.$domain + '/api/user')
      .then(r => {
        if (!r.data.success) {
          v.$router.push({ path: '/login' })
        } else {
          v.prepareCanvas()
          v.connect()
        }
      })
      .catch(error => {
        console.log(error);
        v.$router.push({ path: '/login' })
      });
    },
    mounted () {
      var v = this
    },
    methods: {
      connect() {
        var v = this
        v.ws = new SockJS(v.$domain + "/endpoint/deal");
        // ws 业务
        v.ws.onopen = function () {
        };
        v.ws.onclose = function (event) {
        };
        v.ws.onmessage = function (event) {
          v.receive(JSON.parse(event.data));
        };
        // ws 广播
        v.stomp = Stomp.over(new SockJS(v.$domain + "/endpoint/placard"));
        v.stomp.connect({}, function (frame) {
            v.stomp.subscribe(v.$domain + '/simple/greeting', function (greeting) {
            });
        });
      },
      sendStomp(msg, type) {
        var v = this
        v.stomp.send('/app/say', {}, JSON.stringify({'msg': msg, 'type': type}))
      },
      send(msg, type) {
        var v = this
        v.ws.send(JSON.stringify({'msg': msg, 'type': type}))
      },
      receive(obj){
        var v = this
        if (obj.type == 1) v.drawmousedown(JSON.parse(obj.msg).x, JSON.parse(obj.msg).y);
        else if (obj.type == 2) v.drawmousemove(JSON.parse(obj.msg).x, JSON.parse(obj.msg).y);
        else if (obj.type == 3) v.drawmouseup();
        else if (obj.type == 4) v.drawmouseleave();
        else if (obj.type == 5) v.drawreset();
        else console.info(obj)
      },
      close() {
        var v = this
        v.ws != null ? v.ws.close() : null;
      },
      prepareCanvas(e) {
        var v = this
        v.canvas = document.createElement('canvas');
        v.canvas.setAttribute('width', v.canvasWidth);
        v.canvas.setAttribute('height', v.canvasHeight);

        v.$refs.canvasDiv.appendChild(v.canvas);

        if(typeof G_vmlCanvasManager != 'undefined') {
          v.canvas = G_vmlCanvasManager.initElement(v.canvas);
        }
        v.context = v.canvas.getContext("2d"); // Grab the 2d canvas context
        // Load images
        v.crayonImage.onload = function() { resourceLoaded(); };
        v.crayonImage.src = require("../../assets/crayon-outline.png");
        v.markerImage.onload = function() { resourceLoaded(); };
        v.markerImage.src = require("../../assets/marker-outline.png");
        v.eraserImage.onload = function() { resourceLoaded(); };
        v.eraserImage.src = require("../../assets/eraser-outline.png");
        v.crayonBackgroundImage.onload = function() { resourceLoaded(); };
        v.crayonBackgroundImage.src = require("../../assets/crayon-background.png");
        v.markerBackgroundImage.onload = function() { resourceLoaded(); };
        v.markerBackgroundImage.src = require("../../assets/marker-background.png");
        v.eraserBackgroundImage.onload = function() { resourceLoaded(); };
        v.eraserBackgroundImage.src = require("../../assets/eraser-background.png");
        v.crayonTextureImage.onload = function() { resourceLoaded(); };
        v.crayonTextureImage.src = require("../../assets/crayon-texture.png");
        v.outlineImage.onload = function() { resourceLoaded(); };
        v.outlineImage.src = require("../../assets/outline.png");
        // Event
        document.getElementsByTagName('canvas')[0].onmousedown = function(e){
          var x = e.pageX - this.offsetLeft;
          var y = e.pageY - this.offsetTop;
          v.drawmousedown(x, y);
          v.send(JSON.stringify({'x': x, 'y': y}), 1);
        }
        document.getElementsByTagName('canvas')[0].onmousemove = function(e){
          var x = e.pageX - this.offsetLeft;
          var y = e.pageY - this.offsetTop;
          v.drawmousemove(x, y);
          if (v.paint) v.send(JSON.stringify({'x': x, 'y': y}), 2);
        }
        document.getElementsByTagName('canvas')[0].onmouseup = function(e){
          v.drawmouseup();
          v.send(null, 3);
        }
        document.getElementsByTagName('canvas')[0].onmouseleave = function(e){
          v.drawmouseleave();
          if (v.paint) v.send(null, 4);
        }
        document.getElementsByTagName('button')[0].onclick = function(e) {
          v.drawreset();
          v.send(null, 5);
        }
        document.getElementsByTagName('button')[1].onclick = function(e) {
          v.send(null, 11);
        }
        document.getElementsByTagName('button')[2].onclick = function(e) {
          v.send(document.getElementsByTagName('input')[0].value, 41);
        }
        function resourceLoaded() {
          if(++v.curLoadResNum >= v.totalLoadResources) v.redraw();
        }
      },
      drawmousedown(mouseX, mouseY) {
        var v = this
        if(mouseX < v.drawingAreaX) {
          // Left of the drawing area
          if(mouseX > v.mediumStartX) {
            if(mouseY > v.mediumStartY && mouseY < v.mediumStartY + v.mediumImageHeight) v.curColor = v.colorPurple;
            else if(mouseY > v.mediumStartY + v.mediumImageHeight && mouseY < v.mediumStartY + v.mediumImageHeight * 2) v.curColor = v.colorGreen;
            else if(mouseY > v.mediumStartY + v.mediumImageHeight * 2 && mouseY < v.mediumStartY + v.mediumImageHeight * 3) v.curColor = v.colorYellow;
            else if(mouseY > v.mediumStartY + v.mediumImageHeight * 3 && mouseY < v.mediumStartY + v.mediumImageHeight * 4) v.curColor = v.colorBrown;
          }
        } else if(mouseX > v.drawingAreaX + v.drawingAreaWidth) {
          // Right of the drawing area
          if(mouseY > v.toolHotspotStartY) {
            if(mouseY > v.sizeHotspotStartY) {
              var sizeHotspotStartX = v.drawingAreaX + v.drawingAreaWidth;
              if(mouseY < v.sizeHotspotStartY + v.sizeHotspotHeight && mouseX > sizeHotspotStartX) {
                if(mouseX < sizeHotspotStartX + v.sizeHotspotWidthObject.huge) v.curSize = "huge";
                else if(mouseX < sizeHotspotStartX + v.sizeHotspotWidthObject.large + v.sizeHotspotWidthObject.huge) v.curSize = "large";
                else if(mouseX < sizeHotspotStartX + v.sizeHotspotWidthObject.normal + v.sizeHotspotWidthObject.large + v.sizeHotspotWidthObject.huge) v.curSize = "normal";
                else if(mouseX < sizeHotspotStartX + v.sizeHotspotWidthObject.small + v.sizeHotspotWidthObject.normal + v.sizeHotspotWidthObject.large + v.sizeHotspotWidthObject.huge) v.curSize = "small";
              }
            } else {
              if(mouseY < v.toolHotspotStartY + v.toolHotspotHeight) v.curTool = "crayon";
              else if(mouseY < v.toolHotspotStartY + v.toolHotspotHeight * 2) v.curTool = "marker";
              else if(mouseY < v.toolHotspotStartY + v.toolHotspotHeight * 3) v.curTool = "eraser";
            }
          }
        } else if(mouseY > v.drawingAreaY && mouseY < v.drawingAreaY + v.drawingAreaHeight) {
          // Mouse click location on drawing area
        }
        v.paint = true;
        v.addClick(mouseX, mouseY, false);
        v.redraw();
      },
      drawmousemove(mouseX, mouseY) {
        var v = this
        if(v.paint==true){
          v.addClick(mouseX, mouseY, true);
          v.redraw();
        }
      },
      drawmouseup() {
        var v = this
        v.paint = false;
        v.redraw();
      },
      drawmouseleave() {
        var v = this
        v.paint = false;
      },
      drawreset() {
        var v = this
        v.clickX = [];
        v.clickY = [];
        v.clickTool = [];
        v.clickColor = [];
        v.clickSize = [];
        v.clickDrag = [];
        v.redraw();
      },
      addClick(x, y, dragging){
        var v = this
        v.clickX.push(x);
        v.clickY.push(y);
        v.clickTool.push(v.curTool);
        v.clickColor.push(v.curColor);
        v.clickSize.push(v.curSize);
        v.clickDrag.push(dragging);
      },
      clearCanvas() {
        var v = this
        v.context.clearRect(0, 0, v.canvasWidth, v.canvasHeight);
      },
      preperBrushColor(curColor, type) {
        var v = this
        v.brushColor(v.colorPurple, curColor, v.curTool);
        v.brushColor(v.colorGreen, curColor, v.curTool);
        v.brushColor(v.colorYellow, curColor, v.curTool);
        v.brushColor(v.colorBrown, curColor, v.curTool);
      },
      brushColor(color, curColor, type) {
        var v = this
        var choose = curColor == color
        var locX = choose ? 18 : 52;
        var locY = color == v.colorPurple ? (19+46*0) : color == v.colorGreen ? (19+46*1) : color == v.colorYellow ? (19+46*2) : (19+46*3);

        var image = null;
        v.context.beginPath();
        if (type == "crayon") {
          v.context.moveTo(locX + 41, locY + 11);
          v.context.lineTo(locX + 41, locY + 35);
          v.context.lineTo(locX + 29, locY + 35);
          v.context.lineTo(locX + 29, locY + 33);
          v.context.lineTo(locX + 11, locY + 27);
          v.context.lineTo(locX + 11, locY + 19);
          v.context.lineTo(locX + 29, locY + 13);
          v.context.lineTo(locX + 29, locY + 11);
          v.context.lineTo(locX + 41, locY + 11);
          image = v.crayonImage;
        } else if (type == "marker") {
          v.context.moveTo(locX + 10, locY + 24);
          v.context.lineTo(locX + 10, locY + 24);
          v.context.lineTo(locX + 22, locY + 16);
          v.context.lineTo(locX + 22, locY + 31);
          image = v.markerImage;
        }
        v.context.closePath();
        v.context.fillStyle = color;
        v.context.fill();
        choose ? v.context.drawImage(image, locX, locY, v.mediumImageWidth, v.mediumImageHeight) : v.context.drawImage(image, 0, 0, 59, v.mediumImageHeight, locX, locY, 59, v.mediumImageHeight);
      },
      redraw(){
        var v = this
        v.clearCanvas();

        var locX;
        var locY;
        if(v.curTool == "crayon") {
          v.context.drawImage(v.crayonBackgroundImage, 0, 0, v.canvasWidth, v.canvasHeight);
          v.preperBrushColor(v.curColor, v.curTool);
        } else if(v.curTool == "marker") {
          v.context.drawImage(v.markerBackgroundImage, 0, 0, v.canvasWidth, v.canvasHeight);
          v.preperBrushColor(v.curColor, v.curTool);
        } else if(v.curTool == "eraser") {
          v.context.drawImage(v.eraserBackgroundImage, 0, 0, v.canvasWidth, v.canvasHeight);
          v.context.drawImage(v.eraserImage, 18, 19, v.mediumImageWidth, v.mediumImageHeight);
        } else {
          alert("Error: Current Tool is undefined");
        }

        if(v.curSize == "small"){
          locX = 467;
        }else if(v.curSize == "normal"){
          locX = 450;
        }else if(v.curSize == "large"){
          locX = 428;
        }else if(v.curSize == "huge"){
          locX = 399;
        }
        locY = 189;
        v.context.beginPath();
        v.context.rect(locX, locY, 2, 12);
        v.context.closePath();
        v.context.fillStyle = '#333333';
        v.context.fill();

        // Keep the drawing in the drawing area
        v.context.save();
        v.context.beginPath();
        v.context.rect(v.drawingAreaX, v.drawingAreaY, v.drawingAreaWidth, v.drawingAreaHeight);
        v.context.clip();

        var radius;
        for(var i = 1; i < v.clickX.length; i++)
        {
          if(v.clickSize[i] == "small"){
            radius = 2;
          }else if(v.clickSize[i] == "normal"){
            radius = 5;
          }else if(v.clickSize[i] == "large"){
            radius = 10;
          }else if(v.clickSize[i] == "huge"){
            radius = 20;
          }else{
            alert("Error: Radius is zero for click " + i);
            radius = 0;
          }

          v.context.beginPath();
          if(v.clickDrag[i] && i){
            v.context.moveTo(v.clickX[i-1], v.clickY[i-1]);
          }else{
            v.context.moveTo(v.clickX[i], v.clickY[i]);
          }
          v.context.lineTo(v.clickX[i], v.clickY[i]);
          v.context.closePath();

          v.context.strokeStyle = v.clickTool[i] == "eraser" ? 'white' : v.clickColor[i];
          // context.globalCompositeOperation = clickTool[i] == "eraser" ? "destination-out" : "source-over";

          v.context.lineJoin = "round";
          v.context.lineWidth = radius;
          v.context.stroke();

        }
        v.context.restore();

        // Overlay a crayon texture (if the current tool is crayon)
        if(v.curTool == "crayon"){
          v.context.globalAlpha = 0.4;
          v.context.drawImage(v.crayonTextureImage, 0, 0, v.canvasWidth, v.canvasHeight);
        }
        v.context.globalAlpha = 1;
        v.context.drawImage(v.outlineImage, v.drawingAreaX, v.drawingAreaY, v.drawingAreaWidth, v.drawingAreaHeight);
      }
    }
  }
</script>
