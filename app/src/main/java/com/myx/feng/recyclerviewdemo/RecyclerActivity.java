package com.myx.feng.recyclerviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.myx.feng.R;
import com.myx.feng.recyclerviewdemo.dummy.DummyContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class RecyclerActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    @BindView(R.id.content_recycler)
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.content_recycler, ItemFragment.newInstance(1)).commit();
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.Adapter adapter, DummyContent.DummyItem item) {
        MyItemRecyclerViewAdapter temp = (MyItemRecyclerViewAdapter) adapter;
        List<DummyContent.DummyItem> mDatas = temp.getmValues();


        List<DummyContent.DummyItem> newDatas = deepCopy(mDatas);
        newDatas.remove(item);

        DummyContent.DummyItem test = newDatas.get(0);
        test.setContent(test.getContent() + "test");

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, newDatas), true);
        temp.setmValues(newDatas);
        diffResult.dispatchUpdatesTo(temp);
//        temp.notifyDataSetChanged();
    }

    public List deepCopy(List src) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(src);

            bais = new ByteArrayInputStream(baos.toByteArray());
            ois = new ObjectInputStream(bais);
            return (List) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;

    }

    /**
     * 介绍：核心类 用来判断 新旧Item是否相等
     * 作者：iaiai
     * 邮箱：176291935@qq.com
     * 时间： 2016/9/12.
     */

    public static class DiffCallBack extends DiffUtil.Callback {
        private List<DummyContent.DummyItem> mOldDatas, mNewDatas;//看名字

        public DiffCallBack(List<DummyContent.DummyItem> mOldDatas, List<DummyContent.DummyItem> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        //老数据集size
        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        //新数据集size
        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
         * For example, if your items have unique ids, this method should check their id equality.
         * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
         * 本例判断name字段是否一致
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldDatas.get(oldItemPosition).getId().equals(mNewDatas.get(newItemPosition).getId());
        }

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
         * DiffUtil uses this information to detect if the contents of an item has changed.
         * DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
         * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
         * DiffUtil 用这个方法替代equals方法去检查是否相等。
         * so that you can change its behavior depending on your UI.
         * 所以你可以根据你的UI去改变它的返回值
         * For example, if you are using DiffUtil with a
         * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter}, you should
         * return whether the items' visual representations are the same.
         * 例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
         * This method is called only if {@link #areItemsTheSame(int, int)} returns
         * {@code true} for these items.
         * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list which replaces the
         *                        oldItem
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            DummyContent.DummyItem beanOld = mOldDatas.get(oldItemPosition);
            DummyContent.DummyItem beanNew = mNewDatas.get(newItemPosition);
            if (oldItemPosition == 0 && newItemPosition == 0) {
                Log.e("Dif", "old=" + beanOld.getContent() + "=====news=" + beanNew.getContent());
            }
            if (!beanOld.getContent().equals(beanNew.getContent())) {
                return false;//如果有内容不同，就返回false
            }
            if (!beanOld.getDetails().equals(beanNew.getDetails())) {
                return false;
            }
            return true; //默认两个data内容是相同的
        }
    }
}
