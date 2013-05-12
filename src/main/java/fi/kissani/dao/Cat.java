package fi.kissani.dao;

import java.util.ArrayList;

public class Cat {
    private String id;
    private ArrayList<String> urlsForPics;
    private String name;
    private String nickName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrlsForPics(ArrayList<String> urlsForPics) {
        this.urlsForPics = urlsForPics;
    }

    public ArrayList<String> getUrlsForPics() {
        return urlsForPics;
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
