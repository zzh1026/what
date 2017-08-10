package com.neishenme.what.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.neishenme.what.activity.PayOrderActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.FillOrderTitleResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RestaurantDetailResponse;
import com.neishenme.what.bean.SendDataPublicResponse;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.NSMTypeUtils;
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
 * Created by Administrator on 2016/5/11.
 *  旧的发布活动的弹窗,已弃用
 */
@Deprecated
public class FillOrderDialog extends BaseDialog implements MyDatePickerDialog.OnConfirmListeners, HttpLoader.ResponseListener {
    private EditText mRestTitleTv;
    private ImageView mRestTitleMoreIv;
    private TextView mRestTimeTv;
    private RadioGroup mRestPaytypeRg;
    private RadioGroup mRestTargettypeRg;
    private ImageView mRestVoiceIv;
    private TextView mRestPublishedTv;
    private TextView radioTitle;
    private ImageView radioIcon;
    private TextView tvClickRadio;
    private ImageView ivDeleteAudio;
    private TextView tvDeleteAudio;
    private TextView tvTime;
    private View mFillOrderLine1;

    private TimerTask task;
    private Timer timer;

    private boolean should = true; //是否应该将箭头返回去

    private boolean Recording = false;
    private boolean playing = false;

    private int second = 5;
    //private String path = FileUtil.getSDAudioDir().getAbsolutePath() + File.separator;
    //private String path = "/audio";
    //private String fileName = "order.mp3";

    private MediaRecorder myRecorder = null;
    private MediaPlayer mPlayer = new MediaPlayer();
    private boolean hasRecorded = false;
    FileUtil fileUtils = new FileUtil();

    String orderTime = null;

    private int serviceId;
    private Activity context;
    private PublishOrderActivity mActivity;
    private String time;
    private RestaurantDetailResponse.DataEntity.ServiceEntity serviceEntity;

    private List<String> titleList;
    private TitleAdapter mAdapter;
    private PopupWindow pw;
    RotateAnimation mUpRotateAnimation;
    RotateAnimation mDownRotateAnimation;

    File file;

