package com.neishenme.what.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.HomeNewsAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.HomeNewsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.db.dao.HomeNewsDao;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;

import org.seny.android.utils.ActivityUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/15 13:23
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 * <p>
 * 旧的 消息中心界面, 已弃用 ,新的消息中心界面见 @see {@link HomeNewsActivity}
 */
@Deprecated
public class HomeNewsOldActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private RelativeLayout mRlHomenewsHavemessage;
    private TextView mTvNewsMsg;
    private TextView mTvHomenewsNone;
    private ListView mLvHomeNews;
    private HomeNewsResponse homeNewsResponse;

    private HomeNewsAdapter mHomeNewsAdapter;

    private HomeNewsDao dao;

    @Override
    protected int initContentView() {
        return R.layout.activity_home_news_old;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);
        mRlHomenewsHavemessage = (RelativeLayout) findViewById(R.id.rl_homenews_havemessage);

        mTvNewsMsg = (TextView) findViewById(R.id.tv_news_msg);
        mTvHomenewsNone = (TextView) findViewById(R.id.tv_homenews_none);

        mLvHomeNews = (ListView) findViewById(R.id.lv_home_news);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (App.SP.getBoolean("haveNewMessage", false)) {   //这里已经改动 haveNewMessage 已经被替换, 需要使用 HomeNewsActivity.FLAG_HAVE_MESSAGE_FRIENDS
            mTvNewsMsg.setVisibility(View.VISIBLE);
        } else {
            mTvNewsMsg.setVisibility(View.INVISIBLE);
        }
        if (mHomeNewsAdapter != null) {
            mHomeNewsAdapter.setHomeRes(dao.findAllHN());
            mHomeNewsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRlHomenewsHavemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.EDIT.remove("haveNewMessage").commit();
                ActivityUtils.startActivity(HomeNewsOldActivity.this, FriendsNewsActivity.class);
            }
        });

//        mLvHomeNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HomeNewsResponse.DataEntity.MessagesEntity messagesEntity = homeNewsResponse.getData().getMessages().get(position);
//                int userId = messagesEntity.getId();
//                String inviteId = getInvitedId(messagesEntity);
//                if (!TextUtils.isEmpty(inviteId)) {
//                    if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
//                        ActivityUtils.startActivityForData(HomeNewsActivity.this,
//                                InviteInviterDetailActivity.class, String.valueOf(inviteId));
//                    } else {
//                        ALog.i("不是自己的,跳入加单者的邀请详情界面");
//                        ActivityUtils.startActivityForData(HomeNewsActivity.this,
//                                InviteJoinerDetailActivity.class, String.valueOf(inviteId));
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setText("消息中心");

        getHomeNews();
        dao = HomeNewsDao.getInstance(this);
    }

    private void getHomeNews() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_USER_HOME_NEWS, params, HomeNewsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_HOME_NEWS, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_HOME_NEWS
                && response instanceof HomeNewsResponse) {
            homeNewsResponse = (HomeNewsResponse) response;
            if (1 == homeNewsResponse.getCode()) {
                if (homeNewsResponse.getData().getMessages() == null ||
                        homeNewsResponse.getData().getMessages().size() == 0) {
                    if (dao.hasInfo()) {
                        setInfo();
                    } else {
                        mTvHomenewsNone.setVisibility(View.VISIBLE);
                    }
                } else {
                    mTvHomenewsNone.setVisibility(View.GONE);
                    addToDao(homeNewsResponse.getData().getMessages());
                    setInfo();
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void setInfo() {
//        mHomeNewsAdapter = new HomeNewsAdapter(this, dao.findAllHN());
//        mLvHomeNews.setAdapter(mHomeNewsAdapter);
    }

    private void addToDao(List<HomeNewsResponse.DataEntity.MessagesEntity> messages) {
        Collections.reverse(messages);
        for (HomeNewsResponse.DataEntity.MessagesEntity message : messages) {
            dao.add(String.valueOf(message.getId()), message.getContext(),
                    String.valueOf(message.getData().getInviteId()), String.valueOf(message.getData().getJoinerId()),
                    String.valueOf(message.getData().getUserId()), String.valueOf(message.getUpdateTime()),
                    String.valueOf(message.getType()), message.getData().getLink());
        }
    }

    public void lookInfoForId(String id, int position) {
        if (dao.delete(id)) {
//            mHomeNewsAdapter.removeInfo(position);
            mHomeNewsAdapter.notifyDataSetChanged();
        }
    }
}
