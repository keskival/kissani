package fi.neter.kissani.fb;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.neter.kissani.dao.Cat;
import fi.neter.kissani.dao.PersonJdoDao;
import fi.neter.kissani.general.Http;
import fi.neter.kissani.shared.UIException;

@Service
public class FBService {
	private static final Logger logger = LoggerFactory
			.getLogger(FBService.class);

	private static final String UTF8 = "UTF-8";

	@Autowired
	private PersonJdoDao personDao;

	private static final Hashtable<String, Profile> profileCache = new Hashtable<String, Profile>();

	public Set<Photo> getPhotosOfCat(Cat cat, String accessToken)
			throws IOException {
		HashSet<Photo> allPhotos = new HashSet<Photo>();

		for (Long owner : cat.getOwnersAsSet()) {
			Set<Album> albums = getAlbumsFor(owner, accessToken);
			for (Album album : albums) {
				Set<Photo> photos = getPhotosForTag(cat.getNickName(),
						String.valueOf(album.getId()), accessToken);
				allPhotos.addAll(photos);
			}
		}

		return allPhotos;
	}

	public Profile getProfile(String id, String accessToken) {
		Profile profile;
		try {
			if (!id.equals("me")) {
				profile = profileCache.get(id);
				if (profile != null) {
					return profile;
				}
			}
			String url = "https://graph.facebook.com/" + id
					+ "?access_token=" + URLEncoder.encode(accessToken, UTF8)
					+ "&fields=name,id,link,friends";
        	logger.debug("GetProfile, URL: " + url);
			profile = Http.googleFetch(url, Profile.class);
			
			if (!id.equals("me")) {
				profileCache.put(id, profile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UIException("Problem getting profile.", e);
		}
		return profile;
	}

	public List<Friend> getFriends(String id, String accessToken) {
		Profile profile;
		try {
			String url = "https://graph.facebook.com/" + id
					+ "?access_token=" + URLEncoder.encode(accessToken, UTF8)
					+ "&fields=name,id,link,friends";
        	logger.debug("GetProfile, URL: " + url);
			profile = Http.googleFetch(url, Profile.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UIException("Problem getting profile.", e);
		}
		List<Friend> friendsInApp = new ArrayList<Friend>();

		for (Friend friend : profile.getFriends().getData()) {
			Long friendId = friend.getId();
			List<Long> cats = personDao.getCats(friendId);
			if (cats != null) {
				friendsInApp.add(friend);
			}
		}
		return friendsInApp;
	}
	
	private Set<Album> getAlbumsFor(long owner, String accessToken)
			throws IOException {
		return getAlbumsFor(String.valueOf(owner), accessToken);
	}

	private String addAlbumsFrom(String url, Set<Album> albums)
			throws IOException {
		AlbumsPage albumsPage = Http.googleFetch(url, AlbumsPage.class);

		albums.addAll(albumsPage.getData());
		if (albumsPage.getPaging() != null) {
			return albumsPage.getPaging().getNext();
		}
		return null;
	}

	private String addPhotosFrom(String url, Set<Photo> photos)
			throws IOException {
		PhotosPage photosPage = Http.googleFetch(url, PhotosPage.class);

		photos.addAll(photosPage.getData());

		if (photosPage.getPaging() != null) {
			return photosPage.getPaging().getNext();
		}
		return null;
	}

	private Set<Album> getAlbumsFor(String id, String accessToken)
			throws IOException {
		String url = "https://graph.facebook.com/" + id
				+ "/albums?fields=id&access_token="
				+ URLEncoder.encode(accessToken, UTF8);
		Set<Album> albums = new HashSet<Album>();
		String next = url;
		while ((next = addAlbumsFrom(next, albums)) != null) {
		}
		return albums;
	}

	private Set<Photo> getPhotosFor(String userId, String accessToken)
			throws IOException {
		String url = "https://graph.facebook.com/" + userId
				+ "/photos?fields=id,tags,icon,link&access_token="
				+ URLEncoder.encode(accessToken, UTF8);
		Set<Photo> photos = new HashSet<Photo>();
		String next = url;
		while ((next = addPhotosFrom(next, photos)) != null) {
		}
		return photos;
	}

	private Set<Photo> getPhotosForTag(String tag, String userId,
			String accessToken) throws IOException {
		Set<Photo> photos = getPhotosFor(userId, accessToken);
		Set<Photo> filteredPhotos = new HashSet<Photo>();
		for (Photo photo : photos) {
			boolean found = false;
			if (photo.getTags() != null) {
				for (Tag tagInPhoto : photo.getTags().getData()) {
					if (tag.equalsIgnoreCase(tagInPhoto.getName())) {
						found = true;
					}
				}
			}
			if (found) {
				filteredPhotos.add(photo);
			}
		}
		return filteredPhotos;
	}

	public void setPersonDao(PersonJdoDao personDao) {
		this.personDao = personDao;
	}
}
