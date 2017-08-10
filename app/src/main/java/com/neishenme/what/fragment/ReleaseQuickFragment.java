package com.neishenme.what.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.PayOrderActivity;
import com.neishenme.what.adapter.ReleaseTitleAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.ui.SelectedPlaceActivity;
import com.neishenme.what.bean.FillOrderTitleResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendDataPublicResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.dialog.ReleaseMoneyQuickDialog;
import com.neishenme.what.dialog.ReleaseTimePickerDialog;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnAudioPermissionListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
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

/**
 * 急速发布界面
 */
public class ReleaseQuickFragment extends Fragment implements HttpLoader.ResponseListener {
    private MainActivity homeActivity;

    //    public static String timeLimit;
    private int MONEY_START = 0;
    private int MONEY_END = 1000;
    private int MONEY_BETWEEN = 50;

    public TextView mReleaseQuickPublish;
    private EditText mReleaseQuickTitle;
    private ImageView mReleaseQuickMore;
    private View mReleaseQuickFlagLine;
    private TextView mReleaseQuickTime;
    private RadioGroup mReleaseQuickTargetType;
    private LinearLayout mReleaseQuickRecordSound;
    private ImageView mRadioIcon;
    private TextView mTvClickRadio;
    private LinearLayout mReleaseQuickDeleteSound;
    private TextView mReleaseQuickRecordTime;
    private TextView mReleaseQuickPlease;
    private TextView mReleaseQuickPrice;

    private MediaPlayer mPlayer; //音乐播放器
    private MediaRecorder myRecorder; //声音录制器
    private File rocordSoundFile;   //录制声音

    //显示获取主题的list相关
    private PopupWindow pw;
    private List<String> titleList;
    private ReleaseTitleAdapter mAdapter;
    private boolean should = true; //是否应该将箭头返回去

    //录音倒计时相关
    private TimerTask task; //计时器
    private Timer timer;
    private int second = 5;  //倒计时秒数

    private String selectedTime = ""; //选择的时间

    private long mSelectedTime = 0; //选择的时间,新的

    //播放状态相关
    //正在播放 , 录制完成 , 正在录制 标记三种状态
    private boolean isPlaying = false, hasRecorded = false, isRecording = false;
    private boolean Recording = false;//判断是否录制过
    private ArrayList<String> mMoneyDate;   //该页需要显示的钱

    private boolean isSelectedPlace = false; //标记是否已经选择发单位置
    private String placeName;
    private String placeShow;
    private String placeCity;
    private String placeAddress;
    private String placeLatitude;
    private String placeLongtitude;

    private int mInviteId = 0;


    public ReleaseQuickFragment() {
    }

    public static ReleaseQuickFragment newInstance() {
        ReleaseQuickFragment fragment = new ReleaseQuickFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_release_quick, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mReleaseQuickPublish = (TextView) view.findViewById(R.id.release_quick_publish);

        mReleaseQuickTitle = (EditText) view.findViewById(R.id.release_quick_title);
        mReleaseQuickMore = (ImageView) view.findViewById(R.id.release_quick_more);
        mReleaseQuickFlagLine = view.findViewById(R.id.release_quick_flag_line);

        mReleaseQuickTime = (TextView) view.findViewById(R.id.release_quick_time);
        mReleaseQuickTargetType = (RadioGroup) view.findViewById(R.id.release_quick_target_type);

        mReleaseQuickRecordSound = (LinearLayout) view.findViewById(R.id.release_quick_record_sound);
        mReleaseQuickDeleteSound = (LinearLayout) view.findViewById(R.id.release_quick_delete_sound);
        mRadioIcon = (ImageView) view.findViewById(R.id.radio_icon);
        mTvClickRadio = (TextView) view.findViewById(R.id.tv_click_radio);
        mReleaseQuickRecordTime = (TextView) view.findViewById(R.id.release_quick_record_time);

        mReleaseQuickPlease = (TextView) view.findViewById(R.id.release_quick_please);
        mReleaseQuickPrice = (TextView) view.findViewById(R.id.release_quick_price);
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();

//        StringBuilder sb = new StringBuilder();
//        for (int i = 1; i < 24; i++) {
//            if (i == 23)
//                sb.append(i);
//            else
//                sb.append(i + ",");
//        }
//        timeLimit = sb.toString();

        mMoneyDate = new ArrayList<>();
        for (int i = MONEY_START; i <= MONEY_END; i += MONEY_BETWEEN) {
            mMoneyDate.add(i + "");
        }

        rocordSoundFile = new File(FileUtil.getSDAudioDir(), "mypublish.aac");
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {
            rocordSoundFile.delete();
        }

    }

