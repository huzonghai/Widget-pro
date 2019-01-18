package com.ck.newssdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.ck.newssdk.R;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.utils.imload.cache.disc.impl.UnlimitedDiskCache;
import com.ck.newssdk.utils.imload.cache.disc.naming.HashCodeFileNameGenerator;
import com.ck.newssdk.utils.imload.cache.memory.impl.LruMemoryCache;
import com.ck.newssdk.utils.imload.core.DisplayImageOptions;
import com.ck.newssdk.utils.imload.core.ImageLoader;
import com.ck.newssdk.utils.imload.core.ImageLoaderConfiguration;
import com.ck.newssdk.utils.imload.core.assist.ImageScaleType;
import com.ck.newssdk.utils.imload.core.assist.QueueProcessingType;
import com.ck.newssdk.utils.imload.core.decode.BaseImageDecoder;
import com.ck.newssdk.utils.imload.core.display.SimpleBitmapDisplayer;
import com.ck.newssdk.utils.imload.core.download.BaseImageDownloader;
import com.ck.newssdk.utils.imload.utils.StorageUtils;

import java.io.File;

public class Iml {
    public volatile static DisplayImageOptions options;
    private final static String[] country = {"us", "br", "ru", "in", "id"};

    public Iml() {
    }

    public static void initIml(Context context, String countryCode) {
        // 配置/初始化
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 缓存最大图片大小
//                .diskCacheExtraOptions(480, 800, null) // 闪存最大图片大小
                .threadPoolSize(3) // default 最大线程数
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 线程优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default 线程处理队列，先进先出
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // LruMemory
                .memoryCacheSize(2 * 1024 * 1024) // 缓存
                .memoryCacheSizePercentage(13)    // default 缓存比例？
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 闪存缓存
                .diskCacheSize(50 * 1024 * 1024) // 闪存缓存大小
                .diskCacheFileCount(100) // 闪存缓存图片文件数量
                //                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default 文件名
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs() // LOG
                .build();
        ImageLoader.getInstance().init(config);
        getImlOptions();
        setCountry(countryCode);
    }

    public static void initIml(Context context) {
        // 配置/初始化
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 缓存最大图片大小
//                .diskCacheExtraOptions(480, 800, null) // 闪存最大图片大小
                .threadPoolSize(3) // default 最大线程数
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 线程优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default 线程处理队列，先进先出
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // LruMemory
                .memoryCacheSize(2 * 1024 * 1024) // 缓存
                .memoryCacheSizePercentage(13)    // default 缓存比例？
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 闪存缓存
                .diskCacheSize(50 * 1024 * 1024) // 闪存缓存大小
                .diskCacheFileCount(100) // 闪存缓存图片文件数量
                //                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default 文件名
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs() // LOG
                .build();
        ImageLoader.getInstance().init(config);
        getImlOptions();
    }

    private static void getImlOptions() {
//        if (options == null) {
//            synchronized (DisplayImageOptions.class) {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.placeholder_figure) // resource or drawable
                    .showImageForEmptyUri(R.mipmap.placeholder_figure) // resource or drawable
                    .showImageOnFail(R.mipmap.placeholder_figure) // resource or drawable
                    .resetViewBeforeLoading(false)  // default
                    .delayBeforeLoading(300)
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.RGB_565) // default
//                            .decodingOptions()
                    .displayer(new SimpleBitmapDisplayer()) // default
                    .handler(new Handler()) // default
                    .build();
        }
//            }
//        }
    }

    public static void displayImage(String imageUrl, ImageView image) {
        ImageLoader.getInstance().displayImage(imageUrl, image, options);
    }

    public static void setCountry(String countryCode) {
        String s = countryCode.toLowerCase().trim();
        for (String code : country) {
            if (s.equals(code)) {
                Configuration.CurrentCountry = s;
                return;
            } else {
                Configuration.CurrentCountry = "us";
            }
        }
    }
}
