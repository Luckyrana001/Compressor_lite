package id.zelory.compressor.sample.common;

import java.util.ArrayList;

/* renamed from: id.zelory.compressor.sample.common.BaseArrayList */
public class BaseArrayList<T> extends ArrayList<T> {
    public String tagValue;

    public String getTag() {
        return this.tagValue;
    }

    public void setTag(String tag) {
        this.tagValue = tag;
    }
}
