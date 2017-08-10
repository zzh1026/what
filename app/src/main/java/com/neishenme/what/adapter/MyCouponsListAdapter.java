package com.neishenme.what.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.MyCouPonsResponse;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import org.seny.android.utils.DateUtils;

import java.util.List;

import static android.R.attr.type;

/**
 * 作者：zhaozh create on 2016/12/16 15:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个我的优惠券适配器
 * .
 * 其作用是 :
 * 为我的优惠券界面显示进行适配.
 */
public class MyCouponsListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MyCouPonsResponse.DataBean.CouponsBean> mCouponsBean;

    public MyCouponsListAdapter(Context mContext, List<MyCouPonsResponse.DataBean.CouponsBean> mCouponsBean) {
        this.mContext = mContext;
        this.mCouponsBean = mCouponsBean;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setRecordsList(List<MyCouPonsResponse.DataBean.CouponsBean> mCouponsBean) {
        this.mCouponsBean = mCouponsBean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mCouponsBean || mCouponsBean.size() == 0) {
            return 0;
        }
        return mCouponsBean.size();
    }

    @Override
    public MyCouPonsResponse.DataBean.CouponsBean getItem(int position) {
        return mCouponsBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_my_coupons_list_type_money, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        MyCouPonsResponse.DataBean.CouponsBean itemInfo = getItem(position);
        if (0 == itemInfo.getType()) {    //优惠折扣
            holder.itemCouponsType.setText("专属发布折扣券");
            holder.itemCouponsMoney.setText(getCoupNum(itemInfo.getRebate()));
            holder.itemCouponsMoneyImg.setVisibility(View.GONE);
            holder.itemCouponsDiscount.setVisibility(View.VISIBLE);
        } else {    //优惠金额
            holder.itemCouponsType.setText("专属发布优惠券");
            holder.itemCouponsMoney.setText(NSMTypeUtils.getGreatPrice(itemInfo.getCash()));
            holder.itemCouponsMoneyImg.setVisibility(View.VISIBLE);
            holder.itemCouponsDiscount.setVisibility(View.GONE);
        }

        long startTime = itemInfo.getStartTime();
        long endTime = itemInfo.getEndTime();
        holder.itemCouponsTime.setText(
                "优惠券有效期: " + TimeUtils.getTime(startTime, TimeUtils.DATE_FORMAT_MYCOUPONS) +
                        "至" + TimeUtils.getTime(endTime, TimeUtils.DATE_FORMAT_MYCOUPONS));

        int minpurse = itemInfo.getMinpurse();
        if (minpurse == 0) {
            holder.itemCouponsContent2.setText("2.该卷任意金额可用.");
        } else {
            holder.itemCouponsContent2.setText("2.满" + minpurse + "元使用该优惠券.");
        }

        return view;
    }

    private String getCoupNum(int useCouponsRebate) {
        String s = String.valueOf(useCouponsRebate);
        if (s.length() == 2) {
            if (s.endsWith("0")) {
                return s.substring(0, 1);
            } else {
                return String.valueOf(((double) useCouponsRebate) / 10);
            }
        }
        return s;
    }

    static class ViewHolder {
        TextView itemCouponsMoney;
        ImageView itemCouponsMoneyImg;
        TextView itemCouponsDiscount;

        TextView itemCouponsType;

        TextView itemCouponsTime;

        TextView itemCouponsContent1;
        TextView itemCouponsContent2;
        TextView itemCouponsContent3;

        public ViewHolder(View view) {
            itemCouponsMoney = (TextView) view.findViewById(R.id.item_coupons_money);
            itemCouponsMoneyImg = (ImageView) view.findViewById(R.id.item_coupons_money_img);
            itemCouponsDiscount = (TextView) view.findViewById(R.id.item_coupons_discount);

            itemCouponsType = (TextView) view.findViewById(R.id.item_coupons_type);

            itemCouponsTime = (TextView) view.findViewById(R.id.item_coupons_time);

            itemCouponsContent1 = (TextView) view.findViewById(R.id.item_coupons_content_1);
            itemCouponsContent2 = (TextView) view.findViewById(R.id.item_coupons_content_2);
            itemCouponsContent3 = (TextView) view.findViewById(R.id.item_coupons_content_3);
        }
    }
}
