package com.neishenme.what.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.ActiveAddFragAdapter;
import com.neishenme.what.adapter.UserDetailPhotosAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveMyTakeResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.ImageUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.ListViewAdjustHeight;

import org.seny.android.utils.ActivityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/9 12:36
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 * 旧的活动个人中心界面,由于个人中心的界面改动所以该界面也相应的改动了 新的界面见  @see {@link UserActiveDetailActivity}
 *
 */
@Deprecated
public class UserActiveDetailOldActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener, CustomScrollView.ScrollYChangedListener {
    public static final int STATE_ATTENTION = 1;
    public static final int STATE_UNATTENTION = 2;
    public static final int STATE_NULL = 0;
    public int currentState = STATE_NULL;

    public static final String USER_ID = "userId";
    public static final String TAKE_ME_OUT_ID = "takeMeOutId";

    private CustomScrollView mUserdetailScrolCs;
    private TextView mTvUserName;
    private ImageView mIvUserAttention;
    private TextView mUserDetailSign;
    private ImageView mUserDetailGender;
    private TextView mUserDetailAge;
    private ImageView mUserDetailIconHead, ib_play_icon;
    private ImageView mUserDetailBackgroundIv;
    private ImageView mFirstImage;
    private CircleImageView mUserDetailCiv;
    private FrameLayout mFlUserVoice;

    private View mTitleBar;
    private ImageView mIvIconBack;
    private RelativeLayout mRlInterestMusic;
    private RelativeLayout mRlInterestMovie;
    private RelativeLayout mRlInterestFood;
    private RelativeLayout mRlInterestPlace;
    private RelativeLayout mRlInterestSports;

    private TextView tvAudioEmpty;
    private TextView tvVideoEmpty;
    private ImageButton ibVideoPreview;

    private View mLineMusic;
    private View mLineMovie;
    private View mLineFood;
    private View mLinePlace;
    private View mLineVideo;
    private View mLineAudio;

    private RelativeLayout mRlMusic;
    private RelativeLayout mRlMovie;
    private RelativeLayout mRlFood;
    private RelativeLayout mRlPlace;
    private RelativeLayout mRlSport;

    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private GridView gridView;
    private UserDetailPhotosAdapter adapter;
    private ListViewAdjustHeight mActiveUserDetailMyHarem;

    private int userId;         //用户Id
    private int takeMeOutId;    //活动Id
    private boolean mediaPlayerIsPrepared = false;

    private UserDetailResponse userDetailResponse;
    private UserDetailResponse.DataBean.PhotosBean firstPhoto;
    private List<UserDetailResponse.DataBean.InterestsBean> interestlist;
    private MediaPlayer mediaPlayer;
    private String audio_file;
    private String video_file, video_thumb;
    private RelativeLayout rl_video_frame;
    private RelativeLayout rlAudioAll;
    private RelativeLayout rlVideoAll;

    private TextView mUserDetailUsernameTv;
    private TextView mUserDetailAttentionIv;

    private ArrayList<String> userPhotos;

    @Override
    protected int initContentView() {
        return R.layout.activity_user_active_detail_old;
    }

