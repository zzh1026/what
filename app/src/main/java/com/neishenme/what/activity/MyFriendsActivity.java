package com.neishenme.what.activity;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.fragment.MyFriendsChatFragment;
import com.neishenme.what.fragment.MyFriendsContentFragment;
import com.neishenme.what.huanxinchat.NSMHXHelper;
import com.neishenme.what.utils.HuanXinUtils;

import org.seny.android.utils.ActivityUtils;


/**
 * 我的好友界面, 主要包含了我的好友和聊天功能, 进来的地方时主界面铃铛进入点击我认识的人进入
 */
public class MyFriendsActivity extends BaseActivity {
    public static final int SEARCH_DATA = 2;    //搜索认识的人

    private ImageView ivBack;
    private LinearLayout mRlSearch;

    private FrameLayout mFriendsCntener;
    private RadioGroup mMyFriendsMenu;
    private RadioButton mMyFriendsNews;
    private RadioButton mMyFriendsConnected;

    private FragmentTransaction transaction;
    private MyFriendsChatFragment myFriendsChatFragment;
    private MyFriendsContentFragment myFriendsContentFragment;


    @Override
    protected int initContentView() {
        return R.layout.activity_my_friend;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mRlSearch = (LinearLayout) findViewById(R.id.rl_search);

        mFriendsCntener = (FrameLayout) findViewById(R.id.friends_cntener);
        mMyFriendsMenu = (RadioGroup) findViewById(R.id.my_friends_menu);
        mMyFriendsNews = (RadioButton) findViewById(R.id.my_friends_news);
        mMyFriendsConnected = (RadioButton) findViewById(R.id.my_friends_connected);
    }

    @Override
    protected void initListener() {
        mRlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivityForResult(MyFriendsActivity.this, SearchRecActivity.class, SEARCH_DATA);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMyFriendsMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = getFragmentManager().beginTransaction();
                switch (checkedId) {
                    case R.id.my_friends_news:
                        if (null != myFriendsChatFragment && null != myFriendsContentFragment) {
                            transaction.show(myFriendsChatFragment).hide(myFriendsContentFragment).commit();
                        }
                        break;

                    case R.id.my_friends_connected:
                        if (null != myFriendsChatFragment && null != myFriendsContentFragment) {
                            transaction.show(myFriendsContentFragment).hide(myFriendsChatFragment).commit();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (!HuanXinUtils.isLoginToHX()) {
            HuanXinUtils.login();
        }
        transaction = getFragmentManager().beginTransaction();
        if (null == myFriendsChatFragment) {
            myFriendsChatFragment = MyFriendsChatFragment.newInstance();
        }
        if (null == myFriendsContentFragment) {
            myFriendsContentFragment = MyFriendsContentFragment.newInstance();
        }
        transaction.add(R.id.friends_cntener, myFriendsChatFragment).add(R.id.friends_cntener, myFriendsContentFragment)
                .show(myFriendsChatFragment).hide(myFriendsContentFragment).commit();

        NSMHXHelper.getInstance().setOnMessageRefreshListener(new NSMHXHelper.OnMessageRefreshListener() {
            @Override
            public void onMessageRefreshListener() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myFriendsChatFragment.refresh();
                    }
                });
            }
        });
    }

    public void showContentFragment() {
        mMyFriendsConnected.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        NSMHXHelper.getInstance().removeMessageRefreshListener();
        super.onDestroy();
    }
}
