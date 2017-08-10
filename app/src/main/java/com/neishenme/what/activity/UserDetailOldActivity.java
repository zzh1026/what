package com.neishenme.what.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.UserDetailPhotosAdapter;
import com.neishenme.what.adapter.UserPhotosAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.ImageUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;

import org.seny.android.utils.ActivityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/12/17 14:36
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个旧的用户信息界面,已弃用
 * 新的用户信息界面 @see {@link UserDetailActivity}
 * .
 * 其作用是 :
 */
@Deprecated
public class UserDetailOldActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener, CustomScrollView.ScrollYChangedListener {
    public static final int STATE_ATTENTION = 1;
    public static final int STATE_UNATTENTION = 2;
    public static final int STATE_EDIT_INFO = 3;
    public static final int STATE_NULL = 0;

    public static final int FLAG_TOPAY_ORDER = 4;

    public int currentState = STATE_NULL;

    private CustomScrollView mUserdetailScrolCs;
    private TextView mTvUserName;
    private ImageView mIvUserAttention;
    private TextView mUserDetailSign;
    private ImageView mUserDetailGender;
    private TextView mUserDetailAge;
    private ImageView ib_play_icon;
    private ImageView mUserDetailBackgroundIv;
    private HorizontalScrollView mHlUserPhoto;
    private ImageView mFirstImage;
    private CircleImageView mUserDetailCiv;
    private CircleImageView mUserDetailIconHead;
    private FrameLayout mFlUserVoice;

    private View mTitleBar;
    private ImageView mIvIconBack;
    private RelativeLayout mRlInterestMusic;
    private RelativeLayout mRlInterestMovie;
    private RelativeLayout mRlInterestFood;
    private RelativeLayout mRlInterestPlace;
    private RelativeLayout mRlInterestSports;
    private TextView mTvInterestMusic;
    private TextView mTvInterestMovie;
    private TextView mTvInterestFood;
    private TextView mTvInterestPlace;
    private TextView mTvInterestSports;

    private Button mUserDetailAgreeBtn;
    private RelativeLayout mUserDetailBackground;
    private ImageView mIvIconNull;

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

    private int userId;
    private int inviteId;
    private int newStatus;
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

    private int width, height;

    int time;

//    private Bitmap backgroundBitmap;
//
//    public Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (backgroundBitmap != null) {
//                bitmapDrawable = new BitmapDrawable(getResources(), backgroundBitmap);
//                mUserDetailBackground.setBackground(bitmapDrawable);
//            }
//        }
//    };
//    private BitmapDrawable bitmapDrawable;

    private ArrayList<String> userPhotos;
    private TrideBean stickyEvent;

    @Override
    protected int initContentView() {
        return R.layout.activity_user_old_detail;
    }

