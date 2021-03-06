package com.neishenme.what.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
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
import com.neishenme.what.adapter.InviteJoinerAdapter;
import com.neishenme.what.adapter.ServicePhotoAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.ui.RestaurantHowToGoActivity;
import com.neishenme.what.baidumap.ui.SharedLocationActivity;
import com.neishenme.what.baidumap.ui.SharedLocationV5Activity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteDetailGetChatResponse;
import com.neishenme.what.bean.InviteResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RequestJoinerResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.component.InviteTalkComponent;
import com.neishenme.what.dialog.InviteJoinDialog;
import com.neishenme.what.dialog.InviteUserMissMeetingDialog;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.eventbusobj.TrideBean;
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

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.neishenme.what.utils.HuanXinUtils.MY_HX_USERNAME;

/**
 * 作者：zhaozh create on 2016/12/30 11:34
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个新的 邀请详情 加入者详情的界面
 * 因为加入者和发布这所走的逻辑完全不一样,同时界面交互也不同,所以为了减少代码量和降低耦合度将加入者和发布者分开
 * .
 * 其作用是 :
 */
public class InviteJoinerDetailActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private final int SHOULD_SHOW_TALK_ANIM = 0;
    private final int SHOULD_REQUEST_OTHER_LOCATION = 1;
    private final int REQUEST_OTHER_LOCATION_TIME = 1000 * 5;

    //扫描需要的四个参数
    private String inviteId;    //邀请id  该界面有
    private String joinerId;    //加入id  自己
    private String targetId;    //本人id  自己
    private String publisherId; //发布id  发布者,需要获取信息后可知道

    private InviteJoinerAdapter inviteJoinerAdapter;    //加入的人的适配器
    Gson gson = null;

    private RelativeLayout mInviteJoinerContaner;
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
    private TextView mInviteDetailTitleTv;
    private ImageView mInviteDetailBackIv;
    //底部显示
    private LinearLayout mInviteDetailTogetherShow;
    private Button mInviteDetailTogetherGo;
    private ImageView mInviteDetailTogetherMap;
    private TextView mInviteDetailTogetherDistence;
    //加入者加入活动
    private Button mInviteDetailJoinerJoin;

    //侧边的按钮状态
    private static final int START_NOW = 100;
    private static final int START_ADDRESS = 200;
    private static final int START_SURE_FACE = 300;
    private static final int START_SURE_FINISH = 400;
    private static final int START_IS_FINISH = 500;
    private int startBtnState = START_NOW;

    private int userId; //标记用户的id,方便进入用户信息界面
    private long time;  //标记该单的活动时间,会大量用
    private int mInviteTarget;   //邀请对象,, 加入活动的时候需要判断
    private int mInvitePublishType; //发布方式,活动会用到
    private String mUserLogo;       //用户头像,活动会用到
    private int mPublishAreaId;     //该邀请的发布城市id, 加入者状态会用到
    private boolean shouldShownTalkGuide = false;   //标记是否应该展示谈话功能引导
    private ImageView mShowGuideTalkBg;
    private InviteDetailGetChatResponse.DataBean talkedInfo;    //可以聊天的时候的聊天对象信息

    private ArrayList<String> userHeaderPhoto;

    private double mInviteLatitude;   //商家位置 ,在查看商家位置时候需要
    private double mInviteLongitude;
    private double otherLatitude = 0;   //对方位置 ,在共享位置初始化对方位置时候需要
    private double otherLongitude = 0;
    private String invitererLogo;  //发布者的头像
    private String myLogo;  //自己的头像

    private boolean isSocketSuccess = false;

    private InviteResponse.DataBean mInviteResponseData;    //邀请详情的数据

    private YoYo.AnimationComposer talkAnim;    //提示谈话的动画
    private YoYo.AnimationComposer slideInAnim;    //提示谈话的动画

    private InviteUserMissMeetingDialog missMeetingDialog;  //用户爽约的弹窗

    private boolean isFirst = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOULD_SHOW_TALK_ANIM: //应该展示聊天动画
                    talkAnim.playOn(mInviteDetailTalk);
                    mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_TALK_ANIM, 1500);
                    break;
                case SHOULD_REQUEST_OTHER_LOCATION: //应该连接socket
                    requestLoginSocket();
                    break;
            }
        }
    };
    private long chatTime;


    private MediaPlayer mediaPlayer;
    private String audioFile;
    private String videoFile;
    private ImageView iv_misic; //音乐播放图标
    private ImageView iv_video;//视频播放图标
    private LinearLayout ll_music;//音乐播放器
    private LinearLayout ll_video;//视频播放器
    private LinearLayout ll_video_all;//视频
    private LinearLayout ll_misic_all;//音乐
    private View line_video;
    private View line_misic;
    private TextView tv_misic;

    @Override
    protected int initContentView() {
        return R.layout.activity_invite_new_joiner_detail;
    }

    @Override
    protected void initView() {
        ll_video_all = (LinearLayout) findViewById(R.id.ll_video_all);
        ll_misic_all = (LinearLayout) findViewById(R.id.ll_misic_all);
        line_video = findViewById(R.id.line_video);
        line_misic = findViewById(R.id.line_misic);
        tv_misic = (TextView) findViewById(R.id.tv_misic);

        mInviteJoinerContaner = (RelativeLayout) findViewById(R.id.invite_joiner_contaner);
        mInviteDetailCscroll = (CustomScrollView) findViewById(R.id.invite_detail_cscroll);

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

        mInviteDetailTitleTv = (TextView) findViewById(R.id.invite_detail_title_tv);
        mInviteDetailBackIv = (ImageView) findViewById(R.id.invite_detail_back_iv);

        mInviteDetailTogetherShow = (LinearLayout) findViewById(R.id.invite_detail_together_show);
        mInviteDetailTogetherGo = (Button) findViewById(R.id.invite_detail_together_go);
        mInviteDetailTogetherMap = (ImageView) findViewById(R.id.invite_detail_together_map);
        mInviteDetailTogetherDistence = (TextView) findViewById(R.id.invite_detail_together_distence);

        mInviteDetailJoinerJoin = (Button) findViewById(R.id.invite_detail_joiner_join);

        ll_music = (LinearLayout) findViewById(R.id.ll_music);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        iv_misic = (ImageView) findViewById(R.id.iv_misic);
        iv_video = (ImageView) findViewById(R.id.iv_video);
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
                Intent intent1 = new Intent(InviteJoinerDetailActivity.this, PlayVideoActivity.class);
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
                ActivityUtils.startActivityForListData(InviteJoinerDetailActivity.this,
                        PhotoViewActivity.class, userHeaderPhoto, 0);
            }
        });

        mInviteDetailTogetherGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (startBtnState) {
                    case START_NOW:         //现在出发
                    case START_ADDRESS:     //确认到达
                    case START_SURE_FINISH: //确认完成
                        isSocketSuccess = false;
                        getStartNow(startBtnState);
                        break;
                    case START_SURE_FACE:   //确认见面
                        InviteDetailBean inviteDetailBean = new InviteDetailBean(inviteId, joinerId, targetId, publisherId);
                        ZXingGetActivity.startZXingAct(InviteJoinerDetailActivity.this, inviteDetailBean);
                        break;
                }
            }
        });

        mInviteDetailJoinerJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInviteTarget == 0 || mInviteTarget == NSMTypeUtils.getMyGender()) {
                    showPayType();  //展示加入方式
                } else {
                    showToastWarning("邀请对象不对");
                }
            }
        });

        mInviteDetailTogetherMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //位置共享
                SharedLocationV5Activity.startSharedLocationAct(
                        InviteJoinerDetailActivity.this, mInviteLatitude, mInviteLongitude,
                        otherLatitude, otherLongitude, invitererLogo, myLogo, inviteId);
            }
        });

        mInviteDetailMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到用户详情界面
                UserDetailActivity.startUserDetailAct(InviteJoinerDetailActivity.this, userId, false);
            }
        });

        mInviteDetailInviteAddressMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantHowToGoActivity.startRestHowToGoAct(
                        InviteJoinerDetailActivity.this, mInviteLatitude, mInviteLongitude);
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
                startActivity(new Intent(InviteJoinerDetailActivity.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, talkedInfo.getHxUserName()));
            }
        });
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
    protected void initData() {
        loadingShow();

        gson = new Gson();
        EventBus.getDefault().register(this);

        inviteId = getIntent().getStringExtra("data");
        joinerId = NSMTypeUtils.getMyUserId();
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
//        if (mInviteDetailUserIndicate != null) {
//            mInviteDetailUserIndicate.removeAllViewsInLayout();
//        }

        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
        } else {
            ALog.i("服务已经运行了!!!!!!!!!!");
        }

