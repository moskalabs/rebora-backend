<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/20
  Time: 4:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/header.jsp" %>
</head>
<style>
    tr {
        height: 60px;
        font-size: 20px;
    }

    p {
        margin: 0;
    }

    .border-end {
        border-right: 1px solid #dee2e6;
    }

    html {
        overflow-y: hidden;
    }

    body {
    }
</style>
<fmt:parseDate value="${ payment.content.paidAt }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parsePaidAt" type="both"/>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>결제 상세</h4>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <table class="table align-middle text-left">
                                <tbody style="border: 0">
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">영화 이름</th>
                                    <td style="width: 80%;">
                                        <a style="text-decoration: none; color:black"
                                           href="<%=CURRENT_SERVER%>/admin/movie/info?movieId=${payment.content.movieId}">${payment.content.movieName}</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">유저 이메일</th>
                                    <td style="width: 80%;">
                                        <a style="text-decoration: none; color:black"
                                           href="<%=CURRENT_SERVER%>/admin/user/info?userId=${payment.content.userId}">${payment.content.userEmail}</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">유저 이름</th>
                                    <td style="width: 80%;">
                                        <a style="text-decoration: none; color:black"
                                           href="<%=CURRENT_SERVER%>/admin/user/info?userId=${payment.content.userId}">${payment.content.userName}</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">유저 닉네임</th>
                                    <td style="width: 80%;">
                                        <a style="text-decoration: none; color:black"
                                           href="<%=CURRENT_SERVER%>/admin/user/info?userId=${payment.content.userId}">${payment.content.userNickname}</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 번호</th>
                                    <td style="width: 80%;">
                                        <p>${payment.content.paymentId}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 내용</th>
                                    <td style="width: 80%;">
                                        <p>${payment.content.paymentContent}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 금액</th>
                                    <td style="width: 80%;">
                                        <p><fmt:formatNumber value="${payment.content.paymentAmount}"
                                                             pattern="#,###"/>원</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 방법</th>
                                    <c:choose>
                                        <c:when test="${payment.content.paymentMethod == 'card'}">
                                            <td style="width: 80%;">
                                                <p>카드</p>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="width: 80%;">
                                                <p>현금</p>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 상태</th>
                                    <c:choose>
                                        <c:when test="${payment.content.paymentStatus == 'COMPLETE'}">
                                            <td style="width: 80%;">
                                                결제 완료
                                            </td>
                                        </c:when>
                                        <c:when test="${payment.content.paymentStatus == 'CANCEL'}">
                                            <td style="width: 80%;">
                                                결제 취소
                                            </td>
                                        </c:when>
                                        <c:when test="${payment.content.paymentStatus == 'FAILURE'}">
                                            <td style="width: 80%;">
                                                결제 실패
                                            </td>
                                        </c:when>
                                    </c:choose>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">카드이름</th>
                                    <td style="width: 80%;">
                                        ${payment.content.paymentCardName}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">카드번호</th>
                                    <td style="width: 80%;">
                                        ${payment.content.paymentCardNumber}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">PG결제사</th>
                                    <td style="width: 80%;">
                                        ${payment.content.pgProvider}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제일시</th>
                                    <td style="width: 80%;">
                                        <p><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsePaidAt }"/></p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">영수증</th>
                                    <td style="width: 80%;">
                                        <a style="text-decoration: none; color:black"
                                           href="${payment.content.receiptUrl}">영수증 링크</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">결제 메모</th>
                                    <td style="width: 80%;">
                                        <textarea class="form-control" style="resize: none" rows="3" id="paymentMemo"
                                                  placeholder="Enter ...">${payment.content.paymentMemo}</textarea>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <h4>결제 로그</h4>
            <c:forEach var="paymentLog" items="${payment.content.paymentLogDtoList}" varStatus="status">
                <section class="content">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-12">
                                <div class="card">
                                    <fmt:parseDate value="${ paymentLog.paidAt }" pattern="yyyy-MM-dd'T'HH:mm"
                                                   var="paymentLogPaidAt" type="both"/>
                                    <fmt:parseDate value="${ paymentLog.regDate}" pattern="yyyy-MM-dd'T'HH:mm"
                                                   var="paymentLogRegDate" type="both"/>
                                    <table class="table align-middle text-left border-top">
                                        <tbody>
                                        <tr>
                                            <th class="border-end" scope="row" style="width: 25%;">결제 상태</th>
                                            <c:choose>
                                                <c:when test="${paymentLog.paymentLogStatus == 'COMPLETE'}">
                                                    <td style="width: 80%;">
                                                        결제 완료
                                                    </td>
                                                </c:when>
                                                <c:when test="${paymentLog.paymentLogStatus == 'CANCEL'}">
                                                    <td style="width: 80%;">
                                                        결제 취소
                                                    </td>
                                                </c:when>
                                                <c:when test="${paymentLog.paymentLogStatus == 'FAILURE'}">
                                                    <td style="width: 80%;">
                                                        결제 실패
                                                    </td>
                                                </c:when>
                                            </c:choose>
                                        </tr>
                                        <tr>
                                            <th class="border-end" scope="row" style="width: 25%;">결제 내용</th>
                                            <td style="width: 80%;">
                                                    ${paymentLog.paymentLogContent}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="border-end" scope="row" style="width: 25%;">결제일시</th>
                                            <td style="width: 80%;">
                                                <c:choose>
                                                    <c:when test="${ paymentLog.paidAt != null}">
                                                        <p><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
                                                                           value="${ paymentLogPaidAt }"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
                                                                           value="${ paymentLogRegDate }"/></p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </c:forEach>
            <div class="container-fluid">
                <div class="row">
                    <div style="display:flex; width: 100%; height:120px">
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-start">
                            <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                        </div>
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                            <button type="button" class="btn btn-success mr-3" onclick="savePayment()">&nbsp;저장&nbsp;
                            </button>
                            <button type="button" class="btn btn-danger mr-3" onclick="cancelSave()">&nbsp;취소&nbsp;
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<%@include file="../include/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        let token = localStorage.getItem("token");
        if (token == null || token === "") {
            Swal.fire({
                title: "오류",
                text : "토큰이 만료되었습니다.",
            }).then(() => {
                location.href = "<%=CURRENT_SERVER%>/admin/login";
            })
        }

        $.ajax({
            url        : "<%=CURRENT_SERVER%>/admin/adminCheck",
            headers    : {
                "token": token
            },
            contentType: 'application/json',
            method     : "GET",
            dataType   : "json",
            error      : function (data) {
                if (!data.responseJSON.result) {
                    Swal.fire({
                        title: "오류",
                        text : data.responseJSON.message,
                    }).then(() => {
                        location.href = "<%=CURRENT_SERVER%>/admin/login";
                    })
                }
            }
        }).done(function (data) {
            if (!data.result) {
                Swal.fire({
                    title: "오류",
                    text : data.message,
                }).then(() => {
                    location.href = "<%=CURRENT_SERVER%>/admin/login";
                })
            }
        })
    })

    function goToList() {
        window.history.back();
    }

    function savePayment() {
        let token = localStorage.getItem("token");
        if (token == null || token === "") {
            Swal.fire({
                title: "오류",
                text : "토큰이 만료되었습니다.",
            }).then(() => {
                location.href = "<%=CURRENT_SERVER%>/admin/login";
            })
        }

        let paymentMemo = $("#paymentMemo").val();
        let paymentId = "${payment.content.paymentId}";

        $.ajax({
            url        : "<%=CURRENT_SERVER%>/admin/payment/updatePaymentInfo/" + paymentId,
            headers    : {
                "token": token
            },
            method     : "POST",
            data       : {paymentMemo: paymentMemo},
            dataType   : "json",
            error      : function (data) {
                if (!data.responseJSON.result) {
                    Swal.fire({
                        title: "오류",
                        text : data.message
                    }).then(() => {
                        return false;
                    })
                }
            }
        }).done(function (data) {
            if (data.result) {
                Swal.fire({
                    title: "완료",
                    text : "저장이 완료되었습니다."
                }).then(() => {
                    location.reload();
                })
            }
        })
    }

    function cancelSave() {
        Swal.fire({
            title            : "취소",
            text             : "저장을 취소하시겠습니까?",
            showCancelButton : true,
            confirmButtonText: '확인',
            cancelButtonText : '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                goToList();
            } else {
                return false;
            }
        })
    }
</script>
</body>
</html>
