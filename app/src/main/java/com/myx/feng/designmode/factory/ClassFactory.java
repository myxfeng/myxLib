package com.myx.feng.designmode.factory;

/**
 * Created by mayuxin on 2018/2/26.
 */

public class ClassFactory {
    public ClassInterface produce(int type) {
        ClassInterface classInterface = null;
        if (1 == type) {
            classInterface = new ClassStudent();
        } else {
            classInterface = new ClassBook();
        }

        return classInterface;
    }


    public ClassInterface produceStudent(){
        return new ClassStudent();
    }
    public ClassInterface produceBook(){
        return new ClassBook();
    }
}
