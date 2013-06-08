<html>
    <head><title>Kissani</title></head>
    <body>


       <div id="fb-root"></div>
<script>
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '174781145893917', // App ID
      channelUrl : '//kissani4.appspot.com/resources/channel.html', // Channel File
      status     : true, // check login status
      cookie     : true, // enable cookies to allow the server to access the session
      xfbml      : true  // parse XFBML
    });


    FB.ui({
        appId: '174781145893917', // App ID
        display: 'dialog',
        method: 'send',
        name: 'Haluatko tulla mukaan k‰ytt‰m‰‰n Kissani-sovellusta?',
        link: 'https://kissani4.appspot.com',
        redirect_uri: 'https://kissani4.appspot.com'
        });
    // Additional initialization code here
  };

  // Load the SDK Asynchronously
  (function(d){
     var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all.js";
     d.getElementsByTagName('head')[0].appendChild(js);
   }(document));
  </script>

    </body>
 </html>