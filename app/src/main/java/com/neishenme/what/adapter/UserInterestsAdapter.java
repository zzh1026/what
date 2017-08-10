package com.neishenme.what.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ImageUtils;

import java.util.List;

import static com.neishenme.what.R.drawable.interest_music;
import static com.neishenme.what.R.id.view;

/**
 * 作者：zhaozh create on 2016/12/19 17:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个用户中心用户兴趣的适配器
 * .
 * 其作用是 :
 */
public class UserInterestsAdapter extends BaseAdapter {
    Context mContext;
    List<UserDetailResponse.DataBean.InterestsBean> interestLists;
    LayoutInflater mInflater;

    public UserInterestsAdapter(Context context, List<UserDetailResponse.DataBean.InterestsBean> interestLists) {
        this.interestLists = interestLists;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setPhotos(List<UserDetailResponse.DataBean.InterestsBean> interestLists) {
        this.interestLists = interestLists;
    }

    @Override
    public int getCount() {
        if (interestLists == null || interestLists.size() == 0) {
            return 0;
        }
        return interestLists.size();
    }

    @Override
    public UserDetailResponse.DataBean.InterestsBean getItem(int position) {
        return interestLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_userdetial_interest, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        UserDetailResponse.DataBean.InterestsBean itemInfos = getItem(position);
        View picChildView;
//        if ("music_singer".equals(itemInfos.getKey())) {
//            picChildView = ImageUtils.getPicChildView(itemInfos.getContent(), Color.parseColor("#6BB34F"));
//            viewHolder.mItemUserdetialInterestImage.setImageResource(R.drawable.interest_music);
//        } else
        if ("movie_name".equals(itemInfos.getKey())) {
            viewHolder.mItemUserdetialInterestImage.setImageResource(R.drawable.interest_movie);
            viewHolder.mItemUserdetialInterestContent.addView(ImageUtils.getPicChildView(itemInfos.getContent(), Color.parseColor("#9B7BB6")));
        } else if ("food_name".equals(itemInfos.getKey())) {
            viewHolder.mItemUserdetialInterestImage.setImageResource(R.drawable.interest_food);
            viewHolder.mItemUserdetialInterestContent.addView(ImageUtils.getPicChildView(itemInfos.getContent(), Color.parseColor("#E57373")));
        } else if ("trip_name".equals(itemInfos.getKey())) {
            viewHolder.mItemUserdetialInterestImage.setImageResource(R.drawable.interest_plece);
            viewHolder.mItemUserdetialInterestContent.addView(ImageUtils.getPicChildView(itemInfos.getContent(), Color.parseColor("#82B6C9")));
        }
//        else {
//            picChildView = ImageUtils.getPicChildView(itemInfos.getContent(), Color.parseColor("#CEC175"));
//            viewHolder.mItemUserdetialInterestImage.setImageResource(R.drawable.interest_sports);
//        }
        if (position == interestLists.size() - 1) {
            viewHolder.mUserDetailInterestLine.setVisibility(View.GONE);
        } else {
            viewHolder.mUserDetailInterestLine.setVisibility(View.VISIBLE);
        }
        return view;
    }

    static class ViewHolder {
        ImageView mItemUserdetialInterestImage;
        RelativeLayout mItemUserdetialInterestContent;
        View mUserDetailInterestLine;


        public ViewHolder(View view) {
            mItemUserdetialInterestImage = (ImageView) view.findViewById(R.id.item_userdetial_interest_image);
            mItemUserdetialInterestContent = (RelativeLayout) view.findViewById(R.id.item_userdetial_interest_content);
            mUserDetailInterestLine = view.findViewById(R.id.user_detail_interest_line);
        }
    }
}
