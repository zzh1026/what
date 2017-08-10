package com.neishenme.what.huanxinchat.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.EasyUtils;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.RegestUserInfoActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.dialog.FillOrderDialog;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.fragment.ReleaseNormalFragment;
import com.neishenme.what.huanxinchat.NSMHXHelper;
import com.neishenme.what.huanxinchat.domain.Constant;
import com.neishenme.what.utils.MPermissionManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.DensityUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

public class ChatActivity extends BaseActivity {
    private ChatFragment chatFragment;
    private ImageView mIvActionbarLeft;
    private ImageView mIvUserDetail;
    private TextView mTvActionbarMiddle;
    private ImageView mIvChatMenu;
    private EaseUser myEaseUser;
    private EaseUser chatEaseUser;
    private RelativeLayout mMainTitlebar;
    private PopupWindow menuPop;
    private TitleAdapter mAdapter;

    public static String userLogo = "";
    public static String myLogo = "";

    private ChatInfoBean stickyEvent;

    String toChatUsername;

    private String userName;
    private int userId;

    private String myId;
    private String myName;
    private IBinder token;

    private String[] strings = {"Ta的资料", "我的昵称", "Ta的昵称", "我的资料"};

    @Override
    protected int initContentView() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        mIvUserDetail = (ImageView) findViewById(R.id.iv_user_detail);
        mIvChatMenu = (ImageView) findViewById(R.id.iv_chat_menu);
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);
        mMainTitlebar = (RelativeLayout) findViewById(R.id.main_titlebar);
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvChatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIvUserDetail.getVisibility() == View.VISIBLE) {
                    mIvUserDetail.setVisibility(View.GONE);
                } else {
                    mIvUserDetail.setVisibility(View.VISIBLE);
                }

                //showMenuList();
            }
        });

        mIvUserDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvUserDetail.setVisibility(View.GONE);
                if (userId != 0) {
                    UserDetailActivity.startUserDetailAct(ChatActivity.this, userId, false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //stickyEvent = EventBus.getDefault().getStickyEvent(MyFriendsResponse.DataEntity.FriendsEntity.class);
        stickyEvent = EventBus.getDefault().getStickyEvent(ChatInfoBean.class);
        if (null == stickyEvent) {
            showToastWarning("聊天发生错误");
            finish();
            return;
        }

        try {
            userLogo = stickyEvent.userLogo;
        } catch (Exception e) {
            userLogo = "";
        }
        try {
            userId = Integer.parseInt(stickyEvent.userId);
        } catch (Exception e) {
            userId = 0;
        }
        try {
            userName = stickyEvent.userName;
        } catch (Exception e) {
            userName = "好友";
        }

        myLogo = App.USERSP.getString("logo", "").trim();
        myId = App.USERSP.getString("userId", "").trim();
        myName = App.USERSP.getString("name", "").trim();

        mTvActionbarMiddle.setText(userName);

        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new ChatFragment();

        if (toChatUsername.equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER) || userName.equals(Constant.SEND_USER_NSM_NAME)) {
            mIvChatMenu.setVisibility(View.INVISIBLE);
        } else {
            mIvChatMenu.setVisibility(View.VISIBLE);
        }

        chatEaseUser = new EaseUser(toChatUsername);

        //传入参数
        String myHXUserName = App.USERSP.getString("hxUserName", "");
        myEaseUser = new EaseUser(myHXUserName);

        getUserInfo();
        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO:
                    AndPermission.rationaleDialog(ChatActivity.this, rationale).show();
                    break;
            }
        }
    };

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO) {
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO) {
                MPermissionManager.showToSetting(ChatActivity.this,
                        MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO, new MPermissionManager.OnNegativeListner() {
                            @Override
                            public void onNegative() {
                                finish();
                            }
                        });
            }
        }
    };

    @Override
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        int[] l = {0, 0};
        mIvActionbarLeft.getLocationInWindow(l);
        float left = l[0],
                top = l[1],
                bottom = top + mIvActionbarLeft.getHeight(),
                right = left + mIvActionbarLeft.getWidth();
        if (event.getX() >= left && event.getX() <= right
                && event.getY() >= top && event.getY() <= bottom) {
            return true;
        } else {
            return false;
        }
    }

    private void getUserInfo() {
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                if (username.equals(App.USERSP.getString("hxUserName", ""))) {
                    return getMyInfo(username);
                } else {
                    return getChatInfo(username);
                }
            }
        });
    }

    private void showMenuList() {
//        View view = View.inflate(this, R.layout.popwindow_chat_menu, null);
//        menuPop = new PopupWindow(view, view.getMeasuredWidth(), view.getMeasuredHeight());

        ListView mListView = initListView();
        menuPop = new PopupWindow(mListView, mMainTitlebar.getWidth() / 3, ReleaseNormalFragment.getTotalHeightofListView(mListView));

        // 设置可以使用焦点
        menuPop.setFocusable(true);

        menuPop.setOutsideTouchable(true);
        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.showAsDropDown(mMainTitlebar, 0, 0, Gravity.END);
    }

    private ListView initListView() {
        ListView mListView = new ListView(this);
        mListView.setDividerHeight(1);
        mListView.setBackgroundColor(Color.parseColor("#99000000"));
        // 去掉右侧垂直滑动条
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuPop.dismiss();
            }
        });

        // 设置适配器展示数据
        if (mAdapter == null) {
            mAdapter = new TitleAdapter();
        }
        mListView.setAdapter(mAdapter);
        return mListView;
    }

    class TitleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return strings.length;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ChatActivity.this, R.layout.item_fill_order_title, null);
            TextView tvListviewItemTitle = (TextView) view.findViewById(R.id.tv_listview_item_title);
            tvListviewItemTitle.setGravity(Gravity.CENTER);
            tvListviewItemTitle.setPadding(DensityUtil.dip2px(ChatActivity.this, 8),
                    DensityUtil.dip2px(ChatActivity.this, 10),
                    DensityUtil.dip2px(ChatActivity.this, 8),
                    DensityUtil.dip2px(ChatActivity.this, 10));
            tvListviewItemTitle.setTextSize(13);
            tvListviewItemTitle.setText(strings[position]);
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    public String getSendUserName() {
        return myName;
    }

    public String getSendUserLogo() {
        return myLogo;
    }

    public String getSendUserId() {
        return myId;
    }

    @Override
    protected void onStop() {
        chatFragment.getEaseChatInputMenu().onBackPressed();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 进入聊天界面重置通知栏消息数量
        NSMHXHelper.getInstance().getNotifier().reset();
    }

    @Override
    protected void onStart() {
        chatFragment.hideTitleBar();
        super.onStart();
        // hideTitleBar必须在onStart方法调用，因为EaseChatFragment的titileBar是在onActivityCreate方法里初始化的
        chatFragment.hideTitleBar();

        if (!AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
            AndPermission.with(this)
                    .requestCode(MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO)
                    .permission(Manifest.permission.CAMERA)
                    .rationale(mRationaleListener)
                    .send();
        }
    }

    @Override
    protected void onDestroy() {
        App.EDIT.remove(toChatUsername).commit();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    public EaseUser getMyInfo(String username) {
        myEaseUser.setAvatar(myLogo);
        return myEaseUser;
    }

    public EaseUser getChatInfo(String toChatUsername) {
        chatEaseUser.setAvatar(userLogo);
        return chatEaseUser;
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }
}
