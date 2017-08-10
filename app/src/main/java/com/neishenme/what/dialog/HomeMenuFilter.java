package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/6.
 *
 * @author zzh
 * @version v5.0.4 , 新的筛选dialog ,  旧的 {@link HomeMenuOldFilter}
 * @time 2017/3/17
 */
public class HomeMenuFilter extends BaseDialog implements View.OnClickListener {
    private OnFiltrateSelectedListener onFiltrateSelectedListener;

    private Context context;
    private ImageView ivChoose;
    private ImageView filterMan;
    private ImageView filterWoman;
    private ImageView filterNoFilter;
    private ImageView filterTime;
    private ImageView filterDistance;
    private ImageView filterPrice;
    private ImageView filterVip;
    private manageUiClick mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl,
            mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick;
    private manageUiClick mVipUiClick;
    private manageUiClick[] imageViews;
    private static int a = 0;


    public HomeMenuFilter(Context context, OnFiltrateSelectedListener onFiltrateSelectedListener) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, false);
        setContentView(R.layout.home_filter);
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
        mVipUiClick = new manageUiClick(filterVip, R.drawable.filter_vip, R.drawable.filter_vip_light_light);
        imageViews = new manageUiClick[]{mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl,
                mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick, mVipUiClick};
    }

    public void bindView() {
        ivChoose = (ImageView) findViewById(R.id.iv_dialog_choose);
        filterMan = (ImageView) findViewById(R.id.filter_man);
        filterWoman = (ImageView) findViewById(R.id.filter_woman);
        filterNoFilter = (ImageView) findViewById(R.id.filter_no_filter);
        filterTime = (ImageView) findViewById(R.id.filter_time);
        filterDistance = (ImageView) findViewById(R.id.filter_distance);
        filterPrice = (ImageView) findViewById(R.id.filter_price);
        filterVip = (ImageView) findViewById(R.id.filter_vip);
    }

    public void setListener() {
        ivChoose.setOnClickListener(this);

        filterMan.setOnClickListener(this);
        filterWoman.setOnClickListener(this);
        filterNoFilter.setOnClickListener(this);

        filterTime.setOnClickListener(this);
        filterDistance.setOnClickListener(this);
        filterPrice.setOnClickListener(this);

        filterVip.setOnClickListener(this);
    }

    public interface OnFiltrateSelectedListener {

        void filtrateSelected(String str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_choose:
                String s = "";
                for (int i = 0; i < imageViews.length; i++) {
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
                    mVipUiClick.setDisSelect();
                }
                mManManageUiClick.onClick();
                break;
            case R.id.filter_woman:
                if (!mWomanManageUiClick.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();
                    mVipUiClick.setDisSelect();
                }
                mWomanManageUiClick.onClick();
                break;
            case R.id.filter_no_filter:
                if (!mNoManageUiClicl.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();
                    mVipUiClick.setDisSelect();
                }
                mNoManageUiClicl.onClick();
                break;
            case R.id.filter_time:
                if (!mTimeManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mVipUiClick.setDisSelect();
                }
                mTimeManageUiClick.onClick();
                break;
            case R.id.filter_distance:
                if (!mDistanceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mVipUiClick.setDisSelect();
                }
                mDistanceManageUiClick.onClick();
                break;
            case R.id.filter_price:
                if (!mPriceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                    mVipUiClick.setDisSelect();
                }
                mPriceManageUiClick.onClick();
                break;
            case R.id.filter_vip:
                if (!mVipUiClick.isclicked) {
                    mWomanManageUiClick.setDisSelect();
                    mManManageUiClick.setDisSelect();
                    mNoManageUiClicl.setDisSelect();

                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                }
                mVipUiClick.onClick();
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
