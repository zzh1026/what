package com.neishenme.what.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.adapter.InviteInviterAdapter;
import com.neishenme.what.adapter.ServicePhotoAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.ui.RestaurantHowToGoActivity;
import com.neishenme.what.baidumap.ui.SharedLocationV5Activity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteDetailGetChatResponse;
import com.neishenme.what.bean.InviteResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.component.InviteTalkComponent;
import com.neishenme.what.dialog.InviteUserMissMeetingDialog;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.service.SocketGetLocationService;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.utils.UIUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.GridViewAdjustHeight;
import com.neishenme.what.view.IndicatorLayout;
import com.neishenme.what.view.InviteTalkAnimator;
import com.neishenme.what.view.highlight.view.Guide;
import com.neishenme.what.view.highlight.view.GuideBuilder;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.neishenme.what.utils.HuanXinUtils.MY_HX_USERNAME;

/**
 * 作者：zhaozh create on 2016/12/30 11:30
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个新的 邀请详情 的界面{用来统一之前的加入者详情和发布者详情}
 * .
 * 其作用是 :
 */
public class InviteDetailActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private final int SHOULD_SHOW_TALK_ANIM = 0;
    private final int SHOULD_REQUEST_OTHER_LOCATION = 1;

    private final int REQUEST_OTHER_LOCATION_TIME = 1000 * 5;

    //扫描需要的四个参数
    private String inviteId;        //邀请id  该界面有
    private String joinerId;        //加入id  未知,需要进行判断
    private String targetId;        //本人id  自己
    private String publisherId;     //发布id  自己

    private InviteInviterAdapter inviteInviterAdapter;  //加入者的适配器
    Gson gson = null;

    //总控制
    private CustomScrollView mInviteDetailCscroll;
    //用户头像轮播图
    private ViewPager mInviteDetailUserPhotos;
    private IndicatorLayout mInviteDetailUserIndicate;
    //用户信息
    private TextView mInviteDetailUserName;
    private ImageView mInviteDetailUserGender;
    private TextView mInviteDetailUserAge;
    private TextView mInviteDetailInviteType;
    private TextView mInviteDetailUserSign;
    private TextView mInviteDetailInvitePrice;
    private CircleImageView mInviteDetailUserLogo;
    //显示更多和邀请信息
    private LinearLayout mInviteDetailMoreInfo;
    private TextView mInviteDetailInviteTitle;
    private TextView mInviteDetailInviteTime;
    private TextView mInviteDetailInviteDistime;
    private TextView mInviteDetailInviteAddress;
    private RelativeLayout mInviteDetailInviteAddressMap;
    //加入用户和成单聊天
    private TextView mInviteDetailJoinNumber;
    private RelativeLayout mInviteDetailTalkLayout;
    private ImageView mInviteDetailTalk;
    private GridViewAdjustHeight mInviteDetailJoinPersons;
    //顶部显示
    private ImageView mInviteDetailBackIv;
    private TextView mInviteDetailTitleTv;
    //底部显示
    private LinearLayout mInviteDetailTogetherShow;
    private Button mInviteDetailTogetherGo;
    private ImageView mInviteDetailTogetherMap;
    private TextView mInviteDetailTogetherDistence;

    private LinearLayout ll_music;//音乐播放器
    private LinearLayout ll_video;//视频播放器

    //侧边的按钮状态
    private static final int START_NOW = 100;
    private static final int START_ADDRESS = 200;
    private static final int START_SURE_FACE = 300;
    private static final int START_SURE_FINISH = 400;
    private static final int START_IS_FINISH = 500;
    //    private static final int START_CHOOSE_AGAIN = 3;
    private int startBtnState = START_NOW;


    private long time;  //标记该单的活动时间,会大量用
    private int mInvitePublishType; //发布方式,活动会用到
    private String mUserLogo;       //用户头像,活动会用到
    private int mPublishAreaId;     //该邀请的发布城市id, 加入者状态会用到
    private boolean shouldShownTalkGuide = false;   //标记是否应该展示谈话功能引导
    private ImageView mShowGuideTalkBg;
    private InviteDetailGetChatResponse.DataBean talkedInfo;    //可以聊天的时候的聊天对象信息

    private ArrayList<String> userHeaderPhoto;

    private double mInviteLatitude;   //商家位置 ,在查看商家位置和共享位置初始化商家位置的时候需要
    private double mInviteLongitude;
    private double otherLatitude = 0;   //对方位置 ,在共享位置初始化对方位置时候需要
    private double otherLongitude = 0;
    private String joinerLogo;  //加入者的头像
    private String myLogo;  //自己的头像

    private boolean isSocketSuccess = false;    //标记socket是否连接成功

    private InviteResponse.DataBean mInviteResponseData;    //邀请详情的数据
    private List<InviteResponse.DataBean.JoinersBean> joiners;  //加入者的所有数据,因为发布者可以通过加入者的头像进入其个人信息

    private YoYo.AnimationComposer talkAnim;    //提示谈话的动画
    private YoYo.AnimationComposer slideInAnim;    //提示谈话的动画

    private InviteUserMissMeetingDialog missMeetingDialog;  //用户爽约的弹窗

    private boolean isFirst = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOULD_SHOW_TALK_ANIM:
                    talkAnim.playOn(mInviteDetailTalk);
                    mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_TALK_ANIM, 1500);
                    break;
                case SHOULD_REQUEST_OTHER_LOCATION: //应该连接socket
                    requestLoginSocket();
                    break;
            }
        }
    };
    private MediaPlayer mediaPlayer;
    private String audioFile;
    private String videoFile;
    private ImageView iv_misic; //音乐播放图标
    private ImageView iv_video;//视频播放图标
    private LinearLayout ll_video_all;//视频
    private LinearLayout ll_misic_all;//音乐
    private View line_video;
    private View line_misic;
    private TextView tv_misic;

    @Override
    protected int initContentView() {
        return R.layout.activity_invite_detail;
    }

    @Override
    protected void initView() {
        ll_video_all = (LinearLayout) findViewById(R.id.ll_video_all);
        ll_misic_all = (LinearLayout) findViewById(R.id.ll_misic_all);
        line_video = findViewById(R.id.line_video);
        line_misic = findViewById(R.id.line_misic);
        tv_misic = (TextView) findViewById(R.id.tv_misic);

        mInviteDetailCscroll = (CustomScrollView) findViewById(R.id.invite_detail_cscroll);
        ll_music = (LinearLayout) findViewById(R.id.ll_music);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        iv_misic = (ImageView) findViewById(R.id.iv_misic);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        mInviteDetailUserPhotos = (ViewPager) findViewById(R.id.invite_detail_user_photos);
        mInviteDetailUserIndicate = (IndicatorLayout) findViewById(R.id.invite_detail_user_indicate);

        mInviteDetailUserName = (TextView) findViewById(R.id.invite_detail_user_name);
        mInviteDetailUserGender = (ImageView) findViewById(R.id.invite_detail_user_gender);
        mInviteDetailUserAge = (TextView) findViewById(R.id.invite_detail_user_age);
        mInviteDetailInviteType = (TextView) findViewById(R.id.invite_detail_invite_type);
        mInviteDetailUserSign = (TextView) findViewById(R.id.invite_detail_user_sign);
        mInviteDetailInvitePrice = (TextView) findViewById(R.id.invite_detail_invite_price);
        mInviteDetailUserLogo = (CircleImageView) findViewById(R.id.invite_detail_user_logo);

        mInviteDetailMoreInfo = (LinearLayout) findViewById(R.id.invite_detail_more_info);
        mInviteDetailInviteTitle = (TextView) findViewById(R.id.invite_detail_invite_title);
        mInviteDetailInviteTime = (TextView) findViewById(R.id.invite_detail_invite_time);
        mInviteDetailInviteDistime = (TextView) findViewById(R.id.invite_detail_invite_distime);
        mInviteDetailInviteAddress = (TextView) findViewById(R.id.invite_detail_invite_address);
        mInviteDetailInviteAddressMap = (RelativeLayout) findViewById(R.id.invite_detail_invite_address_map);

        mInviteDetailJoinNumber = (TextView) findViewById(R.id.invite_detail_join_number);
        mInviteDetailTalkLayout = (RelativeLayout) findViewById(R.id.invite_detail_talk_layout);
        mInviteDetailTalk = (ImageView) findViewById(R.id.invite_detail_talk);
        mShowGuideTalkBg = (ImageView) findViewById(R.id.show_guide_talk_bg);
        mInviteDetailJoinPersons = (GridViewAdjustHeight) findViewById(R.id.invite_detail_join_persons);

        mInviteDetailBackIv = (ImageView) findViewById(R.id.invite_detail_back_iv);
        mInviteDetailTitleTv = (TextView) findViewById(R.id.invite_detail_title_tv);

        mInviteDetailTogetherShow = (LinearLayout) findViewById(R.id.invite_detail_together_show);
        mInviteDetailTogetherGo = (Button) findViewById(R.id.invite_detail_together_go);
        mInviteDetailTogetherMap = (ImageView) findViewById(R.id.invite_detail_together_map);
        mInviteDetailTogetherDistence = (TextView) findViewById(R.id.invite_detail_together_distence);
    }

    private void playVoice() {
        if (mediaPlayer == null) {
            return;
        }
        if (TextUtils.isEmpty(audioFile)) {
            showToastInfo("暂无音频");
            return;
        }

        if (mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.stop();
//                iv_misic.setImageResource(R.drawable.paly_audio_icon);
                tv_misic.setText("点击播放");
            } catch (Exception e) {
            }
        } else {
            try {
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
//                        iv_misic.setImageResource(R.drawable.stop_audio_icon);
                        tv_misic.setText("点击停止");
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void initListener() {
        //视频播放器监听
        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(videoFile)) {
                    showToastInfo("暂无视频");
                    return;
                }
                Intent intent1 = new Intent(InviteDetailActivity.this, PlayVideoActivity.class);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, "2");
                intent1.putExtra("videoSity", videoFile);
                startActivity(intent1);
            }
        });
        //音乐播放器监听
        ll_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice();
            }
        });
        mInviteDetailCscroll.setScrollYChangedListener(new CustomScrollView.ScrollYChangedListener() {
            @Override
            public void scrollYChange(int y) {
                if (y < 0) {
                    y = 0;
                }
                mInviteDetailTitleTv.setAlpha((y * (0.005)) >= 1 ? 1 : 0);
            }
        });
        mInviteDetailBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInviteDetailUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == userHeaderPhoto) {
                    return;
                }
                ActivityUtils.startActivityForListData(InviteDetailActivity.this,
                        PhotoViewActivity.class, userHeaderPhoto, 0);
            }
        });

        mInviteDetailJoinPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userId = joiners.get(position).getJoiner_userId();
                int newstatus = mInviteResponseData.getInvite().getNewstatus();
                boolean showBtn = false;
                //newstatus 我的状态, 会员支付或者我已经支付都是可以同意别人的,其他的情况不能同意别人
                if (50 == newstatus || 100 == newstatus) {
                    if (time > System.currentTimeMillis()) {
                        showBtn = true;
                        EventBus.getDefault().removeAllStickyEvents();
                    }
                }
                int inviteId = mInviteResponseData.getInvite().getId();
                UserDetailActivity.startUserDetailAct(InviteDetailActivity.this, userId, showBtn, inviteId, newstatus);
            }
        });

        mInviteDetailTogetherGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (startBtnState) {
                    case START_NOW:     //现在出发
                    case START_ADDRESS: //确认到达
                    case START_SURE_FINISH: //确认完成
                        isSocketSuccess = false;
                        getStartNow(startBtnState);
                        break;
                    case START_SURE_FACE:   //确认见面
                        InviteDetailBean inviteDetailBean = new InviteDetailBean(inviteId, joinerId, targetId, publisherId);
                        ZXingGetActivity.startZXingAct(InviteDetailActivity.this, inviteDetailBean);
                        break;
                }
            }
        });

        mInviteDetailTogetherMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //位置共享
                SharedLocationV5Activity.startSharedLocationAct(
                        InviteDetailActivity.this, mInviteLatitude, mInviteLongitude,
                        otherLatitude, otherLongitude, joinerLogo, myLogo, inviteId);
            }
        });

        mInviteDetailMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到用户详情界面
                UserDetailActivity.startUserDetailAct(InviteDetailActivity.this, Integer.parseInt(NSMTypeUtils.getMyUserId()), false);
            }
        });

        mInviteDetailInviteAddressMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantHowToGoActivity.startRestHowToGoAct(
                        InviteDetailActivity.this, mInviteLatitude, mInviteLongitude);
            }
        });

        mInviteDetailTalkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == talkedInfo) {
                    return;
                }
                if (TextUtils.isEmpty(talkedInfo.getHxUserName())) {
                    return;
                }

                mHandler.removeMessages(SHOULD_SHOW_TALK_ANIM);
                NSMTypeUtils.talkedOnclick();

                ChatInfoBean chatInfoBean = new ChatInfoBean(
                        talkedInfo.getUserlogo(),
                        talkedInfo.getUserid(),
                        talkedInfo.getUsername());
                EventBus.getDefault().postSticky(chatInfoBean);
                startActivity(new Intent(InviteDetailActivity.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, talkedInfo.getHxUserName()));
            }
        });

    }


    @Override
    protected void initData() {
        loadingShow();

        gson = new Gson();
        EventBus.getDefault().register(this);

        inviteId = getIntent().getStringExtra("data");
        publisherId = NSMTypeUtils.getMyUserId();
        targetId = NSMTypeUtils.getMyUserId();

        mInviteDetailTitleTv.setAlpha(0);
//        shouldShownTalkGuide = ShowGuideConfig.shouldShowTalk();

        if (!HuanXinUtils.isLoginToHX()) {
            HuanXinUtils.login();
        }
    }

    public void onEventMainThread(SocketResultBean socketResultBean) {
        if (socketResultBean.getType().equals("pushlocation")) {    //推送位置
            SocketResultBean.DataEntity dataEntity = socketResultBean.getData();
            if (null != dataEntity && mInviteDetailTogetherShow.getVisibility() == View.VISIBLE) {
                if (startBtnState == START_ADDRESS || startBtnState == START_SURE_FACE
                        || startBtnState == START_IS_FINISH) {
                    int inviteId = dataEntity.getInviteId();
                    otherLatitude = dataEntity.getLatitude();
                    otherLongitude = dataEntity.getLongitude();
                    if (inviteId != (Integer.parseInt(this.inviteId))) {
                        return;
                    }
                    double distance;
                    try {
                        distance = LocationToBaiduMap.getDistance(otherLatitude, otherLongitude, mInviteLatitude, mInviteLongitude);
                        isSocketSuccess = true;
                        mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
                    } catch (Exception e) {
                        return;
                    }
                    mInviteDetailTogetherDistence.setText("对方距离活动地点" + LocationToBaiduMap.getDistance(distance));
                } else {
//                    mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                    isSocketSuccess = true;
                    mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
                }
            } else {
//                showToastError("获取对方位置失败,请返回重试");
            }
        } else if (socketResultBean.getType().equals("message")) {
            if (!socketResultBean.getMessage().equalsIgnoreCase("success")) {
                isSocketSuccess = true;
                mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
                if (startBtnState != START_NOW && isFirst) {
//                    showToastInfo(socketResultBean.getMessage());
                    isFirst = false;
                }
//                mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
            }
        } else {
            isSocketSuccess = true;
            mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
            showToastError("与服务器断开连接");
//            mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
        }
    }

    private void getStartNow(int startBtnState) {
        mInviteDetailTogetherGo.setClickable(false);
        mInviteDetailTogetherGo.setText("正在刷新");
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("setout", String.valueOf(startBtnState));
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_SETOUT, params, InviteSetoutResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT, this).setTag(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
        }

