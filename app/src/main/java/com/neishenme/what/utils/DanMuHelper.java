package com.neishenme.what.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.anbetter.danmuku.DanMuView;
import com.anbetter.danmuku.model.DanMuModel;
import com.anbetter.danmuku.model.utils.DimensionUtil;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.ActiveIphone7Response;
import com.neishenme.what.net.HttpLoader;

import org.seny.android.utils.ALog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * 弹幕库使用帮助类
 * <p>
 * 建议凡是弹幕中涉及到的图片，大小控制在50kb以内，尺寸控制在100x100以内（单位像素）
 * <p>
 * Created by android_ls on 2016/12/18.
 */
public final class DanMuHelper {

    private ArrayList<WeakReference<DanMuModel>> mDanMuViewParents;
    private Context mContext;
    private DanMuView mDanMuView;
    private Random mRandom = new Random();

    public DanMuHelper(Context context, DanMuView mDanMuView) {
        this.mContext = context.getApplicationContext();
        this.mDanMuViewParents = new ArrayList<>();
        this.mDanMuView = mDanMuView;
    }

    public void release() {
        if (mDanMuViewParents != null) {
            for (WeakReference<DanMuModel> danMuModel : mDanMuViewParents) {
                if (danMuModel != null) {
                    DanMuModel danMuParent = danMuModel.get();
                    if (danMuParent != null)
                        danMuParent.release();
                }
            }
            mDanMuViewParents.clear();
            mDanMuViewParents = null;
        }

        mDanMuView.release();
        mDanMuView.clear();
        mDanMuView = null;
        mContext = null;
    }

    public void addDanMu(ActiveIphone7Response.DataBean.ListsBean list) {
        if (mDanMuViewParents != null) {

            DanMuModel danMuView = createDanMuView(list);
            if (mDanMuViewParents != null && danMuView != null) {
                mDanMuViewParents.add(new WeakReference<>(danMuView));
                mDanMuView.add(danMuView);
            }
        }
    }

    public void addDanMu(String msg) {
        if (mDanMuViewParents != null) {

            DanMuModel danMuView = createDanMuView(msg);
            if (mDanMuViewParents != null && danMuView != null) {
                mDanMuViewParents.add(new WeakReference<>(danMuView));
                mDanMuView.add(danMuView);
            }
        }
    }

    private DanMuModel createDanMuView(String msg) {
        final DanMuModel danMuView = new DanMuModel();

        danMuView.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
        danMuView.setPriority(DanMuModel.NORMAL);
        danMuView.marginLeft = DimensionUtil.dpToPx(mContext, 10 + mRandom.nextInt(25) * (mRandom.nextInt(5) + 1));

        danMuView.textSize = DimensionUtil.spToPx(mContext, 14);
        danMuView.textColor = ContextCompat.getColor(mContext, R.color.light_green);
        danMuView.textMarginLeft = DimensionUtil.dpToPx(mContext, 8);
        danMuView.text = msg;


        danMuView.textBackground = ContextCompat.getDrawable(mContext, R.drawable.push_main_danmu_bg);
        danMuView.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 15);
        danMuView.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 9);
        danMuView.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 9);
        danMuView.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 15);
        return danMuView;
    }

    private DanMuModel createDanMuView(ActiveIphone7Response.DataBean.ListsBean list) {
        final DanMuModel danMuView = new DanMuModel();

        int avatarSize = DimensionUtil.dpToPx(mContext, 30);
        danMuView.avatarWidth = avatarSize;
        danMuView.avatarHeight = avatarSize;

        String url = list.getUserLogo();
        if (!TextUtils.isEmpty(url)) {
            HttpLoader.getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    danMuView.avatar = DrawableUtils.getCircleBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, 100, 100, ImageView.ScaleType.CENTER_CROP);
        }
        danMuView.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
        danMuView.setPriority(DanMuModel.NORMAL);
        danMuView.marginLeft = DimensionUtil.dpToPx(mContext, 10 + mRandom.nextInt(25) * (mRandom.nextInt(5) + 1));
        danMuView.setSpeed(8.5f);
        danMuView.textSize = DimensionUtil.spToPx(mContext, 14);
        danMuView.textColor = ContextCompat.getColor(mContext, R.color.light_green);
        danMuView.textMarginLeft = DimensionUtil.dpToPx(mContext, 8);
        danMuView.text = list.getUserName() + " 申请 " + list.getPrizeName() + " 通过";


        danMuView.textBackground = ContextCompat.getDrawable(mContext, R.drawable.push_main_danmu_bg);
        danMuView.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 35);
        danMuView.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 9);
        danMuView.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 9);
        danMuView.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 25);
        return danMuView;
    }
}