//        UserMissMeetingBean stickyEvent = EventBus.getDefault().getStickyEvent(UserMissMeetingBean.class);
//        if (null != stickyEvent && String.valueOf(stickyEvent.inviteId).equals(inviteId)) {
//            EventBus.getDefault().removeStickyEvent(UserMissMeetingBean.class);
//        }

        isFirst = true;
        getInviteData();

//        missMeetingDialog = new InviteUserMissMeetingDialog(this, 1, new InviteUserMissMeetingDialog.OnMissmeetingItemSelected() {
//            @Override
//            public void onMissmeetingItemSelected(int item, int type, boolean checked) {
//                ALog.i("点击了第 " + item + "个 , 选中情况为: " + checked + " 该类型为: " + type);
//            }
//        });
//
//        missMeetingDialog.show();

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getInviteData();
    }

    @Override
    protected void onPause() {
        if (missMeetingDialog != null && missMeetingDialog.isShowing()) {
            missMeetingDialog.dismiss();
        }
        tv_misic.setText("点击播放");
        super.onPause();
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

    //获取邀请详情的信息
    public void getInviteData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_DETAIL, params, InviteResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_DETAIL, this).setTag(this);
    }

    //开始支付
    private void payOrderForResult(double price, String tradeNum) {
        InviteResponse.DataBean.InviteBean invite = mInviteResponseData.getInvite();
        TrideBean trideBean = new TrideBean(false, 0, mInvitePublishType,
                mUserLogo, invite.getTitle(), time, price, tradeNum);
        PayOrderActivity.startPayOrderActForResult(this, PayOrderActivity.REQUEST_CODE_START_PAY, trideBean);
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
            userId = mInviteResponseData.getInvite().getUserId();
            if (NSMTypeUtils.getMyUserId().equals(String.valueOf(userId))) {
                showToastInfo("登录出现问题,请重新登录");
                ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
                return;
            }

            //分发界面数据
            disPathInfoData(mInviteResponseData.getInvite(), mInviteResponseData.getInviteMeets());

            //分发用户信息数据
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
            List<InviteResponse.DataBean.JoinersBean> joiners = mInviteResponseData.getJoiners();
            if (null != joiners && 0 != joiners.size()) {
                mInviteDetailJoinNumber.setText("(" + joiners.size() + "人已加入)");
                disPathJoinersData(joiners);
            } else {
                mInviteDetailJoinNumber.setText("(0人已加入)");
            }

            //刷新按钮状态
            int newstatus = mInviteResponseData.getInvite().getNewstatus();
            if (50 == newstatus || 100 == newstatus || 120 == newstatus || 150 == newstatus ||
                    180 == newstatus || 200 == newstatus) {
                refreshMyState(joiners, newstatus);
//                if ((time > System.currentTimeMillis()) || (System.currentTimeMillis() - time) <= 60 * 60 * 1000) {
//                    refreshMyState(joiners, newstatus);
//                } else {
//                    mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
//                    mInviteDetailJoinerJoin.setVisibility(View.GONE);
//                    mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
//                }
            } else {
                mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                mInviteDetailJoinerJoin.setVisibility(View.GONE);
                mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
            }

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

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN
                && response instanceof RequestJoinerResponse) {
            RequestJoinerResponse requestJoinerResponse = (RequestJoinerResponse) response;
            int code = requestJoinerResponse.getCode();
            if (1 == code) {
                //0 说明是免费加入的单子
                if (0 == requestJoinerResponse.getData().getTrade().getPayprice() || requestJoinerResponse.getData().getJoiner().getNewstatus() == 50) {
                    showToastSuccess("加入成功");
//                    if (mInviteDetailUserIndicate != null) {
//                        mInviteDetailUserIndicate.removeAllViewsInLayout();
//                    }
                    mInviteDetailJoinerJoin.setVisibility(View.INVISIBLE);
                    getInviteData();
                } else {
                    mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
                    mInviteDetailJoinerJoin.setText("正在付款");

                    //付费加入
                    RequestJoinerResponse.DataBean.TradeBean trade = requestJoinerResponse.getData().getTrade();
                    payOrderForResult(trade.getPayprice(), trade.getTradeNum());
                }
            } else {
                showToastInfo(requestJoinerResponse.getMessage());
                mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
                mInviteDetailJoinerJoin.setClickable(true);
                mInviteDetailJoinerJoin.setText("申请加入");
            }
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

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN) {
            showToastError("服务器异常,加入失败,请重试");
            mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
            mInviteDetailJoinerJoin.setClickable(true);
            mInviteDetailJoinerJoin.setText("申请加入");
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT) {
            showToastError("服务器请求失败");
            refreshmInviteUserGoBtnState(null);
        }


    }