//        UserMissMeetingBean stickyEvent = EventBus.getDefault().getStickyEvent(UserMissMeetingBean.class);
//        if (null != stickyEvent && String.valueOf(stickyEvent.inviteId).equals(inviteId)) {
//            EventBus.getDefault().removeStickyEvent(UserMissMeetingBean.class);
//        }

        isFirst = true;
        getInviteData();
        //初始化mediaPlayer
        initMediaPlayer();
    }

    //初始化mediaPlayer
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
//                iv_misic.setImageResource(R.drawable.paly_audio_icon);
                tv_misic.setText("点击播放");

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }


    @Override
    protected void onPause() {
        if (missMeetingDialog != null && missMeetingDialog.isShowing()) {
            missMeetingDialog.dismiss();
        }
//        iv_misic.setImageResource(R.drawable.paly_audio_icon);
        tv_misic.setText("点击播放");
        super.onPause();
    }

    //获取邀请详情的信息
    private void getInviteData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_DETAIL, params, InviteResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_DETAIL, this).setTag(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getInviteData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInviteDetailTalkLayout.setVisibility(View.INVISIBLE);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
//            System.gc();
        }
    }

    //请求登录socket
    private void requestLoginSocket() {
        if (!isSocketSuccess) {  //未成功,继续调用
//            App.getClientSocket().sendLoginMessage();
            requestOtherLoc();
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_DETAIL
                && response instanceof InviteResponse) {
            InviteResponse mInviteResponse = (InviteResponse) response;
            if (1 != mInviteResponse.getCode()) {
                showToastInfo(mInviteResponse.getMessage());
                return;
            }

            mInviteResponseData = mInviteResponse.getData();
            if (!NSMTypeUtils.getMyUserId().equals(String.valueOf(mInviteResponseData.getInvite().getUserId()))) {
                showToastError("登录出现问题,请重新登录");
                ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
                return;
            }

            //分发界面数据
            disPathInfoData(mInviteResponseData.getInvite(), mInviteResponseData.getInviteMeets());

            disPathUserData(mInviteResponseData.getUser());

//            if (shouldShownTalkGuide) {
//                shouldShownTalkGuide = false;
//                ShowGuideConfig.shownTalked();
//                showGuideView();
//            }

            //分发用户图片数据
            List<InviteResponse.DataBean.UserPhotosBean> userPhotos = mInviteResponseData.getUserPhotos();
            if (null != userPhotos && 0 != userPhotos.size()) {
                disPathUserPhotoData(userPhotos);
            }

            //分发加入者数量数据
            joiners = mInviteResponseData.getJoiners();
            if (null != joiners && 0 != joiners.size()) {
                mInviteDetailJoinNumber.setText("(" + joiners.size() + "人已加入)");
                int joinerId = getJoinerId(joiners);    //查看是否有人已经成单,来得到加入者的id
                if (0 != joinerId) {
                    this.joinerId = String.valueOf(joinerId);
                }
                disPathJoinersData(joiners);
            } else {
                mInviteDetailJoinNumber.setText("(0人已加入)");
            }

            //刷新按钮状态
            refreshMyState(mInviteResponseData.getInvite());
//            if ((time > System.currentTimeMillis()) || (System.currentTimeMillis() - time) < 60 * 60 * 1000) {
//                refreshMyState(mInviteResponseData.getInvite());
//            } else {
//                mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
//                mInviteDetailTogetherShow.setVisibility(View.GONE);
//            }
            loadingDismiss();

//            String userMissmeetInviteId = AppSharePreConfig.getUserMissmeetInviteId();
//            if (inviteId.equals(userMissmeetInviteId)) {
//                if (publisherId.equals(AppSharePreConfig.getUserMissmeetUserId())) {
//                    missMeetingDialog = new InviteUserMissMeetingDialog(this, mInvitePublishType, new InviteUserMissMeetingDialog.OnMissmeetingItemSelected() {
//                        @Override
//                        public void onMissmeetingItemSelected(int item, int type, boolean checked) {
//                            AppSharePreConfig.saveCheckUserMiss();
//                            checkUserMiss(item, type, checked);
//                        }
//                    });
//                    missMeetingDialog.show();
//                }
//            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT
                && response instanceof InviteSetoutResponse) {
            InviteSetoutResponse inviteSetoutResponse = (InviteSetoutResponse) response;
            int code = inviteSetoutResponse.getCode();
            if (1 == code) {
                switch (startBtnState) {
                    case START_NOW:
                        startBtnState = START_ADDRESS;
                        break;
                    case START_ADDRESS:
                        startBtnState = START_SURE_FACE;
                        break;
                    case START_SURE_FINISH:
                        startBtnState = START_IS_FINISH;
                        break;
                    default:
                        break;
                }
            } else {
                showToastInfo(inviteSetoutResponse.getMessage());
            }
            refreshmInviteUserGoBtnState(inviteSetoutResponse.getData());
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_CHAT
                && response instanceof InviteDetailGetChatResponse) {
            InviteDetailGetChatResponse mChatResponse = (InviteDetailGetChatResponse) response;
            int code = mChatResponse.getCode();
            if (1 == code) {
                String hxUserName = mChatResponse.getData().getHxUserName();
                if (!TextUtils.isEmpty(hxUserName) && !hxUserName.equals(App.USERSP.getString(MY_HX_USERNAME, ""))) {
                    slideInAnim = YoYo
                            .with(Techniques.SlideInRight)
                            .duration(500);
                    talkedInfo = mChatResponse.getData();
                    mInviteDetailTalkLayout.setVisibility(View.VISIBLE);
                    slideInAnim.playOn(mInviteDetailTalkLayout);
                    if (NSMTypeUtils.shouldShowTalkAnim()) {
                        talkAnim = YoYo
                                .with(new InviteTalkAnimator())
                                .duration(600);
                        mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_TALK_ANIM, 1000);
                    }
                }
            }
        }
    }

    private void showGuideView() {
        mShowGuideTalkBg.setVisibility(View.VISIBLE);
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(mShowGuideTalkBg)
                .setAlpha(214)
//                .setHighTargetCorner(10)
//                .setHighTargetPadding(2)
                .setOverlayTarget(false)
                .setOutsideTouchable(false)
                .setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismiss() {
                        mShowGuideTalkBg.setVisibility(View.GONE);
                    }
                });
        builder.addComponent(new InviteTalkComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(this);
    }

    private int getJoinerId(List<InviteResponse.DataBean.JoinersBean> joiners) {
        for (InviteResponse.DataBean.JoinersBean joiner : joiners) {
            if (joiner.getJoiner_newstatus() == 150) {
                joinerLogo = joiner.getUser_thumbnailslogo();
                return joiner.getJoiner_userId();
            }
        }
        return 0;
    }

    private void getChatInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_CHAT, params, InviteDetailGetChatResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_CHAT, this).setTag(this);
    }

    private void checkUserMiss(int item, int type, boolean checked) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", inviteId);
        if (type == InviteUserMissMeetingDialog.INVITE_TYPE_NORMAL) {      //专属发布
            params.put("type", String.valueOf(item));
        } else {
            if (item == 0) {
                params.put("type", String.valueOf(item));
            } else {
                params.put("type", "2");
            }
        }
        if (checked) {
            params.put("prompt", "1");
        } else {
            params.put("prompt", "0");
        }
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_USER_MISSMEETING, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_USER_MISSMEETING, this).setTag(this);
    }

    private void refreshmInviteUserGoBtnState(InviteSetoutResponse.DataEntity data) {
        switch (startBtnState) {
            case START_NOW:
                mInviteDetailTogetherGo.setText("立即出发");
                mInviteDetailTogetherGo.setClickable(true);
                break;
            case START_ADDRESS:
                mInviteDetailTogetherGo.setText("确认到达");
                mInviteDetailTogetherGo.setClickable(true);
                mInviteDetailTogetherDistence.setVisibility(View.VISIBLE);
                mInviteDetailTogetherMap.setVisibility(View.VISIBLE);
                break;
            case START_SURE_FACE:
                mInviteDetailTogetherGo.setText("确认见面");
                mInviteDetailTogetherGo.setClickable(true);
                break;
            case START_IS_FINISH:
                mInviteDetailTogetherGo.setText("已完成");
                mInviteDetailTogetherGo.setClickable(false);
                mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                break;
        }
        if (null != data) {
//            if (data.getInviteInfo().getDistance() <= 5000) {
//                mInviteDetailTogetherGo.setClickable(true);
//            }
            if (!isSocketSuccess) {
                requestOtherLoc();
            }
//            int otherSetout = data.getInviteInfo().getOtherSetout();
//            if (100 == otherSetout && data.getInviteInfo().getOtherDistance() > 0) {
//                mInviteDetailTogetherDistence.setVisibility(View.VISIBLE);
//            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_DETAIL) {
            loadingDismiss();
            showToastError("网络连接失败,请检查网络连接");
            finish();
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT) {
            showToastError("服务器请求失败");
            refreshmInviteUserGoBtnState(null);
        }
    }

    private void disPathInfoData(InviteResponse.DataBean.InviteBean invite,
                                 InviteResponse.DataBean.InviteMeetsBean inviteMeetsBean) {
        mPublishAreaId = invite.getAreaId();

        String title = invite.getTitle().trim();
        if (!NSMTypeUtils.isEmpty(title)) {
            mInviteDetailInviteTitle.setText(title);
        }

        String price = inviteMeetsBean.getPrice();
        if (!NSMTypeUtils.isEmpty(price)) {
            mInviteDetailInvitePrice.setText(NSMTypeUtils.getGreatPrice(price));
        }

        String target = NSMTypeUtils.getUserTarget(invite.getTarget());
        mInvitePublishType = invite.getType();
//        mInviteDetailInviteType.setText(NSMTypeUtils.getPublicType(mInvitePublishType) + target);
        mInviteDetailInviteType.setText("匹配对象" + target);
        time = invite.getTime();
        if (TimeUtils.isToday(time) || TimeUtils.isTomorrow(time)) {
            if (TimeUtils.isToday(time)) {
                mInviteDetailInviteTime.setText("今天 " + TimeUtils.getTime(time));
            } else {
                mInviteDetailInviteTime.setText("明天 " + TimeUtils.getTime(time));
            }
            String overTime = TimeUtils.overTime(time);
            if (time >= System.currentTimeMillis()) {
                mInviteDetailInviteDistime.setText("距离活动还有" + overTime);
            } else {
                mInviteDetailInviteDistime.setText("已超时" + overTime);
            }
        } else {
            mInviteDetailInviteTime.setText(TimeUtils.getTime(time, TimeUtils.DATE_FORMAT_NSM));
        }
        mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);

        long chatTime = inviteMeetsBean.getChatTime();
