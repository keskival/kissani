<%@ include file="head.jspf" %>

<title>${profile.name}</title>
<link rel="canonical" href="https://kissani4.appspot.com/showPerson.do?id=${profile.id}"/>

<%@ include file="head2.jspf" %>
<h2>${profile.name}</h2>

<div><img src="https://graph.facebook.com/${profile.id}/picture?access_token=${access_token}&type=large"/></div>

<h2>Kissat</h2>
<ul class="catList">
<c:forEach var="cat"
	items="${cats}">
	<li><a href="showCat.do?id=${cat.id}">${cat.nickName}</a><c:if test="${canEdit}"> 
	(<a href="deleteCat.do?id=${cat.id}"><img src="<%=request.getContextPath()%>/resources/images/trash.png" alt="poista" title="poista"/></a>)</c:if></li>
</c:forEach>
</ul>
<c:if test="${canEdit}"> <span><a href="addCat.do">Lis‰‰ kissa</a> 
(<a href='javascript:onClick=alert("T‰m‰ toiminto myˆs skannaa kaikista Facebook kuva-albumeistasi kaikki kuvat, joihin on tagattu kissa lempinimell‰‰n, ja lis‰‰ ne kissan kuviin. Huomaa, ett‰ skannaus saattaa kest‰‰ joitain minuutteja jos sinulla on paljon albumeita. Muista myˆs asettaa kuvien n‰kyvyys julkiseksi Facebookista, ett‰ muutkin n‰kev‰t ne.")'><img src="<%=request.getContextPath()%>/resources/images/help.png" alt="ohje" title="T‰m‰ toiminto myˆs skannaa kaikista Facebook kuva-albumeistasi kaikki kuvat, joihin on tagattu kissa lempinimell‰‰n, ja lis‰‰ ne kissan kuviin. Huomaa, ett‰ skannaus saattaa kest‰‰ joitain minuutteja jos sinulla on paljon albumeita. Muista myˆs asettaa kuvien n‰kyvyys julkiseksi Facebookista, ett‰ muutkin n‰kev‰t ne."/></a>)
</span></c:if>

<p>Suora linkki t‰h‰n sivuun: <a href="https://kissani4.appspot.com/showPerson.do?id=${profile.id}">https://kissani4.appspot.com/showPerson.do?id=${profile.id}</a></p>
<%@ include file="trailer.jspf" %>
