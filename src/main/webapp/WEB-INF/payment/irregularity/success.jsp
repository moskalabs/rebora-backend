<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/07
  Time: 4:37 PM
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
<body>
<script>
    $(document).ready(function () {
        $.ajax({
            url: "<%=CURRENT_SERVER%>/api/payment/irregularity/getBillingKey",
            data: {
                customerKey: "${param.customerKey}",
                authKey: "${param.authKey}",
                userId: ${param.userId}
            },
            method: "POST",
            dataType: "json",
            error: function (data) {
                Swal.fire({
                    title: "오류",
                    text: data.responseJSON.message,
                }).then(() => {
                    console.log(data.responseJSON)
                })
            }
        }).done(function (data) {
            Swal.fire({
                title: "완료",
                text: "카드 등록이 완료되었습니다.",
            }).then(() => {

            })
        })
    })
</script>
</body>
</html>
