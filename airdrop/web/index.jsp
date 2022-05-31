<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>文件上传</title>
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <style>

      * {
        padding: 0;
        border: none;
        /*清除默认样式，内外边距与默认边框效果*/
      }

      body{
        background-image: linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%);
      }

      #file-upload{
        margin:50px auto;
        position:relative;
        border:10px solid gray;
        width: 300px;
      }

      .title {
        position:relative;
        left:70px;
      }
      .p1{
        margin-top: 15px;
        border:1px solid black;
        display: inline-block;
        width: 250px;
      }

      .button{
        width: 60px;
        height: 30px;
        border-radius: 5px 5px;
        background-color:gray;
      }

      #determine{
        position: absolute;
        left: 220px;
        bottom:1px;
      }

      #innerdetermine{
        color: white;
        background-color: gray;
      }

      #choose{
        color:white;
        background-color: gray;
      }


    /*  剪切板的样式*/
      #cling{
        margin:200px auto;
        width:300px;
        border:10px solid gray;
      }

      #copy{
        color:white;
        background-color: gray;
      }

      #inputin{
        width: 100%;
      }

    </style>

    <script>
      var msg = "<%=request.getAttribute("eject")%>"
      var state = "ok"
      if(state===msg){
        console.log(msg)
        alert("你已经上传成功,请继续选择")
      }
    </script>
  </head>
  <body>
<div id="outer">
  <div id="file-upload">
    <h2 class="title"><b>文件传输</b></h2>
    <form action="${pageContext.request.contextPath}/upload.do" method="post" enctype="multipart/form-data">
      <p class="p1">目标ip地址:<input type="text" name="ip" placeholder="请输入目标的ip地址"/></p></br>
      <p class="p1">目标端口:<input type="text" name="port" placeholder="请输入服务器端口"/></p>
      <p><input type="file" name="file1"></p>
      <input name="upload" type="radio" value="finished"/>上传完成
      <input name="upload" type="radio" value="continue"/>我还要传
     <div class="button" id="determine"><input id="innerdetermine" type="submit" value="确定上传"/></div>
    </form>

<%--    <form action="${pageContext.request.contextPath}/upload.do" method="get">--%>
<%--      <input name="upload" type="radio" value="finished"/>上传完成--%>
<%--      <input name="upload" type="radio" value="continue"/>我还要传--%>
<%--    </form>--%>

  </div>
</div>


  <div id="cling">
    <h2 class="title"><b>共享剪贴板</b></h2>
    <form action="${pageContext.request.contextPath}/copy.do" method="post">
      <p class="p1"><input id="inputin" type="text" name="copy" placeholder="请将文字将粘贴到这里"> <div class="button"> <input id="copy" type="submit" value="复制"></div></p>
    </form>
  </div>


  </body>
</html>
