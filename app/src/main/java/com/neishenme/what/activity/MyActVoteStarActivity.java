package com.neishenme.what.activity;

import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.MyVoteStarAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.StarMyVoteResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.RadiusImageViewFour;
import com.neishenme.what.view.refreshview.CustomRefreshLayout;

import java.util.HashMap;

/**
 * 网约女明星活动我的投票界面
 * 这个类的作用 :
 * <p>
 * Created by zhaozh on 2016/12/22.
 */
public class MyActVoteStarActivity extends BaseActivity implements HttpLoader.ResponseListener {

    private ImageView mMyVoteBack;
    private RadiusImageViewFour mMyVoteUserHeader;
    private TextView mMyVoteUserName;
    private TextView mMyVoteUserVoteNumber;
    private CustomRefreshLayout mMyVoteRefreshLayout;
    private RecyclerView mMyVoteRecycler;
    private LinearLayout mMyVoteNoVote;
    private LinearLayout mMyVoteStarContentLayout;

    private MyVoteStarAdapter mAdapter;
//    private int mCurrentPage = 1;
//    private boolean hasMoreInfo = false;

    @Override
    protected int initContentView() {
        return R.layout.activity_my_vote_star;
    }

    @Override
    protected void initView() {
        mMyVoteBack = (ImageView) findViewById(R.id.my_vote_back);
        mMyVoteStarContentLayout = (LinearLayout) findViewById(R.id.my_vote_star_content_layout);

        mMyVoteUserHeader = (RadiusImageViewFour) findViewById(R.id.my_vote_user_header);
        mMyVoteUserName = (TextView) findViewById(R.id.my_vote_user_name);
        mMyVoteUserVoteNumber = (TextView) findViewById(R.id.my_vote_user_vote_number);

        mMyVoteRefreshLayout = (CustomRefreshLayout) findViewById(R.id.my_vote_refresh_layout);
        mMyVoteRecycler = (RecyclerView) findViewById(R.id.my_vote_recycler);

        mMyVoteNoVote = (LinearLayout) findViewById(R.id.my_vote_no_vote);
    }

    @Override
    protected void initListener() {
        mMyVoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMyVoteNoVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initData() {
        getStarMyVote();
    }

    //获取我的投票信息
    private void getStarMyVote() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
//        params.put("page", String.valueOf(mCurrentPage));
//        params.put("pageSize", "10");
        HttpLoader.get(ConstantsWhatNSM.URL_GET_STAR_MY_VOTE, params, StarMyVoteResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_STAR_MY_VOTE, this, false).setTag(this);
    }

    private SpannableStringBuilder getVoteText(String chchangeText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(chchangeText);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.parseColor("#fa5252"));
        builder.setSpan(whiteSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.insert(0, "累计已投 ");
        builder.append(" 票");
        return builder;
    }

    private void disPathMyInfo(StarMyVoteResponse.DataBean.CountTicketedBean countTicketed) {
        String userlogo = countTicketed.getUserlogo();
        if (!TextUtils.isEmpty(userlogo)) {
            HttpLoader.getImageLoader().get(userlogo, ImageLoader.getImageListener(
                    mMyVoteUserHeader, R.drawable.my_vote_my_header, R.drawable.my_vote_my_header));
        } else {
            mMyVoteUserHeader.setImageResource(R.drawable.my_vote_my_header);
        }
        mMyVoteUserName.setText(countTicketed.getUsername());
        mMyVoteUserVoteNumber.setText(getVoteText(String.valueOf(countTicketed.getTickets())));
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_STAR_MY_VOTE
                && response instanceof StarMyVoteResponse) {
            StarMyVoteResponse myVoteResponse = (StarMyVoteResponse) response;
            if (myVoteResponse.getCode() == 1) {
                StarMyVoteResponse.DataBean dataBean = myVoteResponse.getData();

                if (dataBean.getListSuperStarsTicketed() == null
                        || dataBean.getListSuperStarsTicketed().size() == 0) {
                    mMyVoteNoVote.setVisibility(View.VISIBLE);
                    mMyVoteStarContentLayout.setVisibility(View.INVISIBLE);
                    return;
                }
                mMyVoteNoVote.setVisibility(View.INVISIBLE);
                mMyVoteStarContentLayout.setVisibility(View.VISIBLE);

                mMyVoteRecycler.setLayoutManager(new LinearLayoutManager(this));
                mAdapter = new MyVoteStarAdapter(this, dataBean.getListSuperStarsTicketed());
                mMyVoteRecycler.setAdapter(mAdapter);
                DividerItemDecoration dividerItemDecoration =
                        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                dividerItemDecoration.setDrawable(getDrawable(R.drawable.my_vote_list_line_bg));
                mMyVoteRecycler.addItemDecoration(dividerItemDecoration);

                disPathMyInfo(dataBean.getCountTicketed());
            } else {
                showToast(myVoteResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToast("网络连接失败");
    }
}
