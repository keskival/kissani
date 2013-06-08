package fi.neter.kissani.fb;

import java.util.List;

import com.google.api.client.util.Key;

public class AlbumsPage {
    @Key
    private Paging paging;
    
    @Key
    private List<Album> data;

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setData(List<Album> data) {
        this.data = data;
    }

    public List<Album> getData() {
        return data;
    }
}
