package com.neishenme.what.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.ActiveMyTakeResponse;

import java.util.List;

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
public class ActiveAddFragAdapter extends BaseAdapter {
    Context mContext;
    private List<ActiveMyTakeResponse.DataBean.ListBean> joinerInfos;

    public ActiveAddFragAdapter(Context context, List<ActiveMyTakeResponse.DataBean.ListBean> joinerInfos) {
        this.mContext = context;
        this.joinerInfos = joinerInfos;
    }

    @Override
    public int getCount() {
        if (joinerInfos != null && joinerInfos.size() != 0) {
            return joinerInfos.size();
        }
        return 0;
    }

    @Override
    public ActiveMyTakeResponse.DataBean.ListBean getItem(int position) {
        return joinerInfos.get(position);
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
            view = View.inflate(mContext, R.layout.item_active_my_add_frag, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        ActiveMyTakeResponse.DataBean.ListBean listBean = getItem(position);
        holder.mTvFragUserName.setText(listBean.getName());
        holder.mTvFragHeartNumber.setText(listBean.getTickets() + "");
        return view;
    }

    static class ViewHolder {
        private TextView mTvFragUserName;
        private TextView mTvFragHeartNumber;

        public ViewHolder(View view) {
            mTvFragUserName = (TextView) view.findViewById(R.id.tv_frag_user_name);
            mTvFragHeartNumber = (TextView) view.findViewById(R.id.tv_frag_heart_number);
        }
    }
}
