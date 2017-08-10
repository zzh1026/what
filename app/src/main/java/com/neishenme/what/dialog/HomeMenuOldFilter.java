package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/6.
 * @version  5.0.4弃用,
 * @time 2017/3/17 弃用
 * @author  zzh
 * 新的筛选界面见{@link HomeMenuFilter}
 */
@Deprecated
public class HomeMenuOldFilter extends BaseDialog implements View.OnClickListener {
    private OnFiltrateSelectedListener onFiltrateSelectedListener;

    private Context context;
    private ImageView ivChoose;
    private ImageView ivDialogCancle;
    private ImageView filterMan;
    private ImageView filterWoman;
    private ImageView filterNoFilter;
    private ImageView filterTime;
    private ImageView filterDistance;
    private ImageView filterPrice;
    private ImageView ivDialogChoose;
    private ImageView filterPublishType1;
    private ImageView filterPublishType2;
    private ImageView filterPublishType3;
    private manageUiClick mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl,
            mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick,
            mQuickRelease, mNormalRelease, mLookRelease;
    private int gender = 0;//1是男 2是女
    private int payType = 0;//1 我付款
    private String orderby = "";
    private manageUiClick[] imageViews;
    private static int a = 0;


    public HomeMenuOldFilter(Context context, OnFiltrateSelectedListener onFiltrateSelectedListener) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, false);
        setContentView(R.layout.home_filter_old_v505);
        this.context = context;
        this.onFiltrateSelectedListener = onFiltrateSelectedListener;
        bindView();
        setListener();
        initUiclick();
    }

    private void initUiclick() {
        mManManageUiClick = new manageUiClick(filterMan, R.drawable.filter_man, R.drawable.filter_man_light);
        mWomanManageUiClick = new manageUiClick(filterWoman, R.drawable.filter_woman, R.drawable.filter_woman_light);
        mNoManageUiClicl = new manageUiClick(filterNoFilter, R.drawable.filter_no_filter, R.drawable.filter_no_filter_light);
        mTimeManageUiClick = new manageUiClick(filterTime, R.drawable.filter_time, R.drawable.filter_time_light);
        mDistanceManageUiClick = new manageUiClick(filterDistance, R.drawable.filter_distance, R.drawable.filter_distance_liget);
        mPriceManageUiClick = new manageUiClick(filterPrice, R.drawable.filter_price, R.drawable.filter_price_light_light);
        mQuickRelease = new manageUiClick(filterPublishType1, R.drawable.home_filter_quick_normal, R.drawable.home_filter_quick_press);
        mNormalRelease = new manageUiClick(filterPublishType2, R.drawable.home_filter_normal_normal, R.drawable.home_filter_normal_press);
        mLookRelease = new manageUiClick(filterPublishType3, R.drawable.home_filter_look_normal, R.drawable.home_filter_look_press);
        imageViews = new manageUiClick[]{mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl,
                mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick, mQuickRelease,
                mNormalRelease, mLookRelease};
    }

    public void bindView() {
        ivChoose = (ImageView) findViewById(R.id.iv_dialog_choose);
        filterMan = (ImageView) findViewById(R.id.filter_man);
        filterWoman = (ImageView) findViewById(R.id.filter_woman);
        filterNoFilter = (ImageView) findViewById(R.id.filter_no_filter);
        filterTime = (ImageView) findViewById(R.id.filter_time);
        filterDistance = (ImageView) findViewById(R.id.filter_distance);
        filterPrice = (ImageView) findViewById(R.id.filter_price);

        filterPublishType1 = (ImageView) findViewById(R.id.filter_publish_type1);
        filterPublishType2 = (ImageView) findViewById(R.id.filter_publish_type2);
        filterPublishType3 = (ImageView) findViewById(R.id.filter_publish_type3);
    }

    public void setListener() {
        ivChoose.setOnClickListener(this);

        filterMan.setOnClickListener(this);
        filterWoman.setOnClickListener(this);
        filterNoFilter.setOnClickListener(this);

        filterTime.setOnClickListener(this);
        filterDistance.setOnClickListener(this);
        filterPrice.setOnClickListener(this);

        filterPublishType1.setOnClickListener(this);
        filterPublishType2.setOnClickListener(this);
        filterPublishType3.setOnClickListener(this);
    }

    public interface OnFiltrateSelectedListener {

        void filtrateSelected(String str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_choose:
                String s = "";
                for (int i = 0; i < 9; i++) {
                    if ((imageViews[i]).isclicked) {
                        s += i;
                    }
                }
                onFiltrateSelectedListener.filtrateSelected(s);
                this.dismiss();
                a = 0;
                break;
            case R.id.filter_man:
                if (!mManManageUiClick.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();
                }
                mManManageUiClick.onClick();
                break;
            case R.id.filter_woman:
                if (!mWomanManageUiClick.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();
                }
                mWomanManageUiClick.onClick();
                break;
            case R.id.filter_no_filter:
                if (!mNoManageUiClicl.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();
                }
                mNoManageUiClicl.onClick();
                break;
            case R.id.filter_time:
                if (!mTimeManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mLookRelease.setDisSelect();
                }
                mTimeManageUiClick.onClick();
                break;
            case R.id.filter_distance:
                if (!mDistanceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mLookRelease.setDisSelect();
                }
                mDistanceManageUiClick.onClick();
                break;
            case R.id.filter_price:
                if (!mPriceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mLookRelease.setDisSelect();
                }
                mPriceManageUiClick.onClick();
                break;
            case R.id.filter_publish_type1:
                if (!mQuickRelease.isclicked) {
                    mQuickRelease.setDisSelect();
                    mNormalRelease.setDisSelect();
                }
                mQuickRelease.onClick();
                break;
            case R.id.filter_publish_type2:
                if (!mNormalRelease.isclicked) {
                    mQuickRelease.setDisSelect();
                    mNormalRelease.setDisSelect();
                }
                mNormalRelease.onClick();
                break;
            case R.id.filter_publish_type3:
                if (!mLookRelease.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mLookRelease.setDisSelect();
                }
                mLookRelease.onClick();
                break;

        }
        if (a == 0) {
            ivChoose.setImageResource(R.drawable.dialog_cancle2x);
        }
        if (a == 1) {
            ivChoose.setImageResource(R.drawable.dialog_choose2x);
        }
    }

    static class manageUiClick {
        public boolean isclicked = false;
        ImageView iv;
        int black_id;
        int light_id;

        manageUiClick(ImageView iv, int black_id, int light_id) {
            this.iv = iv;
            this.black_id = black_id;
            this.light_id = light_id;
        }

        public void onClick() {
            if (isclicked) {
                iv.setImageResource(black_id);
                a--;
            } else {
                iv.setImageResource(light_id);
                a++;
            }
            isclicked = !isclicked;
        }

        public void setDisSelect() {
            if (isclicked == true) {
                iv.setImageResource(black_id);
                isclicked = false;
                a--;
            }
        }
    }
}
