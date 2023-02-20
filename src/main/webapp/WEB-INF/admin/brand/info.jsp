<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2023/02/16
  Time: 1:50 PM
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
                        <h4>브랜드 상세</h4>
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
                                    <th class="border-end" scope="row" style="width: 20%;">브랜드 이름</th>
                                    <td style="width: 80%;">
                                        <input type="text" value="${brand.content.brandName}"
                                               style="width:250px"
                                               class="form-control mb-2" id="brandName"
                                               aria-describedby="emailHelp">
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
                            <button type="button" class="btn btn-success mr-3" onclick="saveBrand()">&nbsp;저장&nbsp;
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
        location.href = "<%=CURRENT_SERVER%>/admin/brand/list";
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

    function saveBrand() {
        let brandId = "${param.brandId}";
        let brandName = $("#brandName").val();

        if (brandName === "") {
            alertMessage("오류", "브랜드를 입력 주세요")
            return false;
        }

        $.ajax({
            url        : "<%=CURRENT_SERVER%>/admin/brand/saveBrand",
            data       : {
                brandId  : brandId,
                brandName: brandName
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
