package fi.kissani.fb;

import java.util.ArrayList;

public class AlbumsPage {
    private Paging paging;
    private ArrayList<Album> data;

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setData(ArrayList<Album> data) {
        this.data = data;
    }

    public ArrayList<Album> getData() {
        return data;
    }
}
