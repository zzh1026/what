package com.neishenme.what.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.ActiveAddFragAdapter;
import com.neishenme.what.adapter.UserPhotosAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveMyTakeResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.ImageUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.ListViewAdjustHeight;
import com.neishenme.what.view.RadiusImageViewFour;

import org.seny.android.utils.ActivityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/12/12 14:36
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个新的用户信息界面 ,用来代替旧的界面 旧的界面为: @see {@link UserDetailOldActivity}
 * .
 * 其作用是 :   一元购活动的 用户中心界面(这个界面在活动过后其实可以删除, 没什么重要作用,现在进入方式是 banner图或者主
 *  界面广告跳转)
 */
public class UserActiveDetailActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    public static final int STATE_ATTENTION = 1;
    public static final int STATE_UNATTENTION = 2;
    public static final int STATE_NULL = 0;

    public int currentState = STATE_NULL;

    public static final String USER_ID = "userId";
    public static final String TAKE_ME_OUT_ID = "takeMeOutId";

    private RelativeLayout mUserDetailTitleBar;
    private TextView mUserDetailTitleUsername;
    private TextView mUserDetailTitleAttention;
    private ImageView mUserDetailBack;

    private CustomScrollView mUserDetailScrollLayout;

    private ImageView mUserDetailBackgroundIv;
    private TextView mUserDetailUsername;
    private ImageView mUserDetailAttention;
    private CircleImageView mUserDetailHeadLogo;
    private ImageView mUserDetailUserIsvip;
    private ImageView mUserDetailCrownImg;
    private TextView mUserDetailGender;
    private TextView mUserDetailAge;
    private TextView mUserDetailFocusNumber;

    private ImageView mUserDetailPhotoFirst;
    private LinearLayout mUserDetailPhotoGallery;
    private GridView mUserDetailPhotoGrid;

    private ImageView mUserDetailVoice;
    private RadiusImageViewFour mUserDetailVideo;

    private RelativeLayout mUserDetailInterestLayoutMovie;
    private RelativeLayout mUserDetailInterestContentMovie;
    private View mUserDetailInterestLineMovie;
    private RelativeLayout mUserDetailInterestLayoutFood;
    private RelativeLayout mUserDetailInterestContentFood;
    private View mUserDetailInterestLineFood;
    private RelativeLayout mUserDetailInterestLayoutPlace;
    private RelativeLayout mUserDetailInterestContentPlace;
    private View mUserDetailInterestLinePlace;
