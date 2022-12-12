<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/09
  Time: 1:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
    <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
    <script src="https://js.tosspayments.com/v1/payment"></script>
</head>
<body>
<script>
    var IMP = window.IMP;   // 생략 가능
    IMP.init("imp43437372");
    IMP.request_pay({
        pg: "tosspayments.627175",
        customer_uid: "${customerUid}",
        customer_id: "${customerId}",
        m_redirect_url: "<%=CURRENT_SERVER%>/api/payment/applyDone?userId=1&customerUId=${customerUid}"
        /* ...생략... */
    }, function (rsp) { // callback
        console.log(rsp)
        if (rsp.success) {
            console.log(rsp)
            // 빌링키 발급 성공
        } else {
            // 빌링키 발급 실패
        }
    });

</script>
</body>
</html>
