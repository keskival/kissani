package fi.neter.kissani.fb;

import com.google.api.client.util.Key;

public class Tag {
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private Long createdTime;
    @Key
    private String text;
    @Key
    private Long imageId;

    public void setId(String id) {
       	this.id = id;
    }

    public void setId(Long id) {
        this.id = String.valueOf(id);
    }

    public Long getId() {
    	try {
    		return Long.valueOf(id);
        } catch (NumberFormatException e) {
        	return null;
   	    }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getImageId() {
        return imageId;
    }
}
