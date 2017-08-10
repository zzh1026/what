package com.neishenme.what.fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.MyCouponsActivity;
import com.neishenme.what.activity.PayOrderActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.adapter.HomePublishServiceAdapter;
import com.neishenme.what.adapter.ReleaseTitleAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.FillOrderTitleResponse;
import com.neishenme.what.bean.GetServiceListResponse;
import com.neishenme.what.bean.MyCouPonsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendDataPublicResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.UserIsVipResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.dialog.ReleaseMoneyNormalDialog;
import com.neishenme.what.dialog.ReleaseTimePickerDialog;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnAudioPermissionListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.utils.UIUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 专属发布界面
 */
public class ReleaseNormalFragment extends Fragment implements HttpLoader.ResponseListener, View.OnClickListener {
    private MainActivity homeActivity;

    private int MONEY_START = 500;
    private int MONEY_END = 5000;
    private int MONEY_BETWEEN = 500;

    private TextView mReleaseNormalPublish;
    private EditText mReleaseNormalTitle;
    private ImageView mReleaseNormalMore;
    private View mReleaseNormalFlagLine;
    private TextView mReleaseNormalTime;
    private RadioGroup mReleaseNormalTargetType;
    private LinearLayout mReleaseNormalRecordSound;
    private ImageView mRadioIcon;
    private TextView mTvClickRadio;
    private LinearLayout mReleaseNormalDeleteSound;
    private TextView mReleaseNormalRecordTime;
    private TextView mReleaseNormalPrice;
    private TextView mReleaseNormalCoupons;
    private RelativeLayout mReleaseNormalCouponsSelect;
    private CheckBox mCbReleaseNormal;
    private TextView mReleaseNormalVipPay;
    private ImageView mReleaseNormalVipQuestion;
    private RelativeLayout mReleaseNormalVipPaySelect;
    private CheckBox mCbReleaseNormalVipPay;
    private HorizontalScrollView mReleaseNormalHorisv;
    private LayoutInflater mInflater;
    private LinearLayout mReleaseNormalLl;
    private GridView mReleaseNormalGrid;

    private LinearLayout mReleaseNormalNoCity;
    private LinearLayout mRelaseNoServiceList;

    //显示获取主题的list相关
    private PopupWindow pw;
    private List<String> titleList;
    private ReleaseTitleAdapter mAdapter;
    private boolean should = true; //是否应该将箭头返回去

    private List<GetServiceListResponse.DataBean.ServiceBean> serviceLists; //服务列表
    private HomePublishServiceAdapter mHomePublishServiceAdapter;  //服务列表适配器
    private GetServiceListResponse.DataBean.ServiceBean serviceBean; //选中的服务

    private MediaPlayer mPlayer; //音乐播放器
    private MediaRecorder myRecorder = null; //声音录制器
    private File rocordSoundFile;

    //录音倒计时相关
    private TimerTask task; //计时器
    private Timer timer;
    private int second = 5;  //倒计时秒数

    private String selectedTime = ""; //选择的时间
    private long mSelectedTime = 0;

    //播放状态相关
    //正在播放 , 录制完成 , 正在录制 标记三种状态
    private boolean isPlaying = false, hasRecorded = false, isRecording = false;

    //服务列表相关
    private int page = 1;
    private boolean isHaveMore = false;
    private boolean isUserVip = false;  //标记用户是否为会员用户
    private boolean isHaveCoupons = false;  //标记用户是否有优惠券

