package com.neishenme.what.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;

import java.util.List;

/**
 * 这个类的作用是  提现界面的选择提现金额条目的 recyclerview 的适配器
 * <p>
 * Created by zhaozh on 2017/5/22.
 */

public class PickMoneyListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;

    public PickMoneyListAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    public void upDateInfo(List<String> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public PickMoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mPickView = mInflater.inflate(R.layout.item_pick_money_list, parent, false);
        PickMoneyViewHolder mViewHolder = new PickMoneyViewHolder(mPickView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PickMoneyViewHolder moneyViewHolder = (PickMoneyViewHolder) holder;
        String str = mDatas.get(position);
        moneyViewHolder.mItemPickMoneyTime.setText("2017.6." + str);
        moneyViewHolder.mItemPickMoneyPrice.setText(str + "00元");
        moneyViewHolder.mItemPickMoneyCheck.setChecked(true);

    }

    @Override
    public int getItemCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size();
    }


    class PickMoneyViewHolder extends RecyclerView.ViewHolder {
        TextView mItemPickMoneyTime;
        TextView mItemPickMoneyPrice;
        CheckBox mItemPickMoneyCheck;

        public PickMoneyViewHolder(View view) {
            super(view);
            mItemPickMoneyTime = (TextView) view.findViewById(R.id.item_pick_money_time);
            mItemPickMoneyPrice = (TextView) view.findViewById(R.id.item_pick_money_price);
            mItemPickMoneyCheck = (CheckBox) view.findViewById(R.id.item_pick_money_check);
        }
    }
}
