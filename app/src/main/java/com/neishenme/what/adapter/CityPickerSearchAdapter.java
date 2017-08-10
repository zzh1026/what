package com.neishenme.what.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.CityPickerActivity;
import com.neishenme.what.bean.CityPickerResponse;
import com.neishenme.what.bean.CitySearchResponse;
import com.neishenme.what.view.ListViewAdjustHeight;

import java.util.List;

import static android.R.string.no;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/12.
 */

public class CityPickerSearchAdapter extends BaseAdapter {
    private CityPickerActivity mCityPickerActivity;
    private List<CitySearchResponse.DataBean.CitylistBean> mSearchList;
    private LayoutInflater mInflater;

    public CityPickerSearchAdapter(CityPickerActivity mCityPickerActivity, List<CitySearchResponse.DataBean.CitylistBean> searchList) {
        this.mCityPickerActivity = mCityPickerActivity;
        this.mInflater = LayoutInflater.from(mCityPickerActivity);
        this.mSearchList = searchList;
    }

    public void changeData(List<CitySearchResponse.DataBean.CitylistBean> searchList) {
        if (mSearchList == null) {
            this.mSearchList = searchList;
        } else {
            mSearchList.clear();
            mSearchList.addAll(searchList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSearchList == null || mSearchList.size() == 0 ? 0 : mSearchList.size();
    }

    @Override
    public CitySearchResponse.DataBean.CitylistBean getItem(int position) {
        return mSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityViewHolder holder;
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_city_picker_type_2, null);
            holder = new CityViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (CityViewHolder) view.getTag();
        }
        CitySearchResponse.DataBean.CitylistBean searchCityInfo = getItem(position);
        holder.mSearchCity.setText(searchCityInfo.getName());
        return view;
    }

    public static class CityViewHolder {
        TextView mSearchCity;

        public CityViewHolder(View view) {
            mSearchCity = (TextView) view.findViewById(R.id.item_city_type_2_child);
        }
    }
}
