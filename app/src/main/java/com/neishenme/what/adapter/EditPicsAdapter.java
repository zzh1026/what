package com.neishenme.what.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/23.
 */
public class EditPicsAdapter extends BaseAdapter {
    private int[] res;
    private Context mContext;

    public EditPicsAdapter(int[] res, Context mContext) {
        this.res = res;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return res[position];
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pic, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(res[position]);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
