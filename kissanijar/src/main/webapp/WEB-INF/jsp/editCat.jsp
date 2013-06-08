<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Muokkaa kissan tietoja</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/kissani.css" />
</head>
<body>
<h2>Muokkaa kissan tietoja</h2>
<form action="submitEditCat.do">
	<input type="hidden" name="id" value="${cat.id}"/>
	<p>Kutsumanimi: <input name="nickName" value="${cat.nickName}"/></p>
	<p>Nimi: <input name="name" value="${cat.name}"/></p>
	<p><a href="showCat.do?id=${cat.id}"><input type="button" name="cancel" value="Peru"/></a>
	<button type="submit">Hyväksy</button></p>
</form>
</body>
</html>