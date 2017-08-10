package com.neishenme.what.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.eventbusobj.UserMeetingSuccess;
import com.neishenme.what.utils.CreateQRImageTest;
import com.neishenme.what.utils.MPermissionManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/3/28 04:00
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 二维码生成界面 在 邀请详情和我的行程界面点击立即见面跳转进入该界面
 * .
 * 其作用是 :
 */
public class ZXingGetActivity extends BaseActivity {

    private ImageView mIvIconBack;
    private Button mBtnZxingErweima;
    private Button mBtnZxingSaoyisao;
    private InviteDetailBean stickyEvent;
    private ImageView mIvZxingErweima;
    private String inviteId;


    @Override
    protected int initContentView() {
        return R.layout.activity_zxing_get;
    }


    @Override
    protected void initView() {
        mIvIconBack = (ImageView) findViewById(R.id.iv_icon_back);

        mIvZxingErweima = (ImageView) findViewById(R.id.iv_zxing_erweima);

        mBtnZxingErweima = (Button) findViewById(R.id.btn_zxing_erweima);
        mBtnZxingSaoyisao = (Button) findViewById(R.id.btn_zxing_saoyisao);
    }

    @Override
    protected void initListener() {
        mIvIconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnZxingSaoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        if (AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
            ActivityUtils.startActivityAndFinish(ZXingGetActivity.this, ZXingGetRichActivity.class);
        } else {
            AndPermission.with(this)
                    .requestCode(MPermissionManager.REQUEST_CODE_CAMERA_ER_WEIMA)
                    .permission(Manifest.permission.CAMERA)
                    .rationale(mRationaleListener)
                    .send();
        }
    }

    @Override
    protected void initData() {

        stickyEvent = EventBus.getDefault().getStickyEvent(InviteDetailBean.class);

        EventBus.getDefault().register(this);

        inviteId = stickyEvent.inviteId;
        String joinerId = stickyEvent.joinerId;
        String targetId = stickyEvent.targetId;
        String publisherId = stickyEvent.publisherId;

        String result = "nsminviteqr:inviteId=" + inviteId + "&targetId="
                + targetId + "&publisherId=" + publisherId + "&joinerId=" + joinerId;

        CreateQRImageTest createQRImageTest = new CreateQRImageTest();
        createQRImageTest.sweepIV = mIvZxingErweima;

        ALog.i("result的值为" + result);
        createQRImageTest.createQRImage(result);
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA_ER_WEIMA:
                    AndPermission.rationaleDialog(ZXingGetActivity.this, rationale).show();
                    break;
            }
        }
    };

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_ER_WEIMA) {
                ActivityUtils.startActivityAndFinish(ZXingGetActivity.this, ZXingGetRichActivity.class);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_ER_WEIMA) {
                MPermissionManager.showToSetting(ZXingGetActivity.this,
                        MPermissionManager.REQUEST_CODE_CAMERA_ER_WEIMA, new MPermissionManager.OnNegativeListner() {
                            @Override
                            public void onNegative() {

                            }
                        });
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    public void onEventMainThread(UserMeetingSuccess userMeetingSuccess) {
        if (inviteId.equals(String.valueOf(userMeetingSuccess.getInviteId()))) {
//            showToastSuccess("恭喜您成功见面");
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 开启扫描二维码活动界面的方法
     *
     * @param context          一个可以开启界面的上下文
     * @param inviteDetailBean 一个足够扫码的数据对象
     */
    public static void startZXingAct(Context context, InviteDetailBean inviteDetailBean) {
        Intent intent = new Intent(context, ZXingGetActivity.class);
        EventBus.getDefault().postSticky(inviteDetailBean);
        context.startActivity(intent);
    }
}
