<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/02
  Time: 4:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        tr, td {
            font-size: 14px;
        }
    </style>
</head>
<c:set var="today" value="<%=new java.util.Date()%>"/>
<c:set var="date"><fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/></c:set>
<fmt:parseDate value="${ param.startDate }" pattern="yyyy-MM-dd"
               var="startDate" type="both"/>
<c:set var="parseStartDate"><fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/></c:set>
<fmt:parseDate value="${ param.endDate }" pattern="yyyy-MM-dd"
               var="endDate" type="both"/>
<c:set var="parseEndDate"><fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/></c:set>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>회원 관리(총 <span style="color:#74b9ff">${userList.page.totalElements}</span>개)</h4>
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
                                <div style="display:flex; flex-direction:column; text-align:left">
                                    <p class="mb-1">상태</p>
                                    <div style="display:flex; width:100px;">
                                        <div class="form-group" style="display:flex; width:130px;">
                                            <select id="userGrade" class="form-control" style="font-size:14px;"
                                                    aria-label="유저 등급">
                                                <c:choose>
                                                    <c:when test="${param.userGrade == '' || param.userGrade == 'NORMAL'}">
                                                        <option value="NORMAL" selected>일반</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="NORMAL">일반</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userGrade == 'ADMIN'}">
                                                        <option value="ADMIN" selected>관리자</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="ADMIN">관리자</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userGrade == 'WITHDRAWAL'}">
                                                        <option value="WITHDRAWAL" selected>탈퇴</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="WITHDRAWAL">탈퇴</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userGrade == 'DORMANCY'}">
                                                        <option value="DORMANCY" selected>휴면</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="DORMANCY">휴면</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="ml-3" style="display:flex; flex-direction:column; text-align:left">
                                    <p class="mb-1">회원타입</p>
                                    <div style="display:flex; width:100px;">
                                        <div class="form-group" style="display:flex; width:130px;">
                                            <select id="userSnsKind" class="form-control" style="font-size:14px;"
                                                    aria-label="Default select example">
                                                <c:choose>
                                                    <c:when test="${param.userSnsKind == '' || param.userSnsKind == 'ALL'}">
                                                        <option value="ALL" selected>전체</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="ALL">전체</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userSnsKind == 'NORMAL'}">
                                                        <option value="NORMAL" selected>일반</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="NORMAL">일반</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userSnsKind == 'NAVER'}">
                                                        <option value="NAVER" selected>네이버</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="NAVER">네이버</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userSnsKind == 'KAKAO'}">
                                                        <option value="NAVER" selected>카카오</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="NAVER">카카오</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.userSnsKind == 'APPLE'}">
                                                        <option value="NAVER" selected>애플</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="NAVER">애플</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="ml-3"
                                     style="display:flex; flex-direction:column; height:100%; text-align:left; justify-content:flex-end;">
                                    <p class="mb-1">가입일</p>
                                    <div class="mb-2" style="display:flex; width:120px;">
                                        <input type="date" id="startDate" name="trip-start"
                                               value="${parseStartDate != '' ? parseStartDate : ''}" min="2022-01-01"
                                               max="2099-12-30">
                                    </div>
                                </div>
                                <div class="ml-1"
                                     style="display:flex; flex-direction:column; height:100%; text-align:left; justify-content:flex-end;">
                                    <p class="mb-1"> &nbsp; </p>
                                    <p class="mb-2" style="text-align: center;"> - </p>
                                </div>
                                <div class="ml-3"
                                     style="display:flex; flex-direction:column; height:100%; text-align:left; justify-content:flex-end;">
                                    <p class="mb-1"> &nbsp; </p>
                                    <div class="mb-2" style="display:flex; width:120px;">
                                        <input type="date" id="endDate" name="trip-start"
                                               value="${parseEndDate != '' ? parseEndDate : ''}" min="2022-01-01"
                                               max="2099-12-30">
                                    </div>
                                </div>
                                <div class="ml-3" style="display:flex; flex-direction:column; text-align:left">
                                    <p class="mb-1">상세조건</p>
                                    <div style="display:flex; width:100px;">
                                        <div class="form-group" style="display:flex; width:130px;">
                                            <select class="form-control" aria-label="Default select example"
                                                    id="searchCondition">
                                                <c:choose>
                                                    <c:when test="${param.searchCondition == '' || param.searchCondition == 'userEmail'}">
                                                        <option value="userEmail" selected>아이디</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="userEmail">아이디</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.searchCondition == 'userName'}">
                                                        <option value="userName" selected>이름</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="userName">이름</option>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${param.searchCondition == 'userNickname'}">
                                                        <option value="userNickname" selected>닉네임</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="userNickname">닉네임</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="ml-3"
                                     style="display:flex; flex-direction:column; height:100%; justify-content:flex-end; align-items: center; width: 60%;">
                                    <p class="mb-1"> &nbsp; </p>
                                    <input type="text" style="width:100%" value="${param.searchWord}"
                                           class="form-control mb-2"
                                           id="searchWord">
                                </div>
                                <div class="ml-3"
                                     style="display:flex; flex-direction:column; text-align:left; height:100%; justify-content:flex-end; width: 80px;">
                                    <p class="mb-1"> &nbsp;</p>
                                    <button type="button" onclick="searchUser()" class="btn btn-secondary mb-2"
                                            style="width: 80px;">&nbsp;검색&nbsp;
                                    </button>
                                </div>
                                <div class="ml-3"
                                     style="display:flex; flex-direction:column; text-align:left; height:100%; justify-content:flex-end; width: 140px;">
                                    <p class="mb-1"> &nbsp;</p>
                                    <button type="button" onclick="createUser()" class="btn btn-secondary mb-2"
                                            style="width: 140px;">&nbsp;회원 등록&nbsp;
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="content-header" style="padding: 6px;">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12" style="display:flex; justify-content:flex-end;">
                        <div class="form-group ml-4" style="display:flex; width:130px;">
                            <select class="form-control select2bs4" aria-label="Default select example"
                                    onchange="changePageSize()"
                                    id="userSize">
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
        </section>
        <section class="content" style="padding-bottom: 100px;">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                    <tr>
                                        <th scope="col" style="width:5%">번호</th>
                                        <th scope="col" style="width:5%">상태</th>
                                        <th scope="col" style="width:15%">아이디</th>
                                        <th scope="col" style="width:8%">회원 타입</th>
                                        <th scope="col" style="width:10%">이름</th>
                                        <th scope="col" style="width:12%">닉네임</th>
                                        <th scope="col" style="width:15%">회원 가입일</th>
                                        <th scope="col" style="width:6%">신고/<br>차단수</th>
                                        <th scope="col" style="width:11%"></th>
                                        <th scope="col" style="width:10%"></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="user" items="${userList.page.content}" varStatus="status">
                                        <fmt:parseDate value="${ user.regDate }" pattern="yyyy-MM-dd'T'HH:mm"
                                                       var="parseUserRegDate" type="both"/>
                                        <tr style="cursor:pointer" onclick="goToUserInfo(${user.userId})">
                                            <th scope="row">${userList.page.pageable.offset + status.index + 1}</th>
                                            <c:choose>
                                                <c:when test="${user.userGrade == 'NORMAL'}">
                                                    <td>일반</td>
                                                </c:when>
                                                <c:when test="${user.userGrade == 'ADMIN'}">
                                                    <td>관리자</td>
                                                </c:when>
                                                <c:when test="${user.userGrade == 'DORMANCY'}">
                                                    <td>휴면</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>탈퇴</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${user.userEmail}</td>
                                            <c:choose>
                                                <c:when test="${user.userSnsKind == 'NAVER'}">
                                                    <td>일반</td>
                                                </c:when>
                                                <c:when test="${user.userSnsKind == 'KAKAO'}">
                                                    <td>관리자</td>
                                                </c:when>
                                                <c:when test="${user.userSnsKind == 'APPLE'}">
                                                    <td>휴면</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>일반</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${user.userName}</td>
                                            <td>${user.userNickname}</td>
                                            <td><fmt:formatDate pattern="yyyy년MM월dd일"
                                                                value="${ parseUserRegDate }"/></td>
                                            <td>10</td>
                                            <c:choose>
                                                <c:when test="${user.userUseYn == true}">
                                                    <td>
                                                        <button type="button"
                                                                class="btn btn-secondary ms-3 btn-secondary"
                                                                onclick="
                                                                        onclickUserUse(${user.userId}, false)">&nbsp;비활성&nbsp;
                                                        </button>
                                                    </td>
                                                </c:when>
                                                <c:when test="${user.userUseYn == false}">
                                                    <td>
                                                        <button type="button" class="btn btn-secondary ms-3 btn-primary"
                                                                onclick="
                                                                        onclickUserUse(${user.userId}, true)
                                                                        ">&nbsp;활성&nbsp;
                                                        </button>
                                                    </td>
                                                </c:when>
                                            </c:choose>
                                            <td>
                                                <button type="button" class="btn btn-secondary ms-3 btn-secondary">
                                                    &nbsp;삭제&nbsp;
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer clearfix">
                                <ul class="pagination pagination-sm m-0 float-right">
                                    <c:if test="${userList.page.first == false && userList.page.pageable.pageNumber > 10}">
                                        <li class="page-item"><a class="page-link" href="#">이전</a></li>
                                    </c:if>
                                    <c:forEach var="pageNum" begin="${(userList.page.pageable.pageNumber+10)/10}"
                                               end="${((userList.page.pageable.pageNumber+10)/10)+9}">
                                        <c:if test="${pageNum <= userList.page.totalPages}">
                                            <c:choose>
                                                <c:when test="${pageNum == userList.page.pageable.pageNumber+1}">
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
                                    <c:if test="${userList.page.last == false && ((userList.page.pageable.pageNumber+10)/10)+9 < userList.page.totalPages}">
                                        <li class="page-item"><a class="page-link"
                                                                 onclick="gotoPagination(`${((userList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<%@include file="../include/footer.jsp" %>
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

    function searchUser() {
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        let userGrade = $("#userGrade").val();
        let userSnsKind = $("#userSnsKind").val();
        let startDate = $("#startDate").val();
        let endDate = $("#endDate").val();

        location.href = "<%=CURRENT_SERVER%>/admin/user/list?page=0&size=10&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&userGrade=" + userGrade + "&userSnsKind=" + userSnsKind + "&startDate=" + startDate + "&endDate=" + endDate;
    }


    function changePageSize() {
        let userSize = $("#userSize").val();
        let searchWord = $("#searchWord").val();
        let searchCondition = $("#searchCondition").val();
        let userGrade = $("#userGrade").val();
        let userSnsKind = $("#userSnsKind").val();
        let startDate = $("#startDate").val();
        let endDate = $("#endDate").val();

        location.href = "<%=CURRENT_SERVER%>/admin/user/list?page=0&size=" + userSize + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition + "&userGrade=" + userGrade + "&userSnsKind=" + userSnsKind + "&startDate=" + startDate + "&endDate=" + endDate;
    }

    function onclickUserUse(userId, userUseYn) {
        event.stopPropagation();
        Swal.fire({
            title            : "취소",
            text             : "해당 유저를 " + (userUseYn ? "활성" : "비활성") + " 하시겠습니까?",
            showCancelButton : true,
            confirmButtonText: '확인',
            cancelButtonText : '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url     : "<%=CURRENT_SERVER%>/admin/user/updateUseYn/" + userId,
                    data    : {
                        userUseYn: userUseYn
                    },
                    method  : "PUT",
                    dataType: "json",
                }).done(function (data) {
                    if (!data.result) {
                        Swal.fire({
                            title: "오류",
                            text : data.message,
                        }).then(() => {
                            return false;
                        })
                    } else {
                        Swal.fire({
                            title: "완료",
                            text : "해당 유저 " + (userUseYn ? "활성" : "비활성") + "이 완료되었습니다.",
                        }).then(() => {
                            location.reload();
                        })
                    }
                });
            } else {
                return false;
            }
        })
    }

    function onclickDeleteUser(userId) {
        Swal.fire({
            title            : "취소",
            text             : "해당 유저를 삭제 하시겠습니까? 삭제된 유저의 데이터는 복원되지 않습니다.",
            showCancelButton : true,
            confirmButtonText: '확인',
            cancelButtonText : '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url     : "<%=CURRENT_SERVER%>/admin/user/deleteUser/" + userId,
                    method  : "DELETE",
                    dataType: "json",
                }).done(function (data) {
                    if (!data.result) {
                        Swal.fire({
                            title: "오류",
                            text : data.message,
                        }).then(() => {
                            return false;
                        })
                    } else {
                        Swal.fire({
                            title: "완료",
                            text : "해당 유저 삭제가 완료 되었습니다.",
                        }).then(() => {
                            location.reload();
                        })
                    }
                });
            } else {
                return false;
            }
        })
    }

    function goToUserInfo(userId) {
        location.href = "<%=CURRENT_SERVER%>/admin/user/info?userId=" + userId;
    }

    function createUser() {
        location.href = "<%=CURRENT_SERVER%>/admin/user/info";
    }

    //추가 개발하애함
    function gotoPagination(pageNum) {
        let searchWord = "${param.searchWord}";
        let searchCondition = "${param.searchCondition}";
        let size = "${param.size}"
        if (size === "") {
            size = 10
        }
        location.href = "<%=CURRENT_SERVER%>/admin/user/list?page=" + (pageNum - 1) + "&size=" + size + "&searchWord=" + searchWord + "&searchCondition=" + searchCondition;
    }
</script>
</html>
