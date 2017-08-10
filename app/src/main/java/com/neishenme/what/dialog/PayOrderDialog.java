package com.neishenme.what.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.nsminterface.PayCancelListener;
import com.neishenme.what.nsminterface.PayOrderInputListener;
import com.neishenme.what.utils.UpSoftInputUtils;
import com.neishenme.what.view.PasswordInputView;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;

/**
 * 作者：zhaozh create on 2016/5/19 16:50
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 自定义的 密码输入框
 * 提现需要输入密码, 使用余额支付需要密码,所以该弹框有 两个入口
 * .
 * 其作用是 :
 */
public class PayOrderDialog {


    private static PasswordInputView et_dialog_pwd;

    public static void showInputPwdDialog(final Context context, final PayOrderInputListener payOrderInputListener) {
        AlertDialog.Builder builder = new Builder(context);
        final View view = View.inflate(context, R.layout.dialog_enter_pwd, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        TextView bt_dialog_cancle = (TextView) view
                .findViewById(R.id.bt_dialog_cancle);

        bt_dialog_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                payOrderInputListener.inputErrorListener();
                dialog.dismiss();
            }
        });

        et_dialog_pwd = (PasswordInputView) view.findViewById(R.id.passwordInputView);

        TextView bt_dialog_ok = (TextView) view.findViewById(R.id.bt_dialog_ok);
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = et_dialog_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(password) || password.length() != 6) {
                    MyToast.showConterToast(context, "请输入正确的密码");
                    et_dialog_pwd.setText("");
                } else {
                    payOrderInputListener.inputSuccessListener(et_dialog_pwd.getText().toString());
                    dialog.dismiss();
                }
            }
        });
    }

    public static void showCancel(final Context context, final PayCancelListener payCancelListener) {
        AlertDialog.Builder builder = new Builder(context);
        final View view = View.inflate(context, R.layout.dialog_pay_cancel, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        TextView bt_dialog_cancle = (TextView) view.findViewById(R.id.bt_dialog_cancle);
        bt_dialog_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                payCancelListener.cancel();
                dialog.dismiss();
            }
        });

        TextView bt_dialog_ok = (TextView) view.findViewById(R.id.bt_dialog_ok);
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                payCancelListener.ok();
                dialog.dismiss();
            }
        });
    }
}
