<%@page import="jp.co.infonic.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page language="java" pageEncoding="windows-31j"%>
<%@ include file="/contents/jsp/include/helper.jsp" %>
<%@ include file="/contents/jsp/include/alias.jsp" %>
<%--
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
--%>
<%!
String actionViewName(Object o) {
	return StringUtil.decamelize((String)o);
}
%>
<% pageContext.setAttribute("CONTEXT", request.getContextPath()); %>
<% Map params = (Map)request.getAttribute("params"); %>
<% Map infoSession = (Map)request.getAttribute("session"); %>