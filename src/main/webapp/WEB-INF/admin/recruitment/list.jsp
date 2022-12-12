<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/01
  Time: 4:59 PM
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
            <div class="mt-5 px-4" style="display:flex; width:100%; align-items:center;">
                <p class="fs-5"><b>모집 목록 관리</b>(총 <span
                        style="color:#74b9ff">${recruitmentList.page.totalElements}</span>개)</p>
            </div>
            <div class="px-4" style="display:flex; width:100%; align-items:center; justify-content:flex-end;">
                <div class="ms-4" style="display:flex; width:130px;">
                    <select class="form-select" aria-label="Default select example" id="searchCondition">
                        <c:choose>
                            <c:when test="${param.searchCondition == '' || param.searchCondition == 'movieName'}">
                                <option value="movieName" selected>영화 이름</option>
                            </c:when>
                            <c:otherwise>
                                <option value="movieName">영화 이름</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${param.searchCondition == 'region'}">
                                <option value="region" selected>지역</option>
                            </c:when>
                            <c:otherwise>
                                <option value="region">지역</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <input type="text" class="form-control ms-2" style="width:300px" id="searchWord"
                       value="${param.searchWord}"
                       placeholder="검색어를 입력해 주세요">
                <button type="button" class="btn btn-secondary ms-3" onclick="searchRecruitment()">&nbsp;검색&nbsp;
                </button>
                <div class="ms-4" style="display:flex; width:130px;">
                    <select class="form-select" aria-label="Default select example" id="recruitmentSize"
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
            </div>
            <c:choose>
                <c:when test="${recruitmentList.page.totalElements == 0}">
                    <div class="mt-4"
                         style="display: flex;width: 100%; height: 600px; justify-content: center; align-items: center;">
                        <h4>해당 조건의 리스트가 없습니다.</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover mt-4 border-top align-middle text-center">
                        <thead class="table-light">
                        <tr>
                            <th scope="col" style="width:5%">번호</th>
                            <th scope="col" style="width:15%">영화 이름</th>
                            <th scope="col" style="width:15%">상영관</th>
                            <th scope="col" style="width:15%">모집 마감일</th>
                            <th scope="col" style="width:15%">상영일</th>
                            <th scope="col" style="width:10%">모집 상태</th>
                            <th scope="col" style="width:15%">현재/최소 모집인원</th>
                            <th scope="col" style="width:12%">최대 모집인원</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="recruitment" items="${recruitmentList.page.content}" varStatus="status">
                            <fmt:parseDate value="${ recruitment.recruitmentEndDate }" pattern="yyyy-MM-dd'T'HH:mm"
                                           var="parseRecruitmentEndDate" type="both"/>
                            <fmt:parseDate value="${ recruitment.theaterStartDatetime }" pattern="yyyy-MM-dd'T'HH:mm"
                                           var="parseTheaterStartDatetime" type="both"/>
                            <tr onclick="goToDetailInfo(${recruitment.recruitmentId})">
                                <th scope="row">${recruitmentList.page.pageable.offset + status.index + 1}</th>
                                <td>${recruitment.movieName}</td>
                                <td>${recruitment.theaterCinemaName} ${recruitment.theaterName}</td>
                                <td><fmt:formatDate pattern="yyyy년MM월dd일" value="${ parseRecruitmentEndDate }"/></td>
                                <td><fmt:formatDate pattern="yyyy년MM월dd일" value="${ parseTheaterStartDatetime }"/></td>
                                <c:choose>
                                    <c:when test="${recruitment.recruitmentStatus == 'CANCEL'}">
                                        <td>취소</td>
                                    </c:when>
                                    <c:when test="${recruitment.recruitmentStatus == 'WAIT'}">
                                        <td>대기</td>
                                    </c:when>
                                    <c:when test="${recruitment.recruitmentStatus == 'RECRUITING'}">
                                        <td>모집중</td>
                                    </c:when>
                                    <c:when test="${recruitment.recruitmentStatus == 'CONFIRMATION'}">
                                        <td>상영 확정</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>상영 완료</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${recruitment.recruitmentPeople}/${recruitment.theaterMinPeople}</td>
                                <td>${recruitment.theaterMaxPeople}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <nav aria-label="Page navigation example">
                        <ul class="pagination justify-content-center">
                            <c:if test="${recruitmentList.page.first == false && recruitmentList.page.pageable.pageNumber > 10}">
                                <li class="page-item"><a class="page-link" href="#">이전</a></li>
                            </c:if>
                            <c:forEach var="pageNum" begin="${(recruitmentList.page.pageable.pageNumber+10)/10}"
                                       end="${((recruitmentList.page.pageable.pageNumber+10)/10)+9}">
                                <c:if test="${pageNum <= recruitmentList.page.totalPages}">
                                    <c:choose>
                                        <c:when test="${pageNum == recruitmentList.page.pageable.pageNumber+1}">
                                            <li class="page-item active"><a class="page-link"> ${pageNum}</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="page-item"><a class="page-link"
                                                                     onclick="gotoPagination(${pageNum})">${pageNum}</a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                            <c:if test="${recruitmentList.page.last == false && ((recruitmentList.page.pageable.pageNumber+10)/10)+9 < recruitmentList.page.totalPages}">
                                <li class="page-item"><a class="page-link"
                                                         onclick="gotoPagination(`${((recruitmentList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
<script type="text/javascript">
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

    function goToDetailInfo(recruitmentId) {
        location.href = "<%=CURRENT_SERVER%>/admin/recruitment/info?recruitmentId=" + recruitmentId
    }

    function changePageSize() {
        let recruitmentSize = $("#recruitmentSize").val();
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        location.href = "<%=CURRENT_SERVER%>/admin/recruitment/list?page=" + 0 + "&size=" + recruitmentSize + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }

    function searchRecruitment() {
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        location.href = "<%=CURRENT_SERVER%>/admin/recruitment/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }

    function gotoPagination(pageNum) {
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        let size = "${param.size}"
        if (size === "") {
            size = 10
        }
        location.href = "<%=CURRENT_SERVER%>/admin/recruitment/list?page=" + (pageNum - 1) + "&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }
</script>
</body>

</html>