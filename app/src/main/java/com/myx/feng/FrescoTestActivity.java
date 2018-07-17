package com.myx.feng;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myx.library.image.IDownloadResult;
import com.myx.library.image.IResult;
import com.myx.library.image.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class FrescoTestActivity extends AppCompatActivity {
    @BindView(R.id.gifImage)
    SimpleDraweeView mGifImage;
    @BindView(R.id.commonImage)
    SimpleDraweeView mCommonImg;
    @BindView(R.id.bitmapImage)
    ImageView mBitmapImage;

    @BindView(R.id.fileImage)
    SimpleDraweeView mFileImage;


    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523422581326&di=6d5555cbd22c46c026ee09265ee5430a&imgtype=0&src=http%3A%2F%2Fpic32.photophoto.cn%2F20140704%2F0035035526987159_b.jpg";
    String gifUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523422604354&di=efa29a5f21ee91e31b6f3ed87698f4dd&imgtype=0&src=http%3A%2F%2Fupload.chinaz.com%2F2016%2F0607%2Fchinaz_60224ca8642fab7a2c8162192eaeeea7.gif";
    String bitmaoUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523423432335&di=e1c79e21bb740e4fa19eff1345d68dbc&imgtype=0&src=http%3A%2F%2Fjrsh.hangzhou.com.cn%2Flive%2Fcontent%2Fattachement%2Fjpg%2Fsite2%2F20160627%2F90b11c6dae5a18dac0e500.jpg";
    String bitgigurl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523423560477&di=c0635c5aafa45e7939a37d9ffa698cfe&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F012e1b569db11032f875520f147d6f.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_test);
        ButterKnife.bind(this);


    }

    public void load(View view) {
        ImageUtils.loadImageOnlyWifi(url, mCommonImg, false, 0);
        ImageUtils.loadImageOnlyWifi(gifUrl, mGifImage, false, 0);
        ImageUtils.loadImage(this, bitmaoUrl, -1, -1, new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap result) {
                mBitmapImage.setImageBitmap(result);
            }
        });
        String filePath = this.getFilesDir() + "/sss.dat";
        ImageUtils.downloadImage(this, bitgigurl, new IDownloadResult(filePath) {
            @Override
            public void onResult(final String filePath) {
                ImageUtils.loadFile(filePath, mFileImage, true, -1, -1);
            }
        });
    }
}
