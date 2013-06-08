<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Kirjaudu sisään</title>
</head>
<body>
      <div id="fb-root"></div>
      <script src="http://connect.facebook.net/en_US/all.js"></script>
      <script>
         FB.init({ 
            appId:'174781145893917', cookie:true, 
            status:true, xfbml:true 
         });
         FB.Event.subscribe('auth.login', function(response) {
           window.location.reload();
         });
      </script>
      <fb:login-button perms="user_photos,user_photo_video_tags,publish_stream,email,offline_access">Kirjaudu sisään Facebookin avulla</fb:login-button>
</body>
</html>
