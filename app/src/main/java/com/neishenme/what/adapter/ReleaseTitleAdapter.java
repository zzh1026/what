package com.neishenme.what.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.ReleaseQuickActivity;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/7.
 */

public class ReleaseTitleAdapter extends BaseAdapter {
    private ReleaseQuickActivity mMainActivity;
    private List<String> titleLists;

    public ReleaseTitleAdapter(ReleaseQuickActivity mMainActivity, List<String> titleLists) {
        this.mMainActivity = mMainActivity;
        this.titleLists = titleLists;
    }

    public void bingData(List<String> titleLists) {
        this.titleLists = titleLists;
    }

    @Override
    public int getCount() {
        return titleLists.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mMainActivity, R.layout.item_fill_order_title, null);
        TextView tvListviewItemTitle = (TextView) view.findViewById(R.id.tv_listview_item_title);
        if (position != titleLists.size()) {
            tvListviewItemTitle.setText(titleLists.get(position));
        } else {
            LinearLayout llFillOrderBg = (LinearLayout) view.findViewById(R.id.ll_fill_order_bg);
            llFillOrderBg.setBackgroundColor(Color.parseColor("#33000000"));
            llFillOrderBg.setGravity(Gravity.CENTER);
            tvListviewItemTitle.setGravity(Gravity.CENTER);
            tvListviewItemTitle.setTextSize(12f);
            tvListviewItemTitle.setText("更 换");
        }
        return view;
    }
}
