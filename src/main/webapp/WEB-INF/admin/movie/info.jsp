<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/01
  Time: 10:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/header.jsp" %>
    <style>
        tr {
            height: 60px;
            font-size: 20px;
        }

        .border-end {
            border-right: 1px solid #dee2e6;
        }

        html {
            overflow-y: hidden;
        }

        label {
            font-weight: normal;
        }
    </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <!-- Navbar -->
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>영화 상세</h4>
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
                                <tbody>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 이름</th>
                                    <td style="width: 80%;">
                                        <input type="text" value="${response.content.movieName}" style="width:250px"
                                               class="form-control" id="movieName"
                                               aria-describedby="emailHelp">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영 등급</th>
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieRating == 'ALL'}">
                                                        <input type="radio" value="ALL"
                                                               name="movieRating"
                                                               id="ALL" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="ALL"
                                                               name="movieRating"
                                                               id="ALL">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="ALL">
                                                    전체 이용가
                                                </label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieRating == 'TWELVE'}">
                                                        <input type="radio" value="TWELVE"
                                                               name="movieRating"
                                                               id="TWELVE"
                                                               checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="TWELVE"
                                                               name="movieRating"
                                                               id="TWELVE">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="TWELVE">
                                                    12세 이용가
                                                </label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieRating == 'FIFTEEN'}">
                                                        <input type="radio"
                                                               value="FIFTEEN"
                                                               name="movieRating"
                                                               id="FIFTEEN"
                                                               checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio"
                                                               value="FIFTEEN"
                                                               name="movieRating"
                                                               id="FIFTEEN">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="FIFTEEN">
                                                    15세 이용가
                                                </label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieRating == 'ADULT'}">
                                                        <input type="radio" value="ADULT"
                                                               name="movieRating"
                                                               id="ADULT" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="ADULT"
                                                               name="movieRating"
                                                               id="ADULT">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="ADULT">
                                                    성인
                                                </label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">감독</th>
                                    <td style="width: 80%;"><input type="text" value="${response.content.movieDirector}"
                                                                   style="width:250px"
                                                                   class="form-control mb-2" id="movieDirector"
                                                                   aria-describedby="emailHelp"></td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">러닝타임(분)</th>
                                    <td style="width: 80%;"><input type="text"
                                                                   value="${response.content.movieRunningTime}"
                                                                   style="width:250px"
                                                                   class="form-control mb-2" id="movieRunningTime"
                                                                   aria-describedby="emailHelp"></td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 가격</th>
                                    <td style="width: 80%;"><input type="text"
                                                                   value="${response.content.moviePrice}"
                                                                   style="width:100px"
                                                                   class="form-control mb-2" id="moviePrice"
                                                                   aria-describedby="emailHelp"></td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">평점</th>
                                    <td style="width: 80%;"><input type="text"
                                                                   value="${response.content.convertStartRation}"
                                                                   style="width:100px"
                                                                   class="form-control mb-2" id="movieStarRating"
                                                                   aria-describedby="emailHelp"></td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row">장르</th>
                                    <td>
                                        <div class="form-group clearfix"
                                             style="width: 100%; display:grid; grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr; grid-auto-rows:40px; font-size:15px; gap: 5px">
                                            <c:forEach var="category" items="${response.content.categoryList}"
                                                       varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${category.categoryYn}">
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="category"
                                                                   value="${category.id}"
                                                                   id="${category.categoryName}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="category"
                                                                   value="${category.id}"
                                                                   id="${category.categoryName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label style="font-size: 12px;" for="${category.categoryName}">
                                                            ${category.categoryName}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row">장르</th>
                                    <td>
                                        <div class="form-group clearfix"
                                             style="width: 100%; display:grid; grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr; grid-auto-rows:40px; font-size:15px; gap: 5px">
                                            <c:forEach var="cinema" items="${response.content.cinemaMovieDtoList}"
                                                       varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${cinema.cinemaYn}">
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="cinema"
                                                                   value="${cinema.cinemaId}"
                                                                   id="${cinema.cinemaId}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="cinema"
                                                                   value="${cinema.cinemaId}"
                                                                   id="${cinema.cinemaId}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label style="font-size: 12px;" for="${cinema.cinemaId}">
                                                            ${cinema.brandName} ${cinema.regionName} ${cinema.cinemaName}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 상세 링크</th>
                                    <td style="width: 80%;">
                                        <input type="text" value="${response.content.movieDetailLink}" style="width:80%"
                                               class="form-control mb-2" id="movieDetailLink"
                                               aria-describedby="emailHelp">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 누적 카운트</th>
                                    <td style="width: 80%;">
                                        <input type="text" value="${response.content.moviePopularCount}"
                                               style="width:80%"
                                               class="form-control mb-2" id="moviePopularCount"
                                               aria-describedby="emailHelp">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 포스터</th>
                                    <td>
                                        <c:if test="${response.content.movieImage != null}">
                                            <img src="${response.content.movieImage}"
                                                 style="width: 200px;" class="img-thumbnail" alt="...">
                                        </c:if>
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" id="changeMovieImage"
                                                   accept="image/gif,image/jpeg,image/png">
                                            <label class="custom-file-label" for="changeMovieImage">파일을 선택해주세요</label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 배너 이미지</th>
                                    <td>
                                        <c:if test="${response.content.movieBannerImage != null}">
                                            <img src="${response.content.movieBannerImage}"
                                                 style="width: 400px;" class="img-thumbnail" alt="...">
                                        </c:if>
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" id="changeMovieBannerImage"
                                                   accept="image/gif,image/jpeg,image/png">
                                            <label class="custom-file-label" for="changeMovieBannerImage">파일을
                                                선택해주세요</label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">영화 모집 이미지</th>
                                    <td>
                                        <c:if test="${response.content.movieRecruitmentImage != null}">
                                            <img src="${response.content.movieRecruitmentImage}"
                                                 style="width: 400px;" class="img-thumbnail" alt="...">
                                        </c:if>
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input"
                                                   id="changeMovieRecruitmentImage"
                                                   accept="image/gif,image/jpeg,image/png">
                                            <label class="custom-file-label" for="changeMovieRecruitmentImage">파일을
                                                선택해주세요</label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">영화 활성화 여부</th>
                                    <td style="width: 100%; height: 60px; font-size:16px; align-items: center">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieUseYn == true}">
                                                        <input style="margin: 0" class="form-check-input" type="radio"
                                                               value="true"
                                                               name="movieUseYn"
                                                               id="movieUseY" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input style="margin: 0" class="form-check-input" type="radio"
                                                               value="true"
                                                               name="movieUseYn"
                                                               id="movieUseY">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label class="form-check-label" for="movieUseY">
                                                    예
                                                </label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${response.content.movieUseYn == false}">
                                                        <input style="margin: 0" class="form-check-input" type="radio"
                                                               value="false"
                                                               name="movieUseY"
                                                               id="movieUseN" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input style="margin: 0" class="form-check-input" type="radio"
                                                               value="false"
                                                               name="movieUseN"
                                                               id="movieUseN">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label class="form-check-label" for="movieUseN">
                                                    아니요
                                                </label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container-fluid">
                <div class="row">
                    <div style="display:flex; width: 100%; height:120px">
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-start">
                            <button type="button" class="btn btn-secondary" onclick="goToList()">
                                &nbsp;목록&nbsp;
                            </button>
                        </div>
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                            <button type="button" class="btn btn-success mr-3" onclick="saveMovie()">&nbsp;저장&nbsp;
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
<script>
    $(document).ready(function () {
        bsCustomFileInput.init();

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

    function saveMovie() {
        let movieId = "${param.movieId}";
        let movieName = $("#movieName").val();
        let movieRating = $('input:radio[name=movieRating]:checked').val();
        let movieDirector = $("#movieDirector").val();
        let movieStarRating = $("#movieStarRating").val();
        let moviePrice = $("#moviePrice").val();
        let movieUseYn = $('input:radio[name=movieUseYn]:checked').val();

        let category = [];
        $("input:checkbox[name='category']").each(function (index, value) {
            if (value.checked) {
                console.log(value.value)
                category.push(value.value)
            }
        })

        let cinema = [];
        $("input:checkbox[name='cinema']").each(function (index, value) {
            if (value.checked) {
                console.log(value.value)
                cinema.push(value.value)
            }
        })

        let movieRunningTime = $("#movieRunningTime").val();
        let movieDetailLink = $("#movieDetailLink").val();
        let moviePopularCount = $("#moviePopularCount").val();
        let changeMovieImage = $('input#changeMovieImage')[0].files[0]
        let changeMovieBannerImage = $('input#changeMovieBannerImage')[0].files[0]
        let changeMovieRecruitmentImage = $('input#changeMovieRecruitmentImage')[0].files[0]

        let formData = new FormData();

        if (movieName != "") {
            formData.append("movieName", movieName);
        }

        if (movieRating != "") {
            formData.append("movieRating", movieRating);
        }

        if (movieDirector != "") {
            formData.append("movieDirector", movieDirector);
        }

        if (movieStarRating != "") {
            formData.append("movieStarRating", movieStarRating);
        }

        if (movieRunningTime != "") {
            formData.append("movieRunningTime", movieRunningTime);
        }

        if (category != "") {
            formData.append("category", category);
        }

        if (cinema != "") {
            formData.append("cinema", cinema);
        }

        if (movieDetailLink != "") {
            formData.append("movieDetailLink", movieDetailLink);
        }

        if (moviePopularCount != "") {
            formData.append("moviePopularCount", moviePopularCount);
        }

        if (changeMovieImage != undefined) {
            formData.append("changeMovieImage", changeMovieImage);
        }

        if (moviePrice != "") {
            formData.append("moviePrice", moviePrice);
        }

        if (changeMovieBannerImage != undefined) {
            formData.append("changeMovieBannerImage", changeMovieBannerImage);
        }

        if (changeMovieRecruitmentImage != undefined) {
            formData.append("changeMovieRecruitmentImage", changeMovieRecruitmentImage);
        }

        if (movieId != "") {
            formData.append("movieId", ${param.movieId});
        }

        if (movieUseYn != undefined) {
            formData.append("movieUseYn", movieUseYn);
        }

        $.ajax({
            url        : "<%=CURRENT_SERVER%>/admin/movie/changeMovie",
            enctype    : 'multipart/form-data',
            data       : formData,
            processData: false,
            contentType: false,
            method     : "POST",
        }).done(function (data) {
            if (data.result) {
                Swal.fire({
                    title: "완료",
                    text : "저장이 완료되었습니다."
                }).then(() => {
                    goToList();
                })
            } else {
                Swal.fire({
                    title: "오류",
                    text : data.message
                }).then(() => {
                    return false;
                })
            }
        })
    }
</script>
</body>
</html>