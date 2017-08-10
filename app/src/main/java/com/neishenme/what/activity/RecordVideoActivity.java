package com.neishenme.what.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neishenme.what.R;
import com.neishenme.what.utils.FileUtil;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 这个事 录制视频界面  ,现在是发布活动的时候如果想要视频就在这里使用
 */
public class RecordVideoActivity extends Activity implements View.OnClickListener {
    private static final int HANDLE_UPDATE_TIME_PROGRESS = 31;
    private static final int HANDLE_UPDATE_TIME_SHOW = 32;

    private File sdVideoDir = FileUtil.getSDVideoDir();
    private File file;

    private MediaRecorder mediaRecorder; //录制类
    private SurfaceView sv_view;
    private SurfaceHolder mSurfaceHolder;
    private ProgressBar progressBar;

    private int limitTime = 5000;
    private long startTime = Long.MAX_VALUE;
    private int cameraId = -1,
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK,
            cameraFront = Camera.CameraInfo.CAMERA_FACING_FRONT;// 默认为后置摄像头

    private ImageButton btn_VideoStart, btn_VideoStop, btn_VideoCancel, btn_Switch, btn_flash;
    private TextView tv_time;
    private boolean SwitchCarmera = false;
    private boolean firstLauch = true;

    Camera.CameraInfo cameraInfo;
    Camera camera;
    Camera.Parameters parameters;
    private Camera.AutoFocusCallback mAutoFocusCallback;

    private boolean flashIsOpen = false;
    private boolean switchCameraDrudation = false;//保证换摄像头不会被连续点击
    private boolean isRecord = false;
    private boolean firstRecord = true;
    private boolean clickLimit = false;
    private boolean isRecording = false;
    private int screenWidth;
    private int screenHeight;
    private String m;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        initVar();
        initView();

        //DisplayMetrics dm = new DisplayMetrics();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        //获取相机信息
        sv_view.getLayoutParams().width = screenWidth;
        sv_view.getLayoutParams().height = screenHeight;//64/48;

        btn_VideoStop.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MediaPrepareTask().execute(null, null, null);
        mHandler.sendEmptyMessageDelayed(HANDLE_UPDATE_TIME_SHOW, 1500);
    }

    private boolean prepareVideoRecorder() {
        if (file.exists()) {
            file.delete();
        }
        mediaRecorder = new MediaRecorder();
        List<Camera.Size> sizeList = null;
        {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                    int numberOfCameras = Camera.getNumberOfCameras();
                    cameraInfo = new Camera.CameraInfo();
                    for (int i = 0; i < numberOfCameras; i++) {
                        Camera.getCameraInfo(i, cameraInfo);
                        if (SwitchCarmera == false) {
                            if (cameraInfo.facing == cameraFacing) {
                                cameraId = i;
                                if (firstLauch == false) {
                                    camera.stopPreview();
                                    camera.release();
                                    camera = null;
                                }
                                firstLauch = false;
                            }
                        } else {
                            if (cameraInfo.facing == cameraFront) {
                                cameraId = i;
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                                firstLauch = false;
                            }
                        }

                    }

                }
                if (cameraId >= 0) {
                    try {
                        camera = Camera.open(cameraId);
                    } catch (Exception e) {
                        openDefaultCamera();
                    }
                } else {
                    openDefaultCamera();
                }
                try {
                    camera.autoFocus(mAutoFocusCallback);
                } catch (Exception e) {

                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (camera != null) {
                parameters = camera.getParameters();
                if (flashIsOpen) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flashIsOpen = false;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    flashIsOpen = true;
                }
                sizeList = parameters.getSupportedVideoSizes();
                if (null == sizeList) {
                    sizeList = parameters.getSupportedPreviewSizes();
                }
                setDispaly(parameters, camera);
                if (SwitchCarmera == true) {
                    parameters.set("rotation", 180);
                }

                camera.unlock();
                mediaRecorder.setCamera(camera);
            }
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            int rotation = 0;
            if (SwitchCarmera == false) {
                mediaRecorder.setOrientationHint(90);//��Ƶ��ת90�
            } else {
                mediaRecorder.setOrientationHint(270);
            }
        }
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//CAMERA
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//MPEG_4//DEFAULT
//       mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_QVGA)) {
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_LOW)) {
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        }
            /*
             * OutputFormat.DEFAULT
			 * AudioEncoder DEFAULT AAC_ELD(phone ok) ACC(phone ok) HE_AAC(GALAXY not ok
			 * VideoEncoder.MPEG_4_SP
			 */
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//MPEG_4_SP
        // 获取自己手机合适的尺寸
//            List<Size> sizeList = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : sizeList) {
            ALog.d("SupportedVideoSize :" + size.width + ", " + size.height);
            if (size.width < 500) {
                mediaRecorder.setVideoSize(size.width, size.height);
                break;
            }
        }

        //Camera.Size previewSize = getPropPreviewSize(parameters.getSupportedPreviewSizes(), previewRate, 1280);
        Point bestCameraResolution = getBestCameraResolution(parameters, new Point(screenWidth, screenHeight));
        parameters.setPreviewSize(bestCameraResolution.x, bestCameraResolution.y);
