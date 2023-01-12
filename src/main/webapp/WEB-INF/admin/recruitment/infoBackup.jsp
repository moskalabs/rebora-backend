<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/02
  Time: 11:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        p {
            margin: 0px;
        }
    </style>
</head>
<body>
<fmt:parseDate value="${ recruitment.content.theaterStartDatetime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterStartDatetime" type="both"/>
<fmt:parseDate value="${ recruitment.content.theaterEndDatetime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterEndDatetime" type="both"/>
<div class="container-xxl" style="min-width:1140px">
    <div class="text-center" style="display:grid; grid-template-columns:2fr 10fr; height:100%;">
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
                    <h5 style="color:#e55039;">모집 관리</h5>
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
                <h3 style="color:#a29bfe">모집 상세</h3>
            </div>
            <table class="table mt-4 align-middle text-leftl border-top">
                <tbody>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">영화 이름</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.movieName}</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">영화 상영날짜</th>
                    <td style="width: 80%;">
                        <p><fmt:formatDate pattern="yyyy년MM월dd일" value="${ parseTheaterStartDatetime }"/></p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">상영관 상영 가능 시간</th>
                    <td style="width: 80%;">
                        <p><fmt:formatDate pattern="HH:mm" value="${ parseTheaterStartDatetime }"/> ~ <fmt:formatDate
                                pattern="HH:mm" value="${ parseTheaterEndDatetime }"/></p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">러닝타임(분)</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.movieRunningTime}분</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">현재/최소 모집인원</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.recruitmentPeople}/${recruitment.content.theaterMinPeople}명</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">최대 모집인원</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.theaterMaxPeople}명</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">상영 정보</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.theaterRegion} ${recruitment.content.theaterCinemaName} ${recruitment.content.theaterName} ${recruitment.content.theaterCinemaBrandName}</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">모집 상태</th>
                    <td>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentStatus == 'CANCEL'}">
                                <input class="form-check-input" type="radio" value="CANCEL" name="recruitmentStatus"
                                       id="CANCEL" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="CANCEL" name="recruitmentStatus"
                                       id="CANCEL">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="CANCEL">모집 취소</label>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentStatus == 'WAIT'}">
                                <input class="form-check-input" type="radio" value="WAIT" name="recruitmentStatus"
                                       id="WAIT" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="WAIT" name="recruitmentStatus"
                                       id="WAIT">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="WAIT">모집 대기</label>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentStatus == 'RECRUITING'}">
                                <input class="form-check-input" type="radio" value="RECRUITING" name="recruitmentStatus"
                                       id="RECRUITING" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="RECRUITING" name="recruitmentStatus"
                                       id="RECRUITING">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="RECRUITING">모집중</label>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentStatus == 'CONFIRMATION'}">
                                <input class="form-check-input" type="radio" value="CONFIRMATION"
                                       name="recruitmentStatus"
                                       id="CONFIRMATION" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="CONFIRMATION"
                                       name="recruitmentStatus"
                                       id="CONFIRMATION">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="CONFIRMATION">상영 확정</label>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentStatus == 'COMPLETED'}">
                                <input class="form-check-input" type="radio" value="COMPLETED" name="recruitmentStatus"
                                       id="COMPLETED" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="COMPLETED" name="recruitmentStatus"
                                       id="COMPLETED">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="COMPLETED">상영 완료</label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">노출 여부</th>
                    <td style="width: 80%;">
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentExposeYn == true}">
                                <input class="form-check-input" type="radio" value="true" name="recruitmentExposeYn"
                                       id="recruitmentExposeYnTrue" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="true" name="recruitmentExposeYn"
                                       id="recruitmentExposeYnTrue">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="COMPLETED">노출</label>
                        <c:choose>
                            <c:when test="${recruitment.content.recruitmentExposeYn == false}">
                                <input class="form-check-input" type="radio" value="false" name="recruitmentExposeYn"
                                       id="recruitmentExposeYnFalse" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input" type="radio" value="false" name="recruitmentExposeYn"
                                       id="recruitmentExposeYnFalse">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="COMPLETED">비노출</label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">모집장</th>
                    <td style="width: 80%;">
                        <p>${recruitment.content.recruiterNickname}</p>
                    </td>
                </tr>
                </tbody>
            </table>
            <div style="display:flex; width: 100%; height:60px">
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-start">
                    <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                </div>
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                    <button type="button" class="btn btn-success me-3" onclick="saveRecruitment()">&nbsp;수정&nbsp;
                    </button>
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
        location.href = "<%=CURRENT_SERVER%>/admin/movie/list";
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
                location.reload();
            } else {
                return false;
            }
        })
    }

    function saveRecruitment() {
        let recruitmentStatus = $('input:radio[name=recruitmentStatus]:checked').val();
        let recruitmentExposeYn = $('input:radio[name=recruitmentExposeYn]:checked').val();
        let recruitmentId = ${recruitment.content.recruitmentId};
        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/recruitment/changeRecruitment/" + recruitmentId,
            data    : {
                recruitmentStatus  : recruitmentStatus,
                recruitmentExposeYn: recruitmentExposeYn
            },
            method  : "POST",
            dataType: "json",
        }).done(function (data) {
            if (data.result) {
                Swal.fire({
                    title: "완료",
                    text : "저장이 완료되었습니다."
                }).then(() => {
                    location.reload();
                })
            } else {
                Swal.fire({
                    title: "오류",
                    text : data.message,
                }).then(() => {
                    return false;
                })
            }
        })
    }
</script>
</body>
</html>
