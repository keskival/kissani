package fi.neter.kissani.fb;

import java.util.List;

import com.google.api.client.util.Key;

public class PhotosPage {
    @Key
    private List<Photo> data;

    @Key
    private Paging paging;

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setData(List<Photo> data) {
        this.data = data;
    }

    public List<Photo> getData() {
        return data;
    }
}
