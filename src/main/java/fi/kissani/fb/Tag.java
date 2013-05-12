package fi.kissani.fb;

public class Tag {
    private Long id;
    private String name;
    private Integer x;
    private Integer y;
    private Long createdTime;
    private String text;
    private Long imageId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
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