    private ArrayList<String> mMoneyDate;   //该页需要显示的钱
    private MyCouPonsResponse.DataBean.CouponsBean useCoupons = null;    //使用的优惠券

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == SHOULD_HIDE) {
//                mReleaseNormalNoCity.setVisibility(View.GONE);
//            }
        }
    };

    private int mInviteId = 0;

    public ReleaseNormalFragment() {
    }

    public static ReleaseNormalFragment newInstance() {
        ReleaseNormalFragment fragment = new ReleaseNormalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_release_normal, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mReleaseNormalPublish = (TextView) view.findViewById(R.id.release_normal_publish);

        mReleaseNormalTitle = (EditText) view.findViewById(R.id.release_normal_title);
        mReleaseNormalMore = (ImageView) view.findViewById(R.id.release_normal_more);
        mReleaseNormalFlagLine = view.findViewById(R.id.release_normal_flag_line);

        mReleaseNormalTime = (TextView) view.findViewById(R.id.release_normal_time);
        mReleaseNormalTargetType = (RadioGroup) view.findViewById(R.id.release_normal_target_type);

        mReleaseNormalRecordSound = (LinearLayout) view.findViewById(R.id.release_normal_record_sound);
        mReleaseNormalDeleteSound = (LinearLayout) view.findViewById(R.id.release_normal_delete_sound);
        mRadioIcon = (ImageView) view.findViewById(R.id.radio_icon);
        mTvClickRadio = (TextView) view.findViewById(R.id.tv_click_radio);
        mReleaseNormalRecordTime = (TextView) view.findViewById(R.id.release_normal_record_time);

        mReleaseNormalPrice = (TextView) view.findViewById(R.id.release_normal_price);

        mReleaseNormalCoupons = (TextView) view.findViewById(R.id.release_normal_coupons);
        mReleaseNormalCouponsSelect = (RelativeLayout) view.findViewById(R.id.release_normal_coupons_select);
        mCbReleaseNormal = (CheckBox) view.findViewById(R.id.cb_release_normal);

        mReleaseNormalVipPay = (TextView) view.findViewById(R.id.release_normal_vip_pay);
        mReleaseNormalVipPaySelect = (RelativeLayout) view.findViewById(R.id.release_normal_vip_pay_select);
        mCbReleaseNormalVipPay = (CheckBox) view.findViewById(R.id.cb_release_normal_vip_pay);
        mReleaseNormalVipQuestion = (ImageView) view.findViewById(R.id.release_normal_vip_question);

        mReleaseNormalHorisv = (HorizontalScrollView) view.findViewById(R.id.release_normal_horisv);
        mReleaseNormalLl = (LinearLayout) view.findViewById(R.id.release_normal_ll);
        mReleaseNormalGrid = (GridView) view.findViewById(R.id.release_normal_grid);

        mReleaseNormalNoCity = (LinearLayout) view.findViewById(R.id.release_normal_no_city);
        mRelaseNoServiceList = (LinearLayout) view.findViewById(R.id.relase_no_service_list);
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();
        mInflater = LayoutInflater.from(homeActivity);

        mMoneyDate = new ArrayList<>();
        for (int i = MONEY_START; i <= MONEY_END; i += MONEY_BETWEEN) {
            mMoneyDate.add(i + "");
        }

        rocordSoundFile = new File(FileUtil.getSDAudioDir(), "mypublish.aac");
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {
            rocordSoundFile.delete();
        }

        EventBus.getDefault().register(this);

        getUserIsVip();
    }

    public void onEventMainThread(MyCouPonsResponse.DataBean.CouponsBean event) {
        useCoupons = event;
        updateCoupons();
    }

    private void initListener() {
        mReleaseNormalPublish.setOnClickListener(this);
        mReleaseNormalMore.setOnClickListener(this);
        mReleaseNormalTime.setOnClickListener(this);
        mReleaseNormalCoupons.setOnClickListener(this);
        mReleaseNormalCouponsSelect.setOnClickListener(this);
        mReleaseNormalVipPaySelect.setOnClickListener(this);
        mReleaseNormalRecordSound.setOnClickListener(this);
        mReleaseNormalDeleteSound.setOnClickListener(this);
        mReleaseNormalPrice.setOnClickListener(this);
        mReleaseNormalVipQuestion.setOnClickListener(this);
        mReleaseNormalGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetServiceListResponse.DataBean.ServiceBean serviceBean = serviceLists.get(position);
                int servicesId = serviceBean.getServices_id();
                PublishOrderActivity.startRestDetailAct(getContext(), servicesId);
            }
        });
        mReleaseNormalHorisv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 判断离右边的距离为0时
                        if (mReleaseNormalGrid.getRight() - mReleaseNormalHorisv.getScrollX() -
                                mReleaseNormalHorisv.getWidth() == 0) {
                            if (isHaveMore) {
                                page++;
                                getServiceList(page);
                            } else {
                                showToast("暂无更多");
                            }
                        }
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mReleaseNormalTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkSentive(mReleaseNormalTitle.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        clearTitleFocus();
        switch (v.getId()) {
            case R.id.release_normal_publish:   //发布
                tryPublic();
                break;
            case R.id.release_normal_more:      //显示更多title
                if (pw != null && pw.isShowing()) {
                } else {
                    mReleaseNormalMore.setImageResource(R.drawable.icon_more2xdown);
                    showTitleList();
                }
                break;
            case R.id.release_normal_time:   //选择时间
                editTime();
                break;
            case R.id.release_normal_coupons:   //进入优惠券界面
//                if (isHaveCoupons && useCoupons != null && !TextUtils.isEmpty(useCoupons.getNumber())) {
                int serviceId = getServiceId();
                if (serviceId == 0) {
                    showToast("请选择餐厅再选择优惠券");
                    return;
                }
                String orderTime = getOrderTime();
                if (TextUtils.isEmpty(orderTime)) {
                    showToast("请选择时间再选择优惠券");
                    return;
                }
//                    if (services_price != 0 && !TextUtils.isEmpty(orderTime)) {
                Intent i = new Intent(homeActivity, MyCouponsActivity.class);
                i.putExtra(MyCouponsActivity.SELECTED_TYPE, MyCouponsActivity.SELECTED_COUPONS);
                i.putExtra(MyCouponsActivity.SELECTED_COUPONS_TIME, orderTime);
                i.putExtra(MyCouponsActivity.SELECTED_COUPONS_ID, serviceId + "");
                homeActivity.startActivity(i);
//                    } else {
//                        ActivityUtils.startActivity(homeActivity, MyCouponsActivity.class);
//                    }
//                } else {
//                    ActivityUtils.startActivity(homeActivity, MyCouponsActivity.class);
//
//                }
                break;
            case R.id.release_normal_coupons_select:   //是否使用优惠券
                if (isHaveCoupons)
                    mCbReleaseNormal.setChecked(!mCbReleaseNormal.isChecked());
                break;
            case R.id.release_normal_vip_pay_select:   //是否使用会员预付
                if (isUserVip)
                    mCbReleaseNormalVipPay.setChecked(!mCbReleaseNormalVipPay.isChecked());
                else
                    getUserIsVip();
                break;
            case R.id.release_normal_vip_question:   //会员预付的问题
                showVipQuestionPopwindow();
                break;
            case R.id.release_normal_record_sound:   //录制音频
                tryRecordSound();
                break;
            case R.id.release_normal_delete_sound:   //删除音频
                tryDeleteSound();
                break;
            case R.id.release_normal_price:   //活动预算
                ReleaseMoneyNormalDialog moneyNormalDialog = new ReleaseMoneyNormalDialog(homeActivity, mMoneyDate, this);
                moneyNormalDialog.show();
                break;
            default:
                break;
        }
    }

    private void showVipQuestionPopwindow() {
        View contentView = LayoutInflater.from(homeActivity).inflate(R.layout.popwindow_vip_question, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = contentView.getMeasuredWidth() / 5 * 3;
        int height = (int) getResources().getDimension(R.dimen.height_4);
        popupWindow.showAsDropDown(mReleaseNormalVipQuestion, -width, -height);
    }

    private void clearTitleFocus() {
        mReleaseNormalTitle.setFocusable(false);
        mReleaseNormalTitle.setFocusableInTouchMode(true);
    }

    private void checkSentive(String title) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", title);
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_SENSITIVE_CHECK, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK, this).setTag(this);
    }

    private void tryRecordSound() {
        if (isPlaying) { //如果正在播放,停止播放
            stopEndPlay();
            return;
        }

        if (hasRecorded) {  //如果已经录制完成就播放
            startPlay();
            isPlaying = true;
            mTvClickRadio.setText("点击停止");
            mRadioIcon.setImageResource(R.drawable.stop_audio_icon);
            return;
        }

        if (isRecording) {  //正在录制,,点击要停止录制
            relaceTimer();
            hasRecorded = true;
            mTvClickRadio.setText("点击播放");
            mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
            mReleaseNormalDeleteSound.setVisibility(View.VISIBLE);
            isRecording = false;
            endRecord();
            mReleaseNormalRecordTime.setVisibility(View.INVISIBLE);
            return;
        }

        //检查权限
        homeActivity.requestAudioPermission(new OnAudioPermissionListener() {
            @Override
            public void onAudioPermissionSuccess() {
                homeActivity.showToastInfo("开始录音...");
                second = 5;
                mTvClickRadio.setText("点击停止");
                startRecord();
            }

            @Override
            public void onAudioPermissionFiler() {
                showToast("只有授予录音权限才能进行录音发单!");
            }
        });
    }

    private void tryDeleteSound() {
        if (isPlaying == true) {
            endPlay();
        }
        if (rocordSoundFile.exists()) {
            rocordSoundFile.delete();
        }
        mReleaseNormalDeleteSound.setVisibility(View.GONE);
        mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
        mTvClickRadio.setText("点击录制");
        hasRecorded = false;
    }

    private void getServiceList(int page) {
        String areaId;
        try {
            areaId = String.valueOf(homeActivity.showCityAreaId);
            if ("0".equals(areaId)) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        String price = getPrice();
        if (TextUtils.isEmpty(price)) {
            return;
        }
        String orderTime = getOrderTime();
        if (TextUtils.isEmpty(orderTime)) {
            return;
        }

        removeAdapter();

        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pageSize", "10");
        params.put("areaId", areaId);
        params.put("time", orderTime);
        params.put("price", price);
        HttpLoader.post(ConstantsWhatNSM.URL_SERVICE_LIST, params, GetServiceListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST, this).setTag(this);
    }

    public void getService() {
        if (mHomePublishServiceAdapter == null) {
            String areaId;
            try {
                areaId = String.valueOf(homeActivity.showCityAreaId);
                if ("0".equals(areaId)) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("areaId", areaId);
            params.put("page", "1");
            params.put("pageSize", "5");
            HttpLoader.post(ConstantsWhatNSM.URL_SERVICE_LIST, params, GetServiceListResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST, this).setTag(this);
        }
    }

    public void notifyData() {
        mHomePublishServiceAdapter = null;
        getService();
    }

    private void tryPublic() {
        stopEndPlay();
        if (35 != CityLocationConfig.cityLocationId) {
            showToast("您的城市暂未开通专属服务,请选择别的发布方式");
            return;
        }

        if (!NSMTypeUtils.isLogin()) {
            homeActivity.showToastInfo(homeActivity.getString(R.string.user_should_login_suggest));
            ActivityUtils.startActivity(homeActivity, LoginActivity.class);
            return;
        }

        String title = mReleaseNormalTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast("输入活动主题");
            return;
        }

        if (title.length() > 15) {
            showToast("主题过长,请修改一下吧");
            mReleaseNormalTitle.setText("");
            return;
        }

        String publishTime = getOrderTime();
        if (TextUtils.isEmpty(publishTime)) {
            showToast("邀请时间至少要在一个小时之后,请重新设置时间");
            mReleaseNormalTime.setHint("点击设置时间");
            return;
        }

        if (isRecording) {
            showToast("请先录音完毕");
            return;
        }

        if (TextUtils.isEmpty(mReleaseNormalPrice.getText().toString().trim())) {
            showToast("选择预算");
            return;
        }

        if (mHomePublishServiceAdapter == null) {
            showToast("请先选择就餐餐厅");
            return;
        }

        int servicesId;
        int servicePosition = mHomePublishServiceAdapter.getmCurrentSelected();
        if (servicePosition == -1) {
            showToast("选择餐厅");
            return;
        } else {
            serviceBean = serviceLists.get(servicePosition);
            servicesId = serviceBean.getServices_id();
        }

        if (CityLocationConfig.citySelectedId != CityLocationConfig.cityLocationId) {
            showToast("异地不能发单,请更改位置");
            return;
        }

        releaseInvite(title, publishTime, servicesId);
    }

    //发布邀请的逻辑
    private void releaseInvite(String title, String publishTime, int servicesId) {
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {

            mReleaseNormalPublish.setClickable(false);
            mReleaseNormalPublish.setText("正在发布");

            final HashMap<String, String> params = new HashMap<>();
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("serviceId", String.valueOf(servicesId));
            params.put("areaId", String.valueOf(CityLocationConfig.cityLocationId));
            params.put("publishType", "0");
            params.put("title", title);
            params.put("time", publishTime);
            params.put("target", getTarget());
            params.put("audioDuration", String.valueOf(5 - second));
            params.put("couponId", mCbReleaseNormal.isChecked() ? useCoupons.getId() + "" : "");
            params.put("prepayflag", mCbReleaseNormalVipPay.isChecked() ? "1" : "0");
            HashMap<String, String> params2 = null;
            if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.length() > 0) {
                params2 = new HashMap<>();
                params2.put("audio", rocordSoundFile.getAbsolutePath());
            }
            final HashMap<String, String> finalParams = params2;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpRequest(ConstantsWhatNSM.URL_SEND_DATA_SEND, params, finalParams);
                }
            }).start();
        } else {
            showToast("录入语音");
            return;
        }
    }

    private void editTime() {
        if (DateUtils.formatHour(System.currentTimeMillis()) > 23) {
            showToast("今天已经不能发单,请明天再来吧");
            return;
        }
//        MyDatePickerDialog myDAtePickerDialog = new MyDatePickerDialog(homeActivity, timeLimit, "时间");
//        myDAtePickerDialog.setOnConfirmListeners(new MyDatePickerDialog.OnConfirmListeners() {
//            @Override
//            public void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex) {
//                Log.d("timeSelect", monthSelectedIndex + "  " + daySelectedIndex);
//                String hour = timeLimit.split(",")[monthSelectedIndex];
//                String minute = daySelectedIndex + "";
//                if (minute.length() == 1) {
//                    minute = "0" + minute;
//                }
//                mReleaseNormalTime.setText(hour + ":" + minute);
//                selectedTime = hour + ":" + minute;
//                page = 1;
//                if (canShowOrder()) {
//                    showToast("发布时间与活动时间间隔过短");
//                }
//                getCoupons();
//                getServiceList(1);
//            }
//        });
//        myDAtePickerDialog.show();

        ReleaseTimePickerDialog mReleaseTimePickerDialog = new ReleaseTimePickerDialog(
                homeActivity, System.currentTimeMillis() + 61 * 60 * 1000,
                ReleaseTimePickerDialog.RELEASE_NORMAL, new ReleaseTimePickerDialog.OnConfirmListeners() {
            @Override
            public void onConfirmClicked(long selectedTime) {
                if ((selectedTime - System.currentTimeMillis()) >= (1000 * 60)) {
                    mSelectedTime = selectedTime;
                    if (TimeUtils.isToday(selectedTime)) {
                        mReleaseNormalTime.setText("今天 " + DateUtils.formatTimeSimple(selectedTime));
                    } else {
                        mReleaseNormalTime.setText("明天 " + DateUtils.formatTimeSimple(selectedTime));
                    }
                    page = 1;
                    getCoupons();
                    getServiceList(1);
                } else {
                    showToast("发布时间与活动时间间隔过短");
                }
            }
        });
        mReleaseTimePickerDialog.show();
    }

    private void showTitleList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("payType", "1");
        params.put("gender", App.USERSP.getString("gender", "0"));
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_DATA_RES_TITLE, params, FillOrderTitleResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_RES_TITLE, this).setTag(this);
    }

    public void startPlay() {
        homeActivity.showToastInfo("开始播放");

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
                mTvClickRadio.setText("点击播放");
            }
        });
        try {
            mPlayer.setDataSource(rocordSoundFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            homeActivity.showToastError("播放失败！");
        }
    }

    public void endPlay() {
        if (null != mPlayer) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
            isPlaying = false;
        }
    }

    private void startRecord() {
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(rocordSoundFile.getAbsolutePath());
        mReleaseNormalRecordTime.setVisibility(View.VISIBLE);
        try {
            myRecorder.prepare();
        } catch (IOException e) {
            homeActivity.showToastError("录音失败！");
            myRecorder.release();
            myRecorder = null;
        }
        myRecorder.start();
        timer = new Timer();
        task = new MyTimerTask();
        timer.schedule(task, 0, 1000);
        isRecording = true;
    }

    public void endRecord() {
        try {
            if (null != myRecorder) {
                myRecorder.stop();
                myRecorder.release();
                myRecorder = null;
                isRecording = false;
            }
        } catch (Exception e) {
        }
    }

    private String getOrderTime() {
//        if (TextUtils.isEmpty(selectedTime)) {
//            return null;
//        }
//        String orderT = DateUtils.formatDate(System.currentTimeMillis()) + " " + selectedTime;
//        long order = DateUtils.formatToLong(orderT, "yyyy-MM-dd HH:mm");
//        if ((order - System.currentTimeMillis()) >= (0)) {
//            return String.valueOf(order);
//        } else {
//            return null;
//        }

        if (mSelectedTime >= System.currentTimeMillis()) {
            return String.valueOf(mSelectedTime);
        } else {
            return null;
        }
    }

    private boolean canShowOrder() {
        if (TextUtils.isEmpty(selectedTime)) {
            return false;
        }
        String orderT = DateUtils.formatDate(System.currentTimeMillis()) + " " + selectedTime;
        long order = DateUtils.formatToLong(orderT, "yyyy-MM-dd HH:mm");
        if ((order - System.currentTimeMillis()) <= (10 * 60 * 1000)) {
            return true;
        } else {
            return false;
        }
    }

    private String getPrice() {
        String price = mReleaseNormalPrice.getText().toString().trim();
        try {
            Integer.parseInt(price);
        } catch (Exception e) {
            return "";
        }
        return price;
    }

    //获取优惠券
    public void getCoupons() {
        useCoupons = null;
        isHaveCoupons = false;

        int serviceId = getServiceId();
        if (serviceId == 0) {
            return;
        }

        String orderTime = getOrderTime();
        if (TextUtils.isEmpty(orderTime)) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("serviceId", serviceId + "");
        params.put("time", orderTime);
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", "1");
        params.put("pageSize", "3");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_COUPONS, params, MyCouPonsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_COUPONS, this).setTag(this);
    }

    private int getServiceId() {
        if (mHomePublishServiceAdapter == null) {
            return 0;
        }
        int currentSelected = mHomePublishServiceAdapter.getmCurrentSelected();
        if (currentSelected == -1) {
            return 0;
        }
        if (serviceLists == null || serviceLists.get(currentSelected) == null) {
            return 0;
        }
        return serviceLists.get(currentSelected).getServices_id();
    }

    //获取用户是否为会员信息
    public void getUserIsVip() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_USER_IS_VIP, params, UserIsVipResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_IS_VIP, this).setTag(this);
    }

    private String getTarget() {
        String target = "0";
        switch (mReleaseNormalTargetType.getCheckedRadioButtonId()) {
            case R.id.release_normal_male:
                target = "1";
                break;
            case R.id.release_normal_female:
                target = "2";
                break;
            case R.id.release_normal_other:
                target = "0";
                break;
        }
        return target;
    }

    //当预算选择成功调用 , 该界面没有自定义,所以只有一个入口
    public void onMoneyEdit(String editMoney) {
        mReleaseNormalPrice.setText(editMoney);
        page = 1;
        mHomePublishServiceAdapter = null;
        getServiceList(1);
    }

    //当支付成功调用
    public void onPaySuccess() {
        if (mInviteId != 0) {
            ActivityUtils.startActivityForData(homeActivity, InviteInviterDetailActivity.class, String.valueOf(mInviteId));
        }
        resetInfo();

        removeAdapter();

        getService();
    }

    private void updateCoupons() {
        isHaveCoupons = true;
        mCbReleaseNormal.setChecked(isHaveCoupons);
        int type = useCoupons.getType();
        if (type == 0) {    //优惠折扣
//            mReleaseNormalCoupons.setText(useCoupons.getRebate() + "折");
            mReleaseNormalCoupons.setText(getCoupNum(useCoupons.getRebate()) + "折");
        } else {    //优惠金额
            mReleaseNormalCoupons.setText("￥ " + useCoupons.getCash());
        }
    }

    private String getCoupNum(int useCouponsRebate) {
        String s = String.valueOf(useCouponsRebate);
        if (s.length() == 2) {
            if (s.endsWith("0")) {
                return s.substring(0, 1);
            } else {
                return String.valueOf(((double) useCouponsRebate) / 10);
            }
        }
        return s;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isPlaying && hidden) {
            stopEndPlay();
        }

        if (homeActivity != null && homeActivity.showCityAreaId != 35) {
//            showToast("您的城市即将开通专属服务");
//            if (hidden) {  //隐藏的时候
//                mReleaseNormalNoCity.setVisibility(View.GONE);
//                mHandler.removeMessages(SHOULD_HIDE);
//            } else {    //展示的时候
//                mHandler.sendEmptyMessageDelayed(SHOULD_HIDE, 2500);
//                mReleaseNormalNoCity.setVisibility(View.VISIBLE);
//            }
        }
    }

    public void stopEndPlay() {
        if (!isPlaying) {
            return;
        }
        endPlay();
        mTvClickRadio.setText("点击播放");
        mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_RES_TITLE
                && response instanceof FillOrderTitleResponse) {
            FillOrderTitleResponse fillOrderTitleResponse = (FillOrderTitleResponse) response;
            if (fillOrderTitleResponse.getCode() == 1) {
                List<String> titles = fillOrderTitleResponse.getData().getTitles();
                if (titles != null && titles.size() != 0) {
                    titleList = titles;
                    showTitleListContent();
                }
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                mReleaseNormalTitle.setText(sendSuccessResponse.getData().getReplaceName());
            } else {
                showToast(sendSuccessResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST
                && response instanceof GetServiceListResponse) {
            GetServiceListResponse getServiceListResponse = (GetServiceListResponse) response;
            if (getServiceListResponse.getCode() == 1) {
                isHaveMore = getServiceListResponse.getData().isHasMore();
                if (!getServiceListResponse.getData().isCityOpen()) {
                    mRelaseNoServiceList.setVisibility(View.VISIBLE);
                    mReleaseNormalHorisv.setScrollX(0);
                    serviceLists = null;
                    mReleaseNormalHorisv.setVisibility(View.INVISIBLE);
                    if (null != mHomePublishServiceAdapter) {
                        mHomePublishServiceAdapter.setmCurrentSelected(-1);
                    }
                    return;
                }
                mRelaseNoServiceList.setVisibility(View.GONE);
                List<GetServiceListResponse.DataBean.ServiceBean> services = getServiceListResponse.getData().getService();
                if (services != null && services.size() != 0) {
                    mReleaseNormalHorisv.setVisibility(View.VISIBLE);
                    if (null == mHomePublishServiceAdapter || page == 1) {
                        mReleaseNormalHorisv.setScrollX(0);
                        serviceLists = services;
                        mHomePublishServiceAdapter = new HomePublishServiceAdapter(homeActivity, this, serviceLists, mReleaseNormalLl, mInflater);
                        mReleaseNormalGrid.setAdapter(mHomePublishServiceAdapter);
                    } else {
                        serviceLists.addAll(services);
                        mHomePublishServiceAdapter.notifyDataSetChanged();
                        mReleaseNormalHorisv.setScrollX(mReleaseNormalHorisv.getScrollX() + DensityUtil.dip2px(homeActivity, 6));
                    }
                    int size = mHomePublishServiceAdapter.getCount();
                    DisplayMetrics dm = new DisplayMetrics();
                    homeActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    float density = dm.density;

                    int columnsNum = size;
                    int allWidth = (int) (173 * columnsNum * density);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                    mReleaseNormalGrid.setLayoutParams(params);
                    mReleaseNormalGrid.setStretchMode(GridView.NO_STRETCH);
                    mReleaseNormalGrid.setNumColumns(mHomePublishServiceAdapter.getCount());
                } else {
                    showToast("没有符合条件的餐厅");
                    mReleaseNormalHorisv.setScrollX(0);
                    serviceLists = null;
                    mReleaseNormalHorisv.setVisibility(View.INVISIBLE);
                    if (null != mHomePublishServiceAdapter) {
                        mHomePublishServiceAdapter.setmCurrentSelected(-1);
                    }
                }
            } else {
                showToast(getServiceListResponse.getMessage());
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_IS_VIP
                && response instanceof UserIsVipResponse) {
            UserIsVipResponse mIsVipResponse = (UserIsVipResponse) response;
            if (mIsVipResponse.getCode() == 1) {
                isUserVip = mIsVipResponse.getData().isIsvip();
            } else {
                isUserVip = false;
            }
            mCbReleaseNormalVipPay.setChecked(isUserVip);
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_COUPONS
                && response instanceof MyCouPonsResponse) {
            MyCouPonsResponse myCouPonsResponse = (MyCouPonsResponse) response;
            if (myCouPonsResponse.getCode() == 1) {
                List<MyCouPonsResponse.DataBean.CouponsBean> mCouponsBean = myCouPonsResponse.getData().getCoupons();
                if (null == mCouponsBean || mCouponsBean.size() == 0) {
                    mReleaseNormalCoupons.setText("无可用优惠券");
                    isHaveCoupons = false;
                    mCbReleaseNormal.setChecked(isHaveCoupons);
                } else {
                    useCoupons = mCouponsBean.get(0);
                    updateCoupons();
                }
            } else {
                isHaveCoupons = false;
                mCbReleaseNormal.setChecked(isHaveCoupons);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST) {
            mRelaseNoServiceList.setVisibility(View.VISIBLE);
            mReleaseNormalHorisv.setScrollX(0);
            serviceLists = null;
            mReleaseNormalHorisv.setVisibility(View.INVISIBLE);
            if (null != mHomePublishServiceAdapter) {
                mHomePublishServiceAdapter.setmCurrentSelected(-1);
            }
        }
    }

    public void showTitleListContent() {
        ListView mListView = initListView();
        pw = new PopupWindow(mListView, mReleaseNormalFlagLine.getWidth() / 4 * 3, getTotalHeightofListView(mListView));
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.showAsDropDown(mReleaseNormalFlagLine, -10, 0, Gravity.END);
        should = true;
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (should) {
                    mReleaseNormalMore.setImageResource(R.drawable.icon_more2x);
                }
            }
        });
    }

    private ListView initListView() {
        ListView mListView = new ListView(homeActivity);
        mListView.setDividerHeight(1);
        mListView.setBackgroundResource(R.drawable.listview_background);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == titleList.size()) {
                    should = false;
                    showTitleList();
                } else {
                    mReleaseNormalTitle.setText(titleList.get(position));
                }
                pw.dismiss();
            }
        });

        if (mAdapter == null) {
//            mAdapter = new ReleaseTitleAdapter(homeActivity, titleList);
        } else {
            mAdapter.bingData(titleList);
        }
        mListView.setAdapter(mAdapter);
        return mListView;
    }

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            homeActivity.runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    second--;
                    mReleaseNormalRecordTime.setText(second + "'" + "秒");
                    if (second == 0) {
                        if (isRecording) { //录音中。。
                            relaceTimer();
                            hasRecorded = true;
                            mTvClickRadio.setText("点击播放");
                            mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
                            mReleaseNormalDeleteSound.setVisibility(View.VISIBLE);
                            isRecording = false;
                            endRecord();
                            homeActivity.showToastInfo("已达到录制时间上限");
                            mReleaseNormalRecordTime.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    }

    private void relaceTimer() {
        timer.cancel();
        timer.purge();
        timer = null;
    }

    //发布邀请成功后重置所有信息
    public void resetInfo() {
        mReleaseNormalTitle.setText("");
        mReleaseNormalTime.setText("");

        serviceBean = null;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (myRecorder != null) {
            myRecorder.release();
            myRecorder = null;
        }
        if (rocordSoundFile.exists()) {
            rocordSoundFile.delete();
        }

        mReleaseNormalDeleteSound.setVisibility(View.GONE);
        mRadioIcon.setVisibility(View.VISIBLE);
        mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
        mTvClickRadio.setText("点击录制");

        selectedTime = "";
        mSelectedTime = 0;

        isPlaying = false;
        hasRecorded = false;
        isRecording = false;

        isHaveCoupons = false;
        useCoupons = null;

        mReleaseNormalPrice.setText("");
        mReleaseNormalCoupons.setText("");
        mCbReleaseNormal.setChecked(false);

        second = 5;

        mReleaseNormalPublish.setText("发布");
        mReleaseNormalPublish.setClickable(true);

        mInviteId = 0;
    }

    private void removeAdapter() {
        serviceLists = null;
        if (mHomePublishServiceAdapter != null) {
            page = 1;
            isHaveMore = false;
            mHomePublishServiceAdapter.setPhotos(serviceLists);
            mHomePublishServiceAdapter.setmCurrentSelected(-1);
            mHomePublishServiceAdapter = null;
        }
    }

    //发起活动
    public String httpRequest(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = null;
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    File file = new File(inputValue);
                    String filename = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + "audio/aac" + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            ALog.d("response" + res);
            final SendDataPublicResponse response;
            Gson gson = new Gson();
            response = gson.fromJson(res, SendDataPublicResponse.class);
            reader.close();
            reader = null;
            if (response.getCode() == 1) {
                SendDataPublicResponse.DataEntity dataBean = response.getData();
                if (dataBean.getTrade().getPrice() > 0 && dataBean.getInvite().getNewstatus() != 50) {
                    TrideBean trideBean = new TrideBean(true, dataBean.getInvite().getServiceId(), dataBean.getPublishType(),
                            dataBean.getUserLogo(), dataBean.getInvite().getTitle(),
                            dataBean.getInvite().getTime(), dataBean.getTrade().getPayprice(), response.getData().getTrade().getTradeNum());
                    mInviteId = dataBean.getInvite().getId();
                    homeActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReleaseNormalPublish.setText("发布");
                            mReleaseNormalPublish.setClickable(true);
                        }
                    });
//                    PayOrderActivity.startPayOrderActForResult(homeActivity, MainActivity.REQUEST_CODE_RELEASE_NORMAL, trideBean);
                } else {
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("发布成功,请耐心等待");
                            mReleaseNormalPublish.setText("发布");
                            mReleaseNormalPublish.setClickable(true);
                            ActivityUtils.startActivityForData(homeActivity,
                                    InviteInviterDetailActivity.class, String.valueOf(response.getData().getInvite().getId()));
                        }
                    });
                }
            } else {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(response.getMessage());
                        mReleaseNormalPublish.setText("发布");
                        mReleaseNormalPublish.setClickable(true);
                    }
                });
            }

        } catch (Exception e) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mReleaseNormalPublish.setClickable(true);
                    mReleaseNormalPublish.setText("发布");
                }
            });
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }

}
