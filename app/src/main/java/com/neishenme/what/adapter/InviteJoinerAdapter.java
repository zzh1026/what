package com.neishenme.what.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.InviteDetailResponse;
import com.neishenme.what.bean.InviteResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/16 15:30
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class InviteJoinerAdapter extends BaseAdapter {
    private List<InviteResponse.DataBean.JoinersBean> joiners;
    private Context context;
    private int mPublishAreaId;


    public InviteJoinerAdapter(List<InviteResponse.DataBean.JoinersBean> joiners, Context context, int mPublishAreaId) {
        this.joiners = joiners;
        this.context = context;
        this.mPublishAreaId = mPublishAreaId;
    }

    public void setJoiners(List<InviteResponse.DataBean.JoinersBean> joiners) {
        this.joiners = joiners;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (joiners == null || joiners.size() == 0) {
            return 0;
        }
        return joiners.size();
    }

    @Override
    public InviteResponse.DataBean.JoinersBean getItem(int position) {
        return joiners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_invite_joiner_head, null);

            //指定Item的宽高
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
//            int width = (int) (80 * density);
            int hight = (int) (84 * density);
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, hight));

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        InviteResponse.DataBean.JoinersBean joinersBean = getItem(position);
        String url = joinersBean.getUser_thumbnailslogo();
        holder.mItemInviteHeadIv.setTag(url);

        if (holder.mItemInviteHeadIv.getTag() != null && holder.mItemInviteHeadIv.getTag().equals(url)) {
            HttpLoader.getImageLoader().get(url,
                    ImageLoader.getImageListener(holder.mItemInviteHeadIv, R.drawable.picture_moren, R.drawable.picture_moren));
            int acceptType = joinersBean.getJoiner_acceptType();
            if (1 == acceptType && NSMTypeUtils.isMyUserId(String.valueOf(joinersBean.getJoiner_userId()))) {
                holder.mItemInviteAgreeIv.setVisibility(View.VISIBLE);
            } else {
                holder.mItemInviteAgreeIv.setVisibility(View.INVISIBLE);
            }

            int joiner_joinPrice = (int) joinersBean.getJoiner_joinPrice();
            if (0 != joiner_joinPrice) {
                holder.itemInvitePrice.setVisibility(View.VISIBLE);
                holder.itemInvitePrice.setText(joiner_joinPrice + "元");
            } else {
                holder.itemInvitePrice.setVisibility(View.INVISIBLE);
            }

            int isVip = getItem(position).getVip_type();
            int joinerAreaId = joinersBean.getJoiner_areaId();
            if (0 == isVip) {   //非会员
                if (mPublishAreaId == joinerAreaId) {   //非异地
                    holder.mItemInviteVipSingleIv.setVisibility(View.GONE);
                } else {    //异地
                    holder.mItemInviteVipSingleIv.setVisibility(View.VISIBLE);
                    holder.mItemInviteVipSingleIv.setImageResource(R.drawable.invite_detail_place_bg);
                }
            } else {    //会员
                holder.mItemInviteVipSingleIv.setVisibility(View.VISIBLE);
                if (mPublishAreaId == joinerAreaId) {   //非异地
                    holder.mItemInviteVipSingleIv.setImageResource(R.drawable.invite_detail_vip_bg);
                } else {    //异地
                    holder.mItemInviteVipSingleIv.setImageResource(R.drawable.invite_detail_all_bg);
                }
            }
        }
        return view;
    }

    static class ViewHolder {
        ImageView mItemInviteHeadIv;
        ImageView mItemInviteAgreeIv;
        ImageView mItemInviteVipSingleIv;
        TextView itemInvitePrice;


        public ViewHolder(View view) {
            mItemInviteHeadIv = (ImageView) view.findViewById(R.id.item_invite_head_iv);
            mItemInviteAgreeIv = (ImageView) view.findViewById(R.id.item_invite_agree_iv);
            mItemInviteVipSingleIv = (ImageView) view.findViewById(R.id.item_invite_vip_single_iv);
            itemInvitePrice = (TextView) view.findViewById(R.id.item_invite_price);
        }
    }
}
