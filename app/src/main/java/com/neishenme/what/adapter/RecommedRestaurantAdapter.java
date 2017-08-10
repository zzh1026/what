package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.bean.RestaurantListResponse;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class RecommedRestaurantAdapter extends BaseAdapter {
    private Context context;
    private List<RestaurantListResponse.DataEntity.ServiceEntity> serviceBeans;

    public RecommedRestaurantAdapter(Context context, List<RestaurantListResponse.DataEntity.ServiceEntity> serviceBeans) {
        this.context = context;
        this.serviceBeans = serviceBeans;
    }

    public void setRecommedRestaurant(List<RestaurantListResponse.DataEntity.ServiceEntity> serviceBeans) {
        this.serviceBeans = serviceBeans;
    }

    @Override
    public int getCount() {
        if (serviceBeans == null || serviceBeans.size() == 0) {
            return 0;
        }
        return serviceBeans.size();
    }

    @Override
    public RestaurantListResponse.DataEntity.ServiceEntity getItem(int position) {
        return serviceBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_restau, null);
            holder.itemFrist = convertView.findViewById(R.id.item_frist);
            holder.ivRecoPic = (ImageView) convertView.findViewById(R.id.iv_reco_pic);
            holder.tvRecoLimit = (TextView) convertView.findViewById(R.id.tv_reco_limit);
            holder.tvRecoName = (TextView) convertView.findViewById(R.id.tv_reco_name);
            holder.tvRecoAddress = (TextView) convertView.findViewById(R.id.tv_reco_address);
            holder.ivRecoNew = (ImageView) convertView.findViewById(R.id.iv_reco_new);
            holder.tvRecoPrice = (TextView) convertView.findViewById(R.id.tv_reco_price);
            holder.tvRecoMap = (TextView) convertView.findViewById(R.id.tv_reco_map);
            holder.tvRecoDistance = (TextView) convertView.findViewById(R.id.tv_reco_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RestaurantListResponse.DataEntity.ServiceEntity serviceBean = getItem(position);

        String services_logo = serviceBean.getServices_logo();
        if (!TextUtils.isEmpty(services_logo))
            HttpLoader.getImageLoader().get(services_logo, ImageLoader.getImageListener(
                    holder.ivRecoPic, R.drawable.picture_moren, R.drawable.picture_moren));
        else
            holder.ivRecoPic.setImageResource(R.drawable.picture_moren);

        String services_isNew = serviceBean.getServices_isNew();
        if (!TextUtils.isEmpty(services_isNew) && "1".equals(services_isNew)) {
            holder.ivRecoNew.setVisibility(View.VISIBLE);
        } else {
            holder.ivRecoNew.setVisibility(View.GONE);
        }

        String services_mark = serviceBean.getServices_mark();
        if (!TextUtils.isEmpty(services_mark)) {
            char[] chars = services_mark.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char aChar : chars) {
                sb.append(aChar);
                sb.append("\n");
            }
            holder.tvRecoLimit.setVisibility(View.VISIBLE);
            holder.tvRecoLimit.setText(sb.toString());
            if ((position + 1) % 2 == 1) {
                holder.tvRecoLimit.setBackgroundResource(R.drawable.icon_limit2x);
            } else if ((position + 1) % 2 == 0) {
                holder.tvRecoLimit.setBackgroundResource(R.drawable.icon_excellent2x);
            }
        } else {
            holder.tvRecoLimit.setVisibility(View.INVISIBLE);
        }

        holder.tvRecoName.setText(serviceBean.getStore_name());
        holder.tvRecoAddress.setText(serviceBean.getServices_name());

        holder.tvRecoPrice.setText(((int) serviceBean.getServices_price()) + "");
        holder.tvRecoMap.setText(serviceBean.getStore_addrDetail());
        holder.tvRecoDistance.setText(LocationToBaiduMap.getNum(serviceBean.getDistance()));


        return convertView;
    }


    class ViewHolder {
        private View itemFrist;
        private ImageView ivRecoPic;
        private TextView tvRecoLimit;
        private TextView tvRecoName;
        private TextView tvRecoAddress;
        private ImageView ivRecoNew;
        private TextView tvRecoPrice;
        private TextView tvRecoMap;
        private TextView tvRecoDistance;

    }
}
