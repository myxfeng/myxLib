package com.myx.library.image;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.myx.library.util.Futils;
import com.myx.library.util.MainHandler;
import com.myx.library.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

import lib.lhh.fiv.library.FrescoImageView;
import lib.lhh.fiv.library.FrescoZoomImageView;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class ImageUtils {
    public static GenericDraweeHierarchyBuilder builder;
    public static GenericDraweeHierarchy hierarchy;
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    /**
     * 使用的缓存数量
     */
    public static final long MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;
    /**
     * 使用的缓存数量
     */
    public static final long MAX_LOW_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 16;
    /**
     * 使用的缓存数量
     */
    public static final long MAX_VERY_LOW_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 32;
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
    public static void loadImageOnlyWifi(String path, final ImageView imageView, boolean isOnlyWifi, final int defaultDrawable) {
        try {
            if (path == null) {
                path = "";
            }
            // FrescoImageView
            if (imageView instanceof FrescoImageView) {
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
                //SimpleDraweeView
            } else if (imageView instanceof SimpleDraweeView) {
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
//                        imageView.setImageURI(Uri.parse(path));
                        loadDrawable((SimpleDraweeView) imageView, Uri.parse(path), true);
                        boolean isInCache = isImageDownloaded(Uri.parse(path));
                        if (isInCache) {
//                            imageView.setImageURI(Uri.parse(path));
                            loadDrawable((SimpleDraweeView) imageView, Uri.parse(path), true);

                        } else {
                            imageView.setImageURI(Uri.parse(""));
                        }
                    }
                } else {
//                    imageView.setImageURI(Uri.parse(path));
                    loadDrawable((SimpleDraweeView) imageView, Uri.parse(path), true);
                }
                // FrescoZoomImageView
            } else if (imageView instanceof FrescoZoomImageView) {
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
                if (isOnlyWifi) {
                    if (NetworkUtils.isActiveNetworkMobile(mContext)) {
                        loadImage(mContext, path, -1, -1, new IResult<Bitmap>() {
                            @Override
                            public void onResult(Bitmap result) {
                                if (result == null && defaultDrawable != 0) {
                                    imageView.setImageResource(defaultDrawable);
                                } else {
                                    imageView.setImageBitmap(result);

                                }
                            }
                        });
                    } else {
                        if (isImageDownloaded(Uri.parse(path))) {
                            loadImage(mContext, path, -1, -1, new IResult<Bitmap>() {
                                @Override
                                public void onResult(Bitmap result) {
                                    if (result == null && defaultDrawable != 0) {
                                        imageView.setImageResource(defaultDrawable);
                                    } else {
                                        imageView.setImageBitmap(result);

                                    }
                                }
                            });
                        } else {
                            if (defaultDrawable != 0) {
                                imageView.setImageResource(defaultDrawable);

                            }
                        }
                    }
                } else {
                    loadImage(mContext, path, -1, -1, new IResult<Bitmap>() {
                        @Override
                        public void onResult(Bitmap result) {
                            if (result == null && defaultDrawable != 0) {
                                imageView.setImageResource(defaultDrawable);
                            } else {
                                imageView.setImageBitmap(result);

                            }
                        }
                    });
                }

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
    public static void loadImageOnlyWifi(String path, ImageView imageView, boolean isOnlyWifi, int defaultDrawable, float precent) {
        if (imageView instanceof SimpleDraweeView) {
            ((SimpleDraweeView) imageView).setAspectRatio(precent);
        }
        loadImageOnlyWifi(path, imageView, isOnlyWifi, defaultDrawable);
    }

    private static void loadDrawable(SimpleDraweeView draweeView, Uri uri, boolean isAutoPlay) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(isAutoPlay)
                .build();
        draweeView.setController(controller);
    }

    /**
     * @param res
     * @param draweeView
     */
    public static void loadResBitmapOnlyWifi(int res, SimpleDraweeView draweeView, boolean isAutoPlay) {
        if (res <= 0) {
            return;
        }
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(res))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(isAutoPlay)
                .build();
        draweeView.setController(controller);
    }

    /**
     * @param filePath
     * @param imageView
     * @param widthSize
     * @param heightSize
     */
    public static void loadFile(String filePath, SimpleDraweeView imageView, boolean isAutoPlay, int widthSize, int heightSize) {
        if (filePath == null) {
            return;
        }
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (widthSize > 0 && heightSize > 0) {
            requestBuilder.setResizeOptions(new ResizeOptions(widthSize, heightSize));
        }
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(imageView.getController())
                .setImageRequest(requestBuilder.build())
                .setAutoPlayAnimations(isAutoPlay)
                .build();
        imageView.setController(controller);
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


    public static File getFile(String imgUrl) {
        File filedir = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/" + mContext.getPackageName() + "/cache/image");
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        return new File(filedir, Futils.getMD5(imgUrl) + ".dat");
    }

    public static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            rceycleBitmapDrawable(bd);
        }
    }

    public static void rceycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            rceycleBitmap(bitmap);
        }
        bitmapDrawable = null;
    }

    public static void rceycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }


    public static void recycleBackgroundBitMap(ImageView view) {
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            rceycleBitmapDrawable(bd);
        }
    }

    /**
     * 从网络下载图片
     * 1、根据提供的图片URL，获取图片数据流
     * 2、将得到的数据流写入指定路径的本地文件
     *
     * @param url            URL
     * @param loadFileResult LoadFileResult
     */
    public static void downloadImage(Context context, String url, final IDownloadResult loadFileResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();

        // 获取未解码的图片数据
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished() || loadFileResult == null) {
                    return;
                }

                CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
                    try {
                        PooledByteBuffer pooledByteBuffer = closeableReference.get();
                        InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);
                        final String photoPath = loadFileResult.getFilePath();
                        byte[] data = StreamTool.read(inputStream);
                        StreamTool.write(photoPath, data);
                        MainHandler.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadFileResult.onResult(photoPath);
                            }
                        });
                    } catch (IOException e) {
                        loadFileResult.onResult(null);
                        e.printStackTrace();
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                int progress = (int) (dataSource.getProgress() * 100);
                if (loadFileResult != null) {
                    loadFileResult.onProgress(progress);
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadFileResult != null) {
                    loadFileResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, Executors.newSingleThreadExecutor());
    }

    /**
     * 从本地文件或网络获取Bitmap
     *
     * @param context
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @param loadImageResult
     */
    public static void loadImage(final Context context,
                                 String url,
                                 final int reqWidth,
                                 final int reqHeight,
                                 final IResult<Bitmap> loadImageResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        if (!UriUtil.isNetworkUri(uri)) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(url)
                    .build();
        }

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (reqWidth > 0 && reqHeight > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(reqWidth, reqHeight));
        }
        ImageRequest imageRequest = imageRequestBuilder.build();

        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableImage> closeableReference = imageReference.clone();
                    try {
                        CloseableImage closeableImage = closeableReference.get();
                        if (closeableImage instanceof CloseableAnimatedImage) {
                            AnimatedImageResult animatedImageResult = ((CloseableAnimatedImage) closeableImage).getImageResult();
                            if (animatedImageResult != null && animatedImageResult.getImage() != null) {
                                int imageWidth = animatedImageResult.getImage().getWidth();
                                int imageHeight = animatedImageResult.getImage().getHeight();

                                Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, bitmapConfig);
                                animatedImageResult.getImage().getFrame(0).renderFrame(imageWidth, imageHeight, bitmap);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(bitmap);
                                }
                            }
                        } else if (closeableImage instanceof CloseableBitmap) {
                            CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                            Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                            if (bitmap != null && !bitmap.isRecycled()) {
                                // https://github.com/facebook/fresco/issues/648
                                final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(tempBitmap);
                                }
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadImageResult != null) {
                    loadImageResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }


}