    @Override
    protected void initView() {
        mUserdetailScrolCs = (CustomScrollView) findViewById(R.id.userdetail_scrol_cs);

        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mIvUserAttention = (ImageView) findViewById(R.id.iv_user_attention);

        mUserDetailSign = (TextView) findViewById(R.id.user_detail_sign);
        mUserDetailGender = (ImageView) findViewById(R.id.user_detail_gender);
        mUserDetailAge = (TextView) findViewById(R.id.user_detail_age);
        mUserDetailIconHead = (ImageView) findViewById(R.id.user_detail_icon_head);
        mFirstImage = (ImageView) findViewById(R.id.first_image);
        gridView = (GridView) findViewById(R.id.gridview);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mInflater = LayoutInflater.from(this);
        mFlUserVoice = (FrameLayout) findViewById(R.id.fl_user_voice);
        mUserDetailCiv = (CircleImageView) findViewById(R.id.user_detail_civ);
        mTitleBar = findViewById(R.id.title_bar);
        mUserDetailBackgroundIv = (ImageView) findViewById(R.id.user_detail_background_iv);
        mIvIconBack = (ImageView) findViewById(R.id.iv_icon_back);
        mRlInterestMusic = (RelativeLayout) findViewById(R.id.rl_interest_music);
        mRlInterestMovie = (RelativeLayout) findViewById(R.id.rl_interest_movie);
        mRlInterestFood = (RelativeLayout) findViewById(R.id.rl_interest_food);
        mRlInterestPlace = (RelativeLayout) findViewById(R.id.rl_interest_place);
        mRlInterestSports = (RelativeLayout) findViewById(R.id.rl_interest_sports);
        tvAudioEmpty = (TextView) findViewById(R.id.tv_audio_empty);
        tvVideoEmpty = (TextView) findViewById(R.id.tv_video_empty);
        ibVideoPreview = (ImageButton) findViewById(R.id.ib_video_preview);
        ib_play_icon = (ImageView) findViewById(R.id.ib_play_icon);
        rl_video_frame = (RelativeLayout) findViewById(R.id.rl_video_frame);
        rlAudioAll = (RelativeLayout) findViewById(R.id.rl_audio_all);
        rlVideoAll = (RelativeLayout) findViewById(R.id.rl_video_all);
        mUserDetailUsernameTv = (TextView) findViewById(R.id.user_detail_username_tv);
        mUserDetailAttentionIv = (TextView) findViewById(R.id.user_detail_attention_iv);
        mLineMusic = (View) findViewById(R.id.line_music);
        mLineMovie = (View) findViewById(R.id.line_movie);
        mLineFood = (View) findViewById(R.id.line_food);
        mLinePlace = (View) findViewById(R.id.line_place);
        mLineVideo = (View) findViewById(R.id.line_video);
        mLineAudio = (View) findViewById(R.id.line_audio);
        mRlMusic = (RelativeLayout) findViewById(R.id.rl_music);
        mRlMovie = (RelativeLayout) findViewById(R.id.rl_movie);
        mRlFood = (RelativeLayout) findViewById(R.id.rl_food);
        mRlPlace = (RelativeLayout) findViewById(R.id.rl_place);
        mRlSport = (RelativeLayout) findViewById(R.id.rl_sport);

        mActiveUserDetailMyHarem = (ListViewAdjustHeight) findViewById(R.id.active_user_detail_my_harem);
    }

