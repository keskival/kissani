<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lisää kissalle uusi kissakaveri</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/kissani.css" />
</head>
<body>
<h2>Lisää kissalle uusi kissakaveri</h2>
<ul>
<c:forEach var="friend"
	items="${possibleNewCatFriends}">
	<li><a href="submitAddCatFriendForCat.do?catId=${cat.id}&friendId=${friend.id}">
	<c:choose>
	<c:when test="${ empty friend.photo }">
		<img src="resources/noCatPhoto.jpg" class="cat-gallery-thumbnail" alt="Tyhjä kuva kissasta" />
	</c:when>
	<c:otherwise>
		<img src="https://graph.facebook.com/${friend.photo.id}/picture?access_token=${access_token}" class="cat-gallery-thumbnail" alt="Kuva kissasta" />
	</c:otherwise>
	</c:choose>
	<c:choose>
	<c:when test="${ empty friend.name }">
	</c:when>
	<c:otherwise>
	${friend.name} : 
	</c:otherwise>
	</c:choose>
	${friend.nickName}</a></li>
</c:forEach>
</ul>
<p><a href="showCat.do?id=${cat.id}">Peruuta</a></p>
</body>
</html>