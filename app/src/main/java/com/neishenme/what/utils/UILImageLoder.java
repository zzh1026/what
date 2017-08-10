package com.neishenme.what.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.neishenme.what.galleryfinal.widget.GFImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by Administrator on 2016/5/20.
 */
public class UILImageLoder implements com.neishenme.what.galleryfinal.ImageLoader {
    private Bitmap.Config mImageConfig;

    public UILImageLoder() {
        this(Bitmap.Config.RGB_565);
    }

    public UILImageLoder(Bitmap.Config config) {
        this.mImageConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .bitmapConfig(mImageConfig)
                .build();
        ImageSize imageSize = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), options);
    }

    @Override
    public void clearMemoryCache() {

    }
}
