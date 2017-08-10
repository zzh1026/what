package com.neishenme.what.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.UserPhotosAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteAgreeTaResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.UserDetailResponse;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.ImageUtils;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.RadiusImageViewFour;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
 * 其作用是 该界面 由  主界面菜单的个人中心 进入
 *
 *  该界面属于非常重要的一个大型界面
 *   大型界面大约有 :  主界面 , 我的行程 , 个人中心(本界面) , 编辑个人信息 , 邀请详情界面
 */
public class UserDetailActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    public static final int STATE_ATTENTION = 1;
    public static final int STATE_UNATTENTION = 2;
    public static final int STATE_EDIT_INFO = 3;
    public static final int STATE_NULL = 0;
    public int currentState = STATE_NULL;

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
//    private ListViewAdjustHeight mUserDetailInterestsList;    //用户兴趣适配器,会出现令人费解的现象,弃用

    //    private LinearLayout mUserDetailMyRelation;
    private RelativeLayout mUserDetailMyFriends;
    private RelativeLayout mUserDetailMyFocus;

    //控制我的音视频的显示隐藏
    private LinearLayout mUserDetailMyVoiceAndVideo;    //总布局
    private RelativeLayout mUserDetailMyVoice;      //我的音频
    private View mUserDetailMyVoiceVideoLine;       //音频和视频之间分割线
    private RelativeLayout mUserDetailMyVideo;      //我的视频
    private View mUserDetailZhanwei;                //占位view,作用是占据一半的屏幕

    private Button mUserDetailAgreeBtn;

    private int userId;
    private int inviteId;
    private int newStatus;

    private ArrayList<String> userPhotos;   //用户的图片地址数据
    private UserPhotosAdapter mUserPhotosAdapter;      //用户头像适配器

    //关于音频
    private MediaPlayer mediaPlayer;    //音频播放器
    private String mVoiceUrl;   //用户音频地址
