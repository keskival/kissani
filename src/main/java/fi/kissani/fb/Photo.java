package fi.kissani.fb;

public class Photo {
    private TagsForImage tags;
    private Long id;
    private Long createdTime;
    private String icon;
    private String link;

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
        return "[icon: " + this.icon + ", link: " + this.link + ", createdTime: " + this.createdTime + ", id: "
                + this.id + ", tags: " + this.tags.toString();
    }
}
