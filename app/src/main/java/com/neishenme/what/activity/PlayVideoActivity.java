package com.neishenme.what.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.LightnessController;
import com.neishenme.what.utils.VolumnController;
import com.neishenme.what.view.FullScreenVideoView;

import org.seny.android.utils.ALog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放视频界面 , 现在的进入方式是 在邀请详情中,如果发单的时候录制了视频, 那么这里就可以点击进行播放
 */
public class PlayVideoActivity extends BaseActivity implements View.OnClickListener {

    private File sdVideoDir = FileUtil.getSDVideoDir();
    private File file;

    // 自定义VideoView
    private FullScreenVideoView mVideo;

    // 头部View
    private View mTopView;

    // 底部View
    private View mBottomView;
    // 视频播放拖动条
    private SeekBar mSeekBar;
    private ImageView mPlay;
    private TextView mPlayTime;
    private TextView mDurationTime;

    // 音频管理器
    private AudioManager mAudioManager;

    // 屏幕宽高
    private float width;
    private float height;

    // 视频播放时间
    private int playTime;

    private String videoUrl = null;
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;

    // 声音调节Toast
    private VolumnController volumnController;

    // 原始屏幕亮度
    private int orginalLight;

    private TextView tv_return;

    private boolean playNative;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    playVideo();
                    break;
                case -1:
                    showToastError("获取视频失败");
                    break;
            }
        }
    };

    @Override
    protected int initContentView() {

        return R.layout.activity_play_video;
    }


    @Override
    protected void initView() {

        volumnController = new VolumnController(this);
        mVideo = (FullScreenVideoView) findViewById(R.id.videoview);
        mPlayTime = (TextView) findViewById(R.id.play_time);
        mDurationTime = (TextView) findViewById(R.id.total_time);
        mPlay = (ImageView) findViewById(R.id.play_btn);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mTopView = findViewById(R.id.top_layout);
        mBottomView = findViewById(R.id.bottom_layout);
        tv_return = (TextView) findViewById(R.id.tv_return);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        width = DensityUtil.getWidthInPx(this);
        height = DensityUtil.getHeightInPx(this);
        threshold = DensityUtil.dip2px(this, 18);
        orginalLight = LightnessController.getLightness(this);
    }

    @Override
    protected void initListener() {
        mPlay.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        tv_return.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            if (intent.getExtras().getString(MediaStore.EXTRA_OUTPUT).equals("1")) {
                file = new File(sdVideoDir, "myVideo.mp4");
                videoUrl = App.USERSP.getString("videoUrl", null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new HttpDownloader().download(videoUrl, file.getAbsolutePath());
                    }
                }).start();
            }
            //播放他人的视频
            if (intent.getExtras().getString(MediaStore.EXTRA_OUTPUT).equals("2")) {
                videoUrl = intent.getExtras().getString("videoSity", null);
                file = new File(sdVideoDir, "otherVideo.mp4");
                if (file.exists() && file.length() > 0) {
                    file.delete();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //确定网络地址和下载地址 如果下载地址有 就直接播放
                        new HttpDownloader().download(videoUrl, file.getAbsolutePath());
                    }
                }).start();
            }

        }

        ALog.d("videourl" + videoUrl);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = DensityUtil.getWidthInPx(this);
            width = DensityUtil.getHeightInPx(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = DensityUtil.getWidthInPx(this);
            height = DensityUtil.getHeightInPx(this);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LightnessController.setLightness(this, orginalLight);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                int time = progress * mVideo.getDuration() / 100;
                mVideo.seekTo(time);
            }
        }
    };

    private void backward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void forward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void volumeDown(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void volumeUp(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void lightDown(float delatY) {
        int down = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY) {
        int up = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        LightnessController.setLightness(this, transformatLight);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mVideo.getCurrentPosition() > 0) {
                        mPlayTime.setText(formatTime(mVideo.getCurrentPosition()));
                        int progress = mVideo.getCurrentPosition() * 100 / mVideo.getDuration();
                        mSeekBar.setProgress(progress);
                        if (mVideo.getCurrentPosition() >= mVideo.getDuration() - 50) {
                            mPlayTime.setText("00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(mVideo.getBufferPercentage());
                    } else {
                        mPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };

    private void playVideo() {
//        String s;
//        if (getIntent().getExtras().getString(MediaStore.EXTRA_OUTPUT).equals("1")) {
//            s = new FileUtil().getSDCARDPATH() + "myVideo" + File.separator + videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
//        } else {
//            s = new FileUtil().getSDCARDPATH() + "otherVideo" + File.separator + videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
//        }
        String s = file.getAbsolutePath();


        mVideo.setVideoURI(Uri.parse(s));
        mVideo.requestFocus();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.setVideoWidth(mp.getVideoWidth());
                mVideo.setVideoHeight(mp.getVideoHeight());

                mVideo.start();
                if (playTime != 0) {
                    mVideo.seekTo(playTime);
                }

                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatTime(mVideo.getDuration()));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 200);
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlay.setImageResource(R.drawable.ta_play_audio);
                mPlayTime.setText("00:00");
                mSeekBar.setProgress(0);
            }
        });
        mVideo.setOnTouchListener(mTouchListener);
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold) {
                        if (absDeltaX < absDeltaY) {
                            isAdjustAudio = true;
                        } else {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
                        isAdjustAudio = false;
                    } else {
                        return true;
                    }
                    if (isAdjustAudio) {
                        if (x < width / 2) {
                            if (deltaY > 0) {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0) {
                                lightUp(absDeltaY);
                            }
                        } else {
                            if (deltaY > 0) {
                                volumeDown(absDeltaY);
                            } else if (deltaY < 0) {
                                volumeUp(absDeltaY);
                            }
                        }

                    } else {
                        if (deltaX > 0) {
                            forward(absDeltaX);
                        } else if (deltaX < 0) {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold
                            || Math.abs(y - startY) > threshold) {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick) {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_btn:
                if (mVideo.isPlaying()) {
                    mVideo.pause();
                    mPlay.setImageResource(R.drawable.ta_play_audio);
                } else {
                    mVideo.start();
                    mPlay.setImageResource(R.drawable.ta_pause_audio);
                }
                break;
            case R.id.tv_return:
                finish();
                break;
            default:
                break;
        }
    }

    private void showOrHide() {
        if (mTopView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_bottom);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    class HttpDownloader {
        private URL url = null;

        /**
         * 下载文本文件
         *
         * @param urlStr
         * @return
         */
        public String download(String urlStr) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader buffer = null;
            try {
                buffer = new BufferedReader(new InputStreamReader(
                        getInputStreamFromUrl(urlStr)));
                //一行一行的读取
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        /**
         * @param urlStr   文件所在的网络地址
         * @param path     存储的目录
         * @param fileName 存放的文件名
         * @return 状态
         */
        public String download(String urlStr, String path, String fileName) {
            InputStream inputStream = null;
            try {
                FileUtil fileUtils = new FileUtil();
                //判断文件是否已存在
                if (fileUtils.isFileExist(path + fileName)) {

                    handler.sendEmptyMessage(0);
                    return "fileExist";
                } else {

                    inputStream = getInputStreamFromUrl(urlStr);
                    File resultFile = fileUtils.writeToSDCard(path, fileName,
                            inputStream);
                    //如果resultFile==null则下载失败
                    if (resultFile == null) {
                        return "downloadError";
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            } catch (Exception e) {
                //如果异常了，也下载失败
                e.printStackTrace();
                return "downloadError";
            } finally {
                try {
                    //别忘了关闭流
                    if (null != inputStream) {
                        inputStream.close();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "downloadOk";

        }

        public void download(String urlStr, String destination) {
            InputStream inputStream = null;
            FileUtil fileUtils = new FileUtil();
            try {
                File file = new File(destination);
                //判断文件是否已存在
                if (file.exists() && file.length() > 0) {
                    handler.sendEmptyMessage(0);
                } else {
                    inputStream = getInputStreamFromUrl(urlStr);
                    File resultFile = fileUtils.writeToDestinaton(destination,
                            inputStream);
                    //如果resultFile==null则下载失败
                    if (resultFile == null) {
                        handler.sendEmptyMessage(-1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            } catch (Exception e) {
                //如果异常了，也下载失败
                e.printStackTrace();
                handler.sendEmptyMessage(-1);
            } finally {
                try {
                    //别忘了关闭流
                    if (null != inputStream) {
                        inputStream.close();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 连接到网络（ 抽取的公共方法）
         *
         * @param urlStr 文件所在的网络地址
         * @return InputStream
         */
        public InputStream getInputStreamFromUrl(String urlStr) {
            ALog.d("下载文件的地址" + urlStr);
            InputStream inputStream = null;
            try {
                // 创建一个URL对象
                url = new URL(urlStr);
                // 根据URL对象创建一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url
                        .openConnection();
                int contentLength = urlConn.getContentLength();
                ALog.d("下载时文件大小" + contentLength);
                // IO流读取数据
                inputStream = urlConn.getInputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }


    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }
}
