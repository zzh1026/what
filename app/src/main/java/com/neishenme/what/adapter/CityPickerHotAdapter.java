package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveActivity;
import com.neishenme.what.activity.CityPickerActivity;
import com.neishenme.what.bean.ActiveListResponse;
import com.neishenme.what.bean.CityPickerResponse;
import com.neishenme.what.net.HttpLoader;

import java.util.List;


/**
 * 城市选择所有城市界面适配器
 */
public class CityPickerHotAdapter extends BaseAdapter {
    private Context mContext;
    private List<CityPickerResponse.DataBean.HotCitylistBean> mHotCitys;

    public CityPickerHotAdapter(CityPickerActivity mCityPickerActivity, List<CityPickerResponse.DataBean.HotCitylistBean> hotCitylist) {
        mContext = mCityPickerActivity;
        mHotCitys = hotCitylist;
    }

    public void setData(List<CityPickerResponse.DataBean.HotCitylistBean> hotCitylist) {
        mHotCitys.clear();
        this.mHotCitys = hotCitylist;
    }

    @Override
    public int getCount() {
        if (mHotCitys != null && mHotCitys.size() != 0) {
            return mHotCitys.size();
        }
        return 0;
    }

    @Override
    public CityPickerResponse.DataBean.HotCitylistBean getItem(int position) {
        return mHotCitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_pick_city_hot, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        CityPickerResponse.DataBean.HotCitylistBean itemInfo = getItem(position);
        holder.mItemHot.setText(itemInfo.getName());
        return view;
    }

    static class ViewHolder {
        private TextView mItemHot;

        public ViewHolder(View view) {
            mItemHot = (TextView) view.findViewById(R.id.city_pick_hot_item);
        }
    }
}
