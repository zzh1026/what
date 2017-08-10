package com.neishenme.what.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.adapter.HomePersonCardAdapter;
import com.neishenme.what.adapter.ShadowTransformer;
import com.neishenme.what.bean.HomePersonResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.ViewPagerGetFocus;

import org.seny.android.utils.AppInfoUtil;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * 旧的 主界面内谁模块, 现在已经弃用  2017/3/16
 */

@Deprecated
public class HomePersonFragment extends Fragment implements HttpLoader.ResponseListener, ViewPager.OnPageChangeListener {

//    public static final int IS_NORMAL = 10;    //普通情况
//    public static final int IS_FALING = 14;    //正在滑动
//    public static final int IS_HALF = 11;       //距离为一半
//
//    public int mCurrentOffest = IS_NORMAL;    //即时的距离

    private MainActivity homeActivity;

    private LinearLayout mLlMainWorningUnshow;
    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private ImageView mHomePersonZhanwei;
    private ImageView mHomePersonHeaderBg;

    private LinearLayout mHomePersonUserInfoLl;
    private TextView mHomePersonUserName;
    private TextView mHomePersonUserSign;
    private TextView mHomePersonUserActiveTime;

    private ImageView mHomePersonLoadAgain;

    private ViewPagerGetFocus mPersonFragCardViewpager;

    private RelativeLayout mHomeNoNetShow;
    private ImageView mHomeNetLoadAgain;

    private LinearLayout mHomePersonNoPeople;

    private int mCurrentSelectedPage = 0; //标记滑动后展示的page页数
    private int page = 1;

    private HomePersonCardAdapter homePersonCardAdapter;    //适配器
    private ShadowTransformer mCardShadowTransformer;
    private List<HomePersonResponse.DataBean.NearlyuserBean> nearlyuser;    //所有需要显示的数据
    private boolean isHasMore;  //标记是否有更多数据

    public static HomePersonFragment newInstance() {
        HomePersonFragment fragment = new HomePersonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mLlMainWorningUnshow = (LinearLayout) view.findViewById(R.id.ll_main_worning_unshow);
        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        mHomePersonHeaderBg = (ImageView) view.findViewById(R.id.home_person_header_bg);
        mHomePersonZhanwei = (ImageView) view.findViewById(R.id.home_person_zhanwei);
        mHomePersonUserInfoLl = (LinearLayout) view.findViewById(R.id.home_person_user_info_ll);
        mHomePersonUserName = (TextView) view.findViewById(R.id.home_person_user_name);
        mHomePersonUserSign = (TextView) view.findViewById(R.id.home_person_user_sign);
        mHomePersonUserActiveTime = (TextView) view.findViewById(R.id.home_person_user_active_time);

        mPersonFragCardViewpager = (ViewPagerGetFocus) view.findViewById(R.id.person_frag_card_viewpager);

        mHomeNoNetShow = (RelativeLayout) view.findViewById(R.id.home_no_net_show);
        mHomeNetLoadAgain = (ImageView) view.findViewById(R.id.home_net_load_again);
        mHomePersonLoadAgain = (ImageView) view.findViewById(R.id.home_person_load_again);

        mHomePersonNoPeople = (LinearLayout) view.findViewById(R.id.home_person_no_people);

    }

    private void initListener() {
        mPersonFragCardViewpager.addOnPageChangeListener(this);

        mHomeNetLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNearPerson();
                if (CityLocationConfig.cityLocationId == 0) {
                    homeActivity.getLocation();
                } else {
                    homeActivity.upShowCity();
                }
            }
        });

        mPersonFragCardViewpager.setOnScrollTouchListener(new ViewPagerGetFocus.OnScrollTouchListener() {
            @Override
            public void onScrollTouchDown() {
                homeActivity.setPagingEnabled(false);
            }

            @Override
            public void onScrollTouchUp() {
                homeActivity.setPagingEnabled(true);
            }
        });

        mHomePersonLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                homePersonCardAdapter = null;
                getNearPerson();
            }
        });
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();

