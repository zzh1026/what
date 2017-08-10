package com.neishenme.what.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.BuyVipResponse;

/**
 * Created by Administrator on 2016/5/26.
 */
public class BuyVipAdapter extends BaseAdapter {
    private Context mContext;
    private BuyVipResponse response;
    private int selectedPosition = 0;

    public BuyVipAdapter(Context mContext, BuyVipResponse response) {
        this.mContext = mContext;
        this.response = response;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        return response.getData().getVipCards().size();
    }

    @Override
    public BuyVipResponse.DataBean.VipCardsBean getItem(int position) {
        return response.getData().getVipCards().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_buy_vip, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        BuyVipResponse.DataBean.VipCardsBean item = getItem(position);
        holder.tvOldPrice.setText(item.getOldPrice() + "");
        holder.tvSignTwo.setText(item.getName() + ": " + item.getPrice() + "元");
        holder.tvRight.setText(item.getIntro());

        if (position == selectedPosition) {
            holder.cb170.setChecked(true);
            holder.cb170.setBackground(mContext.getResources().getDrawable(R.drawable.buy_vip_press));
        } else {
            holder.cb170.setChecked(false);
            holder.cb170.setBackground(mContext.getResources().getDrawable(R.drawable.buy_vip_normal));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        return view;
    }

    static class ViewHolder {
        private TextView tvSignTwo;
        private TextView tvOldPrice;
        private TextView tvRight;
        private CheckBox cb170;

        public ViewHolder(View view) {

            tvSignTwo = (TextView) view.findViewById(R.id.tv_sign_two);
            tvOldPrice = (TextView) view.findViewById(R.id.tv_old_price);
            tvRight = (TextView) view.findViewById(R.id.tv_right);
            cb170 = (CheckBox) view.findViewById(R.id.cb_170);
        }
    }
}
