package com.neishenme.what.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.DateStarPaihangListAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.StarVoteTridResponse;
import com.neishenme.what.bean.SuperStarListResponse;
import com.neishenme.what.eventbusobj.ActivePayTrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CustomVideoView;
import com.neishenme.what.view.ListViewAdjustHeight;
import com.neishenme.what.view.refreshview.CustomRefreshLayout;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 这个类的作用是  网约女明星 活动的主界面
 * <p>
 * Created by zhaozh on 2017/4/13.
 */

public class ActiveDateStarActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener {
    public static final String DATE_STAR_TITLE = "title";
    public static final int ACTIVE_VOTE_REQUEST_CODE = 1;
    public static final int MESSAGE_MEDIA_CONTROL_DISMISS = 100;

    public static final int MESSAGE_MEDIA_CONTROL_TIME = 1000;

    private ImageView mDateStarBack;
    private TextView mDateStarMyVote;
    private CustomRefreshLayout mStarRefreshLayout;
    private CustomVideoView mDateStarVideo;
    private ListViewAdjustHeight mDateStarPaihangList;
    //    private RelativeLayout lksdjflsjdflk;
    private LinearLayout mDateStarRightLayout;
    private TextView mDateStarTitleText;

    //    private MediaController mController;
    private ImageView mDateStarVideoContrl;

    private String mDateStarTitle;
    private String mTradeId;
    private boolean mVideoViewIsPrepare = false;        //标识videoview是否已经就绪可以播放
    private String mVideoUrl;       //标识video的地址
    private DateStarPaihangListAdapter mDateStarPaihangListAdapter; //明星界面的适配器

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_MEDIA_CONTROL_DISMISS) {
                if (mDateStarVideo.isPlaying()) {
                    mDateStarVideoContrl.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_date_star;
    }

    @Override
    protected void onStart() {
        mVideoViewIsPrepare = false;
        super.onStart();
        getListStars();
    }

    @Override
    protected void onStop() {
        removeMessage();
        super.onStop();
    }

    @Override
    protected void initView() {
        mDateStarBack = (ImageView) findViewById(R.id.date_star_back);
        mDateStarMyVote = (TextView) findViewById(R.id.date_star_my_vote);
        mStarRefreshLayout = (CustomRefreshLayout) findViewById(R.id.star_refresh_layout);
        mDateStarVideo = (CustomVideoView) findViewById(R.id.date_star_video);
        mDateStarPaihangList = (ListViewAdjustHeight) findViewById(R.id.date_star_paihang_list);
//        lksdjflsjdflk = (RelativeLayout) findViewById(R.id.lksdjflsjdflk);
        mDateStarRightLayout = (LinearLayout) findViewById(R.id.date_star_right_layout);
        mDateStarVideoContrl = (ImageView) findViewById(R.id.date_star_video_contrl);
        mDateStarTitleText = (TextView) findViewById(R.id.date_star_title);
    }

    @Override
    protected void initListener() {
        mDateStarBack.setOnClickListener(this);
        mDateStarMyVote.setOnClickListener(this);
        mDateStarVideoContrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mVideoViewIsPrepare) {
                    return;
                }
                if (mDateStarVideo.isPlaying() && mDateStarVideo.canPause()) {
                    removeMessage();
                    mDateStarVideoContrl.setVisibility(View.VISIBLE);
                    mDateStarVideoContrl.setImageResource(R.drawable.date_star_video_play);
                    mDateStarVideo.pause();
                } else {
                    mDateStarVideoContrl.setImageResource(R.drawable.date_star_video_pause);
                    mDateStarVideoContrl.setVisibility(View.INVISIBLE);
                    mDateStarVideo.start();
                }
            }
        });

        mDateStarVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ALog.i("触摸了videoview");
                mDateStarVideoContrl.setVisibility(View.VISIBLE);
                removeMessage();
                if (mDateStarVideo.isPlaying()) {
                    mHandler.sendEmptyMessageDelayed(MESSAGE_MEDIA_CONTROL_DISMISS, MESSAGE_MEDIA_CONTROL_TIME);
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        mDateStarTitle = getIntent().getStringExtra(DATE_STAR_TITLE);
        mDateStarTitleText.setText(mDateStarTitle);
    }

    //获取最受欢迎明星排行数据
    private void getListStars() {
        HttpLoader.get(ConstantsWhatNSM.URL_GET_SUPER_STAR_LIST, null, SuperStarListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_SUPER_STAR_LIST, this, false).setTag(this);
    }

    private void mesureRightView() {
        mDateStarRightLayout.removeAllViews();
        int i = mDateStarPaihangList.getMeasuredHeight();
        int i1 = i / DensityUtil.dip2px(ActiveDateStarActivity.this, 230f);
        if (i1 > 0) {
            for (int y = 1; y <= i1; y++) {
                mDateStarRightLayout.addView(LayoutInflater.from(this).inflate(R.layout.date_star_right_bg, null));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_star_back:
                finish();
                break;
            case R.id.date_star_my_vote:
                if (NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(this, MyActVoteStarActivity.class);
                } else {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                }
                break;
        }
    }

    //进行投票
    public void subMitTicketNum(int voteStarId, int ticketNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("superStarId", String.valueOf(voteStarId));
        params.put("tickets", String.valueOf(ticketNumber));
        HttpLoader.get(ConstantsWhatNSM.URL_VOTE_SUPER_STAR, params, StarVoteTridResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_VOTE_SUPER_STAR, this, false).setTag(this);
    }

    private void disPathVideoView() {
        if (TextUtils.isEmpty(mVideoUrl)) {
            return;
        }
        mDateStarVideo.setVideoURI(Uri.parse(mVideoUrl));
        mDateStarVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ALog.i("video 准备完毕");
                mVideoViewIsPrepare = true;
                resetMediaContrl();
            }
        });
        mDateStarVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ALog.i("video 播放完毕");
                resetMediaContrl();
            }
        });
