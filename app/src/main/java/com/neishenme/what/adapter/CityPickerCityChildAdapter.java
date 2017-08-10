package com.neishenme.what.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.CityPickerActivity;
import com.neishenme.what.bean.CityPickerResponse;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/12.
 */

public class CityPickerCityChildAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private CityPickerActivity mCityPickerActivity;
    private LayoutInflater mInflater;
    private String title;
    private List<CityPickerResponse.DataBean.AllCityListBean.ChildsBean> citys;

    public CityPickerCityChildAdapter(CityPickerActivity mCityPickerActivity, CityPickerResponse.DataBean.AllCityListBean mChildCitys) {
        this.mCityPickerActivity = mCityPickerActivity;
        this.mInflater = LayoutInflater.from(mCityPickerActivity);
        title = mChildCitys.getParent();
        citys = mChildCitys.getChilds();
    }

    @Override
    public int getCount() {
        return citys == null || citys.size() == 0 ? 0 : citys.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == citys.size()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public String getItem(int position) {
        return position == 0 ? title : citys.get(position - 1).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityViewHolder holder;
        TextView tv;
        int viewType = getItemViewType(position);

        switch (viewType) {
            case 0: //parent
                convertView = mInflater.inflate(R.layout.item_city_picker_type_1, null);
                tv = (TextView) convertView.findViewById(R.id.item_city_type_1_parent);
                tv.setText(title);
                break;
            case 1: //childs
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_city_picker_type_2, null);
                    holder = new CityViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (CityViewHolder) convertView.getTag();
                }
                String cityName = getItem(position);
                holder.textView.setText(cityName);
                break;
            case 2: //otherchilds
                convertView = mInflater.inflate(R.layout.item_city_picker_type_3, null);
                tv = (TextView) convertView.findViewById(R.id.item_city_type_3_child);
                tv.setText(getItem(position));
                break;
        }
        return convertView;
    }

    public static class CityViewHolder {
        TextView textView;

        public CityViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.item_city_type_2_child);
        }
    }
}
