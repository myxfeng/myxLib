package com.myx.feng.nativeweb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myx.feng.R;
import com.myx.library.util.Futils;
import com.myx.library.util.ToastUtils;

import org.xml.sax.XMLReader;

import java.util.Locale;


/**
 * Created by mayuxin on 2017/11/7.
 */

public class NativeWebView extends TextView {
    private Context mContext;
    private MImageGetter mImageGetter;
    private MTagHandler mTagHandler;

    public NativeWebView(Context context) {
        this(context, null);
    }

    public NativeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

//    public NativeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//    }

    public void init(Context context) {
        mContext = context;
        mImageGetter = new MImageGetter();
        mTagHandler = new MTagHandler(context);
        setMovementMethod(LinkMovementMethod.getInstance());

        paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
    }

    public void setHtml(String html) {
        setText(Html.fromHtml(html, mImageGetter, mTagHandler));
    }

    private int mLineY = 0;//总行高
    private int mViewWidth;//TextView的总宽度
    private TextPaint paint;


    /**
     * 重绘此行.
     *
     * @param canvas    画布
     * @param lineText  该行所有的文字
     * @param lineWidth 该行每个文字的宽度的总和
     */
    private void drawScaleText(Canvas canvas, String lineText, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineText)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, paint);
            float width = StaticLayout.getDesiredWidth(blanks, paint);
            x += width;
            lineText = lineText.substring(3);
        }
        //比如说一共有5个字，中间有4个间隔，
        //那就用整个TextView的宽度 - 5个字的宽度，
        //然后除以4，填补到这4个空隙中
        float interval = (mViewWidth - lineWidth) / (lineText.length() - 1);
        for (int i = 0; i < lineText.length(); i++) {
            String character = String.valueOf(lineText.charAt(i));
            float cw = StaticLayout.getDesiredWidth(character, paint);
            canvas.drawText(character, x, mLineY, paint);
            x += (cw + interval);
        }
    }


    /**
     * 判断是不是段落的第一行.
     * 一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param lineText 该行所有的文字
     */
    private boolean isFirstLineOfParagraph(String lineText) {
        return lineText.length() > 3 && lineText.charAt(0) == ' ' && lineText.charAt(1) == ' ';
    }

    /**
     * 判断需不需要缩放.
     *
     * @param lineText 该行所有的文字
     * @return true 该行最后一个字符不是换行符  false 该行最后一个字符是换行符
     */
    private boolean needScale(String lineText) {
        if (lineText.length() == 0) {
            return false;
        } else {
            return lineText.charAt(lineText.length() - 1) != '\n';
        }
    }


    class MImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(final String source) {
//            Log.e("Test", "source::" + source);
//            final URLDrawable urlDrawable = new NativeWebView.URLDrawable();
//            Glide.with(mContext).load(source).into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
//
//                    float scale = (float) drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth();
//                    drawable.setBounds(0, 0, getImageWidth(), (int) (getImageWidth() * scale));
//                    BitmapDrawable bd = (BitmapDrawable) drawable;
//                    urlDrawable.bitmap = bd.getBitmap();
//                    NativeWebView.this.invalidate();
//                    NativeWebView.this.setText(NativeWebView.this.getText());
//                }
//            });
//            float scale = 0.75f;
//            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
//            urlDrawable.bitmap = ((BitmapDrawable) drawable).getBitmap();
//            urlDrawable.setBounds(0, 0, getImageWidth(), (int) (getImageWidth() * scale));
//            return urlDrawable;
            return null;
        }
    }

    public class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(sss(bitmap, this.getBounds().right, this.getBounds().bottom), 0, 0, getPaint());
            }
        }
    }

    public Bitmap sss(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    class MTagHandler implements Html.TagHandler {

        private Context mContext;

        public MTagHandler(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();
                Log.e("Test", "image :" + imgURL);
                Log.e("Test", "XMLReader :" + xmlReader.toString());
                Log.e("Test", "Editable :" + output.toString());
                // 使图片可点击并监听点击事件
                output.setSpan(new MTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private class ClickableImage extends ClickableSpan {
            private String url;
            private Context context;

            public ClickableImage(Context context, String url) {
                this.context = context;
                this.url = url;
            }

            @Override
            public void onClick(View widget) {
                // 进行图片点击之后的处理
                ToastUtils.showShort(mContext, "url=" + url);
            }
        }
    }

    private int getImageWidth() {
        int w = 0;
        w = Futils.getScreenWidth(mContext) - this.getPaddingLeft() - this.getPaddingRight();
        return w;
    }

    private int getImageHeight() {
        return 0;
    }
}
