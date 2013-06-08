<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${profile.name}</title>
</head>
<body>
<h2>${profile.name}</h2>

<div><img src="https://graph.facebook.com/${profile.id}/picture?access_token=${access_token}&type=large"/></div>

<h2>Kissat</h2>
<ul>
<c:forEach var="cat"
	items="${cats}">
	<li><a href="showCat.do?id=${cat.id}">${cat.nickName}</a><c:if test="${canEdit}"> (<a href="deleteCat.do?id=${cat.id}">Poista</a>)</c:if></li>
</c:forEach>
</ul>
<c:if test="${canEdit}"> <span><a href="addCat.do">Lisää kissa</a></span></c:if>

<h2>Ystävät</h2>
<ul>
<c:forEach var="friend"
	items="${profile.friends.data}">
	<li><a href="showPerson.do?id=${friend.id}">
	<img src="https://graph.facebook.com/${friend.id}/picture?access_token=${access_token}&type=small"/>
	${friend.name}</a></li>
</c:forEach>
</ul>
<c:if test="${canEdit}">
<span><a href="invite.do">Kutsu ystäviäsi</a></span>
</c:if>
</body>
</html>