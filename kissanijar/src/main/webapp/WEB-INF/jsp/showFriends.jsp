<%@ include file="head.jspf" %>

<title>${profile.name}</title>
<link rel="canonical" href="https://kissani4.appspot.com/showFriends.do?id=${profile.id}"/>

<%@ include file="head2.jspf" %>

<h2>Yst‰v‰si, jotka k‰ytt‰v‰t t‰t‰ sovellusta</h2>
<ul class="persons">
<c:forEach var="friend"
	items="${profile.friendsInApp}">
	<li><a href="showPerson.do?id=${friend.id}">
	<img src="https://graph.facebook.com/${friend.id}/picture?access_token=${access_token}&type=small"/>
	${friend.name}</a></li>
</c:forEach>
</ul>
<c:if test="${canEdit}">
<span><a href="invite.do" target="_blank">Kutsu lis‰‰ yst‰vi‰si</a></span>
</c:if>

<%@ include file="trailer.jspf" %>
