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

        #movieImageFile {
            display: none;
        }

        .label-file {
            display: inline-block;
            font-weight: 400;
            text-align: center;
            vertical-align: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
            padding: 0.375rem 0.75rem;
            font-size: 1rem;
            line-height: 1.5;
            border-radius: 0.25rem;
            transition: color .15s ease-in-out, background-color .15s ease-in-out, border-color .15s ease-in-out, box-shadow .15s ease-in-out;
            color: #fff;
            background-color: #6c757d;
            border-color: #6c757d;
            box-shadow: none;
            cursor: pointer;
            overflow: visible;
            text-transform: none;
        }
    </style>
</head>

<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>영화 관리(총 <span style="color:#74b9ff">${movieList.page.totalElements}</span>개)</h4>
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
                                <button type="button" class="btn btn-secondary">전체 누적 카운팅 리셋</button>
                                <div class="ml-4" style="display:flex; width:130px;">
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
                                            <c:when test="${param.searchCondition == '' || param.searchCondition == 'brand'}">
                                                <option value="brand" selected>상영 브랜드</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="brand">상영 브랜드</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                                <input type="text" value="${param.searchWord}" class="form-control ml-2"
                                       style="width:300px" id="searchWord"
                                       placeholder="검색어를 입력해 주세요">
                                <button type="button" class="btn btn-secondary ml-3" onclick="searchMovie()">&nbsp;검색&nbsp;</button>
                                <div class="ml-4" style="display:flex; width:130px;">
                                    <select class="custom-select" aria-label="Default select example" id="movieSize"
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
                                <button type="button" class="btn btn-secondary ml-3" onclick="goToCreateMovie()">
                                    영화 등록
                                </button>
                                <label style="margin: 0" class="ml-3" for="movieImageFile">
                                    <div class="label-file">
                                        영화 이미지 등록
                                    </div>
                                </label>
                                <input type="file" name="movieImageFile" id="movieImageFile" webkitdirectory>
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
                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                    <tr>
                                        <th>번호</th>
                                        <th>영화 이름</th>
                                        <th>포스터 사진</th>
                                        <th>상영 시간</th>
                                        <th>일주일 누적 카운팅</th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="movie" items="${movieList.page.content}" varStatus="status">
                                        <tr onclick="goToDetailInfo(${movie.id})">
                                            <th scope="row">${movieList.page.pageable.offset + status.index + 1}</th>
                                            <td>${movie.movieName}</td>
                                            <td><img src="${movie.movieImage}"
                                                     class="img-fluid mb-2" style="height: 200px; border-radius: 4px"
                                                     alt="..."></td>
                                            <td>${movie.movieRunningTime}분</td>
                                            <td>${movie.moviePopularCount} </td>
                                            <td>
                                                <button type="button" onclick="movieCountResult(${movie.id})"
                                                        class="btn btn-secondary ms-3 btn-secondary">&nbsp;카운팅 리셋&nbsp;
                                                </button>
                                            </td>
                                            <td>
                                                <button type="button" onclick="goToDetailInfo(${movie.id})"
                                                        class="btn btn-secondary ms-3 btn-secondary">&nbsp;수정&nbsp;
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer clearfix">
                                <ul class="pagination pagination-sm m-0 float-right">
                                    <c:if test="${movieList.page.first == false && movieList.page.pageable.pageNumber > 10}">
                                        <li class="page-item"><a class="page-link" href="#">이전</a></li>
                                    </c:if>
                                    <c:forEach var="pageNum" begin="${(movieList.page.pageable.pageNumber+10)/10}"
                                               end="${((movieList.page.pageable.pageNumber+10)/10)+9}">
                                        <c:if test="${pageNum <= movieList.page.totalPages}">
                                            <c:choose>
                                                <c:when test="${pageNum == movieList.page.pageable.pageNumber+1}">
                                                    <li class="page-item active"><a class="page-link"> ${pageNum}</a>
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
                                    <c:if test="${movieList.page.last == false && ((movieList.page.pageable.pageNumber+10)/10)+9 < movieList.page.totalPages}">
                                        <li class="page-item"><a class="page-link"
                                                                 onclick="gotoPagination(`${((movieList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <div class="container-fluid">
            <div class="row">
                <div style="display:flex; width: 100%; height:120px">
                </div>
            </div>
        </div>
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

    function goToDetailInfo(movieId) {
        event.stopPropagation();
        location.href = "<%=CURRENT_SERVER%>/admin/movie/info?movieId=" + movieId
    }

    function goToCreateMovie() {
        location.href = "<%=CURRENT_SERVER%>/admin/movie/info";
    }

    function movieCountResult(movieId) {
        event.stopPropagation();
        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/changeMoviePopularCount",
            data    : {
                movieId
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
        let movieSize = $("#movieSize").val();
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        location.href = "<%=CURRENT_SERVER%>/admin/movie/list?page=" + 0 + "&size=" + movieSize + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }

    function searchMovie() {
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        location.href = "<%=CURRENT_SERVER%>/admin/movie/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
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

    const movieImageFile = document.querySelector('#movieImageFile');
    movieImageFile.addEventListener("change", uploadImageFile)

    function uploadImageFile() {
        let formData = new FormData();

        let onValidFile = true;
        let fileList = movieImageFile.files;

        if (fileList.length === 0) {
            Swal.fire({
                title: "오류",
                text : "파일이 존재하지 않습니다.",
            }).then(() => {
                return false;
            })
        }

        for (let x of fileList) {
            let splitList = x.type.split("/");
            if (splitList[1] !== "png" && splitList[1] !== "jpg" && splitList[1] !== "jpeg") {
                onValidFile = false;
                Swal.fire({
                    title: "오류",
                    text : "이미지가 아닌 파일이 있습니다.",
                }).then(() => {
                })
            } else {
                formData.append("files", x);
            }

        }

        if (onValidFile) {
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
                url        : "<%=CURRENT_SERVER%>/admin/movie/uploadImageFolder",
                headers    : {
                    "token": token
                },
                enctype    : 'multipart/form-data',
                data       : formData,
                processData: false,
                contentType: false,
                method     : "POST",
                error : function (data){
                    Swal.fire({
                        title: "오류",
                        html : data.responseJSON.message,
                    }).then(() => {
                        location.reload();
                    })
                }
            }).done(function (data) {
                if (data.result === true) {
                    Swal.fire({
                        title: "완료",
                        text : "이미지 저장이 완료 되었습니다.",
                    }).then(() => {
                        location.reload();
                    })
                }
            })
        }
    }

</script>
</body>
</html>
