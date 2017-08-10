package org.seny.android.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import org.seny.android.utils.toastlib.Toasty;
import org.seny.android.utils.toastlib.ToastyType;
import org.springframework.util.Assert;

/**
 * Toast 工具类
 *
 * @author Seny
 */
public class MyToast {
    private static Toast mToast;

    /**
     * show a toast
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
//        Assert.notNull(context, "Context cant be NULL!");
//        if (mToast == null) {
//            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        }
//        mToast.setDuration(Toast.LENGTH_SHORT);
//        mToast.setText(text);
//        mToast.show();
        showConterToast(context, text);
    }

    /**
     * show a long toast
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, String text) {
        Assert.notNull(context, "Context cant be NULL!");
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    public static void showLong(Context context, int resId) {
        showLong(context, context.getString(resId));
    }

    public static void showConterToast(Context context, String myToastShow) {
//        Assert.notNull(context, "Context cant be NULL!");
//        if (mToast == null) {
//            mToast = Toast.makeText(context, myToastShow, Toast.LENGTH_LONG);
//        }
//        mToast.setDuration(Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.CENTER, 0, 100);
//        mToast.setText(myToastShow);
//        mToast.show();
        showConterToast(context, myToastShow, ToastyType.TYPE_NORMAL);
    }

    /**
     * 这个一般用不到,  一般使用normal的时候调用 {@link MyToast#showConterToast(Context, String)}
     * 所以这个方法展示定位是 private.
     *
     * @param context
     * @param myToastShow
     */
    private static void showNormal(Context context, String myToastShow) {
        showConterToast(context, myToastShow, ToastyType.TYPE_NORMAL);
    }

    public static void showInfo(Context context, String myToastShow) {
        showConterToast(context, myToastShow, ToastyType.TYPE_INFO);
    }

    public static void showWarning(Context context, String myToastShow) {
        showConterToast(context, myToastShow, ToastyType.TYPE_WARNING);
    }

    public static void showSuccess(Context context, String myToastShow) {
        showConterToast(context, myToastShow, ToastyType.TYPE_SUCCESS);
    }

    public static void showError(Context context, String myToastShow) {
        showConterToast(context, myToastShow, ToastyType.TYPE_ERROR);
    }

    public static void showConterToast(Context context, String myToastShow, ToastyType toastyType) {
        switch (toastyType) {
            case TYPE_NORMAL:
                Toasty.normal(context, myToastShow).show();
                break;
            case TYPE_INFO:
                Toasty.info(context, myToastShow).show();
                break;
            case TYPE_WARNING:
                Toasty.warning(context, myToastShow).show();
                break;
            case TYPE_SUCCESS:
                Toasty.success(context, myToastShow).show();
                break;
            case TYPE_ERROR:
                Toasty.error(context, myToastShow).show();
                break;
        }
    }

    public static void cancleShowToast() {
//        mToast.cancel();
    }
}
