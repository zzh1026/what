package com.neishenme.what.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/3/9 15:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个消费记录的适配器类
 * .
 * 其作用是 :
 * 为消费记录的界面显示进行适配.
 */
public class RecordListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList;

    ViewHolder holder;


    public RecordListAdapter(Context mContext, List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList) {
        this.mContext = mContext;
        this.mRecordsList = mRecordsList;
    }

    public void setRecordsList(List<RecordsListResponse.DataEntity.AccountsEntity> mRecordsList) {
        this.mRecordsList = mRecordsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mRecordsList == null || mRecordsList.size() == 0) {
            return 0;
        }
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
            view = View.inflate(mContext, R.layout.item_recordslist, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        RecordsListResponse.DataEntity.AccountsEntity item = getItem(position);
        if (item.getType() == 1) {
            holder.itemRecordsTypeIv.setImageResource(R.drawable.wallet_records_add);
        } else {
            holder.itemRecordsTypeIv.setImageResource(R.drawable.wallet_records_remove);
        }
//        holder.tvItemPrice.setText(String.valueOf(item.getMoney()));
        holder.tvItemPrice.setText(NSMTypeUtils.getGreatPrice(item.getMoney()));
        holder.tvItemTime.setText(TimeUtils.getTime(item.getCreateTime(), TimeUtils.DATE_FORMAT_NSM));
        holder.tvItemType.setText(NSMTypeUtils.getRocordOrigin(item.getOrigin()));
        return view;
    }

    static class ViewHolder {
        TextView tvItemPrice;
        TextView tvItemTime;
        TextView tvItemType;
        ImageView itemRecordsTypeIv;


        public ViewHolder(View view) {
            tvItemPrice = (TextView) view.findViewById(R.id.tv_item_price);
            itemRecordsTypeIv = (ImageView) view.findViewById(R.id.item_records_type_iv);
            tvItemTime = (TextView) view.findViewById(R.id.tv_item_time);
            tvItemType = (TextView) view.findViewById(R.id.tv_item_type);
        }
    }
}
