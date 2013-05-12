package fi.kissani.fb;

import java.util.ArrayList;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.json.util.JSONUtils;

import org.apache.commons.beanutils.DynaBean;

public class TagsForImage {
    private ArrayList<Tag> data;

    public void setData(ArrayList<DynaBean> data) {
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        BeanMorpher morpher = new BeanMorpher(Tag.class, morpherRegistry);
        morpherRegistry.registerMorpher(morpher);
        this.data = new ArrayList<Tag>();

        for (DynaBean bean : data) {
            this.data.add((Tag) morpherRegistry.morph(Tag.class, bean));
        }
    }

    public ArrayList<Tag> getData() {
        return data;
    }
}
