package fi.neter.kissani.fb;

import javax.persistence.Transient;

import com.google.api.client.util.Key;

import fi.neter.kissani.shared.PhotoTO;

public class Photo {
    @Key
    private TagsForImage tags;

    @Key
    private Long id;

    @Key
    private Long createdTime;

    @Key
    private String icon;

    @Key
    private String link;

    public Photo() {}
    
    public Photo(PhotoTO photoTo) {
    	this.setIcon(photoTo.getIcon());
    	this.setId(photoTo.getId());
    	this.setLink(photoTo.getLink());
	}

	public void setTags(TagsForImage tags) {
        this.tags = tags;
    }

    public TagsForImage getTags() {
        return tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return this.getLink() + "|" + this.getId();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean equals(Photo rhs) {
        return this.id == rhs.getId();
    }
    
    @Transient
    public PhotoTO getPhotoTO() {
    	PhotoTO to = new PhotoTO();
    	
    	to.setIcon(icon);
    	to.setId(id);
    	to.setLink(link);
    	
    	return to;
    }
}
