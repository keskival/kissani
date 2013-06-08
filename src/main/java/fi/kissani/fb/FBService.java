package fi.kissani.fb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.kissani.dao.Cat;
import fi.kissani.general.Http;

@Service
public class FBService {
    private static final Logger logger = LoggerFactory.getLogger(FBService.class);

    private static final String UTF8 = "UTF-8";

    public static Profile parseProfile(String profileStr) {
        JSONObject jsonObject = JSONObject.fromObject(profileStr);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(Profile.class);

        Profile profile = (Profile) JSONSerializer.toJava(jsonObject, jsonConfig);

        return profile;
    }

    public ArrayList<Photo> getPhotosOfCat(Cat cat, String accessToken) throws HttpException, IOException {
        ArrayList<Photo> allPhotos = new ArrayList<Photo>();

        logger.warn("Going through owners: " + cat.getOwners().toString());
        for (Long owner : cat.getOwners()) {
            ArrayList<Album> albums = getAlbumsFor(owner, accessToken);
            logger.warn("Going through albums: " + albums.toString());
            for (Album album : albums) {
                ArrayList<Photo> photos =
                        getPhotosForTag(cat.getNickName(), String.valueOf(album.getId()), accessToken);
                allPhotos.addAll(photos);
            }
        }

        return allPhotos;
    }

    public Profile getProfile(String id, String accessToken) throws HttpException, UnsupportedEncodingException,
            IOException {
        String result =
                Http.fetch("https://graph.facebook.com/" + id + "?access_token=" + URLEncoder.encode(accessToken, UTF8)
                        + "&fields=name,id,link,friends");
        return parseProfile(result);
    }

    public Profile getProfile(long id, String accessToken) throws HttpException, UnsupportedEncodingException,
            IOException {
        return getProfile(String.valueOf(id), accessToken);
    }

    private ArrayList<Album> getAlbumsFor(long owner, String accessToken) throws HttpException, IOException {
        return getAlbumsFor(String.valueOf(owner), accessToken);
    }

    private String addAlbumsFrom(String url, ArrayList<Album> albums) throws HttpException, IOException {
        String result = Http.fetch(url);

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
        String result = Http.fetch(url);

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

}
