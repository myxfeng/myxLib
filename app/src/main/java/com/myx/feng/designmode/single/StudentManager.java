package com.myx.feng.designmode.single;

/**
 * 静态懒加载模式
 * Created by mayuxin on 2018/2/26.
 */

public class StudentManager {
    private StudentManager(){

    }
    public static StudentManager getInstance(){
        return  StudentManagerHolder.INSTANCE;
    }

    private static class  StudentManagerHolder{
        private static final StudentManager INSTANCE=new StudentManager();
    }

    public int getNum(){
        return 1;
    }

}
