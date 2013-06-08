package fi.neter.kissani.fb;

import java.util.List;

import com.google.api.client.util.Key;

public class TagsForImage {
    @Key
    private List<Tag> data;

    public void setData(List<Tag> data) {
        this.data = data;
    }

    public List<Tag> getData() {
        return data;
    }
}
