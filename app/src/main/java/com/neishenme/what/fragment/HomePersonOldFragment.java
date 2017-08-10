package com.neishenme.what.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.adapter.HomePersonCardAdapter;
import com.neishenme.what.adapter.ShadowTransformer;
import com.neishenme.what.bean.NearByPeople;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;

import org.seny.android.utils.ALog;

import jp.wasabeef.blurry.Blurry;

/**
 * 旧的 内谁界面 ,因为对 viewpager的选择进行了新的改造,放弃了大量的代码,但是放弃的代码怕出问题,所以copy了一份
 *
 * @see HomePersonFragment 的代码,然后将那个界面的注释代码全部删掉了,如果那边出了问题在这里相应的可以拿到
 */
@Deprecated
public class HomePersonOldFragment extends Fragment implements HttpLoader.ResponseListener, View.OnClickListener {
    public static final int SHOULD_GET_HEAD_BG = 0;     //标记应该刷新新的头像背景图
    public static final int SHOULD_SHOW_HEAD_BG = 1;    //标记应该显示头像北京图

    public static final int IS_NORMAL = 10;    //普通情况
    public static final int IS_FALING = 14;    //正在滑动
    public static final int IS_HALF = 11;       //距离为一半
    public static final int IS_MORE_HALF = 12;    //距离超过一半
    public static final int IS_LESS_HALF = 13;    //距离不到一半

    public int mCurrentOffest = IS_NORMAL;    //即时的距离
    private boolean isSetGoLeft = false;    //标滑动方向是否改变
    private int isSetOffest = 0;    //标滑动方向是否改变


    private MainActivity mContext;

    private int page = 1;

    private AlphaAnimation alphaAnimation;

    private LinearLayout mLlMainWorningUnshow;
    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private ImageView mHomePersonHeaderBg;

    private LinearLayout mHomePersonUserInfoLl;
    private TextView mHomePersonUserName;
    private TextView mHomePersonUserSign;
    private TextView mHomePersonUserActiveTime;

    private ViewPager mPersonFragCardViewpager;

