package com.neishenme.what.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CustDialogBg;
import com.neishenme.what.view.DraggableItemView;
import com.neishenme.what.view.DraggableSquareView;
import com.neishenme.what.view.TagCloudView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.soundcloud.android.crop.Crop;

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

/**
 * Created by Administrator on 2016/5/17.
 * 就得编辑个人信息界面 ,已弃用, 新的编辑信息界面见 @see{link {@link EditSelfInfoActivity}}
 */
@Deprecated
public class EditSelfInfoOldActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {

    public static final int FLAG_SINGER = 2;
    public static final int FLAG_MOVIE = 3;
    public static final int FLAG_FOOD = 4;
    public static final int FLAG_TRAVEL = 5;
    public static final int FLAG_SPORT = 6;

    public static final int FLAG_CHANGE_NICKNAME = 7;
    public static final int FLAG_CHANGE_SIGN = 8;

    private File sdVideoDir = FileUtil.getSDVideoDir();
    private File file;

    private TextView ivBack;
    private LinearLayout llName, llDate, llSign;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvSign;
    private ImageView ivSelfBg;

    private ImageView ivVoice, ivVideo;
    private ImageView mEditInfoHaveVideoIv;

    private RelativeLayout rlSinger, rlMoive, rlFood, rlTravel, rlSport;
    private String singerStr, movieStr, foodStr, travelStr, sportStr;
    private TextView tvLikeSinger, tvLikeMovie, tvLikeFood, tvTravel, tvSport;
    private List<UserDetailResponse.DataBean.InterestsBean> interestsEntityList;
    private TagCloudView tcvSinger, tcvMovie, tcvFood, tcvTravel, tcvSport;
    private String extraSinger = "";
    private String extraMovie = "";
    private String extraFood = "";
    private String extraTravel = "";
    private String extraSport = "";
    private String birthday;
    private List<String> imgs;

    private DraggableSquareView dragSquare;
    private int imageStatus;
    private boolean isModify;

    private ShowVideoDialog mShowVideoDialog;
    private ShowAudioDialog mShowAudioDialog;

    private ImageView iv_progress_bar, iv_progress_bar_main;
    private TextView tv_progress_bar, tv_progress_bar_main;
    private LinearLayout loading_procress, loading_procress_main;

    public boolean hasVideo = false, hasAudio = false, mediaPlayerIsPrepared = false;
    private boolean audioIsPlay = false;

    private String videoUrl;
    private String VideoThumb;
    private String audioUrl;

    private int oriention; //照片角度

    private ImageView btnDeleteAudio;
    private ImageView btnDeleteVideo;

    private MediaPlayer mediaPlayer;
    private ArrayList<DraggableItemView> DraggableItemViews = new ArrayList<>();
    private UserDetailResponse userDetailResponse;

