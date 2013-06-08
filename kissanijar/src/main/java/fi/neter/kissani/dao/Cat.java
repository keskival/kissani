package fi.neter.kissani.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Text;

import fi.neter.kissani.fb.Photo;
import fi.neter.kissani.shared.CatTO;
import fi.neter.kissani.shared.PhotoTO;

@PersistenceCapable
public class Cat implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    
    @Persistent
    private Text photos = new Text("");
    
    @Persistent
    private String name = "";
    
    @Persistent
    private String nickName = "";

    @Persistent
    private Text owners = new Text("");

    @Persistent
	private Text catFriends = new Text("");

    @Persistent
    private String defaultPhoto = "";

    public Cat() {}
    
    public Cat(CatTO cat) {
    	this.setDefaultPhoto(cat.getDefaultPhoto());
    	this.setFriendsAsSet(cat.getFriends());
    	this.setId(cat.getId());
    	this.setName(cat.getName());
    	this.setNickName(cat.getNickName());
    	this.setOwnersAsSet(cat.getOwners());
    	
    	Set<Photo> photos = new HashSet<Photo>();
    	for (PhotoTO photoTo : cat.getPhotos()) {
    		photos.add(new Photo(photoTo));
    	}
		this.setPhotosAsSet(photos);
	}

	public String getDefaultPhoto() {
    	return defaultPhoto;
    }
    
    public void setDefaultPhoto(String defaultPhoto) {
    	this.defaultPhoto = defaultPhoto;
    }
    
    public Text getCatFriends() {
		return catFriends;
	}

	public void setCatFriends(Text catFriends) {
		this.catFriends = catFriends;
	}

	public void setId(Long l) {
        this.id = l;
    }

    public Long getId() {
        return id;
    }

    @Transient
    public void setOwnersAsSet(Set<Long> owners) {
        int size = owners.size();
        StringBuilder sb = new StringBuilder();
        for (Long ownerId : owners) {
            sb.append(String.valueOf(ownerId));
            sb.append(",");
        }
        if (size > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        this.owners = new Text(sb.toString());
    }

    @Transient
    public Set<Long> getOwnersAsSet() {
    	String ownersStr = owners.getValue();
        if ((owners == null) || (ownersStr.trim().equals(""))) {
            return new HashSet<Long>();
        }
        String[] urlArray = ownersStr.split(",");
        Set<Long> owners = new HashSet<Long>();
        for (String owner : urlArray) {
            if (!owner.trim().equals("")) {
                owners.add(Long.valueOf(owner));
            }
        }
        return owners;
    }

    public void setOwners(Text owners) {
        this.owners = owners;
    }

    public Text getOwners() {
        return owners;
    }

    @Transient
    public void setPhotosAsSet(Set<Photo> photos) {
        int size = photos.size();
        StringBuilder sb = new StringBuilder();
        for (Photo photo : photos) {
            sb.append(photo.toString());
            sb.append(",");
        }
        if (size > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        this.photos = new Text(sb.toString());
    }

    // Returns the URL for the first photo for the cat, or a place holder
    @Transient
    public Photo getPhoto() {
    	Set<Photo> allPhotos = getPhotosAsSet();
    	String defaultPhoto = getDefaultPhoto();
    	if (defaultPhoto != null && !defaultPhoto.equals("")) {
    		for (Photo photo:allPhotos) {
    			if (photo.getId().equals(Long.valueOf(defaultPhoto))) {
    				return photo;
    			}
    		}
    	}
    	if (allPhotos.size() > 0) {
    		Photo photo = allPhotos.iterator().next();
    		return photo;
    	}
    	return null;
    }
    
    public void setPhotos(Text photos) {
        this.photos = photos;
    }

    public Text getPhotos() {
        return photos;
    }

    @Transient
    public Set<Photo> getPhotosAsSet() {
        // "http://gdsgsd|423523,http://gfsdgsdg|3453"
    	String photosStr = photos.getValue();
        if ((photos == null) || (photosStr.trim().equals(""))) {
            return new HashSet<Photo>();
        }
        String[] urlArray = photosStr.split(",");
        Set<Photo> photosSet = new HashSet<Photo>();
        for (String url : urlArray) {
            String[] photoInfo = url.split("\\|");
            Photo photo = new Photo();
            photo.setLink(photoInfo[0]);
            photo.setId(Long.valueOf(photoInfo[1]));
            photosSet.add(photo);
        }
        return photosSet;
    }

    @Transient
    public Set<PhotoTO> getPhotosAsToSet() {
    	Set<Photo> photos = getPhotosAsSet();
    	Set<PhotoTO> result = new HashSet<PhotoTO>();
    	
    	for (Photo photo : photos) {
    		result.add(photo.getPhotoTO());
    	}
        return result;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public String toString() {
    	return name + "|" + nickName + "|" + photos + "|" + owners;
    }

    @Transient
	public Set<Long> getFriendsAsSet() {
    	String friendsStr = catFriends.getValue();
        if ((catFriends == null) || (friendsStr.trim().equals(""))) {
            return new HashSet<Long>();
        }
        String[] catFriendsArray = friendsStr.split(",");
        Set<Long> catFriendsSet = new HashSet<Long>();
        for (String catIdStr : catFriendsArray) {
        	Long catId = Long.valueOf(catIdStr);
            catFriendsSet.add(catId);
        }
        return catFriendsSet;
	}

    @Transient
	public void setFriendsAsSet(Set<Long> catFriends) {
        int size = catFriends.size();
        StringBuilder sb = new StringBuilder();
        for (Long friendId : catFriends) {
            sb.append(friendId.toString());
            sb.append(",");
        }
        if (size > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
		this.catFriends = new Text(sb.toString());
	}
    
    @Override
    public boolean equals(Object rhs) {
    	if (rhs == this) {
    		return true;
    	}
    	if (rhs instanceof Cat) {
    		Cat rhsCat = (Cat)rhs;
    		if (rhsCat.getId() == getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    @Override
    public int hashCode() {
    	if (getId() == null) {
    		return 0;
    	}
    	return getId().intValue();
    }

	public String getDefaultPhotoUrl() {
		return "https://www.facebook.com/photo.php?fbid=" + defaultPhoto;
	}

	@Transient
	public CatTO getCatTO() {
		CatTO catTo = new CatTO();
		catTo.setDefaultPhoto(defaultPhoto);
		catTo.setFriends(getFriendsAsSet());
		catTo.setId(id);
		catTo.setName(name);
		catTo.setNickName(nickName);
		catTo.setOwners(getOwnersAsSet());
		catTo.setPhotos(getPhotosAsToSet());
		return catTo;
	}
}
