package com.neishenme.what.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.daimajia.androidanimations.library.YoYo;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.bean.HomePersonResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.fragment.HomePersonFragment;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.CardAdapter;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.RadiusImageViewTwo;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/5.
 */

public class HomePersonCardAdapter extends PagerAdapter implements CardAdapter, HttpLoader.ResponseListener {
    private MainActivity homeActivity;
    private HomePersonFragment mPersonFragment;
    private LayoutInflater mInflater;
    private List<HomePersonResponse.DataBean.NearlyuserBean> nearlyuser;
    private List<CardView> mViews;
    private float mBaseElevation;
    private ArrayList<String> list;

    private YoYo.AnimationComposer addFocus;

//    private Random ran = new Random();
//    int[] pictures = {
//            R.drawable.home_person_user_interest_bg1, R.drawable.home_person_user_interest_bg2,
//            R.drawable.home_person_user_interest_bg3, R.drawable.home_person_user_interest_bg4};

    private int mCurrentSelectedItem = -1;

    public HomePersonCardAdapter(MainActivity homeActivity, HomePersonFragment mPersonFragment, List<HomePersonResponse.DataBean.NearlyuserBean> nearlyuser) {
        this.homeActivity = homeActivity;
        mInflater = LayoutInflater.from(homeActivity);
        this.nearlyuser = nearlyuser;
        this.mPersonFragment = mPersonFragment;
        initMyViews();
    }

    private void initMyViews() {
        if (mViews != null) {
            mViews.clear();
        } else {
            mViews = new ArrayList<>();
        }
        if (nearlyuser != null && nearlyuser.size() != 0) {
            for (int i = 0; i < nearlyuser.size(); i++) {
                mViews.add(null);
            }
        }
    }

    public void setData(List<HomePersonResponse.DataBean.NearlyuserBean> nearlyuser) {
        this.nearlyuser = nearlyuser;
        initMyViews();
        notifyDataSetChanged();
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        if (nearlyuser == null || nearlyuser.size() == 0) {
            return 0;
        }
        return nearlyuser.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final CardView cardView;
        RadiusImageViewTwo homePersonUserLogo;
        ImageView homePersonUserStar;
        TextView homePersonUserName;
        ImageView homePersonUserGender;
        TextView homePersonUserAge;
        ImageView homePersonAddFocus;
        TextView homePersonInterest1;
        TextView homePersonInterest2;
        TextView homePersonInterest3;
        View view = mInflater.inflate(R.layout.itme_home_person_adapter, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        homePersonUserLogo = (RadiusImageViewTwo) view.findViewById(R.id.home_person_user_logo);
        homePersonUserStar = (ImageView) view.findViewById(R.id.home_person_user_star);
        homePersonUserName = (TextView) view.findViewById(R.id.home_person_user_name);
        homePersonUserGender = (ImageView) view.findViewById(R.id.home_person_user_gender);
        homePersonUserAge = (TextView) view.findViewById(R.id.home_person_user_age);
        homePersonAddFocus = (ImageView) view.findViewById(R.id.home_person_add_focus);
        homePersonInterest1 = (TextView) view.findViewById(R.id.home_person_interest_1);
        homePersonInterest2 = (TextView) view.findViewById(R.id.home_person_interest_2);
        homePersonInterest3 = (TextView) view.findViewById(R.id.home_person_interest_3);

        final HomePersonResponse.DataBean.NearlyuserBean nearlyuserBean = nearlyuser.get(position);
        String userThumbnailslogofile = nearlyuserBean.getUser_logofile();
        if (!TextUtils.isEmpty(userThumbnailslogofile)) {
            HttpLoader.getImageLoader().get(userThumbnailslogofile, ImageLoader.getImageListener(homePersonUserLogo,
                    R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            homePersonUserLogo.setImageResource(R.drawable.picture_moren);
        }

        homePersonUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NSMTypeUtils.isLogin()) {
//                    homeActivity.showToast(homeActivity.getString(R.string.user_should_login_suggest));
                    ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                    return;
                }
                UserDetailActivity.startUserDetailAct(homeActivity, nearlyuserBean.getUser_id(), false);
            }
        });

