package com.myx.library.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by mayuxin on 2017/3/17.
 */

public interface OnHttpListener {
    void onFailure(Call call, IOException e);

    void onSuccess(Call call, Response response, String json) throws IOException;
}
