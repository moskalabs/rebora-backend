<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/11/30
  Time: 2:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/header.jsp" %>
    <style>
        tr {
            cursor: pointer;
        }

        p {
            margin: 0;
        }

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
    </style>
</head>

<body class="hold-transition sidebar-mini" style="min-width: 1600px">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>극장 관리(총 <span style="color:#74b9ff">${cinemaList.page.totalElements}</span>개)</h4>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" style="display: flex; flex-direction: row;">
                                <div class="btn-group btn-group-toggle" data-toggle="buttons" style="width: 270px;">
                                    <c:choose>
                                        <c:when test="${param.cinemaBrand == 'CGV' || param.cinemaBrand == '' || param.cinemaBrand == null}">
                                            <label class="btn bg-purple focus active">
                                                <input type="radio" class="btn-check" name="theaterCinemaBrandName"
                                                       value="CGV"
                                                       id="CGV"
                                                       autocomplete="off" checked>
                                                CGV
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="btn bg-purple">
                                                <input type="radio" class="btn-check" name="theaterCinemaBrandName"
                                                       value="CGV"
                                                       id="CGV"
                                                       autocomplete="off">
                                                CGV
                                            </label>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${param.cinemaBrand == '롯데시네마'}">
                                            <label class="btn bg-purple focus active">
                                                <input type="radio" class="btn-check" name="theaterCinemaBrandName"
                                                       value="롯데시네마"
                                                       id="롯데시네마"
                                                       autocomplete="off" checked>
                                                롯데시네마
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="btn bg-purple">
                                                <input type="radio" class="btn-check"
                                                       name="theaterCinemaBrandName" value="롯데시네마" id="롯데시네마"
                                                       autocomplete="off">
                                                롯데시네마
                                            </label>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${param.cinemaBrand == '메가박스'}">
                                            <label class="btn bg-purple focus active">
                                                <input type="radio" class="btn-check" name="theaterCinemaBrandName"
                                                       value="메가박스"
                                                       id="메가박스"
                                                       autocomplete="off" checked>
                                                메가박스
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="btn bg-purple">
                                                <input type="radio" class="btn-check"
                                                       name="theaterCinemaBrandName" value="메가박스" id="메가박스"
                                                       autocomplete="off">
                                                메가박스
                                            </label>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="form-group ml-3" style="display:flex; width:130px; height: 22px;">
                                    <select class="form-control select2bs4" aria-label="Default select example"
                                            id="searchCondition">
                                        <c:choose>
                                            <c:when test="${param.searchCondition == '' || param.searchCondition == 'cinemaRegion'}">
                                                <option value="cinemaRegion" selected>지역</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="cinemaRegion">지역</option>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${param.searchCondition == '' || param.searchCondition == 'cinemaName'}">
                                                <option value="cinemaName" selected>극장명</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="cinemaName">극장명</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                                <input type="text" value="${param.searchWord}" class="form-control ml-2"
                                       style="width:50%" id="searchWord"
                                       placeholder="검색어를 입력해 주세요">
                                <button type="button" class="btn btn-primary ml-3" style="width: 80px;"
                                        onclick="searchCinema()">&nbsp;검색&nbsp;
                                </button>
                                <button type="button" class="btn bg-purple ml-3" style="width: 100px;"
                                        onclick="createCinema()">&nbsp;극장 등록
                                </button>
                                <div class="form-group ml-4" style="display:flex; width:130px; height: 22px;">
                                    <select class="form-control select2bs4" style="width: 100%;" id="cinemaSize"
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
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <c:choose>
                                <c:when test="${cinemaList.page.totalElements != 0}">
                                    <div class="card-body table-responsive p-0">
                                        <table class="table table-hover text-center text-nowrap">
                                            <thead>
                                            <tr>
                                                <th>번호</th>
                                                <th>브랜드</th>
                                                <th>지역</th>
                                                <th>극장</th>
                                                <th>현재 상영관 수</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="cinema" items="${cinemaList.page.content}"
                                                       varStatus="status">
                                                <tr onclick="goToDetailInfo(${cinema.cinemaId})">
                                                    <th scope="row">${cinemaList.page.pageable.offset + status.index + 1}</th>
                                                    <td>${cinema.brandName}</td>
                                                    <td>${cinema.regionName}</td>
                                                    <td>${cinema.cinemaName}</td>
                                                    <td>${cinema.theaterCount}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="card-footer clearfix">
                                        <ul class="pagination pagination-sm m-0 float-right">
                                            <c:if test="${cinemaList.page.first == false && cinemaList.page.pageable.pageNumber > 10}">
                                                <li class="page-item"><a class="page-link" href="#">이전</a></li>
                                            </c:if>
                                            <c:forEach var="pageNum"
                                                       begin="${(cinemaList.page.pageable.pageNumber+10)/10}"
                                                       end="${((cinemaList.page.pageable.pageNumber+10)/10)+9}">
                                                <c:if test="${pageNum <= cinemaList.page.totalPages}">
                                                    <c:choose>
                                                        <c:when test="${pageNum == cinemaList.page.pageable.pageNumber+1}">
                                                            <li class="page-item active"><a
                                                                    class="page-link"> ${pageNum}</a>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li class="page-item"><a class="page-link"
                                                                                     onclick="gotoPagination(${pageNum})">${pageNum}</a>
                                                            </li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${movieList.page.last == false && ((cinemaList.page.pageable.pageNumber+10)/10)+9 < movieList.page.totalPages}">
                                                <li class="page-item"><a class="page-link"
                                                                         onclick="gotoPagination(`${((cinemaList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div style="display: flex; align-items: center; justify-content: center; width: 100%; height: 200px;">
                                        <p>해당 조건의 게시글이 없습니다.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
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

    function createCinema() {
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/info";
    }

    function goToDetailInfo(cinemaId) {
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/info?cinemaId=" + cinemaId
    }

    function movieCountResultAll() {
        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/changeMoviePopularCount",
            data    : {
                movieAll: "all"
            },
            method  : "POST",
            dataType: "json"
        }).done(function (data) {
            console.log(data);
            if (!data.result) {
                Swal.fire({
                    title: "오류",
                    text : data.message
                })
            } else {
                Swal.fire({
                    title: "완료",
                    text : "카운팅 리셋이 완료되었습니다.",
                }).then(() => {
                    location.reload();
                })
            }
        })
    }

    function changePageSize() {
        let cinemaSize = $("#cinemaSize").val();
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";


        let cinemaBrand = "${param.cinemaBrand}";
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/list?page=" + 0 + "&size=" + cinemaSize + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&cinemaBrand=" + cinemaBrand;
    }

    function searchCinema() {
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        let cinemaBrand = "${param.cinemaBrand}";
        console.log(searchWord);
        console.log(searchCondition);
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&cinemaBrand=" + cinemaBrand;
    }

    function gotoPagination(pageNum) {
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        let size = "${param.size}"
        if (size === "") {
            size = 10
        }
        location.href = "<%=CURRENT_SERVER%>/admin/movie/list?page=" + (pageNum - 1) + "&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }

    $('input[name=theaterCinemaBrandName]').click(() => {
        let cinemaBrand = $('input[name=theaterCinemaBrandName]:checked').val()
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&cinemaBrand=" + cinemaBrand;
    })

</script>
</body>
</html>