//            if (TimeUtils.isShouldShowTalk(time)) {
        mInviteDetailTalkLayout.setVisibility(View.INVISIBLE);
        int newstatus = invite.getNewstatus();
        if (chatTime != 0 && chatTime < System.currentTimeMillis()) {
            if (newstatus == 150 || newstatus == 180) {
                if (HuanXinUtils.isLoginToHX()) {
                    getChatInfo();
                }
            }
        }

        String address = inviteMeetsBean.getPosition();
        if (!NSMTypeUtils.isEmpty(address)) {
            mInviteDetailInviteAddress.setText(address);
        }

        mInviteLatitude = inviteMeetsBean.getLatitude();
        mInviteLongitude = inviteMeetsBean.getLongitude();

        //MP3文件
        audioFile = invite.getAudioFile();
        //视频文件
        videoFile = invite.getVideoFile();

        if (audioFile == null || audioFile == "") {
            ll_misic_all.setVisibility(View.GONE);
            line_misic.setVisibility(View.GONE);
        } else {
            ll_misic_all.setVisibility(View.VISIBLE);
            line_misic.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(audioFile)) {
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(audioFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (videoFile == null || videoFile == "") {
            ll_video_all.setVisibility(View.GONE);
            line_video.setVisibility(View.GONE);
        } else {
            ll_video_all.setVisibility(View.VISIBLE);
            line_video.setVisibility(View.VISIBLE);
        }
    }

    private void disPathUserData(InviteResponse.DataBean.UserBean user) {
        String name = user.getName();
        if (!NSMTypeUtils.isEmpty(name)) {
            mInviteDetailUserName.setText(name);
        }
        mInviteDetailUserGender.setImageResource(1 == user.getGender() ? R.drawable.man_icon : R.drawable.user_female);
        mInviteDetailUserAge.setText(TimeUtils.getAge(user.getBirthday()) + "");
        mInviteDetailUserSign.setText(user.getSign());
        userHeaderPhoto = new ArrayList<>();
        userHeaderPhoto.add(user.getLogo());
        mUserLogo = user.getThumbnailslogo();
        if (!NSMTypeUtils.isEmpty(mUserLogo)) {
            HttpLoader.getImageLoader().get(mUserLogo,
                    ImageLoader.getImageListener(mInviteDetailUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mInviteDetailUserLogo.setImageResource(R.drawable.picture_moren);
        }

        myLogo = user.getThumbnailslogo();
    }

    private void disPathUserPhotoData(List<InviteResponse.DataBean.UserPhotosBean> userPhotos) {
        if (mInviteDetailUserIndicate != null) {
            mInviteDetailUserIndicate.removeAllViewsInLayout();
        }
        mInviteDetailUserPhotos.setAdapter(new ServicePhotoAdapter(getUserPhotos(userPhotos)));
        mInviteDetailUserIndicate.setViewPage(mInviteDetailUserPhotos);
    }

    private void disPathJoinersData(List<InviteResponse.DataBean.JoinersBean> joiners) {
        if (inviteInviterAdapter == null) {
            inviteInviterAdapter = new InviteInviterAdapter(joiners, this, mPublishAreaId);
            mInviteDetailJoinPersons.setAdapter(inviteInviterAdapter);
        } else {
            inviteInviterAdapter.setJoiners(joiners);
        }
    }

    public static List<String> getUserPhotos(List<InviteResponse.DataBean.UserPhotosBean> userPhotos) {
        List<String> lists = new ArrayList<>();
        for (InviteResponse.DataBean.UserPhotosBean userPhoto : userPhotos) {
            String photo = userPhoto.getPhoto();
            if (!TextUtils.isEmpty(photo))
                lists.add(userPhoto.getPhoto());
        }
        return lists;
    }

    private void refreshMyState(InviteResponse.DataBean.InviteBean invite) {
        switch (invite.getNewstatus()) {    //我的状态
            case 150:    //成单
                mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                isSocketSuccess = false;
                requestOtherLoc();  //请求对方的位置

                mInviteDetailInviteAddressMap.setVisibility(View.VISIBLE);
                mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                int setout = invite.getSetout();
                if (0 == setout) {
                    mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                    mInviteDetailTogetherGo.setText("立即出发");
                    startBtnState = START_NOW;
                    mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                    mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                } else if (100 == setout) {
                    mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                    mInviteDetailTogetherGo.setText("确认到达");
                    startBtnState = START_ADDRESS;
                    mInviteDetailTogetherMap.setVisibility(View.VISIBLE);
                    mInviteDetailTogetherDistence.setVisibility(View.VISIBLE);
                } else if (200 == setout) {
                    mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                    mInviteDetailTogetherGo.setText("确认见面");
                    startBtnState = START_SURE_FACE;
                    mInviteDetailTogetherMap.setVisibility(View.VISIBLE);
                    mInviteDetailTogetherDistence.setVisibility(View.VISIBLE);
                }
                break;
            case 180:   //已见面
                mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                isSocketSuccess = false;
                requestOtherLoc();  //请求对方的位置
                startBtnState = START_SURE_FINISH;
                mInviteDetailTogetherGo.setText("确认完成");
                mInviteDetailTogetherGo.setClickable(true);
                mInviteDetailInviteAddressMap.setVisibility(View.VISIBLE);
                mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                break;
            case 200:   //已完成
                startBtnState = START_IS_FINISH;
                mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                mInviteDetailTogetherGo.setClickable(false);
                mInviteDetailTogetherGo.setText("已完成");
                mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                break;
            //穿透
            case 50:    //会员支付
            case 100:   //已支付
                mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherShow.setVisibility(View.GONE);
                break;
            case 0:     //未支付
            case 120:   //等待对方付款    //该状态不会再出现 by 2017_1_5
            case 700:   //主动取消
            case 750:   //超时自动退款
            case 800:   //被禁止
            case 900:   //删除
            default:
                mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                mInviteDetailTogetherShow.setVisibility(View.GONE);
                break;
        }
    }

    private void requestOtherLoc() {
        if (isSocketSuccess) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(SHOULD_REQUEST_OTHER_LOCATION, REQUEST_OTHER_LOCATION_TIME);
        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
//                Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
                SocketSendBean socketSendBean =
                        NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET,
                                location.getLatitude(), location.getLongitude(), Integer.parseInt(inviteId));
                EventBus.getDefault().post(socketSendBean);

//                String sendBean = gson.toJson(socketSendBean);
//                ALog.i(sendBean);
//                try {
//                    App.getClientSocket().addSendMsgToQueue(sendBean);
//                } catch (Exception e) {
//                    ALog.i("失败了");
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onGetError() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
        mHandler.removeMessages(SHOULD_SHOW_TALK_ANIM);
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
