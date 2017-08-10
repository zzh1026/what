package com.neishenme.what.adapter;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveDateStarActivity;
import com.neishenme.what.activity.ActiveStarVotePaihangActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.bean.SuperStarListResponse;
import com.neishenme.what.dialog.ActiveVoteTicketDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;

import org.seny.android.utils.ActivityUtils;

import java.util.List;

/**
 * 这个类的作用是  网约女明星 活动的 最受欢迎排行榜的 适配器
 * <p>
 * Created by zhaozh on 2017/4/17.
 */

public class DateStarPaihangListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SuperStarListResponse.DataBean.ListStarsBean> mDateStars;
    private ActiveDateStarActivity mActiveDateStarActivity;
    private static final int DEFAULT_STAR_LIST_NUMBER = 10;

    private int[] starNo = new int[]{
            R.drawable.date_star_no_1, R.drawable.date_star_no_2, R.drawable.date_star_no_3,
            R.drawable.date_star_no_4, R.drawable.date_star_no_5, R.drawable.date_star_no_6,
            R.drawable.date_star_no_7, R.drawable.date_star_no_8, R.drawable.date_star_no_9,
            R.drawable.date_star_no_10};

    private int[] starNo1Image = new int[]{
            R.drawable.date_star_no_1_star_crow, R.drawable.date_star_no_1_star,
            R.drawable.date_star_no_1_releash, R.drawable.date_star_no_1_header,
            R.drawable.date_star_no_1_context, R.drawable.date_star_no_1_vote_her};

    private int[] starNo2Image = new int[]{
            R.drawable.date_star_no_2_star_crow, R.drawable.date_star_no_2_star,
            R.drawable.date_star_no_2_releash, R.drawable.date_star_no_2_header,
            R.drawable.date_star_no_2_context, R.drawable.date_star_no_2_vote_her};

    private int[] starNo3Image = new int[]{
            R.drawable.date_star_no_3_star_crow, R.drawable.date_star_no_3_star,
            R.drawable.date_star_no_3_releash, R.drawable.date_star_no_3_header,
            R.drawable.date_star_no_3_context, R.drawable.date_star_no_3_vote_her};

    private int[] starNormalImage = new int[]{
            R.drawable.date_star_normal_header,
            R.drawable.date_star_normal_releash,
            R.drawable.date_star_normal_vote_her};

    private int[][] starImageAll = new int[][]{starNo1Image, starNo2Image, starNo3Image, starNormalImage};

    public DateStarPaihangListAdapter(ActiveDateStarActivity mActiveDateStarActivity,
                                      List<SuperStarListResponse.DataBean.ListStarsBean> dateStars) {
        this.mActiveDateStarActivity = mActiveDateStarActivity;
        this.mInflater = LayoutInflater.from(mActiveDateStarActivity);
        this.mDateStars = dateStars;
    }

    public void changeData(List<SuperStarListResponse.DataBean.ListStarsBean> dateStars) {
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
                0 : mDateStars.size() > DEFAULT_STAR_LIST_NUMBER ? DEFAULT_STAR_LIST_NUMBER : mDateStars.size();
    }

    @Override
    public SuperStarListResponse.DataBean.ListStarsBean getItem(int position) {
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
            view = mInflater.inflate(R.layout.item_date_star_list, parent, false);
            holder = new PaihangListViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (PaihangListViewHolder) view.getTag();
        }
        final SuperStarListResponse.DataBean.ListStarsBean item = getItem(position);
        holder.mItemStarNumber.setImageResource(starNo[position]);
        if (0 <= position && position < 3) {
            holder.mItemStarStarCrow.setVisibility(View.VISIBLE);
            holder.mItemStarStarCrow.setImageResource(starImageAll[position][0]);
            holder.mItemStarStarDes.setVisibility(View.VISIBLE);
            holder.mItemStarStarDes.setImageResource(starImageAll[position][1]);

            holder.mItemStarReleashType.setImageResource(starImageAll[position][2]);
            holder.mItemStarStarBg.setBackgroundResource(starImageAll[position][3]);
            holder.mItemStarUserBg.setBackgroundResource(starImageAll[position][3]);

            holder.mItemDataStarUserDes.setVisibility(View.VISIBLE);
            holder.mItemDataStarUserDes.setImageResource(starImageAll[position][4]);

            holder.mItemDataStarVoteHer.setImageResource(starImageAll[position][5]);
        } else {
            holder.mItemStarStarCrow.setVisibility(View.INVISIBLE);
            holder.mItemStarStarDes.setVisibility(View.INVISIBLE);
            holder.mItemDataStarUserDes.setVisibility(View.INVISIBLE);

            holder.mItemStarReleashType.setImageResource(starImageAll[3][1]);
            holder.mItemStarStarBg.setBackgroundResource(starImageAll[3][0]);
            holder.mItemStarUserBg.setBackgroundResource(starImageAll[3][0]);
            holder.mItemDataStarVoteHer.setImageResource(starImageAll[3][2]);
        }

        holder.mItemStarVoteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    ActiveStarVotePaihangActivity.opStarVoteDetail(mActiveDateStarActivity, item.getSuperStarId());
                } else {
                    ActivityUtils.startActivity(mActiveDateStarActivity, LoginActivity.class);
                }
            }
        });

        holder.mItemDataStarVoteHer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NSMTypeUtils.isLogin()) {
                    ActiveVoteTicketDialog mActiveVoteTicketDialog = new ActiveVoteTicketDialog(
                            mActiveDateStarActivity, item.getStarLogo(),
                            item.getSuperStarId(), item.getStarName(), item.getStarQualifier());
                    mActiveVoteTicketDialog.show();
                    mActiveVoteTicketDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mActiveDateStarActivity.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    });
                } else {
                    ActivityUtils.startActivity(mActiveDateStarActivity, LoginActivity.class);
                }
            }
        });
        String starLogo = item.getStarLogo();
        if (!TextUtils.isEmpty(starLogo)) {
            HttpLoader.getImageLoader().get(starLogo, ImageLoader.getImageListener(
                    holder.mItemStarStarHeader, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            holder.mItemStarStarHeader.setImageResource(R.drawable.picture_moren);
        }

        String userLogo = item.getUserLogo();
        if (!TextUtils.isEmpty(userLogo)) {
            HttpLoader.getImageLoader().get(userLogo, ImageLoader.getImageListener(
                    holder.mItemStarUserHeader, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            holder.mItemStarUserHeader.setImageResource(R.drawable.picture_moren);
        }

        holder.mItemStarStarName.setText(item.getStarName());
        holder.mItemDataStarUserName.setText(item.getUsername());
        holder.mItemDataStarVoteNumber.setText(String.valueOf(item.getStarTicket()));
        return view;
    }

    public static class PaihangListViewHolder {
        ImageView mItemStarNumber;
        LinearLayout mItemStarVoteDetail;
        ImageView mItemStarStarCrow;
        FrameLayout mItemStarStarBg;
        CircleImageView mItemStarStarHeader;
        ImageView mItemStarStarDes;
        TextView mItemStarStarName;
        ImageView mItemStarReleashType;
        FrameLayout mItemStarUserBg;
        CircleImageView mItemStarUserHeader;
        ImageView mItemDataStarUserDes;
        TextView mItemDataStarUserName;
        TextView mItemDataStarVoteNumber;
        ImageView mItemDataStarVoteHer;

        public PaihangListViewHolder(View view) {
            mItemStarNumber = (ImageView) view.findViewById(R.id.item_star_number);
            mItemStarVoteDetail = (LinearLayout) view.findViewById(R.id.item_star_vote_detail);
            mItemStarStarCrow = (ImageView) view.findViewById(R.id.item_star_star_crow);
            mItemStarStarBg = (FrameLayout) view.findViewById(R.id.item_star_star_bg);
            mItemStarStarHeader = (CircleImageView) view.findViewById(R.id.item_star_star_header);
            mItemStarStarDes = (ImageView) view.findViewById(R.id.item_star_star_des);
            mItemStarStarName = (TextView) view.findViewById(R.id.item_star_star_name);
            mItemStarReleashType = (ImageView) view.findViewById(R.id.item_star_releash_type);
            mItemStarUserBg = (FrameLayout) view.findViewById(R.id.item_star_user_bg);
            mItemStarUserHeader = (CircleImageView) view.findViewById(R.id.item_star_user_header);
            mItemDataStarUserDes = (ImageView) view.findViewById(R.id.item_data_star_user_des);
            mItemDataStarUserName = (TextView) view.findViewById(R.id.item_data_star_user_name);
            mItemDataStarVoteNumber = (TextView) view.findViewById(R.id.item_data_star_vote_number);
            mItemDataStarVoteHer = (ImageView) view.findViewById(R.id.item_data_star_vote_her);
        }
    }
}
