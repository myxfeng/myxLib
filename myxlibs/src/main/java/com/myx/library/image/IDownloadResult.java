package com.myx.library.image;

import android.content.Context;

/**
 * Created by mayuxin on 2018/3/21.
 */

public abstract  class IDownloadResult implements IResult<String > {
    private String mFilePath;

    public IDownloadResult(String filePath) {
        this.mFilePath = filePath;
    }



    public String getFilePath() {
        return mFilePath;
    }

    public void onProgress(int progress) {

    }

    @Override
    public abstract void onResult(String filePath);
}
