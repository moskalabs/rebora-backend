<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/09
  Time: 2:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>
</head>
<body style="background-color: #a29bfe">
<script>
    $(document).ready(function () {
        console.log("customerUid = ", ${customerUid});
        console.log("imp_success = ", ${imp_success});
        console.log("userRecruitmentId = ", ${userRecruitmentId});
        console.log("error_msg = ", ${error_msg});
    })
</script>
</body>
</html>
