<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/02
  Time: 4:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="today" value="<%=new java.util.Date()%>"/>
<c:set var="date"><fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/></c:set>
<fmt:parseDate value="${ param.selectDate }" pattern="yyyy-MM-dd"
               var="parseSelectDate" type="both"/>
<c:set var="selectDate"><fmt:formatDate value="${parseSelectDate}" pattern="yyyy-MM-dd"/></c:set>
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

        tr, td {
            font-size: 14px;
        }

        #csvUpload {
            visibility: hidden;
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
    <input id="csvUpload" type="file" onchange="onchangeFileUpload()" accept="text/csv"/>
    <div class="text-center" style="display:grid; grid-template-columns:200px 1fr; height:100%;">
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
                    <h5 style="color:#e55039;">상영관 관리</h5>
                </a>
            </div>
            <div class="mt-3 px-4" style="display:flex; width:100%; align-items:center;">
                <a href="<%=CURRENT_SERVER%>/admin/user/list" style="text-decoration: none; color:black">
                    <h5>회원 관리</h5>
                </a>
            </div>
        </div>
        <div class="px-4" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <div class="mt-5 px-4" style="display:flex; width:100%; align-items:center;">
                <p class="fs-5"><b>상영관 목록 관리</b>(총 <span style="color:#74b9ff">${theaterList.page.totalElements}</span>개)
                </p>
            </div>
            <div class="px-4 border rounded"
                 style="display:flex; width:100%; height:80px; align-items:center; justify-content:center; background-color:#dfe6e9">
                <c:forEach var="region" items="${theaterList.adminRegionList}" varStatus="status">
                    <c:choose>
                        <c:when test="${status.index == 0}">
                            <c:choose>
                                <c:when test="${param.theaterRegion == region.region || param.theaterRegion == null}">
                                    <a class="ms-5" style="text-decoration: none; color:black">
                                        <h6 style="color:#e55039;">${region.region}(${region.regionCount})</h6>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a class="ms-5" href="javascript:onclickRegion('${region.region}')"
                                       style="text-decoration: none; color:black">
                                        <h6>${region.region}(${region.regionCount})</h6>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${param.theaterRegion == region.region}">
                                    <a class="ms-5" style="text-decoration: none; color:black">
                                        <h6 style="color:#e55039;">${region.region}(${region.regionCount})</h6>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a class="ms-5" href="javascript:onclickRegion('${region.region}')"
                                       style="text-decoration: none; color:black">
                                        <h6>${region.region}(${region.regionCount})</h6>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
            <div class="mt-3" style="display:flex">
                <div style="display:flex; width:33%;">
                    <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                        <c:choose>
                            <c:when test="${param.theaterCinemaBrandName == 'CGV' || param.theaterCinemaBrandName == '' || param.theaterCinemaBrandName == null}">
                                <input type="radio" class="btn-check" name="theaterCinemaBrandName" value="CGV" id="CGV"
                                       autocomplete="off" checked>
                                <label class="btn btn-outline-primary" for="CGV">CGV</label>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" class="btn-check" onclick="onClickBrand(this.value)"
                                       name="theaterCinemaBrandName" value="CGV" id="CGV"
                                       autocomplete="off">
                                <label class="btn btn-outline-primary" for="CGV">CGV</label>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${param.theaterCinemaBrandName == '롯데시네마'}">
                                <input type="radio" class="btn-check" name="theaterCinemaBrandName" value="롯데시네마"
                                       id="롯데시네마"
                                       autocomplete="off" checked>
                                <label class="btn btn-outline-primary" for="롯데시네마">롯데시네마</label>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" class="btn-check" onclick="onClickBrand(this.value)"
                                       name="theaterCinemaBrandName" value="롯데시네마" id="롯데시네마"
                                       autocomplete="off">
                                <label class="btn btn-outline-primary" for="롯데시네마">롯데시네마</label>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${param.theaterCinemaBrandName == '메가박스'}">
                                <input type="radio" class="btn-check" name="theaterCinemaBrandName" value="메가박스"
                                       id="메가박스"
                                       autocomplete="off" checked>
                                <label class="btn btn-outline-primary" for="메가박스">메가박스</label>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" class="btn-check" onclick="onClickBrand(this.value)"
                                       name="theaterCinemaBrandName" value="메가박스" id="메가박스"
                                       autocomplete="off">
                                <label class="btn btn-outline-primary" for="메가박스">메가박스</label>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <div style="display:flex; width:33%; justify-content:center;">
                    <input type="date" id="start" name="trip-start" onchange="onclickSelectDate(this.value)"
                           value="${selectDate != '' ? selectDate : date}" min="2022-01-01"
                           max="2099-12-30">
                </div>
                <div style="display:flex; width:33%; justify-content:flex-end;">
                    <div class="btn-group" role="group" aria-label="Basic outlined example">
                        <button type="button" onclick="onclickUpload()" class="btn btn-outline-success">엑셀파일 업로드
                        </button>
                        <button type="button" onclick="downloadCsvFile()" class="btn btn-outline-success">엑셀파일 다운로드
                        </button>
                    </div>
                </div>
            </div>
            <table class="table table-hover mt-4 border-top align-middle text-center">
                <thead class="table-light">
                <tr>
                    <th scope="col" style="width:5%">번호</th>
                    <th scope="col" style="width:15%">상영관</th>
                    <th scope="col" style="width:7%">상영 시간</th>
                    <th scope="col" style="width:15%">상영 시작/종료 시각</th>
                    <th scope="col" style="width:8%">상영 가격</th>
                    <th scope="col" style="width:8%">모집 상태</th>
                    <th scope="col" style="width:8%">최소 모집인원</th>
                    <th scope="col" style="width:8%">최대 모집인원</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="theater" items="${theaterList.page.content}" varStatus="status">
                    <fmt:parseDate value="${ theater.theaterStartTime }" pattern="yyyy-MM-dd'T'HH:mm"
                                   var="parseTheaterStartTime" type="both"/>
                    <fmt:parseDate value="${ theater.theaterEndTime }" pattern="yyyy-MM-dd'T'HH:mm"
                                   var="parseTheaterEndTime" type="both"/>
                    <tr style="cursor:pointer" onclick="goToDetail(${theater.theaterId})">
                        <th scope="row">${theaterList.page.pageable.offset + status.index + 1}</th>
                        <td>${theater.theaterRegion} ${theater.theaterCinemaName} ${theater.theaterName}</td>
                        <td>${theater.theaterTime}분</td>
                        <td><fmt:formatDate pattern="HH:mm" value="${ parseTheaterStartTime }"/> ~ <fmt:formatDate
                                pattern="HH:mm" value="${ parseTheaterEndTime }"/></td>
                        <td>${theater.theaterPrice}원</td>
                        <c:choose>
                            <c:when test="${theater.recruitmentStatus == 'CANCEL'}">
                                <td>취소</td>
                            </c:when>
                            <c:when test="${theater.recruitmentStatus == 'WAIT'}">
                                <td>대기</td>
                            </c:when>
                            <c:when test="${theater.recruitmentStatus == 'RECRUITING'}">
                                <td>모집중</td>
                            </c:when>
                            <c:when test="${theater.recruitmentStatus == 'CONFIRMATION'}">
                                <td>상영 확정</td>
                            </c:when>
                            <c:when test="${theater.recruitmentStatus == 'COMPLETED'}">
                                <td>상영 완료</td>
                            </c:when>
                            <c:otherwise>
                                <td>아직 미정</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${theater.theaterMinPeople}</td>
                        <td>${theater.theaterMaxPeople}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <c:if test="${theaterList.page.first == false && theaterList.page.pageable.pageNumber > 10}">
                        <li class="page-item"><a class="page-link" href="#">이전</a></li>
                    </c:if>
                    <c:forEach var="pageNum" begin="${(theaterList.page.pageable.pageNumber+10)/10}"
                               end="${((theaterList.page.pageable.pageNumber+10)/10)+9}">
                        <c:if test="${pageNum <= theaterList.page.totalPages}">
                            <c:choose>
                                <c:when test="${pageNum == theaterList.page.pageable.pageNumber+1}">
                                    <li class="page-item active"><a class="page-link"> ${pageNum}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link"
                                           onclick="gotoPagination(${pageNum})">${pageNum}
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                    <c:if test="${theaterList.page.last == false && ((theaterList.page.pageable.pageNumber+10)/10)+9 < theaterList.page.totalPages}">
                        <li class="page-item"><a class="page-link"
                                                 onclick="gotoPagination(`${((theaterList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
