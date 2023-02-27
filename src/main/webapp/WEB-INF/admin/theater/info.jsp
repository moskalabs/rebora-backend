<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/05
  Time: 05:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:parseDate value="${ theater.theaterStartTime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterStartTime" type="both"/>
<fmt:parseDate value="${ theater.theaterEndTime }" pattern="yyyy-MM-dd'T'HH:mm"
               var="parseTheaterEndTime" type="both"/>
<html>
<head>
    <%@include file="../include/header.jsp" %>
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

        .border-end {
            border-right: 1px solid #dee2e6;
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
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>상영관 상세</h4>
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
                                    <th class="border-end" scope="row" style="width: 25%;">상영관 브랜드</th>
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <c:forEach var="brand" items="${brandList}" varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${theater.theaterCinemaBrandName.equals(brand.brandName)}">
                                                            <input class="form-check-input" type="radio"
                                                                   name="theaterCinemaBrandName"
                                                                   value="${brand.brandName}" id="${brand.brandName}"
                                                                   checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="form-check-input" type="radio"
                                                                   name="theaterCinemaBrandName"
                                                                   value="${brand.brandName}" id="${brand.brandName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label class="form-check-label" for="${brand.brandName}">
                                                            ${brand.brandName}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영관 지역</th>
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0;">
                                            <c:forEach var="region" items="${regionList}" varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${theater.theaterRegion.equals(region)}">
                                                            <input class="form-check-input" type="radio"
                                                                   name="theaterRegion"
                                                                   value="${region}" id="${region}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="form-check-input" type="radio"
                                                                   name="theaterRegion"
                                                                   value="${region}" id="${region}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label class="form-check-label" for="${region}">
                                                            ${region}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영 극장</th>
                                    <td>
                                        <input type="text" value="${theater.theaterCinemaName}"
                                               style="width:250px"
                                               class="form-control" id="theaterCinemaName"
                                               aria-describedby="emailHelp">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영관 이름</th>
                                    <td>
                                        <input type="text" value="${theater.theaterName}"
                                               style="width:250px"
                                               class="form-control" id="theaterName"
                                               aria-describedby="emailHelp">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">영화 상영날짜</th>
                                    <td style="width: 80%;">
                                        <input type="date" id="theaterDate" name="trip-start"
                                               value="<fmt:formatDate pattern='yyyy-MM-dd' value='${ parseTheaterStartTime }'/>"
                                               min="2022-01-01"
                                               max="2099-12-30">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영관 상영 가능 시간</th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <input type="text"
                                               value="<fmt:formatDate pattern='HH' value='${ parseTheaterStartTime }'/>"
                                               style="width:46px"
                                               class="form-control" id="theaterStartHour"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;:&nbsp;</p>
                                        <input type="text"
                                               value="<fmt:formatDate pattern='mm' value='${ parseTheaterStartTime }'/>"
                                               style="width:46px"
                                               class="form-control" id="theaterStartMinute"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;&nbsp;~&nbsp;&nbsp;</p>
                                        <input type="text"
                                               value="<fmt:formatDate pattern='HH' value='${ parseTheaterEndTime }'/>"
                                               style="width:46px"
                                               class="form-control" id="theaterEndHour"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;:&nbsp;</p>
                                        <input type="text"
                                               value="<fmt:formatDate pattern='mm' value='${ parseTheaterEndTime }'/>"
                                               style="width:46px"
                                               class="form-control" id="theaterEndMinute"
                                               maxlength="2"
                                        >
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">러닝타임(분)</th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <input type="text" value="${theater.theaterTime}"
                                               style="width:100px"
                                               class="form-control" id="theaterTime"
                                               aria-describedby="emailHelp">
                                        <p>&nbsp;(분)</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">현재/최소 모집인원</th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <p>${theater.recruitmentPeople}</p>&nbsp/&nbsp<input type="text"
                                                                                             value="${theater.theaterMinPeople}"
                                                                                             style="width:100px"
                                                                                             class="form-control"
                                                                                             id="theaterMinPeople"
                                                                                             aria-describedby="emailHelp">
                                        <p>&nbsp명</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">최대 모집인원</th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <input type="text" value="${theater.theaterMaxPeople}"
                                               style="width:100px"
                                               class="form-control" id="theaterMaxPeople"
                                               aria-describedby="emailHelp">
                                        <p>&nbsp명</p>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">상영 가격</th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <input type="text" value="${theater.theaterPrice}"
                                               style="width:200px"
                                               class="form-control" id="theaterPrice"
                                               aria-describedby="emailHelp">
                                        <p>&nbsp원</p>
                                    </td>
                                </tr>
                                <c:if test="${theater.recruitmentId != null}">
                                    <tr>
                                        <th class="border-end" scope="row" style="width: 25%;">모집 상태</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${theater.recruitmentStatus == 'CANCEL'}">
                                                    <p>
                                                        취소
                                                    </p>
                                                </c:when>
                                                <c:when test="${theater.recruitmentStatus == 'WAIT'}">
                                                    <p>
                                                        대기
                                                    </p>
                                                </c:when>
                                                <c:when test="${theater.recruitmentStatus == 'RECRUITING'}">
                                                    <p>
                                                        모집중
                                                    </p>
                                                </c:when>
                                                <c:when test="${theater.recruitmentStatus == 'CONFIRMATION'}">
                                                    <p>
                                                        상영 확정
                                                    </p>
                                                </c:when>
                                                <c:otherwise>
                                                    <p>
                                                        상영 완료
                                                    </p>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="border-end" scope="row" style="width: 25%;">모집 바로가기</th>
                                        <td>
                                            <a href="<%=CURRENT_SERVER%>/admin/recruitment/info?recruitmentId=${theater.recruitmentId}">
                                                모집 바로가기
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
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
                            <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                        </div>
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                            <button type="button" class="btn btn-success mr-3" onclick="saveTheater()">&nbsp;저장&nbsp;
                            </button>
                            <button type="button" class="btn btn-danger mr-3" onclick="cancelSave()">&nbsp;취소&nbsp;</button>
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

    function saveTheater() {

        let token = localStorage.getItem("token");
        if (token == null || token === "") {
            Swal.fire({
                title: "오류",
                text : "토큰이 만료되었습니다.",
            }).then(() => {
                location.href = "<%=CURRENT_SERVER%>/admin/login";
            })
        }

        let theaterId = '${theater.theaterId}'
        let theaterCinemaBrandName = $('input:radio[name=theaterCinemaBrandName]:checked').val();
        let theaterRegion = $('input:radio[name=theaterRegion]:checked').val();
        let theaterCinemaName = $("#theaterCinemaName").val();
        let theaterName = $("#theaterName").val();
        let theaterDate = $("#theaterDate").val();
        let theaterStartHour = $("#theaterStartHour").val();
        let theaterStartMinute = $("#theaterStartMinute").val();
        let theaterEndHour = $("#theaterEndHour").val();
        let theaterEndMinute = $("#theaterEndMinute").val();
        let theaterTime = $("#theaterTime").val();
        let theaterPrice = $("#theaterPrice").val();
        let theaterMinPeople = $("#theaterMinPeople").val();
        let theaterMaxPeople = $("#theaterMaxPeople").val();

        if (theaterCinemaName === "") {
            SwalAlertError("상영 극장을 입력해 주세요");
        } else if (theaterName === "") {
            SwalAlertError("상영관 이름을 입력해 주세요");
        } else if (theaterDate === "") {
            SwalAlertError("상영날짜를 입력해 주세요");
        } else if (theaterStartHour === "") {
            SwalAlertError("상영 시작 시각을 입력해 주세요");
        } else if (theaterStartMinute === "") {
            SwalAlertError("상영 시작 시각을 입력해 주세요");
        } else if (theaterEndHour === "") {
            SwalAlertError("상영 종료 시각을 입력해 주세요");
        } else if (theaterTime === "") {
            SwalAlertError("러닝타임을 입력해 주세요");
        } else if (theaterEndMinute === "") {
            SwalAlertError("상영 종료 시각을 입력해 주세요");
        } else if (theaterMinPeople === "") {
            SwalAlertError("상영 최소 인원을 입력해 주세요");
        } else if (theaterMaxPeople === "") {
            SwalAlertError("상영 최대 인원을 입력해 주세요");
        } else if (theaterPrice === "") {
            SwalAlertError("상영 가격을 입력해 주세요");
        } else {
            $.ajax({
                url     : "<%=CURRENT_SERVER%>/admin/theater/theaterSave",
                data    : {
                    theaterId,
                    theaterCinemaBrandName: theaterCinemaBrandName,
                    theaterRegion         : theaterRegion,
                    theaterCinemaName     : theaterCinemaName,
                    theaterName           : theaterName,
                    theaterDate           : theaterDate,
                    theaterStartHour      : theaterStartHour,
                    theaterStartMinute    : theaterStartMinute,
                    theaterEndHour        : theaterEndHour,
                    theaterEndMinute      : theaterEndMinute,
                    theaterTime           : theaterTime,
                    theaterPrice          : theaterPrice,
                    theaterMinPeople      : theaterMinPeople,
                    theaterMaxPeople      : theaterMaxPeople
                },
                method  : "POST",
                dataType: "json",
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
    }

    function SwalAlertError(text) {
        Swal.fire({
            title: "오류",
            text : text,
        }).then((result) => {
            return false;
        })
    }
</script>
</body>
</html>
