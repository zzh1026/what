package com.neishenme.what.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.PickMoneyListResponse;
import com.neishenme.what.utils.TimeUtils;

import java.util.List;

import static com.neishenme.what.activity.ZXingGetRichActivity.result;

/**
 * 作者：zhaozh create on 2016/5/10 19:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class PickMoneyAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickMoneyListResponse.DataBean.ListBean> mDatas;
    private LayoutInflater mInflater;
    private boolean mIsVip;

    public PickMoneyAdapter(Context mContext, List<PickMoneyListResponse.DataBean.ListBean> mDatas, boolean mIsVip) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mIsVip = mIsVip;
    }

    @Override
    public int getCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size();
    }

    @Override
    public PickMoneyListResponse.DataBean.ListBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        PickMoneyViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_pick_money_list, parent, false);
            holder = new PickMoneyViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (PickMoneyViewHolder) view.getTag();
        }
        final PickMoneyListResponse.DataBean.ListBean pickInfo = getItem(position);
        holder.mItemPickMoneyTime.setText(
                TimeUtils.getTime(pickInfo.getCreateTime(), TimeUtils.DATE_FORMAT_PICK_MONEY));
        holder.mItemPickMoneyPrice.setText(String.valueOf(pickInfo.getAmount()));
        holder.mItemPickMoneyCheck.setChecked(pickInfo.isCheckd());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkd = pickInfo.isCheckd();
                //不是会员:只能选中一个 , 未选中,说明可能是其他选中,需要重置所有,如果是选中说明重复点击,不需要重置
                if (!mIsVip && !checkd) {
                    changeAllInfo(false);
                }
                pickInfo.setCheckd(!checkd);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    public void changeAllInfo(boolean isChecked) {
        for (PickMoneyListResponse.DataBean.ListBean listBean : mDatas) {
            listBean.setCheckd(isChecked);
        }
    }

    public String getSelectedIds() {
        String result = "";
        for (PickMoneyListResponse.DataBean.ListBean listBean : mDatas) {
            if (listBean.isCheckd()) {  //选中
                result = result + listBean.getId() + ",";
                if (!mIsVip) {  //如果不是vip
                    break;
                }
            }
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public double getSelectedMoneys() {
        double result = 0;
        for (PickMoneyListResponse.DataBean.ListBean listBean : mDatas) {
            if (listBean.isCheckd()) {  //选中
                result += listBean.getAmount();
                if (!mIsVip) {  //如果不是vip
                    break;
                }
            }
        }
        return result;
    }

    public boolean isSelectedAll() {
        for (PickMoneyListResponse.DataBean.ListBean listBean : mDatas) {
            if (!listBean.isCheckd()) {  //有未选中的表示不是全部选中
                return false;
            }
        }
        return true;
    }

    static class PickMoneyViewHolder {
        TextView mItemPickMoneyTime;
        TextView mItemPickMoneyPrice;
        CheckBox mItemPickMoneyCheck;

        public PickMoneyViewHolder(View view) {
            mItemPickMoneyTime = (TextView) view.findViewById(R.id.item_pick_money_time);
            mItemPickMoneyPrice = (TextView) view.findViewById(R.id.item_pick_money_price);
            mItemPickMoneyCheck = (CheckBox) view.findViewById(R.id.item_pick_money_check);
        }
    }
}
