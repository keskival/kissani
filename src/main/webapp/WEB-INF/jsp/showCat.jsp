<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${cat.nickName}</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/MooFlow.css" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>          
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/mootools/mootools-1.2-core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/mootools/mootools-1.2-more.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/mooflow/MooFlow.js"></script>
</head>
<body>
<h2>${cat.nickName}</h2>
<script type="text/javascript">
window.addEvent('domready', function(){
 
	var mf = new MooFlow($('MooFlow'), {
		startIndex: 5,
		useSlider: true,
		useAutoPlay: true,
		useCaption: true,
		useResize: true,
		useWindowResize: true,
		useMouseWheel: true,
		useKeyInput: true
	});
});
</script>

<div id="MooFlow">
<c:forEach var="catPhoto"
	items="${cat.photos}">
	<a href="${catPhoto.link}"><img src="https://graph.facebook.com/${catPhoto.id}/picture?access_token=${access_token}"/></a>
</c:forEach>
</div>

<h2>Tiedot</h2>
<ul>
<li>Kutsumanimi: ${cat.nickName}</li>
<li>Nimi: ${cat.name}</li>
</ul>
<h2>Omistajat</h2>
<ul>
<c:forEach var="owner"
	items="${owners}">
	<li><div><img src="https://graph.facebook.com/${owner.id}/picture?access_token=${access_token}&type=large"/><a href="showPerson.do?id=${owner.id}">${owner.name}</a></div></li>
</c:forEach>
</ul>
<c:if test="${canEdit}">
<a href="addOwner.do?id=${cat.id}">Lisää omistaja</a>
<a href="tagScan.do?id=${cat.id}">Lisää kuvia</a>
<a href="editCat.do?id=${cat.id}">Muokkaa kissan tietoja</a>
</c:if>

<h2>Kissakaverit</h2>
<span>TODO</span>

</body>
</html>