package com.neishenme.what.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.adapter.HomePublishServiceAdapter;
import com.neishenme.what.bean.GetServiceListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.component.HomeReleaseComponent;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.view.highlight.view.Guide;
import com.neishenme.what.view.highlight.view.GuideBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * 年终版本的发布界面
 *
 * 旧的主界面发布模块, 已弃用 2017/3/16
 */

@Deprecated
public class HomeReleaseFragment extends Fragment implements HttpLoader.ResponseListener {
    private MainActivity homeActivity;

    private RelativeLayout mHomeReleaseHeader;
    private RadioGroup mHomeReleaseMenu;
    private RadioButton mHomeReleaseQuick;
    private RadioButton mHomeReleaseNormal;
    private FrameLayout mHomeReleaseContainer;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private ImageView mShouFunctionReleaseIv;

    private FragmentManager fragmentManager;    //fragment的控制器
    private ReleaseQuickFragment quickFragment;
    private ReleaseNormalFragment normalFragment;

    public HomeReleaseFragment() {
    }

    public static HomeReleaseFragment newInstance() {
        HomeReleaseFragment fragment = new HomeReleaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (MainActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_release, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mHomeReleaseHeader = (RelativeLayout) view.findViewById(R.id.home_release__header);
        mHomeReleaseMenu = (RadioGroup) view.findViewById(R.id.home_release_menu);
        mHomeReleaseQuick = (RadioButton) view.findViewById(R.id.home_release_quick);
        mHomeReleaseNormal = (RadioButton) view.findViewById(R.id.home_release_normal);
        mHomeReleaseContainer = (FrameLayout) view.findViewById(R.id.home_release_container);

        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);
        mShouFunctionReleaseIv = (ImageView) view.findViewById(R.id.shou_function_release_iv);
    }

    private void initData() {
        quickFragment = ReleaseQuickFragment.newInstance();
        normalFragment = ReleaseNormalFragment.newInstance();

        homeActivity = (MainActivity) getActivity();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.home_release_container, quickFragment)
                .add(R.id.home_release_container, normalFragment)
                .show(quickFragment)
                .hide(normalFragment)
                .commit();

        getCityOpenInfo();
    }

    public ReleaseQuickFragment getQuickFragment() {
        return quickFragment;
    }

    public ReleaseNormalFragment getNormalFragment() {
        return normalFragment;
    }

    public void stopEndPlay() {
        if (quickFragment != null) {
            quickFragment.stopEndPlay();
        }
        if (normalFragment != null) {
            normalFragment.stopEndPlay();
        }
    }

    public void getCityOpenInfo() {
        String areaId;
        try {
            areaId = String.valueOf(homeActivity.showCityAreaId);
            if ("0".equals(areaId)) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("areaId", areaId);
        params.put("page", "1");
        params.put("pageSize", "1");
        HttpLoader.post(ConstantsWhatNSM.URL_SERVICE_LIST, params, GetServiceListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST, this).setTag(this);
    }

    public void showGuideView() {
        mShouFunctionReleaseIv.setVisibility(View.VISIBLE);
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(mShouFunctionReleaseIv)
                .setAlpha(214)
                .setHighTargetCorner(2)
//                .setHighTargetPadding(5)
                .setOverlayTarget(false)
                .setOutsideTouchable(false)
                .setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {

                    }

                    @Override
                    public void onDismiss() {
                        mShouFunctionReleaseIv.setVisibility(View.GONE);
                    }
                });
        builder.addComponent(new HomeReleaseComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(homeActivity);
    }

    private void initListener() {
        mHomeReleaseMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (quickFragment == null || normalFragment == null) {
                    quickFragment = ReleaseQuickFragment.newInstance();
                    normalFragment = ReleaseNormalFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .add(R.id.home_release_container, quickFragment)
                            .add(R.id.home_release_container, normalFragment)
                            .show(quickFragment)
                            .hide(normalFragment)
                            .commit();
                }
                if (quickFragment == null || normalFragment == null) {
                    return;
                }
                switch (checkedId) {
                    case R.id.home_release_quick:
                        fragmentManager.beginTransaction().hide(normalFragment).show(quickFragment).commit();
                        break;
                    case R.id.home_release_normal:
                        fragmentManager.beginTransaction().hide(quickFragment).show(normalFragment).commit();
                        normalFragment.getService();
                        break;
                }
            }
        });
    }

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST
                && response instanceof GetServiceListResponse) {
            GetServiceListResponse getServiceListResponse = (GetServiceListResponse) response;
            if (getServiceListResponse.getCode() == 1) {
                if (!getServiceListResponse.getData().isCityOpen()) {
                    mHomeReleaseQuick.setChecked(true);
                    fragmentManager.beginTransaction().hide(normalFragment).show(quickFragment).commit();
                    mHomeReleaseHeader.setVisibility(View.GONE);
                } else {
                    normalFragment.notifyData();
                    mHomeReleaseHeader.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
