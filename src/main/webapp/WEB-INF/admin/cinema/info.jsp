<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2023/01/16
  Time: 11:54 AM
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
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>극장 상세</h4>
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
                                    <th class="border-end" scope="row" style="width: 20%;">극장 브랜드</th>
                                    <td style="width: 80%;">
                                        <div class="form-group clearfix" style="margin-bottom: 0; margin-top: 6px;">
                                            <c:forEach var="brand" items="${brandList}" varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${cinema.content.brandName == brand.brandName}">
                                                            <input type="radio" value="${brand.brandName}"
                                                                   name="brandName"
                                                                   id="${brand.brandName}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="radio" value="${brand.brandName}"
                                                                   name="brandName"
                                                                   id="${brand.brandName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label for="${brand.brandName}">
                                                            ${brand.brandName}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">지역명</th>
                                    <td style="width: 80%;">
                                        <div class="form-group" style="display:flex; width:180px;">
                                            <select class="form-control select2bs4" aria-label="Default select example"
                                                    id="regionName">
                                                <c:forEach var="region" items="${regionList}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${cinema.content.regionName.equals(region)}">
                                                            <option value="${region}" selected>${region}</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${region}">${region}</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row" style="width: 20%;">극장명</th>
                                    <td style="width: 80%;">
                                        <input type="text" value="${cinema.content.cinemaName}"
                                               style="width:500px"
                                               placeholder="극장명을 입력해주세요."
                                               class="form-control" id="cinemaName">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="border-end" scope="row">
                                        <p>영화명</p>
                                        <div class="form-group clearfix">
                                            <div class="icheck-primary d-inline">
                                                <input class="custom-control-input" type="checkbox"
                                                       name="allCheck"
                                                       value="allCheck"
                                                       id="allCheck"
                                                       onclick="allClickMovie()"
                                                >
                                                <label style="font-size: 12px;" for="allCheck">
                                                    전체선택/해제
                                                </label>
                                            </div>
                                        </div>
                                    </th>
                                    <td>
                                        <div class="form-group clearfix"
                                             style="width: 100%; display:grid; grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr; grid-auto-rows:40px; font-size:15px; gap: 5px">
                                            <c:forEach var="movie" items="${cinema.content.movieList}"
                                                       varStatus="status">
                                                <div class="icheck-primary d-inline">
                                                    <c:choose>
                                                        <c:when test="${movie.movieCinemaYn}">
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="movie"
                                                                   value="${movie.movieId}"
                                                                   id="${movie.movieName}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="custom-control-input" type="checkbox"
                                                                   name="movie"
                                                                   value="${movie.movieId}"
                                                                   id="${movie.movieName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <label style="font-size: 12px;" for="${movie.movieName}">
                                                            ${movie.movieName}
                                                    </label>
                                                </div>
                                            </c:forEach>
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
                            <button type="button" class="btn btn-success mr-3" onclick="saveInfo()">&nbsp;저장&nbsp;
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

    function saveInfo() {
        let movieList = [];
        let brandName = $('input:radio[name=brandName]:checked').val();
        let cinemaName = $("#cinemaName").val();
        let regionName = $("#regionName").val();
        $("input:checkbox[name='movie']").each(function (index, value) {
            if (value.checked) {
                movieList.push(value.value)
            }
        })
        console.log(brandName);

        if (brandName === undefined) {
            alertMessage("오류", "극장 브랜드를 선택해 주세요")
            return false;
        }

        if (cinemaName === "") {
            alertMessage("오류", "극장명 입력해 주세요")
            return false;
        }

        if (regionName === "") {
            alertMessage("오류", "지역명 입력해 주세요")
            return false;
        }

        $.ajax({
            url     : "<%=CURRENT_SERVER%>/admin/cinema/saveInfo",
            data    : {
                cinemaId  : "${param.cinemaId}",
                brandName : brandName,
                regionName: regionName,
                cinemaName: cinemaName,
                movieList : JSON.stringify(movieList)
            },
            method  : "POST",
            dataType: "json"
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
        location.href = "<%=CURRENT_SERVER%>/admin/cinema/list";
    }

    function allClickMovie() {
        let checkTf = $("#allCheck").is(":checked");
        if (checkTf) {
            $("input:checkbox[name='movie']").prop("checked", true);
        } else {
            $("input:checkbox[name='movie']").prop("checked", false);
        }
    }

    function alertMessage(title, message) {
        Swal.fire({
            title            : title,
            text             : message,
            showCancelButton : true,
            confirmButtonText: '확인',
            cancelButtonText : '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                return false;
            } else {
                return false;
            }
        })
    }
</script>
</body>
</html>
