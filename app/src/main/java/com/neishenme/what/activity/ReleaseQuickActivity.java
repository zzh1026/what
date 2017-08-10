package com.neishenme.what.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.neishenme.what.adapter.ReleaseTitleAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.commonmap.PlaceCityInfoConfig;
import com.neishenme.what.baidumap.ui.SelectedPlaceActivity;
import com.neishenme.what.bean.FillOrderTitleResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.ReleaseMoneyInfo;
import com.neishenme.what.bean.SendDataPublicResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.dialog.ReleaseQuickActivityDialog;
import com.neishenme.what.dialog.ReleaseTimePickerDialog;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnAudioPermissionListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.utils.UIUtils;
import com.neishenme.what.view.AllTitleView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;
import org.seny.android.utils.MyToast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * 发布活动界面
 */
public class ReleaseQuickActivity extends Activity implements HttpLoader.ResponseListener {

    private OnAudioPermissionListener mOnAudioPermissionListener = null;


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
    private RelativeLayout rl_select;
    private ImageView mRadioIcon;
    private ImageView iv_video; // 视频图片
    private TextView mTvClickRadio;
    private LinearLayout mReleaseQuickDeleteSound;
    private TextView mReleaseQuickRecordTime;
    private TextView mReleaseQuickPlease;
    private TextView mReleaseQuickPrice;
//    private TextView tv_money;
//    private ImageView cb_release;//财富值
//    private boolean isSelectMoney = false;

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
    private LinearLayout ll_video;//录制视频
    private TextView tv_vedio;//长按录制视频
    private LinearLayout ll_video_delete;//删除视频
    private File sdVideoDir = FileUtil.getSDVideoDir();     //获取视频的文件夹
    private File file; //视频文件目录

    private int mInviteId = 0;

    public final int REQUEST_CODE_SELECTED_PLACE = 17;   //极速发布选择地址请求码
    public final int REQUEST_CODE_RELEASE_QUICK = 15;   //极速发布请求码
    public final int REQUEST_CODE_QUICK_RELEASE_PRICE = 12;   //极速发布请求码
    private HashMap map_file;
    private Intent intent;
    private String time;

    private AllTitleView tltle_releasequick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rekeasequick_layout);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        tltle_releasequick = (AllTitleView) findViewById(R.id.tltle_releasequick);
        ll_video = (LinearLayout) findViewById(R.id.ll_video); //点击长按录制视频
        ll_video_delete = (LinearLayout) findViewById(R.id.ll_video_delete); //点击删除视频
        tv_vedio = (TextView) findViewById(R.id.tv_vedio);  //长按录制视频
        iv_video = (ImageView) findViewById(R.id.iv_video); //视频图片
        mReleaseQuickPublish = (TextView) findViewById(R.id.release_quick_publish);
        mReleaseQuickTitle = (EditText) findViewById(R.id.release_quick_title);
        mReleaseQuickMore = (ImageView) findViewById(R.id.release_quick_more);
        mReleaseQuickFlagLine = findViewById(R.id.release_quick_flag_line);
        mReleaseQuickTime = (TextView) findViewById(R.id.release_quick_time);
        mReleaseQuickTargetType = (RadioGroup) findViewById(R.id.release_quick_target_type);

        mReleaseQuickRecordSound = (LinearLayout) findViewById(R.id.release_quick_record_sound);
        mReleaseQuickDeleteSound = (LinearLayout) findViewById(R.id.release_quick_delete_sound);
        mRadioIcon = (ImageView) findViewById(R.id.radio_icon);
        mTvClickRadio = (TextView) findViewById(R.id.tv_click_radio);
        mReleaseQuickRecordTime = (TextView) findViewById(R.id.release_quick_record_time);

        mReleaseQuickPlease = (TextView) findViewById(R.id.release_quick_please);
        mReleaseQuickPrice = (TextView) findViewById(R.id.release_quick_price);
