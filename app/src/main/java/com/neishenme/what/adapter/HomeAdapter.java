package com.neishenme.what.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.bean.HomeResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Administrator on 2016/5/4.
 * 旧的 主要附近的邀请适配器, 已弃用,新的邀请详情见 @{@link HomeInviteAdapter}
 */
@Deprecated
public class HomeAdapter extends BaseAdapter implements View.OnClickListener, HttpLoader.ResponseListener {
    private Context mContext;
    private MainActivity mainActivity;
    private HomeResponse response;
    private int mCurrentItem = -1;
    private boolean isPlaying;
    private MediaPlayer mp;
    private int mSelectedItem;

    int[] pictures = {
            R.drawable.invite_detail_flag_1, R.drawable.invite_detail_flag_2,
            R.drawable.invite_detail_flag_3, R.drawable.invite_detail_flag_4};
    private Random ran = new Random();

    public HomeAdapter(Context context, HomeResponse response) {
        this.mContext = context;
        this.response = response;
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.iv_play_audio:
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
                    try {
                        mp = new MediaPlayer();
                        if (TextUtils.isEmpty(response.getData().getInvites().get(position).getInvite_audiofile())) {
                            MyToast.show(mContext, "暂无音频。。");
                            return;
                        }
                        mp.setDataSource(response.getData().getInvites().get(position).getInvite_audiofile());
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mCurrentItem = -1;
                                HomeAdapter.this.notifyDataSetChanged();
                                isPlaying = false;
                            }
                        });
                        mp.prepare();
                        mp.start();
                        isPlaying = true;
                        HomeAdapter.this.notifyDataSetChanged();
                    } catch (IOException e) {

                    }
                } else {
                    endplay();
                    try {
                        if (null == response.getData().getInvites().get(position).getInvite_audiofile()) {
                            MyToast.show(mContext, "暂无音频。。");
                            return;
                        }
                        mp = new MediaPlayer();
                        mp.setDataSource(response.getData().getInvites().get(position).getInvite_audiofile());
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mCurrentItem = -1;
                                HomeAdapter.this.notifyDataSetChanged();
                                isPlaying = false;
                            }
                        });
                        mp.prepare();
                        mp.start();
                        isPlaying = true;
                        HomeAdapter.this.notifyDataSetChanged();
                    } catch (IOException e) {

                    }
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
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                mainActivity.showToastSuccess("添加关注成功");
                addFocus(mSelectedItem);
            } else {
                mainActivity.showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    public interface itemPopCallBack {
        public void setOnitem(int i);
    }

    public MediaPlayer getMp() {
        return mp;
    }

    itemPopCallBack itemPopCallBack = null;

    public void setOnItemPop(itemPopCallBack callBack) {
        itemPopCallBack = callBack;
    }

    @Override
    public int getCount() {
        return response.getData().getInvites().size();
    }

    @Override
    public Object getItem(int position) {
        return response.getData().getInvites().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addFocus(int addCurrentItem) {
        response.getData().getInvites().get(addCurrentItem).setUserfoucs_state(1);
        notifyDataSetChanged();
    }

    public void cancleFocus(int cancleCurrentItem) {
        response.getData().getInvites().get(cancleCurrentItem).setUserfoucs_state(0);
        notifyDataSetChanged();
    }

    public void shieldPeople(int shieldItem) {
        response.getData().getInvites().remove(shieldItem);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_invite, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        HomeResponse.DataBean.InvitesBean userInfo = (HomeResponse.DataBean.InvitesBean) getItem(position);
        holder.itemFrist.setVisibility(View.GONE);
        holder.tvHomeTitle.setText(userInfo.getInvite_title());
        holder.tvUserName.setText(userInfo.getUser_name());

        switch (userInfo.getUser_gender()) {
            case 0:
                holder.ivGender.setVisibility(View.GONE);
                holder.tvGender.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.tvGender.setVisibility(View.GONE);
                holder.ivGender.setImageResource(R.drawable.man_icon);
                break;
            case 2:
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.tvGender.setVisibility(View.GONE);
                holder.ivGender.setImageResource(R.drawable.woman_icon);
                break;
        }

        String payType = NSMTypeUtils.getUserPayType(userInfo.getInvite_payType());
        String inviteTarget = NSMTypeUtils.getUserTarget(userInfo.getInvite_target());
        holder.tvAa.setText(payType + inviteTarget);
        holder.tvPrice.setText("￥ " + userInfo.getServices_price());
        holder.tvSeeNumber.setText(userInfo.getInvite_preview() + "");
        holder.tvPlayNumber.setText(userInfo.getInvite_audiofile() == null ? "0" : userInfo.getInvite_audioDuration() + "");
        holder.ivPlayAudio.setOnClickListener(this);
        holder.homeUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin())
                    UserDetailActivity.startUserDetailAct(mContext, response.getData().getInvites().get(position).getUser_id(), false);
                else
                    mainActivity.showToastInfo("请登录后重试");
            }
        });
