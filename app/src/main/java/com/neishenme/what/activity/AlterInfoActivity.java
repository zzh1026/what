package com.neishenme.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/18.
 *
 * 这个界面是 编辑个人信息界面点击 修改昵称和签名跳转的界面
 */
public class AlterInfoActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private TextView tvCancle;
    private TextView tvCommit;
    private TextView tvTitle;
    private EditText etAlter;
    private CharSequence temp; // 监听前的文本
    private int editStart; // 光标开始位置
    private int editEnd; // 光标结束位置
    private int maxText;
    String titleName;
    private String type;
    //private int count;

    private String mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_alter_info;
    }

    @Override
    protected void initView() {
        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        etAlter = (EditText) findViewById(R.id.et_alter);

    }

    @Override
    protected void initListener() {
        tvCancle.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        etAlter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = etAlter.getSelectionStart();
                editEnd = etAlter.getSelectionEnd();
                if (temp.length() > maxText) {
                    s.delete(editStart - 1, editEnd);
                    etAlter.setText(s);
                    etAlter.setSelection(s.length());
                }

            }
        });

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            titleName = bundle.getString("title");
            type = bundle.getString("type");
            //count = bundle.getInt("selectedCount");

        } else {
            titleName = getIntent().getStringExtra("data");
        }

        tvTitle.setText(titleName);
        etAlter.setHint("添加" + titleName);
//        etAlter.setHintTextColor(Color.parseColor("#ff0000"));
//        etAlter.setTextColor(Color.parseColor("#ffffff"));
        if (titleName.equals("昵称")) {
            maxText = 8;
        } else {
            maxText = 15;
        }

        etAlter.setFocusable(true);
        etAlter.requestFocus();
        //UpSoftInputUtils.openKeyBoard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.tv_commit:
//                if (count >= 3) {
//                    Toast.makeText(this, "最多只能选3个", Toast.LENGTH_LONG).show();
//                    finish();
//                } else {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", etAlter.getText().toString().trim());
                HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_SENSITIVE_CHECK, params, SendSuccessResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK, this).setTag(this);

//                }

                break;
        }

    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_POST_INTERSTEDS
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (1 != sendSuccessResponse.getCode()) {
                showToastInfo(sendSuccessResponse.getMessage());
                finish();
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("result", mContext);
            setResult(RESULT_OK, intent);
            showToastSuccess("保存成功");
            finish();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                mContext = sendSuccessResponse.getData().getReplaceName();
                HashMap params = new HashMap();
                params.put("interestype", type);
                params.put("content", mContext);
                params.put("token", NSMTypeUtils.getMyToken());
                HttpLoader.post(ConstantsWhatNSM.URL_POST_INTERSTEDS, params, SendSuccessResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_POST_INTERSTEDS, this, false).setTag(this);
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }

    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络连接失败,请连接网络后重试");
        finish();
    }
}