//        rl_select = (RelativeLayout) findViewById(R.id.rl_select);
//        cb_release = (ImageView) findViewById(cb_release);
//
//        tv_money = (TextView) findViewById(tv_money);
    }

    private void initData() {
        //设置title文字
        tltle_releasequick.tv_title.setText("发布");
//        initrequestUrlDate();//初始化数据联网
        file = new File(sdVideoDir, "myVideo.mp4"); //视频路径

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

    private void initrequestUrlDate() {
        String myToken = NSMTypeUtils.getMyToken();
        HashMap<String, String> params2 = new HashMap<>();
        if (myToken != null) {
            params2.put("token", myToken);
        }
        HttpLoader.post(ConstantsWhatNSM.RELEASEMONEY, params2, ReleaseMoneyInfo.class,
                ConstantsWhatNSM.RELEASEMONEY_STATE, this).setTag(this);
    }

    public void deleteVideo() {
        showToast("删除成功");
        setVideoState(false);
        if (file.exists() && file.length() > 0) {
            file.delete();
        }

        ll_video_delete.setVisibility(View.GONE);
        tv_vedio.setText("点击录制");
        iv_video.setImageDrawable(getDrawable(R.drawable.relesea_vedio));
    }

    private void initListener() {
        //财富值的监听事件
//        rl_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isSelectMoney) {
//                    isSelectMoney = false;
//                    cb_release.setImageResource(R.drawable.icon_checbox);
//                } else {
//                    isSelectMoney = true;
//                    cb_release.setImageResource(R.drawable.buy_vip_press);
//                }
//            }
//        });

        //视频删除监听事件
        ll_video_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVideoState(false);
                showToast("删除成功");
                if (file.exists() && file.length() > 0) {
                    file.delete();
                }

                ll_video_delete.setVisibility(View.GONE);
                tv_vedio.setText("点击录制");
                iv_video.setImageDrawable(getDrawable(R.drawable.relesea_vedio));

            }
        });

        //视频点击录制监听事件
        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasVideo) {
                    Intent intent = new Intent(ReleaseQuickActivity.this, PlayVideoActivity.class);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, "1");//播放自己的视频
                    startActivity(intent);
                } else {    //如果没有说明点击的是添加视频,去添加界面
                    //以下是添加权限
                    if (AndPermission.hasPermission(ReleaseQuickActivity.this, Manifest.permission.CAMERA) &&
                            AndPermission.hasPermission(ReleaseQuickActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            AndPermission.hasPermission(ReleaseQuickActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        intent = new Intent(ReleaseQuickActivity.this, RecordVideoActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        requestVideoPermission();
                    }
                }
            }
        });

        //发布逻辑
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
//                    setRecordingButton();
                    return;
                }

                //没有正在播放 , 没有录制完成, 没有正在录制 ,..说明此时是刚进来,点击是需要录制的
                //检查权限
                requestAudioPermission(new OnAudioPermissionListener() {
                    @Override
                    public void onAudioPermissionSuccess() {
                        showToast("开始录音");
                        second = 5;
                        mTvClickRadio.setText("点击停止");
                        startRecord();
                    }

                    @Override
                    public void onAudioPermissionFiler() {
                        showToast("只有授予录音权限才能进行录音发单!");
                    }
                });
