package fi.kissani.filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import fi.kissani.fb.FBService;
import fi.kissani.general.Http;

public class FacebookUserInterceptor extends HandlerInterceptorAdapter {

    private static final String CODE_REQUEST_VAR = "code";

    private static final String ACCESS_TOKEN_SESSION_VAR = "access_token";
    private static final String EXPIRES_SESSION_VAR = "expires";

    private static final Logger logger = LoggerFactory.getLogger(FacebookUserInterceptor.class);

    private static final String UTF8 = "UTF-8";

    private String api_key;
    private String secret;
    private String appId;

    private final String redirectUri = "http://apps.facebook.com/kissani/";

    @Autowired
    private FBService fbService;

    public static String getApiToken(HttpServletRequest req) {
        return (String) req.getSession(true).getAttribute(ACCESS_TOKEN_SESSION_VAR);

    }

    @Required
    public void setApiKey(String apiKey) {
        this.api_key = apiKey;
    }

    @Required
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Required
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = null;
        Long expires = null;
        String code = request.getParameter(CODE_REQUEST_VAR);
        if (code != null) {
            // First step completed. We have the Code.
            logger.warn("Code: " + code);
            accessToken =
                    Http.fetch("https://graph.facebook.com/oauth/access_token?" + "client_id="
                            + URLEncoder.encode(api_key, UTF8) + "&" + "redirect_uri="
                            + URLEncoder.encode(redirectUri, UTF8) + "&" + "client_secret="
                            + URLEncoder.encode(secret, UTF8) + "&" + "code=" + URLEncoder.encode(code, UTF8));
        }

        if (accessToken == null) {
            accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN_SESSION_VAR);
            expires = (Long) request.getSession().getAttribute(EXPIRES_SESSION_VAR);
            if ((expires != null) && ((new Date()).getTime() >= 1000 * (expires - 600))) {
                expires = null;
                accessToken = null;
            }
            if (accessToken == null) {
                //String url =
                //        "http://www.facebook.com/dialog/oauth/?"
                //                + "scope=user_photos,user_photo_video_tags,publish_stream&" + "client_id="
                //                + URLEncoder.encode(api_key, UTF8) + "&" + "redirect_uri="
                //                + URLEncoder.encode(redirectUri, UTF8) + "&" + "response_type=token";

                String url =
                        "https://graph.facebook.com/oauth/authorize?client_id=" + URLEncoder.encode(api_key, UTF8)
                                + "&scope=user_photos,user_photo_video_tags,publish_stream&redirect_uri="
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
                keyValues.put(keyValue[0], keyValue[1]);
            }
            accessToken = keyValues.get(ACCESS_TOKEN_SESSION_VAR);
            expires = (new Date()).getTime() + Long.valueOf(keyValues.get(EXPIRES_SESSION_VAR));
            request.getSession(true).setAttribute(ACCESS_TOKEN_SESSION_VAR, accessToken);
            request.getSession(true).setAttribute(EXPIRES_SESSION_VAR, expires);
        }
        logger.warn("AccessToken: " + accessToken);

        return accessToken;
    }

    private void redirect(HttpServletResponse response, String string) throws IOException {
        logger.debug("Redirect to: " + string);
        //response.sendRedirect(string);
        response.setContentType("text/html");
        response.getWriter().append("<html><head><script type=\"text/javascript\">" + "window.location = \"" + string
                                            + "\"" + "</script></head></html>");
        /*response.getWriter().append("<html><head><script type=\"text/javascript\">" + "window.open(\"" + string
                                            + "\", \"Authorize\");" + "</script></head></html>");*/
        response.flushBuffer();
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        try {

            HttpServletRequest request = req;
            HttpServletResponse response = res;

            logger.warn("Request from:" + req.getRemoteAddr() + ", request: " + request.getQueryString());

            String accessToken = requireLogin(request, response);

            if (accessToken == null) {
                return false;
            }

            logger.warn("accessToken: " + String.valueOf(accessToken));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setFbService(FBService fbService) {
        this.fbService = fbService;
    }
}
