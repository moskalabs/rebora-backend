<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/06
  Time: 5:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:parseDate value="${ user.content.regDate }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseRegDate" type="both"/>
<fmt:parseDate value="${ user.content.modDate  }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseModDate" type="both"/>
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
            margin: 0;
        }

        a {
            text-decoration: none;
            color: black;
        }
    </style>
</head>
<body>
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
                    <h5 style="color:#e55039;">회원 관리</h5>
                </a>
            </div>
        </div>
        <div class="px-4" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="mt-5 px-2" style="display:flex; width:100%;">
                <h3 style="color:#a29bfe">모집 상세</h3>
            </div>
            <table class="table mt-4 align-middle text-left border-top">
                <tbody>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 이메일</th>
                    <td>
                        <input type="text" value="${user.content.userEmail}"
                               style="width:300px"
                               placeholder="이메일을 입력해 주세요"
                               class="form-control" id="userEmail"
                               aria-describedby="emailHelp">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 이름</th>
                    <td>
                        <input type="text" value="${user.content.userName}"
                               style="width:300px"
                               placeholder="이름을 입력해 주세요"
                               class="form-control" id="userName"
                               aria-describedby="emailHelp">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 닉네임</th>
                    <td>
                        <input type="text" value="${user.content.userNickname}"
                               style="width:300px"
                               placeholder="닉네임을 입력해 주세요"
                               class="form-control" id="userNickname"
                               aria-describedby="emailHelp">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 이미지</th>
                    <td>
                        <c:if test="${user.content.userImage != null}">
                            <img src="${user.content.userImage}"
                                 style="height: 200px;" class="img-thumbnail" alt="...">
                        </c:if>
                        <input type="file" class="form-control" id="userImage"
                               accept="image/gif,image/jpeg,image/png">
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">참여한 모집 수</th>
                    <td>
                        <p>${user.content.participationHistoryCount} 명</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">모집한 모집 수</th>
                    <td>
                        <p>${user.content.recruiterCount} 명</p>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 푸쉬 여부</th>
                    <td style="width: 100%; height: 60px; font-size:16px; align-items: center">
                        <c:choose>
                            <c:when test="${user.content.userPushYn == true}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userPushYn"
                                       id="userPushY" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userPushYn"
                                       id="userPushY">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushY">
                            예
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userPushYn == false}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userPushYn"
                                       id="userPushN" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userPushYn"
                                       id="userPushN">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            아니요
                        </label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 야간 푸쉬 여부</th>
                    <td style="width: 100%; height: 60px; font-size:16px; align-items: center">
                        <c:choose>
                            <c:when test="${user.content.userPushNightYn == true}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userPushNightYn"
                                       id="userPushNightY" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userPushNightYn"
                                       id="userPushNightY">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushY">
                            예
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userPushNightYn == false}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userPushNightYn"
                                       id="userPushNightN" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userPushNightYn"
                                       id="userPushNightN">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            아니요
                        </label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 활성화 여부</th>
                    <td style="width: 100%; height: 60px; font-size:16px; align-items: center">
                        <c:choose>
                            <c:when test="${user.content.userUseYn == true}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userUseYn"
                                       id="userUseY" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="true"
                                       name="userUseYn"
                                       id="userUseY">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushY">
                            예
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userPushNightYn == false}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userUseYn"
                                       id="userUseN" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="false"
                                       name="userUseYn"
                                       id="userUseN">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            아니요
                        </label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 등급</th>
                    <td style="width: 100%; height: 60px; font-size:16px; align-items: center">
                        <c:choose>
                            <c:when test="${user.content.userGrade == 'NORMAL'}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="NORMAL"
                                       name="userGrade"
                                       id="NORMAL" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="NORMAL"
                                       name="userGrade"
                                       id="NORMAL">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushY">
                            일반
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userGrade == 'ADMIN'}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="ADMIN"
                                       name="userGrade"
                                       id="ADMIN" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="ADMIN"
                                       name="userGrade"
                                       id="ADMIN">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            관리자
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userGrade == 'DORMANCY'}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="DORMANCY"
                                       name="userGrade"
                                       id="DORMANCY" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="DORMANCY"
                                       name="userGrade"
                                       id="DORMANCY">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            휴면
                        </label>
                        <c:choose>
                            <c:when test="${user.content.userGrade == 'WITHDRAWAL'}">
                                <input style="margin: 0" class="form-check-input" type="radio" value="WITHDRAWAL"
                                       name="userGrade"
                                       id="WITHDRAWAL" checked>
                            </c:when>
                            <c:otherwise>
                                <input style="margin: 0" class="form-check-input" type="radio" value="WITHDRAWAL"
                                       name="userGrade"
                                       id="WITHDRAWAL">
                            </c:otherwise>
                        </c:choose>
                        <label class="form-check-label" for="userPushN">
                            휴면
                        </label>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">유저 SNS 종류</th>
                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                        <c:choose>
                            <c:when test="${user.content.userSnsKind == 'NAVER'}">
                                <p>일반</p>
                            </c:when>
                            <c:when test="${user.content.userSnsKind == 'KAKAO'}">
                                <p>관리자</p>
                            </c:when>
                            <c:when test="${user.content.userSnsKind == 'APPLE'}">
                                <p>휴면</p>
                            </c:when>
                            <c:otherwise>
                                <p>일반</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th class="border-end" scope="row" style="width: 25%;">등록일</th>
                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                        <p><fmt:formatDate pattern="yyyy년MM월dd일 HH:mm" value="${ parseRegDate }"/></p>
                    </td>
                </tr>
                </tbody>
            </table>
            <div style="display:flex; width: 100%; height:60px">
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-start">
                    <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                </div>
                <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                    <button type="button" class="btn btn-success me-3" onclick="saveUser()">&nbsp;수정&nbsp;
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

    function  goToList() {
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

    function saveUser() {
        let userId = "${param.userId}";
        let userEmail = $("#userEmail").val();
        let userName = $("#userName").val();
        let userNickname = $("#userNickname").val();
        let userImage = $('input#userImage')[0].files[0];
        let userPushYn = $('input:radio[name=userPushYn]:checked').val();
        let userPushNightYn = $('input:radio[name=userPushNightYn]:checked').val();
        let userUseYn = $('input:radio[name=userUseYn]:checked').val();
        let userGrade = $('input:radio[name=userGrade]:checked').val();

        let formData = new FormData();

        if (userEmail != "") {
            formData.append("userEmail", userEmail);
        } else {
            SwalAlertError("아이디를 입력해 주세요")
            return false;
        }

        if (userName != "") {
            formData.append("userName", userName);
        } else {
            SwalAlertError("이름을 입력해 주세요")
            return false;
        }

        if (userNickname != "") {
            formData.append("userNickname", userNickname);
        } else {
            SwalAlertError("닉네임을 입력해 주세요")
            return false;
        }

        if (userImage != undefined) {
            formData.append("userImage", userImage);
        }

        if (userPushYn != undefined) {
            formData.append("userPushYn", userPushYn);
        }

        if (userPushNightYn != undefined) {
            formData.append("userPushNightYn", userPushNightYn);
        }
        if (userUseYn != undefined) {
            formData.append("userUseYn", userUseYn);
        }
        if (userGrade != undefined) {
            formData.append("userGrade", userGrade);
        }

        if (userId != "") {
            formData.append("userId", ${param.userId});
        }

        $.ajax({
            url: "<%=CURRENT_SERVER%>/admin/user/saveUser",
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

    function SwalAlertError(text) {
        Swal.fire({
            title: "오류",
            text: text,
        }).then((result) => {
            return false;
        })
    }
</script>
</body>
</html>