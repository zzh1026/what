package com.neishenme.what.fragment;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.adapter.HomePublishServiceAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.bean.FillOrderTitleResponse;
import com.neishenme.what.bean.GetServiceListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendDataPublicResponse;
import com.neishenme.what.dialog.MyDatePickerDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.Gps;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PositionUtil;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 发布界面
 */
@Deprecated
public class HomePublishFragment extends Fragment implements HttpLoader.ResponseListener {
    private MainActivity homeActivity;
    private LocationService locationService; //获取位置

    public static String timeLimit = "10,11,12,13,14,15,16,17,18,19,20,21,22";

    private boolean isGetLocation = false; //判断是否获取位置
    private double userLatitude = 0.0;
    private double userLonggitude = 0.0;

    private PopupWindow pw;
    private List<String> titleList;
    private TitleAdapter mAdapter;

    private HomePublishServiceAdapter mHomePublishServiceAdapter;  //服务列表适配器
    private List<GetServiceListResponse.DataBean.ServiceBean> serviceLists; //服务列表
    private GetServiceListResponse.DataBean.ServiceBean serviceBean; //选中的服务

    private MediaPlayer mPlayer; //音乐播放器
    private MediaRecorder myRecorder = null; //声音录制器
    private File rocordSoundFile;

    private TimerTask task; //计时器
    private Timer timer;
    private int second = 5;  //倒计时秒数

    private boolean should = true; //是否应该将箭头返回去
    private boolean isClearCheck = false; //是否应该将箭头返回去
    private String selectedTime = ""; //选择的时间

    //正在播放 , 录制完成 , 正在录制 标记三种状态
    private boolean isPlaying = false, hasRecorded = false, isRecording = false;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private EditText mHomePublishTitle;
    private View mHomePublishLineFlag;
    private ImageView mHomePublishMore;
    private TextView mHomePublishTime;

    private RadioGroup mRestPaytypeRg;
    private RadioGroup mRestTargettypeRg;

    private LinearLayout mHomePublishRecord;
    private LinearLayout mHomePublishDelete;
    private ImageView mRadioIcon;
    private TextView mTvClickRadio;
    private TextView mTvTime;

    private RadioGroup mHomePublishRgYusuan;
    private LinearLayout mIdGallery;
    private GridView mGridview;
    private LayoutInflater mInflater;

    private TextView mHomePublishPublish;
    private HorizontalScrollView mInviteHome;

    private String selectedPrice = "";
    private int selectedPage = 1;
    private boolean isHaveMore = false;

    public HomePublishFragment() {

    }

    public static HomePublishFragment newInstance() {
        HomePublishFragment fragment = new HomePublishFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (MainActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_publish, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        mHomePublishTitle = (EditText) view.findViewById(R.id.home_publish_title);
        mHomePublishMore = (ImageView) view.findViewById(R.id.home_publish_more);
        mHomePublishTime = (TextView) view.findViewById(R.id.home_publish_time);

        mRestPaytypeRg = (RadioGroup) view.findViewById(R.id.rest_paytype_rg);
        mRestTargettypeRg = (RadioGroup) view.findViewById(R.id.rest_targettype_rg);

        mHomePublishRecord = (LinearLayout) view.findViewById(R.id.home_publish_record);
        mHomePublishDelete = (LinearLayout) view.findViewById(R.id.home_publish_delete);
        mRadioIcon = (ImageView) view.findViewById(R.id.radio_icon);
        mTvClickRadio = (TextView) view.findViewById(R.id.tv_click_radio);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);

        mHomePublishRgYusuan = (RadioGroup) view.findViewById(R.id.home_publish_rg_yusuan);
        mIdGallery = (LinearLayout) view.findViewById(R.id.id_gallery);
        mGridview = (GridView) view.findViewById(R.id.gridview);
        mInflater = LayoutInflater.from(homeActivity);

        mHomePublishPublish = (TextView) view.findViewById(R.id.home_publish_publish);

        mHomePublishLineFlag = view.findViewById(R.id.home_publish_line_flag);

        mInviteHome = (HorizontalScrollView) view.findViewById(R.id.invite_home);
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();

        rocordSoundFile = new File(FileUtil.getSDAudioDir(), "mypublish.aac");
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {
            rocordSoundFile.delete();
        }

        locationService = NSMMapHelper.getInstance().locationService;
        locationService.registerListener(mListener);

        getLocation();
    }

