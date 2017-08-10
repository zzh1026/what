package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.RestaurantDetailResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<RestaurantDetailResponse.DataEntity.ServicemenuEntity> dataList;

    public MenuAdapter(Context context, List<RestaurantDetailResponse.DataEntity.ServicemenuEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, null, true);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        RestaurantDetailResponse.DataEntity.ServicemenuEntity serviceMenu = dataList.get(position);
        if (serviceMenu != null) {
            if (!TextUtils.isEmpty(serviceMenu.getName())) {
                holder.tvCHName.setVisibility(View.VISIBLE);
                holder.tvCHName.setText(serviceMenu.getName());
            } else {
                holder.tvCHName.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(serviceMenu.getEngname())) {
                holder.tvEGName.setVisibility(View.VISIBLE);
                holder.tvEGName.setText(serviceMenu.getEngname());
            } else {
                holder.tvEGName.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tvCHName;
        private TextView tvEGName;

        public ViewHolder(View view) {
            tvCHName = (TextView) view.findViewById(R.id.tv_ch_name);
            tvEGName = (TextView) view.findViewById(R.id.tv_eg_name);
        }
    }
}
