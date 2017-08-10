package com.neishenme.what.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.BuyVipResponse;
import com.neishenme.what.bean.ConvertVipCardResponse;

import java.util.List;

import static com.neishenme.what.R.id.view;

/**
 * Created by Administrator on 2017/5/18.
 * <p>
 * 兑换会员的适配器
 */
public class ConverVipAdapter extends BaseAdapter {
    private double mDeduction;
    private Context mContext;
    private List<ConvertVipCardResponse.DataBean.VipCardsBean> vipCardsList;
    private int selectedPosition = 0;

    public ConverVipAdapter(Context mContext, List<ConvertVipCardResponse.DataBean.VipCardsBean> vipCardsList,
                            double deduction) {
        this.mContext = mContext;
        this.vipCardsList = vipCardsList;
        this.mDeduction = deduction;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        if (vipCardsList == null || vipCardsList.size() == 0) {
            return 0;
        }
        return vipCardsList.size();
    }

    @Override
    public ConvertVipCardResponse.DataBean.VipCardsBean getItem(int position) {
        return vipCardsList.get(position);
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
            view = View.inflate(mContext, R.layout.item_convert_vip, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        ConvertVipCardResponse.DataBean.VipCardsBean item = getItem(position);
        holder.tvOldPrice.setText(item.getOldPrice() + "");
        holder.tvSignTwo.setText(item.getName() + ": " + item.getPrice() + "元");
        holder.tvRight.setText(item.getIntro());
        holder.mItemConvertVipConvertPrice.setText(String.format("可抵折扣 %s 元", mDeduction + ""));

        if (position == selectedPosition) {
            holder.cb170.setChecked(true);
        } else {
            holder.cb170.setChecked(false);
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
        private TextView mItemConvertVipConvertPrice;

        public ViewHolder(View view) {

            tvSignTwo = (TextView) view.findViewById(R.id.tv_sign_two);
            tvOldPrice = (TextView) view.findViewById(R.id.tv_old_price);
            tvRight = (TextView) view.findViewById(R.id.tv_right);
            cb170 = (CheckBox) view.findViewById(R.id.cb_170);
            mItemConvertVipConvertPrice = (TextView) view.findViewById(R.id.item_convert_vip_convert_price);
        }
    }
}
