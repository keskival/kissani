package fi.neter.kissani.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CatTO implements Serializable {
	@Override
	public String toString() {
		return "CatTO [id=" + id + ", name=" + name + ", nickName=" + nickName
				+ ", defaultPhoto=" + defaultPhoto + ", owners=" + owners
				+ ", photos=" + photos + ", friends=" + friends + "]";
	}

	private static final long serialVersionUID = 1L;

    private Long id;
    
    private String name;
    
    private String nickName;

    private String defaultPhoto;

    private Set<Long> owners;
    
    private Set<PhotoTO> photos;

    private Set<Long> friends;

    private List<CatTO> prefetchedFriends;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDefaultPhoto() {
		return defaultPhoto;
	}

	public void setDefaultPhoto(String defaultPhoto) {
		this.defaultPhoto = defaultPhoto;
	}

	public Set<Long> getOwners() {
		return owners;
	}

	public void setOwners(Set<Long> owners) {
		this.owners = owners;
	}

	public Set<PhotoTO> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<PhotoTO> photos) {
		this.photos = photos;
	}

	public Set<Long> getFriends() {
		return friends;
	}

	public void setFriends(Set<Long> friends) {
		this.friends = friends;
	}

	public List<CatTO> getPrefetchedFriends() {
		return prefetchedFriends;
	}

	public void setPrefetchedFriends(List<CatTO> prefetchedFriends) {
		this.prefetchedFriends = prefetchedFriends;
	}
}
