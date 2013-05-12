<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="fi.kissani.fb.Profile"%>
<%@page import="fi.kissani.fb.Photo"%>
<%@page import="java.util.ArrayList"%>
<% 
	Profile profile = (Profile) request.getAttribute("profile");
	ArrayList<Photo> nala = (ArrayList<Photo>) request.getAttribute("nala");
%>
<html>
    <head><title>Kissani</title></head>
    <body>
       <div id="fb-root"></div>
       <script src="http://connect.facebook.net/en_US/all.js"></script>
       <div width="740px">
			<span>Tervetuloa <%= profile.getName() %>!</span>
			<h2>Kissasi:</h2>
			<ul id="listOfCats" style="visible: false">
			<li>Bene (<a href="">muokkaa</a> / <a href="">poista</a>)</li>
			<li>Luna (<a href="">muokkaa</a> / <a href="">poista</a>)</li>
			<li>Nala (<a href="">muokkaa</a> / <a href="">poista</a>)</li>
			</ul>
			<span>Test:</span>
			<c:forEach var="kuva" items="${nala}">
				<div><a href="${kuva.link}"><img src="https://graph.facebook.com/${kuva.id}/picture?access_token=${sessionScope.access_token}"/></a></div>
			</c:forEach>
			<a href="">Lis‰‰ kissa</a>
			<h2>Yst‰v‰si:</h2>
			<ul>
			<li>Tero</li>
			</ul>
			<a href="">Lis‰‰ yst‰v‰</a>
		</div>
    </body>
 </html>