//                requestAudioPermission(new OnAudioPermissionListener() {
//                    @Override
//                    public void onAudioPermissionSuccess() {
//                        showToast("开始录音");
//                        second = 5;
//                        mTvClickRadio.setText("点击停止");
//                        startRecord();
//                    }
//
//                    @Override
//                    public void onAudioPermissionFiler() {
//                        showToast("只有授予录音权限才能进行录音发单!");
//                    }
//                });
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

        mReleaseQuickPlease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();

                startActivityForResult(new Intent(ReleaseQuickActivity.this, SelectedPlaceActivity.class),
                        REQUEST_CODE_SELECTED_PLACE);
            }
        });

        mReleaseQuickPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTitleFocus();
                ReleaseQuickActivityDialog moneyDialog = new ReleaseQuickActivityDialog(ReleaseQuickActivity.this, mMoneyDate, ReleaseQuickActivity.this);
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
        if (inputComplete) {
            mReleaseQuickPublish.setEnabled(true);
            mReleaseQuickPublish.setText("发布");
            mReleaseQuickPublish.setTextColor(Color.BLACK);
            mReleaseQuickPublish.setBackground(getResources().getDrawable(R.drawable.bg_date2x));
//            mReleaseQuickPublish.setBackground(getActivity().getDrawable(R.drawable.yes_recording));
        } else {
            mReleaseQuickPublish.setText("");
            mReleaseQuickPublish.setEnabled(false);
            mReleaseQuickPublish.setBackground(getResources().getDrawable(R.drawable.no_recording));
        }
    }

    public void requestAudioPermission(OnAudioPermissionListener mOnAudioPermissionListener) {
        this.mOnAudioPermissionListener = mOnAudioPermissionListener;
        AndPermission.with(this)
                .requestCode(MPermissionManager.REQUEST_CODE_AUDIO)
                .permission(Manifest.permission.RECORD_AUDIO)
                .rationale(mRationaleListener)
                .send();
    }

    private void requestVideoPermission() {
        AndPermission.with(ReleaseQuickActivity.this)
                .requestCode(MPermissionManager.REQUEST_CODE_CAMERA)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .rationale(mRationaleListener)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA:

                    break;
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    if (mOnAudioPermissionListener != null) {
                        mOnAudioPermissionListener.onAudioPermissionSuccess();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA:

                    break;
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    MPermissionManager.showToSetting(ReleaseQuickActivity.this,
                            MPermissionManager.REQUEST_CODE_AUDIO, new MPermissionManager.OnNegativeListner() {
                                @Override
                                public void onNegative() {
                                    if (mOnAudioPermissionListener != null) {
                                        mOnAudioPermissionListener.onAudioPermissionFiler();
                                    }
                                }
                            });
                    break;
            }
        }
    };

    private String message;
    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    message = MPermissionManager.REQUEST_AUDIO_DISCRIBE;
                    break;
                case MPermissionManager.REQUEST_CODE_CAMERA:
                    message = MPermissionManager.REQUEST_CAMERA_RELEASE;
                    break;
            }
            new AlertDialog.Builder(ReleaseQuickActivity.this)
                    .setTitle(MPermissionManager.REQUEST_TITLE)
                    .setMessage(message)
                    .setPositiveButton(MPermissionManager.REQUEST_POSITIVE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();// 用户同意继续申请。
                        }
                    })
                    .setNegativeButton(MPermissionManager.REQUEST_NEGATIVE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel(); // 用户拒绝申请。
                        }
                    }).show();
        }
    };

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
            ActivityUtils.startActivity(ReleaseQuickActivity.this, LoginActivity.class);
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

//        if (isRecording) {
//            showToast("请先录音完毕");
//            return;
//        }

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
//        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.getName().contains(".aac")) {

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
        if (rocordSoundFile.exists() && rocordSoundFile.isFile() && rocordSoundFile.length() > 0) { //mp3
            params2 = new HashMap<>();
            params2.put("audio", rocordSoundFile.getAbsolutePath());
        }
        if (hasVideo) { //视频
            map_file = new HashMap<>();
            map_file.put("video", file.getAbsolutePath());
            params.put("videoDuration", time);
        }
//        if (isSelectMoney) {  //财富值
//            params.put("showCashFlag", 1 + "");
//        } else {
//            params.put("showCashFlag", 0 + "");
//        }

        final HashMap<String, String> finalParams = params2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpRequest(ConstantsWhatNSM.RELESEQUCIKURL, params, finalParams, map_file);
            }
        }).start();