    @Override
    protected void initListener() {
        mUserdetailScrolCs.setScrollYChangedListener(this);

        mIvUserAttention.setOnClickListener(this);
        mUserDetailAttentionIv.setOnClickListener(this);
        mUserDetailIconHead.setOnClickListener(this);
        mFlUserVoice.setOnClickListener(this);
        ib_play_icon.setOnClickListener(this);
        ibVideoPreview.setOnClickListener(this);

        mIvIconBack.setOnClickListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userPhotos != null) {
                    ActivityUtils.startActivityForListData(UserActiveDetailOldActivity.this, PhotoViewActivity.class, userPhotos, position + 1);
                }
            }
        });

        mFirstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPhotos != null) {
                    ActivityUtils.startActivityForListData(UserActiveDetailOldActivity.this, PhotoViewActivity.class, userPhotos, 0);
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userId = intent.getIntExtra(USER_ID, -1);
        takeMeOutId = intent.getIntExtra(TAKE_ME_OUT_ID, -1);
        if (userId == -1 || NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
            showToastError("未知错误,请重试!");
            finish();
            return;
        }
        mTitleBar.setAlpha(0);
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioIsPlay = false;
                mUserDetailCiv.setImageResource(R.drawable.user_detail_voice_play);
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
            userDetailResponse = (UserDetailResponse) response;
            if (1 != userDetailResponse.getCode()) {
                showToastInfo(userDetailResponse.getMessage());
                return;
            }

            //是否是自己的
            if (!NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
                int foucs = userDetailResponse.getData().getFoucs();
                if (0 == foucs) {
                    mIvUserAttention.setImageResource(R.drawable.user_detail_attention);
                    mUserDetailAttentionIv.setText("关注");
                    currentState = STATE_ATTENTION;
                } else if (1 == foucs) {
                    mIvUserAttention.setImageResource(R.drawable.user_detail_unattention);
                    mUserDetailAttentionIv.setText("取消关注");
                    currentState = STATE_UNATTENTION;
                } else {
                    mIvUserAttention.setVisibility(View.INVISIBLE);
                    mUserDetailAttentionIv.setVisibility(View.INVISIBLE);
                    currentState = STATE_NULL;
                }
            } else {
                mIvUserAttention.setVisibility(View.INVISIBLE);
                mUserDetailAttentionIv.setVisibility(View.INVISIBLE);
                currentState = STATE_NULL;
            }

            //分发用户信息界面数据
            disPathUserData(userDetailResponse.getData().getUser());

            //分发用户照片信息数据
            List<UserDetailResponse.DataBean.PhotosBean> photos = userDetailResponse.getData().getPhotos();
            if (null != photos && 0 != photos.size()) {
                disPathPhotoData(photos);
            } else {
                //如果没有图片怎么办;
            }

            //分发爱好数据
            interestlist = userDetailResponse.getData().getInterests();
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
                mIvUserAttention.setImageResource(R.drawable.user_detail_unattention);
                mUserDetailAttentionIv.setText("取消关注");
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
                mIvUserAttention.setImageResource(R.drawable.user_detail_attention);
                mUserDetailAttentionIv.setText("关注");
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
            mTvUserName.setText(userName);
            mUserDetailUsernameTv.setText(userName);
        }
        mUserDetailGender.setImageResource(1 == user.getGender() ? R.drawable.man_icon : R.drawable.user_female);
        mUserDetailAge.setText("年龄 " + TimeUtils.getAge(user.getBirthday()));
        mUserDetailSign.setText(user.getSign());
        String userLogo = user.getThumbnailslogo();
        if (!NSMTypeUtils.isEmpty(userLogo)) {
            HttpLoader.getImageLoader().get(userLogo,
                    ImageLoader.getImageListener(mUserDetailIconHead, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mUserDetailIconHead.setImageResource(R.drawable.picture_moren);
        }
        if (user.getAudioDuration() != 0 && !TextUtils.isEmpty(user.getAudioFile())) {
            rlAudioAll.setVisibility(View.VISIBLE);
            mLineAudio.setVisibility(View.VISIBLE);
            audio_file = user.getAudioFile();
            try {
                mediaPlayer.setDataSource(audio_file);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerIsPrepared = true;
                    }
                });
            } catch (IOException e) {
                mediaPlayer = null;
                e.printStackTrace();
            }
            mFlUserVoice.setVisibility(View.VISIBLE);
            tvAudioEmpty.setVisibility(View.GONE);
        } else {
            rlAudioAll.setVisibility(View.GONE);
            mLineAudio.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(user.getVideoFile()) && user.getVideoFile().contains(".mp4")) {
            rlVideoAll.setVisibility(View.VISIBLE);
            mLineVideo.setVisibility(View.VISIBLE);
            video_file = user.getVideoFile();
            video_thumb = user.getVideoThumb();
            rl_video_frame.setVisibility(View.VISIBLE);
            tvVideoEmpty.setVisibility(View.GONE);
            HttpLoader.getImageLoader().get(video_thumb,
                    ImageLoader.getImageListener(ibVideoPreview, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            rlVideoAll.setVisibility(View.GONE);
            mLineVideo.setVisibility(View.GONE);
        }
        final String background = user.getBackground();
        if (!TextUtils.isEmpty(background)) {
            HttpLoader.getImageLoader().get(background,
                    ImageLoader.getImageListener(mUserDetailBackgroundIv, R.drawable.picture_moren,
                            R.drawable.picture_moren));

        } else {
            mUserDetailBackgroundIv.setImageResource(R.drawable.user_background_default);
        }
    }

    private void disPathPhotoData(List<UserDetailResponse.DataBean.PhotosBean> photos) {

        userPhotos = new ArrayList<>();

        String firstImageUrl = photos.get(0).getThumbnails();
        if (!NSMTypeUtils.isEmpty(firstImageUrl)) {
            HttpLoader.getImageLoader().get(firstImageUrl,
                    ImageLoader.getImageListener(mFirstImage, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mFirstImage.setImageResource(R.drawable.picture_moren);
        }
        userPhotos.add(photos.get(0).getPhoto());
        firstPhoto = photos.remove(0);

        adapter = new UserDetailPhotosAdapter(this, getPhotos(photos), mGallery, mInflater);
        gridView.setAdapter(adapter);

        int size = photos.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int columnsNum = size % 2 == 0 ? size / 2 : size / 2 + 1;
        int allWidth = (int) (83 * columnsNum * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridView.setLayoutParams(params);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(columnsNum);
    }

    private void disPathInterestData(List<UserDetailResponse.DataBean.InterestsBean> interestlist) {
        for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
            if ("music_singer".equals(interest.getKey())) {
                String musicContent = interest.getContent();
                if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
                    mRlMusic.setVisibility(View.VISIBLE);
                    mLineMusic.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#6BB34F"));
                    mRlInterestMusic.addView(picChildView);
                } else {
                    mRlMusic.setVisibility(View.GONE);
                    mLineMusic.setVisibility(View.GONE);
                }
            }
            if ("movie_name".equals(interest.getKey())) {
                String movieContent = interest.getContent();
                if (!TextUtils.isEmpty(movieContent) && movieContent.trim() != null) {
                    mRlMovie.setVisibility(View.VISIBLE);
                    mLineMovie.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#9B7BB6"));
                    mRlInterestMovie.addView(picChildView);
                } else {
                    mRlMovie.setVisibility(View.GONE);
                    mLineMovie.setVisibility(View.GONE);
                }
            }
            if ("food_name".equals(interest.getKey())) {
                String foodContent = interest.getContent();
                if (!TextUtils.isEmpty(foodContent) && foodContent.trim() != null) {
                    mRlFood.setVisibility(View.VISIBLE);
                    mLineFood.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#E57373"));
                    mRlInterestFood.addView(picChildView);
                } else {
                    mRlFood.setVisibility(View.GONE);
                    mLineFood.setVisibility(View.GONE);
                }
            }
            if ("trip_name".equals(interest.getKey())) {
                String tripContent = interest.getContent();
                if (!TextUtils.isEmpty(tripContent) && tripContent.trim() != null) {
                    mRlPlace.setVisibility(View.VISIBLE);
                    mLinePlace.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#82B6C9"));
                    mRlInterestPlace.addView(picChildView);
                } else {
                    mRlPlace.setVisibility(View.GONE);
                    mLinePlace.setVisibility(View.GONE);
                }
            }
            if ("sport_name".equals(interest.getKey())) {
                String sportContent = interest.getContent();
                if (!TextUtils.isEmpty(sportContent) && sportContent.trim() != null) {
                    mRlSport.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#CEC175"));
                    mRlInterestSports.addView(picChildView);
                } else {
                    mRlSport.setVisibility(View.GONE);
                }
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

    private boolean audioIsPlay = false;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //点击关注
            case R.id.user_detail_attention_iv:
            case R.id.iv_user_attention:
                switch (currentState) {
                    //关注
                    case STATE_ATTENTION:
                        attentionAdd();
                        break;
                    //取关
                    case STATE_UNATTENTION:
                        attentionUnAdd();
                        break;
                    default:
                        break;
                }
                //点击头像
            case R.id.user_detail_icon_head:

                break;

            //点击声音
            case R.id.fl_user_voice:
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
                        mUserDetailCiv.setImageResource(R.drawable.user_detail_voice_play);
                    } catch (Exception e) {

                    }
                } else {
                    if (!mediaPlayerIsPrepared) {
                        return;
                    }
                    try {
                        mediaPlayer.start();
                        audioIsPlay = true;
                        mUserDetailCiv.setImageResource(R.drawable.pause_x3);
                    } catch (Exception e) {

                    }
                }
                break;

            //点击视频
            case R.id.ib_video_preview:
            case R.id.ib_play_icon:
                Intent intent1 = new Intent(this, PlayVideoActivity.class);
                if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, "1");
                } else {
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, "2");
                    intent1.putExtra("videoSity", video_file);
                }
                startActivity(intent1);
                break;
            //点击返回
            case R.id.iv_icon_back:
                finish();
                break;
        }
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
        }
        audioIsPlay = false;
        mUserDetailCiv.setImageResource(R.drawable.user_detail_voice_play);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    /**
     * 开启活动用户详情界面方法,
     *
     * @param context 上下文
     * @param userId  用户的id
     */
    public static void startUserDetailAct(Context context, int userId, int takeMeOutId) {
        Intent intent = new Intent(context, UserActiveDetailOldActivity.class);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(TAKE_ME_OUT_ID, takeMeOutId);
        context.startActivity(intent);
    }

    @Override
    public void scrollYChange(int y) {
        int showBtn;
        if (y < 0) {
            y = 0;
        }
        showBtn = (int) (y * (0.005));
        if (showBtn >= 1) {
            mUserDetailAttentionIv.setVisibility(View.VISIBLE);
            mUserDetailUsernameTv.setVisibility(View.VISIBLE);
            mTitleBar.setAlpha(1);
        } else {
            mUserDetailAttentionIv.setVisibility(View.INVISIBLE);
            mUserDetailUsernameTv.setVisibility(View.INVISIBLE);
            mTitleBar.setAlpha(0);
        }
    }
}
