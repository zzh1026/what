package com.neishenme.what.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.GetServiceListResponse;
import com.neishenme.what.fragment.ReleaseNormalFragment;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

import static android.R.string.no;

/**
 * 作者：zhaozh create on 2016/5/10 19:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个发布界面点击价位后获取到数据的适配器
 * .
 * 其作用是 :
 */
public class HomePublishServiceAdapter extends BaseAdapter {
    List<GetServiceListResponse.DataBean.ServiceBean> servicesLists;
    private ReleaseNormalFragment releaseNormalFragment;
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private Context mContext;

    private int mCurrentSelected = -1;

    public HomePublishServiceAdapter(Context context, ReleaseNormalFragment releaseNormalFragment, List<GetServiceListResponse.DataBean.ServiceBean> servicesLists, LinearLayout mGallery, LayoutInflater mInflater) {
        this.servicesLists = servicesLists;
        this.releaseNormalFragment = releaseNormalFragment;
        this.mGallery = mGallery;
        this.mInflater = mInflater;
        this.mContext = context;
    }

    public void setPhotos(List<GetServiceListResponse.DataBean.ServiceBean> servicesLists) {
        this.servicesLists = servicesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (servicesLists == null || servicesLists.size() == 0) {
            return 0;
        }
        return servicesLists.size();
    }

    @Override
    public GetServiceListResponse.DataBean.ServiceBean getItem(int position) {
        return servicesLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_home_publish_service, mGallery, false);
            holder = new ViewHolder(view);

            //指定Item的宽高
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int width = (int) (168 * density);
            int hight = (int) (171 * density);
            view.setLayoutParams(new AbsListView.LayoutParams(width, hight));

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        final GetServiceListResponse.DataBean.ServiceBean item = getItem(position);
        String servicesLogo = item.getServices_logo();
        if (!TextUtils.isEmpty(servicesLogo)) {
            HttpLoader.getImageLoader().get(servicesLogo,
                    ImageLoader.getImageListener(holder.mItemPublishServiceLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            holder.mItemPublishServiceLogo.setImageResource(R.drawable.picture_moren);
        }

        holder.mItemPublishServiceName.setText(item.getServices_name());
        holder.mItemPublishServicePrice.setText(item.getServices_price() + "");

        if (position == mCurrentSelected) {
            holder.mItemPublishServiceSelect.setImageResource(R.drawable.item_publish_service_selected);
            holder.mItemPublishServiceTop.setVisibility(View.VISIBLE);
        } else {
            holder.mItemPublishServiceSelect.setImageResource(R.drawable.item_publish_service_unselected);
            holder.mItemPublishServiceTop.setVisibility(View.INVISIBLE);
        }

        holder.mItemPublishServiceSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentSelected = position;
                releaseNormalFragment.getCoupons();
                notifyDataSetChanged();
            }
        });
        holder.mItemPublishServiceSelect.setTag(position);
        return view;
    }

    public int getmCurrentSelected() {
        return mCurrentSelected;
    }

    public void setmCurrentSelected(int mCurrentSelected) {
        this.mCurrentSelected = mCurrentSelected;
    }

    static class ViewHolder {
        ImageView mItemPublishServiceLogo;
        ImageView mItemPublishServiceSelect;
        TextView mItemPublishServiceName;
        TextView mItemPublishServicePrice;
        TextView mItemPublishServiceTop;

        public ViewHolder(View view) {
            mItemPublishServiceLogo = (ImageView) view.findViewById(R.id.item_publish_service_logo);
            mItemPublishServiceSelect = (ImageView) view.findViewById(R.id.item_publish_service_select);
            mItemPublishServiceName = (TextView) view.findViewById(R.id.item_publish_service_name);
            mItemPublishServicePrice = (TextView) view.findViewById(R.id.item_publish_service_price);
            mItemPublishServiceTop = (TextView) view.findViewById(R.id.item_publish_service_top);

        }
    }
}
