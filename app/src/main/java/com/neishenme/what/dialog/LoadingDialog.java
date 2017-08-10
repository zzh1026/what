package com.neishenme.what.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.neishenme.what.R;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoadingDialog extends BaseDialog {
    //public LoadingDialog loadingDialog = null;
    private ProgressBar firstBar;

    public LoadingDialog(Activity mContext) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, true, false);

        setContentView(R.layout.dialog_loadings);
        initData();
    }

    private void initData() {

        firstBar = (ProgressBar) findViewById(R.id.firstBar);
        //firstBar.setDrawingCacheBackgroundColor(Color.parseColor("#000000"));

    }

//    public static LoadingDialog getInstance(Activity mActivity) {
//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(mActivity);
//        }
//        return loadingDialog;
//    }
}
