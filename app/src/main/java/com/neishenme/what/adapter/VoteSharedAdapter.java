package com.neishenme.what.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.GetVoteRankResponse;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

/**
 * 这个类的作用是  我的投票界面 的可刷新的 recyclerview 的适配器
 * <p>
 * Created by zhaozh on 2017/4/18.
 */

public class VoteSharedAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<GetVoteRankResponse.DataBean.ListOverUserLogoBean> mDatas;
    private LayoutInflater mInflater;

    public VoteSharedAdapter(Context mContext, List<GetVoteRankResponse.DataBean.ListOverUserLogoBean> mDatas) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    public void upDateInfo(List<GetVoteRankResponse.DataBean.ListOverUserLogoBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public VoteSharedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myVoteView = mInflater.inflate(R.layout.item_vote_shared_list, parent, false);
        VoteSharedViewHolder mViewHolder = new VoteSharedViewHolder(myVoteView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VoteSharedViewHolder myVoteViewHolder = (VoteSharedViewHolder) holder;
        GetVoteRankResponse.DataBean.ListOverUserLogoBean userInfo = mDatas.get(position);
        String userlogo = userInfo.getUserlogo();
        if (!TextUtils.isEmpty(userlogo)) {
            HttpLoader.getImageLoader().get(userlogo, ImageLoader.getImageListener(
                    myVoteViewHolder.mVoteSharedUserHeader,
                    R.drawable.vote_shared_user_header, R.drawable.vote_shared_user_header));
        } else {
            myVoteViewHolder.mVoteSharedUserHeader.setImageResource(R.drawable.vote_shared_user_header);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size();
    }

    class VoteSharedViewHolder extends RecyclerView.ViewHolder {
        ImageView mVoteSharedUserHeader;

        public VoteSharedViewHolder(View view) {
            super(view);
            mVoteSharedUserHeader = (ImageView) view.findViewById(R.id.vote_shared_user_header);
        }
    }
}
