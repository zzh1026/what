package com.neishenme.what.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.MyTripActivity;
import com.neishenme.what.bean.CancelMyInvite;
import com.neishenme.what.bean.MyOrderListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.fragment.HomeTrivelFragment;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class HomeMyTripAdapter extends BaseAdapter implements HttpLoader.ResponseListener {
    private Context mContext;
    private MainActivity activity;
    private List<MyOrderListResponse.DataBean.AllJourneyBean> mAllJourneyLists;
    private LayoutInflater mInflater;
    private HomeTrivelFragment mHomeTrivelFragment;
    private String mOrderInviteId;  //标记通知栏到达的order标记

    public HomeMyTripAdapter(Context context, HomeTrivelFragment mHomeTrivelFragment,
                             List<MyOrderListResponse.DataBean.AllJourneyBean> mAllJourneyLists,
                             String mOrderInviteId) {
        mContext = context;
        this.mAllJourneyLists = mAllJourneyLists;
        this.activity = (MainActivity) context;
        this.mHomeTrivelFragment = mHomeTrivelFragment;
        mInflater = LayoutInflater.from(context);
        this.mOrderInviteId = mOrderInviteId;
    }

    @Override
    public int getCount() {
        if (null == mAllJourneyLists || mAllJourneyLists.size() == 0) {
            return 0;
        }
        return mAllJourneyLists.size();
    }

    @Override
    public MyOrderListResponse.DataBean.AllJourneyBean getItem(int position) {
        return mAllJourneyLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;
        if (null != convertView) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_home_trip, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final MyOrderListResponse.DataBean.AllJourneyBean itemInfo = getItem(position);
        //标题
        if (!TextUtils.isEmpty(itemInfo.getInvite_title())) {
            holder.tvTitle.setText(itemInfo.getInvite_title());
        }
        //位置
        String invitePosition = itemInfo.getInvite_position();
        if (!TextUtils.isEmpty(invitePosition)) {
            holder.tvRestrantantName.setText(invitePosition);
        }
        //价格
        holder.tvAa.setText("￥" + NSMTypeUtils.getGreatPrice(itemInfo.getInvite_price()));
        //发布者的头像
        if (null != itemInfo.getUser_thumbnailslogo()) {
            HttpLoader.getImageLoader().get(itemInfo.getUser_thumbnailslogo(),
                    ImageLoader.getImageListener(holder.userIcon, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        final int publishType = itemInfo.getType();
        //状态
        String newStatusType = "";
        if (publishType == 0) {  //type=0我加入的
            newStatusType = itemInfo.getJoiner_newstatus();
        } else {    //type = 1,我发布的
            newStatusType = itemInfo.getInvite_newstatus();
        }

        final String flag = itemInfo.getFlag();
        if ("0".equals(flag)) {  //有正在进行中的活动,尝试显示该界面
            activity.tryShowMyTrip();
        }

        if ("0".equals(flag)) {
            holder.deleteBtn.setText("取消");
        } else {
            holder.deleteBtn.setText("删除");
        }

        //时间 和 状态的对比
        if ("0".equals(newStatusType) || "50".equals(newStatusType) || "100".equals(newStatusType) || "150".equals(newStatusType)) {
            holder.tvSignOne.setVisibility(View.VISIBLE);
            holder.tvOrderState.setVisibility(View.INVISIBLE);
            //时间
            long inviteTime = itemInfo.getInvite_time();
            if (inviteTime > System.currentTimeMillis()) {
                holder.tvSignOne.setText("距离活动还有");
            } else {
                holder.tvSignOne.setText("已超时");
            }
            holder.tvDistanceTime.setVisibility(View.VISIBLE);
            holder.tvDistanceTime.setText(TimeUtils.overTime(inviteTime));
        } else {
            holder.tvOrderState.setVisibility(View.VISIBLE);
            holder.tvSignOne.setVisibility(View.INVISIBLE);
            holder.tvDistanceTime.setVisibility(View.INVISIBLE);
            if ("180".equals(newStatusType)) {
                holder.tvOrderState.setText("已扫码");
            } else if ("200".equals(newStatusType)) {
                holder.tvOrderState.setText("已完成");
            } else if ("650".equals(newStatusType)) {
                holder.tvOrderState.setText("已拒绝");
            } else if ("500".equals(newStatusType)) {
                holder.tvOrderState.setText("审核中");
            } else {
                holder.tvOrderState.setText("已失效");
            }
        }

        if ("0".equals(newStatusType) || "50".equals(newStatusType) || "100".equals(newStatusType)) {
            //0,50,100显示加入人数
            holder.tvPeopleNumber.setVisibility(View.VISIBLE);
            holder.cvJoin.setVisibility(View.INVISIBLE);
            if (publishType == 1) {
                holder.tvPeopleNumber.setText(itemInfo.getJoinCount() + "人加入");
            } else {
                holder.tvPeopleNumber.setText(itemInfo.getJoinCount() + "人申请");
            }
        } else {
            holder.tvPeopleNumber.setVisibility(View.INVISIBLE);
        }

        //提示点
//        if ("100".equals(newStatusType) || "150".equals(newStatusType)) {
        if (!TextUtils.isEmpty(mOrderInviteId) &&
                mOrderInviteId.equals(String.valueOf(itemInfo.getInvite_id()))) {
            holder.mItemHomeTripInfoBg.setVisibility(View.VISIBLE);
        } else {
            holder.mItemHomeTripInfoBg.setVisibility(View.INVISIBLE);
        }
//        } else {
//            holder.mItemHomeTripInfoBg.setVisibility(View.INVISIBLE);
//        }

        if ("150".equals(newStatusType)) {
            holder.cvJoin.setVisibility(View.VISIBLE);
            holder.tvPeopleNumber.setVisibility(View.INVISIBLE);
            if (null == itemInfo.getJoinUser_thumbnailslogo()) {
                holder.cvJoin.setImageResource(R.drawable.picture_moren);
            } else {
                HttpLoader.getImageLoader().get(itemInfo.getJoinUser_thumbnailslogo(),
                        ImageLoader.getImageListener(holder.cvJoin, R.drawable.picture_moren, R.drawable.picture_moren));
            }
        } else {
            holder.cvJoin.setVisibility(View.INVISIBLE);
        }

        //删除
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.layout.quickClose();
                activity.resetOrderInviteId(String.valueOf(itemInfo.getInvite_id()));
                if ("0".equals(flag)) {
                    //取消我发起的邀请
                    if (itemInfo.getType() == 1) {
                        HashMap params = new HashMap();
                        params.put("token", NSMTypeUtils.getMyToken());
                        params.put("inviteId", itemInfo.getInvite_id() + "");
                        HttpLoader.post(ConstantsWhatNSM.URL_CANCEL_MYINVITE, params, CancelMyInvite.class,
                                Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYINVITE + "" + position)
                                , HomeMyTripAdapter.this).setTag(this);
                    } else {
                        HashMap params = new HashMap();
                        params.put("token", NSMTypeUtils.getMyToken());
                        params.put("joinerId", itemInfo.getJoiner_id() + "");
                        HttpLoader.post(ConstantsWhatNSM.URL_CANCEL_MYJOIN, params, CancelMyInvite.class,
                                Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYJOIN + "" + position)
                                , HomeMyTripAdapter.this).setTag(this);
                    }
                } else {
                    new AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage("确定删除?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //删除我发起的邀请
                                    if (itemInfo.getType() == 1) {
                                        HashMap params = new HashMap();
                                        params.put("token", NSMTypeUtils.getMyToken());
                                        params.put("inviteId", itemInfo.getInvite_id() + "");
                                        HttpLoader.post(ConstantsWhatNSM.URL_DELETE_MYINVITE, params, CancelMyInvite.class,
                                                Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE + "" + position)
                                                , HomeMyTripAdapter.this).setTag(this);
                                    } else {
                                        HashMap params = new HashMap();
                                        params.put("token", NSMTypeUtils.getMyToken());
                                        params.put("joinerId", itemInfo.getJoiner_id());
                                        HttpLoader.post(ConstantsWhatNSM.URL_DELETE_MYJOIN, params, CancelMyInvite.class,
                                                Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "" + position)
                                                , HomeMyTripAdapter.this).setTag(this);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null).show();
                }
            }
        });

        final String finalNewStatusType = newStatusType;
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.resetOrderInviteId(String.valueOf(itemInfo.getInvite_id()));
                if ("200".equals(finalNewStatusType) || "150".equals(finalNewStatusType) || "180".equals(finalNewStatusType)) {
                    Intent intent = new Intent(activity, MyTripActivity.class);
                    intent.putExtra("data", String.valueOf(itemInfo.getInvite_id()));
                    intent.putExtra("status", finalNewStatusType);
                    activity.startActivity(intent);
                } else {
                    if (publishType == 1) {
                        ActivityUtils.startActivityForData(activity,
                                InviteInviterDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(activity,
                                InviteJoinerDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    }
                }
            }
        });

        return view;
    }

    public void setOrderInviteId(String orderInviteId) {
        mOrderInviteId = orderInviteId;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private SwipeMenuLayout layout;
        private TextView tvTitle;
        private CircleImageView userIcon;
        private TextView tvRestrantantName;
        private TextView tvAa;
        private TextView tvPeopleNumber;
        private TextView tvSignOne;
        private TextView tvDistanceTime;
        private TextView deleteBtn;
        private RelativeLayout rlMain;
        private TextView tvOrderState;
        private CircleImageView cvJoin;
        private TextView mItemHomeTripInfoBg;

        public ViewHolder(View view) {
            layout = (SwipeMenuLayout) view.findViewById(R.id.item_trip_layout);
            rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            userIcon = (CircleImageView) view.findViewById(R.id.user_icon);
            tvRestrantantName = (TextView) view.findViewById(R.id.tv_restrantant_name);
            tvAa = (TextView) view.findViewById(R.id.tv_aa);
            deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
            tvPeopleNumber = (TextView) view.findViewById(R.id.tv_people_number);
            tvSignOne = (TextView) view.findViewById(R.id.tv_sign_one);
            tvOrderState = (TextView) view.findViewById(R.id.tv_order_state);
            tvDistanceTime = (TextView) view.findViewById(R.id.tv_distance_time);
            cvJoin = (CircleImageView) view.findViewById(R.id.cv_join);
            mItemHomeTripInfoBg = (TextView) view.findViewById(R.id.item_home_trip_info_bg);
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE + "")
                && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showInfo(mContext, "删除成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE
                        + "", ""));
                this.mAllJourneyLists.remove(position);
                this.notifyDataSetChanged();
            } else {
                MyToast.showWarning(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "") && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showInfo(mContext, "删除成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "", ""));
                this.mAllJourneyLists.remove(position);
                notifyDataSetChanged();
            } else {
                MyToast.showWarning(mContext, ((CancelMyInvite) response).getMessage());
            }
        }

        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYINVITE + "")
                && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showInfo(mContext, "取消成功");
                mHomeTrivelFragment.refreshDate();
            } else {
                MyToast.showWarning(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYJOIN + "") && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showConterToast(mContext, "取消成功");
                mHomeTrivelFragment.refreshDate();
            } else {
                MyToast.showWarning(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        //MyToast.showConterToast(mContext, error.getMessage());
    }
}
