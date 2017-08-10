package com.neishenme.what.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/5/4.
 * 新的主页邀请详情适配器
 *
 * 弃用 ,对 homeinviteadapter 的备份, 其关联 {@link com.neishenme.what.fragment.HomeInviteOldFragment}
 * @author zzh
 * @version v5.0.4
 */
@Deprecated
public class HomeInviteOldAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private MainActivity mainActivity;
    private HomeInviteFragment mHomeInviteFragment;
    private List<HomeInviteResponse.DataBean.InvitesBean> response;

    private int mCurrentItem = -1;
    private boolean isPlaying;
    private MediaPlayer mp;

    int[] pictures_1 = {
            R.drawable.home_invite_1_1, R.drawable.home_invite_1_2, R.drawable.home_invite_1_3};
    int[] pictures_2 = {
            R.drawable.home_invite_2_1, R.drawable.home_invite_2_2, R.drawable.home_invite_2_3};
    int[] pictures_3 = {
            R.drawable.home_invite_3_1, R.drawable.home_invite_3_2, R.drawable.home_invite_3_3};
    int[] pictures_4 = {
            R.drawable.home_invite_4_1, R.drawable.home_invite_4_2, R.drawable.home_invite_4_3};
    int[][] pictures = {pictures_1, pictures_2, pictures_3};

    private Random ran = new Random();

    public HomeInviteOldAdapter(HomeInviteFragment homeInviteFragment, Context context, List<HomeInviteResponse.DataBean.InvitesBean> response) {
        this.mContext = context;
        this.response = response;
        this.mHomeInviteFragment = homeInviteFragment;
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.invite_item_play_sound:
                if (mCurrentItem == position && isPlaying == true) {
                    if (mp != null) {
                        endplay();
                        mCurrentItem = -1;
                    }
                    ((ImageView) v).setImageResource(R.drawable.home_play);
                    return;
                }
                mCurrentItem = position;
                if (isPlaying == false) {
//                    try {
//                        mp = new MediaPlayer();
//                        if (TextUtils.isEmpty(response.get(position).getInvite_audiofile())) {
//                            MyToast.show(mContext, "暂无音频。。");
//                            return;
//                        }
//                        mp.setDataSource(response.get(position).getInvite_audiofile());
//                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mCurrentItem = -1;
//                                HomeInviteOldAdapter.this.notifyDataSetChanged();
//                                isPlaying = false;
//                            }
//                        });
//                        mp.prepare();
//                        mp.start();
//                        isPlaying = true;
//                        HomeInviteOldAdapter.this.notifyDataSetChanged();
//                    } catch (IOException e) {
//
//                    }
                } else {
                    endplay();
//                    try {
//                        if (null == response.get(position).getInvite_audiofile()) {
//                            MyToast.show(mContext, "暂无音频。。");
//                            return;
//                        }
//                        mp = new MediaPlayer();
//                        mp.setDataSource(response.get(position).getInvite_audiofile());
//                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mCurrentItem = -1;
//                                HomeInviteOldAdapter.this.notifyDataSetChanged();
//                                isPlaying = false;
//                            }
//                        });
//                        mp.prepare();
//                        mp.start();
//                        isPlaying = true;
//                        HomeInviteOldAdapter.this.notifyDataSetChanged();
//                    } catch (IOException e) {
//
//                    }
                }
                break;
        }
    }

    public void endplay() {
        mp.release();
        mp = null;
        isPlaying = false;
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
            view = View.inflate(mContext, R.layout.item_home_invite, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final HomeInviteResponse.DataBean.InvitesBean userInfo = getItem(position);
        holder.inviteItemTitle.setText(userInfo.getInvite_title());
        holder.inviteItemUserName.setText(userInfo.getUser_name());

        int[] flagBackGround = getFlagBackGround(position);
        holder.inviteItemLinebg1.setBackgroundResource(flagBackGround[0]);
        holder.inviteItemLinebg2.setBackgroundResource(flagBackGround[1]);
        holder.inviteItemLinebg3.setBackgroundResource(flagBackGround[2]);

        switch (userInfo.getUser_gender()) {
            case 0:
                holder.inviteItemUserGender.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.inviteItemUserGender.setVisibility(View.VISIBLE);
                holder.inviteItemUserGender.setImageResource(R.drawable.man_icon);
                break;
            case 2:
                holder.inviteItemUserGender.setVisibility(View.VISIBLE);
                holder.inviteItemUserGender.setImageResource(R.drawable.woman_icon);
                break;
        }

        holder.inviteItemType.setText(NSMTypeUtils.getUserTargetWithHome(userInfo.getInvite_target()));
        holder.inviteItemPosition.setText(userInfo.getInvite_position());
//        holder.inviteItemJoinNumber.setText(userInfo.getInvite_joinercount() + "人");

        holder.inviteItemPriceInvite.setText(NSMTypeUtils.getGreatPrice(userInfo.getInvite_price()));
        holder.inviteItemDistence.setText(LocationToBaiduMap.getDistance(userInfo.getDistance()));
//        holder.inviteItemPlayTime.setText(TextUtils.isEmpty(userInfo.getInvite_audiofile()) ? "0" : userInfo.getInvite_audioDuration() + "");
        holder.inviteItemPlaySound.setOnClickListener(this);
        holder.inviteItemHeaderLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    UserDetailActivity.startUserDetailAct(mContext, response.get(position).getUser_id(), false);
                } else {
//                    mainActivity.showToast(mainActivity.getString(R.string.user_should_login_suggest));
                    ActivityUtils.startActivity(mainActivity, LoginActivity.class);
                }
            }
        });

        //让“+”号图片变成灰色
        if(userInfo!=null) {
            if (userInfo.getInvite_target() == 0 || NSMTypeUtils.getMyGender() == userInfo.getInvite_target()) {
            } else {
                holder.inviteItemLinebg3.setBackgroundResource(R.drawable.active_frag_hert_bg);
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
//                    mainActivity.showToast(mainActivity.getString(R.string.user_should_login_suggest));
                    ActivityUtils.startActivity(mainActivity, LoginActivity.class);
                }
            }
        });

        if (position == mCurrentItem) {
            holder.inviteItemPlaySound.setImageResource(R.drawable.home_pause);
        } else {
            holder.inviteItemPlaySound.setImageResource(R.drawable.home_play);
        }
        holder.inviteItemPlaySound.setVisibility(View.VISIBLE);

        if (null != userInfo.getUser_thumbnailslogofile()) {
            HttpLoader.getImageLoader().get(userInfo.getUser_thumbnailslogofile(), ImageLoader.getImageListener(
                    holder.inviteItemHeaderLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        holder.inviteItemPlaySound.setTag(position);
        holder.inviteItemHeaderLogo.setTag(position);
        return view;
    }

    private int[] getFlagBackGround(int position) {
        return pictures[position % 3];
//        switch (ran.nextInt(3)) {
//            case 0:
//                return pictures_1;
//            case 1:
//                return pictures_2;
////            case 2:
////                return pictures_3;
//            default:
//                return pictures_3;
//        }
    }

    static class ViewHolder {
        private ImageView inviteItemLinebg1;
        private ImageView inviteItemHeaderLogo;
        private TextView inviteItemTitle;
        private TextView inviteItemType;
        private TextView inviteItemPosition;
        private TextView inviteItemJoinNumber;
        private TextView inviteItemPriceInvite;
        private ImageView inviteItemUserGender;
        private TextView inviteItemUserName;
        private ImageView inviteItemPlaySound;
        private TextView inviteItemPlayTime;
        private TextView inviteItemDistence;
        private ImageView inviteItemLinebg2;
        private RelativeLayout inviteItemJoinInvite;
        private ImageView inviteItemLinebg3;

        public ViewHolder(View view) {
            inviteItemLinebg1 = (ImageView) view.findViewById(R.id.invite_item_linebg_1);
            inviteItemHeaderLogo = (ImageView) view.findViewById(R.id.invite_item_header_logo);
            inviteItemTitle = (TextView) view.findViewById(R.id.invite_item_title);
            inviteItemType = (TextView) view.findViewById(R.id.invite_item_type);
            inviteItemPosition = (TextView) view.findViewById(R.id.invite_item_position);
            inviteItemJoinNumber = (TextView) view.findViewById(R.id.invite_item_join_number);
            inviteItemPriceInvite = (TextView) view.findViewById(R.id.invite_item_price_invite);
            inviteItemUserGender = (ImageView) view.findViewById(R.id.invite_item_user_gender);
            inviteItemUserName = (TextView) view.findViewById(R.id.invite_item_user_name);
            inviteItemPlaySound = (ImageView) view.findViewById(R.id.invite_item_play_sound);
            inviteItemPlayTime = (TextView) view.findViewById(R.id.invite_item_play_time);
            inviteItemDistence = (TextView) view.findViewById(R.id.invite_item_distence);
            inviteItemLinebg2 = (ImageView) view.findViewById(R.id.invite_item_linebg_2);
            inviteItemJoinInvite = (RelativeLayout) view.findViewById(R.id.invite_item_join_invite);
            inviteItemLinebg3 = (ImageView) view.findViewById(R.id.invite_item_linebg_3);
        }
    }

    public void onDestory() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            try {
                mp.release();
            } catch (Exception e) {
            }
            mp = null;
            isPlaying = false;
            mCurrentItem = -1;
            notifyDataSetChanged();
        }
    }
}
