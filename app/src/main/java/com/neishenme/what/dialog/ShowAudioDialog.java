package com.neishenme.what.dialog;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditSelfInfoActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.AudioResponse;
import com.neishenme.what.utils.ClsOscilloscope;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;

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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ShowAudioDialog extends BaseDialog implements View.OnClickListener {
    private EditSelfInfoActivity context;

    private static final int RECORD_COMPLETE_READY = 1; //准备
    private static final int RECORD_COMPLETE_START = 2; //录制
    private static final int RECORD_COMPLETE_END = 3;   //结束

    private int mCurrentRecordComplete;

    //private Paint mPaint;
    private MediaPlayer mPlayer;
    private MediaRecorder myRecorder;  //声音录制类
    private boolean isPlaying = false; //是否完毕录制音频

    private int BASE = 600;

    private LinearLayout mLoadingProcress;
    private ImageView mIvProgressBar;
    private TextView mTvProgressBar;

    private TextView mTvTime;
    private ImageView mIvVoiceWave;

    private ImageView mBtnPause;
    private Button mBtnVoiceDelete;
    private Button mBtnVoiceComplete;

    private Timer timer;
    private TimerTask task;

    private int second = 5;
    private String audioFile;
    private int audioDuration;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                context.showToastSuccess("上传成功");
                dismiss();
                context.audioSuccess(audioFile, audioDuration);
            }
            if (msg.what == -1) {
                context.showToastError("上传失败");
                dismiss();
            }
        }
    };

    static final int xMax = 16;//X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
    static final int xMin = 8;//X轴缩小比例最小值
    static final int yMax = 10;//Y轴缩小比例最大值
    static final int yMin = 1;//Y轴缩小比例最小值

    private File nsmSdAudioDir;
    private File recorderFile;
    //private File recorderFile = new File(FileUtil.getSDCARDPATH(), "myVoice.mp3");

    public ShowAudioDialog(EditSelfInfoActivity context) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.pop_voice);
        this.context = context;
        initView();
        initListener();
        initData();
    }

    public void initView() {
        mLoadingProcress = (LinearLayout) findViewById(R.id.loading_procress);
        mIvProgressBar = (ImageView) findViewById(R.id.iv_progress_bar);
        mTvProgressBar = (TextView) findViewById(R.id.tv_progress_bar);

        mTvTime = (TextView) findViewById(R.id.tv_time);
        mIvVoiceWave = (ImageView) findViewById(R.id.iv_voice_wave);

        mBtnPause = (ImageView) findViewById(R.id.btn_pause);

        mBtnVoiceDelete = (Button) findViewById(R.id.btn_voice_delete);
        mBtnVoiceComplete = (Button) findViewById(R.id.btn_voice_complete);
    }

    private void initListener() {
        mBtnPause.setOnClickListener(this);

        mBtnVoiceDelete.setOnClickListener(this);

        mBtnVoiceComplete.setOnClickListener(this);
    }

    private void initData() {
        nsmSdAudioDir = FileUtil.getSDAudioDir();
        recorderFile = new File(nsmSdAudioDir, "myVoice.aac");

        //mPaint = new Paint();
        //mPaint.setColor(Color.GREEN);// 画笔为绿色
        //mPaint.setStrokeWidth(1);// 设置画笔粗细

        ClsOscilloscope clsOscilloscope = new ClsOscilloscope();
        clsOscilloscope.initOscilloscope(xMax / 2, yMax / 2, mIvVoiceWave.getHeight() / 2);

        mCurrentRecordComplete = RECORD_COMPLETE_READY;

        mBtnVoiceDelete.setVisibility(View.INVISIBLE);
        mBtnVoiceComplete.setVisibility(View.INVISIBLE);

        //endRecord();
    }

    private void initRecorder() {
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(recorderFile.getAbsolutePath());
    }

    public void endRecord() {
        if (null == myRecorder) {
            return;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        try {
            if (myRecorder != null) {
                myRecorder.stop();
                myRecorder.release();
                myRecorder = null;
            }
            if (null != mIvVoiceWave)
                mIvVoiceWave.setImageResource(R.drawable.record_icon);
        } catch (Exception e) {

        }
    }

    private void updateMicStatus() {
        if (myRecorder != null && mIvVoiceWave != null) {
            // int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
            int ratio = myRecorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            switch (db / 4) {
                case 0:
                    mIvVoiceWave.setImageResource(R.drawable.wave0);
                    break;
                case 1:
                    mIvVoiceWave.setImageResource(R.drawable.wave1);
                    break;
                case 2:
                    mIvVoiceWave.setImageResource(R.drawable.wave2);
                    break;
                case 3:
                    mIvVoiceWave.setImageResource(R.drawable.wave3);
                    break;
                case 4:
                    mIvVoiceWave.setImageResource(R.drawable.wave4);
                    break;
                case 5:
                    mIvVoiceWave.setImageResource(R.drawable.wave5);
                    break;
                case 6:
                    mIvVoiceWave.setImageResource(R.drawable.wave6);
                    break;
                default:
                    mIvVoiceWave.setImageResource(R.drawable.wave7);
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateMicStatus();
                }
            }, 100);
                     /*
           * if (db > 1) { vuSize = (int) (20 * Math.log10(db)); Log.i("mic_",
             * "麦克风的音量的大小：" + vuSize); } else Log.i("mic_", "麦克风的音量的大小：" + 0);
             */
        }
    }

    class MyTimeTask extends TimerTask {

        @Override
        public void run() {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    second--;
                    if (second == 0) {
                        mTvTime.setText("最多录制时长5秒，录制结束");
                        endRecord();
                        mCurrentRecordComplete = RECORD_COMPLETE_END;
                        mBtnPause.setImageResource(R.drawable.voice_play);
                        mBtnVoiceDelete.setVisibility(View.VISIBLE);
                        mBtnVoiceComplete.setVisibility(View.VISIBLE);
                    } else {
                        mTvTime.setText("当前剩余录制时长" + second + "''");
                    }

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                switch (mCurrentRecordComplete) {
                    case RECORD_COMPLETE_READY:
                        startRecordComplete();
                        break;
                    case RECORD_COMPLETE_START:
                        endRecordComplete();
                        break;
                    case RECORD_COMPLETE_END:
                        if (isPlaying) {
                            endPlay();
                            mBtnPause.setImageResource(R.drawable.voice_play);
                        } else {
                            startPlay();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_voice_delete:
                if (recorderFile.exists()) {
                    recorderFile.delete();
                }
//                mBtnPause.setImageResource(R.drawable.voice_start);
//                mCurrentRecordComplete = RECORD_COMPLETE_READY;
//                mBtnVoiceDelete.setVisibility(View.INVISIBLE);
//                mBtnVoiceComplete.setVisibility(View.INVISIBLE);
                dismiss();
                break;
            case R.id.btn_voice_complete:
                if (recorderFile.exists()) {
                    final Map params = new HashMap();
                    params.put("token", NSMTypeUtils.getMyToken());
                    params.put("duration", (5 - second) + "");
                    final HashMap<String, String> file_map = new HashMap<>();
                    file_map.put("audio", recorderFile.getAbsolutePath());
                    mBtnVoiceComplete.setVisibility(View.INVISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequest(ConstantsWhatNSM.URL_UPLOAD_AUDIO, params, file_map);
                        }
                    }).start();
                } else {
                    context.showToastInfo("该音频被损坏,无法上传,请重试");
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    private void startRecordComplete() {
        mCurrentRecordComplete = RECORD_COMPLETE_START;
        mBtnPause.setImageResource(R.drawable.voice_pause);
        initRecorder();
        timer = new Timer();
        task = new MyTimeTask();

        second = 5;
        mTvTime.setText("当前剩余录制时长5''");

        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IOException e) {
            dismiss();
            e.printStackTrace();
        }

        updateMicStatus();
        timer.schedule(task, 0, 1000);
    }

    private void endRecordComplete() {
        mCurrentRecordComplete = RECORD_COMPLETE_END;
        mBtnPause.setImageResource(R.drawable.voice_play);
        endRecord();
        timer.cancel();
        timer.purge();
        if ((5 - second) < 2) {
            mTvTime.setText("你的录音低于2秒，请重新录制");
            if (recorderFile.exists()) {
                recorderFile.delete();
            }
            mCurrentRecordComplete = RECORD_COMPLETE_READY;
            mBtnPause.setImageResource(R.drawable.voice_start);
        } else {
            mBtnVoiceComplete.setVisibility(View.VISIBLE);
            mBtnVoiceDelete.setVisibility(View.VISIBLE);
        }
    }

    public void endPlay() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            try {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                isPlaying = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startPlay() {
        isPlaying = true;
        mBtnPause.setImageResource(R.drawable.voice_pause);

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBtnPause.setImageResource(R.drawable.voice_play);
                isPlaying = false;
            }
        });

        try {
            mPlayer.setDataSource(recorderFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            mBtnPause.setImageResource(R.drawable.voice_play);
            context.showToastError("播放失败!");
            isPlaying = false;
        }
    }

    /**
     * 上传录音
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @return
     */
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

            Gson gson = new Gson();
            ALog.d("res" + res);
            AudioResponse response;
            response = gson.fromJson(res, AudioResponse.class);

            audioFile = response.getData().getAudioFile();
            audioDuration = 5 - second;
            App.USEREDIT.putString("audioUrl", audioFile).commit();
            App.USEREDIT.putString("audioDuration", audioDuration + "").commit();

            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            reader.close();
            reader = null;

        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            Message msg = new Message();
            msg.what = -1;
            handler.sendMessage(msg);
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
}
