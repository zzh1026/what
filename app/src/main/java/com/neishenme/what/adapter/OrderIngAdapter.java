package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.neishenme.what.bean.MyOrderResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.ScreenListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.SwLin;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 */
public class OrderIngAdapter extends BaseAdapter implements HttpLoader.ResponseListener {
    private Context mContext;
    private MyOrderResponse response;
    private Map<Integer, SwLin> mapView;
    private boolean mTouch = false;
    private String showMenuTag;
    private ViewHolder holder;
    private MyOrderActivity activity;

    public OrderIngAdapter(Context context, MyOrderResponse response) {
        mContext = context;
        this.response = response;
        this.activity = (MyOrderActivity) context;
        mapView = new HashMap<Integer, SwLin>();
    }

    @Override
    public int getCount() {
        if (null == response || response.getData().getOrdering().size() == 0) {
            return 0;
        }
        return response.getData().getOrdering().size();
    }

    @Override
    public MyOrderResponse.DataBean.OrderingBean getItem(int position) {
        return response.getData().getOrdering().get(position);
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
            view = View.inflate(mContext, R.layout.item_order_ing, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        final MyOrderResponse.DataBean.OrderingBean itemInfo = getItem(position);
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
        /**
         * 点击条目事件,因为在listview中的item点击事件呗拦截,所以在内部进行事件的处理
         */
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwLin s = mapView.get(position);
                if (s != null & s.getCurrentScreen() == 1) {
                    s.showScreen(0);//0 为主页面 1编辑删除按钮界面
                    return;
                }
                if (itemInfo.getType() == 1) {
                    if (itemInfo.getInvite_newstatus() == 150) {
                        activity.toTripAct(String.valueOf(itemInfo.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(activity,
                                InviteInviterDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    }
                } else {
                    if ("150".equals(itemInfo.getJoiner_newstatus())) {
                        activity.toTripAct(String.valueOf(itemInfo.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(activity,
                                InviteJoinerDetailActivity.class, String.valueOf(itemInfo.getInvite_id()));
                    }
                }

                showMainLayout();
            }
        });

        holder.deleteBtn.setTag(position);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMainLayout();
                //取消我发起的邀请
                if (itemInfo.getType() == 1) {
                    HashMap params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("inviteId", itemInfo.getInvite_id() + "");
                    HttpLoader.post(ConstantsWhatNSM.URL_CANCEL_MYINVITE, params, CancelMyInvite.class,
                            Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYINVITE + "" + position)
                            , OrderIngAdapter.this).setTag(this);
                } else {
                    HashMap params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("joinerId", itemInfo.getJoiner_id() + "");
                    HttpLoader.post(ConstantsWhatNSM.URL_CANCEL_MYJOIN, params, CancelMyInvite.class,
                            Integer.valueOf(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYJOIN + "" + position)
                            , OrderIngAdapter.this).setTag(this);
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
        if (itemInfo.getType() == 1) {
            holder.tvPeopleNumber.setText(itemInfo.getJoinCount() + "人加入");
        } else {
            holder.tvPeopleNumber.setText(itemInfo.getJoinCount() + "人申请");
        }

        long inviteTime = itemInfo.getInvite_time();
        if (inviteTime != 0) {
            if (inviteTime > System.currentTimeMillis()) {
                holder.tvSignOne.setText("距离活动还有");
            } else {
                holder.tvSignOne.setText("已超时");
            }
            holder.tvDistanceTime.setVisibility(View.VISIBLE);
            holder.tvDistanceTime.setText(TimeUtils.overTime(inviteTime));
        } else {
            holder.tvSignOne.setText("已超时");
            holder.tvDistanceTime.setVisibility(View.INVISIBLE);
        }

        if (response.getData().getOrdering().get(position).getType() == 1) {
            if (response.getData().getOrdering().get(position).getInvite_newstatus() == 150) {
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
                holder.tvPeopleNumber.setVisibility(View.VISIBLE);
            }
        } else {
            if (response.getData().getOrdering().get(position).getJoiner_newstatus().equals("150")) {
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
                holder.tvPeopleNumber.setVisibility(View.VISIBLE);
            }
        }
        String user_thumbnailslogo = itemInfo.getUser_thumbnailslogo();
        if (!TextUtils.isEmpty(user_thumbnailslogo)) {
            HttpLoader.getImageLoader().get(user_thumbnailslogo,
                    ImageLoader.getImageListener(holder.userIcon, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            holder.userIcon.setImageResource(R.drawable.picture_moren);
        }
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
        private CircleImageView cvJoin;


        public ViewHolder(View view) {
            layout = (SwLin) view.findViewById(R.id.layout);
            rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            userIcon = (CircleImageView) view.findViewById(R.id.user_icon);
            tvRestrantantName = (TextView) view.findViewById(R.id.tv_restrantant_name);
            tvAa = (TextView) view.findViewById(R.id.tv_aa);
            tvPeopleNumber = (TextView) view.findViewById(R.id.tv_people_number);
            tvSignOne = (TextView) view.findViewById(R.id.tv_sign_one);
            tvDistanceTime = (TextView) view.findViewById(R.id.tv_distance_time);
            deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
            cvJoin = (CircleImageView) view.findViewById(R.id.cv_join);

        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYINVITE + "")
                && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showConterToast(mContext, "取消成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYINVITE
                        + "", ""));
                this.response.getData().getOrdering().remove(position);
                this.notifyDataSetChanged();
            } else {
                MyToast.showConterToast(mContext, ((CancelMyInvite) response).getMessage());
            }
        }
        if ((requestCode + "").contains(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYJOIN + "") && response instanceof CancelMyInvite) {
            if (((CancelMyInvite) response).getCode() == 1) {
                MyToast.showConterToast(mContext, "取消成功");
                int position = Integer.parseInt((requestCode + "").replace(ConstantsWhatNSM.REQUEST_CODE_CANCEL_MYJOIN + "", ""));
                this.response.getData().getOrdering().remove(position);
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