    @Override
    protected void initView() {
        mUserdetailScrolCs = (CustomScrollView) findViewById(R.id.userdetail_scrol_cs);

        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mIvUserAttention = (ImageView) findViewById(R.id.iv_user_attention);

        mUserDetailSign = (TextView) findViewById(R.id.user_detail_sign);
        mUserDetailGender = (ImageView) findViewById(R.id.user_detail_gender);
        mUserDetailAge = (TextView) findViewById(R.id.user_detail_age);

        mUserDetailIconHead = (CircleImageView) findViewById(R.id.user_detail_icon_head);

        mHlUserPhoto = (HorizontalScrollView) findViewById(R.id.hl_user_photo);
        mFirstImage = (ImageView) findViewById(R.id.first_image);
        gridView = (GridView) findViewById(R.id.gridview);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mInflater = LayoutInflater.from(this);

        mFlUserVoice = (FrameLayout) findViewById(R.id.fl_user_voice);
        mUserDetailCiv = (CircleImageView) findViewById(R.id.user_detail_civ);
        mUserDetailBackground = (RelativeLayout) findViewById(R.id.user_detail_background);
        mIvIconNull = (ImageView) findViewById(R.id.iv_icon_null);


        mTitleBar = findViewById(R.id.title_bar);
        mUserDetailBackgroundIv = (ImageView) findViewById(R.id.user_detail_background_iv);

        mIvIconBack = (ImageView) findViewById(R.id.iv_icon_back);

        mRlInterestMusic = (RelativeLayout) findViewById(R.id.rl_interest_music);
        mRlInterestMovie = (RelativeLayout) findViewById(R.id.rl_interest_movie);
        mRlInterestFood = (RelativeLayout) findViewById(R.id.rl_interest_food);
        mRlInterestPlace = (RelativeLayout) findViewById(R.id.rl_interest_place);
        mRlInterestSports = (RelativeLayout) findViewById(R.id.rl_interest_sports);
        mTvInterestMusic = (TextView) findViewById(R.id.tv_interest_music);
        mTvInterestMovie = (TextView) findViewById(R.id.tv_interest_movie);
        mTvInterestFood = (TextView) findViewById(R.id.tv_interest_food);
        mTvInterestPlace = (TextView) findViewById(R.id.tv_interest_place);
        mTvInterestSports = (TextView) findViewById(R.id.tv_interest_sports);

        mUserDetailAgreeBtn = (Button) findViewById(R.id.user_detail_agree_btn);

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
                    ActivityUtils.startActivityForListData(UserDetailOldActivity.this, PhotoViewActivity.class, userPhotos, position + 1);
                }
            }
        });

        mFirstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPhotos != null) {
                    ActivityUtils.startActivityForListData(UserDetailOldActivity.this, PhotoViewActivity.class, userPhotos, 0);
                }
            }
        });

        mUserDetailAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgreeTA();
            }
        });
    }

    private void tryAgreeTA() {
        if (newStatus == 100) {
            agreeTA();
        } else if (newStatus == 50) {
            //PayOrderActivity.startPayOrderActForResult(this, stickyEvent);
            EventBus.getDefault().postSticky(stickyEvent);
            ActivityUtils.startActivityForResultBoolean(this, PayOrderActivity.class, true, FLAG_TOPAY_ORDER);
        } else {
            showToastError("创建活动失败,请重试");
        }
    }

    private void agreeTA() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", String.valueOf(inviteId));
        params.put("userId", String.valueOf(userId));

        mUserDetailAgreeBtn.setClickable(false);
        mUserDetailAgreeBtn.setText("正在同意");
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_ACCEPTUSER, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_ACCEPTUSER, this, false).setTag(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_TOPAY_ORDER && resultCode == RESULT_OK) {
            showToastSuccess("支付成功");
            agreeTA();
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        boolean showButton = intent.getBooleanExtra("showButton", false);
        //是否显示按钮
        if (showButton && !NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
            mUserDetailAgreeBtn.setVisibility(View.VISIBLE);
            inviteId = intent.getIntExtra("inviteId", -1);
            newStatus = intent.getIntExtra("newStatus", 0);
            stickyEvent = EventBus.getDefault().getStickyEvent(TrideBean.class);
        } else {
            mUserDetailAgreeBtn.setVisibility(View.GONE);
        }

        mTitleBar.setAlpha(0);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        initMediaPlayer();

        //getUserDetail();
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
            if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
                //是自己的显示编辑条目
                mIvUserAttention.setImageResource(R.drawable.user_detail_editinfo);
                mUserDetailAttentionIv.setText("编辑资料");
                currentState = STATE_EDIT_INFO;
            } else {
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

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_ACCEPTUSER
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                showToastSuccess("同意成功");
                mUserDetailAgreeBtn.setVisibility(View.INVISIBLE);
            } else {
                showToastInfo(requestResponse.getMessage());
                mUserDetailAgreeBtn.setClickable(true);
                mUserDetailAgreeBtn.setText("同意TA");
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

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//                        BitmapFactory.decodeStream(new URL(background).openStream(), null, options);
//
//
//                        int width = options.outWidth, height = options.outHeight;
//                        int scale = 1;
//                        int temp = width > height ? width : height;
//                        while (true) {
//                            if (temp / 2 < 120)
//                                break;
//                            temp = temp / 2;
//                            scale *= 2;
//                        }
//
//                        BitmapFactory.Options opt = new BitmapFactory.Options();
//                        opt.inSampleSize = scale;
////                        Bitmap bitmap = BitmapFactory.decodeStream(new URL(background).openStream(), null, opt);
//                        backgroundBitmap = BitmapFactory.decodeStream(new URL(background).openStream(), null, opt);
//                        mHandler.sendEmptyMessage(0);
////                      blur(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        mUserDetailBackground.setBackgroundResource(R.drawable.user_background_default);
//                    }
//                }
//            }).start();
        } else {
            mUserDetailBackgroundIv.setImageResource(R.drawable.user_background_default);
            //mUserDetailBackground.setBackgroundResource(R.drawable.user_background_default);
        }
    }

    private void blur(Bitmap bkg) {
//        int radius = 1;
//        backgroundBitmap = ImageUtils.doBlur(bkg, radius, false);
//        mHandler.sendEmptyMessage(0);
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
        //int itemWidth = (int) (85 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridView.setLayoutParams(params);
        //gridView.setColumnWidth(itemWidth);
        //gridView.setHorizontalSpacing(10);
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

//        for (UserDetailResponse.DataBean.PhotosBean userphonesEntity : photos) {
//            String photo = userphonesEntity.getPhoto();
//            if (!TextUtils.isEmpty(photo)) {
//                userPhotos.add(photo);
//            }
//        }
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
                    //编辑
                    case STATE_EDIT_INFO:
                        ActivityUtils.startActivity(this, EditSelfInfoActivity.class);
                        break;
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
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mUserDetailBackground.setBackground(null);
//        if (backgroundBitmap != null && !backgroundBitmap.isRecycled()) {
//            backgroundBitmap.recycle();
//            backgroundBitmap = null;
//        }
//        if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
//            bitmapDrawable.getBitmap().recycle();
//            bitmapDrawable = null;
//        }
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
     * 开启用户详情界面方法,
     *
     * @param context    上下文
     * @param userId     用户的id
     * @param showButton 是否显示"同意他"的按钮
     */
    public static void startUserDetailAct(Context context, int userId, boolean showButton) {
        Intent intent = new Intent(context, UserDetailOldActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("showButton", showButton);
        context.startActivity(intent);
    }

    public static void startUserDetailAct(Context context, int userId, boolean showButton, int inviteId, int newStatus) {
        Intent intent = new Intent(context, UserDetailOldActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("showButton", showButton);
        intent.putExtra("inviteId", inviteId);
        intent.putExtra("newStatus", newStatus);
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
