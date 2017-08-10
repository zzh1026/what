package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.bean.FocusPeopleListResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.ScreenListener;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.SwLin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/17.
 */
public class FocusPeopleAdapter extends BaseAdapter {
    private Context mContext;
    public Map<Integer, SwLin> mapView;
    private boolean mTouch = false;
    private String showMenuTag;
    private List<FocusPeopleListResponse.DataEntity.FoucsEntity> focusData;
    PullToRefreshListView lvFocus;
    RemoveDataListener removeDataListener;

    public interface RemoveDataListener {
        void removeData(int position);
    }

    public void setRemoveDataListener(RemoveDataListener removeDataListener) {
        this.removeDataListener = removeDataListener;
    }

    public FocusPeopleAdapter(Context context) {
        mContext = context;
        mapView = new HashMap<Integer, SwLin>();
    }

    public void bindData(List<FocusPeopleListResponse.DataEntity.FoucsEntity> focusData) {
        this.focusData = focusData;
    }

    @Override
    public int getCount() {

        return focusData == null ? 0 : focusData.size();
    }

    @Override
    public Object getItem(int position) {
        return focusData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FocusPeopleListResponse.DataEntity.FoucsEntity foucsEntity = focusData.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_focus_people, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (foucsEntity.getUser_thumbnailslogo() != null) {
            HttpLoader.getImageLoader().get(foucsEntity.getUser_thumbnailslogo(), ImageLoader.getImageListener(
                    holder.ivAvatar, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        if (!TextUtils.isEmpty(foucsEntity.getUser_name())) {
            holder.tvName.setText(foucsEntity.getUser_name());
        }
        if (!TextUtils.isEmpty(foucsEntity.getUser_sign())) {
            holder.tvSign.setText(foucsEntity.getUser_sign());
        }

        holder.mIvGender.setImageResource(foucsEntity.getUser_gender() == 1 ? R.drawable.man_icon : R.drawable.woman_icon);

        holder.tvAge.setText(String.valueOf(TimeUtils.getAge(foucsEntity.getUser_birthday())));


        holder.swLin.setTag(position);
        mapView.put(position, holder.swLin);
        holder.swLin.setScreenListener(new ScreenListener() {
            @Override
            public boolean startTouch(String tag) {
                if (mTouch) {
                    if (showMenuTag.equals(tag)) {
                        mTouch = false;
                    } else {
                        //int p=Integer.parseInt(showMenuTag);
                        showMainLayout();
                    }
                }
                return mTouch;
            }

            @Override
            public void canTouch(boolean flag) {

                mTouch = false;
            }

            @Override
            public void changeScreen(int screen, String tag) {
                if (screen == 1) {
                    mTouch = true;
                    showMenuTag = tag;
                }

            }
        });

        holder.rlMain.setTag(position);
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwLin s = mapView.get(position);
                if (s != null && s.getCurrentScreen() == 1) {
                    s.showScreen(0);//0 为主页面 1编辑删除按钮界面
                    return;
                }
                //跳转到查看用户详情界面
                UserDetailActivity.startUserDetailAct(mContext, focusData.get(position).getUser_id(), false);
                showMainLayout();
            }
        });

        holder.tvDelete.setTag(position);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeDataListener.removeData(position);
            }
        });


        return convertView;
    }


    public void showMainLayout() {
        for (int key : mapView.keySet()) {
            mapView.get(key).showScreen(0);
        }
    }

    public static class ViewHolder {
        SwLin swLin;
        private RelativeLayout rlMain;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvAge;
        private TextView tvSign;
        private TextView tvDelete;
        private ImageView mIvGender;

        public ViewHolder() {
        }

        public ViewHolder(View view) {
            swLin = (SwLin) view.findViewById(R.id.layout);
            rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAge = (TextView) view.findViewById(R.id.tv_age);
            tvSign = (TextView) view.findViewById(R.id.tv_sign);
            tvDelete = (TextView) view.findViewById(R.id.delete_btn);
            mIvGender = (ImageView) view.findViewById(R.id.iv_gender);
        }

        public SwLin getSwLin() {
            return swLin;
        }
    }
}
