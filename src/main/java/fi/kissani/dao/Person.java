package fi.kissani.dao;

import java.util.ArrayList;

public class Person {
    private Long id;
    private ArrayList<Long> cats;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCats(ArrayList<Long> cats) {
        this.cats = cats;
    }

    public void setCats(String cats) {
        String[] catArray = cats.split(",");
        this.cats = new ArrayList<Long>();
        for (String owner : catArray) {
            this.cats.add(Long.valueOf(owner));
        }
    }

    public ArrayList<Long> getCats() {
        return cats;
    }
}