//			mediaRecorder.setVideoSize(640, 480);
//			mediaRecorder.setVideoSize(1280, 720);
        mediaRecorder.setMaxDuration(limitTime);

        // some device not available
//			mediaRecorder.setVideoFrameRate(24);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if (null != mediaRecorder) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                if (camera != null) {
                    camera.release();
                    camera = null;
                }
                isRecording = false;
                btn_VideoStart.setEnabled(true);
                btn_VideoStop.setEnabled(false);
                Toast.makeText(RecordVideoActivity.this, "Record Error", Toast.LENGTH_LONG).show();
            }
        });
        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void openDefaultCamera() {
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Toast.makeText(this, "录制视频需要权限,请在权限管理中打开该权限重试", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mediaRecorder.start();
                clickLimit = false;
                startTime = System.currentTimeMillis();
                if (isRecord) {
                    mHandler.sendEmptyMessage(HANDLE_UPDATE_TIME_PROGRESS);
                }
                isRecording = true;

            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            switchCameraDrudation = false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                RecordVideoActivity.this.finish();
            }
            // inform the user that recording has started

        }
    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            //nexus 5x会出问题,这里进行了修改
            int cameraDisplayOrientation = getCameraDisplayOrientation(this, cameraId);
            setDisplayOrientation(camera, cameraDisplayOrientation);
            //setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    public static int getCameraDisplayOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private void initVar() {
        file = new File(sdVideoDir, "myVideo.mp4");
        mAutoFocusCallback = new Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if (success) {
                    camera.setOneShotPreviewCallback(null);
                }

            }
        };
    }

    private void initView() {
        sv_view = (SurfaceView) findViewById(R.id.sv_view);

        btn_VideoStart = (ImageButton) findViewById(R.id.btn_VideoStart);

        btn_VideoStop = (ImageButton) findViewById(R.id.btn_VideoStop);
        btn_VideoCancel = (ImageButton) findViewById(R.id.btn_VideoCancel);

        btn_Switch = (ImageButton) findViewById(R.id.btn_swith);
        btn_flash = (ImageButton) findViewById(R.id.btn_flash);

        progressBar = (ProgressBar) findViewById(R.id.progress_time);
        tv_time = (TextView) findViewById(R.id.tv_time);

        mSurfaceHolder = sv_view.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btn_VideoStart.setEnabled(false);
        btn_VideoStart.setClickable(false);

        btn_VideoStart.setOnClickListener(this);
        btn_VideoStop.setOnClickListener(this);
        btn_VideoCancel.setOnClickListener(this);

        btn_Switch.setOnClickListener(this);
        btn_flash.setOnClickListener(this);
        btn_flash.setVisibility(View.INVISIBLE);
    }

    protected void start() {
        startTime = System.currentTimeMillis();
        mHandler.sendEmptyMessage(HANDLE_UPDATE_TIME_PROGRESS);
        btn_VideoStart.setEnabled(false);
        btn_VideoStop.setEnabled(true);
        isRecording = true;
        Toast.makeText(RecordVideoActivity.this, "Start Record", Toast.LENGTH_SHORT).show();
    }

    private void stop() {
        if (isRecording) {
            if (null != mediaRecorder) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
            if (camera != null) {
                camera.stopPreview();
                camera.lock();
                camera.release();
                camera = null;
            }
            isRecording = false;
            btn_VideoStart.setEnabled(true);
//			btn_VideoStop.setEnabled(false);
            if (null != file && file.exists()) {
                try {
                    ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_ROTATE_270);
                    exifInterface.saveAttributes();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRecording) {
            mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
            if (null != mediaRecorder) {
                if (isRecord) {
                    mediaRecorder.stop();
                }
                mediaRecorder.release();
                mediaRecorder = null;
            }
            if (camera != null) {
                camera.lock();
                camera.release();
                camera = null;
            }
            ALog.d("destory..--------------------------------");
        }
    }

    @Override
    protected void onDestroy() {
        if (isRecording) {
            mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
            if (null != mediaRecorder) {
                if (isRecord) {
                    mediaRecorder.stop();
                }
                mediaRecorder.release();
                mediaRecorder = null;
            }

            if (camera != null) {
                camera.lock();
                camera.release();
                camera = null;
            }
            ALog.d("destory..--------------------------------");
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_VideoStart:
                if (clickLimit) {
                    return;
                }

                clickLimit = true;
                mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);

                if (isRecording) {
                    if (isRecord) {
                        mediaRecorder.stop();
                        isRecord = false;
                        btn_VideoStart.setVisibility(View.INVISIBLE);
                        btn_VideoCancel.setVisibility(View.VISIBLE);
                        btn_VideoStop.setVisibility(View.VISIBLE);
                        if (tv_time.getText().equals("00:05") || tv_time.getText().equals("00:04")) {
                            MyToast.showConterToast(this, "时间过短");
                            clickLimit = false;
                            if (isRecording) {
                                mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
                                if (null != mediaRecorder) {
                                    mediaRecorder.release();
                                    mediaRecorder = null;
                                }
                                if (null != file && file.exists()) {
                                    file.delete();
                                }
                                if (camera != null) {
                                    camera.release();
                                    camera = null;
                                }
                            }
                            setResult(RESULT_CANCELED);
                            finish();
                        } else {
                            MyToast.showConterToast(this, "录制完成");
                            clickLimit = false;
                        }
                    } else {
                        if (firstRecord) {
                            mediaRecorder.stop();
                        }
                        file.delete();
                        firstRecord = false;
                        new MediaPrepareTask().execute(null, null, null);
                        btn_VideoStart.setImageResource(R.drawable.video_record_start_pressed);
                        btn_VideoStart.setClickable(false);
                        btn_VideoStart.setEnabled(true);
                        btn_VideoStart.setVisibility(View.VISIBLE);
                        btn_VideoCancel.setVisibility(View.INVISIBLE);
                        btn_VideoStop.setVisibility(View.INVISIBLE);
                        btn_Switch.setVisibility(View.INVISIBLE);
                        btn_Switch.setClickable(false);
                        isRecord = true;
                        mHandler.sendEmptyMessageDelayed(HANDLE_UPDATE_TIME_SHOW, 1500);
                    }
                } else {
                    new MediaPrepareTask().execute(null, null, null);
                }

                break;
            case R.id.btn_VideoStop: {
                if (!tv_time.getText().equals("00:05") && !tv_time.getText().equals("00:04")) {
                    mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
                    if (isRecord) {
                        stop();
                    }
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();

                    bundle.putString("time",time+"");
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    MyToast.showConterToast(this, "录制时间过短,请重新录制");
                }

            }
            break;

            case R.id.btn_VideoCancel: {
                if (isRecording) {
                    mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
                    if (null != mediaRecorder) {
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                    if (camera != null) {
                        camera.release();
                        camera = null;
                    }
                }
                setResult(RESULT_CANCELED);
                finish();
            }

            break;
            case R.id.btn_swith:
                if (switchCameraDrudation) {
                    return;
                }
                btn_VideoStart.setClickable(false);
                switchCameraDrudation = true;
                mHandler.removeMessages(HANDLE_UPDATE_TIME_PROGRESS);
                SwitchCarmera = !SwitchCarmera;
                if (null != mediaRecorder) {
                    try {
                        mediaRecorder.stop();
                    } catch (Exception e) {

                    }
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                ObjectAnimator rotation = ObjectAnimator.ofFloat(btn_Switch, "rotation", 0f, 360f).setDuration(1000);
                rotation.start();
                rotation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_VideoStart.setClickable(true);
                        super.onAnimationEnd(animation);
                    }
                });
                new MediaPrepareTask().execute(null, null, null);
                break;
            case R.id.btn_flash:
//                if (flashIsOpen) {
//                    if (null != camera.getParameters()) {
//                        parameters = camera.getParameters();
//                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                        camera.setParameters(parameters);
//                        flashIsOpen = false;
//                    }
//
//                } else {
//                    if (null != camera.getParameters()) {
//                        parameters = camera.getParameters();
//                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                        camera.setParameters(parameters);
//                        flashIsOpen = true;
//                    }
//                }
                break;

            default:
                break;
        }

    }

    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            // clear recorder configuration
            mediaRecorder.reset();
            // release the recorder object
            mediaRecorder.release();
            mediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            try {
                camera.lock();
            } catch (Exception e) {
                ALog.e("wrong");
            }
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_UPDATE_TIME_PROGRESS: {
                    long timeSpan = System.currentTimeMillis() - startTime;
                    m = null;
                    time = (int) (timeSpan / 1000);
                    if (time < 10) {
                        m = "00:0" + (5 - time);
                    } else {
                        m = "00:" + time + "";
                    }
                    tv_time.setText(m);
                    int percent = (int) (timeSpan * 100 / limitTime);
                    progressBar.setProgress(percent);
                    if (percent >= 100) {
                        mediaRecorder.stop();
                        isRecord = false;
                        btn_VideoStart.setVisibility(View.INVISIBLE);
                        btn_VideoCancel.setVisibility(View.VISIBLE);
                        btn_VideoStop.setVisibility(View.VISIBLE);
                        clickLimit = false;
                        Toast.makeText(RecordVideoActivity.this, "录制结束", Toast.LENGTH_SHORT).show();
                    } else {
                        mHandler.sendEmptyMessageDelayed(HANDLE_UPDATE_TIME_PROGRESS, 500);
                    }
                }
                break;

                case HANDLE_UPDATE_TIME_SHOW:
                    btn_VideoStart.setEnabled(true);
                    btn_VideoStart.setClickable(true);
                    break;

                default:
                    break;
            }
        }

    };
}