//        } else {
//            showToast("录入语音");
//            return;
//        }
    }

    private void editTime() {
        ReleaseTimePickerDialog mReleaseTimePickerDialog = new ReleaseTimePickerDialog(
                ReleaseQuickActivity.this, System.currentTimeMillis() + 120 * 1000,
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
        if (!TextUtils.isEmpty(getOrderTime())) {
            mReleaseTimePickerDialog.setSelectedTime(mSelectedTime);
        }
    }

    private void showTitleList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("payType", "1");
        params.put("gender", App.USERSP.getString("gender", "0"));
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_DATA_RES_TITLE, params, FillOrderTitleResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_RES_TITLE, this).setTag(this);
    }

    public void startPlay() {
        showToast("开始播放");

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
            showToast("播放失败！");
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
            showToast("录音失败！");
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
            ActivityUtils.startActivityForData(ReleaseQuickActivity.this, InviteInviterDetailActivity.class, String.valueOf(mInviteId));
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
//        if (requestCode == ConstantsWhatNSM.RELEASEMONEY_STATE
//                && response instanceof ReleaseMoneyInfo) {
//            ReleaseMoneyInfo sendSuccessResponse = (ReleaseMoneyInfo) response;
//            if (sendSuccessResponse.getCode() == 1) {
//                tv_money.setText("¥ " + sendSuccessResponse.getData().getUserCash());
//            } else {
//                showToast(sendSuccessResponse.getMessage());
//            }
//        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToast("网络访问失败了,请您检查一下网络设置吧");
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
        ListView mListView = new ListView(ReleaseQuickActivity.this);
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
            mAdapter = new ReleaseTitleAdapter(ReleaseQuickActivity.this, titleList);
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
            runOnUiThread(new Runnable() {      // UI thread
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
                            showToast("已达到录制时间上限");
                            mReleaseQuickRecordTime.setVisibility(View.INVISIBLE);

                            //当录音完成是判断一下是不是全部输入完成
//                            setRecordingButton();
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

        iv_video.setImageDrawable(getDrawable(R.drawable.relesea_vedio));
        tv_vedio.setText("点击录制");
        ll_video_delete.setVisibility(View.GONE);

//        cb_release.setImageResource(R.drawable.icon_checbox);

        hasVideo = false;

        if (map_file != null) {
            map_file.clear();
        }
    }

    //发起活动
    public String httpRequest(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, Map<String, String> fileMapvedio) {
        String res = null;
        HttpURLConnection conn = null;
        String filename2 = null;
        String mp4Path = null;
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
            if (textMap != null) {   //参数
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

//             file
            if (fileMap != null) {  //mp3
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


//            mp4
            if (fileMapvedio != null) {
                Iterator<Map.Entry<String, String>> iter = fileMapvedio.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    mp4Path = inputValue;
                    File file = new File(inputValue);
                    filename2 = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename2
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + "video/mp4" + "\r\n\r\n");
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


//
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
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
                runOnUiThread(new Runnable() {
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
                    PayOrderActivity.startPayOrderActForResult(ReleaseQuickActivity.this, REQUEST_CODE_RELEASE_QUICK, trideBean);
                } else {
                    runOnUiThread(new Runnable() {
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
//                        mReleaseQuickPublish.setText("");
                        mReleaseQuickPublish.setText("发布");
                        mReleaseQuickPublish.setClickable(true);
                    }
                });
            }

        } catch (Exception e) {
            Log.e("asd12312adsa", e.toString());
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mReleaseQuickPublish.setClickable(true);
//                    mReleaseQuickPublish.setText("");
                    mReleaseQuickPublish.setText("发布");
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
        MyToast.showConterToast(App.getApplication(), str);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //判断所有选项是不是全部输入完成，按钮变色
    private Boolean isInputComplete() {
        String title = mReleaseQuickTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
//            showToast("输入活动主题");
            return false;
        }

        String publishTime = getOrderTime();
        if (publishTime == null || TextUtils.isEmpty(publishTime)) {
//            showToast("活动时间不正确,请重新设置时间");
//            mReleaseQuickTime.setHint("点击设置时间");
            return false;
        }

//        if (!Recording) {
////            showToast("请先录音完毕");
//            return false;
//        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RELEASE_QUICK:  //极速发布支付
                if (resultCode == RESULT_OK) {
                    showToast("支付成功");
                    onPaySuccess();
                } else {
                    mReleaseQuickPublish.setText("发布");
                    showToast("支付失败");
                }
                break;

            case REQUEST_CODE_SELECTED_PLACE:  //地址选择
                if (resultCode == RESULT_OK) {
                    onPlaceSelected(
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_NAME),
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_SHOW_ADDRESS),
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_CITY),
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_ADDRESS),
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_LATITUDE),
                            data.getStringExtra(PlaceCityInfoConfig.PLACE_LONGITUDE)
                    );
                }
                break;
            case REQUEST_CODE_QUICK_RELEASE_PRICE:  //自定义金额
                if (resultCode == RESULT_OK) {
                    ALog.i("钱数已经确定:" + data.getStringExtra(EditPayMoneyActivity.PAY_MONEY_BACK_DATA));
                    onMoneyEdit(data.getStringExtra(EditPayMoneyActivity.PAY_MONEY_BACK_DATA));
                }
                break;
            case 1:  //录制视频成功毁掉
                if (resultCode == RESULT_OK) {
                    setVideoState(true);
                    ll_video_delete.setVisibility(View.VISIBLE);
                    tv_vedio.setText("点击播放");
                    iv_video.setImageDrawable(getDrawable(R.drawable.paly_audio_icon));

                    Bundle extras = data.getExtras();
                    time = extras.getString("time");
                }
                break;

        }
    }

    public boolean hasVideo = false; //是不是有录制的视频

    //设置视频文件的状态  @param 是否有视频
    private void setVideoState(boolean isHaveVideo) {
        if (isHaveVideo) {  //有
            hasVideo = true;
        } else {    //没有
            hasVideo = false;
            if (map_file != null) {
                map_file.clear();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hasVideo = false;
        if (map_file != null) {
            map_file.clear();
        }
    }
}
