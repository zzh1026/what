package com.neishenme.what.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.InviteDetailResponse;
import com.neishenme.what.net.HttpLoader;

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
 *
 * 就得发布者 中的加入者头像适配器 ,已弃用, 新的见 @see {@link InviteInviterAdapter}
 */
@Deprecated
public class InviteInviterOldAdapter extends BaseAdapter {
    private List<InviteDetailResponse.DataEntity.JoinersEntity> joiners;
    private Context context;


    public InviteInviterOldAdapter(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners, Context context) {
        this.joiners = joiners;
        this.context = context;
    }

    public void setJoiners(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners, Context context) {
        this.joiners = joiners;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (joiners == null || joiners.size() == 0) {
            return 0;
        }
        return joiners.size();
    }

    @Override
    public InviteDetailResponse.DataEntity.JoinersEntity getItem(int position) {
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

        String url = getItem(position).getThumbnailslogo();
        holder.mItemInviteHeadIv.setTag(url);

        if (holder.mItemInviteHeadIv.getTag() != null && holder.mItemInviteHeadIv.getTag().equals(url)) {
            HttpLoader.getImageLoader().get(url,
                    ImageLoader.getImageListener(holder.mItemInviteHeadIv, R.drawable.picture_moren, R.drawable.picture_moren));
            int acceptType = getItem(position).getAcceptType();
            if (1 == acceptType) {
                holder.mItemInviteAgreeIv.setVisibility(View.VISIBLE);
            } else {
                holder.mItemInviteAgreeIv.setVisibility(View.INVISIBLE);
            }
            int isfranchise = getItem(position).getIsfranchise();
            if (1 == isfranchise) {
                holder.mItemInviteVipSingleIv.setVisibility(View.VISIBLE);
            } else {
                holder.mItemInviteVipSingleIv.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    static class ViewHolder {
        ImageView mItemInviteHeadIv;
        ImageView mItemInviteAgreeIv;
        ImageView mItemInviteVipSingleIv;


        public ViewHolder(View view) {
            mItemInviteHeadIv = (ImageView) view.findViewById(R.id.item_invite_head_iv);
            mItemInviteAgreeIv = (ImageView) view.findViewById(R.id.item_invite_agree_iv);
            mItemInviteVipSingleIv = (ImageView) view.findViewById(R.id.item_invite_vip_single_iv);
        }
    }
}
