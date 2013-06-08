<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lisää kissa</title>
</head>
<body>
<h2>Lisää kissa</h2>
<form action="submitAddCat.do">
	<p>Kutsumanimi: <input name="nickName"/></p>
	<p>Nimi: <input name="name"/></p>
	<p><a href="showSelf.do"><input type="button" name="cancel" value="Peru"/></a>
	<button type="submit">Hyväksy</button></p>
</form>
</body>
</html>