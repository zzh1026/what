package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/6.
 * 旧的主界面删选弹窗,已弃用, 新的弹窗见 @see {@link HomeMenuFilter}
 */
@Deprecated
public class HomeMenuFilterOld extends BaseDialog implements View.OnClickListener {
    private OnFiltrateSelectedListener onFiltrateSelectedListener;

    private Context context;
    private ImageView ivCancle;
    private ImageView ivChoose;
    private ImageView ivDialogCancle;
    private ImageView filterMan;
    private ImageView filterWoman;
    private ImageView filterNoFilter;
    private ImageView filterMyTreat;
    private ImageView filterTreatGuest;
    private ImageView filterAA;
    private ImageView filterTime;
    private ImageView filterDistance;
    private ImageView filterPrice;
    private ImageView ivDialogChoose;
    private manageUiClick mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl, mMyThreatMa,
            mTreatGManageUiClick, mAAManageUiClick, mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick;
    private int gender = 0;//1是男 2是女
    private int payType = 0;//1 我付款
    private String orderby = "";
    private manageUiClick[] imageViews;
    private static int a = 0;


    public HomeMenuFilterOld(Context context, OnFiltrateSelectedListener onFiltrateSelectedListener) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, false);
        setContentView(R.layout.home_filter_old);
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
        mMyThreatMa = new manageUiClick(filterMyTreat, R.drawable.filter_my_treat, R.drawable.filter_my_treat_light);
        mTreatGManageUiClick = new manageUiClick(filterTreatGuest, R.drawable.filter_treat_guest, R.drawable.filter_treat_guest_light);
        mAAManageUiClick = new manageUiClick(filterAA, R.drawable.aa, R.drawable.aa_light);
        mTimeManageUiClick = new manageUiClick(filterTime, R.drawable.filter_time, R.drawable.filter_time_light);
        mDistanceManageUiClick = new manageUiClick(filterDistance, R.drawable.filter_distance, R.drawable.filter_distance_liget);
        mPriceManageUiClick = new manageUiClick(filterPrice, R.drawable.filter_price, R.drawable.filter_price_light_light);
        imageViews = new manageUiClick[]{mManManageUiClick, mWomanManageUiClick, mNoManageUiClicl, mMyThreatMa, mTreatGManageUiClick, mAAManageUiClick,
                mTimeManageUiClick, mDistanceManageUiClick, mPriceManageUiClick};
    }

    public void bindView() {
        ivCancle = (ImageView) findViewById(R.id.iv_dialog_cancle);
        ivChoose = (ImageView) findViewById(R.id.iv_dialog_choose);
        filterMan = (ImageView) findViewById(R.id.filter_man);
        filterWoman = (ImageView) findViewById(R.id.filter_woman);
        filterNoFilter = (ImageView) findViewById(R.id.filter_no_filter);
        filterMyTreat = (ImageView) findViewById(R.id.filter_my_treat);
        filterTreatGuest = (ImageView) findViewById(R.id.filter_treat_guest);
        filterAA = (ImageView) findViewById(R.id.filter_AA);
        filterTime = (ImageView) findViewById(R.id.filter_time);
        filterDistance = (ImageView) findViewById(R.id.filter_distance);
        filterPrice = (ImageView) findViewById(R.id.filter_price);

    }

    public void setListener() {
        ivCancle.setOnClickListener(this);
        ivChoose.setOnClickListener(this);

        filterMan.setOnClickListener(this);
        filterWoman.setOnClickListener(this);
        filterNoFilter.setOnClickListener(this);

        filterMyTreat.setOnClickListener(this);
        filterTreatGuest.setOnClickListener(this);
        filterAA.setOnClickListener(this);

        filterTime.setOnClickListener(this);
        filterDistance.setOnClickListener(this);
        filterPrice.setOnClickListener(this);
    }

    public interface OnFiltrateSelectedListener {

        void filtrateSelected(String str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_cancle:
                onFiltrateSelectedListener.filtrateSelected("取消");
                this.dismiss();
                a = 0;
                break;
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
            case R.id.filter_my_treat:
                if (!mMyThreatMa.isclicked) {
                    mMyThreatMa.setDisSelect();
                    mTreatGManageUiClick.setDisSelect();
                    mAAManageUiClick.setDisSelect();
                }
                mMyThreatMa.onClick();
                break;
            case R.id.filter_treat_guest:
                if (!mTreatGManageUiClick.isclicked) {
                    mMyThreatMa.setDisSelect();
                    mTreatGManageUiClick.setDisSelect();
                    mAAManageUiClick.setDisSelect();
                }
                mTreatGManageUiClick.onClick();
                break;
            case R.id.filter_AA:
                if (!mAAManageUiClick.isclicked) {
                    mMyThreatMa.setDisSelect();
                    mTreatGManageUiClick.setDisSelect();
                    mAAManageUiClick.setDisSelect();
                }
                mAAManageUiClick.onClick();
                break;
            case R.id.filter_time:
                if (!mTimeManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                }
                mTimeManageUiClick.onClick();
                break;
            case R.id.filter_distance:
                if (!mDistanceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                }
                mDistanceManageUiClick.onClick();
                break;
            case R.id.filter_price:
                if (!mPriceManageUiClick.isclicked) {
                    mTimeManageUiClick.setDisSelect();
                    mDistanceManageUiClick.setDisSelect();
                    mPriceManageUiClick.setDisSelect();
                }
                mPriceManageUiClick.onClick();
                break;

        }
        if (a == 0) {
            ivChoose.setImageResource(R.drawable.dialog_cancle2x);
            ivCancle.setVisibility(View.GONE);
        }
        if (a == 1) {
            ivChoose.setImageResource(R.drawable.dialog_choose2x);
            ivCancle.setVisibility(View.VISIBLE);
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
