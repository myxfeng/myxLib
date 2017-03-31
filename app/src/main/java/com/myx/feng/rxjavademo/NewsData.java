package com.myx.feng.rxjavademo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class NewsData implements Parcelable ,Serializable{
    private static final long serialVersionUID = -7431293620499077005L;
    //cover=http://rmrbimg2.people.cn/thumbs1/1020/0/data/rmtyimg//userfiles/1/images/cms/article/2017/03/1489638277866.jpg__.webp
    private String cover;
    private String news_title;

    public NewsData() {
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cover);
        dest.writeString(this.news_title);
    }

    protected NewsData(Parcel in) {
        this.cover = in.readString();
        this.news_title = in.readString();
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel source) {
            return new NewsData(source);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };
}
