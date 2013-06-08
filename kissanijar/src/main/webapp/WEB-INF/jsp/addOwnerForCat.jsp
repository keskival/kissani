<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lisää kissalle uusi omistaja</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/kissani.css" />
</head>
<body>
<h2>Lisää kissalle uusi omistaja</h2>
<ul>
<c:forEach var="friend"
	items="${profile.friendsInApp}">
	<li><a href="submitAddOwnerForCat.do?ownerId=${friend.id}&catId=${cat.id}">
	<img src="https://graph.facebook.com/${friend.id}/picture?access_token=${access_token}&type=small"/>
	${friend.name}</a></li>
</c:forEach>
</ul>
<p><a href="showCat.do?id=${cat.id}">Peruuta</a></p>
</body>
</html>