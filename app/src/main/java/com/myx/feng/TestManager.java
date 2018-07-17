//package com.myx.feng;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//
//import junit.framework.Test;
//
///**
// * Created by mayuxin on 2018/2/7.
// */
//
//public class TestManager {
//    private static volatile TestManager manager;
//
//    public static TestManager getInatance() {
//        if (manager == null) {
//            synchronized (TestManager.class) {
//                if (manager == null) {
//                    manager = new TestManager();
//                }
//            }
//        }
//        return manager;
//    }
//
//
//    private static TestManager manager2;
//
//    public static TestManager getInstance2(){
//        return SingleHolder.instance;
//    }
//    private static final class SingleHolder{
//        private static final TestManager instance=new TestManager();
//    }
//
//
//    class testview extends View{
//
//        public testview(Context context) {
//            super(context);
//        }
//
//        public testview(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public testview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//
//
//            Paint paint=new Paint();
//            paint.setColor(Color.BLUE);
//            paint.setStyle(Paint.Style.FILL_AND_STROKE);
//
//            Path path=new Path();
//
//            canvas.tra
//
//            canvas.drawPath(path,paint);
//        }
//
//    }
//}
