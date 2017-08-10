package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.neishenme.what.R;
import com.neishenme.what.bean.ActiveMyJoinResponse;

import java.util.List;
import java.util.zip.Inflater;

import static android.view.View.inflate;
import static com.neishenme.what.R.id.view;

/**
 * 作者：zhaozh create on 2016/5/10 19:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SelectedPOIAdapter extends BaseAdapter {
    private Context mContext;
    private List<PoiInfo> mPoiInfos;

    public SelectedPOIAdapter(Context context, List<PoiInfo> mPoiInfos) {
        this.mContext = context;
        this.mPoiInfos = mPoiInfos;
    }

    public void updatePOIInfos(List<PoiInfo> mPoiInfos) {
        this.mPoiInfos = mPoiInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPoiInfos.size();
    }

    @Override
    public PoiInfo getItem(int position) {
        return mPoiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_selected_place_poi, parent, false);
//            view = View.inflate(mContext, R.layout.item_selected_place_poi, parent);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        PoiInfo itemInfo = getItem(position);
//        if (position == 0) {
//            holder.mItemPoiName.setText("当前地图位置");
//        } else {
        holder.mItemPoiName.setText(itemInfo.name);
//        }
        if (TextUtils.isEmpty(itemInfo.address) || TextUtils.isEmpty(itemInfo.address.trim())) {
            holder.mItemPoiAddress.setText(itemInfo.name);
        } else {
            holder.mItemPoiAddress.setText(itemInfo.address);
        }
        holder.mItemPoiLingBg.setVisibility(position == getCount() - 1 ? View.INVISIBLE : View.VISIBLE);
        return view;
    }

    static class ViewHolder {
        private TextView mItemPoiName;
        private TextView mItemPoiAddress;
        private View mItemPoiLingBg;

        public ViewHolder(View view) {
            mItemPoiName = (TextView) view.findViewById(R.id.item_poi_name);
            mItemPoiAddress = (TextView) view.findViewById(R.id.item_poi_address);
            mItemPoiLingBg = (View) view.findViewById(R.id.item_poi_ling_bg);
        }
    }
}