//    private ListViewAdjustHeight mUserDetailInterestsList;

    //控制我的音视频的显示隐藏
    private LinearLayout mUserDetailMyVoiceAndVideo;    //总布局
    private RelativeLayout mUserDetailMyVoice;      //我的音频
    private View mUserDetailMyVoiceVideoLine;       //音频和视频之间分割线
    private RelativeLayout mUserDetailMyVideo;      //我的视频
    private View mUserDetailZhanwei;                //占位view,作用是占据一半的屏幕

    private ListViewAdjustHeight mActiveUserDetailMyHarem;  //我的后宫

    private int userId;         //用户Id
    private int takeMeOutId;    //活动Id

    private ArrayList<String> userPhotos;   //用户的图片地址数据
    private UserPhotosAdapter mUserPhotosAdapter;      //用户头像适配器

    //关于音频
    private MediaPlayer mediaPlayer;    //音频播放器
    private boolean mediaPlayerIsPrepared = false;  //标记播放器是否准备好
    private String mVoiceUrl;   //用户音频地址
    private String mVideoUrl;   //用户视频地址

    int time;

    private TrideBean stickyEvent;

    @Override
    protected int initContentView() {
        return R.layout.activity_user_active_detail;
    }

    @Override
    protected void initView() {
        mUserDetailTitleBar = (RelativeLayout) findViewById(R.id.user_detail_title_bar);
        mUserDetailTitleUsername = (TextView) findViewById(R.id.user_detail_title_username);
        mUserDetailTitleAttention = (TextView) findViewById(R.id.user_detail_title_attention);
        mUserDetailBack = (ImageView) findViewById(R.id.user_detail_back);

        mUserDetailScrollLayout = (CustomScrollView) findViewById(R.id.user_detail_scroll_layout);

        mUserDetailBackgroundIv = (ImageView) findViewById(R.id.user_detail_background_iv);
        mUserDetailUsername = (TextView) findViewById(R.id.user_detail_username);
        mUserDetailAttention = (ImageView) findViewById(R.id.user_detail_attention);
        mUserDetailHeadLogo = (CircleImageView) findViewById(R.id.user_detail_head_logo);
        mUserDetailUserIsvip = (ImageView) findViewById(R.id.user_detail_user_isvip);
        mUserDetailCrownImg = (ImageView) findViewById(R.id.user_detail_crown_img);
        mUserDetailGender = (TextView) findViewById(R.id.user_detail_gender);
        mUserDetailAge = (TextView) findViewById(R.id.user_detail_age);
        mUserDetailFocusNumber = (TextView) findViewById(R.id.user_detail_focus_number);

        mUserDetailPhotoFirst = (ImageView) findViewById(R.id.user_detail_photo_first);
        mUserDetailPhotoGallery = (LinearLayout) findViewById(R.id.user_detail_photo_gallery);
        mUserDetailPhotoGrid = (GridView) findViewById(R.id.user_detail_photo_grid);

        mUserDetailVoice = (ImageView) findViewById(R.id.user_detail_voice);
        mUserDetailVideo = (RadiusImageViewFour) findViewById(R.id.user_detail_video);

        mUserDetailInterestLayoutMovie = (RelativeLayout) findViewById(R.id.user_detail_interest_layout_movie);
        mUserDetailInterestContentMovie = (RelativeLayout) findViewById(R.id.user_detail_interest_content_movie);
        mUserDetailInterestLineMovie = (View) findViewById(R.id.user_detail_interest_line_movie);
        mUserDetailInterestLayoutFood = (RelativeLayout) findViewById(R.id.user_detail_interest_layout_food);
        mUserDetailInterestContentFood = (RelativeLayout) findViewById(R.id.user_detail_interest_content_food);
        mUserDetailInterestLineFood = (View) findViewById(R.id.user_detail_interest_line_food);
        mUserDetailInterestLayoutPlace = (RelativeLayout) findViewById(R.id.user_detail_interest_layout_place);
        mUserDetailInterestContentPlace = (RelativeLayout) findViewById(R.id.user_detail_interest_content_place);
        mUserDetailInterestLinePlace = (View) findViewById(R.id.user_detail_interest_line_place);
//        mUserDetailInterestsList = (ListViewAdjustHeight) findViewById(R.id.user_detail_interests_list);

        mUserDetailMyVoiceAndVideo = (LinearLayout) findViewById(R.id.user_detail_my_voice_and_video);
        mUserDetailMyVoice = (RelativeLayout) findViewById(R.id.user_detail_my_voice);
        mUserDetailMyVoiceVideoLine = (View) findViewById(R.id.user_detail_my_voice_video_line);
        mUserDetailMyVideo = (RelativeLayout) findViewById(R.id.user_detail_my_video);
        mUserDetailZhanwei = (View) findViewById(R.id.user_detail_zhanwei);

        mActiveUserDetailMyHarem = (ListViewAdjustHeight) findViewById(R.id.active_user_detail_my_harem);
    }

    @Override
    protected void initListener() {
        mUserDetailScrollLayout.setScrollYChangedListener(new CustomScrollView.ScrollYChangedListener() {
            @Override
            public void scrollYChange(int y) {
                if (y < 0)
                    y = 0;
                if ((int) (y * (0.005)) >= 1) {
                    mUserDetailTitleBar.setVisibility(View.VISIBLE);
                } else {
                    mUserDetailTitleBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        mUserDetailBack.setOnClickListener(this);
        mUserDetailTitleAttention.setOnClickListener(this);

        mUserDetailAttention.setOnClickListener(this);

        mUserDetailPhotoFirst.setOnClickListener(this);
        mUserDetailPhotoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPhotoView(position + 1);
            }
        });

        mUserDetailVoice.setOnClickListener(this);
        mUserDetailVideo.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        takeMeOutId = intent.getIntExtra(TAKE_ME_OUT_ID, -1);
        if (userId == -1 || NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
            showToastError("未知错误,请重试!");
            finish();
            return;
        }
        mUserDetailTitleBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_detail_title_attention:  //点击关注
            case R.id.user_detail_attention:
                switch (currentState) {
                    case STATE_ATTENTION:   //关注
                        attentionAdd();
                        break;
                    case STATE_UNATTENTION: //取关
                        attentionUnAdd();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.user_detail_photo_first:  //第一张图片
                toPhotoView(0);
                break;
            case R.id.user_detail_voice:    //点击声音
                playVoice();
                break;
            case R.id.user_detail_video:     //点击视频
//                if (TextUtils.isEmpty(mVideoUrl)) {
//                    showToast("暂无视频");
//                    return;
//                }
//                Intent intent1 = new Intent(this, PlayVideoActivity.class);
//                if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
//                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, "1");
//                } else {
//                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, "2");
//                    intent1.putExtra("videoSity", mVideoUrl);
//                }
//                startActivity(intent1);
                break;
            case R.id.user_detail_back:  //点击返回
                finish();
                break;
        }
    }

    private void toPhotoView(int photoPosition) {
        if (userPhotos != null) {
            ActivityUtils.startActivityForListData(UserActiveDetailActivity.this, PhotoViewActivity.class, userPhotos, photoPosition);
        }
    }

    private void playVoice() {
        if (mediaPlayer == null) {
            return;
        }
        if (TextUtils.isEmpty(mVoiceUrl)) {
            showToastInfo("暂无音频");
            return;
        }
        if (mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.stop();
                mUserDetailVoice.setImageResource(R.drawable.user_voice_play_bg);
            } catch (Exception e) {
            }
        } else {
            try {
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        mUserDetailVoice.setImageResource(R.drawable.user_voice_pause_bg);
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void initMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mUserDetailVoice.setImageResource(R.drawable.user_voice_play_bg);
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
        loadingShow();

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_USER_DETAIL, params, UserDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL, this).setTag(this);
    }

    //获取用户的后宫
    private void getUserHarem() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("takeMeOutId", String.valueOf(takeMeOutId));
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_USER_JOIN, params, ActiveMyTakeResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_USER_JOIN, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL
                && response instanceof UserDetailResponse) {
            UserDetailResponse userDetailResponse = (UserDetailResponse) response;
            if (1 != userDetailResponse.getCode()) {
                showToastInfo(userDetailResponse.getMessage());
                return;
            }
            //是否是自己的
            if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {  //是自己的显示编辑条目
                mUserDetailAttention.setVisibility(View.INVISIBLE);
                mUserDetailTitleAttention.setVisibility(View.INVISIBLE);
                currentState = STATE_NULL;
            } else {
                int foucs = userDetailResponse.getData().getFoucs();
                if (0 == foucs) {
                    mUserDetailAttention.setImageResource(R.drawable.user_detail_attention);
                    mUserDetailTitleAttention.setText("关注");
                    currentState = STATE_ATTENTION;
                } else if (1 == foucs) {
                    mUserDetailAttention.setImageResource(R.drawable.user_detail_unattention);
                    mUserDetailTitleAttention.setText("取消关注");
                    currentState = STATE_UNATTENTION;
                } else {
                    mUserDetailAttention.setVisibility(View.INVISIBLE);
                    mUserDetailTitleAttention.setVisibility(View.INVISIBLE);
                    currentState = STATE_NULL;
                }
            }

            //分发用户信息界面数据
            disPathUserData(userDetailResponse.getData().getUser());

            UserDetailResponse.DataBean.VipBean vip = userDetailResponse.getData().getVip();
            if (null != vip) {
                disPathUserVipInfo(vip);
            } else {
                mUserDetailUserIsvip.setVisibility(View.INVISIBLE);
                mUserDetailCrownImg.setVisibility(View.INVISIBLE);
            }

            //分发用户照片信息数据
            List<UserDetailResponse.DataBean.PhotosBean> photos = userDetailResponse.getData().getPhotos();
            if (null != photos && 0 != photos.size()) {
                disPathPhotoData(photos);
            } else {    //肯定有照片数据,如果没有在这里处理
                String userLogo = userDetailResponse.getData().getUser().getThumbnailslogo();
                if (!NSMTypeUtils.isEmpty(userLogo)) {
                    HttpLoader.getImageLoader().get(userLogo,
                            ImageLoader.getImageListener(mUserDetailPhotoFirst, R.drawable.picture_moren, R.drawable.picture_moren));
                } else {
                    mUserDetailPhotoFirst.setImageResource(R.drawable.picture_moren);
                }
            }

            //分发爱好数据
            List<UserDetailResponse.DataBean.InterestsBean> interestlist = userDetailResponse.getData().getInterests();
            if (null != interestlist && 0 != interestlist.size()) {
                disPathInterestData(interestlist);
            }
            loadingDismiss();
        }

        //获取用户的后宫
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_USER_JOIN
                && response instanceof ActiveMyTakeResponse) {
            ActiveMyTakeResponse myTakeResponse = (ActiveMyTakeResponse) response;
            if (myTakeResponse.getCode() == 1) {
                ActiveAddFragAdapter activeAddFragAdapter = new ActiveAddFragAdapter(this, myTakeResponse.getData().getList());
                mActiveUserDetailMyHarem.setAdapter(activeAddFragAdapter);
            } else {
                showToastInfo(myTakeResponse.getMessage());
            }
        }

        //关注成功否
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS &&
                response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                showToastSuccess("关注成功");
                mUserDetailAttention.setImageResource(R.drawable.user_detail_unattention);
                mUserDetailTitleAttention.setText("取消关注");
                currentState = STATE_UNATTENTION;
            } else {
                showToastError("关注失败,请重试");
            }
        }

        //取消关注是否成功
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                showToastSuccess("取消关注成功");
                mUserDetailAttention.setImageResource(R.drawable.user_detail_attention);
                mUserDetailTitleAttention.setText("关注");
                currentState = STATE_ATTENTION;
            } else {
                showToastError("取消失败,请重试");
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_DETAIL) {
            showToastError("网络连接错误!");
            loadingDismiss();
            finish();
        }
    }

    private void disPathUserData(UserDetailResponse.DataBean.UserBean user) {
        String userName = user.getName();
        if (!NSMTypeUtils.isEmpty(userName)) {
            mUserDetailUsername.setText(userName);
            mUserDetailTitleUsername.setText(userName);
        }
        mUserDetailGender.setText(1 == user.getGender() ? "男" : "女");
        mUserDetailAge.setText(TimeUtils.getAge(user.getBirthday()) + "");
//        mUserDetailFocusNumber.setText(user.getFocusNumber());
        String userLogo = user.getThumbnailslogo();
        if (!NSMTypeUtils.isEmpty(userLogo)) {
            HttpLoader.getImageLoader().get(userLogo,
                    ImageLoader.getImageListener(mUserDetailHeadLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mUserDetailHeadLogo.setImageResource(R.drawable.picture_moren);
        }

        boolean hasVoice, hasVideo; //标记是否有音视频

        mVoiceUrl = user.getAudioFile();
        if (user.getAudioDuration() != 0 && !TextUtils.isEmpty(mVoiceUrl)) {
            try {
                mediaPlayer.setDataSource(mVoiceUrl);
                hasVoice = true;
            } catch (IOException e) {
                mediaPlayer = null;
                e.printStackTrace();
                hasVoice = false;
            }
        } else {
            hasVoice = false;
        }

//        mVideoUrl = user.getVideoFile();
//        if (!TextUtils.isEmpty(user.getVideoFile()) && mVideoUrl.contains(".mp4")) {
//            mUserDetailVideo.setVisibility(View.VISIBLE);
//            String videoThumb = user.getVideoThumb();
//            HttpLoader.getImageLoader().get(videoThumb,
//                    ImageLoader.getImageListener(mUserDetailVideo, R.drawable.picture_moren, R.drawable.picture_moren));
//            hasVideo = true;
//        } else {
//            hasVideo = false;
//        }

        //刷新我的音视频ui
//        if (hasVoice && hasVideo) { //都有
//            mUserDetailMyVoiceAndVideo.setVisibility(View.VISIBLE);
//            mUserDetailMyVoice.setVisibility(View.VISIBLE);
//            mUserDetailMyVoiceVideoLine.setVisibility(View.VISIBLE);
//            mUserDetailMyVideo.setVisibility(View.VISIBLE);
//            mUserDetailZhanwei.setVisibility(View.GONE);
//        } else if (hasVoice && !hasVideo) { //有声音没视频
//            mUserDetailMyVoiceAndVideo.setVisibility(View.VISIBLE);
//            mUserDetailMyVoice.setVisibility(View.VISIBLE);
//            mUserDetailMyVoiceVideoLine.setVisibility(View.INVISIBLE);
//            mUserDetailMyVideo.setVisibility(View.INVISIBLE);
//            mUserDetailZhanwei.setVisibility(View.GONE);
//        } else if (!hasVoice && hasVideo) { //有视频没声音
//            mUserDetailMyVoiceAndVideo.setVisibility(View.VISIBLE);
//            mUserDetailMyVoice.setVisibility(View.GONE);
//            mUserDetailMyVoiceVideoLine.setVisibility(View.GONE);
//            mUserDetailMyVideo.setVisibility(View.VISIBLE);
//            mUserDetailZhanwei.setVisibility(View.VISIBLE);
//        } else if (!hasVoice && !hasVideo) {    //没声音没视频
//            mUserDetailMyVoiceAndVideo.setVisibility(View.GONE);
//        } else {    //其他
//            mUserDetailMyVoiceAndVideo.setVisibility(View.GONE);
//        }

        if (hasVoice) {
            mUserDetailMyVoiceAndVideo.setVisibility(View.VISIBLE);
            mUserDetailMyVoice.setVisibility(View.VISIBLE);
        } else {
            mUserDetailMyVoiceAndVideo.setVisibility(View.GONE);
        }

        String background = user.getBackground();   //背景图
        if (!TextUtils.isEmpty(background)) {
            HttpLoader.getImageLoader().get(background,
                    ImageLoader.getImageListener(mUserDetailBackgroundIv, R.drawable.picture_moren,
                            R.drawable.picture_moren));

        } else {
            mUserDetailBackgroundIv.setImageResource(R.drawable.user_background_default);
        }
    }

    private void disPathUserVipInfo(UserDetailResponse.DataBean.VipBean vip) {
        long viptime = vip.getViptime();
        switch (vip.getType()) {
            case 1:
            case 2:
                if (isVip(viptime)) {
                    mUserDetailUserIsvip.setVisibility(View.VISIBLE);
                } else {
                    mUserDetailUserIsvip.setVisibility(View.INVISIBLE);
                }
                break;
            case 3:
                mUserDetailUserIsvip.setVisibility(View.VISIBLE);
                break;
            case 0:
            default:
                mUserDetailUserIsvip.setVisibility(View.INVISIBLE);
                break;
        }

        switch (vip.getVipIdentity()) {
            case 4:
            case 5:
                mUserDetailCrownImg.setVisibility(View.VISIBLE);
                break;
            case 0:
            default:
                mUserDetailCrownImg.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean isVip(long viptime) {
        return viptime > TimeUtils.getCurrentTimeInLong();
    }

    private void disPathPhotoData(List<UserDetailResponse.DataBean.PhotosBean> photos) {
        userPhotos = new ArrayList<>();
        String firstImageUrl = photos.get(0).getThumbnails();
        if (!NSMTypeUtils.isEmpty(firstImageUrl)) {
            HttpLoader.getImageLoader().get(firstImageUrl,
                    ImageLoader.getImageListener(mUserDetailPhotoFirst, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mUserDetailPhotoFirst.setImageResource(R.drawable.picture_moren);
        }
        userPhotos.add(photos.get(0).getPhoto());      //添加第一张
        photos.remove(0);                             //普通显示里面去掉第一张图片

        List<String> photoLists = getPhotos(photos);
        mUserPhotosAdapter = new UserPhotosAdapter(this, photoLists);
        mUserDetailPhotoGrid.setAdapter(mUserPhotosAdapter);

        int size = photoLists.size();
        int columnsNum = size % 2 == 0 ? size / 2 : size / 2 + 1;
        float dimension = getResources().getDimension(R.dimen.margin_userdetail_width);
        int allWidth = (int) (columnsNum * dimension);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        mUserDetailPhotoGrid.setLayoutParams(params);
        mUserDetailPhotoGrid.setNumColumns(columnsNum);
    }

    private void disPathInterestData(List<UserDetailResponse.DataBean.InterestsBean> interestlist) {
        for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
            if ("movie_name".equals(interest.getKey())) {
                String musicContent = interest.getContent();
                if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
                    mUserDetailInterestLayoutMovie.setVisibility(View.VISIBLE);
                    mUserDetailInterestLineMovie.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#9B7BB6"));
                    mUserDetailInterestContentMovie.addView(picChildView);
                } else {
                    mUserDetailInterestLayoutMovie.setVisibility(View.GONE);
                    mUserDetailInterestLineMovie.setVisibility(View.GONE);
                }
                continue;
            }

            if ("food_name".equals(interest.getKey())) {
                String musicContent = interest.getContent();
                if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
                    mUserDetailInterestLayoutFood.setVisibility(View.VISIBLE);
                    mUserDetailInterestLineFood.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#E57373"));
                    mUserDetailInterestContentFood.addView(picChildView);
                } else {
                    mUserDetailInterestLayoutFood.setVisibility(View.GONE);
                    mUserDetailInterestLineFood.setVisibility(View.GONE);
                }
                continue;
            }

            if ("trip_name".equals(interest.getKey())) {
                String musicContent = interest.getContent();
                if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
                    mUserDetailInterestLayoutPlace.setVisibility(View.VISIBLE);
                    mUserDetailInterestLinePlace.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#82B6C9"));
                    mUserDetailInterestContentPlace.addView(picChildView);
                } else {
                    mUserDetailInterestLayoutPlace.setVisibility(View.GONE);
                    mUserDetailInterestLinePlace.setVisibility(View.GONE);
                }
                continue;
            }
        }
    }

    private List<String> getPhotos(List<UserDetailResponse.DataBean.PhotosBean> photos) {
        List<String> lists = new ArrayList<>();
        for (UserDetailResponse.DataBean.PhotosBean photosEntity : photos) {
            lists.add(photosEntity.getThumbnailsPhoto());
            userPhotos.add(photosEntity.getPhoto());
        }
        return lists;
    }

    private void attentionAdd() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("targetId", String.valueOf(userId));
        HttpLoader.post(ConstantsWhatNSM.URL_ADDFOCUS, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS, this, false).setTag(this);
    }

    private void attentionUnAdd() {
        HashMap params = new HashMap();
        params.put("targetId", String.valueOf(userId));
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_CANCLE_FOUCS_PEOPLE, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE, this, false).setTag(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMediaPlayer();
        getUserDetail();
        getUserHarem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            System.gc();
        }
        mUserDetailVoice.setImageResource(R.drawable.user_voice_play_bg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    /**
     * 开启用户详情界面方法,
     *
     * @param context     上下文
     * @param userId      用户的id
     * @param takeMeOutId 是否显示"同意他"的按钮
     */
    public static void startUserDetailAct(Context context, int userId, int takeMeOutId) {
        Intent intent = new Intent(context, UserActiveDetailActivity.class);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(TAKE_ME_OUT_ID, takeMeOutId);
        context.startActivity(intent);
    }
}
