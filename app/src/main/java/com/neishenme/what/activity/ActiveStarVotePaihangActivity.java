package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.StarVotePaihangListAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.StarVoteDetailResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.ListViewAdjustHeight;
import com.neishenme.what.view.refreshview.CustomRefreshLayout;

import java.util.HashMap;

/**
 * 这个类的作用是  网约女明星 明星投票的排行榜
 * <p>
 * Created by zhaozh on 2017/4/13.
 */

public class ActiveStarVotePaihangActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener {
    private static final String SUPER_STAR_ID = "superId";

    private CustomRefreshLayout mStarVoteRefreshLayout;
    private CustomScrollView mStarVoteScrollView;
    private ImageView mStarVoteStarBg;
    private TextView mStarVoteStarName;
    private TextView mStarVoteStarVote;
    private ListViewAdjustHeight mStarVoteVoteList;
    private CircleImageView mStarVoteStarHeader;
    private View mStarVoteTitleBg;
    private ImageView mStarVoteBack;
    private TextView mStarVoteMyRankingNum;
    private ImageView mStarVoteMyHeader;
    private TextView mStarVoteMyName;
    private TextView mStarVoteMyVoteNum;
    private LinearLayout mStarVoteDetailMyInfo;

    private int mStarId;    //明星的标识id

    @Override
    protected int initContentView() {
        return R.layout.activity_star_vote_paihang;
    }

    @Override
    protected void initView() {
        mStarVoteRefreshLayout = (CustomRefreshLayout) findViewById(R.id.star_vote_refresh_layout);
        mStarVoteScrollView = (CustomScrollView) findViewById(R.id.star_vote_scroll_view);

        mStarVoteStarHeader = (CircleImageView) findViewById(R.id.star_vote_star_header);
        mStarVoteStarBg = (ImageView) findViewById(R.id.star_vote_star_bg);
        mStarVoteStarName = (TextView) findViewById(R.id.star_vote_star_name);
        mStarVoteStarVote = (TextView) findViewById(R.id.star_vote_star_vote);

        mStarVoteVoteList = (ListViewAdjustHeight) findViewById(R.id.star_vote_vote_list);

        mStarVoteTitleBg = findViewById(R.id.star_vote_title_bg);
        mStarVoteBack = (ImageView) findViewById(R.id.star_vote_back);

        mStarVoteMyRankingNum = (TextView) findViewById(R.id.star_vote_my_ranking_num);
        mStarVoteMyHeader = (ImageView) findViewById(R.id.star_vote_my_header);
        mStarVoteMyName = (TextView) findViewById(R.id.star_vote_my_name);
        mStarVoteMyVoteNum = (TextView) findViewById(R.id.star_vote_my_vote_num);

        mStarVoteDetailMyInfo = (LinearLayout) findViewById(R.id.star_vote_detail_my_info);
    }

    @Override
    protected void initListener() {
        mStarVoteBack.setOnClickListener(this);

        mStarVoteScrollView.setScrollYChangedListener(new CustomScrollView.ScrollYChangedListener() {
            @Override
            public void scrollYChange(int y) {
                if (y < 0)
                    y = 0;
                if ((int) (y * (0.005)) >= 1) {
                    mStarVoteTitleBg.setVisibility(View.VISIBLE);
                } else {
                    mStarVoteTitleBg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mStarId = getIntent().getIntExtra(SUPER_STAR_ID, 0);
        getStarVoteDetail();
    }

    private void getStarVoteDetail() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("superStarId", String.valueOf(mStarId));
        HttpLoader.get(ConstantsWhatNSM.URL_SUPER_STAR_VOTE_DETAIL, params, StarVoteDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SUPER_STAR_VOTE_DETAIL, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star_vote_back:
                finish();
                break;
        }
    }

    //展示明星的信息
    private void disPathStarInfo(StarVoteDetailResponse.DataBean.ActivitySuperStarDOBean activitySuperStarDO) {
        String starLogo = activitySuperStarDO.getLogo();
        if (!TextUtils.isEmpty(starLogo)) {
            HttpLoader.getImageLoader().get(starLogo, ImageLoader.getImageListener(
                    mStarVoteStarHeader,
                    R.drawable.star_vote_star_header, R.drawable.star_vote_star_header));
        } else {
            mStarVoteStarHeader.setImageResource(R.drawable.star_vote_star_header);
        }
        String starBackground = activitySuperStarDO.getBackgroudImg();
        if (!TextUtils.isEmpty(starBackground)) {
            HttpLoader.getImageLoader().get(starBackground, ImageLoader.getImageListener(
                    mStarVoteStarBg,
                    R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mStarVoteStarBg.setImageResource(R.drawable.picture_moren);
        }
        mStarVoteStarName.setText(activitySuperStarDO.getName());
        mStarVoteStarVote.setText("当前票数: " + activitySuperStarDO.getTicket());
    }

    //展示我的信息
    private void disPathMyInfo(StarVoteDetailResponse.DataBean.UserTicketRankBean userTicketRank) {
        String myLogo = userTicketRank.getUserlogo();
        if (!TextUtils.isEmpty(myLogo)) {
            HttpLoader.getImageLoader().get(myLogo, ImageLoader.getImageListener(
                    mStarVoteMyHeader,
                    R.drawable.star_vote_user_header, R.drawable.star_vote_user_header));
        } else {
            mStarVoteMyHeader.setImageResource(R.drawable.star_vote_user_header);
        }
        mStarVoteMyRankingNum.setText(userTicketRank.getRowNo() + "");
        mStarVoteMyName.setText(userTicketRank.getName());
        mStarVoteMyVoteNum.setText(userTicketRank.getUserTickets() + "");
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SUPER_STAR_VOTE_DETAIL
                && response instanceof StarVoteDetailResponse) {
            StarVoteDetailResponse mVoteResponse = (StarVoteDetailResponse) response;
            if (mVoteResponse.getCode() == 1) {
                StarVoteDetailResponse.DataBean dataBean = mVoteResponse.getData();
                if (dataBean != null) {
                    if (dataBean.getActivitySuperStarDO() != null) {
                        disPathStarInfo(dataBean.getActivitySuperStarDO());
                    }
                    if (dataBean.getUserTicketRank() != null) {
                        mStarVoteDetailMyInfo.setVisibility(View.VISIBLE);
                        disPathMyInfo(dataBean.getUserTicketRank());
                    } else {
                        mStarVoteDetailMyInfo.setVisibility(View.GONE);
                    }
                    if (dataBean.getListStars() != null && dataBean.getListStars().size() != 0) {
                        StarVotePaihangListAdapter mAdapter =
                                new StarVotePaihangListAdapter(this, dataBean.getListStars());
                        mStarVoteVoteList.setAdapter(mAdapter);
                    }
                }

            } else {
                showToast(mVoteResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    public static void opStarVoteDetail(Activity activity, int superStarId) {
        Intent intent = new Intent(activity, ActiveStarVotePaihangActivity.class);
        intent.putExtra(SUPER_STAR_ID, superStarId);
        activity.startActivity(intent);
    }
}
