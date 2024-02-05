<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
    <meta name="format-detection" content="telephone=no,email=no,date=no,address=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="">
    <meta name="description" content="">
    <title>Tips</title>
</head>
<body>
    <style>
        #loading {
                   margin: auto;
                   margin-top: 10px;
                   display: block;
                   width:50px;
                   height: 100px;
                   background: #FFFFFF;
                   /*border: 1px solid black;*/
               }
       
                   #loading .pic {
                       display: block;
                       width:50px;
                       height: 50px;
                       border-radius: 50%;
                       box-shadow: 0 2px 0 #666;
                       animation: loop 1s infinite;
                       -webkit-animation: loop 1s infinite;
                   }
       
               @keyframes loop {
                   0% {
                       transform: rotate(0deg);
                   }
       
                   100% {
                       transform: rotate(360deg );
                   }
               }
       #loadText{
           position:absolute; /*参照物是父容器*/
           left:50%;
           height:50%;
           top:45%;
           transform:translateX(-50%); 
       }
       #loadAni{
           position:absolute; /*参照物是父容器*/
           left:50%;
           top:54%;
           height:50%;
           transform:translateX(-50%); 
       }
       </style>

<div id="loadText">
    <h2 id="load_text">正在载入请等待</h2>
    </div>
    
    <div id="loadAni">
    <p id="loading">
        <span class="pic"></span>
    </p>
    </div>
    <script type="text/javascript">
    
    window.onload = function () {
        document.getElementById("loading").style.display = 'block';
    }
    document.onreadystatechange = function () {
        if (document.readyState == "complete") {//检测加载完成
            try {
                //document.getElementById("loadAni").style.display = 'none';
                //document.getElementById("loadText").style.display = 'none';
                //document.body.removeChild(p);
            } catch (e) {
    
            }
        }
    }
    </script>
 
    
    <script type="text/javascript">
        setTimeout(function(){
            let error = '${error}';
            let payUrl = '${payUrl}';
            if (payUrl) {
                window.location.href = payUrl;
                document.getElementById("load_text").innerHTML="正在跳转...";
            } else if (error) {
                document.getElementById("loadAni").style.display = 'none';
                document.getElementById("load_text").innerHTML=error;
            } 
        },1000);
    </script>
</body>
</html>