    private void initListener() {
        mReleaseQuickPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                tryPublic();
            }
        });

        mReleaseQuickMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {

                } else {
                    mReleaseQuickMore.setImageResource(R.drawable.icon_more2xdown);
                    showTitleList();
                }
            }
        });
        mReleaseQuickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                editTime();
            }
        });
        mReleaseQuickRecordSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                Recording = true;
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
                    mReleaseQuickDeleteSound.setVisibility(View.VISIBLE);
                    isRecording = false;
                    endRecord();
                    mReleaseQuickRecordTime.setVisibility(View.INVISIBLE);
                    setRecordingButton();
                    return;
                }

                //没有正在播放 , 没有录制完成, 没有正在录制 ,..说明此时是刚进来,点击是需要录制的
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
        });

        mReleaseQuickDeleteSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                Recording = false;
                if (isPlaying) {
                    endPlay();
                }
                if (rocordSoundFile.exists()) {
                    rocordSoundFile.delete();
                }
                mReleaseQuickDeleteSound.setVisibility(View.GONE);
                mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
                mTvClickRadio.setText("点击录制");
                hasRecorded = false;
                //设置button颜色
                setRecordingButton();
            }
        });

//        mReleaseQuickPlease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearTitleFocus();
//                homeActivity.startActivityForResult(new Intent(homeActivity, SelectedPlaceActivity.class),
//                        MainActivity.REQUEST_CODE_SELECTED_PLACE);
//            }
//        });

        mReleaseQuickPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                ReleaseMoneyQuickDialog moneyDialog = new ReleaseMoneyQuickDialog(homeActivity, mMoneyDate, ReleaseQuickFragment.this);
                moneyDialog.show();
            }
        });

        mReleaseQuickTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (v.getId() == R.id.release_quick_title && !hasFocus) {
                if (!hasFocus) {
                    checkSentive(mReleaseQuickTitle.getText().toString().trim());
                }
            }
        });

        mReleaseQuickTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //当输入完主题判断一下是不是全部输入完成
                setRecordingButton();
            }
        });
    }

    //设置发布按钮button是灰色还是亮色
    private void setRecordingButton() {
        //在录音时，点击停止，判断一下是不是全部输入完成
        Boolean inputComplete = isInputComplete();
        if(inputComplete) {
            mReleaseQuickPublish.setEnabled(true);
            mReleaseQuickPublish.setText("发布");
            mReleaseQuickPublish.setTextColor(Color.BLACK);
//            mReleaseQuickPublish.setBackground(getActivity().getDrawable(R.drawable.bg_date2x));
//            mReleaseQuickPublish.setBackground(getActivity().getDrawable(R.drawable.yes_recording));
        }else {
            mReleaseQuickPublish.setText("");
            mReleaseQuickPublish.setEnabled(false);
//            mReleaseQuickPublish.setBackground(getActivity().getDrawable(R.drawable.no_recording));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isPlaying && hidden) {
            stopEndPlay();
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

    private void clearTitleFocus() {
        mReleaseQuickTitle.setFocusable(false);
        mReleaseQuickTitle.setFocusableInTouchMode(true);
    }

    private void checkSentive(String title) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", title);
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_SENSITIVE_CHECK, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK, this).setTag(this);
    }

    private void tryPublic() {
        stopEndPlay();
        if (!NSMTypeUtils.isLogin()) {
            showToast(getString(R.string.user_should_login_suggest));
            ActivityUtils.startActivity(homeActivity, LoginActivity.class);
            return;
        }

        if (homeActivity.showCityAreaId != CityLocationConfig.cityLocationId) {
            showToast("暂不能发布异地活动");
            return;
        }

        String title = mReleaseQuickTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast("输入活动主题");
            return;
        }
        if (title.length() > 15) {
            showToast("主题过长,请修改一下吧");
            mReleaseQuickTitle.setText("");
            return;
        }

        String publishTime = getOrderTime();
        if (TextUtils.isEmpty(publishTime)) {
            showToast("活动时间不正确,请重新设置时间");
            mReleaseQuickTime.setHint("点击设置时间");
            return;
        }

        if (isRecording) {
            showToast("请先录音完毕");
            return;
        }

        if (!isSelectedPlace) {
            showToast("选择地点");
            return;
        }

        String price = getPrice();
        if (TextUtils.isEmpty(price)) {
            showToast("选择预算");
            return;
        }

        releaseInvite(title, publishTime, price);
    }

    //发布邀请的逻辑
    private void releaseInvite(String title, String publishTime, String price) {
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {

            mReleaseQuickPublish.setClickable(false);
            mReleaseQuickPublish.setText("正在发布");

            final HashMap<String, String> params = new HashMap<>();
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("areaId", String.valueOf(CityLocationConfig.cityLocationId));
            params.put("title", title);
            params.put("publishType", "1");
            params.put("target", getTarget());
            params.put("time", publishTime);
            params.put("precharge", price);
            params.put("inviteCity", placeCity);
            params.put("address", placeAddress);
            params.put("position", placeName);
            params.put("indistinct", placeShow);
            params.put("invitelat", placeLatitude);
            params.put("invitelog", placeLongtitude);
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
            showToast("录入语音");
            return;
        }
    }

    private void editTime() {
//        MyDateReleaseQuickPickerDialog myDAtePickerDialog = new MyDateReleaseQuickPickerDialog(homeActivity, timeLimit, "时间");
//        myDAtePickerDialog.setOnConfirmListeners(new MyDateReleaseQuickPickerDialog.OnConfirmListeners() {
//            @Override
//            public void onConfirmClicked(int hourSelected, int minuteSelected) {
//                String hour = timeLimit.split(",")[hourSelected];
//                String minute = minuteSelected + "";
//                if (minute.length() == 1) {
//                    minute = "0" + minute;
//                }
//                selectedTime = hour + ":" + minute;
//                if (getGreatTime(selectedTime)) {
//                    mReleaseQuickTime.setText(hour + ":" + minute);
//                } else {
//                    showToast("发布时间与活动时间间隔过短");
//                }
//            }
//        });
//        myDAtePickerDialog.show();

        ReleaseTimePickerDialog mReleaseTimePickerDialog = new ReleaseTimePickerDialog(
                homeActivity, System.currentTimeMillis() + 120 * 1000,
                ReleaseTimePickerDialog.RELEASE_QUICK, new ReleaseTimePickerDialog.OnConfirmListeners() {
            @Override
            public void onConfirmClicked(long selectedTime) {
                if ((selectedTime - System.currentTimeMillis()) >= (1000 * 60)) {
                    mSelectedTime = selectedTime;
                    if (TimeUtils.isToday(selectedTime)) {
                        mReleaseQuickTime.setText("今天 " + DateUtils.formatTimeSimple(selectedTime));

                    } else {
                        mReleaseQuickTime.setText("明天 " + DateUtils.formatTimeSimple(selectedTime));
                    }

                    //当选择完时间以后判断一下是不是全部输入完成
                    setRecordingButton();

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
        mReleaseQuickRecordTime.setVisibility(View.VISIBLE);
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IOException e) {
            homeActivity.showToastError("录音失败！");
            myRecorder.release();
            myRecorder = null;
        }
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

    private String getPrice() {
        String price = mReleaseQuickPrice.getText().toString().trim();
        try {
            Integer.parseInt(price);
        } catch (Exception e) {
            return "";
        }
        return price;
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

    private boolean getGreatTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return false;
        }
        String orderT = DateUtils.formatDate(System.currentTimeMillis()) + " " + selectedTime;
        long order = DateUtils.formatToLong(orderT, "yyyy-MM-dd HH:mm");
        if ((order - System.currentTimeMillis()) >= (1000 * 60)) {
            return true;
        } else {
            return false;
        }
    }

    private String getTarget() {
        String target = "0";
        switch (mReleaseQuickTargetType.getCheckedRadioButtonId()) {
            case R.id.release_quick_male:
                target = "1";
                break;
            case R.id.release_quick_female:
                target = "2";
                break;
            case R.id.release_quick_other:
                target = "0";
                break;
        }
        return target;
    }

    //当预算选择成功调用 ,可以自定已也可以直接选择,所以有两个入口
    public void onMoneyEdit(String editMoney) {
        mReleaseQuickPrice.setText(editMoney);

        //当输入预算以后是判断一下是不是全部输入完成
        setRecordingButton();
    }

    //当place选择调用
    public void onPlaceSelected(
            String placeName, String showPlace, String placeCity, String placeAdress, String latitude, String longtitude) {
        this.placeName = placeName;
        this.placeShow = showPlace;
        this.placeCity = placeCity;
        this.placeAddress = placeAdress;
        this.placeLatitude = latitude;
        this.placeLongtitude = longtitude;
        isSelectedPlace = true;
        mReleaseQuickPlease.setText(placeShow);

        //当选择地点完成是判断一下是不是全部输入完成
        setRecordingButton();
    }

    //当支付成功时调用
    public void onPaySuccess() {
        if (mInviteId != 0) {
            ActivityUtils.startActivityForData(homeActivity, InviteInviterDetailActivity.class, String.valueOf(mInviteId));
        }
        resetInfo();
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
                mReleaseQuickTitle.setText(sendSuccessResponse.getData().getReplaceName());
            } else {
                showToast(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
    }

    public void showTitleListContent() {
        ListView mListView = initListView();
        pw = new PopupWindow(mListView, mReleaseQuickFlagLine.getWidth() / 4 * 3, getTotalHeightofListView(mListView));
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.showAsDropDown(mReleaseQuickFlagLine, -10, 0, Gravity.END);
        should = true;
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (should) {
                    mReleaseQuickMore.setImageResource(R.drawable.icon_more2x);
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
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == titleList.size()) {
                    should = false;
                    showTitleList();
                } else {
                    mReleaseQuickTitle.setText(titleList.get(position));
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
                    mReleaseQuickRecordTime.setText(second + "'" + "秒");
                    if (second == 0) {
                        if (isRecording) { //录音中。。
                            relaceTimer();
                            hasRecorded = true;
                            mTvClickRadio.setText("点击播放");
                            mRadioIcon.setImageResource(R.drawable.paly_audio_icon);
                            mReleaseQuickDeleteSound.setVisibility(View.VISIBLE);
                            isRecording = false;
                            endRecord();
                            homeActivity.showToastInfo("已达到录制时间上限");
                            mReleaseQuickRecordTime.setVisibility(View.INVISIBLE);

                            //当录音完成是判断一下是不是全部输入完成
                            setRecordingButton();
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
        mReleaseQuickTitle.setText("");
        mReleaseQuickTime.setText("");

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
        mReleaseQuickDeleteSound.setVisibility(View.GONE);
        mRadioIcon.setImageResource(R.drawable.icon_microphone2x);
        mTvClickRadio.setText("点击录制");

        selectedTime = "";
        mSelectedTime = 0;

        isPlaying = false;
        hasRecorded = false;
        isRecording = false;

        mReleaseQuickPlease.setText("");
        mReleaseQuickPrice.setText("");
        isSelectedPlace = false;

        second = 5;

        mReleaseQuickPublish.setText("");
//        mReleaseQuickPublish.setText("发布");
        mReleaseQuickPublish.setClickable(true);

        mInviteId = 0;
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
//                if (dataBean.getTrade().getPrice() > 0 && dataBean.getInvite().getNewstatus() != 50) {
                mInviteId = dataBean.getInvite().getId();
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReleaseQuickPublish.setText("");
//                        mReleaseQuickPublish.setText("发布");
                        mReleaseQuickPublish.setClickable(true);
                    }
                });
                if (dataBean.getTrade().getPrice() > 0) {
                    TrideBean trideBean = new TrideBean(false, 0, dataBean.getPublishType(),
                            dataBean.getUserLogo(), dataBean.getInvite().getTitle(),
                            dataBean.getInvite().getTime(), dataBean.getTrade().getPayprice(),
                            response.getData().getTrade().getTradeNum());
//                    PayOrderActivity.startPayOrderActForResult(homeActivity, MainActivity.REQUEST_CODE_RELEASE_QUICK, trideBean);
                } else {
                    homeActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("发布成功");
                            mReleaseQuickPublish.setText("");
                            onPaySuccess();
                        }
                    });
                }
//                } else {
//                    UIUtils.runInMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showToast("发布成功,请耐心等待");
//                            mReleaseQuickPublish.setText("发布");
//                            mReleaseQuickPublish.setClickable(true);
////                            ActivityUtils.startActivityForData(homeActivity,
////                                    InviteInviterDetailActivity.class, String.valueOf(response.getData().getInvite().getId()));
//                        }
//                    });
//                }
            } else {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(response.getMessage());
                        mReleaseQuickPublish.setText("");
//                        mReleaseQuickPublish.setText("发布");
                        mReleaseQuickPublish.setClickable(true);
                    }
                });
            }

        } catch (Exception e) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mReleaseQuickPublish.setClickable(true);
                    mReleaseQuickPublish.setText("");
//                    mReleaseQuickPublish.setText("发布");
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


    //判断所有选项是不是全部输入完成，按钮变色
    private Boolean isInputComplete(){
        String title = mReleaseQuickTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
//            showToast("输入活动主题");
            return false;
        }

        String publishTime = getOrderTime();
        if (publishTime == null ||TextUtils.isEmpty(publishTime)) {
//            showToast("活动时间不正确,请重新设置时间");
//            mReleaseQuickTime.setHint("点击设置时间");
            return false;
        }

        if (!Recording) {
//            showToast("请先录音完毕");
            return false;
        }

        if (!isSelectedPlace) {
//            showToast("选择地点");
            return false;
        }

        String price = getPrice();
        if (TextUtils.isEmpty(price)) {
//            showToast("选择预算");
            return false;
        }
        return true;
    }

}
