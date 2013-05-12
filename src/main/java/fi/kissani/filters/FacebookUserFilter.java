package fi.kissani.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.kissani.fb.Album;
import fi.kissani.fb.AlbumsPage;
import fi.kissani.fb.Photo;
import fi.kissani.fb.PhotosPage;
import fi.kissani.fb.Profile;
import fi.kissani.fb.Tag;

/**
 * The Facebook User Filter ensures that a Facebook client that pertains to the
 * logged in user is available in the session object named
 * "facebook.user.client".
 * 
 * The session ID is stored as "facebook.user.session". It's important to get
 * the session ID only when the application actually needs it. The user has to
 * authorise to give the application a session key.
 * 
 * @author Dave
 */
public class FacebookUserFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(FacebookUserFilter.class);

    private static final String UTF8 = "UTF-8";

    private String api_key;
    private String secret;
    private String appId;

    private final String redirectUri = "http://apps.facebook.com/kissani/";

    //private final String redirectUri = "http://malaysia.homelinux.net/kissani/";

    public void init(FilterConfig filterConfig) throws ServletException {
        api_key = filterConfig.getInitParameter("facebook_api_key");
        appId = filterConfig.getInitParameter("facebook_app_id");
        secret = filterConfig.getInitParameter("facebook_secret");
        if ((api_key == null) || (secret == null)) {
            throw new ServletException("Cannot initialise Facebook User Filter because the "
                    + "facebook_api_key or facebook_secret context init "
                    + "params have not been set. Check that they're there " + "in your servlet context descriptor.");
        }
        else {
            logger.info("Using facebook API key: " + api_key);
        }
    }

    public void destroy() {
    }

    public String requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = null;
        String code = request.getParameter("code");
        if (code != null) {
            // First step completed. We have the Code.
            logger.warn("Code: " + code);
            accessToken =
                    fetch("https://graph.facebook.com/oauth/access_token?" + "client_id="
                            + URLEncoder.encode(api_key, UTF8) + "&" + "redirect_uri="
                            + URLEncoder.encode(redirectUri, UTF8) + "&" + "client_secret="
                            + URLEncoder.encode(secret, UTF8) + "&" + "code=" + URLEncoder.encode(code, UTF8));
        }

        if (accessToken == null) {
            //accessToken = (String) request.getSession().getAttribute("access_token");
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
            accessToken = keyValues.get("access_token");
            request.getSession(true).setAttribute("access_token", accessToken);
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

    private String fetch(String string) throws HttpException, IOException {
        logger.debug("Fetching to: " + string);
        HttpClient restClient = new HttpClient();
        GetMethod method = new GetMethod(string);
        int result = restClient.executeMethod(method);
        logger.debug("HTTP result: " + result + ", from URL: " + string);
        if (result == 200) {
            return method.getResponseBodyAsString();
        }
        return null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        try {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            logger.warn("Request from:" + req.getRemoteAddr() + ", request: " + request.getQueryString());

            String accessToken = requireLogin(request, response);

            if (accessToken == null) {
                return;
            }

            Profile profile = getProfile("me", accessToken);
            request.setAttribute("profile", profile);

            ArrayList<Album> albums = getAlbumsFor("me", accessToken);
            ArrayList<Photo> allPhotos = new ArrayList<Photo>();
            for (Album album : albums) {
                ArrayList<Photo> photos = getPhotosForTag("Nala", String.valueOf(album.getId()), accessToken);
                allPhotos.addAll(photos);
            }
            request.setAttribute("nala", allPhotos);

            logger.warn("accessToken: " + String.valueOf(accessToken));
            logger.warn("Response: " + allPhotos.toString());

            chain.doFilter(request, response);
        } finally {
        }
    }

    private Profile getProfile(String id, String accessToken) throws HttpException, UnsupportedEncodingException,
            IOException {
        String result =
                fetch("https://graph.facebook.com/" + id + "?access_token=" + URLEncoder.encode(accessToken, UTF8)
                        + "&fields=name,id");
        return parseProfile(result);
    }

    private Profile getProfile(long id, String accessToken) throws HttpException, UnsupportedEncodingException,
            IOException {
        String result =
                fetch("https://graph.facebook.com/" + id + "?access_token=" + URLEncoder.encode(accessToken, UTF8)
                        + "&fields=name,id");
        return parseProfile(result);
    }

    private String addAlbumsFrom(String url, ArrayList<Album> albums) throws HttpException, IOException {
        String result = fetch(url);

        JSONObject jsonObject = JSONObject.fromObject(result);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(AlbumsPage.class);
        HashMap<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("data", Album.class);
        jsonConfig.setClassMap(classMap);

        AlbumsPage albumsPage = (AlbumsPage) JSONSerializer.toJava(jsonObject, jsonConfig);
        albums.addAll(albumsPage.getData());
        logger.debug("Adding albums: " + albumsPage.toString() + ", from: " + url);
        if (albumsPage.getPaging() != null) {
            return albumsPage.getPaging().getNext();
        }
        return null;
    }

    private String addPhotosFrom(String url, ArrayList<Photo> photos) throws HttpException, IOException {
        String result = fetch(url);

        JSONObject jsonObject = JSONObject.fromObject(result);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(PhotosPage.class);

        PhotosPage photosPage = (PhotosPage) JSONSerializer.toJava(jsonObject, jsonConfig);
        photos.addAll(photosPage.getData());

        if (photosPage.getPaging() != null) {
            return photosPage.getPaging().getNext();
        }
        return null;
    }

    private ArrayList<Album> getAlbumsFor(String id, String accessToken) throws HttpException, IOException {
        String url =
                "https://graph.facebook.com/" + id + "/albums?fields=id&access_token="
                        + URLEncoder.encode(accessToken, UTF8);
        ArrayList<Album> albums = new ArrayList<Album>();
        String next = url;
        while ((next = addAlbumsFrom(next, albums)) != null) {
        }
        return albums;
    }

    private ArrayList<Photo> getPhotosFor(String userId, String accessToken) throws HttpException, IOException {
        String url =
                "https://graph.facebook.com/" + userId + "/photos?fields=id,tags,icon,link&access_token="
                        + URLEncoder.encode(accessToken, UTF8);
        ArrayList<Photo> photos = new ArrayList<Photo>();
        String next = url;
        while ((next = addPhotosFrom(next, photos)) != null) {
        }
        return photos;
    }

    private ArrayList<Photo> getPhotosForTag(String tag, String userId, String accessToken) throws HttpException,
            IOException {
        ArrayList<Photo> photos = getPhotosFor(userId, accessToken);
        ArrayList<Photo> filteredPhotos = new ArrayList<Photo>();
        for (Photo photo : photos) {
            boolean found = false;
            for (Tag tagInPhoto : photo.getTags().getData()) {
                if (tag.equalsIgnoreCase(tagInPhoto.getName())) {
                    found = true;
                }
            }
            if (found) {
                filteredPhotos.add(photo);
            }
        }
        return filteredPhotos;
    }

    public static Profile parseProfile(String profileStr) {
        JSONObject jsonObject = JSONObject.fromObject(profileStr);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(Profile.class);

        Profile profile = (Profile) JSONSerializer.toJava(jsonObject, jsonConfig);

        return profile;
    }
}