//        if (TextUtils.isEmpty(response.getData().getInvites().get(position).getInvite_audiofile())) {
//            holder.ivPlayAudio.setVisibility(View.INVISIBLE);
//        } else {
        if (position == mCurrentItem) {
            holder.ivPlayAudio.setImageResource(R.drawable.home_pause);
        } else {
            holder.ivPlayAudio.setImageResource(R.drawable.home_play);
        }
        holder.ivPlayAudio.setVisibility(View.VISIBLE);
//        }
        if (null != userInfo.getStores_name()) {
            holder.tvRestrantantName.setText(userInfo.getStores_name());
        }
        if (null != userInfo.getUser_logofile()) {
            HttpLoader.getImageLoader().get(userInfo.getUser_logofile(), ImageLoader.getImageListener(
                    holder.homeUserIcon, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        if (null != userInfo.getServices_logofile()) {
            HttpLoader.getImageLoader().get(userInfo.getServices_logofile(), ImageLoader.getImageListener(
                    holder.ivRestaurantIcon, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        if (0 == userInfo.getUserfoucs_state() && !NSMTypeUtils.isMyUserId(String.valueOf(userInfo.getUser_id()))) {
            holder.ivAddFriends.setVisibility(View.VISIBLE);
        } else {
            holder.ivAddFriends.setVisibility(View.INVISIBLE);
        }

        String services_name = userInfo.getServices_name();
        if (!TextUtils.isEmpty(services_name)) {
            holder.mInviteDetailFlatTv.setText(services_name);
            holder.mInviteDetailFlatTv.setBackgroundResource(getFlagBackGround());
        }

        holder.ivPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestory();
                if (NSMTypeUtils.isLogin()) {
                    if (itemPopCallBack != null)
                        itemPopCallBack.setOnitem(position);
                } else {
                    mainActivity.showToastInfo("您尚未登录,请登录后重试");
                    ActivityUtils.startActivity(mainActivity, LoginActivity.class);
                }

            }
        });
        holder.ivAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    HashMap params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("targetId", response.getData().getInvites().get(position).getUser_id() + "");
                    HttpLoader.post(ConstantsWhatNSM.URL_ADDFOCUS, params, SendSuccessResponse.class,
                            ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS, HomeAdapter.this, false).setTag(this);
                    mSelectedItem = position;

                } else {
                    mainActivity.showToastInfo("请登录后重试");
                }
            }
        });
        holder.ivPlayAudio.setTag(position);
        holder.homeUserIcon.setTag(position);
        return view;
    }

    private int getFlagBackGround() {
        return pictures[ran.nextInt(4)];
    }

    static class ViewHolder {
        private ImageView ivAddFriends;
        private TextView tvHomeTitle;
        private CircleImageView homeUserIcon;
        private ImageView ivGender;
        private TextView tvUserName;
        private ImageView ivPickUp;
        private ImageView ivRestaurantIcon;
        private TextView tvPrice;
        private TextView tvRestrantantName;
        private TextView tvAa;
        private ImageView ivPlayVideo;
        private ImageView ivPlayAudio;
        private TextView tvSeeNumber;
        private TextView tvGender;
        private View itemFrist;
        private TextView tvPlayNumber;
        private TextView mInviteDetailFlatTv;

        public ViewHolder(View view) {
            itemFrist = view.findViewById(R.id.item_frist);
            tvHomeTitle = (TextView) view.findViewById(R.id.tv_home_title);
            homeUserIcon = (CircleImageView) view.findViewById(R.id.home_user_icon);
            ivGender = (ImageView) view.findViewById(R.id.iv_gender);
            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);

            ivAddFriends = (ImageView) view.findViewById(R.id.iv_add_friends);
            ivPickUp = (ImageView) view.findViewById(R.id.iv_pick_up);

            ivRestaurantIcon = (ImageView) view.findViewById(R.id.iv_restaurant_icon);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvRestrantantName = (TextView) view.findViewById(R.id.tv_restrantant_name);
            tvAa = (TextView) view.findViewById(R.id.tv_aa);
            ivPlayAudio = (ImageView) view.findViewById(R.id.iv_play_audio);
            tvSeeNumber = (TextView) view.findViewById(R.id.tv_see_number);
            tvGender = (TextView) view.findViewById(R.id.tv_gender);

            tvPlayNumber = (TextView) view.findViewById(R.id.tv_play_number);
            mInviteDetailFlatTv = (TextView) view.findViewById(R.id.invite_detail_flat_tv);
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
