<%@ include file="head.jspf" %>

<title>${cat.nickName}</title>
<link rel="canonical" href="https://kissani4.appspot.com/showCat.do?id=${cat.id}"/>

<%@ include file="head2.jspf" %>

<div class="titleDiv">
<h2>${cat.nickName}</h2>

<img src="https://graph.facebook.com/${cat.photo.id}/picture?access_token=${access_token}" class="cat-photo" alt="Kuva kissasta" />
</div>
	
<div class="gallery">
<c:forEach var="catPhoto"
	items="${cat.photosAsSet}">
	<div class="photo">
	<a href="https://graph.facebook.com/${catPhoto.id}/picture?access_token=${access_token}" rel="lytebox[cat]"
		title="&lt;a href=&quot;${catPhoto.link}&quot; target=&quot;_blank&quot;&gt;Alkuper‰inen&lt;/a&gt;">
	<img src="https://graph.facebook.com/${catPhoto.id}/picture?access_token=${access_token}" class="cat-gallery-thumbnail" alt="Kuva kissasta" /></a>
	<c:if test="${canEdit}">
	<div class="underpictext">
        <a href="setCatDefaultPhoto.do?catId=${cat.id}&photoId=${catPhoto.id}">aseta oletuskuvaksi</a>
		(<a href="removePhotoFromCat.do?catId=${cat.id}&photoId=${catPhoto.id}"><img src="<%=request.getContextPath()%>/resources/images/trash.png" alt="poista kuva galleriasta" title="poista kuva galleriasta"/></a>)
    </div>
	</c:if>
	<div style="clear: both"></div>
    </div>
</c:forEach>
</div>

<div class="info">
<h2>Tiedot</h2>
<ul>
<li>Kutsumanimi: ${cat.nickName}</li>
<li>Nimi: ${cat.name}</li>
</ul>
<h2>Omistajat</h2>
<ul id="owners">
<c:forEach var="owner"
	items="${owners}">
	<li><div>
	<img src="https://graph.facebook.com/${owner.id}/picture?access_token=${access_token}&type=large" style="border: none; height: 120px;"/>
	<div class="underpictext">
	<a href="showPerson.do?id=${owner.id}">${owner.name}</a>
	<c:if test="${canEdit}">
	<c:if test="${profile.id ne owner.id}">
		(<a href="removeOwnerFromCat.do?ownerId=${owner.id}&catId=${cat.id}"><img src="<%=request.getContextPath()%>/resources/images/trash.png" alt="poista omistaja kissalta" title="poista omistaja kissalta"/></a>)
	</c:if>
	</c:if>
    </div>
	</div></li>
</c:forEach>
</ul>
<ul id="catMenu">
<c:if test="${canEdit}">
<li><a href="addOwnerForCatForm.do?id=${cat.id}">Lis‰‰ omistaja</a>
(<a href='javascript:onClick=alert("T‰ll‰ voit lis‰t‰ kavereistasi muita omistajia kissallesi.")'><img src="<%=request.getContextPath()%>/resources/images/help.png" alt="ohje" title="T‰ll‰ voit lis‰t‰ kavereistasi muita omistajia kissallesi."/></a>)
</li>
<li><a href="tagScan.do?id=${cat.id}">Lis‰‰ kuvia skannaamalla albumisi</a> 
(<a href='javascript:onClick=alert("T‰m‰ toiminto skannaa kaikista kissan omistajien Facebook kuva-albumeista kaikki kuvat, joihin on tagattu kissa lempinimell‰‰n, ja lis‰‰ ne kissan kuviin. Huomaa, ett‰ skannaus saattaa kest‰‰ joitain minuutteja jos sinulla on paljon albumeita. Muista myˆs asettaa kuvien n‰kyvyys julkiseksi Facebookista, ett‰ muutkin n‰kev‰t ne.")'><img src="<%=request.getContextPath()%>/resources/images/help.png" alt="ohje" title="T‰m‰ toiminto skannaa kaikista kissan omistajien Facebook kuva-albumeista kaikki kuvat, joihin on tagattu kissa lempinimell‰‰n, ja lis‰‰ ne kissan kuviin. Huomaa, ett‰ skannaus saattaa kest‰‰ joitain minuutteja jos sinulla on paljon albumeita. Muista myˆs asettaa kuvien n‰kyvyys julkiseksi Facebookista, ett‰ muutkin n‰kev‰t ne."/></a>)
</li>
<li><a href="editCat.do?id=${cat.id}">Muokkaa kissan tietoja</a></li>
</c:if>
<li><a href="addCatFriendForCatForm.do?id=${cat.id}">Lis‰‰ t‰m‰ kissa oman kissasi kaveriksi</a></li>
</ul>
</div>

<div class="friends">
<h2>Kissakaverit</h2>

<ul class="catFriendList">
<c:forEach var="friend"
	items="${catFriends}">
	<li><div class="catFriend">
	<a href="showCat.do?id=${friend.id}">
	<c:choose>
	
	<c:when test='${ friend.photo == null }'>
		<img src="resources/noCatPhoto.jpg" class="cat-gallery-thumbnail" alt="Tyhj‰ kuva kissasta" />
	</c:when>
	<c:otherwise>
		<img src="https://graph.facebook.com/${friend.photo.id}/picture?access_token=${access_token}" class="cat-gallery-thumbnail" alt="Kuva kissasta" />
	</c:otherwise>

	</c:choose>
	</a>
	<div class="underpictext">
	<a href="showCat.do?id=${friend.id}">
	<c:choose>
	<c:when test='${ empty friend.name }'>
	</c:when>
	<c:otherwise>
	${friend.name} : 
	</c:otherwise>
	</c:choose>
	${friend.nickName}
	</a>
	<c:if test="${canEdit}">
		(<a href="removeFriendFromCat.do?friendId=${friend.id}&catId=${cat.id}"><img src="<%=request.getContextPath()%>/resources/images/trash.png" alt="poista yst‰v‰ kissalta" title="poista yst‰v‰ kissalta"/></a>)
	</c:if>
    </div>
    </div>
	</li>
</c:forEach>
</ul>
</div>

<div class="footer">
<p>Suora linkki t‰h‰n sivuun: <a href="https://kissani4.appspot.com/showCat.do?id=${cat.id}">https://kissani4.appspot.com/showCat.do?id=${cat.id}</a></p>
</div>
<%@ include file="trailer.jspf" %>
