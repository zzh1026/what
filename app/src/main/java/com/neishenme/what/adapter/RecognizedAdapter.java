package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.neishenme.what.R;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.MyToast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class RecognizedAdapter extends BaseAdapter implements HttpLoader.ResponseListener {
    private Context mContext;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> response;
    private boolean canScreening;
    private OnScreenFriend onScreenFriend;
    private int mCurrentSelectPosition = -1;


    public RecognizedAdapter(Context mContext, List<MyFriendsResponse.DataEntity.FriendsEntity> response,
                             boolean canScreening) {
        this.mContext = mContext;
        this.response = response;
        this.canScreening = canScreening;
    }

    public void setData(List<MyFriendsResponse.DataEntity.FriendsEntity> response) {
        this.response = response;
    }

    @Override
    public int getCount() {
        if (response != null && response.size() != 0) {
            return response.size();
        }
        return 0;
    }

    @Override
    public MyFriendsResponse.DataEntity.FriendsEntity getItem(int position) {
        return response.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_recognized, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        MyFriendsResponse.DataEntity.FriendsEntity item = getItem(position);
        String logo = item.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            HttpLoader.getImageLoader().get(item.getLogo(), ImageLoader.getImageListener(
                    holder.ivUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        holder.tvUserName.setText(item.getName());
        int gender = item.getGender();
        if (1 == gender) {
            holder.ivUserGender.setImageResource(R.drawable.man_icon);
        } else {
            holder.ivUserGender.setImageResource(R.drawable.woman_icon);
        }
        if (null != item.getSign()) {
            holder.tvTitle.setText(item.getSign());
        }

        if (canScreening) {
            holder.btnShield.setVisibility(View.VISIBLE);
            if (item.getRelation() == 2) {
                holder.btnShield.setVisibility(View.VISIBLE);
                holder.btnShield.setImageResource(R.drawable.btn_shield);
            } else if (item.getRelation() == 1) {
                holder.btnShield.setVisibility(View.VISIBLE);
                holder.btnShield.setImageResource(R.drawable.btn_unshield);
            } else {
                holder.btnShield.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.btnShield.setVisibility(View.INVISIBLE);
        }

        holder.btnShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentSelectPosition = position;
                HashMap params = new HashMap();
                params.put("token", NSMTypeUtils.getMyToken());
                params.put("targetId", String.valueOf(response.get(position).getId()));
                HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_SCREEEN, params, SendSuccessResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_SCREEEN, RecognizedAdapter.this, false).setTag(this);
            }
        });
        return view;
    }

    public interface OnScreenFriend {
        void onScreenFriend(int position);
    }

    public void setOnScreenFriend(OnScreenFriend onScreenFriend) {
        this.onScreenFriend = onScreenFriend;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_SCREEEN &&
                response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                if (mCurrentSelectPosition != -1) {
                    MyFriendsResponse.DataEntity.FriendsEntity friendsEntity = this.response.get(mCurrentSelectPosition);
                    int relation = friendsEntity.getRelation();
                    if (1 == relation) {
                        try {
                            EMClient.getInstance().contactManager().removeUserFromBlackList(friendsEntity.getHxUserName());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false，则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
                        try {
                            EMClient.getInstance().contactManager().
                                    addUserToBlackList(friendsEntity.getHxUserName(), false);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                    if (onScreenFriend != null) {
                        onScreenFriend.onScreenFriend(mCurrentSelectPosition);
                    }
                }
                mCurrentSelectPosition = -1;
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_SCREEEN) {
            MyToast.showConterToast(mContext, "网络连接失败");
        }
    }

    static class ViewHolder {
        private ImageView ivUserLogo;
        private TextView tvUserName;
        private TextView tvTitle;
        private ImageView btnShield;
        ImageView ivUserGender;

        public ViewHolder(View view) {
            ivUserLogo = (ImageView) view.findViewById(R.id.iv_user_logo);
            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            btnShield = (ImageView) view.findViewById(R.id.btn_shield);
            ivUserGender = (ImageView) view.findViewById(R.id.iv_user_gender);
        }
    }
}
