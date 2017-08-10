package com.neishenme.what.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.MyOrderActivity;
import com.neishenme.what.bean.CancelMyInvite;
import com.neishenme.what.bean.HistoryOrderResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.ScreenListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.SwLin;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ItemHistoryOrderAdapter extends BaseAdapter implements HttpLoader.ResponseListener {
    private Context mContext;
    private HistoryOrderResponse response;
    private Map<Integer, SwLin> mapView;
    private boolean mTouch = false;
    private String showMenuTag;
    private ViewHolder holder;
    private MyOrderActivity activity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int position = msg.getData().getInt("position");
                    response.getData().getHistroyorder().remove(position);
                    ItemHistoryOrderAdapter.this.notifyDataSetChanged();
                    break;
            }
        }
    };

    public ItemHistoryOrderAdapter(Context context, HistoryOrderResponse response) {
        mContext = context;
        this.response = response;
        this.activity = (MyOrderActivity) context;
        mapView = new HashMap<Integer, SwLin>();
    }

    @Override
    public int getCount() {
        if (null == response.getData().getHistroyorder() || response.getData().getHistroyorder().size() == 0) {
            return 0;
        }
        return response.getData().getHistroyorder().size();
    }

    @Override
    public Object getItem(int position) {
        return response.getData().getHistroyorder().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (null != convertView) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_history_order, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        final HistoryOrderResponse.DataBean.HistroyorderBean itemInfo = response.getData().getHistroyorder().get(position);
        holder.layout.setTag(position);
        mapView.put(position, holder.layout);
        holder.layout.setScreenListener(new ScreenListener() {

            @Override
            public boolean startTouch(String tag) {

                if (mTouch) {
                    if (showMenuTag.equals(tag)) {
                        mTouch = false;
                    } else {
                        int p = Integer.parseInt(showMenuTag);
                        showMainLayout();
                    }

                }
                return mTouch;
            }

            @Override
            public void changeScreen(int screen, String tag) {

                if (screen == 1) {
                    mTouch = true;
                    showMenuTag = tag;
                }
            }

            @Override
            public void canTouch(boolean flag) {
                //System.out.println("canTouch:" + flag);
                mTouch = false;
            }

        });

        holder.deleteBtn.setTag(position);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMainLayout();
                //删除我发起的邀请
                if (itemInfo.getType() == 1) {
                    HashMap params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("inviteId", itemInfo.getInvite_id() + "");
                    HttpLoader.post(ConstantsWhatNSM.URL_DELETE_MYINVITE, params, CancelMyInvite.class,
                            Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE + "" + position)
                            , ItemHistoryOrderAdapter.this).setTag(this);
                    Log.d("one", Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE + "" + position) + "");
                } else {
                    HashMap params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("joinerId", itemInfo.getJoiner_id());
                    HttpLoader.post(ConstantsWhatNSM.URL_DELETE_MYJOIN, params, CancelMyInvite.class,
                            Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "" + position)
                            , ItemHistoryOrderAdapter.this).setTag(this);
                    Log.d("one", Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "" + position) + "");
                }

            }
        });

        if (!TextUtils.isEmpty(itemInfo.getInvite_title())) {
            holder.tvTitle.setText(itemInfo.getInvite_title());
        }
        int publishType = itemInfo.getPublishType();
        if (publishType == 1) { //极速
            String invitePosition = itemInfo.getInvite_position();
            if (!TextUtils.isEmpty(invitePosition)) {
                holder.tvRestrantantName.setText(invitePosition);
            }
            holder.tvAa.setText("极速发布 / ￥" + itemInfo.getInvite_price());
        } else {    //专属
            String storesName = itemInfo.getStores_name();
            if (!TextUtils.isEmpty(storesName)) {
                holder.tvRestrantantName.setText(storesName);
            }
            holder.tvAa.setText("专属发布 / ￥" + itemInfo.getInvite_price());
        }
        //(1我发起的/2我参与的)
