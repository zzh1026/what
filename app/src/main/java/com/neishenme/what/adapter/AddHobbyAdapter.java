package com.neishenme.what.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.AddHobbyActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AddHobbyAdapter extends AddHobbySelectableAdapter {
    private Context mContext;
    private List<String> data;
    private AddHobbyActivity mAddHobbyActivity;

    public AddHobbyAdapter(Context mContext) {
        this.mContext = mContext;
        mAddHobbyActivity = (AddHobbyActivity) mContext;
    }


    public void bindData(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(data.get(position));
        final boolean isChecked = isSelected(data.get(position));
        holder.rlItem.setSelected(isChecked);
        if (holder.rlItem.isSelected()) {
            holder.ivChoose.setVisibility(View.VISIBLE);
            holder.tv.setTextColor(Color.parseColor("#303030"));
        } else {
            holder.ivChoose.setVisibility(View.GONE);
            holder.tv.setTextColor(Color.parseColor("#858585"));
        }
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedList().size() > AddHobbyActivity.ADD_HOBBY_NUMBER - 1 && !isChecked) {
                    mAddHobbyActivity.showToastInfo("最多选" + AddHobbyActivity.ADD_HOBBY_NUMBER + "项");
                } else {
                    toggleSelection(data.get(position));
                    notifyDataSetChanged();
                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tv;
        private ImageView ivChoose;
        private RelativeLayout rlItem;

        public ViewHolder(View view) {
            rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
            tv = (TextView) view.findViewById(R.id.tv);
            ivChoose = (ImageView) view.findViewById(R.id.iv_choose);
        }
    }


}