    CustDialogBg dialog;
    private View.OnClickListener dialogListener;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                iv_progress_bar_main.clearAnimation();
                loading_procress_main.setVisibility(View.GONE);
                for (int i = 0; i < 9; i++) {
                    ALog.i("第" + i + "个图片的tag为" + dragSquare.getChildAt(i).getTag() + "");
                    if (null == dragSquare.getChildAt(i).getTag() && !isHeaderPic()) {
                        dragSquare.getChildAt(i).setTag(Integer.valueOf(msg.getData().getString("photoTag")));
                        ALog.d("重置第" + i + "个图片的tag" + msg.getData().getString("photoTag"));
                        break;
                    }
                }
                if (isHeaderPic()) {
                    showToastSuccess("更换头像成功");
                } else {
                    showToastSuccess("修改成功");
                }
            }
            if (msg.what == -2) {
                iv_progress_bar_main.clearAnimation();
                loading_procress_main.setVisibility(View.GONE);
                if (isHeaderPic()) {
                    showToastError("更换头像失败");
                } else {
                    showToastError("修改失败");
                }
            }
            if (msg.what == 3) {
                iv_progress_bar_main.clearAnimation();
                loading_procress_main.setVisibility(View.GONE);
                showToastSuccess("更换背景成功");
            }
            if (msg.what == -3) {
                iv_progress_bar_main.clearAnimation();
                loading_procress_main.setVisibility(View.GONE);
                showToastError("更换背景失败");
            }
        }
    };


    @Override
    protected int initContentView() {
        return R.layout.activity_edit_self_info_old;
    }


    @Override
    protected void initView() {
        ivBack = (TextView) findViewById(R.id.tv_cancle);


        llName = (LinearLayout) findViewById(R.id.ll_name);
        llDate = (LinearLayout) findViewById(R.id.ll_date);
        llSign = (LinearLayout) findViewById(R.id.ll_sign);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvSign = (TextView) findViewById(R.id.tv_sign);
        ivSelfBg = (ImageView) findViewById(R.id.iv_self_bg);

        rlSinger = (RelativeLayout) findViewById(R.id.rl_singer);
        rlMoive = (RelativeLayout) findViewById(R.id.rl_movie);
        rlFood = (RelativeLayout) findViewById(R.id.rl_food);
        rlTravel = (RelativeLayout) findViewById(R.id.rl_travel);
        rlSport = (RelativeLayout) findViewById(R.id.rl_sport);

        tcvSinger = (TagCloudView) findViewById(R.id.tag_cloud_view_singer);
        tcvMovie = (TagCloudView) findViewById(R.id.tag_cloud_view_movie);
        tcvFood = (TagCloudView) findViewById(R.id.tag_cloud_view_food);
        tcvTravel = (TagCloudView) findViewById(R.id.tag_cloud_view_travel);
        tcvSport = (TagCloudView) findViewById(R.id.tag_cloud_view_sport);
        tvLikeSinger = (TextView) findViewById(R.id.tv_like_music);
        tvLikeMovie = (TextView) findViewById(R.id.tv_like_movie);
        tvLikeFood = (TextView) findViewById(R.id.tv_like_food);
        tvTravel = (TextView) findViewById(R.id.tv_like_travel);
        tvSport = (TextView) findViewById(R.id.tv_like_sport);

        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        ivVideo = (ImageView) findViewById(R.id.iv_video);
        mEditInfoHaveVideoIv = (ImageView) findViewById(R.id.edit_info_have_video_iv);
        btnDeleteAudio = (ImageView) findViewById(R.id.btn_delete_audio);
        btnDeleteVideo = (ImageView) findViewById(R.id.btn_delete_video);

        dragSquare = (DraggableSquareView) findViewById(R.id.drag_square);

        iv_progress_bar_main = (ImageView) findViewById(R.id.iv_progress_bar);
        tv_progress_bar_main = (TextView) findViewById(R.id.tv_progress_bar);
        loading_procress_main = (LinearLayout) findViewById(R.id.loading_procress);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        llName.setOnClickListener(this);
        llDate.setOnClickListener(this);
        llSign.setOnClickListener(this);

        ivVideo.setOnClickListener(this);
        ivVoice.setOnClickListener(this);

        rlSinger.setOnClickListener(this);
        rlMoive.setOnClickListener(this);
        rlFood.setOnClickListener(this);
        rlTravel.setOnClickListener(this);
        rlSport.setOnClickListener(this);

        ivSelfBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CustDialogBg(EditSelfInfoOldActivity.this);
                dialog.setClickListener(dialogListener);
                dialog.show();
            }
        });

        dialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.pick_image) {
                    Crop.pickBgImage(EditSelfInfoOldActivity.this);
                } else {
                    Crop.pickBgImageCapture(EditSelfInfoOldActivity.this);
                }
            }
        };
        initImageLoader(this);
        btnDeleteAudio.setOnClickListener(this);
        btnDeleteVideo.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getUserDetail();

        initMediaPlayer();
        file = new File(sdVideoDir, "myVideo.mp4");

        for (int i = 0; i < 9; i++) {
            DraggableItemViews.add((DraggableItemView) dragSquare.getChildAt(i));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hasAudio = true;
                audioIsPlay = false;
                ivVoice.setImageResource(R.drawable.user_detail_voice_play);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                upBaseInfo();
                //UserDetailActivity.startUserDetailAct(this, Integer.parseInt(NSMTypeUtils.getMyUserId()), false);
                finish();
                break;

            case R.id.ll_name:
                ActivityUtils.startActivityForResultString(this, EditMyInfoActivity.class, "nickname", null, FLAG_CHANGE_NICKNAME);
                break;
            case R.id.ll_date:
                editBirthday();
                break;
            case R.id.ll_sign:
                ActivityUtils.startActivityForResultString(this, EditMyInfoActivity.class, "sign", null, FLAG_CHANGE_SIGN);
                break;
            case R.id.iv_self_bg:
                break;

            //关于视频
            case R.id.iv_video:
                if (hasVideo == true) {
                    showVideoPopWindow();
                } else {
                    Intent intent = new Intent(this, RecordVideoActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;

            //关于音频
            case R.id.iv_voice:
                if (hasAudio) {
                    if (mediaPlayer == null) {
                        return;
                    }

                    if (audioIsPlay) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mediaPlayerIsPrepared = true;
                                }
                            });
                            audioIsPlay = false;
                            ivVoice.setImageResource(R.drawable.user_detail_voice_play);
                        } catch (Exception e) {

                        }
                    } else {
                        if (!mediaPlayerIsPrepared) {
                            return;
                        }
                        try {
                            mediaPlayer.start();
                            audioIsPlay = true;
                            ivVoice.setImageResource(R.drawable.pause_x3);
                        } catch (Exception e) {

                        }
                    }
                } else {    //没有说明此时显示的是添加声音
                    showVoicePopWindow();
                }
                break;

            //删除视频音频
            case R.id.btn_delete_audio:
                deleteAudio();
                break;
            case R.id.btn_delete_video:
                deleteVideo();
                break;

            //点击条目添加
            case R.id.rl_singer:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "歌手", extraSinger.toString(), FLAG_SINGER);
                break;
            case R.id.rl_movie:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "电影", extraMovie.toString(), FLAG_MOVIE);
                break;
            case R.id.rl_food:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "美食", extraFood.toString(), FLAG_FOOD);
                break;
            case R.id.rl_travel:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "旅途", extraTravel.toString(), FLAG_TRAVEL);
                break;
            case R.id.rl_sport:
                ActivityUtils.startActivityForResultString(
                        this, AddHobbyActivity.class, "运动", extraSport.toString(), FLAG_SPORT);
                break;

        }
    }

    private void upBaseInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("name", tvName.getText().toString().trim());
        params.put("birthday", String.valueOf(DateUtils.formatToLong(tvDate.getText().toString().trim(), "yyyy-MM-dd")));
        params.put("sign", tvSign.getText().toString().trim());

        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_UPDATE_INFO, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_UPDATE_INFO, this, false).setTag(this);
    }

    public void editBirthday() {
        long time;
        final String birthdayTime = tvDate.getText().toString().trim();
        time = DateUtils.formatToLong(birthdayTime, "yyyy-MM-dd");
        if (0 >= time) {
            time = DateUtils.formatToLong("1995-10-10", "yyyy-MM-dd");
        }
        TimeUtils.setBirthdayTime(this, time, new OnBirthdayTimeSelect() {
            @Override
            public void onTimeSelect(long time) {
                birthday = DateUtils.formatDate(time);
                tvDate.setText(birthday);
            }
        });
        App.USEREDIT.putString("birthday", String.valueOf(time)).commit();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showVoicePopWindow() {
//        mShowAudioDialog = new ShowAudioDialog(this);
//        mShowAudioDialog.show();
    }

    private void showVideoPopWindow() {
//        mShowVideoDialog = new ShowVideoDialog(this, videoUrl, VideoThumb);
//        mShowVideoDialog.show();
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        //获取到该界面的所有数据
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL
                && response instanceof UserDetailResponse) {
            userDetailResponse = (UserDetailResponse) response;

            if (1 != userDetailResponse.getCode()) {
                showToastInfo(userDetailResponse.getMessage());
                return;
            }

            //分发用户信息界面数据
            disPathUserData(userDetailResponse.getData().getUser());

            interestsEntityList = userDetailResponse.getData().getInterests();
            if (interestsEntityList != null) {
                disPathInterestData(interestsEntityList);
            }

            //分发用户照片信息数据
            List<UserDetailResponse.DataBean.PhotosBean> photosEntityList = userDetailResponse.getData().getPhotos();
            imgs = new ArrayList<>();
            for (int i = 0; i < photosEntityList.size(); i++) {
                imgs.add(photosEntityList.get(i).getThumbnails());
                dragSquare.fillItemImage(i, photosEntityList.get(i).getThumbnails(), false);
                if (null != String.valueOf(photosEntityList.get(i).getId()) && i < 9) {
                    dragSquare.getChildAt(i).setTag(photosEntityList.get(i).getId());
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_PHOTO && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                MyToast.showConterToast(EditSelfInfoOldActivity.this, "删除成功");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_UPLOAD_BACKGROUND_PHOTOS
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("修改成功");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_UPDATE_INFO
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                App.USEREDIT.putString("signature", tvSign.getText().toString().trim()).commit();
                App.USEREDIT.putString("name", tvName.getText().toString().trim()).commit();
            } else {
                showToastError("更新信息失败");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_VIDEO
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("删除视频成功");

                hasVideo = false;
                btnDeleteVideo.setVisibility(View.INVISIBLE);
                mEditInfoHaveVideoIv.setVisibility(View.INVISIBLE);
                ivVideo.setImageResource(R.drawable.add_video);

                if (file.exists() && file.length() > 0) {
                    file.delete();
                }
            } else {
                showToastSuccess("删除出现了问题,请重试");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_AUDIO
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("删除声音成功");
                hasAudio = false;
                btnDeleteAudio.setVisibility(View.INVISIBLE);
                ivVoice.setImageResource(R.drawable.add_voice2x);
                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (null != mediaPlayer) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            } else {
                showToastWarning("删除出现了问题,请重试");
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void disPathUserData(UserDetailResponse.DataBean.UserBean user) {

        String userName = user.getName();
        if (!NSMTypeUtils.isEmpty(userName)) {
            tvName.setText(userName);
        }
        tvDate.setText(DateUtils.formatDate(user.getBirthday()));
        tvSign.setText(user.getSign());

        String background = user.getBackground();
        if (!TextUtils.isEmpty(background)) {
            HttpLoader.getImageLoader().get(background,
                    ImageLoader.getImageListener(ivSelfBg, R.drawable.bg_self, R.drawable.bg_self));
        }

        birthday = DateUtils.formatDate(user.getBirthday());

        //音频视频
        videoUrl = user.getVideoFile();
        VideoThumb = user.getVideoThumb();

        if (!TextUtils.isEmpty(videoUrl)) {
            hasVideo = true;
            if (!TextUtils.isEmpty(VideoThumb))
                HttpLoader.getImageLoader().get(VideoThumb,
                        ImageLoader.getImageListener(ivVideo, R.color.no_color, R.color.no_color));
            btnDeleteVideo.setVisibility(View.VISIBLE);
            mEditInfoHaveVideoIv.setVisibility(View.VISIBLE);
        } else {
            btnDeleteVideo.setVisibility(View.INVISIBLE);
            ivVideo.setImageResource(R.drawable.add_video);
        }

        audioUrl = user.getAudioFile();
        if (user.getAudioDuration() == 0 || TextUtils.isEmpty(audioUrl)) {
            hasAudio = false;
            ivVoice.setImageResource(R.drawable.add_voice2x);
            btnDeleteAudio.setVisibility(View.INVISIBLE);
        } else {
            hasAudio = true;
            ivVoice.setImageResource(R.drawable.user_detail_voice_play);
            btnDeleteAudio.setVisibility(View.VISIBLE);
            try {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerIsPrepared = true;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hasAudio = false;
                ivVoice.setImageResource(R.drawable.add_voice2x);
                btnDeleteAudio.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void audioSuccess(String audioUrl, int audioDuration) {
        UserDetailResponse.DataBean.UserBean user = userDetailResponse.getData().getUser();
        user.setAudioDuration(audioDuration);
        user.setAudioFile(audioUrl);
        hasAudio = true;
        initMediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayerIsPrepared = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            hasAudio = false;
            ivVoice.setImageResource(R.drawable.add_voice2x);
            btnDeleteAudio.setVisibility(View.INVISIBLE);
        }
        ivVoice.setImageResource(R.drawable.user_detail_voice_play);
        btnDeleteAudio.setVisibility(View.VISIBLE);
    }

    public List<String> stringToList(String str) {
        String[] arrayStr = new String[]{};
        arrayStr = str.split(";");
        return java.util.Arrays.asList(arrayStr);
    }

    private void disPathInterestData(List<UserDetailResponse.DataBean.InterestsBean> interestlist) {
        for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
            if ("music_singer".equals(interest.getKey())) {
                singerStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraSinger = interest.getContent();
                    List<String> interestedData = stringToList(extraSinger);
                    tcvSinger.setTags(interestedData);
                } else {
                    tvLikeSinger.setText(singerStr);
                }
            }
            if ("movie_name".equals(interest.getKey())) {
                movieStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraMovie = interest.getContent();
                    List<String> interestedData = stringToList(extraMovie);
                    tcvMovie.setTags(interestedData);
                } else {
                    tvLikeMovie.setText(movieStr);
                }
            }

            if ("food_name".equals(interest.getKey())) {
                foodStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraFood = interest.getContent();
                    List<String> interestedData = stringToList(extraFood);
                    tcvFood.setTags(interestedData);
                } else {
                    tvLikeFood.setText(foodStr);
                }
            }
            if ("trip_name".equals(interest.getKey())) {
                travelStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraTravel = interest.getContent();
                    List<String> interestedData = stringToList(extraTravel);
                    tcvTravel.setTags(interestedData);
                } else {
                    tvTravel.setText(travelStr);
                }
            }
            if ("sport_name".equals(interest.getKey())) {
                sportStr = interest.getPlaceholder();
                if (!TextUtils.isEmpty(interest.getContent())) {
                    extraSport = interest.getContent();
                    List<String> interestedData = stringToList(extraSport);
                    tcvSport.setTags(interestedData);
                } else {
                    tvSport.setText(sportStr);
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

    public String httpRequestMp4(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = null;
        HttpURLConnection conn = null;
        String filename = null;
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
                    filename = file.getName();
                    // MagicMatch match = Magic.getMagicMatch(file, false,
                    // true);
                    // String contentType = match.getMimeType();

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
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

            if (fileMap != null) {
                String inputName = "thumbfile";
                filename = filename.replace(".mp4", ".jpg");
                ALog.d("filename" + filename);
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                        + "\"\r\n");
                strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");

                out.write(strBuf.toString().getBytes());

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(file.getAbsolutePath());
                Bitmap bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                out.write(Bitmap2Bytes(bitmap));
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
            VideoResponse response = new VideoResponse();
            Gson gson = new Gson();
            response = gson.fromJson(res, VideoResponse.class);
            if (response.getCode() == 1) {
                App.USEREDIT.putString("videoUrl", (videoUrl = response.getData().getVideoFile())).commit();
                App.USEREDIT.putString("videoThumb", (VideoThumb = response.getData().getVideoThumb())).commit();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToastSuccess("上传成功");
                    mEditInfoHaveVideoIv.setVisibility(View.VISIBLE);
                    btnDeleteVideo.setVisibility(View.VISIBLE);
                }
            });
            reader.close();
            reader = null;


        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToastError("上传失败");
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


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(file.getAbsolutePath());
                    Bitmap bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    if (null != bitmap) {
                        ivVideo.setImageBitmap(bitmap);
                    }
                    hasVideo = true;
                    final HashMap<String, String> map_param = new HashMap<>();
                    map_param.put("token", NSMTypeUtils.getMyToken());
                    final HashMap<String, String> map_file = new HashMap<>();
                    map_file.put("video", file.getAbsolutePath());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestMp4(ConstantsWhatNSM.URL_ADD_VIDEO, map_param, map_file);
                        }
                    }).start();
                }
                break;

            //照片,图片添加**************************************************************
            case Crop.REQUEST_PICK:
                if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                    beginCrop(data.getData(), Crop.REQUEST_CROP);
                }
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data, false);
                break;

            case Crop.REQUEST_BG_PICK:
                if (requestCode == Crop.REQUEST_BG_PICK && resultCode == RESULT_OK) {
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

            //五种爱好 ****************************************************************
            case FLAG_SINGER:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("music_singer")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            tcvSinger.removeAllViews();
                            tvLikeSinger.setText(singerStr);
                            extraSinger = "";
                        } else {
                            tvLikeSinger.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            tcvSinger.setTags(interestedData);
                            extraSinger = str;
                        }
                    }
                }
                break;
            case FLAG_MOVIE:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("movie_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            tcvMovie.removeAllViews();
                            tvLikeMovie.setText(movieStr);
                            extraMovie = "";
                        } else {
                            tvLikeMovie.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            tcvMovie.setTags(interestedData);
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
                            tcvFood.removeAllViews();
                            tvLikeFood.setText(foodStr);
                            extraFood = "";
                        } else {
                            tvLikeFood.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            tcvFood.setTags(interestedData);
                            extraFood = str;
                        }
                    }
                }
                break;
            case FLAG_TRAVEL:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("trip_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            tcvTravel.removeAllViews();
                            tvTravel.setText(travelStr);
                            extraTravel = "";
                        } else {
                            tvTravel.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            tcvTravel.setTags(interestedData);
                            extraTravel = str;
                        }
                    }
                }
                break;
            case FLAG_SPORT:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("type").equals("sport_name")) {
                        String str = data.getStringExtra("str");
                        if (TextUtils.isEmpty(str)) {
                            tcvSport.removeAllViews();
                            tvSport.setText(sportStr);
                            extraSport = "";
                        } else {
                            tvSport.setVisibility(View.INVISIBLE);
                            List<String> interestedData = stringToList(str);
                            tcvSport.setTags(interestedData);
                            extraSport = str;
                        }
                    }
                }
                break;
            //五种兴趣完毕 **********************************************************

            //修改姓名和签名 *******************************************************
            case FLAG_CHANGE_NICKNAME:
                if (resultCode == RESULT_OK) {
                    String upNick = data.getStringExtra("upNick");
                    tvName.setText(upNick);
                }
                break;
            case FLAG_CHANGE_SIGN:
                if (resultCode == RESULT_OK) {
                    String upSign = data.getStringExtra("upSign");
                    tvSign.setText(upSign);
                }
                break;
            //姓名签名修改完毕 ******************************************************

            default:
                break;
        }
    }

    private void beginCrop(Uri source, int resultCode) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        String imagePath = getRealFilePath(this, source);
        if (null == imagePath) {
            imagePath = source.getPath();
        }
        oriention = getBitmapDegree(imagePath);
        Crop.of(source, destination).asSquare(oriention).start(this, resultCode);
    }

    private void handleBgCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();

            final HashMap<String, String> map_file = new HashMap<>();
            map_file.put("background", imagePath.replace("file://", ""));

            loading_procress_main.setVisibility(View.VISIBLE);
            tv_progress_bar_main.setText("照片上传中。。。");
            iv_progress_bar_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_loading));

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(imagePath, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Matrix m = new Matrix();
                    if (oriention != 0) {
                        m.setRotate(oriention);
                        int width = loadedImage.getWidth();
                        int height = loadedImage.getHeight();
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, height, m, true);
                        Log.d("editself", "loadedImage.getHeight()" + loadedImage.getHeight() + "");
                        ivSelfBg.setImageBitmap(loadedImage);
                    } else {
                        int width = loadedImage.getWidth();
                        int height = loadedImage.getHeight();
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, height, null, true);
                        Log.d("editself", "loadedImage.getHeight()" + loadedImage.getHeight() + "");
                        ivSelfBg.setImageBitmap(loadedImage);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    failReason.getCause();
                    Log.d("editself", " failReason.getCause()" + failReason.toString());
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", NSMTypeUtils.getMyToken());
                    httpRequestPhoto(ConstantsWhatNSM.URL_UPLOAD_BACKGROUND_PHOTOS, map, map_file, oriention, false);
                }
            }).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void handleCrop(int resultCode, Intent result, final boolean isCapture) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();

            dragSquare.fillItemImage(imageStatus, imagePath, isModify, oriention);

            final HashMap<String, String> map_file = new HashMap<>();
            if (isHeaderPic()) {
                map_file.put("logo", imagePath.replace("file://", ""));
            } else {
                map_file.put("photo", imagePath.replace("file://", ""));
            }

            loading_procress_main.setVisibility(View.VISIBLE);
            tv_progress_bar_main.setText("照片上传中。。。");
            iv_progress_bar_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_loading));

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
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
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
                            App.USEREDIT.putString("background", response.getData().getBackground()).commit();
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

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @TargetApi(19)
    public static String getRealFilePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasAudio && mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerIsPrepared = true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioIsPlay = false;
            ivVoice.setImageResource(R.drawable.user_detail_voice_play);
        }
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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
