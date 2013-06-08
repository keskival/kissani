package fi.kissani.dao;

import java.util.ArrayList;
import java.util.List;

import fi.kissani.fb.Photo;

public class Cat {
    private Long id;
    private List<Photo> photos = new ArrayList<Photo>();
    private String name;
    private String nickName;
    private List<Long> owners = new ArrayList<Long>();

    public void setId(Long l) {
        this.id = l;
    }

    public Long getId() {
        return id;
    }

    public void setOwners(List<Long> owners) {
        this.owners = owners;
    }

    public List<Long> getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        if ((owners == null) || (owners.trim().equals(""))) {
            this.owners = new ArrayList<Long>();
            return;
        }
        String[] urlArray = owners.split(",");
        this.owners = new ArrayList<Long>();
        for (String owner : urlArray) {
            this.owners.add(Long.valueOf(owner));
        }
    }

    public String getOwnersAsString() {
        int size = owners.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            sb.append(String.valueOf(this.owners.get(i)));
            sb.append(",");
        }
        if (size > 0) {
            sb.append(String.valueOf(this.owners.get(size - 1)));
        }
        return sb.toString();
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void setPhotos(String urlList) {
        // "http://gdsgsd|423523,http://gfsdgsdg|3453"
        if ((urlList == null) || (urlList.trim().equals(""))) {
            this.photos = new ArrayList<Photo>();
            return;
        }
        String[] urlArray = urlList.split(",");
        this.photos = new ArrayList<Photo>();
        for (String url : urlArray) {
            String[] photoInfo = url.split("\\|");
            Photo photo = new Photo();
            photo.setLink(photoInfo[0]);
            photo.setId(Long.valueOf(photoInfo[1]));
            this.photos.add(photo);
        }
    }

    public String getUrlsForPicsAsString() {
        int size = photos.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            sb.append(this.photos.get(i).toString());
            sb.append(",");
        }
        if (size > 0) {
            sb.append(this.photos.get(size - 1));
        }
        return sb.toString();
    }

    public List<Photo> getPhotos() {
        return photos;
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

}
