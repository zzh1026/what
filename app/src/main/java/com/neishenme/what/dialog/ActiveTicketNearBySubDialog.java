package com.neishenme.what.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveJoinPayActivity;
import com.neishenme.what.activity.ActiveNearByActivity;
import com.neishenme.what.bean.ActiveJoinerTrideResponse;
import com.neishenme.what.bean.ActiveListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.eventbusobj.ActivePayTrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CustomScrollView;

import org.seny.android.utils.ALog;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个活动戳我约她的界面
 * .
 * 其作用是 :
 *
 *  @author Administrator
 *   2017/4/20  ,该dialog已经弃用了,因为 {@link ActiveNearByActivity} 弃用了
 */

@Deprecated
public class ActiveTicketNearBySubDialog extends BaseDialog implements View.OnClickListener, HttpLoader.ResponseListener {

    private ActiveNearByActivity mContext;
    private int mEditText = 1;
    private ActiveListResponse.DataBean.TakemeoutBean takemeoutBean;

    private CustomScrollView mTicketScroll;
    private ImageView mTicketCancle;
    private ImageView mDialogTicketUserLogo;
    private ImageView mTicketSubUserGender;
    private TextView mTicketSubUserName;
    private ImageView mActiveDialogLess;
    private EditText mActiveDialogSubNum;
    private ImageView mActiveDialogAdd;
    private TextView mActiveDialogTicketNum;
    private TextView mActiveDialogSub;

    public ActiveTicketNearBySubDialog(ActiveNearByActivity mContext, ActiveListResponse.DataBean.TakemeoutBean takemeoutBean) {
        super(mContext, Gravity.TOP, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.takemeoutBean = takemeoutBean;
        setContentView(R.layout.dialog_ticket_sub_active);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        mTicketScroll = (CustomScrollView) findViewById(R.id.ticket_scroll);
        mTicketCancle = (ImageView) findViewById(R.id.ticket_cancle);
        mDialogTicketUserLogo = (ImageView) findViewById(R.id.dialog_ticket_user_logo);
        mTicketSubUserGender = (ImageView) findViewById(R.id.ticket_sub_user_gender);
        mTicketSubUserName = (TextView) findViewById(R.id.ticket_sub_user_name);
        mActiveDialogLess = (ImageView) findViewById(R.id.active_dialog_less);
        mActiveDialogSubNum = (EditText) findViewById(R.id.active_dialog_sub_num);
        mActiveDialogAdd = (ImageView) findViewById(R.id.active_dialog_add);
        mActiveDialogTicketNum = (TextView) findViewById(R.id.active_dialog_ticket_num);
        mActiveDialogSub = (TextView) findViewById(R.id.active_dialog_sub);
    }

    private void initListener() {
        mTicketCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.dismissDialog();
            }
        });

        mActiveDialogAdd.setOnClickListener(this);

        mActiveDialogLess.setOnClickListener(this);

        mActiveDialogSub.setOnClickListener(this);

        mTicketScroll.setScrollYChangedListener(new CustomScrollView.ScrollYChangedListener() {
            @Override
            public void scrollYChange(int y) {
                if (y == 0) {
                    mActiveDialogSubNum.setCursorVisible(false);
                } else {
                    mActiveDialogSubNum.setCursorVisible(true);
                }
            }
        });

    }

    private void initData() {
        String userLogo = takemeoutBean.getLogo();
        if (TextUtils.isEmpty(userLogo)) {
            mDialogTicketUserLogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(userLogo, ImageLoader.getImageListener(
                    mDialogTicketUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        mTicketSubUserGender.setImageResource(takemeoutBean.getGender() == 1 ? R.drawable.man_icon : R.drawable.woman_icon);
        mTicketSubUserName.setText(takemeoutBean.getName());
        mActiveDialogTicketNum.setText(NSMTypeUtils.getTakeMeOutId(takemeoutBean.getId()));
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
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
            default:
                break;
        }
    }

    private int getEditText() {
        try {
            return Integer.parseInt(mActiveDialogSubNum.getText().toString().trim());
        } catch (Exception e) {
            mContext.showToastError("输入错误!请重试");
            return 1;
        }
    }

    private void trySubMit() {
        int number;
        try {
            number = Integer.parseInt(mActiveDialogSubNum.getText().toString().trim());
            if (number == 0) {
                mContext.showToastError("提交失败,请输入正确数字!");
                mEditText = 1;
                mActiveDialogSubNum.setText(mEditText + "");
                return;
            }
            mEditText = number;
            //确认数量
            subMitTicketNum();
        } catch (Exception e) {
            mContext.showToastError("提交失败,请输入正确数字!");
            mEditText = 1;
            mActiveDialogSubNum.setText(mEditText + "");
        }
    }

    private void subMitTicketNum() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("takeMeOutId", takemeoutBean.getId() + "");
        ALog.i("takeMeOutId = " + takemeoutBean.getId());
        params.put("tickets", mEditText + "");
        ALog.i("tickets = " + mEditText);
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_TACK_JOIN, params, ActiveJoinerTrideResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACK_JOIN, this, false).setTag(this);
    }

    private void setEditAdd() {
        mEditText = getEditText();
        mEditText++;
        mActiveDialogSubNum.setText(mEditText + "");
    }

    private void setEditLess() {
        mEditText = getEditText();
        if (mEditText > 1) {
            mEditText--;
            mActiveDialogSubNum.setText(mEditText + "");
        } else {
            mContext.showToastInfo("最小值为1");
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACK_JOIN
                && response instanceof ActiveJoinerTrideResponse) {
            ActiveJoinerTrideResponse activeJoinerTrideResponse = (ActiveJoinerTrideResponse) response;
            if (activeJoinerTrideResponse.getCode() == 1) {
//                ActiveJoinerTrideResponse.DataBean dataBean = activeJoinerTrideResponse.getData();
//                ActivePayTrideBean mActivePayTrideBean = new ActivePayTrideBean("TakeMeOut",
//                        takemeoutBean.getThumbnailslogo(), takemeoutBean.getName(),
//                        dataBean.getTrade().getPrice(), dataBean.getTrade().getJobType(),
//                        dataBean.getTrade().getTradeNum(), mEditText);
//                ActiveJoinPayActivity.startActiveJoinPayAct(mContext, mActivePayTrideBean);
                dismiss();
            } else {
                mContext.showToastInfo(activeJoinerTrideResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
