package com.neishenme.what.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.activity.FocusPeopleActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.RecognizedPeopleActivity;
import com.neishenme.what.adapter.GvWhatAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.NearByPeople;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.component.MutiComponents;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.highlight.view.Component;
import com.neishenme.what.view.highlight.view.Guide;
import com.neishenme.what.view.highlight.view.GuideBuilder;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

/**
 * 内谁,已过时
 */
@Deprecated
public class WhatFragment extends Fragment implements HttpLoader.ResponseListener, View.OnClickListener {

    private MainActivity mContext;
    private PullToRefreshGridView mGv;
    private GvWhatAdapter gvAdapter;
    private NearByPeople nearResponse;
    private RelativeLayout rlRecognizedPeople;
    private RelativeLayout rlFocusPeople;
    private ImageView mUserHeaderPic;

    //根据这个是否是点击进来的调用不同的方法
    public boolean isClickVisible = false;
    public boolean isStopPagerScrol = false;
    private int page = 1;

    private LinearLayout mLlMainWorningUnshow;
    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    public static WhatFragment newInstance() {
        WhatFragment fragment = new WhatFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isClickVisible) {
            if (isVisibleToUser && App.SP.getBoolean("show_function_guide_what_who", false)) {
                rlRecognizedPeople.post(new Runnable() {
                    @Override
                    public void run() {
                        showGuideView();
                    }
                });
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void tryShowFunction() {
        if (App.SP.getBoolean("show_function_guide_what_who", false)) {
            mUserHeaderPic.post(new Runnable() {
                @Override
                public void run() {
                    showGuideView();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_what, container, false);
        mGv = (PullToRefreshGridView) view.findViewById(R.id.pull_refresh_grid);
        mGv.setMode(PullToRefreshBase.Mode.BOTH);
        mGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                gvAdapter = null;
                page = 1;
                HashMap params = new HashMap();
                if (NSMTypeUtils.isLogin()) {
                    params.put("userId", NSMTypeUtils.getMyUserId());
                }
                params.put("longitude", LocationUtils.getLongitude(getActivity()) + "");
                params.put("latitude", LocationUtils.getLatitude(getActivity()) + "");
                params.put("page", "1");
                params.put("pageSize", "20");
                HttpLoader.post(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
                        ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, WhatFragment.this, false).setTag(this);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                HashMap params = new HashMap();
                if (NSMTypeUtils.isLogin()) {
                    params.put("userId", NSMTypeUtils.getMyUserId());
                }
                params.put("longitude", LocationUtils.getLongitude(getActivity()) + "");
                params.put("latitude", LocationUtils.getLatitude(getActivity()) + "");
                params.put("page", String.valueOf(page));
                params.put("pageSize", "20");
                HttpLoader.post(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
                        ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, WhatFragment.this, false).setTag(this);
            }

        });

        initListener(view);
        initData();
        return view;
    }

    private void initListener(View view) {
        rlRecognizedPeople = (RelativeLayout) view.findViewById(R.id.rl_recognized_people);
        rlFocusPeople = (RelativeLayout) view.findViewById(R.id.rl_focus_people);

        mLlMainWorningUnshow = (LinearLayout) view.findViewById(R.id.ll_main_worning_unshow);
        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        mUserHeaderPic = (ImageView) view.findViewById(R.id.user_header_pic);

        rlRecognizedPeople.setOnClickListener(this);
        rlFocusPeople.setOnClickListener(this);

    }

    private void initData() {

        HashMap params = new HashMap();
        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }
        params.put("longitude", LocationUtils.getLongitude(getActivity()) + "");
        params.put("latitude", LocationUtils.getLatitude(getActivity()) + "");
        params.put("page", "1");
        params.put("pageSize", "20");
        HttpLoader.get(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
                ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER &&
                response instanceof NearByPeople) {
            NearByPeople NearByPeople_down = (NearByPeople) response;
            if (NearByPeople_down.getCode() == 1) {
                mLlMainWorningUnshow.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);

                if (null == gvAdapter) {
                    nearResponse = NearByPeople_down;
                    gvAdapter = new GvWhatAdapter(getActivity(), nearResponse, mGv, this);
                    mGv.setAdapter(gvAdapter);
                } else {
                    nearResponse.getData().getNearlyuser().addAll(NearByPeople_down.getData().getNearlyuser());

                }

                gvAdapter.setOnReFreshListener(new GvWhatAdapter.OnReFreshListener() {
                    @Override
                    public void onShouldReFresh() {
                        page++;
                        gvAdapter = null;
                        HashMap params = new HashMap();
                        if (NSMTypeUtils.isLogin()) {
                            params.put("userId", NSMTypeUtils.getMyUserId());
                        }
                        params.put("longitude", LocationUtils.getLongitude(getActivity()) + "");
                        params.put("latitude", LocationUtils.getLatitude(getActivity()) + "");
                        params.put("page", String.valueOf(page));
                        params.put("pageSize", "20");
                        HttpLoader.post(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
                                ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, WhatFragment.this, false).setTag(this);
                    }
                });

                gvAdapter.notifyDataSetChanged();
            } else {
                mLlMainWorningUnshow.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(NearByPeople_down.getMessage());
            }
            mGv.onRefreshComplete();
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    public void showGuideView() {
        GuideBuilder builder = new GuideBuilder();
//        builder.setTargetView(mUserHeaderPic)
        builder.setTargetView(mUserHeaderPic)
                .setAlpha(220)
                .setHighTargetGraphStyle(Component.CIRCLE)
                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {

            }

            @Override
            public void onDismiss() {
                App.EDIT.remove("show_function_guide_what_who").commit();
            }
        });

        builder.addComponent(new MutiComponents());
        builder.setOverlayTarget(false);
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_focus_people:
                if (NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(getActivity(), FocusPeopleActivity.class);
                } else {
                    ActivityUtils.startActivity(getActivity(), LoginActivity.class);
                }
                break;
            case R.id.rl_recognized_people:
                if (NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(getActivity(), RecognizedPeopleActivity.class);
                } else {
                    ActivityUtils.startActivity(getActivity(), LoginActivity.class);
                }
                break;
        }
    }
}
