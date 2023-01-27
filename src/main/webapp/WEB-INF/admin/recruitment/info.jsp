<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/02
  Time: 11:03 AM
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
<fmt:parseDate value="${ recruitment.content.theaterStartDatetime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterStartDatetime" type="both"/>
<fmt:parseDate value="${ recruitment.content.theaterEndDatetime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterEndDatetime" type="both"/>
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>모집 상세</h4>
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
                                    <th class="border-end" scope="row" style="width: 25%;">영화 이름</th>
                                    <td style="width: 80%;">
                                        <p>${recruitment.content.movieName}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">영화 상영날짜</th>
                                    <td style="width: 80%;">
                                        <p><fmt:formatDate pattern="yyyy년MM월dd일"
                                                           value="${ parseTheaterStartDatetime }"/></p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영관 상영 가능 시간</th>
                                    <td style="width: 80%;">
                                        <p><fmt:formatDate pattern="HH:mm" value="${ parseTheaterStartDatetime }"/> ~
                                            <fmt:formatDate
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
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentStatus == 'CANCEL'}">
                                                        <input type="radio" value="CANCEL"
                                                               name="recruitmentStatus"
                                                               id="CANCEL" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="CANCEL"
                                                               name="recruitmentStatus"
                                                               id="CANCEL">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="CANCEL">
                                                    모집 취소
                                                </label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentStatus == 'WAIT'}">
                                                        <input type="radio" value="WAIT"
                                                               name="recruitmentStatus"
                                                               id="WAIT" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="WAIT"
                                                               name="recruitmentStatus"
                                                               id="WAIT">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="WAIT">모집 대기</label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentStatus == 'RECRUITING'}">
                                                        <input type="radio" value="RECRUITING"
                                                               name="recruitmentStatus"
                                                               id="RECRUITING" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="RECRUITING"
                                                               name="recruitmentStatus"
                                                               id="RECRUITING">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="RECRUITING">모집중</label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentStatus == 'CONFIRMATION'}">
                                                        <input type="radio"
                                                               value="CONFIRMATION"
                                                               name="recruitmentStatus"
                                                               id="CONFIRMATION" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio"
                                                               value="CONFIRMATION"
                                                               name="recruitmentStatus"
                                                               id="CONFIRMATION">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="CONFIRMATION">상영 확정</label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentStatus == 'COMPLETED'}">
                                                        <input type="radio" value="COMPLETED"
                                                               name="recruitmentStatus"
                                                               id="COMPLETED" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="COMPLETED"
                                                               name="recruitmentStatus"
                                                               id="COMPLETED">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="COMPLETED">상영 완료</label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">노출 여부</th>
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentExposeYn == true}">
                                                        <input type="radio" value="true"
                                                               name="recruitmentExposeYn"
                                                               id="recruitmentExposeYnTrue" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="true"
                                                               name="recruitmentExposeYn"
                                                               id="recruitmentExposeYnTrue">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="recruitmentExposeYnTrue">노출</label>
                                            </div>
                                            <div class="icheck-primary d-inline">
                                                <c:choose>
                                                    <c:when test="${recruitment.content.recruitmentExposeYn == false}">
                                                        <input type="radio" value="false"
                                                               name="recruitmentExposeYn"
                                                               id="recruitmentExposeYnFalse" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" value="false"
                                                               name="recruitmentExposeYn"
                                                               id="recruitmentExposeYnFalse">
                                                    </c:otherwise>
                                                </c:choose>
                                                <label for="recruitmentExposeYnFalse">비노출</label>
                                            </div>
                                        </div>
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
                            <button type="button" class="btn btn-success mr-3" onclick="saveRecruitment()">
                                &nbsp;저장&nbsp;
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
        location.href = "<%=CURRENT_SERVER%>/admin/recruitment/list";
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
