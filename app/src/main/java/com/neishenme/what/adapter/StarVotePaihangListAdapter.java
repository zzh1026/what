package com.neishenme.what.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveStarVotePaihangActivity;
import com.neishenme.what.bean.StarVoteDetailResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.view.SGTextView;

import java.util.List;
import java.util.Random;

/**
 * 这个类的作用是  网约女明星单个女明星的具体排行榜 排行榜的 适配器
 * <p>
 * Created by zhaozh on 2017/4/17.
 */

public class StarVotePaihangListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<StarVoteDetailResponse.DataBean.ListStarsBean> mDateStars;
    private ActiveStarVotePaihangActivity mStarVoteActivity;

    private static final int MAX_SHOW_NUMBER = 15;

    private int[] mRankingColor = new int[]{
            Color.parseColor("#fbe34b"), Color.parseColor("#fa1313"),
            Color.parseColor("#1395fa"), Color.parseColor("#242424")};

    public StarVotePaihangListAdapter(ActiveStarVotePaihangActivity mStarVoteActivity,
                                      List<StarVoteDetailResponse.DataBean.ListStarsBean> dateStars) {
        this.mStarVoteActivity = mStarVoteActivity;
        this.mInflater = LayoutInflater.from(mStarVoteActivity);
        this.mDateStars = dateStars;
    }

    public void changeData(List<StarVoteDetailResponse.DataBean.ListStarsBean> dateStars) {
        if (dateStars == null) {
            this.mDateStars = dateStars;
        } else {
            mDateStars.clear();
            mDateStars.addAll(dateStars);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDateStars == null || mDateStars.size() == 0 ?
                0 : mDateStars.size() > MAX_SHOW_NUMBER ? MAX_SHOW_NUMBER : mDateStars.size();
    }

    @Override
    public StarVoteDetailResponse.DataBean.ListStarsBean getItem(int position) {
        return mDateStars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PaihangListViewHolder holder;
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_star_vote_list, parent, false);
            holder = new PaihangListViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (PaihangListViewHolder) view.getTag();
        }

        StarVoteDetailResponse.DataBean.ListStarsBean item = getItem(position);
        holder.mVoteListUserRankingNum.setText(String.valueOf(position + 1));
        holder.mVoteListUserRankingNum.setStrokColor(position < 3 ? mRankingColor[position] : mRankingColor[3]);
        String userlogo = item.getUserlogo();
        if (!TextUtils.isEmpty(userlogo)) {
            HttpLoader.getImageLoader().get(userlogo, ImageLoader.getImageListener(
                    holder.mVoteListUserHeader,
                    R.drawable.star_vote_user_header, R.drawable.star_vote_user_header));
        } else {
            holder.mVoteListUserHeader.setImageResource(R.drawable.star_vote_user_header);
        }

        holder.mVoteListUserName.setText(item.getName());
        holder.mStarVoteUserVoteNum.setText(item.getUserTickets() + "");
        return view;
    }

    public static class PaihangListViewHolder {
        SGTextView mVoteListUserRankingNum;
        ImageView mVoteListUserHeader;
        TextView mVoteListUserName;
        TextView mStarVoteUserVoteNum;

        public PaihangListViewHolder(View view) {
            mVoteListUserRankingNum = (SGTextView) view.findViewById(R.id.vote_list_user_ranking_num);
            mVoteListUserHeader = (ImageView) view.findViewById(R.id.vote_list_user_header);
            mVoteListUserName = (TextView) view.findViewById(R.id.vote_list_user_name);
            mStarVoteUserVoteNum = (TextView) view.findViewById(R.id.star_vote_user_vote_num);
        }
    }
}
