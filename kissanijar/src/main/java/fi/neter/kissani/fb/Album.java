package fi.neter.kissani.fb;

import com.google.api.client.util.Key;

public class Album {
    @Key
    private Long id;
    
    @Key
    private Long createdTime;

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
}
