package com.neishenme.what.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.CityPickerActivity;
import com.neishenme.what.bean.CityPickerResponse;
import com.neishenme.what.view.ListViewAdjustHeight;

import java.util.List;

import static com.neishenme.what.R.id.view;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/12.
 */

public class CityPickerAllAdapter extends BaseAdapter {
    private CityPickerActivity mCityPickerActivity;
    private List<CityPickerResponse.DataBean.AllCityListBean> mCityList;
    private LayoutInflater mInflater;

    public CityPickerAllAdapter(CityPickerActivity mCityPickerActivity, List<CityPickerResponse.DataBean.AllCityListBean> allCityList) {
        this.mCityPickerActivity = mCityPickerActivity;
        this.mInflater = LayoutInflater.from(mCityPickerActivity);
        this.mCityList = allCityList;
    }

    @Override
    public int getCount() {
        return mCityList == null || mCityList.size() == 0 ? 0 : mCityList.size();
    }

    @Override
    public CityPickerResponse.DataBean.AllCityListBean getItem(int position) {
        return mCityList.get(position);
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
            view = mInflater.inflate(R.layout.item_city_picker_parent, null);
            holder = new CityViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (CityViewHolder) view.getTag();
        }
        final CityPickerResponse.DataBean.AllCityListBean itemInfos = getItem(position);
        CityPickerCityChildAdapter mCityPickerCityChildAdapter = new CityPickerCityChildAdapter(mCityPickerActivity, itemInfos);
        holder.mItemCityPickChild.setAdapter(mCityPickerCityChildAdapter);
        holder.mItemCityPickChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    CityPickerResponse.DataBean.AllCityListBean.ChildsBean childsBean = itemInfos.getChilds().get(position - 1);
                    mCityPickerActivity.setLocation(childsBean.getId(), childsBean.getName());
                }
            }
        });
        return view;
    }

    public static class CityViewHolder {
        ListViewAdjustHeight mItemCityPickChild;

        public CityViewHolder(View view) {
            mItemCityPickChild = (ListViewAdjustHeight) view.findViewById(R.id.item_city_pick_child);
        }
    }
}
