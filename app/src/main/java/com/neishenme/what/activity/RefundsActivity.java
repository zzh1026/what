package com.neishenme.what.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RefundsInfo;
import com.neishenme.what.bean.RefundsStates;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.seny.android.utils.MyToast;

import java.util.HashMap;

/**
 * 进度查询页面
 * 2017-2-23 mw
 * 进度查询,,,,, 别人的
 */
public class RefundsActivity extends AutoLayoutActivity implements HttpLoader.ResponseListener {

    private TextView all_tv; //title文字
    private TextView refunds_apply_date; //申请日期
    private TextView refunds_apply_time; //申请时间
    private TextView refunds_tv_submit_date; //提交日期
    private TextView refunds_submit_time; //提交时间
    private TextView refunds_ing_date; //进行日期
    private TextView refunds_ing_time; //进行时间
    private TextView refunds_end_date; //结束日期
    private TextView refunds_end_time; //结束时间
    private ImageView iv_state_refunds;//状态图片
    private ImageView iv_back;//返回按钮
    private ImageView refunds_imbttn;//取消申请图片
    private String[] split;  //截取的字符串
    private int year;   //获取本地时间- 年
    private int month;  //获取本地时间- 月
    private int date;  //获取本地时间- 日
    private StringBuilder time; //时间
    private RefundsInfo walletMaxPrice;//取消申请接口返回的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_refunds);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(backlistener); //返回监听事件
        refunds_imbttn.setOnClickListener(cancelListener);//取消申请的监听事件
    }

    protected void initView() {
        all_tv = (TextView) findViewById(R.id.tv_title);
        iv_state_refunds = (ImageView) findViewById(R.id.iv_state_refunds);
        refunds_apply_date = (TextView) findViewById(R.id.refunds_apply_date);
        refunds_apply_time = (TextView) findViewById(R.id.refunds_apply_time);
        refunds_tv_submit_date = (TextView) findViewById(R.id.refunds_tv_submit_date);
        refunds_submit_time = (TextView) findViewById(R.id.refunds_submit_time);
        refunds_imbttn = (ImageView) findViewById(R.id.refunds_imbttn);
        iv_back = (ImageView) findViewById(R.id.iv_left_back);
        refunds_ing_date = (TextView) findViewById(R.id.refunds_ing_date);
        refunds_ing_time = (TextView) findViewById(R.id.refunds_ing_time);
        refunds_end_time = (TextView) findViewById(R.id.refunds_end_time);
        refunds_end_date = (TextView) findViewById(R.id.refunds_end_date);
    }

    protected void initData() {
        all_tv.setText("进度查询");
        //输入密码过来以后的业务逻辑处理
        setPassWordDate();
        //点击进度查询过来以后的业务逻辑处理
        setStateTime();
    }

    private void setPassWordDate() {
        String data = getIntent().getStringExtra("data"); //支付密码成功以后过来的
        if (data != null) {
            setApplyDateAndTime(data);
        }
    }

    private void setStateTime() {
        Intent intent = this.getIntent();
        RefundsStates states = (RefundsStates) intent.getSerializableExtra("states");
        if (states != null) {
            String applyTime = states.getApplyTime();//提交时间
            String applyPassTime = states.getApplyPassTime();//申请通过时间
            String refundInTime = states.getRefundInTime();//进行中时间
            String finishTime = states.getFinishTime();//完成时间
            refunds_imbttn.setImageDrawable(getResources().getDrawable(R.drawable.refuns_button));//取消申请的按钮

            if (applyTime != null && !applyTime.equals("")) {     //设置提交时间 和日期
                String date = getDate(applyTime);
                if (date.equals(split[0].trim())) {
                    refunds_tv_submit_date.setText("今天");
                } else {
                    refunds_tv_submit_date.setText(split[0]);
                }
                refunds_submit_time.setText(split[1]);
                iv_state_refunds.setImageDrawable((getResources().getDrawable(R.drawable.refunds_one)));
            }
            if (applyPassTime != null && !applyPassTime.equals("")) { //设置申请时间 和日期
                String date = getDate(applyPassTime);
                if (date.equals(split[0].trim())) {
                    refunds_apply_date.setText("今天");
                } else {
                    refunds_apply_date.setText(split[0]);
                }
                refunds_apply_time.setText(split[1]);
                iv_state_refunds.setImageDrawable((getResources().getDrawable(R.drawable.refunds_two)));
            }
            if (refundInTime != null && !refundInTime.equals("")) {  //进行中申请时间 和日期
                String date = getDate(refundInTime);
                if (date.equals(split[0].trim())) {
                    refunds_ing_date.setText("今天");
                } else {
                    refunds_ing_date.setText(split[0]);
                }
                refunds_ing_time.setText(split[1]);
                iv_state_refunds.setImageDrawable((getResources().getDrawable(R.drawable.refunds_three)));
                refunds_imbttn.setImageDrawable(getResources().getDrawable(R.drawable.refunds_cancel));
            }
            if (finishTime != null && !finishTime.equals("")) { //完成申请时间 和日期
                String date = getDate(finishTime);
                if (date.equals(split[0].trim())) {
                    refunds_end_date.setText("今天");
                } else {
                    refunds_end_date.setText(split[0]);
                }
                refunds_end_time.setText(split[1]);
                iv_state_refunds.setImageDrawable((getResources().getDrawable(R.drawable.refunds_end)));
            }
        }
    }

    /**
     * 设置提交时间和日期
     *
     * @param data
     */
    private void setApplyDateAndTime(String data) {
        String date = getDate(data);
        Log.e("a1123", "         " + time + "");
        if (date.equals(split[0].trim())) {
            refunds_tv_submit_date.setText("今天");
        } else {
            refunds_tv_submit_date.setText(split[0]);
        }
        //设置提交日期
        refunds_submit_time.setText(split[1]);       //设置提交时间
    }

    /**
     * 时间统一处理
     */
    private String getDate(String datea) {
        split = datea.split(" ");
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        year = t.year;
        month = t.month + 1;
        date = t.monthDay;
        time = new StringBuilder();
        time.append(year + ".");
        String months = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = "" + month;
        }
        time.append(months + ".");
        time.append(date);
        return time.toString();
    }

    /**
     * 返回监听事件
     */
    View.OnClickListener backlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 取消申请的监听事件
     */
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setBtnState(false);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", NSMTypeUtils.getMyToken());
//            HttpLoader.post(ConstantsWhatNSM.URL_CANCEL_REFUNDS, params, RefundsInfo.class,
//                    ConstantsWhatNSM.URL_CANCEL_REFUNDS_STATE, RefundsActivity.this, false).setTag(this);
            HttpLoader.post(ConstantsWhatNSM.URL_PICK_MONEY_CANCEL_REFUND, params, RefundsInfo.class,
                    ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_CANCEL_REFUND, RefundsActivity.this, false).setTag(this);
        }
    };

    private void setBtnState(boolean canClick) {
        refunds_imbttn.setClickable(canClick);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_CANCEL_REFUND           //取消申请接口的业务逻辑
                && response instanceof RefundsInfo) {
            walletMaxPrice = (RefundsInfo) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                MyToast.showConterToast(App.getApplication(), walletMaxPrice.getMessage());
                finish();
            } else {
                MyToast.showConterToast(App.getApplication(), walletMaxPrice.getMessage());
            }
            setBtnState(true);
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
