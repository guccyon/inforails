<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="windows-31j"%>
<%@ include file="/contents/jsp/include/standard.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>InfoRails</title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<%=stylesheet("application") %>
<%=stylesheet(actionViewName(params.get("action"))) %>
<%=script("prototype") %>
<%=script(actionViewName(params.get("action"))) %>
</head>
<body>
<div id="wrapper">
<jsp:include flush="true" page="${action}"></jsp:include>
</div>
</body>
</html>