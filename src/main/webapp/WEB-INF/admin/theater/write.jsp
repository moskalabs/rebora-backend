<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2023/02/10
  Time: 10:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                        <h4>상영관 생성</h4>
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
                                    <th class="border-end" scope="row" style="width: 25%;">지역</th>
                                    <td style="width: 80%;">
                                        <div class="form-group" style="display:flex; width:180px;">
                                            <select class="form-control select2bs4" aria-label="Default select example"
                                                    onchange="onChangeCinema()" id="region">
                                                <c:forEach var="region" items="${regionList}" varStatus="status">
                                                    <option value="${region}">${region}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 브랜드
                                    </th>
                                    <td style="width: 80%;">
                                        <div class="form-group" style="display:flex; width:180px;">
                                            <select class="form-control select2bs4" aria-label="Default select example"
                                                    onchange="onChangeCinema()" id="brand">
                                                <c:forEach var="brand" items="${brandList}" varStatus="status">
                                                    <option value="${brand.brandName}">${brand.brandName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 극장
                                    </th>
                                    <td style="width: 80%;">
                                        <div class="form-group" style="display:flex; width:180px;" id="cinemaHtml">
                                            <select class="form-control select2bs4" aria-label="Default select example"
                                                    id="cinema">
                                                <option value="none">극장이 없습니다.</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관
                                    </th>
                                    <td style="width: 80%;">
                                        <input type="text" class="form-control"
                                               style="width:50%" id="theaterName"
                                               placeholder="상영관을 입력해 주세요">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영 날짜
                                    </th>
                                    <td>
                                        <input type="date" id="theaterDate" name="trip-start"
                                               min="2022-01-01"
                                               max="2099-12-30">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영 시작/종료 시각
                                    </th>
                                    <td style="width: 80%; height: 60px; display: flex; flex-direction: row; align-items: center">
                                        <input type="text"
                                               style="width:46px"
                                               class="form-control" id="theaterStartHour"
                                               placeholder="01"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;:&nbsp;</p>
                                        <input type="text"
                                               style="width:46px"
                                               class="form-control" id="theaterStartMinute"
                                               placeholder="01"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;&nbsp;~&nbsp;&nbsp;</p>
                                        <input type="text"
                                               style="width:46px"
                                               class="form-control" id="theaterEndHour"
                                               placeholder="01"
                                               maxlength="2"
                                        >
                                        <p class="mb-1">&nbsp;:&nbsp;</p>
                                        <input type="text"
                                               style="width:46px"
                                               class="form-control" id="theaterEndMinute"
                                               placeholder="01"
                                               maxlength="2"
                                        >
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 최소 인원
                                    </th>
                                    <td style="width: 80%;">
                                        <input type="text" class="form-control"
                                               style="width:50%" id="theaterMinPeople"
                                               placeholder="상영관 최소 인원을 입력해 주세요">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 최대 인원
                                    </th>
                                    <td style="width: 80%;">
                                        <input type="text" class="form-control"
                                               style="width:50%" id="theaterMaxPeople"
                                               placeholder="상영관 최대 인원을 입력해 주세요">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 시간
                                    </th>
                                    <td style="width: 80%;">
                                        <input type="text" class="form-control"
                                               style="width:50%" id="theaterTime"
                                               placeholder="시간을 입력해 주세요">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 25%;">
                                        상영관 가격
                                    </th>
                                    <td style="width: 80%;">
                                        <input type="text" class="form-control"
                                               style="width:50%" id="theaterPrice"
                                               placeholder="가격을 입력해 주세요">
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
                            <button type="button" class="btn btn-secondary" onclick="goToList()">&nbsp;목록&nbsp;</button>
                        </div>
                        <div style="display:flex; width: 100%; height:40px; justify-content:flex-end">
                            <button type="button" class="btn btn-success mr-3" onclick="saveTheater()">&nbsp;저장&nbsp;
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

        let region = $("#region").val();
        let brand = $("#brand").val();
        getCinema(region, brand);
    })

    function saveTheater() {
        let region = $("#region").val();
        let brand = $("#brand").val();
        let cinema = $("#cinema").val();
        let theaterName = $("#theaterName").val();
        let theaterDate = $("#theaterDate").val();
        let theaterStartHour = $("#theaterStartHour").val();
        let theaterStartMinute = $("#theaterStartMinute").val();
        let theaterEndHour = $("#theaterEndHour").val();
        let theaterEndMinute = $("#theaterEndMinute").val();
        let theaterMinPeople = $("#theaterMinPeople").val();
        let theaterMaxPeople = $("#theaterMaxPeople").val();
        let theaterTime = $("#theaterTime").val();
        let theaterPrice = $("#theaterPrice").val();
        let today = new Date();

        if (cinema === "none") {
            SwalAlertError("극장을 선택해 주세요.");
            return false;
        }

        if (theaterName === "") {
            SwalAlertError("상영관을 입력해 주세요.");
            return false;
        }

        if (theaterDate === undefined || theaterDate === "") {
            SwalAlertError("상영 날짜를 선택해 주세요.");
            return false;
        }

        if (theaterStartHour === "" || theaterStartMinute === "" || theaterEndHour === "" || theaterEndMinute === "") {
            SwalAlertError("상영 시작 종료 시각을 선택해 주세요.");
            return false;
        }

        if (theaterMinPeople === "") {
            SwalAlertError("최소 인원을 입력 해 주세요.");
            return false;
        }

        if (theaterMaxPeople === "") {
            SwalAlertError("최대 인원을 입력 해 주세요.");
            return false;
        }

        if (theaterTime === "") {
            SwalAlertError("상영 시간을 입력 해 주세요.");
            return false;
        }

        if (theaterPrice === "") {
            SwalAlertError("상영 가격을 입력 해 주세요.");
            return false;
        }

        if (today > new Date(theaterDate)) {
            SwalAlertError("오늘 날짜보다 이전의 데이터를 입력 할 수 없습니다.");
            return false;
        }

        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/theater/createTheater",
            data    : {
                regionName        : region,
                brandName         : brand,
                cinemaId          : cinema,
                theaterName       : theaterName,
                theaterDate       : theaterDate,
                theaterStartHour  : theaterStartHour,
                theaterStartMinute: theaterStartMinute,
                theaterEndHour    : theaterEndHour,
                theaterEndMinute  : theaterEndMinute,
                theaterMinPeople  : theaterMinPeople,
                theaterMaxPeople  : theaterMaxPeople,
                theaterTime       : theaterTime,
                theaterPrice      : theaterPrice,
            },
            method  : "POST",
            dataType: "json",
            error      : function (data) {
                if (!data.responseJSON.result) {
                    Swal.fire({
                        title: "오류",
                        text : data.responseJSON.message,
                    }).then(() => {
                        return false;
                    })
                }
            }
        }).done(function (data) {
            if (data.result) {
                Swal.fire({
                    title: "완료",
                    text : "저장이 완료 되었습니다.",
                }).then((result) => {
                    goToList();
                })
            } else {
                SwalAlertError(data.message);
                location.reload();
            }
        })

    }

    function onChangeCinema() {

        let region = $("#region").val();
        let brand = $("#brand").val();

        getCinema(region, brand);
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

    function goToList() {
        location.href = "<%=CURRENT_SERVER%>/admin/theater/list"
    }

    function getCinema(region, brand) {
        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/theater/getCinema",
            data    : {
                region: region,
                brand : brand
            },
            method  : "GET",
            dataType: "json"
        }).done(function (data) {
            let cinemaHtml = $("#cinemaHtml");
            cinemaHtml.empty();
            if (data.cinemaList.length == 0) {
                cinemaHtml.append('<select class="form-control select2bs4" aria-label="Default select example" id="cinema"><option value="none">극장이 없습니다.</option></select>');
            } else {
                let content = "";
                data.cinemaList.forEach(cinema => {
                    content += '<option value="' + cinema.id + '">' + cinema.cinemaName + '</option>';
                })
                cinemaHtml.append('<select class="form-control select2bs4" aria-label="Default select example" id="cinema">' + content + '</select>');
            }
        })
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
