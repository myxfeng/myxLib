package com.myx.feng;

import com.myx.feng.rxjavademo.NewsData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayuxin on 2017/3/24.
 */

public class CollectResult implements Serializable{
    private static final long serialVersionUID = -6431337010254835338L;
    private List<NewsData> data;

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
}