    private int mDownOffest;
    private int oldPage = 0; //标记旧滑动之前的page页数
    private int mCurrentSelectedPage = 0; //标记滑动后展示的page页数
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case SHOULD_GET_HEAD_BG:
//                    HttpLoader.getImageLoader().get(url, ImageLoader.getImageListener(mHomePersonHeaderBg,
//                            R.drawable.picture_moren, R.drawable.picture_moren));
//                    break;
//                case SHOULD_SHOW_HEAD_BG:
//                    mHomePersonHeaderBg.clearAnimation();
//                    mHomePersonHeaderBg.setAnimation(alphaAnimation);
//                    showUserHeader();
//                    break;
            }
        }
    };


    private String url;
    public static String url1 = "http://192.168.3.99:8888/images/637aff257cab4392aa20f3d2804182e7/source.jpg";
    public static String url2 = "http://192.168.3.99:8888/users/180/photos/5e58cc32-42be-44ca-9229-e2b4c8512f7a_57600.jpg";
    public static String url3 = "http://192.168.3.99:8888/users/86/photos/48f6012c-a717-424f-9074-8b4a54703c21_57600.jpg";
    private ImageView imageView;

    public static HomePersonOldFragment newInstance() {
        HomePersonOldFragment fragment = new HomePersonOldFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
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
        mHomePersonUserInfoLl = (LinearLayout) view.findViewById(R.id.home_person_user_info_ll);
        mHomePersonUserName = (TextView) view.findViewById(R.id.home_person_user_name);
        mHomePersonUserSign = (TextView) view.findViewById(R.id.home_person_user_sign);
        mHomePersonUserActiveTime = (TextView) view.findViewById(R.id.home_person_user_active_time);

        mPersonFragCardViewpager = (ViewPager) view.findViewById(R.id.person_frag_card_viewpager);

        HomePersonCardAdapter homePersonCardAdapter = new HomePersonCardAdapter(mContext, null,null);
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(mPersonFragCardViewpager, homePersonCardAdapter);

        mPersonFragCardViewpager.setAdapter(homePersonCardAdapter);
        mPersonFragCardViewpager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                15, getResources().getDisplayMetrics()));
        mPersonFragCardViewpager.setPageTransformer(false, mCardShadowTransformer);
        mPersonFragCardViewpager.setOffscreenPageLimit(3);


    }

    private void initListener() {
        mPersonFragCardViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDownOffest = mPersonFragCardViewpager.getScrollX();
                    isSetGoLeft = false;
                    isSetOffest = 0;
                }
                return false;
            }
        });

        mPersonFragCardViewpager
                .addOnPageChangeListener(
                        new ViewPager.OnPageChangeListener() {

                            private float aFloat;
                            private float alphaNum;
                            private boolean isGoLeft;     //是否向左滑动

                            @Override
                            public void onPageScrolled(int i, float v, int i1) {
                                ALog.i("当前的页面为:" + i + "-------当前百分比为:" + v + "--------当钱像素为:" + i1);


                                mHandler.removeMessages(1);
                                //是否向左滑动,向左滑动界面向右
                                aFloat = v * 2;
                                alphaNum = Math.abs(aFloat - 1);
                                isGoLeft = mDownOffest < mPersonFragCardViewpager.getScrollX();

                                if (v >= 0.5 && v < 0.9) {
                                    refreshData(i + 1);
                                } else if (0.1 < v && v < 0.5) {
                                    refreshData(i);
                                }

//                                if (isGoLeft) {
//                                    if (v > 0.5 && 0.9 > v)
//                                        refreshData(i + 1);
//                                    else if (0.1 < v && v < 0.5)
//                                        refreshData(i);
//                                } else {
//                                    if (0.1 < v && v < 0.5)
//                                        refreshData(i);
//                                    else if (0.5 < v && v < 0.9)
//                                        refreshData(i + 1);
//                                }

                                if (aFloat >= 0.98 && aFloat < 1.02) {
                                    mCurrentOffest = IS_HALF;
                                } else if (aFloat <= 0.2 || aFloat >= 1.8) {
                                    mCurrentOffest = IS_NORMAL;
                                } else {
                                    mCurrentOffest = IS_FALING;
                                }

                                if (isGoLeft) { //向右走 ,界面向左
                                    switch (mCurrentOffest) {
                                        case IS_HALF:
                                            mHomePersonHeaderBg.setAlpha(0f);
                                            mHomePersonUserInfoLl.setAlpha(0f);
                                            break;
                                        case IS_FALING:
                                            mHomePersonHeaderBg.setAlpha(alphaNum);
                                            mHomePersonUserInfoLl.setAlpha(alphaNum);
                                            break;
                                        case IS_NORMAL:
                                        default:
                                            mHomePersonHeaderBg.setAlpha(1f);
                                            mHomePersonUserInfoLl.setAlpha(1f);
                                            break;
                                    }
                                } else {    //左划,画面向右
                                    switch (mCurrentOffest) {
                                        case IS_HALF:
                                            mHomePersonHeaderBg.setAlpha(0f);
                                            mHomePersonUserInfoLl.setAlpha(0f);
                                            break;
                                        case IS_FALING:
                                            mHomePersonHeaderBg.setAlpha(alphaNum);
                                            mHomePersonUserInfoLl.setAlpha(alphaNum);
                                            break;
                                        case IS_NORMAL:
                                        default:
                                            mHomePersonHeaderBg.setAlpha(1f);
                                            mHomePersonUserInfoLl.setAlpha(1f);
                                            break;
                                    }

//                    if (aFloat >= 0.98 && aFloat < 1.02) {
//                        mCurrentOffest = IS_HALF;
//                    } else if (aFloat >= 1.02 && aFloat < 1.9) {
//                        mCurrentOffest = IS_MORE_HALF;
//                    } else if (aFloat >= 0.1 && aFloat < 0.98) {
//                        mCurrentOffest = IS_LESS_HALF;
//                    } else {
//                        mCurrentOffest = IS_NORMAL;
//                    }
//                    if (isGoLeft) {
//                        //向右走: 从 1到0
//                        switch (mCurrentOffest) {
//                            case IS_HALF:
//                                mHomePersonHeaderBg.setAlpha(0f);
//                                mHomePersonUserInfoLl.setAlpha(0f);
//                                break;
//                            case IS_MORE_HALF:
//                                refreshData(i, isGoLeft, mCurrentOffest);
//                                mHomePersonHeaderBg.setAlpha(0f);
//                                mHomePersonUserInfoLl.setAlpha(alphaNum);
//                                break;
//                            case IS_LESS_HALF:
//                                refreshData(i, isGoLeft, mCurrentOffest);
//                                mHomePersonHeaderBg.setAlpha(alphaNum);
//                                mHomePersonUserInfoLl.setAlpha(alphaNum);
//                                break;
//                            case IS_NORMAL:
//                            default:
////                            mHomePersonHeaderBg.setAlpha(1f);
//                                mHomePersonUserInfoLl.setAlpha(1f);
//                                break;
//                        }
//                    } else {
//                        //向左走 :从1到0
//                        switch (mCurrentOffest) {
//                            case IS_HALF:
//                                mHomePersonHeaderBg.setAlpha(0f);
//                                mHomePersonUserInfoLl.setAlpha(0f);
//                                break;
//                            case IS_MORE_HALF:
//                                refreshData(i, isGoLeft, mCurrentOffest);
//                                mHomePersonHeaderBg.setAlpha(alphaNum);
//                                mHomePersonUserInfoLl.setAlpha(alphaNum);
//                                break;
//                            case IS_LESS_HALF:
//                                refreshData(i, isGoLeft, mCurrentOffest);
//                                mHomePersonHeaderBg.setAlpha(0f);
//                                mHomePersonUserInfoLl.setAlpha(alphaNum);
//                                break;
//                            case IS_NORMAL:
//                            default:
////                            mHomePersonHeaderBg.setAlpha(1f);
//                                mHomePersonUserInfoLl.setAlpha(1f);
//                                break;
//                        }
                                }
                            }

                            @Override
                            public void onPageSelected(int i) {
//                mCurrentSelectedPage = i;
//                if (i % 3 == 0) {
//                    url = url1;
//                } else if (i % 3 == 1) {
//                    url = url2;
//                } else {
//                    url = url3;
//                }
//                mHandler.sendEmptyMessageDelayed(SHOULD_GET_HEAD_BG, 200);
                            }

                            @Override
                            public void onPageScrollStateChanged(int i) {
//                if (i == 0 && oldPage != mCurrentSelectedPage) {
//                    Blurry.with(mContext).async().radius(20).capture(mHomePersonHeaderBg).into(mHomePersonHeaderBg);
//                    mHandler.sendEmptyMessage(SHOULD_SHOW_HEAD_BG);
//                    oldPage = mCurrentSelectedPage;
//                }
                            }
                        }

                );
    }

    private void initData() {
        initAnimation();
        imageView = new ImageView(getActivity());

//        mHomePersonHeaderBg.setAlpha(0f);
        HttpLoader.getImageLoader().get(url1, ImageLoader.getImageListener(imageView,
                R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response) {
                        Blurry.with(mContext).async().radius(20).capture(mHomePersonHeaderBg).into(mHomePersonHeaderBg);
                    }
                }));
        mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_HEAD_BG, 500);
    }

    private void initAnimation() {
        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        mHomePersonHeaderBg.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHomePersonHeaderBg.setAlpha(1f);
                mHomePersonUserInfoLl.setAlpha(1f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showUserHeader() {
        alphaAnimation.start();
    }

    public void refreshData(int selectedItem) {
        if (mCurrentSelectedPage == selectedItem) {
            return;
        }

        mHomePersonUserName.setText("显示第:" + selectedItem + "人的名字");
        mHomePersonUserSign.setText("显示第:" + selectedItem + "人的签名");
        mHomePersonUserActiveTime.setText("显示第:" + selectedItem + "人的时间");

        if (selectedItem % 3 == 0) {
            url = url1;
        } else if (selectedItem % 3 == 1) {
            url = url2;
        } else {
            url = url3;
        }
        HttpLoader.getImageLoader().get(url, ImageLoader.getImageListener(imageView,
                R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response) {
                        Blurry.with(mContext).async().radius(20).from(response.getBitmap()).into(mHomePersonHeaderBg);
                    }
                }));


        mCurrentSelectedPage = selectedItem;
    }

    public void refreshData(int selectedItem, boolean isGoLeft, int mCurrentOffest) {
        if (isSetGoLeft == isGoLeft && isSetOffest == mCurrentOffest) {
            return;
        }

        if (isGoLeft) { //左划,显示下一个 selectedItem+1
            if (mCurrentOffest == IS_LESS_HALF) {   //显示这个
                if (mCurrentSelectedPage == selectedItem) {
                    mHomePersonUserName.setText("这个的名字");
                    mHomePersonUserSign.setText("这个的签名");
                    mHomePersonUserActiveTime.setText("这个的时间");
                }
            } else {        //显示下一个
                mHomePersonUserName.setText("下一个的名字");
                mHomePersonUserSign.setText("下一个的签名");
                mHomePersonUserActiveTime.setText("下一个的时间");
            }
        } else {    //右划,显示上一个, selectedItem-1
            if (mCurrentOffest == IS_LESS_HALF) {   //显示上一个
                mHomePersonUserName.setText("上一个的名字");
                mHomePersonUserSign.setText("上一个的签名");
                mHomePersonUserActiveTime.setText("上一个的时间");
            } else {    //多,说明没有划到上一个,显示这个
                if (mCurrentSelectedPage == selectedItem) {
                    mHomePersonUserName.setText("这个的名字");
                    mHomePersonUserSign.setText("这个的签名");
                    mHomePersonUserActiveTime.setText("这个的时间");
                }
            }
        }

        isSetGoLeft = isGoLeft;
        isSetOffest = mCurrentOffest;

    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER &&
                response instanceof NearByPeople) {
            NearByPeople NearByPeople_down = (NearByPeople) response;
            if (NearByPeople_down.getCode() == 1) {
                mLlMainWorningUnshow.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);
//                if (null == gvAdapter) {
//                    nearResponse = NearByPeople_down;
////                    gvAdapter = new GvWhatAdapter(getActivity(), nearResponse, mGv, this);
//                    mGv.setAdapter(gvAdapter);
//                } else {
//                    nearResponse.getData().getNearlyuser().addAll(NearByPeople_down.getData().getNearlyuser());
//
//                }
//
//                gvAdapter.setOnReFreshListener(new GvWhatAdapter.OnReFreshListener() {
//                    @Override
//                    public void onShouldReFresh() {
//                        page++;
//                        gvAdapter = null;
//                        HashMap params = new HashMap();
//                        if (NSMTypeUtils.isLogin()) {
//                            params.put("userId", NSMTypeUtils.getMyUserId());
//                        }
//                        params.put("longitude", LocationUtils.getLongitude(getActivity()) + "");
//                        params.put("latitude", LocationUtils.getLatitude(getActivity()) + "");
//                        params.put("page", String.valueOf(page));
//                        params.put("pageSize", "20");
//                        HttpLoader.post(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
//                                ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, HomePersonFragment.this, false).setTag(this);
//                    }
//                });
//
//                gvAdapter.notifyDataSetChanged();
            } else {
                mLlMainWorningUnshow.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(NearByPeople_down.getMessage());
            }
//            mGv.onRefreshComplete();
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_focus_people:
//                if (NSMTypeUtils.isLogin()) {
//                    ActivityUtils.startActivity(getActivity(), FocusPeopleActivity.class);
//                } else {
//                    ActivityUtils.startActivity(getActivity(), LoginActivity.class);
//                }
                break;
            case R.id.rl_recognized_people:
//                if (NSMTypeUtils.isLogin()) {
//                    ActivityUtils.startActivity(getActivity(), RecognizedPeopleActivity.class);
//                } else {
//                    ActivityUtils.startActivity(getActivity(), LoginActivity.class);
//                }
                break;
        }
    }
}
