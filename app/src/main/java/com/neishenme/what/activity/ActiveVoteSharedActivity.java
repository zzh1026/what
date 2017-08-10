package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.adapter.VoteSharedAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.GetVoteRankResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.dialog.ActiveApplySharedDialog;
import com.neishenme.what.dialog.ActiveDateStarLuckyDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.SharedDataCallback;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.RadiusImageViewFour;
import com.neishenme.what.view.refreshview.CustomRefreshLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;

/**
 * 这个类的作用是  网约女明星 付款成功后的分享界面
 * <p>
 * Created by zhaozh on 2017/4/19.
 */

public class ActiveVoteSharedActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener, SharedDataCallback {
    private static final String VOTE_SHARED_TRID_ID = "tradeId";

    private CustomRefreshLayout mVoteSharedRefreshLayout;
    private ImageView mVoteSharedStarImage;
    private TextView mVoteSharedMyPaiming;
    private TextView mVoteSharedUpNum;
    private RadiusImageViewFour mVoteSharedMyHeader;
    private RecyclerView mVoteSharedAllUser;
    private ImageView mVoteSharedBack;
    private ImageView mVoteSharedShare;

    private GetVoteRankResponse.DataBean.ActivityShareDOBean mActivityShareDO;
    private ActiveApplySharedDialog mActiveApplySharedDialog;


    private VoteSharedAdapter mAdapter;

    private String mTridId; //标记tridId的值
    boolean b = true;

    @Override
    protected int initContentView() {
        return R.layout.activity_active_vote_shared;
    }

    @Override
    protected void initView() {
        mVoteSharedRefreshLayout = (CustomRefreshLayout) findViewById(R.id.vote_shared_refresh_layout);
        mVoteSharedStarImage = (ImageView) findViewById(R.id.vote_shared_star_image);
        mVoteSharedMyPaiming = (TextView) findViewById(R.id.vote_shared_my_paiming);
        mVoteSharedUpNum = (TextView) findViewById(R.id.vote_shared_up_num);
        mVoteSharedMyHeader = (RadiusImageViewFour) findViewById(R.id.vote_shared_my_header);

        mVoteSharedAllUser = (RecyclerView) findViewById(R.id.vote_shared_all_user);

        mVoteSharedBack = (ImageView) findViewById(R.id.vote_shared_back);
        mVoteSharedShare = (ImageView) findViewById(R.id.vote_shared_share);
    }

    @Override
    protected void initListener() {
        mVoteSharedBack.setOnClickListener(this);
        mVoteSharedShare.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTridId = getIntent().getStringExtra(VOTE_SHARED_TRID_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (b) {
            b = false;
            getStarRankInfos();
        }
    }

    //获取分享的数据
    private void getStarRankInfos() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("tradeId", mTridId);
        HttpLoader.get(ConstantsWhatNSM.URL_GET_STAR_MY_RANK_NUMBER, params, GetVoteRankResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_STAR_MY_RANK_NUMBER, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vote_shared_back:
                finish();
                break;
            case R.id.vote_shared_share:
                if (mActivityShareDO != null) {
                    if (mActiveApplySharedDialog == null) {
                        mActiveApplySharedDialog = new ActiveApplySharedDialog(this, this);
                    }
                    if (!mActiveApplySharedDialog.isShowing()) {
                        mActiveApplySharedDialog.show();
                    }
                }
                break;
        }
    }

