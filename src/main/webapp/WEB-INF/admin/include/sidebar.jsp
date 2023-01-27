<%@ page import="static moska.rebora.Common.CommonConst.CURRENT_SERVER" %>
<%--
  Created by IntelliJ IDEA.
  User: kibong
  Date: 2022/12/21
  Time: 4:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/movie/list" class="nav-link">영화 관리</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/recruitment/list" class="nav-link">모집 관리</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/cinema/list" class="nav-link">극장 관리</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/theater/list" class="nav-link">상영관 관리</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/user/list" class="nav-link">회원 관리</a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <a href="<%=CURRENT_SERVER%>/admin/payment/list" class="nav-link">결제 관리</a>
        </li>
    </ul>
    <!-- Right navbar links -->
</nav>
<!-- /.navbar -->

<!-- Main Sidebar Container -->
<aside class="main-sidebar sidebar-dark-primary elevation-4">
    <!-- Brand Logo -->

    <!-- Sidebar -->
    <div class="sidebar">
        <!-- Sidebar Menu -->
        <nav class="mt-2">
            <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu"
                data-accordion="false">
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/movie/list" class="brand-link">
                        <img src="https://rebora.s3.ap-northeast-2.amazonaws.com/default/logo.png" alt="AdminLTE Logo"
                             class="brand-image img-circle elevation-3"
                             style="opacity: .8">
                        <span class="brand-text font-weight-light">리보라 관리자</span>
                    </a>
                </li>
                <!-- Add icons to the links using the .nav-icon class
                     with font-awesome or any other icon font library -->
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/movie/list" class="nav-link">
                        <i class="nav-icon fas fa-film"></i>
                        <p>
                            영화 관리
                        </p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/recruitment/list" class="nav-link">
                        <i class="nav-icon fas fa-users"></i>
                        <p>
                            모집 관리
                        </p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/cinema/list" class="nav-link">
                        <i class="nav-icon fas fa-building"></i>
                        <p>
                            극장 관리
                        </p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/theater/list" class="nav-link">
                        <i class="nav-icon fas fa-ticket-alt"></i>
                        <p>
                            상영관 관리
                        </p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/user/list" class="nav-link">
                        <i class="nav-icon fas fa-users"></i>
                        <p>
                            회원 관리
                        </p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%=CURRENT_SERVER%>/admin/payment/list" class="nav-link">
                        <i class="nav-icon fas fa-shopping-cart"></i>
                        <p>
                            결제 관리
                        </p>
                    </a>
                </li>
            </ul>
        </nav>
        <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
</aside>
</body>
</html>
