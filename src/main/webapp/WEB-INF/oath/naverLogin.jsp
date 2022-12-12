<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/11/28
  Time: 3:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>naverLogin</title>
    <script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
</head>
<body>
    naverLogin
    ${NAVER_KEY}
    ${NAVER_CLIENT_ID}
    ${CALLBACK_URL}
    <script type="text/javascript">
        $(document).ready(function () {
            location.href = `https://nid.naver.com/oauth2.0/authorize?client_id=${NAVER_CLIENT_ID}&redirect_uri=${CALLBACK_URL}&response_type=code&state=oauth_state`
        })
    </script>
</body>
</html>