    private void disPathInfoData(GetVoteRankResponse.DataBean.RelatedInfoBean relatedInfo) {
        String starBgImg = relatedInfo.getSuperStarSharebgImg();
        if (!TextUtils.isEmpty(starBgImg)) {
            HttpLoader.getImageLoader().get(starBgImg, ImageLoader.getImageListener(
                    mVoteSharedStarImage,
                    R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mVoteSharedStarImage.setImageResource(R.drawable.picture_moren);
        }

        String userlogo = relatedInfo.getUserlogo();
        if (!TextUtils.isEmpty(userlogo)) {
            HttpLoader.getImageLoader().get(userlogo, ImageLoader.getImageListener(
                    mVoteSharedMyHeader,
                    R.drawable.my_vote_my_header, R.drawable.my_vote_my_header));
        } else {
            mVoteSharedMyHeader.setImageResource(R.drawable.my_vote_my_header);
        }
        mVoteSharedMyPaiming.setText(NSMTypeUtils.getDifColorText(Color.parseColor("#fa2e2e"),
                String.valueOf(relatedInfo.getRank()), "排名上升到了第 ", " 位"));
        mVoteSharedUpNum.setText("击败了 " + (int) (relatedInfo.getPercentage() * 100) + "% 的用户");
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_STAR_MY_RANK_NUMBER
                && response instanceof GetVoteRankResponse) {
            GetVoteRankResponse mVoteRankResponse = (GetVoteRankResponse) response;
            if (mVoteRankResponse.getCode() == 1) {
                GetVoteRankResponse.DataBean dataBean = mVoteRankResponse.getData();
                if (dataBean != null) {
                    if (dataBean.getPrize() != null) {
                        ActiveDateStarLuckyDialog mLuckyDialog =
                                new ActiveDateStarLuckyDialog(this, dataBean.getPrize());
                        mLuckyDialog.show();
                    }
                    if (dataBean.getRelatedInfo() != null) {
                        disPathInfoData(dataBean.getRelatedInfo());
                    }
                    if (dataBean.getListOverUserLogo() != null && dataBean.getListOverUserLogo().size() != 0) {
                        mVoteSharedAllUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        mAdapter = new VoteSharedAdapter(this, dataBean.getListOverUserLogo());
                        mVoteSharedAllUser.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
                        dividerItemDecoration.setDrawable(getDrawable(R.drawable.vote_shared_dickerview_bg));
                        mVoteSharedAllUser.addItemDecoration(dividerItemDecoration);
                    }
                    if (dataBean.getActivityShareDO() != null) {
                        mActivityShareDO = dataBean.getActivityShareDO();
                    }
                }
            } else {
                showToast(mVoteRankResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACCETP_STAR_SHARED_PRIZE
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse mResponse = (SendSuccessResponse) response;
            if (mResponse.getCode() == 1) {
                showToastInfo("领取成功");
            } else {
                showToastError(mResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    public void startShared(int shareChannel) {
        ShareAction shareAction = new ShareAction(this);
        HashMap<String, String> param = new HashMap<>();
        switch (shareChannel) {
            case SharedDataCallback.SHARE_TO_WEIXIN:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                param.put("sharechannel", "weixin");
                break;
            case SharedDataCallback.SHARE_TO_WEIXINFRIEND:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                param.put("sharechannel", "weixinpengyouquan");
                break;
            case SharedDataCallback.SHARE_TO_QQFRIEND:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                param.put("sharechannel", "qqkongjian");
                break;
            case SharedDataCallback.SHARE_TO_SINA:
                shareAction.setPlatform(SHARE_MEDIA.SINA);
                param.put("sharechannel", "xinlangweibo");
                break;
        }
        param.put("shareuserid", NSMTypeUtils.getMyUserId());
        param.put("platform", "android");
        param.put("token", NSMTypeUtils.getMyToken());
        param.put("activitySuperStarRankDetailId", String.valueOf(mActivityShareDO.getActivitySuperStarRankDetailId()));

        UMWeb web;
        if (mActivityShareDO != null) {
            if (!TextUtils.isEmpty(mActivityShareDO.getShareLink())) {
                web = new UMWeb(mActivityShareDO.getShareLink() +
                        HttpLoader.buildGetParam(param, mActivityShareDO.getShareLink()));

                if (!TextUtils.isEmpty(mActivityShareDO.getShareTitle())) {
                    web.setTitle(mActivityShareDO.getShareTitle());
                }
                if (!TextUtils.isEmpty(mActivityShareDO.getShareImage())) {
                    web.setThumb(new UMImage(this, mActivityShareDO.getShareImage()));
                }
                if (!TextUtils.isEmpty(mActivityShareDO.getShareDescribe())) {
                    web.setDescription(mActivityShareDO.getShareDescribe());
                }
                shareAction.withMedia(web);
            }
            if (!TextUtils.isEmpty(mActivityShareDO.getShareDescribe())) {
                shareAction.withText(mActivityShareDO.getShareDescribe());
            }
            shareAction.setCallback(umShareListener)
                    .share();
        } else {
            web = new UMWeb("http://www.neishenme.com");
            web.setTitle("内什么");
            web.setDescription("内什么");
            shareAction
                    .withText("来自内什么的分享")
                    .setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (mActiveApplySharedDialog != null && mActiveApplySharedDialog.isShowing()) {
                mActiveApplySharedDialog.dismiss();
            }
            showToastSuccess("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToastError("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void getAcceptPrize(int activityLotteryDetailId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("activityLotteryDetailId", String.valueOf(activityLotteryDetailId));
        HttpLoader.get(ConstantsWhatNSM.URL_ACCETP_STAR_SHARED_PRIZE, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACCETP_STAR_SHARED_PRIZE, this, false).setTag(this);
    }

    public static void openVoteSharedAct(Activity mActivity, String tradeId) {
        Intent intent = new Intent(mActivity, ActiveVoteSharedActivity.class);
        intent.putExtra(VOTE_SHARED_TRID_ID, tradeId);
        mActivity.startActivity(intent);
    }
}
