<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/08
  Time: 5:50 PM
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
        customer_uid: "gildong_1234",
        pg: "tosspayments.627175",
        customer_uid: "rebora_1_kkp02052@gmail.com",
        customer_id: "rebora_1_kkp02052@gmail.com",
        m_redirect_url: "http://localhost:8080/api/payment/irregularity/success?userId=1"
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
