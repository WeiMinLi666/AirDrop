<%--
  Created by IntelliJ IDEA.
  User: 86139
  Date: 2022/5/21
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--不忽略EL表达式--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
    <title>发送成功</title>
</head>
<body>
<h1>恭喜你上传文件成功,耗时${pageContext.request.getAttribute("hms")}</h1>
</body>
</html>
