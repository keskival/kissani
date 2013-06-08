package fi.kissani.fb;

import java.util.ArrayList;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.json.util.JSONUtils;

import org.apache.commons.beanutils.DynaBean;

public class Friends {
    private ArrayList<Friend> data;

    public void setData(ArrayList<DynaBean> data) {
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        BeanMorpher morpher = new BeanMorpher(Friend.class, morpherRegistry);
        morpherRegistry.registerMorpher(morpher);
        this.data = new ArrayList<Friend>();

        for (DynaBean bean : data) {
            this.data.add((Friend) morpherRegistry.morph(Friend.class, bean));
        }
    }

    public ArrayList<Friend> getData() {
        return data;
    }
}
