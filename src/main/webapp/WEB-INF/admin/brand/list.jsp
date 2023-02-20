<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2023/02/16
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/header.jsp" %>
    <style>
        tr {
            cursor: pointer;
        }

        p {
            margin: 0;
        }

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
<body class="hold-transition sidebar-mini" style="min-width: 1600px">
<div class="wrapper">
    <%@include file="../include/sidebar.jsp" %>
    <div class="content-wrapper" style="overflow-y: auto">
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>브랜드 관리(총 <span style="color:#74b9ff">${brandList.page.totalElements}</span>개)</h4>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <section class="content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <button type="button"
                                        class="btn btn-primary"
                                        style="width: 160px;"
                                        onclick="createBrand()"
                                >&nbsp;브랜드 등록
                                </button>
                            </div>
                            <c:choose>
                                <c:when test="${brandList.page.totalElements != 0}">
                                    <div class="card-body table-responsive p-0">
                                        <table class="table text-nowrap">
                                            <thead>
                                            <tr>
                                                <th>번호</th>
                                                <th>브랜드 이름</th>
                                                <th>등록일</th>
                                                <th>삭제</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="brand" items="${brandList.page.content}" varStatus="status">
                                                <fmt:parseDate value="${ brand.regDate }" pattern="yyyy-MM-dd'T'HH:mm"
                                                               var="parseBrandRegDate" type="both"/>
                                                <tr onclick="goToDetailInfo(${brand.brandId})">
                                                    <th scope="row">${brandList.page.pageable.offset + status.index + 1}</th>
                                                    <td>${brand.brandName}</td>
                                                    <td><fmt:formatDate pattern="yyyy년MM월dd일 HH:mm:ss"
                                                                        value="${ parseBrandRegDate }"/></td>
                                                    <td>
                                                        <button type="button"
                                                                class="btn btn-danger"
                                                                style="width: 80px;"
                                                                onclick="deleteBrand(${brand.brandId})"
                                                        >&nbsp;삭제&nbsp;
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="card-footer clearfix">
                                        <ul class="pagination pagination-sm m-0 float-right">
                                            <c:if test="${brandList.page.first == false && brandList.page.pageable.pageNumber > 10}">
                                                <li class="page-item"><a class="page-link" href="#">이전</a></li>
                                            </c:if>
                                            <c:forEach var="pageNum"
                                                       begin="${(brandList.page.pageable.pageNumber+10)/10}"
                                                       end="${((brandList.page.pageable.pageNumber+10)/10)+9}">
                                                <c:if test="${pageNum <= brandList.page.totalPages}">
                                                    <c:choose>
                                                        <c:when test="${pageNum == brandList.page.pageable.pageNumber+1}">
                                                            <li class="page-item active"><a
                                                                    class="page-link"> ${pageNum}</a>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li class="page-item"><a class="page-link"
                                                                                     onclick="gotoPagination(${pageNum})">${pageNum}</a>
                                                            </li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${brandList.page.last == false && ((brandList.page.pageable.pageNumber+10)/10)+9 < brandList.page.totalPages}">
                                                <li class="page-item"><a class="page-link"
                                                                         onclick="gotoPagination(`${((brandList.page.pageable.pageNumber+10)/10)+10}`)">다음</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div style="display: flex; align-items: center; justify-content: center; width: 100%; height: 200px;">
                                        <p>해당 조건의 게시글이 없습니다.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
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

    function deleteBrand(brandId) {
        event.stopPropagation();
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
            url        : "<%=CURRENT_SERVER%>/admin/brand/deleteBrand/" + brandId,
            headers    : {
                "token": token
            },
            contentType: 'application/json',
            method     : "DELETE",
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
            } else {
                Swal.fire({
                    title: "완료",
                    text : "삭제가 완료됐습니다.",
                }).then(() => {
                    location.reload();
                })
            }
        })
    }
    
    function goToDetailInfo(brandId) {
        location.href = "<%=CURRENT_SERVER%>/admin/brand/info?brandId="+brandId;
    }

    function gotoPagination(pageNum) {
        let size = "${param.size}"

        if (size === "") {
            size = 10
        }

        location.href = "<%=CURRENT_SERVER%>/admin/movie/list?page=" + (pageNum - 1) + "&size=" + size;
    }

    function createBrand(){
        location.href = "<%=CURRENT_SERVER%>/admin/brand/info"
    }
</script>
</html>

