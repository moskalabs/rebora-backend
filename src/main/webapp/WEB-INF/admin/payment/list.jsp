<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/21
  Time: 2:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/header.jsp" %>
</head>
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
<style>
    p {
        margin: 0;
    }
</style>
<body class="hold-transition sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <!-- Navbar -->
    <%@include file="../include/sidebar.jsp" %>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>결제 관리</h4>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" style="display: flex; flex-direction: row">
                                <div style="display: flex; width:260px; height: 100%; flex-direction: column">
                                    <div style="display: flex; height: 30px; align-items: flex-end;">
                                        <p><b>조회기간</b></p>
                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                                        <div class="btn-group btn-group-toggle btn-group-sm" data-toggle="buttons">
                                            <c:choose>
                                                <c:when test="${fromDate == today && toDate == today}">
                                                    <label class="btn btn-secondary active">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="today" id="today"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                               checked>
                                                        오늘
                                                    </label>
                                                </c:when>
                                                <c:otherwise>
                                                    <label class="btn btn-secondary active">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="today" id="today"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                               checked>
                                                        오늘
                                                    </label>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${fromDate == parseBeforeWeek && toDate == today}">
                                                    <label class="btn btn-secondary active">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="aWeek" id="aWeek"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                               checked>
                                                        일주일
                                                    </label>
                                                </c:when>
                                                <c:otherwise>
                                                    <label class="btn btn-secondary">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="aWeek" id="aWeek"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                        >
                                                        일주일
                                                    </label>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${fromDate == parseBeforeMonth && toDate == today}">
                                                    <label class="btn btn-secondary active">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="aMonth" id="aMonth"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                               checked
                                                        >
                                                        1개월
                                                    </label>
                                                </c:when>
                                                <c:otherwise>
                                                    <label class="btn btn-secondary">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="aMonth" id="aMonth"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                        >
                                                        1개월
                                                    </label>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${fromDate == parseBefore3Month && toDate == today}">
                                                    <label class="btn btn-secondary active">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="3month" id="3month"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                               checked
                                                        >
                                                        3개월
                                                    </label>
                                                </c:when>
                                                <c:otherwise>
                                                    <label class="btn btn-secondary">
                                                        <input type="radio" class="btn-check" name="simpleDate"
                                                               value="3month" id="3month"
                                                               autocomplete="off"
                                                               onclick="onClickSimpleDate(this.value)"
                                                        >
                                                        3개월
                                                    </label>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                <div style="display: flex; width:280px; height: 100%; flex-direction: column">
                                    <div style="display: flex; height: 30px; align-items: flex-end;">
                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                                        <div class="input-group">
                                            <input type="date" id="fromDate" name="trip-start"
                                                   onchange="onclickSelectDate()"
                                                   value="${fromDate}" min="2022-01-01"
                                                   max="2099-12-30"
                                            >
                                            <p>&nbsp;~&nbsp;</p>
                                            <input type="date" id="toDate" name="trip-start"
                                                   onchange="onclickSelectDate()"
                                                   value="${toDate}" min="2022-01-01"
                                                   max="2099-12-30">
                                        </div>
                                    </div>
                                </div>
                                <div
                                        style="display: flex; width:100px; height: 100%; flex-direction: column; margin-left: 10px;">
                                    <div style="display: flex; height: 30px; align-items: flex-end;">
                                        상세 조건
                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                                        <select class="custom-select" aria-label="Default select example"
                                                id="searchCondition">
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
                                <div class="ms-2"
                                     style="display: flex; width:200px; height: 100%; flex-direction: column; margin-left: 10px;">
                                    <div style="display: flex; height: 30px; align-items: flex-end;">

                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                                        <input type="text" class="form-control form-control-sm ms-2" style="width:300px"
                                               id="searchWord"
                                               value="${param.searchWord}"
                                               placeholder="검색어를 입력해 주세요">
                                    </div>
                                </div>
                                <div
                                        style="display: flex; width:50px; height: 100%; flex-direction: column; margin-left: 10px;">
                                    <div style="display: flex; height: 30px; align-items: flex-end;">

                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: flex-start; height: 50px;">
                                        <button type="button" class="btn btn-secondary btn-sm"
                                                onclick="onClickSearch()">&nbsp;검색&nbsp;
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <!-- /.card-header -->
                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                    <tr>
                                        <th>번호</th>
                                        <th>상태</th>
                                        <th>결제 번호</th>
                                        <th>예약 영화</th>
                                        <th>금액</th>
                                        <th>아이디</th>
                                        <th>이름</th>
                                        <th>결제일시</th>
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
                                            <td><fmt:formatNumber value="${payment.paymentAmount }" pattern="#,###"/>원
                                            </td>
                                            <td>${payment.userEmail}</td>
                                            <td>${payment.userName}</td>
                                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
                                                                value="${ parsePaidAt }"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.card-body -->
                            <div class="card-footer clearfix">
                                <ul class="pagination pagination-sm m-0 float-right">
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
                                                                             onclick="gotoPagination(${pageNum})">${pageNum}</a></li>
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
                            </div>
                        </div>
                        <!-- /.card -->
                    </div>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
        <!-- Control sidebar content goes here -->
    </aside>
    <!-- /.control-sidebar -->
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
        location.href = "<%=CURRENT_SERVER%>/admin/payment/list?page=" + (pageNum - 1) + "&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&fromDate=" + fromDate + "&toDate=" + toDate;
    }

    function goToDetail(paymentId) {
        console.log(paymentId);
        location.href = "<%=CURRENT_SERVER%>/admin/payment/info/" + paymentId;
    }
</script>
</body>
</html>
