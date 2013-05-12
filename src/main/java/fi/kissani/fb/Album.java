package fi.kissani.fb;

public class Album {
    private Long id;
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