    // public interface
    public FillOrderDialog(Activity context, int serviceId, String time,
                           RestaurantDetailResponse.DataEntity.ServiceEntity serviceEntity) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_fill_order);
        this.context = context;
        this.serviceId = serviceId;
        mActivity = (PublishOrderActivity) context;
        this.time = time;
        this.serviceEntity = serviceEntity;
        initView();
        initListener();
        initData();
    }

    private void initData() {
        //File file = new File(fileUtils.getSDCARDPATH() + path + fileName);
        file = new File(FileUtil.getSDAudioDir(), "mypublish.aac");
        if (file.exists() && file.isFile() && file.getName().contains(".aac")) {
            file.delete();
        }
    }

    @Override
    public void dismiss() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        if (myRecorder != null) {
            //myRecorder.stop();
            myRecorder.release();
            myRecorder = null;
        }
        super.dismiss();
    }

    public void initView() {
        mRestTitleTv = (EditText) findViewById(R.id.rest_title_tv);
        mRestTitleMoreIv = (ImageView) findViewById(R.id.rest_title_more_iv);
        mRestTimeTv = (TextView) findViewById(R.id.rest_time_tv);

        mRestPaytypeRg = (RadioGroup) findViewById(R.id.rest_paytype_rg);
        mRestTargettypeRg = (RadioGroup) findViewById(R.id.rest_targettype_rg);

        mRestPublishedTv = (TextView) findViewById(R.id.rest_published_tv);
        radioTitle = (TextView) findViewById(R.id.radio_title);

        radioIcon = (ImageView) findViewById(R.id.radio_icon);
        tvClickRadio = (TextView) findViewById(R.id.tv_click_radio);

        ivDeleteAudio = (ImageView) findViewById(R.id.iv_delete_audio);
        tvDeleteAudio = (TextView) findViewById(R.id.tv_delete_audio);

        tvTime = (TextView) findViewById(R.id.tv_time);
        mFillOrderLine1 = (View) findViewById(R.id.fill_order_line_1);

    }

    private void initListener() {
        mRestTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTime();
            }
        });
        mRestTitleMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {

                } else {
                    showTitleList();
                    mRestTitleMoreIv.setImageResource(R.drawable.icon_more2xdown);
                }
            }
        });

        mRestPublishedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryPublicInvite();
            }
        });
        tvDeleteAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing == true) {
                    endPlay();
                }
                //File file = fileUtils.creatSDFile(path + fileName);
                if (file.exists()) {
                    file.delete();
                }
                tvDeleteAudio.setVisibility(View.INVISIBLE);
                ivDeleteAudio.setVisibility(View.INVISIBLE);
                radioIcon.setVisibility(View.VISIBLE);
                radioIcon.setImageResource(R.drawable.icon_microphone2x);
                tvClickRadio.setText("点击录制");
                hasRecorded = false;
            }
        });

        ivDeleteAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing == true) {
                    endPlay();
                }
                //File file = fileUtils.creatSDFile(path + fileName);
                if (file.exists()) {
                    file.delete();
                }
                tvDeleteAudio.setVisibility(View.INVISIBLE);
                ivDeleteAudio.setVisibility(View.INVISIBLE);
                radioIcon.setVisibility(View.VISIBLE);
                radioIcon.setImageResource(R.drawable.icon_microphone2x);
                tvClickRadio.setText("点击录制");
                hasRecorded = false;
            }
        });

        radioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    endPlay();
                    tvClickRadio.setText("点击播放");
                    radioIcon.setImageResource(R.drawable.paly_audio_icon);
                    return;
                }

                if (hasRecorded) {
                    startPlay();
                    playing = true;
                    tvClickRadio.setText("点击停止");
                    radioIcon.setImageResource(R.drawable.stop_audio_icon);
                    return;
                }
            }
        });


        tvClickRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (hasPlay) {
//                    File file = fileUtils.creatSDFile(path + fileName);
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    mTvClickRadio.setText("点击录制");
//                    hasPlay = false;
//                    return;
//                }
//
                if (playing) {
                    endPlay();
                    tvClickRadio.setText("点击播放");
                    radioIcon.setImageResource(R.drawable.paly_audio_icon);
                    return;
                }

                if (hasRecorded) {
                    startPlay();
                    playing = true;
                    tvClickRadio.setText("点击停止");
                    radioIcon.setImageResource(R.drawable.stop_audio_icon);
                    return;
                }
                if (Recording) {//录音中。。
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    hasRecorded = true;
                    tvClickRadio.setText("点击播放");
                    radioIcon.setImageResource(R.drawable.paly_audio_icon);
                    tvDeleteAudio.setVisibility(View.VISIBLE);
                    ivDeleteAudio.setVisibility(View.VISIBLE);
                    Recording = false;
                    endRecord();
                    tvTime.setVisibility(View.INVISIBLE);
                    return;
                }


                //开始录音
                mActivity.showToastInfo("开始录音...");
                second = 5;

                myRecorder = new MediaRecorder();
                myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                //myRecorder.setOutputFile(fileUtils.getSDCARDPATH() + path + fileName);
                myRecorder.setOutputFile(file.getAbsolutePath());
                tvTime.setVisibility(View.VISIBLE);
                try {
                    myRecorder.prepare();
                } catch (IOException e) {
                    mActivity.showToastError("录音失败！");
                    e.printStackTrace();
                }
                myRecorder.start();
                timer = new Timer();
                task = new MyTimerTask();
                timer.schedule(task, 0, 1000);
                Recording = true;
                tvClickRadio.setText("点击停止");
            }
        });
    }

    private void showTitleList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("payType", getPayType());
        params.put("gender", App.USERSP.getString("gender", "0"));
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_DATA_RES_TITLE, params, FillOrderTitleResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_RES_TITLE, this).setTag(this);
    }

    private void editTime() {
        MyDatePickerDialog myDAtePickerDialog = new MyDatePickerDialog(context, time, "时间");
        myDAtePickerDialog.setOnConfirmListeners(new MyDatePickerDialog.OnConfirmListeners() {
            @Override
            public void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex) {
                Log.d("timeSelect", monthSelectedIndex + "  " + daySelectedIndex);
                String hour = time.split(",")[monthSelectedIndex];
                String minute = daySelectedIndex * 15 + "";
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                mRestTimeTv.setText(hour + ":" + minute);
                orderTime = hour + ":" + minute;
            }
        });
        myDAtePickerDialog.show();
    }

    public void startPlay() {
        mActivity.showToastInfo("开始播放");

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playing = false;
                radioIcon.setImageResource(R.drawable.paly_audio_icon);
                tvClickRadio.setText("点击播放");
            }
        });
        try {
//            mPlayer.setDataSource(fileUtils.getSDCARDPATH() + path + fileName);
            mPlayer.setDataSource(file.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
            mActivity.showToastError("播放失败！");
        }

    }

    public void endRecord() {
        try {
            if (null != myRecorder) {
                myRecorder.stop();
                myRecorder.release();
                myRecorder = null;
                Recording = false;
            }

        } catch (Exception e) {

        }

    }

    public void endPlay() {
        if (null != mPlayer) {
            mPlayer.release();
            mPlayer = null;
            playing = false;
            tvClickRadio.setText("点击播放");
            radioIcon.setImageResource(R.drawable.paly_audio_icon);
        }

    }

    @Override
    public void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex) {
        Log.d("timeSelect", monthSelectedIndex + "   " + daySelectedIndex);
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
        }
    }

    private void upAnimation() {
        if (mUpRotateAnimation == null) {
            mUpRotateAnimation = new RotateAnimation(0, 90, 0.5f, 0.5f);
            mUpRotateAnimation.setDuration(200);
            mUpRotateAnimation.setFillAfter(true);
        }
        mRestTitleMoreIv.startAnimation(mUpRotateAnimation);
    }

    private void downAnimation() {
        if (mDownRotateAnimation == null) {
            mDownRotateAnimation = new RotateAnimation(0, -90, 0.5f, 0.5f);
            mDownRotateAnimation.setDuration(200);
            mDownRotateAnimation.setFillAfter(true);
        }
        mRestTitleMoreIv.startAnimation(mUpRotateAnimation);
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    public void showTitleListContent() {
        // 初始化ListView控件和里边的数据
        ListView mListView = initListView();
        // 弹出一个PopupWindow的窗体, 把ListView作为其内容部分显示.

        pw = new PopupWindow(mListView, mFillOrderLine1.getWidth() / 4 * 3, getTotalHeightofListView(mListView));

        // 设置可以使用焦点
        pw.setFocusable(true);

        // 设置popupwindow点击外部可以被关闭
        pw.setOutsideTouchable(true);
        // 设置一个popupWindow的背景
        pw.setBackgroundDrawable(new BitmapDrawable());

        // 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
        pw.showAsDropDown(mFillOrderLine1, -10, 0, Gravity.END);
        should = true;

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (should) {
                    mRestTitleMoreIv.setImageResource(R.drawable.icon_more2x);
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
        ListView mListView = new ListView(mActivity);
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
                    mRestTitleTv.setText(titleList.get(position));
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

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            context.runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    second--;
                    tvTime.setText(second + "'" + "秒");
                    if (second == 0) {
                        if (Recording) {//录音中。。
                            timer.cancel();
                            timer.purge();
                            timer = null;
                            hasRecorded = true;
                            tvClickRadio.setText("点击播放");
                            radioIcon.setImageResource(R.drawable.paly_audio_icon);
                            tvDeleteAudio.setVisibility(View.VISIBLE);
                            ivDeleteAudio.setVisibility(View.VISIBLE);
                            Recording = false;
                            endRecord();
                            mActivity.showToastInfo("已达到录制时间上限");
                            tvTime.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    }

    private void tryPublicInvite() {
        String token = NSMTypeUtils.getMyToken();
        if (TextUtils.isEmpty(token)) {
            mActivity.showToastInfo("您尚未登录,请先登录");
            ActivityUtils.startActivity(mActivity, LoginActivity.class);
            return;
        }

        String title = mRestTitleTv.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            mActivity.showToastInfo("请标明主题");
            return;
        }

        if (title.length() > 15) {
            mActivity.showToastInfo("主题过长,请修改一下吧");
            mRestTitleTv.setText("");
            return;
        }

        if (getOrderTime() == null) {
            mActivity.showToastInfo("邀请时间至少要在一个小时之后,请重新设置时间");
            mRestTimeTv.setText("点击设置时间");
            return;
        }

        if (Recording) {
            mActivity.showToastInfo("请先录音完毕");
            return;
        }

        //File file = new File(fileUtils.getSDCARDPATH() + path + fileName);
        if (file.exists() && file.isFile() && file.getName().contains(".aac")) {

            mRestPublishedTv.setClickable(false);
            mRestPublishedTv.setText("正在发布");

            final HashMap<String, String> params = new HashMap<>();
            params.put("token", token);
            params.put("serviceId", String.valueOf(serviceId));
            params.put("title", title);
            params.put("target", getTarget());
            params.put("payType", getPayType());
            params.put("time", getOrderTime());
            params.put("audioDuration", String.valueOf(5 - second));
            HashMap<String, String> params2 = null;
            if (file.exists() && file.isFile() && file.length() > 0) {
                params2 = new HashMap<>();
                //params2.put("audio", fileUtils.getSDCARDPATH() + path + fileName);
                params2.put("audio", file.getAbsolutePath());
            }

//        HttpLoader.get(ConstantsWhatNSM.URL_SEND_DATA_SEND, params, SendDataPublicResponse.class,
//                ConstantsWhatNSM.REQUEST_CODE_SEND_DATA_SEND, this).setTag(this);
            final HashMap<String, String> finalParams = params2;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpRequest(ConstantsWhatNSM.URL_SEND_DATA_SEND, params, finalParams);
                }
            }).start();
        } else {
            mActivity.showToastInfo("请录下声音再发起活动吧");
            return;
        }
    }

    /**
     * @return
     */
    private String getOrderTime() {
        if (orderTime != null) {
            String orderT = DateUtils.formatDate(System.currentTimeMillis()) + " " + orderTime;
            long order = DateUtils.formatToLong(orderT, "yyyy-MM-dd HH:mm");
            if ((order - System.currentTimeMillis()) >= (0)) {
                return String.valueOf(order);
            } else {
                return null;
            }
        }
        return null;
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
                    String inputName = (String) entry.getKey();
                    ALog.d("inputname" + inputName);
                    String inputValue = (String) entry.getValue();
                    ALog.d("inputvalue" + inputValue);

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
                    String inputName = (String) entry.getKey();

                    String inputValue = (String) entry.getValue();

                    ALog.d("inputValue" + inputValue);


                    File file = new File(inputValue);
                    String filename = file.getName();
                    // MagicMatch match = Magic.getMagicMatch(file, false,
                    // true);
                    // String contentType = match.getMimeType();

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
            ALog.d("res" + res);
            final SendDataPublicResponse response;
            Gson gson = new Gson();
            response = gson.fromJson(res, SendDataPublicResponse.class);
            reader.close();
            reader = null;
            if (response.getCode() == 1) {
                if (response.getData().getTrade().getPrice() > 0 && response.getData().getInvite().getNewstatus() != 50) {
                    SendDataPublicResponse.DataEntity.TradeEntity tradeBean = response.getData().getTrade();
//                    TrideBean trideBean = new TrideBean(serviceEntity.getName(), serviceEntity.getLogo(), response.getData().getInvite().getTitle(),
//                            response.getData().getInvite().getTime(), response.getData().getTrade().getPrice(), response.getData().getInvite().getPayType()
//                            , response.getData().getInvite().getServiceId(), response.getData().getTrade().getTradeNum());
////                    PayOrderActivity.startPayOrderActForResult(context, trideBean);
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.finish();
                        }
                    });
                } else {
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.showToastSuccess("发布成功,请耐心等待");
                            mRestPublishedTv.setText("已发布");
                            mRestPublishedTv.setClickable(false);
                            dismiss();
                            //ActivityUtils.startActivityAndFinish(context, MainActivity.class);
                            ActivityUtils.startActivityForData(context,
                                    InviteInviterDetailActivity.class, String.valueOf(response.getData().getInvite().getId()));
                        }
                    });
                }
            } else {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        PublishOrderActivity context = (PublishOrderActivity) FillOrderDialog.this.context;
                        context.showToastInfo(response.getMessage());
                        mRestPublishedTv.setText("发布邀请");
                        mRestPublishedTv.setClickable(true);
                    }
                });
            }

        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mRestPublishedTv.setClickable(true);
                    mRestPublishedTv.setText("发布邀请");
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

    class TitleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titleList.size() + 1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.item_fill_order_title, null);
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


}
