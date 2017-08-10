package com.neishenme.what.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.common.AppSharePreConfig;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 作者：zhaozh create on 2016/3/11 18:10
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个app引导界面
 * .
 * 其作用是 :
 */
public class GuideImageActivity extends Activity {
    private ViewPager mPager;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout mLinearLayout;
    private ImageView redImageView;
    private ImageView mGuideStartIv;

//    以前的现在不用了,但是为了能找到那两张图片留下了这个代码没有删掉
//    private int[] mImages = new int[]{R.drawable.guide_1,
//            R.drawable.guide_2};

    private int[] mImages = new int[]{R.drawable.guide_img_1,
            R.drawable.guide_img_2, R.drawable.guide_img_3, R.drawable.guide_img_4};

    private int pointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_guide_image);

        mLinearLayout = (LinearLayout) findViewById(R.id.ll_guide_point);

        initData();

        mGuideStartIv = (ImageView) findViewById(R.id.guide_start_iv);
        mGuideStartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.EDIT.putBoolean(AppSharePreConfig.IS_FIRST_ENTER, false).commit();
                ActivityUtils.startActivityAndFinish(GuideImageActivity.this, MainActivity.class);
            }
        });

        App.addActivity(this);
        redImageView = (ImageView) findViewById(R.id.iv_guide_select);
        redImageView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        redImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        pointDis = mLinearLayout.getChildAt(1).getLeft()
                                - mLinearLayout.getChildAt(0).getLeft();
                    }
                });


        mPager = (ViewPager) findViewById(R.id.vp_guide_show);
        mPager.setAdapter(new GuideAdapter());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

                int leftMargen = (int) (pointDis * (positionOffset + position));
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) redImageView.getLayoutParams();
                params.leftMargin = leftMargen;

                //redImageView.setAlpha((float) Math.abs(0.5 - positionOffset));
                redImageView.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageViewList.size() - 1) {
                    mGuideStartIv.setVisibility(View.VISIBLE);
                } else {
                    mGuideStartIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化数据的方法
     */
    private void initData() {
        imageViewList = new ArrayList<>();
        for (int i = 0; i < mImages.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(mImages[i]);
            imageViewList.add(iv);

            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guide_normal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(App.getApplication(), 5),
                    DensityUtil.dip2px(App.getApplication(), 5));
            if (i > 0) {
                params.leftMargin = DensityUtil.dip2px(App.getApplication(), 10);
            }
            point.setLayoutParams(params);

            mLinearLayout.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = imageViewList.get(position);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