//        mPersonFragCardViewpager.setPadding(AppInfoUtil.getScreenWidth(homeActivity)/5,
//                (int) getResources().getDimension(R.dimen.margin_10),
//                AppInfoUtil.getScreenWidth(homeActivity)/5,0);

        //获取附近的用户
        getNearPerson();
    }

    private void getNearPerson() {
        String areaId;
        try {
            areaId = String.valueOf(CityLocationConfig.cityLocationId);
        } catch (Exception e) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }
        params.put("areaId", areaId);
        params.put("page", page + "");
        params.put("pageSize", "10");
        HttpLoader.post(ConstantsWhatNSM.URL_HOME_PERSONS, params, HomePersonResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME_PERSONS, HomePersonFragment.this, false).setTag(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
//        float aFloat = v * 2;
//        float alphaNum = Math.abs(aFloat - 1);

        if (v >= 0.5 && v < 0.9) {
            refreshData(i + 1);
        } else if (0.1 < v && v < 0.5) {
            refreshData(i);
        }

//        if (aFloat >= 0.98 && aFloat < 1.02) {
//            mCurrentOffest = IS_HALF;
//        } else if (aFloat <= 0.2 || aFloat >= 1.8) {
//            mCurrentOffest = IS_NORMAL;
//        } else {
//            mCurrentOffest = IS_FALING;
//        }

        //这里只需要控制显隐即可,不需要关心是朝那个方向移动
//        switch (mCurrentOffest) {
//            case IS_HALF:
//                mHomePersonHeaderBg.setAlpha(0f);
//                mHomePersonUserInfoLl.setAlpha(0f);
//                break;
//            case IS_FALING:
//                mHomePersonHeaderBg.setAlpha(alphaNum);
//                mHomePersonUserInfoLl.setAlpha(alphaNum);
//                break;
//            case IS_NORMAL:
//            default:
//                mHomePersonHeaderBg.setAlpha(1f);
//                mHomePersonUserInfoLl.setAlpha(1f);
//                break;
//        }
    }

    @Override
    public void onPageSelected(int i) {
        if (!isHasMore) {
            return;
        }
        if (nearlyuser != null && nearlyuser.size() - i <= 3) {
            page++;
            getNearPerson();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //城市选择完毕后重新刷新数据
    public void notifyData() {
//        homePersonCardAdapter = null;
//        page = 1;
//        mCurrentSelectedPage = 0;
//        getNearPerson();
    }

    //添加关注有去掉的动画然后刷新数据
    public void onAddFocus() {
//        mCurrentSelectedPage--;
//        int currentItem = mPersonFragCardViewpager.getCurrentItem();
//        mPersonFragCardViewpager.setCurrentItem(currentItem, false);
//        nearlyuser.remove(currentItem);
//        homePersonCardAdapter.notifyDataSetChanged();
//        refreshData(currentItem);
    }

    public int getcurrentPosition() {
        return mPersonFragCardViewpager.getCurrentItem();
    }

    private void setPositionData(int position) {
        if (mCurrentSelectedPage == position) {
            mCurrentSelectedPage = -1;
            refreshData(position);
        }
    }

    public void refreshData(int selectedItem) {
        if (mCurrentSelectedPage == selectedItem) {
            return;
        }

        if (nearlyuser == null || nearlyuser.size() == 0) {
            return;
        }

        HomePersonResponse.DataBean.NearlyuserBean nearlyuserBean = nearlyuser.get(selectedItem);
        mHomePersonUserName.setText(nearlyuserBean.getUsername());
        mHomePersonUserSign.setText(nearlyuserBean.getUsersign());
        mHomePersonUserActiveTime.setText(TimeUtils.getLastTimeBefore(nearlyuserBean.getLastlogintime()));

        String userLogofile = nearlyuserBean.getUser_logofile();
        if (!TextUtils.isEmpty(userLogofile)) {
            HttpLoader.getImageLoader().get(userLogofile, ImageLoader.getImageListener(mHomePersonZhanwei,
                    R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response) {
                            Blurry.with(homeActivity).radius(17).sampling(4).async().from(response.getBitmap()).into(mHomePersonHeaderBg);
                        }
                    }));
        } else {
            mHomePersonHeaderBg.setImageResource(R.drawable.picture_moren);
        }
        mCurrentSelectedPage = selectedItem;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mHomeNoNetShow.setVisibility(View.GONE);

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME_PERSONS &&
                response instanceof HomePersonResponse) {
            HomePersonResponse homePersonResponse = (HomePersonResponse) response;
            if (homePersonResponse.getCode() == 1) {
                mLlMainWorningUnshow.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);

                isHasMore = homePersonResponse.getData().isHasMore();
                if (homePersonCardAdapter == null) {
                    nearlyuser = homePersonResponse.getData().getNearlyuser();
                    homePersonCardAdapter = new HomePersonCardAdapter(homeActivity, this, nearlyuser);
                    mCardShadowTransformer = new ShadowTransformer(mPersonFragCardViewpager, homePersonCardAdapter);
                    mPersonFragCardViewpager.setAdapter(homePersonCardAdapter);
                    //判断Fragment是否Attach到Activity，使用isAdded()方法。
                    if(isAdded()) {
                        mPersonFragCardViewpager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                15, getResources().getDisplayMetrics()));
                    }
                    mPersonFragCardViewpager.setPageTransformer(false, mCardShadowTransformer);
                    mPersonFragCardViewpager.setOffscreenPageLimit(3);
                    setPositionData(0);
                } else {
                    nearlyuser.addAll(homePersonResponse.getData().getNearlyuser());
                    homePersonCardAdapter.setData(nearlyuser);
                }

                if (null != nearlyuser && 0 != nearlyuser.size()) {
                    mHomePersonUserInfoLl.setVisibility(View.VISIBLE);
                    mHomePersonNoPeople.setVisibility(View.INVISIBLE);
                } else {
                    mHomePersonUserInfoLl.setVisibility(View.INVISIBLE);
                    mHomePersonNoPeople.setVisibility(View.VISIBLE);
                }
            } else {
                mHomePersonUserInfoLl.setVisibility(View.INVISIBLE);
                mLlMainWorningUnshow.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(homePersonResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME_PERSONS) {
            homeActivity.showToastError("网络连接失败,请检查您的网络连接");
            if (nearlyuser == null || nearlyuser.size() == 0) {
                mHomePersonUserInfoLl.setVisibility(View.INVISIBLE);
                mHomeNoNetShow.setVisibility(View.VISIBLE);
            }
        }
    }
}