//        if (mController == null) {
//            mController = new MediaController(this);
//            mController.setMediaPlayer(mDateStarVideo);
//        }
//        mDateStarVideo.setMediaController(mController);
    }

    private void resetMediaContrl() {
        mDateStarVideoContrl.setVisibility(View.VISIBLE);
        mDateStarVideoContrl.setImageResource(R.drawable.date_star_video_play);
        mDateStarVideo.seekTo(1);
    }

    private void removeMessage() {
        mHandler.removeMessages(MESSAGE_MEDIA_CONTROL_DISMISS);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_SUPER_STAR_LIST
                && response instanceof SuperStarListResponse) {
            SuperStarListResponse mStarListResponse = (SuperStarListResponse) response;
            if (mStarListResponse.getCode() == 1) {
                SuperStarListResponse.DataBean data = mStarListResponse.getData();
                if (data != null && data.getListStars() != null) {
                    List<SuperStarListResponse.DataBean.ListStarsBean> listStars = data.getListStars();
                    if (mDateStarPaihangListAdapter == null) {
                        mDateStarPaihangListAdapter = new DateStarPaihangListAdapter(this, listStars);
                        mDateStarPaihangList.setAdapter(mDateStarPaihangListAdapter);
                    } else {
                        mDateStarPaihangListAdapter.changeData(listStars);
                    }
                    mDateStarPaihangList.post(new Runnable() {
                        @Override
                        public void run() {
                            mesureRightView();
                        }
                    });

                    if (TextUtils.isEmpty(mVideoUrl) || !data.getVideoFile().equals(mVideoUrl)) {
                        mVideoUrl = data.getVideoFile();
                        disPathVideoView();
                    }
                }
            } else {
                showToast(mStarListResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_VOTE_SUPER_STAR
                && response instanceof StarVoteTridResponse) {
            StarVoteTridResponse mVoteTridResponse = (StarVoteTridResponse) response;
            if (mVoteTridResponse.getCode() == 1) {
                StarVoteTridResponse.DataBean dataBean = mVoteTridResponse.getData();
                StarVoteTridResponse.DataBean.TradeBean tradeInfo = dataBean.getTrade();
                ActivePayTrideBean mActivePayTrideBean = new ActivePayTrideBean(dataBean.getTitle(),
                        dataBean.getStarLogo(), dataBean.getStarName(),
                        tradeInfo.getPayprice(), tradeInfo.getJobType(),
                        tradeInfo.getTradeNum(), dataBean.getTickets());
                mTradeId = tradeInfo.getId();
                ActiveJoinPayActivity.startActivePayForResult(this, mActivePayTrideBean, ACTIVE_VOTE_REQUEST_CODE);
            } else {
                showToast(mVoteTridResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVE_VOTE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showToastSuccess("投票成功");
                ActiveVoteSharedActivity.openVoteSharedAct(this, mTradeId);
            }
        }
    }
}
