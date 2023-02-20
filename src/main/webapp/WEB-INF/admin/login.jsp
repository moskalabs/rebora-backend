<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %><%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/11/29
  Time: 1:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>리보라 관리자 페이지</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>
</head>
<body style="
    align-items: center;
    justify-content: center;
    display: flex;
    background-color:#a29bfe;
    ">
<div class="container text-center container rounded-2" style="max-width:400px; background-color:#6c5ce7">
    <div class="row row-cols-1">
        <div class="col mt-4">
            <h3 style="color:white">리보라 관리자</h3>
        </div>
        <div class="col input-group mb-3 mt-4">
            <span class="input-group-text">아이디&nbsp;&nbsp;&nbsp;</span>
            <input type="text" class="form-control" aria-label="유저 이메일"  id="userEmail" placeholder="아이디를 입력해 주세요">
        </div>
        <div class="col input-group mb-3 mt-2">
            <span class="input-group-text">비밀번호</span>
            <input type="password" class="form-control" id="password" placeholder="비밀번호를 입력해 주세요" aria-label="패스워드">
        </div>
        <div class="col mx-auto mb-3 mt-3">
            <button type="button" onclick="onclickLogin()" class="btn btn-light btn-lg">로그인</button>
        </div>
    </div>
</div>
<script>
    function onclickLogin() {
        let userEmail = $("#userEmail").val();
        let password = $("#password").val();
        if(userEmail === ""){
            Swal.fire({
                title : "오류",
                text : "아이디를 입력해 주세요."
            })
            return false;
        }else if(password === ""){
            Swal.fire({
                title : "오류",
                text : "비밀번호를 입력해 주세요."
            })
            return false;
        }else{
            $.ajax({
                url : "<%=CURRENT_SERVER%>/admin/adminLogin",
                data : {
                    userEmail,
                    password
                },
                method : "POST",
                dataType : "json"
            }).done(function (data) {
                console.log(data);
                if(!data.result){
                    Swal.fire({
                        title : "오류",
                        text : data.message
                    })
                }else{
                    localStorage.setItem("token", data.token);
                    location.href = "<%=CURRENT_SERVER%>/admin/movie/list";
                }
            })
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</body>

</html>