        switch (nearlyuserBean.getVitality()) {
            case 1:
                homePersonUserStar.setImageResource(R.drawable.home_person_hot_half);
                break;
            case 2:
                homePersonUserStar.setImageResource(R.drawable.home_person_hot_hot);
                break;
            case 0:
            default:
                homePersonUserStar.setImageResource(R.drawable.home_person_hot_normal);
                break;
        }

        homePersonUserName.setText(nearlyuserBean.getUsername());
        homePersonUserGender.setImageResource(nearlyuserBean.getUser_gender() == 1 ? R.drawable.man_icon : R.drawable.woman_icon);
        homePersonUserAge.setText(TimeUtils.getAge(nearlyuserBean.getBirthday()) + "");

        int relation = nearlyuserBean.getRelation();
        if (relation == 0) {
            homePersonAddFocus.setVisibility(View.VISIBLE);
        } else {
            homePersonAddFocus.setVisibility(View.INVISIBLE);
        }
        homePersonAddFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NSMTypeUtils.isLogin()) {
//                    homeActivity.showToast(homeActivity.getString(R.string.user_should_login_suggest));
                    ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                    return;
                }

                String user_id = String.valueOf(nearlyuserBean.getUser_id());
                if (TextUtils.equals(NSMTypeUtils.getMyUserId(), user_id)) {
                    homeActivity.showToastInfo(homeActivity.getString(R.string.home_person_cannot_focus_myself));
                    nearlyuserBean.setRelation(1);
                    notifyDataSetChanged();
                    return;
                }

                mCurrentSelectedItem = position;
                HashMap params = new HashMap();
                params.put("token", NSMTypeUtils.getMyToken());
                params.put("targetId", nearlyuserBean.getUser_id() + "");
                HttpLoader.post(ConstantsWhatNSM.URL_ADDFOCUS, params, SendSuccessResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS, HomePersonCardAdapter.this, false).setTag(this);

//                mPersonFragment.onAddFocus();
            }
        });

        HomePersonResponse.DataBean.NearlyuserBean.InterestsBean interests = nearlyuserBean.getInterests();
        list = new ArrayList<>();
        String food_name = interests.getFood_name();
        String music_singer = interests.getMusic_singer();
        String trip_name = interests.getTrip_name();

        if (!TextUtils.isEmpty(food_name)) {
            list.add(food_name);
        }
        if (!TextUtils.isEmpty(music_singer)) {
            list.add(music_singer);
        }
        if (!TextUtils.isEmpty(trip_name)) {
            list.add(trip_name);
        }

        homePersonInterest1.setVisibility(View.INVISIBLE);
        homePersonInterest2.setVisibility(View.INVISIBLE);
        homePersonInterest3.setVisibility(View.INVISIBLE);

        switch (list.size()) {
            case 3:
                homePersonInterest3.setVisibility(View.VISIBLE);
                homePersonInterest3.setText(list.get(2));
//                homePersonInterest3.setBackgroundResource(getRan());
            case 2:
                homePersonInterest2.setVisibility(View.VISIBLE);
                homePersonInterest2.setText(list.get(1));
//                homePersonInterest2.setBackgroundResource(getRan());
            case 1:
                homePersonInterest1.setVisibility(View.VISIBLE);
                homePersonInterest1.setText(list.get(0));
//                homePersonInterest1.setBackgroundResource(getRan());
                break;
            default:
                break;
        }

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void addFocus(int mCurrentSelectedItem) {
        nearlyuser.get(mCurrentSelectedItem).setRelation(1);
        notifyDataSetChanged();
    }

//    private int getRan() {
//        return pictures[ran.nextInt(pictures.length)];
//    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                homeActivity.showToastSuccess("添加关注成功");
                addFocus(mCurrentSelectedItem);
            } else {
                homeActivity.showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
