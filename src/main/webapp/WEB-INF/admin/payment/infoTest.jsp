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
<head>
    <%@include file="../include/header.jsp" %>
</head>
<body class="hold-transition sidebar-mini layout-boxed">
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
                        <h4>모집 목록 관리</h4>
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
                                        <th>ID</th>
                                        <th>User</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                        <th>Reason</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>183</td>
                                        <td>John Doe</td>
                                        <td>11-7-2014</td>
                                        <td><span class="tag tag-success">Approved</span></td>
                                        <td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
                                    </tr>
                                    <tr>
                                        <td>219</td>
                                        <td>Alexander Pierce</td>
                                        <td>11-7-2014</td>
                                        <td><span class="tag tag-warning">Pending</span></td>
                                        <td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
                                    </tr>
                                    <tr>
                                        <td>657</td>
                                        <td>Bob Doe</td>
                                        <td>11-7-2014</td>
                                        <td><span class="tag tag-primary">Approved</span></td>
                                        <td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
                                    </tr>
                                    <tr>
                                        <td>175</td>
                                        <td>Mike Doe</td>
                                        <td>11-7-2014</td>
                                        <td><span class="tag tag-danger">Denied</span></td>
                                        <td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.card-body -->
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
<script>
    $(document).ready(function () {
        $('#reservation').daterangepicker()
    });

    function onChangeValue(value) {
        console.log(value);
    }
</script>
</body>
</html>