//        holder.tvDistanceTime.setText(TimeUtils.overTime(itemInfo.getInvite_time()));
//        if (response.getData().getHistroyorder().get(position).getType() == 1) {
//            if (response.getData().getHistroyorder().get(position).getInvite_newstatus() == 150) {
//                if (null == itemInfo.getUser_thumbnailslogo()) {
//                    holder.cvJoin.setImageResource(R.drawable.picture_moren);
//                } else {
//                    HttpLoader.getImageLoader().get(itemInfo.getUser_thumbnailslogo(),
//                            ImageLoader.getImageListener(holder.cvJoin, R.drawable.picture_moren, R.drawable.picture_moren));
//                }
//            }
//
//        } else {
//            if (response.getData().getHistroyorder().get(position).getJoiner_newstatus().equals("150")) {
//                if (null == itemInfo.getUser_thumbnailslogo()) {
//                    holder.cvJoin.setImageResource(R.drawable.picture_moren);
//                } else {
//                    HttpLoader.getImageLoader().get(itemInfo.getUser_thumbnailslogo(),
//                            ImageLoader.getImageListener(holder.cvJoin, R.drawable.picture_moren, R.drawable.picture_moren));
//                }
//            }
//        }
        if (null != itemInfo.getUser_thumbnailslogo()) {
            HttpLoader.getImageLoader().get(itemInfo.getUser_thumbnailslogo(),
                    ImageLoader.getImageListener(holder.userIcon, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        //
        String newStatusType = "";
        if (itemInfo.getType() == 0) {  //type=0我加入的
            newStatusType = itemInfo.getJoiner_newstatus();
        } else {    //type = 1,我发布的
            newStatusType = itemInfo.getInvite_newstatus();
        }
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

        /**
         * 点击条目事件,因为在listview中的item点击事件呗拦截,所以在内部进行事件的处理
         */
        final String finalNewStatusType = newStatusType;
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwLin s = mapView.get(position);
                if (s != null & s.getCurrentScreen() == 1) {
                    s.showScreen(0);//0 为主页面 1编辑删除按钮界面
                    return;
                }
                if (itemInfo.getType() == 1) {
                    if ("200".equals(finalNewStatusType)) {
                        activity.toTripAct(String.valueOf(itemInfo.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(activity,
                                InviteInviterDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    }
                } else {
                    if ("200".equals(finalNewStatusType)) {
                        activity.toTripAct(String.valueOf(itemInfo.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(activity,
                                InviteJoinerDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    }
                }

                showMainLayout();
            }
        });

//        view.setTag(itemInfo.getInvite_id());
        return view;
    }

    public void showMainLayout() {

        for (int key : mapView.keySet()) {
            mapView.get(key).showScreen(0);
        }
    }

    static class ViewHolder {
        private SwLin layout;
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


        public ViewHolder(View view) {
            layout = (SwLin) view.findViewById(R.id.layout);
            rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            userIcon = (CircleImageView) view.findViewById(R.id.user_icon);
            tvRestrantantName = (TextView) view.findViewById(R.id.tv_restrantant_name);
            tvAa = (TextView) view.findViewById(R.id.tv_aa);
            deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
            tvOrderState = (TextView) view.findViewById(R.id.tv_order_state);
            tvDistanceTime = (TextView) view.findViewById(R.id.tv_distance_time);
            cvJoin = (CircleImageView) view.findViewById(R.id.cv_join);
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE + "")
                && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showConterToast(mContext, "删除成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYINVITE
                        + "", ""));
                this.response.getData().getHistroyorder().remove(position);
                this.notifyDataSetChanged();
            } else {
                MyToast.showConterToast(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "") && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showConterToast(mContext, "删除成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_DELETE_MYJOIN + "", ""));
                this.response.getData().getHistroyorder().remove(position);
                this.notifyDataSetChanged();
            } else {
                MyToast.showConterToast(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        //MyToast.showConterToast(mContext, error.getMessage());
    }
}
