package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveActivity;
import com.neishenme.what.bean.ActiveListResponse;
import com.neishenme.what.net.HttpLoader;

import java.util.List;


/**
 * 活动界面适配器
 */
public class ActiveAdapter extends BaseAdapter {
    private Context mContext;
    private ActiveActivity mActiveActivity;
    private List<ActiveListResponse.DataBean.TakemeoutBean> takemeoutAll;

    public ActiveAdapter(ActiveActivity mActiveActivity, List<ActiveListResponse.DataBean.TakemeoutBean> takemeoutAll) {
        mContext = mActiveActivity;
        this.mActiveActivity = mActiveActivity;
        this.takemeoutAll = takemeoutAll;
    }

    public void setData(List<ActiveListResponse.DataBean.TakemeoutBean> takemeoutAll) {
        this.takemeoutAll = takemeoutAll;
    }

    @Override
    public int getCount() {
        if (takemeoutAll != null && takemeoutAll.size() != 0) {
            return takemeoutAll.size();
        }
        return 0;
    }

    @Override
    public ActiveListResponse.DataBean.TakemeoutBean getItem(int position) {
        return takemeoutAll.get(position);
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
            view = View.inflate(mContext, R.layout.item_active_grid, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        ActiveListResponse.DataBean.TakemeoutBean itemInfo = getItem(position);
        String userLogo = itemInfo.getThumbnailslogo();
        if (TextUtils.isEmpty(userLogo)) {
            holder.mItemActiveUserLogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(userLogo, ImageLoader.getImageListener(
                    holder.mItemActiveUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        holder.mItemActiveTicketNum.setText(itemInfo.getTickets() + "");
        holder.mItemActiveUserGender.setImageResource(itemInfo.getGender() == 1 ? R.drawable.man_icon : R.drawable.woman_icon);
        holder.mItemActiveUserName.setText(itemInfo.getName());
        holder.mItemActiveOrderNum.setText(itemInfo.getUsers() + "人已约TA");
        String itemInfoId = itemInfo.getId();
        if (itemInfoId.length() == 1) {
            holder.mItemActiveSrcialNum.setText("00" + itemInfoId);
        } else if (itemInfoId.length() == 2) {
            holder.mItemActiveSrcialNum.setText("0" + itemInfoId);
        } else {
            holder.mItemActiveSrcialNum.setText(itemInfoId);
        }
        holder.mItemActiveTicketSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveActivity.ticketSub(position);
            }
        });
        return view;
    }

    static class ViewHolder {
        private ImageView mItemActiveUserLogo;
        private TextView mItemActiveTicketNum;
        private ImageView mItemActiveUserGender;
        private TextView mItemActiveUserName;
        private TextView mItemActiveOrderNum;
        private TextView mItemActiveTicketSub;
        private TextView mItemActiveSrcialNum;

        public ViewHolder(View view) {
            mItemActiveUserLogo = (ImageView) view.findViewById(R.id.item_active_user_logo);
            mItemActiveTicketNum = (TextView) view.findViewById(R.id.item_active_ticket_num);
            mItemActiveUserGender = (ImageView) view.findViewById(R.id.item_active_user_gender);
            mItemActiveUserName = (TextView) view.findViewById(R.id.item_active_user_name);
            mItemActiveOrderNum = (TextView) view.findViewById(R.id.item_active_order_num);
            mItemActiveTicketSub = (TextView) view.findViewById(R.id.item_active_ticket_sub);
            mItemActiveSrcialNum = (TextView) view.findViewById(R.id.item_active_srcial_num);
        }
    }
}
