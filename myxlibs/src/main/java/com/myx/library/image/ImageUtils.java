package com.myx.library.image;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.myx.library.util.CheckUtils;
import com.myx.library.util.Futils;
import com.myx.library.util.NetworkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lib.lhh.fiv.library.FrescoImageView;
import lib.lhh.fiv.library.FrescoZoomImageView;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class ImageUtils {
    public static GenericDraweeHierarchyBuilder builder;
    public static GenericDraweeHierarchy hierarchy;
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    public static final long MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;//使用的缓存数量
    public static final long MAX_LOW_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 16;//使用的缓存数量
    public static final long MAX_VERY_LOW_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 32;//使用的缓存数量

    public static final int FADE_TIME = 120;
    public static Application mContext;

    /**
     * @param context
     */
    public static void initialize(Application context) {
        mContext = context;
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName("v1")
                .setMaxCacheSize(MAX_MEMORY_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(MAX_LOW_MEMORY_CACHE_SIZE)
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_VERY_LOW_MEMORY_CACHE_SIZE)
                .setVersion(1)
                .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context).setMainDiskCacheConfig(diskCacheConfig).
                build();

        Fresco.initialize(context, imagePipelineConfig);
        builder = new GenericDraweeHierarchyBuilder(context.getResources());
        hierarchy = builder.setFadeDuration(FADE_TIME).build();
    }

    /**
     * @param loadUri
     * @return
     */
    private static boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey);
    }

    /**
     * @param path
     * @param imageView
     * @param isOnlyWifi
     * @param defaultDrawable
     */
    public static void loadBitmapOnlyWifi(String path, ImageView imageView, boolean isOnlyWifi, int defaultDrawable) {
        try {
            if (path == null) {
                path = "";
            }
            if (CheckUtils.isNoEmptyStr(path) && !path.startsWith("http://") && !path.startsWith("file://") && !path.startsWith("res://")) {
                path = "file://" + path;
            }
            if (imageView instanceof FrescoImageView) {// FrescoImageView
                if (isOnlyWifi) {
                    if (NetworkUtils.isNetworkAvailable(mContext)) {
                        ((FrescoImageView) imageView).loadView(path, defaultDrawable);
                    } else {
                        boolean isInCache = isImageDownloaded(Uri.parse(path));
                        if (isInCache) {
                            ((FrescoImageView) imageView).loadView(path, defaultDrawable);
                        } else {
                            ((FrescoImageView) imageView).loadView("", defaultDrawable);
                        }
                    }
                } else {
                    ((FrescoImageView) imageView).loadView(path, defaultDrawable);
                }
            } else if (imageView instanceof SimpleDraweeView) {//SimpleDraweeView
                if (((SimpleDraweeView) imageView).getHierarchy() == null) {
                    if (defaultDrawable != 0) {
                        hierarchy.setPlaceholderImage(defaultDrawable);
                        ((SimpleDraweeView) imageView).setHierarchy(hierarchy);
                    }
                } else {
                    if (defaultDrawable != 0) {
                        ((SimpleDraweeView) imageView).getHierarchy().setPlaceholderImage(defaultDrawable);
                    }
                }

                if (isOnlyWifi) {
                    if (NetworkUtils.isNetworkAvailable(mContext)) {
                        imageView.setImageURI(Uri.parse(path));
                    } else {
                        boolean isInCache = isImageDownloaded(Uri.parse(path));
                        if (isInCache) {
                            imageView.setImageURI(Uri.parse(path));
                        } else {
                            imageView.setImageURI(Uri.parse(""));
                        }
                    }
                } else {
                    imageView.setImageURI(Uri.parse(path));
                }
            } else if (imageView instanceof FrescoZoomImageView) {// FrescoZoomImageView
                if (isOnlyWifi) {
                    if (NetworkUtils.isNetworkAvailable(mContext)) {
                        ((FrescoZoomImageView) imageView).loadView(path, defaultDrawable);
                    } else {
                        boolean isInCache = isImageDownloaded(Uri.parse(path));
                        if (isInCache) {
                            ((FrescoZoomImageView) imageView).loadView(path, defaultDrawable);
                        } else {
                            ((FrescoZoomImageView) imageView).loadView("", defaultDrawable);
                        }
                    }
                } else {
                    ((FrescoZoomImageView) imageView).loadView(path, defaultDrawable);
                }
            } else {
//                imageView.setImageURI();
                // ImageView
//                loadCallBackBitmapOnlyWifiWithAiliyun(mContext, path, isOnlyWifi, new ImageCallback() {
//                    @Override
//                    public void imageLoaded(Bitmap imageDrawable, int width, int height) {
//                        if (imageDrawable != null) {
//                            imageView.setImageBitmap(imageDrawable);
//                        }
//
//                    }
//                }, defaultDrawable);
            }
        } catch (OutOfMemoryError error) {
            Fresco.getImagePipeline().clearMemoryCaches();
        }
    }

    /**
     * @param path
     * @param imageView
     * @param isOnlyWifi
     * @param defaultDrawable
     * @param precent
     */
    public static void loadBitmapOnlyWifi(String path, ImageView imageView, boolean isOnlyWifi, int defaultDrawable, float precent) {
        if (imageView instanceof SimpleDraweeView) {
            ((SimpleDraweeView) imageView).setAspectRatio(precent);
        }
        loadBitmapOnlyWifi(path, imageView, isOnlyWifi, defaultDrawable);
    }

    /**
     * @param res
     * @param imageView
     * @param isOnlyWifi
     * @param defaultDrawable
     * @param width
     * @param precent
     */
    public static void loadBitmapOnlyWifi(int res, ImageView imageView, boolean isOnlyWifi, int defaultDrawable, int width, float precent) {
        if (res <= 0) {
            return;
        }
        String path = "res://" + mContext.getPackageName() + "/" + res;
        if (imageView instanceof SimpleDraweeView) {
            ViewGroup.LayoutParams p = imageView.getLayoutParams();
            p.width = width;
            ((SimpleDraweeView) imageView).setAspectRatio(precent);
        }
        loadBitmapOnlyWifi(path, imageView, isOnlyWifi, defaultDrawable);
    }

    /**
     * @param filePath
     * @param imageView
     * @param widthSize
     * @param heightSize
     */
    public static void loadBitmap(String filePath, ImageView imageView, int widthSize, int heightSize) {
        if (filePath == null) {
            return;
        }
        if (imageView instanceof SimpleDraweeView) {
            ViewGroup.LayoutParams p = imageView.getLayoutParams();
            p.width = widthSize;
            p.height = heightSize;
            filePath = "file://" + filePath;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(filePath))
                    .setResizeOptions(new ResizeOptions(widthSize, heightSize))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(((SimpleDraweeView) imageView).getController())
                    .setImageRequest(request)
                    .build();
            ((SimpleDraweeView) imageView).setController(controller);
        }
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            if (inSampleSize <= 0) {
                inSampleSize = 1;
            }
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     *
     */
    public static void clearFrescoCache() {
        try {
            Fresco.getImagePipeline().clearCaches();
        } catch (Exception e) {
        }
    }

    public static interface ImageCallBack {
        void onSuccess(String imgaeUrl, File file);
    }

    public static void test(final String imgUrl, final ImageCallBack imageCallBack) {
        File temp = getFile(imgUrl);
        if (temp.exists() && temp.length() > 0) {
            if (imageCallBack != null) {
                imageCallBack.onSuccess(imgUrl, temp);
                return;
            }
            // 已下载 直接读
        } else {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(ImageRequest.fromUri(Uri.parse(imgUrl)), null);
            dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
                @Override
                protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                    if (!dataSource.isFinished()) {
                        return;
                    }

//                    CloseableReference ref = dataSource.getResult();
                    new ImageAsyncTask(imageCallBack, imgUrl).execute(dataSource.getResult());
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

                }
            }, CallerThreadExecutor.getInstance());

        }
    }

    static class ImageAsyncTask extends AsyncTask<Object, Void, File> {
        private ImageCallBack imageCallBack;
        private String imageUrl;

        public ImageAsyncTask(ImageCallBack imageCallBack, String imageUrl) {
            this.imageCallBack = imageCallBack;
            this.imageUrl = imageUrl;
        }

        @Override
        protected File doInBackground(Object... params) {
            CloseableReference ref = (CloseableReference) params[0];
            InputStream is = new PooledByteBufferInputStream((PooledByteBuffer) ref.get());
            FileOutputStream bos = null;
            File f = getFile(imageUrl);
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }
                bos = new FileOutputStream(f);
                byte[] b = new byte[2048];
                int n;
                while ((n = is.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                bos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Closeables.closeQuietly(is);
                CloseableReference.closeSafely(ref);
                ref = null;
            }
            return f;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null && imageCallBack != null) {
                imageCallBack.onSuccess(imageUrl, file);
            }
        }
    }

    public static File getFile(String imgUrl) {
        File filedir = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/" + mContext.getPackageName() + "/cache/image");
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        return new File(filedir, Futils.getMD5(imgUrl) + ".dat");
    }

    private static final int BLUR_RADIUS = 50;

    @Nullable
    public static Bitmap blur(Bitmap source) {
        if (source == null) {
            return null;
        }

        try {
            return blur(source, BLUR_RADIUS);
        } catch (Exception e) {
            e.printStackTrace();
            return source;
        }
    }

    /**
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * <p>
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     * <p>
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     * <p>
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     * <p>
     * If you are using this algorithm in your code please add
     * the following line:
     * <p>
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     */
    private static Bitmap blur(Bitmap source, int radius) {
        Bitmap bitmap = source.copy(source.getConfig(), true);

        if (radius < 1) {
            return null;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rSum, gSum, bSum, x, y, i, p, yp, yi, yw;
        int vMin[] = new int[Math.max(w, h)];

        int divSum = (div + 1) >> 1;
        divSum *= divSum;
        int dv[] = new int[256 * divSum];
        for (i = 0; i < 256 * divSum; i++) {
            dv[i] = (i / divSum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int rOutSum, gOutSum, bOutSum;
        int rInSum, gInSum, bInSum;

        for (y = 0; y < h; y++) {
            rInSum = gInSum = bInSum = rOutSum = gOutSum = bOutSum = rSum = gSum = bSum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rSum += sir[0] * rbs;
                gSum += sir[1] * rbs;
                bSum += sir[2] * rbs;
                if (i > 0) {
                    rInSum += sir[0];
                    gInSum += sir[1];
                    bInSum += sir[2];
                } else {
                    rOutSum += sir[0];
                    gOutSum += sir[1];
                    bOutSum += sir[2];
                }
            }
            stackPointer = radius;

            for (x = 0; x < w; x++) {
                r[yi] = dv[rSum];
                g[yi] = dv[gSum];
                b[yi] = dv[bSum];

                rSum -= rOutSum;
                gSum -= gOutSum;
                bSum -= bOutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                rOutSum -= sir[0];
                gOutSum -= sir[1];
                bOutSum -= sir[2];

                if (y == 0) {
                    vMin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vMin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rInSum += sir[0];
                gInSum += sir[1];
                bInSum += sir[2];

                rSum += rInSum;
                gSum += gInSum;
                bSum += bInSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];

                rOutSum += sir[0];
                gOutSum += sir[1];
                bOutSum += sir[2];

                rInSum -= sir[0];
                gInSum -= sir[1];
                bInSum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rInSum = gInSum = bInSum = rOutSum = gOutSum = bOutSum = rSum = gSum = bSum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rSum += r[yi] * rbs;
                gSum += g[yi] * rbs;
                bSum += b[yi] * rbs;

                if (i > 0) {
                    rInSum += sir[0];
                    gInSum += sir[1];
                    bInSum += sir[2];
                } else {
                    rOutSum += sir[0];
                    gOutSum += sir[1];
                    bOutSum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackPointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                rSum -= rOutSum;
                gSum -= gOutSum;
                bSum -= bOutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                rOutSum -= sir[0];
                gOutSum -= sir[1];
                bOutSum -= sir[2];

                if (x == 0) {
                    vMin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vMin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rInSum += sir[0];
                gInSum += sir[1];
                bInSum += sir[2];

                rSum += rInSum;
                gSum += gInSum;
                bSum += bInSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                rOutSum += sir[0];
                gOutSum += sir[1];
                bOutSum += sir[2];

                rInSum -= sir[0];
                gInSum -= sir[1];
                bInSum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return bitmap;
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int dstWidth, int dstHeight) {
        if (source == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
    }

    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }

        int length = Math.min(source.getWidth(), source.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

//    public static void startAlbum(Activity activity) {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        activity.startActivityForResult(intent, RequestCode.REQUEST_ALBUM);
//    }
//
//    public static void startCorp(Activity activity, Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", CoverLoader.THUMBNAIL_MAX_LENGTH);
//        intent.putExtra("outputY", CoverLoader.THUMBNAIL_MAX_LENGTH);
//        intent.putExtra("return-data", false);
//        File outFile = new File(FileUtils.getCorpImagePath(activity));
//        Uri outUri = Uri.fromFile(outFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        // 取消人脸识别
//        intent.putExtra("noFaceDetection", true);
//        activity.startActivityForResult(intent, RequestCode.REQUEST_CORP);
//    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
