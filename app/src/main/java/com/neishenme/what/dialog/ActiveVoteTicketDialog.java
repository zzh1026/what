package com.neishenme.what.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveDateStarActivity;
import com.neishenme.what.bean.ActiveJoinerTrideResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnVoteTicketSubListener;
import com.neishenme.what.utils.ConstantsWhatNSM;

import org.seny.android.utils.ALog;

/**
 * 作者：zhaozh create on 2017/4/17 16:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 约会 投票的dialog , 和takemeout活动的dialog类似,但是该dialog对方法进行了抽取, 进行接口回调的方法来进行
 * .
 * 其作用是 :
 */
public class ActiveVoteTicketDialog extends BaseDialog implements View.OnClickListener, HttpLoader.ResponseListener {
    private ActiveDateStarActivity mContext;
    private static final int DEFAULT_TICKET_NUM = 100;
    private int mEditText = DEFAULT_TICKET_NUM;

    private int mStarId;
    private String mStarLogo;
    private String mStarName;
    private String mStarType;

    private ImageView mTicketCancle;
    private ImageView mDialogTicketUserLogo;
    private TextView mTicketSubUserName;
    private ImageView mActiveDialogLess;
    private EditText mActiveDialogSubNum;
    private ImageView mActiveDialogAdd;
    private TextView mActiveDialogSub;

    public ActiveVoteTicketDialog(Activity mContext, String starLogo, int superStarId,
                                  String starName, String starType) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        setContentView(R.layout.dialog_vote_ticket_active);
        this.mContext = (ActiveDateStarActivity) mContext;
        this.mStarId = superStarId;
        this.mStarLogo = starLogo;
        this.mStarName = starName;
        this.mStarType = starType;
        initView();
        initListener();
        initData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView() {
        mTicketCancle = (ImageView) findViewById(R.id.ticket_cancle);
        mDialogTicketUserLogo = (ImageView) findViewById(R.id.dialog_ticket_user_logo);
        mTicketSubUserName = (TextView) findViewById(R.id.ticket_sub_user_name);
        mActiveDialogLess = (ImageView) findViewById(R.id.active_dialog_less);
        mActiveDialogSubNum = (EditText) findViewById(R.id.active_dialog_sub_num);
        mActiveDialogAdd = (ImageView) findViewById(R.id.active_dialog_add);
        mActiveDialogSub = (TextView) findViewById(R.id.active_dialog_sub);
    }

    private void initListener() {
        mTicketCancle.setOnClickListener(this);

        mActiveDialogAdd.setOnClickListener(this);

        mActiveDialogLess.setOnClickListener(this);

        mActiveDialogSub.setOnClickListener(this);

        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {
                    //软键盘弹起
                    ALog.i("window监听到软键盘弹起");
                    mActiveDialogSubNum.setCursorVisible(true);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 0)) {
                    //软件盘关闭
                    mActiveDialogSubNum.setCursorVisible(false);
                }
            }
        });
    }

    private void initData() {
        clearTextFocus();
        if (TextUtils.isEmpty(mStarLogo)) {
            mDialogTicketUserLogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(mStarLogo, ImageLoader.getImageListener(
                    mDialogTicketUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        mActiveDialogSubNum.setText(String.valueOf(mEditText));

        mTicketSubUserName.setText(mStarType + ": " + mStarName);
    }

    private void clearTextFocus() {
        closeSoftInput(mContext);
        mActiveDialogSubNum.setFocusable(false);
        mActiveDialogSubNum.clearFocus();
        mActiveDialogSubNum.setFocusableInTouchMode(true);
    }

    private void requestTextFocus() {
        mActiveDialogSubNum.setFocusable(true);
        mActiveDialogSubNum.setFocusableInTouchMode(true);
        mActiveDialogSubNum.requestFocus();
    }

    public void closeSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActiveDialogSubNum.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        clearTextFocus();
        switch (v.getId()) {
            case R.id.active_dialog_sub:
                trySubMit();
                break;
            case R.id.active_dialog_less:
                setEditLess();
                break;
            case R.id.active_dialog_add:
                setEditAdd();
                break;
            case R.id.ticket_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }

    private int getEditText() {
        try {
            return Integer.parseInt(mActiveDialogSubNum.getText().toString().trim());
        } catch (Exception e) {
            return DEFAULT_TICKET_NUM;
        }
    }

    private void trySubMit() {
        int number;
        try {
            number = Integer.parseInt(mActiveDialogSubNum.getText().toString().trim());
            if (number == 0) {
                mContext.showToastError("提交失败,请输入正确数字!");
                mEditText = DEFAULT_TICKET_NUM;
                mActiveDialogSubNum.setText(String.valueOf(mEditText));
                return;
            }
            mEditText = number;
            //确认数量
            subMitTicketNum();
        } catch (Exception e) {
            mContext.showToastError("提交失败,请输入正确数字!");
            mEditText = DEFAULT_TICKET_NUM;
            mActiveDialogSubNum.setText(String.valueOf(mEditText));
        }
    }

    private void subMitTicketNum() {
        mContext.subMitTicketNum(mStarId, mEditText);
        dismiss();
    }

    private void setEditAdd() {
        mEditText = getEditText();
        mEditText += 10;
        mActiveDialogSubNum.setText(String.valueOf(mEditText));
    }

    private void setEditLess() {
        mEditText = getEditText();
        if (mEditText > 10) {
            mEditText -= 10;
            mActiveDialogSubNum.setText(String.valueOf(mEditText));
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACK_JOIN
                && response instanceof ActiveJoinerTrideResponse) {
//            ActiveJoinerTrideResponse activeJoinerTrideResponse = (ActiveJoinerTrideResponse) response;
//            if (activeJoinerTrideResponse.getCode() == 1) {
//                ActiveJoinerTrideResponse.DataBean dataBean = activeJoinerTrideResponse.getData();
//                ActivePayTrideBean mActivePayTrideBean = new ActivePayTrideBean("TakeMeOut",
//                        takemeoutBean.getThumbnailslogo(), takemeoutBean.getName(),
//                        dataBean.getTrade().getPrice(), dataBean.getTrade().getJobType(),
//                        dataBean.getTrade().getTradeNum(), mEditText);
//                ActiveJoinPayActivity.startActiveJoinPayAct(mContext, mActivePayTrideBean);
//                dismiss();
//            } else {
//                mContext.showToastInfo(activeJoinerTrideResponse.getMessage());
//            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