//    private String mVideoUrl;   //用户视频地址


    @Override
    protected int initContentView() {
        return R.layout.activity_user_detail;
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
//        mUserDetailInterestsList = (ListViewAdjustHeight) findViewById(R.id.user_detail_interests_list);

//        mUserDetailMyRelation = (LinearLayout) findViewById(R.id.user_detail_my_relation);
        mUserDetailMyFriends = (RelativeLayout) findViewById(R.id.user_detail_my_friends);
        mUserDetailMyFocus = (RelativeLayout) findViewById(R.id.user_detail_my_focus);

        mUserDetailAgreeBtn = (Button) findViewById(R.id.user_detail_agree_btn);

        mUserDetailMyVoiceAndVideo = (LinearLayout) findViewById(R.id.user_detail_my_voice_and_video);
        mUserDetailMyVoice = (RelativeLayout) findViewById(R.id.user_detail_my_voice);
        mUserDetailMyVoiceVideoLine = (View) findViewById(R.id.user_detail_my_voice_video_line);
        mUserDetailMyVideo = (RelativeLayout) findViewById(R.id.user_detail_my_video);
        mUserDetailZhanwei = (View) findViewById(R.id.user_detail_zhanwei);
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

        mUserDetailMyFriends.setOnClickListener(this);
        mUserDetailMyFocus.setOnClickListener(this);

        mUserDetailAgreeBtn.setOnClickListener(this);
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
//            stickyEvent = EventBus.getDefault().getStickyEvent(TrideBean.class);
        } else {
            mUserDetailAgreeBtn.setVisibility(View.GONE);
        }

        mUserDetailTitleBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_detail_title_attention:  //点击关注
            case R.id.user_detail_attention:
                switch (currentState) {
                    case STATE_EDIT_INFO:   //编辑
                        if (AndPermission.hasPermission(this, Manifest.permission.CAMERA) &&
                                AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                                AndPermission.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
                            ActivityUtils.startActivity(this, EditSelfInfoActivity.class);
                        } else {
                            AndPermission.with(this)
                                    .requestCode(MPermissionManager.REQUEST_CODE_CAMERA)
                                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                    .rationale(mRationaleListener)
                                    .send();
                        }
                        break;
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
            case R.id.user_detail_agree_btn:  //点击同意TA
                tryAgreeTA();
                break;
            case R.id.user_detail_my_friends:  //点击我认识的人
//                ActivityUtils.startActivity(this, RecognizedPeopleActivity.class);
                break;
            case R.id.user_detail_my_focus:  //点击我关注的人
//                ActivityUtils.startActivity(this, FocusPeopleActivity.class);
                break;
            case R.id.user_detail_back:  //点击返回
                finish();
                break;
        }
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA:
                    AndPermission.rationaleDialog(UserDetailActivity.this, rationale).show();
                    break;
            }
        }
    };

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA) {
                ActivityUtils.startActivity(UserDetailActivity.this, EditSelfInfoActivity.class);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA) {
                MPermissionManager.showToSetting(UserDetailActivity.this,
                        MPermissionManager.REQUEST_CODE_CAMERA, new MPermissionManager.OnNegativeListner() {
                            @Override
                            public void onNegative() {

                            }
                        });
            }
        }
    };

    private void tryAgreeTA() {
        if (newStatus == 100 || newStatus == 50) {
            agreeTA();
        } else {
            showToastError("创建活动失败,请重试");
        }
    }

    private void agreeTA() {
        mUserDetailAgreeBtn.setClickable(false);
        mUserDetailAgreeBtn.setText("正在同意");

        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", String.valueOf(inviteId));
        params.put("userId", String.valueOf(userId));
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_ACCEPTUSER, params, InviteAgreeTaResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_ACCEPTUSER, this, false).setTag(this);
    }

    private void toPhotoView(int photoPosition) {
        if (userPhotos != null) {
            ActivityUtils.startActivityForListData(UserDetailActivity.this, PhotoViewActivity.class, userPhotos, photoPosition);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayOrderActivity.REQUEST_CODE_START_PAY)
            if (resultCode == RESULT_OK) {
                showToastSuccess("同意成功");
                mUserDetailAgreeBtn.setVisibility(View.GONE);
            } else {
                showToastError("同意失败");
                mUserDetailAgreeBtn.setClickable(true);
                mUserDetailAgreeBtn.setVisibility(View.VISIBLE);
                mUserDetailAgreeBtn.setText("同意TA");
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

    @Override
    protected void onPause() {
        loadingDismiss();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
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
                mUserDetailAttention.setImageResource(R.drawable.user_detail_edit);
                mUserDetailTitleAttention.setText("编辑资料");
                currentState = STATE_EDIT_INFO;
//                mUserDetailMyRelation.setVisibility(View.VISIBLE);
            } else {
//                mUserDetailMyRelation.setVisibility(View.GONE);
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
                showToastSuccess("取消成功");
                mUserDetailAttention.setImageResource(R.drawable.user_detail_attention);
                mUserDetailTitleAttention.setText("关注");
                currentState = STATE_ATTENTION;
            } else {
                showToastError("取消失败,请重试");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_ACCEPTUSER
                && response instanceof InviteAgreeTaResponse) {
            InviteAgreeTaResponse inviteAgreeTaResponse = (InviteAgreeTaResponse) response;
            int code = inviteAgreeTaResponse.getCode();
            if (1 == code) {
                InviteAgreeTaResponse.DataBean data = inviteAgreeTaResponse.getData();
                if (null == data || null == data.getInvite() || null == data.getTrade()) {     //已经加入成功
                    showToastSuccess("同意成功");
                    mUserDetailAgreeBtn.setVisibility(View.GONE);
                } else {
                    mUserDetailAgreeBtn.setClickable(true);
                    mUserDetailAgreeBtn.setText("正在付款");
                    TrideBean trideBean = new TrideBean(true, 0, data.getPublishType(),
                            data.getUserLogo(), data.getInvite().getTitle(), data.getInvite().getTime(),
                            data.getTrade().getPayprice(), data.getTrade().getTradeNum());
                    PayOrderActivity.startPayOrderActForResult(this, PayOrderActivity.REQUEST_CODE_START_PAY, trideBean);
                }
            } else {
                showToastInfo(inviteAgreeTaResponse.getMessage());
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
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_ACCEPTUSER) {
            mUserDetailAgreeBtn.setClickable(true);
            mUserDetailAgreeBtn.setText("同意TA");
            getUserDetail();
        }
    }

    private void disPathUserData(UserDetailResponse.DataBean.UserBean user) {
        String userName = user.getName();
        if (!NSMTypeUtils.isEmpty(userName)) {
            mUserDetailUsername.setText(userName);
            mUserDetailTitleUsername.setText(userName);
        }
//        user.v
        mUserDetailGender.setText(1 == user.getGender() ? "男" : "女");
        mUserDetailAge.setText(TimeUtils.getAge(user.getBirthday()) + "");
        mUserDetailFocusNumber.setText(user.getFoucsUsersCount() + "");
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

        //新的去掉视频之后的判断
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
        String firstPhoto = photos.get(0).getPhoto();
        if (!TextUtils.isEmpty(firstPhoto)) {
            userPhotos.add(photos.get(0).getPhoto());      //添加第一张
        }
        photos.remove(0);                             //普通显示里面去掉第一张图片

        List<String> photoLists = getPhotos(photos);
        mUserPhotosAdapter = new UserPhotosAdapter(this, photoLists);
        mUserDetailPhotoGrid.setAdapter(mUserPhotosAdapter);

        int size = photoLists.size();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
        int columnsNum = size % 2 == 0 ? size / 2 : size / 2 + 1;
        float dimension = getResources().getDimension(R.dimen.margin_userdetail_width);
        int allWidth = (int) (columnsNum * dimension);
//        //int itemWidth = (int) (85 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        mUserDetailPhotoGrid.setLayoutParams(params);
        //gridView.setColumnWidth(itemWidth);
        //gridView.setHorizontalSpacing(10);
//        mUserDetailPhotoGrid.setStretchMode(GridView.NO_STRETCH);
        mUserDetailPhotoGrid.setNumColumns(columnsNum);
    }

    private void disPathInterestData(List<UserDetailResponse.DataBean.InterestsBean> interestlist) {
        StringBuffer sb = new StringBuffer();
        for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
            if ("movie_name".equals(interest.getKey())) {
                String musicContent = interest.getContent();
                if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
                    mUserDetailInterestLayoutMovie.setVisibility(View.VISIBLE);
                    mUserDetailInterestLineMovie.setVisibility(View.VISIBLE);
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#9B7BB6"));
                    mUserDetailInterestContentMovie.addView(picChildView);
                    sb.append("1");
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
                    sb.append("2");
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
                    View picChildView = ImageUtils.getPicChildView(interest.getContent(), Color.parseColor("#82B6C9"));
                    mUserDetailInterestContentPlace.addView(picChildView);
                    sb.append("3");
                } else {
                    mUserDetailInterestLayoutPlace.setVisibility(View.GONE);
                }
                continue;
            }
        }

        if (sb.length() == 1) {
            mUserDetailInterestLineMovie.setVisibility(View.GONE);
            mUserDetailInterestLineFood.setVisibility(View.GONE);
        }

//        List<UserDetailResponse.DataBean.InterestsBean> interestlists = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            switch (i) {
//                case 0:
//                    for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
//                        if ("movie_name".equals(interest.getKey())) {
//                            String musicContent = interest.getContent();
//                            if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
//                                interestlists.add(interest);
//                                break;
//                            }
//                        }
//                    }
//                    break;
//                case 1:
//                    for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
//                        if ("food_name".equals(interest.getKey())) {
//                            String musicContent = interest.getContent();
//                            if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
//                                interestlists.add(interest);
//                                break;
//                            }
//                        }
//                    }
//                    break;
//                case 2:
//                    for (UserDetailResponse.DataBean.InterestsBean interest : interestlist) {
//                        if ("trip_name".equals(interest.getKey())) {
//                            String musicContent = interest.getContent();
//                            if (!TextUtils.isEmpty(musicContent) && musicContent.trim() != null) {
//                                interestlists.add(interest);
//                                break;
//                            }
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        UserInterestsAdapter mUserInterestsAdapter = new UserInterestsAdapter(this, interestlists);
//        mUserDetailInterestsList.setAdapter(mUserInterestsAdapter);
    }

    private List<String> getPhotos(List<UserDetailResponse.DataBean.PhotosBean> photos) {
        List<String> lists = new ArrayList<>();
        for (UserDetailResponse.DataBean.PhotosBean photosEntity : photos) {
            String thumbnailsPhoto = photosEntity.getThumbnailsPhoto();
            if (!TextUtils.isEmpty(thumbnailsPhoto)) {
                lists.add(photosEntity.getThumbnailsPhoto());
            }
            String photo = photosEntity.getPhoto();
            if (!TextUtils.isEmpty(photo)) {
                userPhotos.add(photo);
            }
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
     * @param context    上下文
     * @param userId     用户的id
     * @param showButton 是否显示"同意他"的按钮
     */
    public static void startUserDetailAct(Context context, int userId, boolean showButton) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("showButton", showButton);
        context.startActivity(intent);
    }

    public static void startUserDetailAct(Context context, int userId, boolean showButton, int inviteId, int newStatus) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("showButton", showButton);
        intent.putExtra("inviteId", inviteId);
        intent.putExtra("newStatus", newStatus);
        context.startActivity(intent);
    }
}
