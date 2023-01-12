<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.Instant" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/01
  Time: 4:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%=new java.util.Date()%>"/>
<c:set var="beforeWeek" value="<%= java.sql.Date.valueOf(LocalDate.now().minusWeeks(1L)) %>"/>
<c:set var="beforeMonth" value="<%= java.sql.Date.valueOf(LocalDate.now().minusMonths(1L)) %>"/>
<c:set var="before3Month" value="<%= java.sql.Date.valueOf(LocalDate.now().minusMonths(3L)) %>"/>
<c:set var="today"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/></c:set>
<c:set var="parseBeforeWeek"><fmt:formatDate value="${beforeWeek}" pattern="yyyy-MM-dd"/></c:set>
<c:set var="parseBeforeMonth"><fmt:formatDate value="${beforeMonth}" pattern="yyyy-MM-dd"/></c:set>
<c:set var="parseBefore3Month"><fmt:formatDate value="${before3Month}" pattern="yyyy-MM-dd"/></c:set>
<fmt:parseDate value="${ param.fromDate != null ? param.fromDate : today}" pattern="yyyy-MM-dd"
               var="parseFromDate" type="both"/>
<fmt:parseDate value="${ param.toDate != null ? param.toDate : today}" pattern="yyyy-MM-dd"
               var="parseToDate" type="both"/>
<c:set var="fromDate"><fmt:formatDate value="${parseFromDate}" pattern="yyyy-MM-dd"/></c:set>
<c:set var="toDate"><fmt:formatDate value="${parseToDate}" pattern="yyyy-MM-dd"/></c:set>
<html>
<head>
    <title>리보라 관리자</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>
    <style>
        .btn-primary,
        .btn-primary:active,
        .btn-primary:visited {
            background-color: #a29bfe !important;
            border-color: #a29bfe !important;
        }

        .btn-primary:hover {
            background-color: #6c5ce7 !important;
            border-color: #6c5ce7 !important;
        }

        p {
            margin: 0;
        }
    </style>
</head>

<body style="
    justify-content: center;
    display: flex;
    background-color: white;
    overflow:auto;
    ">
