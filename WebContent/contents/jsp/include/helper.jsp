<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page language="java" pageEncoding="windows-31j"%>
<%@page import="jp.co.infonic.inforails.framework.presentation.routing.ContextContentsResolver"%>
<%!
static final String REGEXP_URL = "(http://|https://){1}[\\w\\.\\-/:]+";
static final String REGEXP_MAIL = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

static String htmlEncode(String value) {
	value = value.replaceAll( "<" , "&lt;" ).replaceAll(">" , "&gt;" );
	return value.replaceAll( "(\r\n|\n|\r)" , "<br>" );
}

static String urlReplace(String value) {
	value = value.replaceAll(REGEXP_URL,"<a href='$0' target='blank'>$0</a>" );
	return value.replaceAll(REGEXP_MAIL, "<a href='mailto:$0'>$0</a>");
}
%>

<%!
static String script(String file, String charset) {
	file = file.matches(".js$") ? file : file + ".js";
	if (ContextContentsResolver.isExist("public/javascript/" + file)) {
		String src = ContextContentsResolver.getCollectPath("public/javascript/" + file);
		String chset = charset != null ? "charset='" + charset + "'" : "";
		return "<script type='text/javascript' src='" + src + "' " + chset + "></script>";
	} else {
		return "";
	}
}
static String script(String file) {
	return script(file, null);
}
static String stylesheet(String file, String charset) {
	file = file.matches(".css$") ? file : file + ".css";
	if (ContextContentsResolver.isExist("public/css/" + file)) {
		String href = ContextContentsResolver.getCollectPath("public/css/" + file);
		return "<link rel='stylesheet' type='text/css' href='" + href + "'></link>";
	} else {
		return "";
	}
}

static String stylesheet(String file) {
	return stylesheet(file, null);
}

static String img_tag(String file) {
	return "<img src='" + ContextContentsResolver.getCollectPath("public/images/" + file) + "'>";
}


static String radio_tag(String name, String value) {
	return radio_tag(name, value, null);
}
static String radio_tag(String obj, String name, String value, Map values) {
	return radio_tag(obj + "." + name, value, values.get(name));
}
static String radio_tag(String name, String value, Object defa) {
	StringBuffer sb = new StringBuffer();
	sb.append("<input type='radio' name='").append(name).append("' ");
	sb.append("value='").append(value).append("' ");
	if (defa != null && value.equals(defa)) sb.append("checked='checked' ");
	return sb.append(">").toString();
}



static String check_box(String name, Object value) {
	StringBuffer sb = new StringBuffer();
	sb.append("<input type='checkbox' name='").append(name).append("' ");
	if (value instanceof Boolean && (Boolean)value) {
		sb.append("checked='checked'");
	}
	return sb.append(">").toString();
}
static String check_box(String obj, String name, Map values) { return check_box(obj + "." + name, values.get(name)); }
static String check_box(String name) { return check_box(name, false); }
%>
