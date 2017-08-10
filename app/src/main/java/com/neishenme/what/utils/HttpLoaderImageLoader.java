package com.neishenme.what.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neishenme.what.R;
import com.youth.banner.loader.ImageLoader;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/11.
 */

public class HttpLoaderImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide
                .with(context)
                .load(path)
                .placeholder(R.drawable.picture_moren)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }
}
