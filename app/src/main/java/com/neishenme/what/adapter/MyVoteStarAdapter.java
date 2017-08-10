package com.neishenme.what.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.StarMyVoteResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.view.CircleImageView;

import java.util.List;
import java.util.Random;

/**
 * 这个类的作用是  我的投票界面 的可刷新的 recyclerview 的适配器
 * <p>
 * Created by zhaozh on 2017/4/18.
 */

public class MyVoteStarAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<StarMyVoteResponse.DataBean.ListSuperStarsTicketedBean> mDatas;
    private LayoutInflater mInflater;

    public MyVoteStarAdapter(Context mContext, List<StarMyVoteResponse.DataBean.ListSuperStarsTicketedBean> mDatas) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    public void upDateInfo(List<StarMyVoteResponse.DataBean.ListSuperStarsTicketedBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public MyVoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myVoteView = mInflater.inflate(R.layout.item_star_my_vote, parent, false);
        MyVoteViewHolder myVoteViewHolder = new MyVoteViewHolder(myVoteView);
        return myVoteViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyVoteViewHolder myVoteViewHolder = (MyVoteViewHolder) holder;
        StarMyVoteResponse.DataBean.ListSuperStarsTicketedBean items = mDatas.get(position);
        String starlogo = items.getStarlogo();
        if (!TextUtils.isEmpty(starlogo)) {
            HttpLoader.getImageLoader().get(starlogo, ImageLoader.getImageListener(
                    myVoteViewHolder.mMyVoteStarHeader,
                    R.drawable.my_vote_user_header, R.drawable.my_vote_user_header));
        } else {
            myVoteViewHolder.mMyVoteStarHeader.setImageResource(R.drawable.my_vote_user_header);
        }
        myVoteViewHolder.mMyVoteStarName.setText(items.getStarname());
        myVoteViewHolder.mMyVoteStarNumber.setText(String.valueOf(items.getTickets()));
    }

    @Override
    public int getItemCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size();
    }

    class MyVoteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mMyVoteStarHeader;
        TextView mMyVoteStarName;
        TextView mMyVoteStarNumber;

        public MyVoteViewHolder(View view) {
            super(view);
            mMyVoteStarHeader = (CircleImageView) view.findViewById(R.id.my_vote_star_header);
            mMyVoteStarName = (TextView) view.findViewById(R.id.my_vote_star_name);
            mMyVoteStarNumber = (TextView) view.findViewById(R.id.my_vote_star_number);
        }
    }
}
