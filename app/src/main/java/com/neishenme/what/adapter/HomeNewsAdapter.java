package com.neishenme.what.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.HomeNewsActivity;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.MainPushADActivity;
import com.neishenme.what.activity.MyActiveActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.activity.VIPCenterActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.HomeNewsInfoBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import static com.neishenme.what.R.id.fab_crop;
import static com.neishenme.what.R.id.view;

/**
 * 作者：zhaozh create on 2016/3/9 15:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个消费记录的适配器类
 * .
 * 其作用是 :
 * 为消费记录的界面显示进行适配.
 */
public class HomeNewsAdapter extends BaseAdapter {
    private List<HomeNewsInfoBean> mExpenseInfos;
    private HomeNewsActivity mActivity;
    private LayoutInflater mInflater;

    public HomeNewsAdapter(HomeNewsActivity activity, List<HomeNewsInfoBean> messages) {
        mExpenseInfos = messages;
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void setHomeRes(List<HomeNewsInfoBean> mExpenseInfos) {
        this.mExpenseInfos = mExpenseInfos;
        notifyDataSetChanged();
    }

    public void removeInfo(int position) {
        if (mExpenseInfos.size() > position) {
            mExpenseInfos.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (mExpenseInfos != null && mExpenseInfos.size() != 0) {
            return mExpenseInfos.size();
        }
        return 0;
    }

    @Override
    public HomeNewsInfoBean getItem(int position) {
        return mExpenseInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tv;
        View view;
        TextView homeNewsTvData;
        TextView homeNewsTvTime;
        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
        } else {
            view = mInflater.inflate(R.layout.item_home_news, parent, false);
        }
        tv = (TextView) view.findViewById(R.id.tv_news_home);
        homeNewsTvData = (TextView) view.findViewById(R.id.home_news_tv_data);
        homeNewsTvTime = (TextView) view.findViewById(R.id.home_news_tv_time);
        final HomeNewsInfoBean item = getItem(position);


        tv.setText(item.getContext());
        long l = Long.parseLong(item.getEndtime());
        if (TimeUtils.isToday(l)) {
            homeNewsTvData.setText("今天");
        } else {
            homeNewsTvData.setText(TimeUtils.getTime(l, TimeUtils.DATE_FORMAT_HOME_NEWS));
        }
        homeNewsTvTime.setText(TimeUtils.getTime(l));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.getId();
                mActivity.lookInfoForId(id, position);
                String type = item.getType();
                if ("ad".equals(type)) {
                    String url;
                    if (NSMTypeUtils.isLogin()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("token", NSMTypeUtils.getMyToken());
                        url = item.getLink() + HttpLoader.buildGetParam(params, item.getLink());
                    } else {
                        url = item.getLink();
                    }
                    Intent intent = new Intent(mActivity, MainPushADActivity.class);
                    intent.putExtra(MainPushADActivity.AD_DATA, url);
                    intent.putExtra(MainPushADActivity.AD_TYPE, 2);
                    mActivity.startActivity(intent);
                } else if ("invite".equals(type)) {
                    String inviteId = item.getInviteid().trim();
                    if (!TextUtils.isEmpty(inviteId) && !inviteId.equals("null")) {
                        if (NSMTypeUtils.isMyUserId(item.getUserid())) {
                            ActivityUtils.startActivityForData(mActivity, InviteInviterDetailActivity.class, inviteId);
                        } else {
                            ActivityUtils.startActivityForData(mActivity, InviteJoinerDetailActivity.class, inviteId);
                        }
                    }
                } else if ("vip".equals(type)) {
                    ActivityUtils.startActivity(mActivity, VIPCenterActivity.class);
                } else if ("server".equals(type)) {

                } else if ("user".equals(type)) {
                    UserDetailActivity.startUserDetailAct(mActivity, Integer.parseInt(item.getUserid()), false);
                } else if ("takemeout".equals(type)) {
                    ActivityUtils.startActivity(mActivity, MyActiveActivity.class);
                } else if ("usercenter".equals(type)) {
                    UserDetailActivity.startUserDetailAct(mActivity, Integer.parseInt(NSMTypeUtils.getMyUserId()), false);
                } else if ("text".equals(type)) {

                }
            }
        });
        return view;
    }
}
