package com.myx.feng.rxjavademo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by mayuxin on 2017/3/17.
 */

public class NewsResult extends BaseResult implements Parcelable ,Serializable{
    private static final long serialVersionUID = 5329812873261315687L;
    private NewsData data;
    private String tag="ok";
    public NewsData getData() {
        return data;
    }

    public void setData(NewsData data) {
        this.data = data;
    }

    public NewsResult() {

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    protected NewsResult(Parcel in) {
        this.data = in.readParcelable(NewsData.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }



    public static final Parcelable.Creator<NewsResult> CREATOR = new Parcelable.Creator<NewsResult>() {
        @Override
        public NewsResult createFromParcel(Parcel source) {
            return new NewsResult(source);
        }

        @Override
        public NewsResult[] newArray(int size) {
            return new NewsResult[size];
        }
    };
}
