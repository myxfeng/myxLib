package com.myx.feng.cycleviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myx.feng.R;
import com.myx.feng.rxjavademo.NewsData;
import com.myx.library.util.ToastUtils;
import com.myx.library.widget.CycleViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.cycler)
    CustomCycler customCycler;
    String url0 = "http://img.fjdaily.com/thumbs/306/230/data//userfiles/32/images/cms/article/2017/04/1491433682624.jpg__.webp";
    String url1 = "http://img.fjdaily.com/thumbs/306/230/data//userfiles/32/images/cms/article/2017/04/1491462350738.jpg__.webp";
    String url2 = "http://img.fjdaily.com/thumbs/1080/607.5/data//userfiles/16/images/cms/article/2017/04/1491387329838.jpg__.webp";
    private List<NewsData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);

        NewsData newsData = new NewsData();
        newsData.setCover(url0);
        newsData.setNews_title("000000000");
        list.add(newsData);
        NewsData newsData1 = new NewsData();
        newsData1.setCover(url1);
        newsData1.setNews_title("1111111111");
        list.add(newsData1);
        NewsData newsData2 = new NewsData();
        newsData2.setCover(url2);
        newsData2.setNews_title("222222222222");
        list.add(newsData2);
        customCycler.setWheel(false);
        customCycler.setData(true,list, new CycleViewPager.ImageCycleViewListener() {
            @Override
            public void onImageClick(Object info, int position, View view) {
                ToastUtils.showShort(CyclerViewActivity.this, info.toString());
            }
        });
    }


}