<div class="container-xxl" style="min-width:1140px">
    <div class="text-center" style="display:grid; grid-template-columns:200px 1fr; height:100%;">
        <div class="border-end" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="my-5 px-2" style="display:flex; width:100%; align-items:center; justify-content:center;">
                <h3 style="color:#a29bfe">RE : BORA</h3>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/movie/list" style="text-decoration: none; color:black">
                    <h5>영화 목록 관리</h5>
                </a>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/recruitment/list" style="text-decoration: none; color:black">
                    <h5>모집 관리</h5>
                </a>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/theater/list" style="text-decoration: none; color:black">
                    <h5>상영관 관리</h5>
                </a>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/user/list" style="text-decoration: none; color:black">
                    <h5>회원 관리</h5>
                </a>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/payment/list" style="text-decoration: none; color:black">
                    <h5 style="color:#e55039;">결제 관리</h5>
                </a>
            </div>
        </div>
        <div class="px-4" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="mt-5 px-4" style="display:flex; width:100%; align-items:center;">
                <p class="fs-5"><b>모집 목록 관리</b>(총 <span
                        style="color:#74b9ff">${paymentList.page.totalElements}</span>개)
                </p>
            </div>
            <div class="px-4"
                 style="
                    display:flex;
                    width:100%;
                    height: 90px;
                    align-items:center;
                    justify-content:flex-start;
                    background-color: #dfe6e9;
                    border-radius: 10px;
                "
            >
                <div style="display: flex; width:270px; height: 100%; flex-direction: column">
                    <div style="display: flex; height: 30px; align-items: flex-end;">
                        <p><b>조회기간</b></p>
                    </div>
                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                        <div class="btn-group btn-group-sm" role="group" aria-label="Basic radio toggle button group">
                            <c:choose>
                                <c:when test="${fromDate == today && toDate == today}">
                                    <input type="radio" class="btn-check" name="simpleDate" value="today" id="today"
                                           autocomplete="off" onclick="onClickSimpleDate(this.value)" checked>
                                    <label class="btn btn-outline-secondary" for="today">
                                        오늘
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" class="btn-check" name="simpleDate" value="today" id="today"
                                           autocomplete="off" onclick="onClickSimpleDate(this.value)">
                                    <label class="btn btn-outline-secondary" for="today">
                                        오늘
                                    </label>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fromDate == parseBeforeWeek && toDate == today}">
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="aWeek" id="aWeek"
                                           autocomplete="off" checked>
                                    <label class="btn btn-outline-secondary" for="aWeek">일주일</label>
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="aWeek" id="aWeek"
                                           autocomplete="off">
                                    <label class="btn btn-outline-secondary" for="aWeek">일주일</label>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fromDate == parseBeforeMonth && toDate == today}">
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="aMonth" id="aMonth"
                                           autocomplete="off" checked>
                                    <label class="btn btn-outline-secondary" for="aMonth">1개월</label>
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="aMonth" id="aMonth"
                                           autocomplete="off">
                                    <label class="btn btn-outline-secondary" for="aMonth">1개월</label>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fromDate == parseBefore3Month && toDate == today}">
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="3month" id="3month"
                                           autocomplete="off" checked>
                                    <label class="btn btn-outline-secondary" for="3month">3개월</label>
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" class="btn-check" onclick="onClickSimpleDate(this.value)"
                                           name="simpleDate" value="3month" id="3month"
                                           autocomplete="off">
                                    <label class="btn btn-outline-secondary" for="3month">3개월</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div style="display: flex; width:290px; height: 100%; flex-direction: column">
                    <div style="display: flex; height: 30px; align-items: flex-end;">
                    </div>
                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                        <input type="date" id="fromDate" name="trip-start" onchange="onclickSelectDate()"
                               value="${fromDate}" min="2022-01-01"
                               max="2099-12-30"
                        >
                        <p>&nbsp;~&nbsp;</p>
                        <input type="date" id="toDate" name="trip-start" onchange="onclickSelectDate()"
                               value="${toDate}" min="2022-01-01"
                               max="2099-12-30">
                    </div>
                </div>
                <div class="ms-2" style="display: flex; width:100px; height: 100%; flex-direction: column">
                    <div style="display: flex; height: 30px; align-items: flex-end;">
                        상세 조건
                    </div>
                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                        <select class="form-select-sm" aria-label="Default select example" id="searchCondition">
                            <c:choose>
                                <c:when test="${param.searchCondition == '' || param.searchCondition == 'movieName'}">
                                    <option value="movieName" selected>영화 이름</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="movieName">영화 이름</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.searchCondition == 'userName'}">
                                    <option value="userName" selected>유저 이름</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="userName">유저 이름</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.searchCondition == 'userEmail'}">
                                    <option value="userEmail" selected>유저 이메일</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="userEmail">유저 이메일</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
                <div class="ms-2" style="display: flex; width:300px; height: 100%; flex-direction: column">
                    <div style="display: flex; height: 30px; align-items: flex-end;">

                    </div>
                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                        <input type="text" class="form-control form-control-sm ms-2" style="width:300px" id="searchWord"
                               value="${param.searchWord}"
                               placeholder="검색어를 입력해 주세요">
                    </div>
                </div>
                <div class="ms-2" style="display: flex; width:50px; height: 100%; flex-direction: column">
                    <div style="display: flex; height: 30px; align-items: flex-end;">

                    </div>
                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                        <button type="button" class="btn btn-secondary btn-sm" onclick="onClickSearch()">&nbsp;검색&nbsp;
                        </button>
                    </div>
                </div>
            </div>
            <c:choose>
                <c:when test="${paymentList.page.totalElements == 0}">
                    <div class="mt-4"
                         style="display: flex;width: 100%; height: 600px; justify-content: center; align-items: center;">
                        <h4>해당 조건의 리스트가 없습니다.</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="width: 100%; height: 50px; display: flex; align-items: flex-end; justify-content: flex-end">
                        <div style="width: 150px;">
                            <select class="form-select" aria-label="Default select example" id="paymentSize"
                                    onchange="changePageSize()">
                                <c:choose>
                                    <c:when test="${param.size == '' || param.size == 10}">
                                        <option value="10" selected>10개씩 보기</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="10">10개씩 보기</option>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${param.size == 20}">
                                        <option value="20" selected>20개씩 보기</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="20">20개씩 보기</option>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${param.size == 50}">
                                        <option value="50" selected>50개씩 보기</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="50">50개씩 보기</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>
                    <table class="table table-hover mt-4 border-top align-middle text-center">
                        <thead class="table-light">
                        <tr>
                            <th scope="col" style="width:5%">번호</th>
                            <th scope="col" style="width:10%">상태</th>
                            <th scope="col" style="width:15%">결제 번호</th>
                            <th scope="col" style="width:15%">예약 영화</th>
                            <th scope="col" style="width:10%">금액</th>
                            <th scope="col" style="width:10%">아이디</th>
                            <th scope="col" style="width:10%">이름</th>
                            <th scope="col" style="width:15%">결제일시</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="payment" items="${paymentList.page.content}" varStatus="status">
                            <fmt:parseDate value="${ payment.paidAt }" pattern="yyyy-MM-dd'T'HH:mm"
                                           var="parsePaidAt" type="both"/>
                            <tr style="cursor:pointer" onclick="goToDetail('${payment.paymentId}')">
                                <th scope="row">${paymentList.page.pageable.offset + status.index + 1}</th>
                                <c:choose>
                                    <c:when test="${payment.paymentStatus == 'COMPLETE'}">
                                        <td>
                                            결제 완료
                                        </td>
                                    </c:when>
                                    <c:when test="${payment.paymentStatus == 'CANCEL'}">
                                        <td>
                                            결제 취소
                                        </td>
                                    </c:when>
                                    <c:when test="${payment.paymentStatus == 'FAILURE'}">
                                        <td>
                                            결제 실패
                                        </td>
                                    </c:when>
                                </c:choose>
                                <td>${payment.paymentId}</td>
                                <td>${payment.movieName}</td>
                                <td><fmt:formatNumber value="${payment.paymentAmount }" pattern="#,###"/>원</td>
                                <td>${payment.userEmail}</td>
                                <td>${payment.userName}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsePaidAt }"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <nav aria-label="Page navigation example">
                        <ul class="pagination justify-content-center">
                            <c:if test="${paymentList.page.first == false && paymentList.page.pageable.pageNumber > 10}">
                                <li class="page-item"><a class="page-link" href="#">이전</a></li>
                            </c:if>
                            <c:forEach var="pageNum" begin="${(paymentList.page.pageable.pageNumber+10)/10}"
                                       end="${((paymentList.page.pageable.pageNumber+10)/10)+9}">
                                <c:if test="${pageNum <= paymentList.page.totalPages}">
                                    <c:choose>
                                        <c:when test="${pageNum == paymentList.page.pageable.pageNumber+1}">
                                            <li class="page-item active"><a class="page-link"> ${pageNum}</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="page-item"><a class="page-link"
                                                                     onclick="gotoPagination(${pageNum})">${pageNum}</a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                            <c:if test="${paymentList.page.last == false && ((paymentList.page.pageable.pageNumber+10)/10)+9 < paymentList.page.totalPages}">
                                <li class="page-item"><a class="page-link"
                                                         onclick="gotoPagination(`${((paymentList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
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

    function onClickSimpleDate(simpleValue) {

        let fromDate;
        let toDate;

        console.log(simpleValue);
        switch (simpleValue) {
            case "today": {
                fromDate = "${today}";
                toDate = "${today}";
                break;
            }
            case "aWeek" : {
                fromDate = "${beforeWeek}";
                toDate = "${today}";
                break;
            }
            case "aMonth" : {
                fromDate = "${beforeMonth}";
                toDate = "${today}";
                break;
            }
            case "3month" : {
                fromDate = "${before3Month}";
                toDate = "${today}";
                break;
            }
        }
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";

        location.href = "<%=CURRENT_SERVER%>/admin/payment/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&fromDate=" + fromDate + "&toDate=" + toDate;
        console.log("fromDate =", fromDate);
        console.log("toDate =", toDate);
    }

    function onclickSelectDate() {
        let fromDate = $("#fromDate").val();
        let toDate = $("#toDate").val();

        if (fromDate > toDate) {
            console.log(toDate)
            $("#fromDate").val(toDate)
        } else {
            if (toDate < fromDate) {
                console.log(fromDate)
                $("#toDate").val(fromDate)
            }
        }
    }

    function onClickSearch() {
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        let fromDate = $("#fromDate").val();
        let toDate = $("#toDate").val();

        console.log("searchWord =", searchWord);
        console.log("searchCondition =", searchCondition);
        console.log("fromDate =", fromDate);
        console.log("toDate =", toDate);

        location.href = "<%=CURRENT_SERVER%>/admin/payment/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&fromDate=" + fromDate + "&toDate=" + toDate;
    }

    function changePageSize() {
        let size = $("#paymentSize").val();
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        let fromDate = "${fromDate}";
        let toDate = "${toDate}";



        location.href = "<%=CURRENT_SERVER%>/admin/payment/list?page=0&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&fromDate=" + fromDate + "&toDate=" + toDate;
    }

    function gotoPagination(pageNum) {
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        let fromDate = "${fromDate}";
        let toDate = "${toDate}";
        let size = "${param.size}"
        if (size === "") {
            size = 10
        }
        location.href = "<%=CURRENT_SERVER%>/admin/payment/list?page="+(pageNum - 1)+"&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&fromDate=" + fromDate + "&toDate=" + toDate;
    }

    function goToDetail(paymentId) {
        console.log(paymentId);
        location.href = "<%=CURRENT_SERVER%>/admin/payment/info/" + paymentId;
    }
</script>
</body>

</html>