//    private void toPayForVip(InviteResponse.DataBean.TradeBean trade) {
//        payOrderForResult(trade.getPayprice(), trade.getTradeNum());
//    }

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

        mInviteTarget = invite.getTarget();
        mInvitePublishType = invite.getType();
        mInviteDetailInviteType.setText("匹配对象" + NSMTypeUtils.getUserTarget(mInviteTarget));
//        mInviteDetailInviteType.setText(NSMTypeUtils.getPublicType(mInvitePublishType) + NSMTypeUtils.getUserTarget(mInviteTarget));
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

        chatTime = inviteMeetsBean.getChatTime();
//            if (TimeUtils.isShouldShowTalk(time)) {
        mInviteDetailTalkLayout.setVisibility(View.INVISIBLE);

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

    private void disPathJoinersData(List<InviteResponse.DataBean.JoinersBean> joiners) {
        if (inviteJoinerAdapter == null) {
            inviteJoinerAdapter = new InviteJoinerAdapter(joiners, this, mPublishAreaId);
            mInviteDetailJoinPersons.setAdapter(inviteJoinerAdapter);
        } else {
            inviteJoinerAdapter.setJoiners(joiners);
        }
    }

    private void disPathUserData(InviteResponse.DataBean.UserBean user) {
        //用户Id
        publisherId = String.valueOf(user.getId());

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

        invitererLogo = user.getThumbnailslogo();

    }

    private void disPathUserPhotoData(List<InviteResponse.DataBean.UserPhotosBean> userPhotos) {
        if (mInviteDetailUserIndicate != null) {
            mInviteDetailUserIndicate.removeAllViewsInLayout();
        }
        mInviteDetailUserPhotos.setAdapter(new ServicePhotoAdapter(InviteInviterDetailActivity.getUserPhotos(userPhotos)));
        mInviteDetailUserIndicate.setViewPage(mInviteDetailUserPhotos);
    }

    private void showPayType() {
        showBlurAll();
//        InviteDetailJoinDialog mInviteDetailJoinDialog = new InviteDetailJoinDialog(this);
//        mInviteDetailJoinDialog.show();

        InviteJoinDialog mInviteJoinDialog = new InviteJoinDialog(this);
        mInviteJoinDialog.show();
    }

    public void showBlurAll() {
//        Blurry.with(InviteJoinerDetailActivity.this).radius(10).sampling(2).async().onto(mInviteJoinerContaner);
    }

    public void hideBlurAll() {
//        Blurry.delete(mInviteJoinerContaner);
    }

    public void requestJoin(String money) {
        mInviteDetailJoinerJoin.setClickable(false);
        mInviteDetailJoinerJoin.setText("正在加入");
        HashMap<String, String> params = new HashMap<>();
        params.put("areaId", CityLocationConfig.cityLocationId + "");
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", inviteId);
        if (TextUtils.isEmpty(money)) {
            params.put("joinPrice", "0");
        } else {
            params.put("joinPrice", money);
        }
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_JOIN, params, RequestJoinerResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN, this, false).setTag(this);
    }

    private void getChatInfo() {
        if (!HuanXinUtils.isLoginToHX()) {
            return;
        }
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

    private void refreshMyState(List<InviteResponse.DataBean.JoinersBean> joiners, int newstatus) {
        if (joiners != null) {
            //用户自己的Id
            String myUserId = NSMTypeUtils.getMyUserId();
            if (TextUtils.isEmpty(myUserId)) {
                return;
            }
            //初始化状态
            mInviteDetailJoinerJoin.setVisibility(View.GONE);
            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
            for (InviteResponse.DataBean.JoinersBean joiner : joiners) {
                if (myUserId.equals(String.valueOf(joiner.getJoiner_userId()))) {
                    myLogo = joiner.getUser_thumbnailslogo();
                    switch (joiner.getJoiner_newstatus()) {    //说明自己已经加入了,判断加入状态.
                        case 50:    //会员支付
                        case 100:   //已支付
                        case 110:
                            mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                            mInviteDetailJoinerJoin.setVisibility(View.GONE);
                            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
                            break;
                        case 0: //未支付
                        case 120:   //去支付 //这个状态不会有了
//                            mInviteDetailInviteAddressMap.setVisibility(View.VISIBLE);
//                            if (120 == newstatus) {
//                                mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
//                                mInviteDetailJoinerJoin.setText("立即付款");
//                                mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
//                                joinBtnState = TO_PAY;
//                            } else {
//                                mInviteDetailJoinerJoin.setVisibility(View.GONE);
//                                mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
//                            }
                            mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                            mInviteDetailJoinerJoin.setVisibility(View.GONE);
                            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
                            break;
                        case 150:       //成单
                            if (chatTime != 0 && chatTime < System.currentTimeMillis()) {
                                getChatInfo();
                            }
                            mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                            isSocketSuccess = false;
                            requestOtherLoc();
                            mInviteDetailInviteAddressMap.setVisibility(View.VISIBLE);  //小地图展示可以查看商家位置
                            mInviteDetailJoinerJoin.setVisibility(View.INVISIBLE);
                            mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                            int setout = joiner.getJoiner_setout();
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
                        //已见面
                        case 180:
                            if (chatTime != 0 && chatTime < System.currentTimeMillis()) {
                                getChatInfo();
                            }
                            mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
                            isSocketSuccess = false;
                            requestOtherLoc();
                            startBtnState = START_SURE_FINISH;
                            mInviteDetailTogetherGo.setText("确认完成");
                            mInviteDetailTogetherGo.setClickable(true);
                            mInviteDetailInviteAddressMap.setVisibility(View.VISIBLE);
                            mInviteDetailJoinerJoin.setVisibility(View.INVISIBLE);
                            mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                            mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                            mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                            mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                            break;
                        //已完成
                        case 200:
                            startBtnState = START_IS_FINISH;
                            mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                            mInviteDetailJoinerJoin.setVisibility(View.INVISIBLE);
                            mInviteDetailTogetherShow.setVisibility(View.VISIBLE);
                            mInviteDetailTogetherGo.setVisibility(View.VISIBLE);
                            mInviteDetailTogetherGo.setClickable(false);
                            mInviteDetailTogetherGo.setText("已完成");
                            mInviteDetailTogetherMap.setVisibility(View.INVISIBLE);
                            mInviteDetailTogetherDistence.setVisibility(View.INVISIBLE);
                            break;
                        //穿透
                        case 650:   //邀请者同意其他人,自己被拒绝
                        case 700:   //邀请者主动取消
                        case 710:   //参与者主动取消
                        case 750:   //超时自动退款
                        case 900:   //删除
                        default:
                            mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
                            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
                            mInviteDetailJoinerJoin.setVisibility(View.GONE);
                            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return;
                }
            }
        }

        if (time > System.currentTimeMillis() && (newstatus == 50 || newstatus == 100)) {
            mInviteDetailInviteDistime.setVisibility(View.VISIBLE);
            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
            mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
            mInviteDetailJoinerJoin.setText("申请加入");
            mInviteDetailJoinerJoin.setClickable(true);
//        joinBtnState = TO_ADD;
            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);

            if (mInviteTarget == 0 || mInviteTarget == NSMTypeUtils.getMyGender()) {
                mInviteDetailJoinerJoin.setVisibility(View.VISIBLE);
            } else {
                mInviteDetailJoinerJoin.setVisibility(View.GONE);
            }
        } else {
            mInviteDetailInviteDistime.setVisibility(View.INVISIBLE);
            mInviteDetailInviteAddressMap.setVisibility(View.INVISIBLE);
            mInviteDetailJoinerJoin.setVisibility(View.GONE);
            mInviteDetailTogetherShow.setVisibility(View.INVISIBLE);
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
//                ALog.i("latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EditPayMoneyActivity.REQUEST_CODE_JOIN://加入
                if (resultCode == RESULT_OK) {
                    requestJoin(data.getStringExtra(EditPayMoneyActivity.PAY_MONEY_BACK_DATA));
                }
                break;
            case PayOrderActivity.REQUEST_CODE_START_PAY:   //付费加入成功
                if (requestCode == RESULT_OK) {
                    showToastSuccess("加入成功");
                    mInviteDetailJoinerJoin.setVisibility(View.INVISIBLE);
                    mInviteDetailJoinerJoin.setText("申请加入");
//                    if (mInviteDetailUserIndicate != null) {
//                        mInviteDetailUserIndicate.removeAllViewsInLayout();
//                    }
                    getInviteData();
                }
                break;
        }
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
