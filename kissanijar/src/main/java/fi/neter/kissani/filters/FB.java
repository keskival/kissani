package fi.neter.kissani.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import fi.neter.kissani.fb.FBCookie;
import fi.neter.kissani.general.Http;

public class FB {
	private static final Logger logger = LoggerFactory
			.getLogger(FB.class);
	
    private static final String UTF8 = "UTF-8";

    private String appId;
    private String secret;

    private final String redirectUri = "";

    private static final String ACCESS_TOKEN_SESSION_VAR = "access_token";
    private static final String EXPIRES_SESSION_VAR = "expires";
    
    public static String getApiToken(HttpServletRequest req) {
        return (String) req.getSession(true).getAttribute(ACCESS_TOKEN_SESSION_VAR);
    }

    @Required
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Required
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static void clearApiToken(HttpServletRequest request) {
        request.getSession(true).removeAttribute(ACCESS_TOKEN_SESSION_VAR);
        request.getSession(true).removeAttribute(EXPIRES_SESSION_VAR);
    }

    public FBCookie requireLogin(HttpServletRequest request) {
    	FBCookie fbSession = null;
    	Cookie[] cookies = request.getCookies();
    	
    	String fbSessionCookie = null;

    	if (cookies != null) {
    		for (Cookie cookie : cookies) {
            	logger.debug("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            	
        		if (cookie.getName().startsWith("fbsr_")) {
                	logger.debug("Found FB cookie: " + cookie.getName() + " = " + cookie.getValue());
        			fbSessionCookie = cookie.getValue();
        			break;
        		}
        	}
    	}
    	if (fbSessionCookie != null) {
        	try {
        		fbSession = FBCookie.decode(fbSessionCookie, secret);
        	} catch (Exception e) {
        		logger.debug("Problem decoding cookie: ", e);
        		fbSession = new FBCookie();
        	}
    	}

    	logger.debug("Accesstoken=" + fbSession.getAccessToken() + ", code=" + fbSession.getCode() +
    			", id=" + fbSession.getFbId());
    	
    	if (fbSession.getAccessToken() == null) {
    		// Perhaps we have a cached session?
    		fbSession.setAccessToken(getApiToken(request));
    	}
    	
    	if (fbSession.getAccessToken() == null) {
    		// Nope. So, let's see if we got the "code".
    		if (fbSession.getCode() != null) {
                String url;
				try {
					url = "https://graph.facebook.com/oauth/access_token?" + "client_id="
					    	+ URLEncoder.encode(appId, UTF8) + "&" + "redirect_uri="
					    	+ URLEncoder.encode(redirectUri, UTF8) + "&" + "client_secret="
					    	+ URLEncoder.encode(secret, UTF8) + "&" + "code=" + URLEncoder.encode(fbSession.getCode(), UTF8);

					String token = Http.fetch(url);
			    	String accessToken = token.substring(13).split("&")[0];
					logger.debug("Code url=" + url + ", token: " + token + ", accessToken: " + accessToken);
                    fbSession.setAccessToken(accessToken);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		// If no code either, we'll return an empty session.
    	}
    	
        return fbSession;
    	
    }

}
