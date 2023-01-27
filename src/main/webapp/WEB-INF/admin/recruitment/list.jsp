<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/01
  Time: 4:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <%@include file="../include/header.jsp" %>
    <style>
        .select2-selection {
            height: 35px;
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
                        <h4>모집 관리(총 <span style="color:#74b9ff">${recruitmentList.page.totalElements}</span>개)</h4>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" style="display: flex; flex-direction: row;">
                                <div class="form-group ml-4" style="display:flex; width:130px;">
                                    <select class="form-control select2bs4" style="width: 100%;" id="searchCondition">
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
                                <input type="text" class="form-control ml-4" style="width:100%" id="searchWord"
                                       value="${param.searchWord}"
                                       placeholder="검색어를 입력해 주세요">
                                <div class="ml-4" style="display:flex; width:130px; height: 36px;">
                                    <button type="button" class="btn btn-block bg-gradient-secondary"
                                            onclick="searchRecruitment()">
                                        &nbsp;검색&nbsp;
                                    </button>
                                </div>
                                <div class="form-group ml-4" style="display:flex; width:130px;">
                                    <select class="form-control select2bs4" style="width: 100%;" id="recruitmentSize"
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
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <div class="px-4" style="display:flex; flex-direction:column; width: 100%; height:100%;">
            <c:choose>
                <c:when test="${recruitmentList.page.totalElements == 0}">
                    <div class="mt-4"
                         style="display: flex;width: 100%; height: 600px; justify-content: center; align-items: center;">
                        <h4>해당 조건의 리스트가 없습니다.</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <section class="content">
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body table-responsive p-0">
                                            <table class="table table-hover text-nowrap">
                                                <thead>
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
                                                <c:forEach var="recruitment" items="${recruitmentList.page.content}"
                                                           varStatus="status">
                                                    <fmt:parseDate value="${ recruitment.recruitmentEndDate }"
                                                                   pattern="yyyy-MM-dd'T'HH:mm"
                                                                   var="parseRecruitmentEndDate" type="both"/>
                                                    <fmt:parseDate value="${ recruitment.theaterStartDatetime }"
                                                                   pattern="yyyy-MM-dd'T'HH:mm"
                                                                   var="parseTheaterStartDatetime" type="both"/>
                                                    <tr onclick="goToDetailInfo(${recruitment.recruitmentId})">
                                                        <th>${recruitmentList.page.pageable.offset + status.index + 1}</th>
                                                        <td>${recruitment.movieName}</td>
                                                        <td>${recruitment.theaterCinemaName} ${recruitment.theaterName}</td>
                                                        <td><fmt:formatDate pattern="yyyy년MM월dd일"
                                                                            value="${ parseRecruitmentEndDate }"/></td>
                                                        <td><fmt:formatDate pattern="yyyy년MM월dd일"
                                                                            value="${ parseTheaterStartDatetime }"/></td>
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
                                        </div>
                                        <div class="card-footer clearfix">
                                            <ul class="pagination pagination-sm m-0 float-right">
                                                <c:if test="${recruitmentList.page.first == false && recruitmentList.page.pageable.pageNumber > 10}">
                                                    <li class="page-item"><a class="page-link" href="#">이전</a></li>
                                                </c:if>
                                                <c:forEach var="pageNum"
                                                           begin="${(recruitmentList.page.pageable.pageNumber+10)/10}"
                                                           end="${((recruitmentList.page.pageable.pageNumber+10)/10)+9}">
                                                    <c:if test="${pageNum <= recruitmentList.page.totalPages}">
                                                        <c:choose>
                                                            <c:when test="${pageNum == recruitmentList.page.pageable.pageNumber+1}">
                                                                <li class="page-item active"><a
                                                                        class="page-link"> ${pageNum}</a></li>
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
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<%@include file="../include/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {

        $('.select2bs4').select2({
            theme: 'bootstrap4'
        })

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
        console.log()
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