package com.neishenme.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/6/1 20:13
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class EditMyInfoActivity extends BaseActivity implements HttpLoader.ResponseListener {
    public static final int NICK_NAME = 0;
    public static final int SIGN = 1;
    public int currentSelect = -1;

    private TextView mEditInfoCancleTv;
    private TextView mEditInfoTitleTv;
    private TextView mEditInfoSubmitTv;
    private EditText mEditInfoAddEt;

    private String mContext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    protected void initView() {
        mEditInfoCancleTv = (TextView) findViewById(R.id.edit_info_cancle_tv);
        mEditInfoTitleTv = (TextView) findViewById(R.id.edit_info_title_tv);
        mEditInfoSubmitTv = (TextView) findViewById(R.id.edit_info_submit_tv);
        mEditInfoAddEt = (EditText) findViewById(R.id.edit_info_add_et);

    }

    @Override
    protected void initListener() {
        mEditInfoCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditInfoSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentSelect) {
                    case NICK_NAME:
                        String nickName = mEditInfoAddEt.getText().toString().trim();
                        if (TextUtils.isEmpty(nickName)) {
                            showToastWarning("请填写昵称");
                            return;
                        }
                        if (nickName.length() > 6) {
                            showToastWarning("昵称过长,请重新填写");
                            mEditInfoAddEt.setText("");
                            return;
                        }
                        //更新昵称
                        mContext = nickName;
                        upNick();
                        break;
                    case SIGN:
                        String sign = mEditInfoAddEt.getText().toString().trim();
                        if (TextUtils.isEmpty(sign)) {
                            showToastWarning("请填写签名");
                            return;
                        }
                        if (sign.length() > 15) {
                            showToastWarning("签名过长,请重新填写");
                            mEditInfoAddEt.setText("");
                            return;
                        }
                        //更新昵称
                        mContext = sign;
                        upSign();
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        if (data.equals("nickname")) {
            currentSelect = NICK_NAME;
            mEditInfoTitleTv.setText("昵称");
            mEditInfoAddEt.setHint("请填写8字以内的昵称");
            InputFilter[] filters = {new InputFilter.LengthFilter(8)};
            mEditInfoAddEt.setFilters(filters);
        } else {
            currentSelect = SIGN;
            mEditInfoTitleTv.setText("个性签名");
            mEditInfoAddEt.setHint("请填写15字以内的个性签名");
            InputFilter[] filters = {new InputFilter.LengthFilter(15)};
            mEditInfoAddEt.setFilters(filters);
        }

    }

    private void upSign() {
        //App.USEREDIT.putString("signature", sign).commit();
        checkSentive();

    }

    private void checkSentive() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", mContext);
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_SENSITIVE_CHECK, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK, this).setTag(this);
    }

    private void upNick() {
        //App.USEREDIT.putString("name", nickName).commit();
        checkSentive();
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                showToastSuccess("保存成功");
                mContext = sendSuccessResponse.getData().getReplaceName();
                switch (currentSelect) {
                    case NICK_NAME:
                        showToastSuccess("保存成功");
                        Intent intent = new Intent();
                        intent.putExtra("upNick", mContext);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case SIGN:
                        showToastSuccess("保存成功");
                        Intent intent2 = new Intent();
                        intent2.putExtra("upSign", mContext);
                        setResult(RESULT_OK, intent2);
                        finish();
                        break;
                }
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络连接失败");
    }
}