</body>
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

    function onclickRegion(region) {
        let cinemaBrandName = '${param.cinemaBrandName}';
        let selectDate = '${param.selectDate}';
        if (cinemaBrandName === '') {
            cinemaBrandName = "CGV";
        }

        location.href = "<%=CURRENT_SERVER%>/admin/theater/list?page=0&size=10&theaterCinemaBrandName=" + cinemaBrandName + "&theaterRegion=" + region + "&selectDate=" + selectDate;
    }

    function onclickSelectDate(selectDate) {
        let cinemaBrandName = '${param.cinemaBrandName}';
        let theaterRegion = '${param.theaterRegion}';

        if (cinemaBrandName === '') {
            cinemaBrandName = "CGV";
        }

        if (theaterRegion === '') {
            theaterRegion = "서울";
        }

        location.href = "<%=CURRENT_SERVER%>/admin/theater/list?page=0&size=10&theaterCinemaBrandName=" + cinemaBrandName + "&theaterRegion=" + theaterRegion + "&selectDate=" + selectDate;
    }

    function onClickBrand(selectBrand) {

        let theaterRegion = '${param.theaterRegion}';
        if (theaterRegion === '') {
            theaterRegion = "서울";
        }
        let selectDate = '${param.selectDate}';
        location.href = "<%=CURRENT_SERVER%>/admin/theater/list?page=0&size=10&theaterCinemaBrandName=" + selectBrand + "&theaterRegion=" + theaterRegion + "&selectDate=" + selectDate;
    }

    function onclickUpload() {
        let csvUpload = document.getElementById("csvUpload");
        csvUpload.click();
    }

    function onchangeFileUpload() {
        let file = $('input#csvUpload')[0].files[0]

        let formData = new FormData();
        if (file !== undefined) {
            formData.append("file", file);
        } else {
            Swal.fire({
                title: "오류",
                text : "파일을 선택해 주세요",
            }).then(() => {
                return false;
            })
        }

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
            url        : "<%=CURRENT_SERVER%>/admin/theater/readCsvFile",
            headers    : {
                "token": token
            },
            enctype    : 'multipart/form-data',
            data       : formData,
            processData: false,
            contentType: false,
            method     : "POST",
        }).done(function (data) {
            if (data.result === true) {
                Swal.fire({
                    title: "완료",
                    text : "CSV 파일 저장이 완료 되었습니다.",
                }).then(() => {
                    location.reload();
                })
            }
        })
    }

    function downloadCsvFile() {

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
            url      : "<%=CURRENT_SERVER%>/admin/theater/downloadCsvFile",
            headers  : {
                "token": token
            },
            cache    : false,
            xhrFields: {
                responseType: "blob",
            },
        }).done(function (data) {
            let blob = new Blob([data], {type: "application/octetstream"});
            let link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = "theaterCsv.csv";
            link.click();
        })
    }

    function gotoPagination(pageNum) {
        let cinemaBrandName = '${param.cinemaBrandName}';
        let theaterRegion = '${param.theaterRegion}';
        let selectDate = '${param.selectDate}';
        location.href = "<%=CURRENT_SERVER%>/admin/theater/list?page=" + (pageNum - 1) + "&size=10&theaterCinemaBrandName=" + cinemaBrandName + "&theaterRegion=" + theaterRegion + "&selectDate=" + selectDate;
    }

    function goToDetail(theaterId) {

        location.href = "<%=CURRENT_SERVER%>/admin/theater/info/" + theaterId;
    }
</script>
</html>
