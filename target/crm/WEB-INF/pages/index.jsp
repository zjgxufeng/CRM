<%@ page contentType="text/html;charset=UTF-8" language="java" %>//jsp标志
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">

	<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		window.location.href = "settings/qx/user/toLogin.do";
	</script>
</body>
</html>