<%@ page import="moska.rebora.User.DTO.UserLoginDto" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/11/28
  Time: 5:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
</head>
<body>
<c:set var="userLoginDto" value="${userLoginDto}"/>
<script type="text/javascript">
    $(document).ready(function () {
        console.log('result= ', `${userLoginDto.result}`);
        console.log('userEmail= ', `${userLoginDto.userEmail}`);
        console.log('userSnsKind= ', `${userLoginDto.userSnsKind}`);
        console.log('userName= ', `${userLoginDto.userName}`);
        console.log('userSnsId= ', `${userLoginDto.userSnsId}`);
        console.log('errorCode= ', `${userLoginDto.errorCode}`);
    })
</script>
</body>
</html>
