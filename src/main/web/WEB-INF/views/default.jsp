<%--
  Created by IntelliJ IDEA.
  User: suyeon
  Date: 2018-12-20
  Time: 21:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <%@ include file = "/resources/inc/meta.jsp" %>
    <title>Do it!</title>
    <script type="text/javascript" src="/resources/js/todo.js?ver=1.0.0" charset="utf-8" ></script>
    <link rel="stylesheet" href="/resources/css/todo.css?ver=1.0.0">
</head>
<body>
<div id="wrap">
    <header>
        <h1>Do it!</h1>
    </header>
    <section>
        <div id="searchArea">
            <form method="post" name="searchForm">
                <fieldset>
                    <legend>검색폼</legend>
                    <input type="text" name="input_dt" class="datepicker" placeholder="작성일" />
                    <input type="text" name="update_dt" class="datepicker" placeholder="수정일" />
                    <input type="text" name="content" placeholder="내용검색" />
                    <button type="submit">검색</button>
                </fieldset>
            </form>
        </div>
        <div id="formDiv">
            <form method="post" name="writeForm">
                <fieldset>
                    <input type="hidden" name="todo_no" value="0" />
                    <legend>등록폼</legend>
                    <input type="text" name="content" placeholder="할일을 입력 후 Enter!" /><button type="reset" class="btnCancel">다시</button>
                </fieldset>
            </form>
        </div>

        <div id="listDiv">
            <ul id="listPage"></ul>

            <div id="pagination-1"></div>
        </div>
    </section>
    <footer>

    </footer>
</div>
</body>
</html>
