package com.myx.feng.designmode;

import com.myx.feng.designmode.Build.ManBuilder;
import com.myx.feng.designmode.Build.PersonDirector;
import com.myx.feng.designmode.factory.ClassBookFatory;
import com.myx.feng.designmode.factory.ClassFactory;
import com.myx.feng.designmode.factory.ClassFactoryInterface;
import com.myx.feng.designmode.factory.ClassInterface;
import com.myx.feng.designmode.single.BookManager;
import com.myx.feng.designmode.single.StudentManager;

/**
 * Created by mayuxin on 2018/2/26.
 */

public class Test {

    void test(){
        //单例模式
        BookManager.getInstance().getNum();
        StudentManager.getInstance().getNum();

        //工厂模式
        ClassInterface classInterface= new ClassFactory().produce(1);
        classInterface.getNum();

        ClassFactoryInterface factoryInterface=new ClassBookFatory();
        factoryInterface.produce().getNum();


        // 建造者模式
        PersonDirector director=new PersonDirector();
        director.constructPerson(new ManBuilder());
    }
}
