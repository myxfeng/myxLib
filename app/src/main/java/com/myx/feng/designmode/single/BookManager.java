package com.myx.feng.designmode.single;

/**
 *
 * 懒汉模式
 * Created by mayuxin on 2018/2/26.
 */

public class BookManager {
    private  volatile static BookManager instance;

    private BookManager(){

    }
    public static BookManager getInstance(){
        if(instance==null){//  第一次check 减少不必要的同步
            synchronized (BookManager.class){
                if(instance==null){// 保证线程安全
                    instance=new BookManager();
                }
            }
        }

        return  instance;
    }

    public int getNum(){
        return 2;
    }
}
