package fi.kissani.fb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;

public class PhotosPage {
    private ArrayList<Photo> data;
    private Paging paging;

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setData(ArrayList<DynaBean> data) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        this.data = new ArrayList<Photo>();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(TagsForImage.class);
        for (DynaBean bean : data) {
            try {
                Photo photo = new Photo();
                // photo.setCreatedTime(new Date(PropertyUtils.getProperty(bean, "created_time").toString()).getTime());
                photo.setIcon(PropertyUtils.getProperty(bean, "icon").toString());
                photo.setId(Long.valueOf(PropertyUtils.getProperty(bean, "id").toString()));
                photo.setLink(PropertyUtils.getProperty(bean, "link").toString());

                DynaBean tags = (DynaBean) PropertyUtils.getProperty(bean, "tags");
                TagsForImage tagsObj = new TagsForImage();
                tagsObj.setData((ArrayList<DynaBean>) tags.get("data"));

                photo.setTags(tagsObj);

                this.data.add(photo);
            } catch (NoSuchMethodException e) {
                // Skipping if icon, id, link or tags were not found.
            }
        }
    }

    public ArrayList<Photo> getData() {
        return data;
    }
}
