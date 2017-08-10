package com.neishenme.what.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import java.util.List;

import static com.neishenme.what.R.id.view;

/**
 * 作者：zhaozh create on 2016/12/16 15:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个消费记录的适配器类
 * .
 * 其作用是 :
 * 为我的钱包界面显示进行适配.
 */
public class WalletRecordsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList;

    public WalletRecordsListAdapter(Context mContext, List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList) {
        this.mContext = mContext;
        this.mRecordsList = mRecordsList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setRecordsList(List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList) {
        this.mRecordsList = mRecordsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mRecordsList.size();
    }

    @Override
    public RecordsListResponse.DataEntity.AccountsEntity getItem(int position) {
        return mRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_recordslist_mywallet, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        RecordsListResponse.DataEntity.AccountsEntity item = getItem(position);
//        holder.mTvItemPrice.setText(String.valueOf(item.getMoney()));
        holder.mTvItemPrice.setText(NSMTypeUtils.getGreatPrice(item.getMoney()));
        holder.mTvItemTime.setText(TimeUtils.getTime(item.getCreateTime(), TimeUtils.DATE_FORMAT_NSM));
        holder.mTvItemType.setText(NSMTypeUtils.getRocordOrigin(item.getOrigin()));
        return view;
    }

    static class ViewHolder {
        TextView mTvItemPrice;
        TextView mTvItemTime;
        TextView mTvItemType;

        public ViewHolder(View view) {
            mTvItemPrice = (TextView) view.findViewById(R.id.tv_item_price);
            mTvItemTime = (TextView) view.findViewById(R.id.tv_item_time);
            mTvItemType = (TextView) view.findViewById(R.id.tv_item_type);
        }
    }
}
