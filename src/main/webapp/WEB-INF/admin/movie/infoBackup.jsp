<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Rebora Admin</title>
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

        tr {
            height: 60px;
            font-size: 20px;
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
    <div class="text-center" style="display:grid; grid-template-columns:2fr 10fr; height:100%;">
        <div class="border-end" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="my-5 px-2" style="display:flex; width:100%; align-items:center; justify-content:center;">
                <h3 style="color:#a29bfe">RE : BORA</h3>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/movie/list" style="text-decoration: none; color:black">
                    <h5 style="color:#e55039;">영화 목록 관리</h5>
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
        </div>
        <div class="px-4" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="mt-5 px-2" style="display:flex; width:100%;">
                <h3 style="color:#a29bfe">영화 상세</h3>
            </div>
            <table class="table mt-4 align-middle text-leftl border-top">
                <tbody>
                <tr>
                    <th class="border-end" scope="row" style="width: 20%;">영화 이름</th>
                    <td style="width: 80%;">
                        <input type="text" value="${response.content.movieName}" style="width:250px"
                               class="form-control mb-2" id="movieName"
                               aria-describedby="emailHelp">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 20%;">상영 등급</th>
                    <td style="width: 80%;">
                        <c:choose>
                            <c:when test="${response.content.movieRating == 'ALL'}">
                                <input class="form-check-input" type="radio" value="ALL" name="movieRating"
                                       id="ALL" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="ALL" name="movieRating"
                                       id="ALL">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="ALL">
                            전체 이용가
                        </label>
                        <c:choose>
                            <c:when test="${response.content.movieRating == 'TWELVE'}">
                                <input class="form-check-input ms-2" type="radio" value="TWELVE" name="movieRating"
                                       id="TWELVE"
                                       checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input ms-2" type="radio" value="TWELVE" name="movieRating"
                                       id="TWELVE">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="TWELVE">
                            12세 이용가
                        </label>
                        <c:choose>
                            <c:when test="${response.content.movieRating == 'FIFTEEN'}">
                                <input class="form-check-input ms-2" type="radio" value="FIFTEEN" name="movieRating"
                                       id="FIFTEEN"
                                       checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input ms-2" type="radio" value="FIFTEEN" name="movieRating"
                                       id="FIFTEEN">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="FIFTEEN">
                            15세 이용가
                        </label>
                        <c:choose>
                            <c:when test="${response.content.movieRating == 'ADULT'}">
                                <input class="form-check-input ms-2" type="radio" value="ADULT" name="movieRating"
                                       id="ADULT" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input ms-2" type="radio" value="ADULT" name="movieRating"
                                       id="ADULT">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="ADULT">
                            성인
                        </label>
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
                    <td style="width: 80%;"><input type="text" value="${response.content.movieRunningTime}"
                                                   style="width:250px"
                                                   class="form-control mb-2" id="movieRunningTime"
                                                   aria-describedby="emailHelp"></td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 20%;">평점</th>
                    <td style="width: 80%;"><input type="text" value="${response.content.convertStartRation}"
                                                   style="width:100px"
                                                   class="form-control mb-2" id="movieStarRating"
                                                   aria-describedby="emailHelp"></td>
                </tr>
                <tr>
                    <th class="border-end" scope="row">장르</th>
                    <td style="width: 100%; display:grid; grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr; grid-auto-rows:20px; font-size:15px;">
                        <c:forEach var="category" items="${response.content.categoryList}" varStatus="status">
                            <div>
                                <c:choose>
                                    <c:when test="${category.categoryYn}">
                                        <input class="form-check-input" type="checkbox" name="category"
                                               value="${category.id}"
                                               id="${category.categoryName}" checked>
                                    </c:when>
                                    <c:otherwise>
                                        <input class="form-check-input" type="checkbox" name="category"
                                               value="${category.id}"
                                               id="${category.categoryName}">
                                    </c:otherwise>
                                </c:choose>
                                <label class="form-check-label" for="${category.categoryName}">
                                        ${category.categoryName}
                                </label>
                            </div>
                        </c:forEach>
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
                        <input type="text" value="${response.content.moviePopularCount}" style="width:80%"
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
                        <input type="file" class="form-control" id="changeMovieImage"
                               accept="image/gif,image/jpeg,image/png">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 20%;">영화 배너 이미지</th>
                    <td>
                        <c:if test="${response.content.movieBannerImage != null}">
                            <img src="${response.content.movieBannerImage}"
                                 style="width: 400px;" class="img-thumbnail" alt="...">
                        </c:if>
                        <input type="file" class="form-control" id="changeMovieBannerImage"
                               accept="image/gif,image/jpeg,image/png">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 20%;">영화 모집 이미지</th>
                    <td>
                        <c:if test="${response.content.movieRecruitmentImage != null}">
                            <img src="${response.content.movieRecruitmentImage}"
                                 style="width: 400px;" class="img-thumbnail" alt="...">
                        </c:if>
                        <input type="file" class="form-control" id="changeMovieRecruitmentImage"
                               accept="image/gif,image/jpeg,image/png">
                    </td>
                </tr>
                </tbody>
            </table>
            <div style="display:flex; width: 100%; height:60px">
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-start">
                    <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                </div>
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                    <button type="button" class="btn btn-success me-3" onclick="saveMovie()">&nbsp;수정&nbsp;</button>
                    <button type="button" class="btn btn-danger" onclick="cancelSave()">&nbsp;취소&nbsp;</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
<script>
    $(document).ready(function () {
        let token = localStorage.getItem("token");
        if (token == null || token === "") {
            Swal.fire({
                title: "오류",
                text: "토큰이 만료되었습니다.",
            }).then(() => {
                location.href = "<%=CURRENT_SERVER%>/admin/login";
            })
        }

        $.ajax({
            url: "<%=CURRENT_SERVER%>/admin/adminCheck",
            headers: {
                "token": token
            },
            contentType: 'application/json',
            method: "GET",
            dataType: "json",
            error: function (data) {
                if (!data.responseJSON.result) {
                    Swal.fire({
                        title: "오류",
                        text: data.responseJSON.message,
                    }).then(() => {
                        location.href = "<%=CURRENT_SERVER%>/admin/login";
                    })
                }
            }
        }).done(function (data) {
            if (!data.result) {
                Swal.fire({
                    title: "오류",
                    text: data.message,
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
            title: "취소",
            text: "저장을 취소하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                location.reload();
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
        let category = [];
        $("input:checkbox[name='category']").each(function (index, value) {
            if (value.checked) {
                console.log(value.value)
                category.push(value.value)
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

        if (movieDetailLink != "") {
            formData.append("movieDetailLink", movieDetailLink);
        }

        if (moviePopularCount != "") {
            formData.append("moviePopularCount", moviePopularCount);
        }

        if (changeMovieImage != undefined) {
            formData.append("changeMovieImage", changeMovieImage);
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

        $.ajax({
            url: "<%=CURRENT_SERVER%>/admin/movie/changeMovie",
            enctype: 'multipart/form-data',
            data: formData,
            processData: false,
            contentType: false,
            method: "POST",
        }).done(function (data) {
            if (data.result) {
                Swal.fire({
                    title: "완료",
                    text: "저장이 완료되었습니다."
                }).then(() => {
                    location.reload();
                })
            } else {
                Swal.fire({
                    title: "오류",
                    text: data.message
                }).then(() => {
                    return false;
                })
            }
        })
    }
</script>
</body>
</html>