package com.neishenme.what.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.dialog.InviteTimeOutDialog;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class InviteTimeOutAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mReasonLists;
    private LayoutInflater mInflater;
    private InviteTimeOutDialog inviteTimeOutDialog;

    private int mCurrentPosition = -1;

    public InviteTimeOutAdapter(InviteTimeOutDialog inviteTimeOutDialog, Context mContext, List<String> reasonLists) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.inviteTimeOutDialog = inviteTimeOutDialog;
        this.mReasonLists = reasonLists;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mReasonLists != null && mReasonLists.size() != 0) {
            return mReasonLists.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        return mReasonLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_time_out_reason, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        String reason = getItem(position);

        holder.mItemTimeOutReason.setText(reason);

        if (position == mCurrentPosition) {
            holder.mItemTimeOutCb.setChecked(true);
        } else {
            holder.mItemTimeOutCb.setChecked(false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = position;
                notifyDataSetChanged();
                inviteTimeOutDialog.changeDate(position);
            }
        });
        return view;
    }

    static class ViewHolder {
        private CheckBox mItemTimeOutCb;
        private TextView mItemTimeOutReason;

        public ViewHolder(View view) {
            mItemTimeOutCb = (CheckBox) view.findViewById(R.id.item_time_out_cb);
            mItemTimeOutReason = (TextView) view.findViewById(R.id.item_time_out_reason);
        }
    }
}
