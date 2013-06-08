package fi.kissani.fb;


public class Profile {
    private Long id;
    private String name;
    private String photo;
    private String link;
    private Friends friends;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "[" + this.getId() + ":" + this.getName() + "," + this.getLink() + "]";
    }
}
