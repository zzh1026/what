package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.bean.AdResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/4/27 13:24
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
@Deprecated
public class ADActivity extends Activity implements HttpLoader.ResponseListener {
    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    private ImageView mAdAd;
    private TextView mAdJump;
    private long startTime;
    private AdResponse adResponse;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ALog.i("msg 的值为" + msg.what);
            toHomeActi();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_ad);
        ALog.i("进入了广告界面");
        mAdAd = (ImageView) findViewById(R.id.ad_ad);
        mAdJump = (TextView) findViewById(R.id.ad_jump);

        getAd();

        startTime = System.currentTimeMillis();


        mAdJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHomeActi();
            }
        });
        mAdAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != adResponse && !TextUtils.isEmpty(adResponse.getData().getLink())) {
//                    Uri uri;
//                    if (NSMTypeUtils.isLogin()) {
//                        HashMap<String, String> params = new HashMap<>();
//                        params.put("token", NSMTypeUtils.getMyToken());
//                        uri = Uri.parse(adResponse.getData().getLink() + HttpLoader.buildGetParam(params));
//                    } else {
//                        uri = Uri.parse(adResponse.getData().getLink());
//                    }
//                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ADActivity.this.startActivity(it);
//                }
            }
        });

        waitTime();

    }

    private void getAd() {
        //HashMap<String, String> params = new HashMap<>();
        HttpLoader.get(ConstantsWhatNSM.URL_AD, null, AdResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_AD, this, false).setTag(this);
    }

    private void waitTime() {
        long endTime = System.currentTimeMillis();
        long dTime = endTime - startTime;
        if (dTime < 3000) {
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000 - dTime);
        } else {
            toHomeActi();
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mAdJump.setVisibility(View.VISIBLE);
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_AD
                && response instanceof AdResponse) {
            adResponse = (AdResponse) response;
            if (1 == adResponse.getCode()) {

                HttpLoader.getImageLoader().get(adResponse.getData().getImage(),
                        ImageLoader.getImageListener(mAdAd, R.drawable.splash_nsm, R.drawable.splash_nsm));
                long endTime = System.currentTimeMillis();
                long dTime = endTime - startTime;

                if (dTime < 3000) {
                    mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000 - dTime);
                } else {
                    toHomeActi();
                }
            } else {
                waitTime();
            }
        } else {
            waitTime();
        }
    }

    private void toHomeActi() {
        if (NSMTypeUtils.isLogin())
            ActivityUtils.startActivityAndFinish(this, MainActivity.class);
        else
            ActivityUtils.startActivityAndFinish(this, GuideStartActivity.class);

    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mAdJump.setVisibility(View.VISIBLE);
        waitTime();
    }

    @Override
    protected void onStop() {
        HttpLoader.cancelRequest(this);
        super.onStop();
    }
}