    private void initListener() {
        mHomePublishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCheckRB();

                removeAdapter();

                editTime();
            }
        });

        mHomePublishRgYusuan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheck) {
                    isClearCheck = false;
                    return;
                }

                selectedPage = 1;
                selectedPrice = "";
                switch (checkedId) {
                    case R.id.home_publish_rb_1:
                        selectedPrice = "1000";
                        break;
                    case R.id.home_publish_rb_2:
                        selectedPrice = "2000";
                        break;
                    case R.id.home_publish_rb_3:
                        selectedPrice = "3000";
                        break;
                    case R.id.home_publish_rb_4:
                        selectedPrice = "4000";
                        break;
                    case R.id.home_publish_rb_5:
                        selectedPrice = "5000";
                        break;
                    default:
                        break;
                }
                if (TextUtils.isEmpty(selectedPrice)) {
                    return;
                }

                removeAdapter();

                getLocation();
                getServiceList(1);
            }
        });

        mHomePublishMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {

                } else {
                    mHomePublishMore.setImageResource(R.drawable.icon_more2xdown);
                    showTitleList();
                }
            }
        });

        mHomePublishRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) { //如果正在播放,停止播放
                    endPlay();
                    mTvClickRadio.setText("点击播放");
                    mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
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
                    mHomePublishDelete.setVisibility(View.VISIBLE);
                    isRecording = false;
                    endRecord();
                    mTvTime.setVisibility(View.INVISIBLE);
                    return;
                }

                //没有正在播放 , 没有录制完成, 没有正在录制 ,..说明此时是刚进来,点击是需要录制的
                homeActivity.showToastInfo("开始录音...");
                second = 5;
                mTvClickRadio.setText("点击停止");
                startRecord();
            }
        });

        mHomePublishDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying == true) {
                    endPlay();
                }
                if (rocordSoundFile.exists()) {
                    rocordSoundFile.delete();
                }
                mHomePublishDelete.setVisibility(View.INVISIBLE);
                mRadioIcon.setVisibility(View.VISIBLE);
                mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
                mTvClickRadio.setText("点击录制");
                hasRecorded = false;
            }
        });

        mHomePublishPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryPublic();
            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetServiceListResponse.DataBean.ServiceBean serviceBean = serviceLists.get(position);
                int servicesId = serviceBean.getServices_id();
                PublishOrderActivity.startRestDetailAct(getContext(), servicesId);
            }
        });

        mInviteHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 按下
                    case MotionEvent.ACTION_DOWN:
                        break;
                    // 滑动
                    case MotionEvent.ACTION_MOVE:
                        // 判断离右边的距离为0时
                        if (mGridview.getRight() - mInviteHome.getScrollX() - mInviteHome.getWidth() == 0) {
                            if (isHaveMore) {
                                getServiceList(++selectedPage);
                            } else {
                                showToast("暂无更多");
                            }
                        }
                        // 放手
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void clearCheckRB() {
        isClearCheck = true;
        mHomePublishRgYusuan.clearCheck();
    }

    private void getServiceList(int page) {
        String orderTime = getOrderTime();
        if (TextUtils.isEmpty(orderTime)) {
            showToast("请先选择时间");
            mHomePublishRgYusuan.clearCheck();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pageSize", "10");
        params.put("longitude", String.valueOf(userLonggitude));
        params.put("latitude", String.valueOf(userLatitude));
        params.put("price", selectedPrice);
        params.put("time", orderTime);
        HttpLoader.post(ConstantsWhatNSM.URL_SERVICE_LIST, params, GetServiceListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST, this).setTag(this);
    }

    private void getLocation() {
        isGetLocation = true;
        locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
        locationService.start();
    }

    private void tryPublic() {
        String token = NSMTypeUtils.getMyToken();
        if (TextUtils.isEmpty(token)) {
            showToast("您尚未登录,请先登录");
            ActivityUtils.startActivity(homeActivity, LoginActivity.class);
            return;
        }

        String title = mHomePublishTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast("请标明主题");
            return;
        }

        if (title.length() > 15) {
            showToast("主题过长,请修改一下吧");
            mHomePublishTitle.setText("");
            return;
        }

        String publishTime = getOrderTime();
        if (TextUtils.isEmpty(publishTime)) {
            showToast("邀请时间至少要在一个小时之后,请重新设置时间");
            mHomePublishTime.setHint("点击设置时间");
            return;
        }

        if (isRecording) {
            showToast("请先录音完毕");
            return;
        }

        int servicesId;
        int servicePosition = mHomePublishServiceAdapter.getmCurrentSelected();
        if (servicePosition == -1) {
            showToast("请先选择就餐餐厅");
            return;
        } else {
            serviceBean = serviceLists.get(servicePosition);
            servicesId = serviceBean.getServices_id();
        }

        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {

            mHomePublishPublish.setClickable(false);
            mHomePublishPublish.setText("正在发布");

            final HashMap<String, String> params = new HashMap<>();
            params.put("token", token);
            params.put("serviceId", String.valueOf(servicesId));
            params.put("title", title);
            params.put("target", getTarget());
            params.put("payType", getPayType());
            params.put("time", publishTime);
            params.put("audioDuration", String.valueOf(5 - second));
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
            showToast("请录下声音再发起活动吧");
            return;
        }
    }

    private void editTime() {
        if (DateUtils.formatHour(System.currentTimeMillis()) > 22) {
            showToast("今天已经不能发单,请明天再来吧");
            return;
        }
        MyDatePickerDialog myDAtePickerDialog = new MyDatePickerDialog(homeActivity, timeLimit, "时间");
        myDAtePickerDialog.setOnConfirmListeners(new MyDatePickerDialog.OnConfirmListeners() {
            @Override
            public void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex) {
                Log.d("timeSelect", monthSelectedIndex + "  " + daySelectedIndex);
                String hour = timeLimit.split(",")[monthSelectedIndex];
                String minute = daySelectedIndex * 15 + "";
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                mHomePublishTime.setText(hour + ":" + minute);
                selectedTime = hour + ":" + minute;
            }
        });
        myDAtePickerDialog.show();
    }

    private void showTitleList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("payType", getPayType());
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
        mTvTime.setVisibility(View.VISIBLE);
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
        if (TextUtils.isEmpty(selectedTime)) {
            return null;
        }
        if (selectedTime != null) {
            String orderT = DateUtils.formatDate(System.currentTimeMillis()) + " " + selectedTime;
            long order = DateUtils.formatToLong(orderT, "yyyy-MM-dd HH:mm");
            if ((order - System.currentTimeMillis()) >= (0)) {
                return String.valueOf(order);
            } else {
                return null;
            }
        }
        return null;
    }

    private String getPayType() {
        String target = "3";
        switch (mRestPaytypeRg.getCheckedRadioButtonId()) {
            case R.id.rest_payme_rb:
                target = "1";
                break;
            case R.id.rest_payother_rb:
                target = "2";
                break;
            case R.id.rest_payaa_rb:
                target = "3";
                break;
        }
        return target;
    }

    private String getTarget() {
        String target = "0";
        switch (mRestTargettypeRg.getCheckedRadioButtonId()) {
            case R.id.rest_targetmale_rb:
                target = "1";
                break;
            case R.id.rest_targetfemale_rb:
                target = "2";
                break;
            case R.id.rest_targetunlimit_rb:
                target = "0";
                break;
        }
        return target;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_RES_TITLE
                && response instanceof FillOrderTitleResponse) {
            FillOrderTitleResponse fillOrderTitleResponse = (FillOrderTitleResponse) response;
            if (fillOrderTitleResponse.getCode() == 1) {
                List<String> titles = fillOrderTitleResponse.getData().getTitles();
                if (titles != null && titles.size() != 0) {
                    //upAnimation();
                    titleList = titles;
                    showTitleListContent();
                }
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_LIST
                && response instanceof GetServiceListResponse) {
            GetServiceListResponse getServiceListResponse = (GetServiceListResponse) response;
            if (getServiceListResponse.getCode() == 1) {
                isHaveMore = getServiceListResponse.getData().isHasMore();
                List<GetServiceListResponse.DataBean.ServiceBean> services = getServiceListResponse.getData().getService();
                if (null == mHomePublishServiceAdapter) {
                    mInviteHome.setScrollX(0);
                    serviceLists = services;
//                    mHomePublishServiceAdapter = new HomePublishServiceAdapter(homeActivity, this, serviceLists, mIdGallery, mInflater);
//                    mGridview.setAdapter(mHomePublishServiceAdapter);
                } else {
                    serviceLists.addAll(services);
                    mHomePublishServiceAdapter.notifyDataSetChanged();
                    mInviteHome.setScrollX(mInviteHome.getScrollX() + DensityUtil.dip2px(homeActivity, 6));
                }

                int size = mHomePublishServiceAdapter.getCount();
                DisplayMetrics dm = new DisplayMetrics();
                homeActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                float density = dm.density;

                int columnsNum = size;
                int allWidth = (int) (173 * columnsNum * density);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                mGridview.setLayoutParams(params);
                mGridview.setStretchMode(GridView.NO_STRETCH);
                mGridview.setNumColumns(mHomePublishServiceAdapter.getCount());
            } else {
                showToast(getServiceListResponse.getMessage());
            }
            return;
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
    }

    public void showTitleListContent() {
        // 初始化ListView控件和里边的数据
        ListView mListView = initListView();
        // 弹出一个PopupWindow的窗体, 把ListView作为其内容部分显示.

        pw = new PopupWindow(mListView, mHomePublishLineFlag.getWidth() / 4 * 3, getTotalHeightofListView(mListView));

        // 设置可以使用焦点
        pw.setFocusable(true);

        // 设置popupwindow点击外部可以被关闭
        pw.setOutsideTouchable(true);
        // 设置一个popupWindow的背景
        pw.setBackgroundDrawable(new BitmapDrawable());

        // 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
        pw.showAsDropDown(mHomePublishLineFlag, -10, 0, Gravity.END);
        should = true;

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (should) {
                    mHomePublishMore.setImageResource(R.drawable.icon_more2x);
                }
            }
        });
    }

    /**
     * 初始化一个Listview
     *
     * @return
     */
    private ListView initListView() {
        ListView mListView = new ListView(homeActivity);
        mListView.setDividerHeight(1);
        mListView.setBackgroundResource(R.drawable.listview_background);
        // 去掉右侧垂直滑动条
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == titleList.size()) {
                    should = false;
                    showTitleList();
                } else {
                    mHomePublishTitle.setText(titleList.get(position));
                }
                pw.dismiss();
            }
        });

        // 设置适配器展示数据
        if (mAdapter == null) {
            mAdapter = new TitleAdapter();
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
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        return totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                if (isGetLocation) {
                    isGetLocation = false;
//                    Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
//                    userLatitude = gps.getWgLat();
//                    userLonggitude = gps.getWgLon();
                }
//                locationService.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            homeActivity.runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    second--;
                    mTvTime.setText(second + "'" + "秒");
                    if (second == 0) {
                        if (isRecording) { //录音中。。
                            relaceTimer();
                            hasRecorded = true;
                            mTvClickRadio.setText("点击播放");
                            mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
                            mHomePublishDelete.setVisibility(View.VISIBLE);
                            isRecording = false;
                            endRecord();
                            homeActivity.showToastInfo("已达到录制时间上限");
                            mTvTime.setVisibility(View.INVISIBLE);
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
    private void resetInfo() {
        mHomePublishTitle.setText("");
        mHomePublishTime.setText("点击选择时间");

        removeAdapter();

        serviceBean = null;
        mPlayer = null;
        myRecorder = null;
        if (rocordSoundFile.exists()) {
            rocordSoundFile.delete();
        }
        selectedTime = "";

        isPlaying = false;
        hasRecorded = false;
        isRecording = false;

        clearCheckRB();
        mHomePublishDelete.setVisibility(View.INVISIBLE);
        mRadioIcon.setVisibility(View.VISIBLE);
        mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
        mTvClickRadio.setText("点击录制");

        mHomePublishPublish.setText("发布");
        mHomePublishPublish.setClickable(true);
    }

    private void removeAdapter() {
        serviceLists = null;
        if (mHomePublishServiceAdapter != null) {
            mHomePublishServiceAdapter.setPhotos(serviceLists);
            mHomePublishServiceAdapter.setmCurrentSelected(-1);
            mHomePublishServiceAdapter.notifyDataSetChanged();
            mHomePublishServiceAdapter = null;
        }
    }

    class TitleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titleList.size() + 1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(homeActivity, R.layout.item_fill_order_title, null);
            TextView tvListviewItemTitle = (TextView) view.findViewById(R.id.tv_listview_item_title);
            if (position != titleList.size()) {
                tvListviewItemTitle.setText(titleList.get(position));
            } else {
                LinearLayout llFillOrderBg = (LinearLayout) view.findViewById(R.id.ll_fill_order_bg);
                llFillOrderBg.setBackgroundColor(Color.parseColor("#33000000"));
                llFillOrderBg.setGravity(Gravity.CENTER);
                tvListviewItemTitle.setGravity(Gravity.CENTER);
                tvListviewItemTitle.setTextSize(12f);
                tvListviewItemTitle.setText("更 换");
            }
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetInfo();
                    }
                });
                if (response.getData().getTrade().getPrice() > 0 && response.getData().getInvite().getNewstatus() != 50) {
                    SendDataPublicResponse.DataEntity.TradeEntity tradeBean = response.getData().getTrade();
//                    TrideBean trideBean = new TrideBean(serviceBean.getServices_name(), serviceBean.getServices_logo(),
//                            response.getData().getInvite().getTitle(), response.getData().getInvite().getTime(),
//                            response.getData().getTrade().getPrice(), response.getData().getInvite().getPayType(),
//                            response.getData().getInvite().getServiceId(), response.getData().getTrade().getTradeNum());
////                    PayOrderActivity.startPayOrderActForResult(homeActivity, trideBean);
                } else {
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("发布成功,请耐心等待");
                            mHomePublishPublish.setText("发布");
                            mHomePublishPublish.setClickable(true);
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
                        mHomePublishPublish.setText("发布");
                        mHomePublishPublish.setClickable(true);
                    }
                });
            }

        } catch (Exception e) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mHomePublishPublish.setClickable(true);
                    mHomePublishPublish.setText("发布");
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

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }

}
