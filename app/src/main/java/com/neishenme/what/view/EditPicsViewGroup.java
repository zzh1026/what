package com.neishenme.what.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.dialog.PhotoWallDialog;
import com.neishenme.what.galleryfinal.CoreConfig;
import com.neishenme.what.galleryfinal.FunctionConfig;
import com.neishenme.what.galleryfinal.GalleryFinal;
import com.neishenme.what.galleryfinal.ThemeConfig;
import com.neishenme.what.galleryfinal.model.PhotoInfo;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.UILImageLoder;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class EditPicsViewGroup extends ViewGroup implements PhotoWallDialog.DeletePhotoListener {
    private Context mContext;
    private int screenWidth;
    private List<String> imgs;
    private LayoutInflater mInflater;
    private static final int DEFAULT_TAG_RESID = R.layout.default_pic;
    private int mTagResId;
    com.neishenme.what.galleryfinal.ImageLoader imageLoader;
    private PhotoWallDialog photoWallDialog;
    private int deletePos;



    @Override
    public void deleteListener() {
        imgs.remove(deletePos);
        setTags(imgs);
        photoWallDialog.dismiss();

    }

    public interface GridItemClickListener {
        void onItemClick(int position);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                switch (reqeustCode) {

                }
            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    public EditPicsViewGroup(Context context) {
        super(context);
        mContext = context;
        screenWidth = getWindowWidth();
        mInflater = LayoutInflater.from(mContext);
        imageLoader = new UILImageLoder();
        init();
    }

    public EditPicsViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        screenWidth = getWindowWidth();
        mInflater = LayoutInflater.from(mContext);
        imageLoader = new UILImageLoder();
        init();

    }

    public EditPicsViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        screenWidth = getWindowWidth();
        mInflater = LayoutInflater.from(mContext);
        imageLoader = new UILImageLoder();

        //图片加载设置
        imageLoader = new UILImageLoder();
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditPicsViewGroup,
                defStyleAttr,
                defStyleAttr
        );

        mTagResId = a.getResourceId(R.styleable.EditPicsViewGroup_picDefaultResId, DEFAULT_TAG_RESID);
        a.recycle();
    }


    public void init() {
        ThemeConfig themeConfig = null;
        ThemeConfig theme = ThemeConfig.BLACK;
        themeConfig = theme;
        final FunctionConfig functionConfig = fouctionCondig();
        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
        photoWallDialog = new PhotoWallDialog(mContext, functionConfig, mOnHanlderResultCallback, this);

    }

    public FunctionConfig fouctionCondig() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        //剪裁成正方形
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setCropSquare(true);
        functionConfigBuilder.setForceCrop(true);
        functionConfigBuilder.setEnableEdit(true);
        return functionConfigBuilder.build();
    }

    public void setTags(List<String> imgsList) {
        this.imgs = imgsList;
        this.removeAllViews();
        if (imgs != null && imgs.size() > 0) {
            for (int i = 0; i < 9; i++) {
                ImageView tagView = (ImageView) mInflater.inflate(R.layout.default_pic, null);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                tagView.setLayoutParams(layoutParams);
                if (i < imgs.size()) {
                    HttpLoader.getImageLoader().get(imgs.get(i), ImageLoader.getImageListener(
                            tagView, R.drawable.default_pic, R.drawable.default_pic));
                }
                final int finalI = i;
                tagView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePos = finalI;
                        photoWallDialog.show();

                    }
                });

                addView(tagView);
            }
        }
        postInvalidate();
    }

    public int getWindowWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int cCount = getChildCount();

        //定义每个item的大小
        int fristItemWidth = (screenWidth - 3 * 6) / 4 * 2 + 6;
        int fristItemHeight = fristItemWidth;
        int itemWidth = (screenWidth - 6 * 3) / 4;
        int itemHeight = itemWidth;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            if (i == 0) {
                measureChild(childView, fristItemWidth, fristItemHeight);
            } else {
                measureChild(childView, itemWidth, itemHeight);
            }
        }

        //当控件layout_width=match_parent;layout_height=wrap_content;

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? screenWidth : 100,
                heightMode == MeasureSpec.AT_MOST ? 100 : (itemHeight * 3 + 6 * 2));


    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        //定义每个item的大小
        int fristItemWidth = (screenWidth - 3 * 6) / 4 * 2 + 6;
        int fristItemHeight = fristItemWidth;
        int itemWidth = (screenWidth - 6 * 3) / 4;
        int itemHeight = itemWidth;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            int cl = 0, ct = 0, cr = 0, cb = 0;
            int fr = 0, fb = 0;
            switch (i) {
                case 0:
                    cl = 0;
                    ct = 0;
                    fr = cl + fristItemWidth;
                    fb = ct + fristItemHeight;
                    break;
                case 1:
                    cl = fristItemWidth + 6;
                    ct = 0;
                    break;
                case 2:
                    cl = fristItemWidth + 6 + itemWidth + 6;
                    ct = 0;
                    break;
                case 3:
                    cl = fristItemWidth + 6;
                    ct = itemHeight + 6;
                    break;
                case 4:
                    cl = fristItemWidth + 6 + itemWidth + 6;
                    ct = itemHeight + 6;
                    break;
                case 5:
                    cl = fristItemWidth + 6 + itemWidth + 6;
                    ct = itemHeight * 2 + 6 * 2;
                    break;
                case 6:
                    cl = fristItemWidth + 6;
                    ct = itemHeight * 2 + 6 * 2;
                    break;
                case 7:
                    cl = itemWidth + 6;
                    ct = fristItemHeight + 6;
                    break;
                case 8:
                    cl = 0;
                    ct = fristItemHeight + 6;
                    break;
            }

            cr = cl + itemWidth;
            cb = itemHeight + ct;
            childView.layout(cl, ct, i == 0 ? fr : cr, i == 0 ? fb : cb);

        }


    }
}
