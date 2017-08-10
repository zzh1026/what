package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.bean.HomeInviteResponse;
import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 * 新的主页邀请详情适配器
 */
public class HomeInviteAdapter extends BaseAdapter {
    private Context mContext;
    private MainActivity mainActivity;
    private LayoutInflater mInflater;

    private HomeInviteFragment mHomeInviteFragment;
    private List<HomeInviteResponse.DataBean.InvitesBean> response;

    int[] pictures_1 = {
            R.drawable.home_invite_1_1, R.drawable.home_invite_1_2, R.drawable.home_invite_1_3};
    int[] pictures_2 = {
            R.drawable.home_invite_2_1, R.drawable.home_invite_2_2, R.drawable.home_invite_2_3};
    int[] pictures_3 = {
            R.drawable.home_invite_3_1, R.drawable.home_invite_3_2, R.drawable.home_invite_3_3};
    int[][] pictures = {pictures_1, pictures_2, pictures_3};

    public HomeInviteAdapter(HomeInviteFragment homeInviteFragment, Context context, List<HomeInviteResponse.DataBean.InvitesBean> response) {
        this.mContext = context;
        this.response = response;
        this.mHomeInviteFragment = homeInviteFragment;
        mInflater = LayoutInflater.from(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public int getCount() {
        if (response != null && response.size() != 0)
            return response.size();
        return 0;
    }

    @Override
    public HomeInviteResponse.DataBean.InvitesBean getItem(int position) {
        return response.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_home_active, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final HomeInviteResponse.DataBean.InvitesBean userInfo = getItem(position);

        //三条线
        int[] flagBackGround = getFlagBackGround(position);
        holder.inviteItemLinebg1.setBackgroundResource(flagBackGround[0]);
        holder.inviteItemLinebg2.setBackgroundResource(flagBackGround[1]);
        holder.inviteItemLinebg3.setBackgroundResource(flagBackGround[2]);

        //头像
        if (!TextUtils.isEmpty(userInfo.getUser_thumbnailslogofile())) {
            HttpLoader.getImageLoader().get(userInfo.getUser_thumbnailslogofile(), ImageLoader.getImageListener(
                    holder.inviteItemHeaderLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        //标题和名字
        holder.inviteItemTitle.setText(userInfo.getInvite_title());
        holder.homeActiveUsername.setText(userInfo.getUser_name());

        //性别和年龄
        switch (userInfo.getUser_gender()) {
            case 0:
                holder.homeActiveGenderBg.setVisibility(View.GONE);
                break;
            case 1:
                holder.homeActiveGenderBg.setVisibility(View.VISIBLE);
                holder.homeActiveGenderBg.setBackgroundResource(R.drawable.home_active_gender_men_bg);
                holder.homeActiveGender.setVisibility(View.VISIBLE);
                holder.homeActiveGender.setImageResource(R.drawable.home_active_men);
                break;
            case 2:
                holder.homeActiveGenderBg.setVisibility(View.VISIBLE);
                holder.homeActiveGenderBg.setBackgroundResource(R.drawable.home_active_gender_women_bg);
                holder.homeActiveGender.setVisibility(View.VISIBLE);
                holder.homeActiveGender.setImageResource(R.drawable.home_active_women);
                break;
        }
        holder.homeActiveAge.setText(TimeUtils.getAge(userInfo.getUser_birthday()) + "");

        //活动时间
        long inviteTime = userInfo.getInvite_time();
        holder.homeActiveTime.setText(DateUtils.formatTimeSimple(inviteTime));
//        int hour = DateUtils.formatHour(inviteTime);
//        String minute = String.valueOf(DateUtils.formatMinute(inviteTime));
//        if (!TextUtils.isEmpty(minute)) {
//            minute = minute.length() == 1 ? "0" + minute : minute;
//        }
//        if (hour > 12) {
//            holder.homeActiveTime.setText("PM  " + (hour - 12) + ":" + minute);
//        } else {
//            holder.homeActiveTime.setText("PM  " + hour + ":" + minute);
//        }

        //邀请对象和邀请金额
        holder.inviteItemType.setText(NSMTypeUtils.getUserTargetWithHome(userInfo.getInvite_target()));
        holder.inviteItemPriceInvite.setText(NSMTypeUtils.getGreatPrice(userInfo.getInvite_price()));

        if (userInfo.getHot() == 1) {
            holder.mInviteItemHotBg.setVisibility(View.VISIBLE);
        } else {
            holder.mInviteItemHotBg.setVisibility(View.INVISIBLE);
        }
        //财富值
//        if (userInfo.getInvite_showCashFlag() == 1) {
//            holder.homeActiveMoneyLayout.setVisibility(View.VISIBLE);
//            holder.homeActiveMoneyContent.setText(
//                    new BigDecimal(userInfo.getUserCash()).setScale(0, BigDecimal.ROUND_HALF_UP) + "");
//        } else {
//            holder.homeActiveMoneyLayout.setVisibility(View.GONE);
//            holder.homeActiveMoneyContent.setText("");
//        }

        //查看次数和距离
        holder.homeActiveDistence.setText(LocationToBaiduMap.getDistance(userInfo.getDistance()));
        holder.homeInvitePreviewNumber.setText(userInfo.getInvite_preview() + "");

        //头像点击
        holder.inviteItemHeaderLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    UserDetailActivity.startUserDetailAct(mContext, userInfo.getUser_id(), false);
                } else {
                    ActivityUtils.startActivity(mainActivity, LoginActivity.class);
                }
            }
        });

        //处理是否可以加入
        if (NSMTypeUtils.isLogin()) {
            if (NSMTypeUtils.isMyUserId(String.valueOf(userInfo.getUser_id()))) {
                holder.inviteItemLinebg3.setBackgroundResource(R.drawable.home_active_no_can_join_bg);
            }
            int joiner_newstatus = userInfo.getJoiner_newstatus();
            if (joiner_newstatus == 50 || joiner_newstatus == 100 || joiner_newstatus == 120) {
                holder.inviteItemLinebg3.setBackgroundResource(R.drawable.home_active_no_can_join_bg);
            } else {
                if (userInfo.getInvite_target() == 0 || NSMTypeUtils.getMyGender() == userInfo.getInvite_target()) {
                } else {
                    holder.inviteItemLinebg3.setBackgroundResource(R.drawable.home_active_no_can_join_bg);
                }
            }
        }

        //发布列表的点击加入的监听
        holder.inviteItemJoinInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    if (NSMTypeUtils.isMyUserId(String.valueOf(userInfo.getUser_id()))) {
                        mainActivity.showToastInfo("不能加入自己发的单");
                        return;
                    }
                    int joiner_newstatus = userInfo.getJoiner_newstatus();
                    if (joiner_newstatus == 50 || joiner_newstatus == 100 || joiner_newstatus == 120) {
                        mainActivity.showToastSuccess("您已经成功加入该单");
                    } else {
                        if (userInfo.getInvite_target() == 0 || NSMTypeUtils.getMyGender() == userInfo.getInvite_target()) {
                            mHomeInviteFragment.tryAddInvite(position);
                        } else {
                            mainActivity.showToastInfo("对象不对");
                        }
                    }
                } else {
                    ActivityUtils.startActivity(mainActivity, LoginActivity.class);
                }
            }
        });
        return view;
    }

    private int[] getFlagBackGround(int position) {
        return pictures[position % 3];
    }

    static class ViewHolder {
        private ImageView inviteItemLinebg1;
        private ImageView inviteItemHeaderLogo;
        private TextView inviteItemTitle;
        private TextView homeActiveUsername;
        private LinearLayout homeActiveGenderBg;
        private ImageView homeActiveGender;
        private TextView homeActiveAge;
        private TextView homeActiveTime;
        private TextView inviteItemType;
        //        private LinearLayout homeActiveMoneyLayout;
//        private TextView homeActiveMoneyContent;
        private TextView inviteItemPriceInvite;
        private TextView homeInvitePreviewNumber;
        private TextView homeActiveDistence;
        private ImageView inviteItemLinebg2;
        private RelativeLayout inviteItemJoinInvite;
        private ImageView inviteItemLinebg3;
        private ImageView mInviteItemHotBg;

        public ViewHolder(View view) {
            inviteItemLinebg1 = (ImageView) view.findViewById(R.id.invite_item_linebg_1);
            inviteItemHeaderLogo = (ImageView) view.findViewById(R.id.invite_item_header_logo);
            inviteItemTitle = (TextView) view.findViewById(R.id.invite_item_title);
            homeActiveUsername = (TextView) view.findViewById(R.id.home_active_username);
            homeActiveGenderBg = (LinearLayout) view.findViewById(R.id.home_active_gender_bg);
            homeActiveGender = (ImageView) view.findViewById(R.id.home_active_gender);
            homeActiveAge = (TextView) view.findViewById(R.id.home_active_age);
            homeActiveTime = (TextView) view.findViewById(R.id.home_active_time);
            inviteItemType = (TextView) view.findViewById(R.id.invite_item_type);
//            homeActiveMoneyLayout = (LinearLayout) view.findViewById(R.id.home_active_money_layout);
//            homeActiveMoneyContent = (TextView) view.findViewById(R.id.home_active_money_content);
            inviteItemPriceInvite = (TextView) view.findViewById(R.id.invite_item_price_invite);
            homeInvitePreviewNumber = (TextView) view.findViewById(R.id.home_invite_preview_number);
            homeActiveDistence = (TextView) view.findViewById(R.id.home_active_distence);
            inviteItemLinebg2 = (ImageView) view.findViewById(R.id.invite_item_linebg_2);
            inviteItemJoinInvite = (RelativeLayout) view.findViewById(R.id.invite_item_join_invite);
            inviteItemLinebg3 = (ImageView) view.findViewById(R.id.invite_item_linebg_3);
            mInviteItemHotBg = (ImageView) view.findViewById(R.id.invite_item_hot_bg);
        }
    }
}
