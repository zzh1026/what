package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.bean.NearByPeople;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.fragment.WhatFragment;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;

import java.util.HashMap;


/**
 * Created by Administrator on 2016/5/17.
 */
public class GvWhatAdapter extends BaseAdapter implements HttpLoader.ResponseListener {
    private Context mContext;
    private NearByPeople response;
    private WhatFragment whatFragment;
    private TranslateAnimation taLeft, taRight, taTop, taBlow;
    int i = 0;
    private PullToRefreshGridView mGv;
    //private ArrayList<View> arrayList = new ArrayList<>();
    private OnReFreshListener mOnFreshListener;

    public GvWhatAdapter(Context context, NearByPeople response, PullToRefreshGridView mGv, WhatFragment whatFragment) {
        this.mContext = context;
        this.response = response;
        this.mGv = mGv;
        this.whatFragment = whatFragment;

        InitAnima();
    }

    @Override
    public int getCount() {
        if (null != response && response.getData() != null && response.getData().getNearlyuser() != null) {
            return response.getData().getNearlyuser().size();
        }
        return 0;
    }

    @Override
    public NearByPeople.DataBean.NearlyuserBean getItem(int position) {
        return response.getData().getNearlyuser().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();

        } else {
            view = View.inflate(mContext, R.layout.item_gv, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

//        final Random ran = new Random();
//        int rand = ran.nextInt(4);
//        if (userAnimation) {
//            rand = 2;
//            switch (rand) {
//                case 0:
//                    view.startAnimation(taLeft);
//                    break;
//                case 1:
//                    view.startAnimation(taRight);
//                    break;
//                case 2:
////                    taTop.setStartOffset(position * 500);
//                    view.startAnimation(taTop);
//                    break;
//                case 3:
//                    view.startAnimation(taBlow);
//                    break;
//                default:
//                    break;
//            }
//        }

        holder.civUserLogo.setTag(position);
        final NearByPeople.DataBean.NearlyuserBean item = getItem(position);
        String url = item.getUser_thumbnailslogofile();
        if (!TextUtils.isEmpty(url)) {
//            HttpLoader.getImageLoader().get(url, ImageLoader.getImageListener(holder.civUserLogo,
//                    R.drawable.picture_moren, R.drawable.picture_moren, mContext, taTop, view));
            HttpLoader.getImageLoader().get(url, ImageLoader.getImageListener(holder.civUserLogo,
                    R.drawable.picture_moren, R.drawable.picture_moren));
        }

        holder.civUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NSMTypeUtils.isLogin()) {
                    ((MainActivity) mContext).showToastInfo("您尚未登录,请登录后重试");
                    return;
                }
                UserDetailActivity.startUserDetailAct(mContext, item.getUser_id(), false);
            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NSMTypeUtils.isLogin()) {
                    ((MainActivity) mContext).showToastInfo("您尚未登录,请登录后重试");
                    return;
                }

                HashMap params = new HashMap();
                params.put("token", NSMTypeUtils.getMyToken());
                params.put("targetId", item.getUser_id() + "");
                HttpLoader.post(ConstantsWhatNSM.URL_ADDFOCUS, params, SendSuccessResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS, GvWhatAdapter.this, false).setTag(this);

                response.getData().getNearlyuser().remove(position);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);
                view.startAnimation(scaleAnimation);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        view.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS &&
                response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                ((MainActivity) mContext).showToastSuccess("关注成功");
                if (this.response.getData().getNearlyuser().size() == 0) {
                    mOnFreshListener.onShouldReFresh();
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    static class ViewHolder {
        private CircleImageView civUserLogo;
        private ImageView ivPlus;


        public ViewHolder(View view) {
            civUserLogo = (CircleImageView) view.findViewById(R.id.civ_user_logo);
            ivPlus = (ImageView) view.findViewById(R.id.iv_plus);
        }


    }

    private void InitAnima() {
        // TODO Auto-generated method stub
        taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taLeft.setDuration(1000);
        taRight.setDuration(1000);
        taTop.setDuration(500);
        taBlow.setDuration(1000);
    }

    public void setOnReFreshListener(OnReFreshListener onFreshListener) {
        mOnFreshListener = onFreshListener;
    }

    public interface OnReFreshListener {
        void onShouldReFresh();
    }

//    private void deletePattern(final View view, final int position) {
//
//        Animation.AnimationListener al = new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
////                response.getData().getNearlyuser().remove(position);
////                GvWhatAdapter.this.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        };
//        collapse(view, al);
//
//    }
//
//    private void collapse(final View view, Animation.AnimationListener al) {
//        final int originWeight = view.getMeasuredWidth();
//
//        Animation animation = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1.0f) {
//                    view.setVisibility(View.GONE);
//                } else {
//                    view.getLayoutParams().width = originWeight - (int) (originWeight * interpolatedTime);
//                    view.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//        if (al != null) {
//            animation.setAnimationListener(al);
//        }
//        animation.setDuration(300);
//        view.startAnimation(animation);
//    }
}
