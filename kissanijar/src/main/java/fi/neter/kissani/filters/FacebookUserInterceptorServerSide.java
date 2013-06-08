package fi.neter.kissani.filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import fi.neter.kissani.general.Http;

public class FacebookUserInterceptorServerSide extends HandlerInterceptorAdapter {

    private static final String CODE_REQUEST_VAR = "code";

    private static final String ACCESS_TOKEN_SESSION_VAR = "access_token";
    private static final String EXPIRES_SESSION_VAR = "expires";

    private static final Logger logger = LoggerFactory.getLogger(FacebookUserInterceptorServerSide.class);

    private static final String UTF8 = "UTF-8";

    private String api_key;
    private String secret;

    // private final String redirectUri = "https://apps.facebook.com/kissani/showSelf.do";
    private final String redirectUri = "https://kissani4.appspot.com/showSelf.do";

    public static String getApiToken(HttpServletRequest req) {
        return (String) req.getSession(true).getAttribute(ACCESS_TOKEN_SESSION_VAR);
    }

    @Required
    public void setApiKey(String apiKey) {
        this.api_key = apiKey;
    }

    @Required
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = null;
        Long expires = null;
        logger.debug("Interceptor request: " + request.getParameterMap());
        String errorReason = request.getParameter("error_reason"); 
        if ((errorReason != null) && (errorReason.equalsIgnoreCase("user_denied"))) {
            logger.debug("Found error case: " + errorReason);
        	// The user has denied the application access permissions. Ask again.
            String url =
                    "https://apps.facebook.com/kissani/resources/errorUser.html";
            redirect(response, url);
            return null;
        }
        
        String[] codeAr = (String[]) (request.getParameterMap().get(CODE_REQUEST_VAR));
        if (codeAr != null) {
            String code = codeAr[0];
            // First step completed. We have the Code.
            String url = "https://graph.facebook.com/oauth/access_token?" + "client_id="
            	+ URLEncoder.encode(api_key, UTF8) + "&" + "redirect_uri="
            	+ URLEncoder.encode(redirectUri, UTF8) + "&" + "client_secret="
            	+ URLEncoder.encode(secret, UTF8) + "&" + "code=" + URLEncoder.encode(code, UTF8);
            accessToken =
                    Http.fetch(url);
        }

        if (accessToken == null) {
            accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN_SESSION_VAR);
            expires = (Long) request.getSession().getAttribute(EXPIRES_SESSION_VAR);
            if ((expires != null) && ((new Date()).getTime() >= 1000 * (expires - 600))) {
                expires = null;
                accessToken = null;
            }
            if (accessToken == null) {
                String url =
                        "https://www.facebook.com/dialog/oauth?client_id=" + URLEncoder.encode(api_key, UTF8)
                                + "&scope=user_photos,user_photo_video_tags,publish_stream,offline_access&redirect_uri="
                                + URLEncoder.encode(redirectUri, UTF8);
                redirect(response, url);
                return null;
            }
        }
        else {
            // access_token=174781145893917|2.ruWgp2OFDecNeSP_CL437g__.3600.1294527600-713645661|BNI9fqC_wtf_HLG4OsZaO61odrY&expires=4564
            String[] values = accessToken.split("&");
            HashMap<String, String> keyValues = new HashMap<String, String>();
            for (String value : values) {
                String keyValue[] = value.split("=");
                String key = keyValue[0].trim();
                String paramValue = keyValue[1].trim();
                if (paramValue.startsWith("\"")) {
                    paramValue = paramValue.substring(1, paramValue.length()-1);
                }
                keyValues.put(key, paramValue);
            }
            accessToken = keyValues.get(ACCESS_TOKEN_SESSION_VAR);
            request.getSession(true).setAttribute(ACCESS_TOKEN_SESSION_VAR, accessToken);
            try {
            	expires = (new Date()).getTime() + Long.valueOf(keyValues.get(EXPIRES_SESSION_VAR));
                request.getSession(true).setAttribute(EXPIRES_SESSION_VAR, expires);
            } catch (NumberFormatException e) {
            	expires = null;
            }
        }
        return accessToken;
    }
    
    public static void clearApiToken(HttpServletRequest request) {
        request.getSession(true).removeAttribute(ACCESS_TOKEN_SESSION_VAR);
        request.getSession(true).removeAttribute(EXPIRES_SESSION_VAR);
    }

    private void redirect(HttpServletResponse response, String string) throws IOException {
        logger.debug("Redirecting to: " + string);
        response.setContentType("text/html");
        response.getWriter().append("<html><head><script type=\"text/javascript\">" + "window.location = \"" + string
                                            + "\"" + "</script></head></html>");
        response.flushBuffer();
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        try {
            HttpServletRequest request = req;
            HttpServletResponse response = res;
            String pathInfo = request.getRequestURL().toString();

            if ((pathInfo != null) && ((pathInfo.endsWith(".task")) ||
                    (pathInfo.endsWith("signIn.do")))) {
                return true;
            }
            String accessToken = requireLogin(request, response);

            if (accessToken == null) {
                return false;
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

	public static String getId(HttpServletRequest req) {
		String apiToken = getApiToken(req);
		if (apiToken == null) {
			return "me";
		}
		String[] tokenParts = apiToken.split("|");

		if (tokenParts.length != 3) {
			return "me";
		}

		String[] idParts = tokenParts[1].split("-");
		
		return idParts[idParts.length-1];
	}
}
