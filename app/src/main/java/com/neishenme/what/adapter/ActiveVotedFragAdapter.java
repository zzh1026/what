package com.neishenme.what.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.bean.ActiveMyJoinResponse;

import java.util.List;

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
public class ActiveVotedFragAdapter extends BaseAdapter {
    private Context mContext;
    private List<ActiveMyJoinResponse.DataBean.ListBean> joinListInfos;

    public ActiveVotedFragAdapter(Context context, List<ActiveMyJoinResponse.DataBean.ListBean> joinListInfos) {
        this.mContext = context;
        this.joinListInfos = joinListInfos;
    }

    @Override
    public int getCount() {
        if (joinListInfos == null || joinListInfos.size() == 0) {
            return 0;
        }
        return joinListInfos.size();
    }

    @Override
    public ActiveMyJoinResponse.DataBean.ListBean getItem(int position) {
        return joinListInfos.get(position);
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
            view = View.inflate(mContext, R.layout.item_active_my_voted_frag, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        ActiveMyJoinResponse.DataBean.ListBean itemUserInfo = getItem(position);
        holder.mTvFragUserName.setText(itemUserInfo.getName());
        holder.mTvFragHeartNumber.setText(itemUserInfo.getTickets() + "");
        if (itemUserInfo.getTickets() == 1) {
            holder.mTvFragInviteCode.setText("邀请码" + itemUserInfo.getTicketsPrefix() +
                    "-" + itemUserInfo.getStartNum());
        } else {
            holder.mTvFragInviteCode.setText(
                    "邀请码"
                    + itemUserInfo.getTicketsPrefix()
                    + "-" + itemUserInfo.getStartNum()
                    + "-" + (itemUserInfo.getStartNum()
                    + itemUserInfo.getTickets() - 1));
        }

        return view;
    }

    static class ViewHolder {
        private TextView mTvFragUserName;
        private TextView mTvFragHeartNumber;
        private TextView mTvFragInviteCode;

        public ViewHolder(View view) {
            mTvFragUserName = (TextView) view.findViewById(R.id.tv_frag_user_name);
            mTvFragHeartNumber = (TextView) view.findViewById(R.id.tv_frag_heart_number);
            mTvFragInviteCode = (TextView) view.findViewById(R.id.tv_frag_invite_code);
        }
    }
}
