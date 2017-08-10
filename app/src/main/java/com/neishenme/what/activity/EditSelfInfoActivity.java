package com.neishenme.what.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.AddPhotos;
import com.neishenme.what.bean.EditSelfUploadBackGround;
import com.neishenme.what.bean.EditSelfUploadLogo;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.bean.VideoResponse;
import com.neishenme.what.dialog.ShowAudioDialog;
import com.neishenme.what.dialog.ShowVideoDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnBirthdayTimeSelect;
import com.neishenme.what.nsminterface.UpLoadResponseListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.ImageUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CustDialogBg;
import com.neishenme.what.view.DraggableItemView;
import com.neishenme.what.view.DraggableSquareView;
import com.neishenme.what.view.RadiusImageViewFour;
import com.neishenme.what.view.TagCloudView;
import com.soundcloud.android.crop.Crop;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaozh on 2016/12/20.
 * 编辑个人资料页面
 */
public class EditSelfInfoActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    public static final String BIRTHDAY_TEMLATE = "yyyy年MM月dd日";

    public static final int FLAG_MOVIE = 2;
    public static final int FLAG_FOOD = 3;
    public static final int FLAG_PLACE = 4;

    public static final int FLAG_CHANGE_NICKNAME = 7;
    public static final int FLAG_CHANGE_SIGN = 8;

    private File sdVideoDir = FileUtil.getSDVideoDir();     //获取视频的文件夹
    private File file;

    private MediaPlayer mediaPlayer;    //播放器

    private TextView mEditSelfBack;

    private DraggableSquareView mEditSelfPhotoManage;

    private LinearLayout mLoadingProcress;
    private ImageView mIvProgressBar;
    private TextView mTvProgressBar;

    private RelativeLayout mEditSelfBaseUsername;
    private TextView mEditSelfUsername;
    private RelativeLayout mEditSelfBaseBirthday;
    private TextView mEditSelfBirthday;
    private RelativeLayout mEditSelfBaseSign;
    private TextView mEditSelfSign;
    private RelativeLayout mEditSelfBaseBackground;
    private ImageView mEditSelfBackground;

    private ImageView mEditSelfVoice;
    private ImageView mEditSelfVoiceDelete;
    private RadiusImageViewFour mEditSelfVideo;
    private ImageView mEditSelfVideoDelete;
    private ImageView mEditSelfVideoPlay;

    private RelativeLayout mEditSelfUpdateMusic;
    private TagCloudView mEditSelfTagCloudMusic;
    private TextView mEditSelfMusicNull;
    private RelativeLayout mEditSelfUpdateFood;
    private TagCloudView mEditSelfTagCloudFood;
    private TextView mEditSelfFoodNull;
    private RelativeLayout mEditSelfUpdatePlace;
    private TagCloudView mEditSelfTagCloudPlace;
    private TextView mEditSelfPlaceNull;

    private String singerStr, foodStr, travelStr;
    private List<UserDetailResponse.DataBean.InterestsBean> interestsEntityList;
    private String extraMovie = "";
    private String extraFood = "";
    private String extraTravel = "";
    private List<String> imgs;

    private int imageStatus;
    private boolean isModify;

    private ShowVideoDialog mShowVideoDialog;   //声音的弹窗
    private ShowAudioDialog mShowAudioDialog;   //视频的弹窗

    //标记是否有音视频以及播放器是否就绪
    public boolean hasVideo = false, hasAudio = false;

    private String videoUrl;
    private String VideoThumb;
    private String audioUrl;
    private int oriention; //照片角度

    private ArrayList<DraggableItemView> mDraggableItemViews = new ArrayList<>();
    private UserDetailResponse.DataBean.UserBean userInfo;  //用户信息,抽取出来可修改

    CustDialogBg dialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            loadingWaitHide();
            if (msg.what == 2) {
                for (int i = 0; i < 9; i++) {
                    ALog.i("第" + i + "个图片的tag为" + mEditSelfPhotoManage.getChildAt(i).getTag() + "");
                    if (null == mEditSelfPhotoManage.getChildAt(i).getTag() && !isHeaderPic()) {
                        mEditSelfPhotoManage.getChildAt(i).setTag(Integer.valueOf(msg.getData().getString("photoTag")));
                        ALog.d("重置第" + i + "个图片的tag" + msg.getData().getString("photoTag"));
                        break;
                    }
                }
                if (isHeaderPic()) {
                    showToastSuccess("更换成功");
                } else {
                    showToastSuccess("修改成功");
                }
            }
            if (msg.what == -2) {
                if (isHeaderPic()) {
                    showToastError("更换失败");
                } else {
                    showToastError("修改失败");
                }
            }
            if (msg.what == 3) {
                showToastSuccess("更换成功");
            }
            if (msg.what == -3) {
                showToastError("更换失败");
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_edit_self_info;
    }

    @Override
    protected void initView() {
        mEditSelfBack = (TextView) findViewById(R.id.edit_self_back);

        mEditSelfPhotoManage = (DraggableSquareView) findViewById(R.id.edit_self_photo_manage);

        mLoadingProcress = (LinearLayout) findViewById(R.id.loading_procress);
        mIvProgressBar = (ImageView) findViewById(R.id.iv_progress_bar);
        mTvProgressBar = (TextView) findViewById(R.id.tv_progress_bar);

        mEditSelfBaseUsername = (RelativeLayout) findViewById(R.id.edit_self_base_username);
        mEditSelfUsername = (TextView) findViewById(R.id.edit_self_username);
        mEditSelfBaseBirthday = (RelativeLayout) findViewById(R.id.edit_self_base_birthday);
        mEditSelfBirthday = (TextView) findViewById(R.id.edit_self_birthday);
        mEditSelfBaseSign = (RelativeLayout) findViewById(R.id.edit_self_base_sign);
        mEditSelfSign = (TextView) findViewById(R.id.edit_self_sign);
        mEditSelfBaseBackground = (RelativeLayout) findViewById(R.id.edit_self_base_background);
        mEditSelfBackground = (ImageView) findViewById(R.id.edit_self_background);

        mEditSelfVoice = (ImageView) findViewById(R.id.edit_self_voice);
        mEditSelfVoiceDelete = (ImageView) findViewById(R.id.edit_self_voice_delete);
        mEditSelfVideo = (RadiusImageViewFour) findViewById(R.id.edit_self_video);
        mEditSelfVideoDelete = (ImageView) findViewById(R.id.edit_self_video_delete);
        mEditSelfVideoPlay = (ImageView) findViewById(R.id.edit_self_video_play);

        mEditSelfUpdateMusic = (RelativeLayout) findViewById(R.id.edit_self_update_music);
        mEditSelfTagCloudMusic = (TagCloudView) findViewById(R.id.edit_self_tag_cloud_music);
        mEditSelfMusicNull = (TextView) findViewById(R.id.edit_self_music_null);
        mEditSelfUpdateFood = (RelativeLayout) findViewById(R.id.edit_self_update_food);
        mEditSelfTagCloudFood = (TagCloudView) findViewById(R.id.edit_self_tag_cloud_food);
        mEditSelfFoodNull = (TextView) findViewById(R.id.edit_self_food_null);
        mEditSelfUpdatePlace = (RelativeLayout) findViewById(R.id.edit_self_update_place);
        mEditSelfTagCloudPlace = (TagCloudView) findViewById(R.id.edit_self_tag_cloud_place);
        mEditSelfPlaceNull = (TextView) findViewById(R.id.edit_self_place_null);
    }

    @Override
    protected void initListener() {
        mEditSelfBack.setOnClickListener(this);

        mEditSelfBaseUsername.setOnClickListener(this);
        mEditSelfBaseBirthday.setOnClickListener(this);
        mEditSelfBaseSign.setOnClickListener(this);
        mEditSelfBaseBackground.setOnClickListener(this);

        mEditSelfVoice.setOnClickListener(this);
        mEditSelfVoiceDelete.setOnClickListener(this);
        mEditSelfVideo.setOnClickListener(this);
        mEditSelfVideoDelete.setOnClickListener(this);

        mEditSelfUpdateMusic.setOnClickListener(this);
        mEditSelfUpdateFood.setOnClickListener(this);
        mEditSelfUpdatePlace.setOnClickListener(this);
        initImageLoader(this);
    }

    @Override
    protected void initData() {
        getUserDetail();

        initMediaPlayer();
        file = new File(sdVideoDir, "myVideo.mp4");

        for (int i = 0; i < 9; i++) {
            mDraggableItemViews.add((DraggableItemView) mEditSelfPhotoManage.getChildAt(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_self_back:   //返回
                upBaseInfo();
                finish();
                break;

            case R.id.edit_self_base_username:  //编辑昵称
                ActivityUtils.startActivityForResultString(this, EditMyInfoActivity.class, "nickname", null, FLAG_CHANGE_NICKNAME);
                break;
            case R.id.edit_self_base_birthday:  //编辑生日
                editBirthday();
                break;
            case R.id.edit_self_base_sign:  //编辑签名
                ActivityUtils.startActivityForResultString(this, EditMyInfoActivity.class, "sign", null, FLAG_CHANGE_SIGN);
                break;
            case R.id.edit_self_base_background:    //更换背景
                dialog = new CustDialogBg(EditSelfInfoActivity.this);
                dialog.setClickListener(this);
                dialog.show();
                break;

            //点击添加背景
            case R.id.pick_image:
                Crop.pickBgImage(EditSelfInfoActivity.this);
                break;
            case R.id.capture_image:
                Crop.pickBgImageCapture(EditSelfInfoActivity.this);
                break;

            case R.id.edit_self_voice: //点击音频
                if (hasAudio) {
                    playOrStopMedia();  //有音频则根据此时的情况开始播放或者停止播放
                } else {    //没有说明此时显示的是添加声音
                    showVoicePopWindow();
                }
                break;

            case R.id.edit_self_video: //点击视频
//                if (hasVideo) {
//                    showVideoPopWindow();
//                } else {    //如果没有说明点击的是添加视频,去添加界面
//                    Intent intent = new Intent(this, RecordVideoActivity.class);
//                    startActivityForResult(intent, 1);
//                }
                break;

            case R.id.edit_self_voice_delete:   //删除音频
                deleteAudio();
                break;
            case R.id.edit_self_video_delete:
                deleteVideo();
                break;

            //点击各个添加条目
            case R.id.edit_self_update_music:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "电影", extraMovie.toString(), FLAG_MOVIE);
                break;
            case R.id.edit_self_update_food:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "美食", extraFood.toString(), FLAG_FOOD);
                break;
            case R.id.edit_self_update_place:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "旅途", extraTravel.toString(), FLAG_PLACE);
                break;
            default:
                break;
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mEditSelfVoice.setImageResource(R.drawable.user_detail_voice_play);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    private void getUserDetail() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", NSMTypeUtils.getMyUserId());
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_USER_DETAIL, params, UserDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL, this).setTag(this);
    }

    public void pickImage(int imageStatus, boolean isModify) {
        this.imageStatus = imageStatus;
        this.isModify = isModify;
        Crop.pickImage(this);
    }

    public void captureImage(int imageStatus, boolean isModify) {
        this.imageStatus = imageStatus;
        this.isModify = isModify;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Crop.captureImage(this);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    private void upBaseInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("name", mEditSelfUsername.getText().toString().trim());
        params.put("birthday", String.valueOf(DateUtils.formatToLong(mEditSelfBirthday.getText().toString().trim(), BIRTHDAY_TEMLATE)));
        params.put("sign", mEditSelfSign.getText().toString().trim());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_UPDATE_INFO, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_UPDATE_INFO, this, false).setTag(this);
    }

    public void editBirthday() {
        long time;
        final String birthdayTime = mEditSelfBirthday.getText().toString().trim();
        time = DateUtils.formatToLong(birthdayTime, BIRTHDAY_TEMLATE);
        if (0 >= time) {
            time = DateUtils.formatToLong("1995年10月10日", BIRTHDAY_TEMLATE);
        }
        TimeUtils.setBirthdayTime(this, time, new OnBirthdayTimeSelect() {
            @Override
            public void onTimeSelect(long time) {
                mEditSelfBirthday.setText(DateUtils.formatDate(time, BIRTHDAY_TEMLATE));
            }
        });
        App.USEREDIT.putString("birthday", String.valueOf(time)).commit();
    }

    //上传图片显示等待
    private void loadingWaitShow() {
        mLoadingProcress.setVisibility(View.VISIBLE);
        mTvProgressBar.setText("照片上传中。。。");
        mIvProgressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_loading));
    }

    //上传完毕关闭等待
    private void loadingWaitHide() {
        mIvProgressBar.clearAnimation();
        mLoadingProcress.setVisibility(View.GONE);
    }

    private void playOrStopMedia() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {  //如果播放则停止播放
            try {
                mediaPlayer.stop();
                mEditSelfVoice.setImageResource(R.drawable.user_detail_voice_play);
            } catch (Exception e) {
            }
        } else {
            try {
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        mEditSelfVoice.setImageResource(R.drawable.pause_x3);
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    private void showVoicePopWindow() {
        mShowAudioDialog = new ShowAudioDialog(this);
        mShowAudioDialog.show();
    }

    private void showVideoPopWindow() {
        mShowVideoDialog = new ShowVideoDialog(this, videoUrl, VideoThumb);
        mShowVideoDialog.show();
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        //获取到该界面的所有数据
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL
                && response instanceof UserDetailResponse) {
            UserDetailResponse userDetailResponse = (UserDetailResponse) response;
            if (1 != userDetailResponse.getCode()) {
                showToastInfo(userDetailResponse.getMessage());
                return;
            }
            //分发用户信息界面数据
            userInfo = userDetailResponse.getData().getUser();
            disPathUserData(userInfo);

            interestsEntityList = userDetailResponse.getData().getInterests();
            if (interestsEntityList != null) {
                disPathInterestData(interestsEntityList);
            }

            //分发用户照片信息数据
            List<UserDetailResponse.DataBean.PhotosBean> photosEntityList = userDetailResponse.getData().getPhotos();
            imgs = new ArrayList<>();
            for (int i = 0; i < photosEntityList.size(); i++) {
                imgs.add(photosEntityList.get(i).getThumbnails());
                mEditSelfPhotoManage.fillItemImage(i, photosEntityList.get(i).getThumbnails(), false);
                if (null != String.valueOf(photosEntityList.get(i).getId()) && i < 9) {
                    mEditSelfPhotoManage.getChildAt(i).setTag(photosEntityList.get(i).getId());
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_UPDATE_INFO
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                App.USEREDIT.putString("signature", mEditSelfSign.getText().toString().trim()).commit();
                App.USEREDIT.putString("name", mEditSelfUsername.getText().toString().trim()).commit();
            } else {
                showToastError("更新信息失败");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_VIDEO
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("删除成功");
                setVideoState(false);
                if (file.exists() && file.length() > 0) {
                    file.delete();
                }
            } else {
                showToastError("删除出现了问题,请重试");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_AUDIO
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("删除成功");
                setVoiceState(false);
                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (null != mediaPlayer) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            } else {
                showToastError("删除出现了问题,请重试");
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void disPathUserData(UserDetailResponse.DataBean.UserBean user) {
        String userName = user.getName();
        if (!NSMTypeUtils.isEmpty(userName)) {
            mEditSelfUsername.setText(userName);
        }
        mEditSelfBirthday.setText(DateUtils.formatDate(user.getBirthday(), BIRTHDAY_TEMLATE));
        mEditSelfSign.setText(user.getSign());

        String background = user.getBackground();
        if (!TextUtils.isEmpty(background)) {
            HttpLoader.getImageLoader().get(background,
                    ImageLoader.getImageListener(mEditSelfBackground, R.drawable.bg_self, R.drawable.bg_self));
        }

        //音频视频
        videoUrl = user.getVideoFile();
        VideoThumb = user.getVideoThumb();

//        if (!TextUtils.isEmpty(videoUrl)) {
//            if (!TextUtils.isEmpty(VideoThumb))
//                HttpLoader.getImageLoader().get(VideoThumb,
//                        ImageLoader.getImageListener(mEditSelfVideo, R.color.no_color, R.color.no_color));
//            setVideoState(true);
//        } else {
//            setVideoState(false);
//        }

        audioUrl = user.getAudioFile();
        if (user.getAudioDuration() == 0 || TextUtils.isEmpty(audioUrl)) {
            setVoiceState(false);
        } else {
            setVoiceState(true);
            try {
                if (mediaPlayer == null) {
                    initMediaPlayer();
                }
                mediaPlayer.setDataSource(audioUrl);
            } catch (IOException e) {
                e.printStackTrace();
                setVoiceState(false);
            }
        }
    }

    public void audioSuccess(String audioUrl, int audioDuration) {
        userInfo.setAudioDuration(audioDuration);
        userInfo.setAudioFile(audioUrl);
        initMediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            setVoiceState(true);
        } catch (IOException e) {
            e.printStackTrace();
            setVoiceState(false);
        }
    }

    public List<String> stringToList(String str) {
        String[] arrayStr;
        arrayStr = str.split(";");
        return java.util.Arrays.asList(arrayStr);
    }

    private void disPathInterestData(List<UserDetailResponse.DataBean.InterestsBean> interestlist) {
        for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
            if ("movie_name".equals(interest.getKey())) {
                singerStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraMovie = interest.getContent();
                    List<String> interestedData = stringToList(extraMovie);
                    mEditSelfTagCloudMusic.setTags(interestedData);
                } else {
                    mEditSelfMusicNull.setText(singerStr);
                }
            }
            if ("food_name".equals(interest.getKey())) {
                foodStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraFood = interest.getContent();
                    List<String> interestedData = stringToList(extraFood);
                    mEditSelfTagCloudFood.setTags(interestedData);
                } else {
                    mEditSelfFoodNull.setText(foodStr);
                }
            }
            if ("trip_name".equals(interest.getKey())) {
                travelStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraTravel = interest.getContent();
                    List<String> interestedData = stringToList(extraTravel);
                    mEditSelfTagCloudPlace.setTags(interestedData);
                } else {
                    mEditSelfPlaceNull.setText(travelStr);
                }
            }
        }
    }

    public void deleteVideo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_DELETE_VIDEO, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_DELETE_VIDEO, this).setTag(this);
    }

    public void deleteAudio() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_DELETE_AUDIO, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_DELETE_AUDIO, this).setTag(this);
    }

    //设置音频文件的状态  @param 是否有音频
    private void setVoiceState(boolean isHaveVoice) {
        if (isHaveVoice) {  //有
            hasAudio = true;
            mEditSelfVoiceDelete.setVisibility(View.VISIBLE);
            mEditSelfVoice.setImageResource(R.drawable.user_detail_voice_play);
        } else {    //没有
            hasAudio = false;
            mEditSelfVoiceDelete.setVisibility(View.INVISIBLE);
            mEditSelfVoice.setImageResource(R.drawable.add_voice2x);
        }
    }

    //设置视频文件的状态  @param 是否有视频
    private void setVideoState(boolean isHaveVideo) {
        if (isHaveVideo) {  //有
            hasVideo = true;
            mEditSelfVideoDelete.setVisibility(View.VISIBLE);
            mEditSelfVideoPlay.setVisibility(View.VISIBLE);
        } else {    //没有
            hasVideo = false;
            mEditSelfVideoDelete.setVisibility(View.INVISIBLE);
            mEditSelfVideoPlay.setVisibility(View.INVISIBLE);
            mEditSelfVideo.setImageResource(R.drawable.add_video);
        }
    }

    //重置播放器的状态
    private void resetMediaPlayer() {
        try {
            mediaPlayer.stop();
            mEditSelfVoice.setImageResource(R.drawable.user_detail_voice_play);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final HashMap<String, String> map_param = new HashMap<>();
                    map_param.put("token", NSMTypeUtils.getMyToken());
                    final HashMap<String, String> map_file = new HashMap<>();
                    map_file.put("video", file.getAbsolutePath());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpLoader.httpRequestMp4(ConstantsWhatNSM.URL_ADD_VIDEO, map_param, map_file, file.getAbsolutePath(), new UpLoadResponseListener() {
                                @Override
                                public void onResponseSuccess(String requestString) {
                                    VideoResponse response;
                                    Gson gson = new Gson();
                                    response = gson.fromJson(requestString, VideoResponse.class);
                                    if (response.getCode() == 1) {
                                        videoUrl = response.getData().getVideoFile();
                                        VideoThumb = response.getData().getVideoThumb();
                                        App.USEREDIT.putString("videoUrl", videoUrl).commit();
                                        App.USEREDIT.putString("videoThumb", VideoThumb).commit();
                                        showToastSuccess("上传成功");
                                        HttpLoader.getImageLoader().get(VideoThumb,
                                                ImageLoader.getImageListener(mEditSelfVideo, R.drawable.picture_moren, R.drawable.picture_moren));
                                        setVideoState(true);
                                    } else {
                                        showToastInfo(response.getMessage());
                                    }
                                }

                                @Override
                                public void onResponseError(Exception e) {
                                    showToastError("上传失败");
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                }
                            });
                        }
                    }).start();
                }
                break;

            //照片,图片添加**************************************************************
            case Crop.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    beginCrop(data.getData(), Crop.REQUEST_CROP);
                }
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data, false);
                break;

            case Crop.REQUEST_BG_PICK:
                if (resultCode == RESULT_OK) {
                    beginCrop(data.getData(), Crop.REQUEST_Bg_CROP);
                }
                break;
            case Crop.REQUEST_Bg_CROP:
                handleBgCrop(resultCode, data);
                break;
            case Crop.REQUEST_BG_CAPTURE:
                if (resultCode == RESULT_OK) {
                    beginCrop(Uri.fromFile(Crop.bgFile), Crop.REQUEST_Bg_CROP);
                }
                break;
            case Crop.REQUEST_CAPTURE:
                if (resultCode == RESULT_OK) {
                    beginCrop(Uri.fromFile(Crop.file), Crop.REQUEST_CAPTURE_CROP);
                }
                break;
            case Crop.REQUEST_CAPTURE_CROP:
                handleCrop(resultCode, data, true);
                break;
            //照片,图片添加完毕*********************************************************

            //三种爱好 ****************************************************************
            case FLAG_MOVIE:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("movie_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            mEditSelfTagCloudMusic.removeAllViews();
                            mEditSelfMusicNull.setText(singerStr);
                            extraMovie = "";
                        } else {
                            mEditSelfMusicNull.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            mEditSelfTagCloudMusic.setTags(interestedData);
                            extraMovie = str;
                        }
                    }
                }
                break;
            case FLAG_FOOD:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("food_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            mEditSelfTagCloudFood.removeAllViews();
                            mEditSelfFoodNull.setText(foodStr);
                            extraFood = "";
                        } else {
                            mEditSelfFoodNull.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            mEditSelfTagCloudFood.setTags(interestedData);
                            extraFood = str;
                        }
                    }
                }
                break;
            case FLAG_PLACE:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("trip_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            mEditSelfTagCloudPlace.removeAllViews();
                            mEditSelfPlaceNull.setText(travelStr);
                            extraTravel = "";
                        } else {
                            mEditSelfPlaceNull.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            mEditSelfTagCloudPlace.setTags(interestedData);
                            extraTravel = str;
                        }
                    }
                }
                break;
            //三种兴趣完毕 **********************************************************

            //修改姓名和签名 *******************************************************
            case FLAG_CHANGE_NICKNAME:
                if (resultCode == RESULT_OK) {
                    String upNick = data.getStringExtra("upNick");
                    mEditSelfUsername.setText(upNick);
                }
                break;
            case FLAG_CHANGE_SIGN:
                if (resultCode == RESULT_OK) {
                    String upSign = data.getStringExtra("upSign");
                    mEditSelfSign.setText(upSign);
                }
                break;
            //姓名签名修改完毕 ******************************************************
            default:
                break;
        }
    }

    private void beginCrop(Uri source, int resultCode) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        String imagePath = FileUtil.getRealFilePath(this, source);
        if (null == imagePath) {
            imagePath = source.getPath();
        }
        oriention = ImageUtils.getBitmapDegree(imagePath);
        Crop.of(source, destination).asSquare(oriention).start(this, resultCode);
    }

    private void handleBgCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();

            final HashMap<String, String> map_file = new HashMap<>();
            map_file.put("background", imagePath.replace("file://", ""));

            loadingWaitShow();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", NSMTypeUtils.getMyToken());
                    httpRequestPhoto(ConstantsWhatNSM.URL_UPLOAD_BACKGROUND_PHOTOS, map, map_file, oriention, false);
                }
            }).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            showToastError("上传失败,请重试");
        }
    }

    private void handleCrop(int resultCode, Intent result, final boolean isCapture) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();

            mEditSelfPhotoManage.fillItemImage(imageStatus, imagePath, isModify, oriention);

            final HashMap<String, String> map_file = new HashMap<>();
            if (isHeaderPic()) {
                map_file.put("logo", imagePath.replace("file://", ""));
            } else {
                map_file.put("photo", imagePath.replace("file://", ""));
            }

            loadingWaitShow();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", NSMTypeUtils.getMyToken());
                    if (isCapture) {
                        map.put("photoState", "0");
                    } else {
                        map.put("photoState", "1");
                    }
                    if (isHeaderPic()) {
                        httpRequestPhoto(ConstantsWhatNSM.URL_UPDATE_LOGO, map, map_file, oriention, true);
                    } else {
                        httpRequestPhoto(ConstantsWhatNSM.URL_UPLOAD_PHOTOS, map, map_file, oriention, false);
                    }
                }
            }).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            showToastError("获取图片失败");
        }
    }

    private boolean isHeaderPic() {
        return imageStatus == DraggableItemView.STATUS_LEFT_TOP;
    }

    public String httpRequestPhoto(final String urlStr, Map<String, String> textMap, Map<String, String> fileMap, int oriention, final boolean isHeaderPic) {
        return HttpLoader.upLoadPic(urlStr, textMap, fileMap, oriention, new UpLoadResponseListener() {
            @Override
            public void onResponseSuccess(String requestString) {
                Gson gson = new Gson();
                if (isHeaderPic) {
                    EditSelfUploadLogo response = gson.fromJson(requestString, EditSelfUploadLogo.class);
                    if (response.getCode() == 1) {
                        App.USEREDIT.putString("logo", response.getData().getLogo()).commit();
                        App.USEREDIT.putString("thumbnailslogo", response.getData().getLogo()).commit();
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(-2);
                    }
                } else {
                    if (urlStr.equals(ConstantsWhatNSM.URL_UPLOAD_BACKGROUND_PHOTOS)) {
                        EditSelfUploadBackGround response = gson.fromJson(requestString, EditSelfUploadBackGround.class);
                        if (response.getCode() == 1) {
                            String background = response.getData().getBackground();
                            App.USEREDIT.putString("background", background).commit();
                            HttpLoader.getImageLoader().get(background,
                                    ImageLoader.getImageListener(mEditSelfBackground, R.drawable.bg_self, R.drawable.bg_self));
                            handler.sendEmptyMessage(3);
                        } else {
                            handler.sendEmptyMessage(-3);
                        }
                    } else {
                        AddPhotos response = gson.fromJson(requestString, AddPhotos.class);
                        if (response.getCode() == 1) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("photoTag", response.getData().getPhoto().getId() + "");
                            msg.setData(bundle);
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(-2);
                        }
                    }
                }
            }

            @Override
            public void onResponseError(Exception e) {
                handler.sendEmptyMessage(-2);
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStop() {
        if (hasAudio) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                resetMediaPlayer();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
