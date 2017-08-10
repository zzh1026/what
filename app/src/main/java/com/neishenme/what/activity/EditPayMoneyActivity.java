package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;

/**
 * 作者：zhaozh create on 2016/12/22 16:13
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个用户输入预算和付费金额的界面
 * .
 * 其作用是 :   (4个入口 ,2中类型)-->(三个入口了 , 专属发布 预算不会再进入该界面了)
 * 1,发布活动界面 极速发布 和 专属发布 预算 点击自定义预算金额进入该界面(两个地方)-->(专属发布不进来了,所以发单只有一个入口可以进来)
 * 2,主界面附近的邀请点击加入  ,付费加入  和  邀请详情点击申请加入  ,付费加入 进入该界面(两个地方)
 */
public class EditPayMoneyActivity extends BaseActivity {
    public static final String PAY_MONEY_FLAG_YUSUAN = "yusuan"; //发布预算
    public static final String PAY_MONEY_FLAG_JOIN = "join"; //付费加入

    private static final String PAY_MONEY_REQUEST_DATA = "data"; //进入本界面需要的请求数据码
    public static final String PAY_MONEY_BACK_DATA = "money"; //设置结果的返回标志

    public static final int REQUEST_CODE_RELEASE_YUSUAN = 0; //发布预算,预算只在主界面,为了防止冲突,不用这个
    public static final int REQUEST_CODE_JOIN = 1; //付费加入   ,这个只有 邀请详情点击申请加入 会用到

    private ImageView mEditMoneyBack;
    private EditText mEditPayMoneyInput;
    private TextView mEditPaySave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_edit_pay_money;
    }

    @Override
    protected void initView() {
        mEditMoneyBack = (ImageView) findViewById(R.id.edit_money_back);
        mEditPayMoneyInput = (EditText) findViewById(R.id.edit_pay_money_input);
        mEditPaySave = (TextView) findViewById(R.id.edit_pay_save);
    }

    @Override
    protected void initListener() {
        mEditMoneyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditPaySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySave();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra(PAY_MONEY_REQUEST_DATA);
        if (PAY_MONEY_FLAG_JOIN.equals(data)) {
            mEditPayMoneyInput.setHint("请输入预付金额...");
            mEditPaySave.setText("去支付");
        } else {
            mEditPayMoneyInput.setHint("请输入预算金额...");
            mEditPaySave.setText("确定");
        }
    }

    private void trySave() {
        String moneys = mEditPayMoneyInput.getText().toString().trim();
        try {
            int money = Integer.parseInt(moneys);
            if (money >= 0) {
                Intent intent = new Intent();
                intent.putExtra(PAY_MONEY_BACK_DATA, money + "");
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showToastWarning("输入错误");
                mEditPayMoneyInput.setText("");
            }
        } catch (Exception e) {
            showToastWarning("输入错误");
            mEditPayMoneyInput.setText("");
        }
    }

    public static void startEditPayMoneyForResult(Activity activity, int requestCode, String data) {
        Intent intent = new Intent(activity, EditPayMoneyActivity.class);
        intent.putExtra(PAY_MONEY_REQUEST_DATA, data);
        activity.startActivityForResult(intent, requestCode);
    }
}
