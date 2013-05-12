<html>
    <head><title>Kissani</title></head>
    <body>
       <div id="fb-root"></div>
       <script src="http://connect.facebook.net/en_US/all.js"></script>
       <script>
          FB.init({ appId:'174781145893917', cookie:true, xfbml:true });
       </script>
       <fb:serverFbml width="740px">
          <script type="text/fbml">
          <fb:fbml>
           <fb:request-form method='POST' invite=true
            type='Kissani'
            content='Haluatko tulla mukaan käyttämään Kissani sovellusta?'>
            <fb:multi-friend-selector cols=3
             actiontext="Kutsu ystäväsi mukaan käyttämään tätä sovellusta!"
            />
            </fb:request-form>
           </fb:fbml>
           </script>
       </fb:serverFbml>
    </body>
 </html>