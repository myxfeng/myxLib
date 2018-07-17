package com.myx.feng.designmode;

/**
 * Created by mayuxin on 2018/2/26.
 */

public class Testss {
    private static final Testss ourInstance = new Testss();

    public static Testss getInstance() {
        return ourInstance;
    }

    private Testss() {
    }
}
