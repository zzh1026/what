package com.neishenme.what.dialog;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveDateStarActivity;
import com.neishenme.what.activity.ActiveRuleActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.bean.ActiveMainShowDialog;
import com.neishenme.what.net.HttpLoader;

import org.seny.android.utils.ActivityUtils;

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
public class ActiveShowDialog extends BaseDialog {
    private static final String ACTIVE_TAKE_ME_OUT = "takemeout";
    private static final String ACTIVE_SUPER_STAR = "superstar";

    private MainActivity mContext;
    private ActiveMainShowDialog.DataBean.DialogBean infos;

    private ImageView mActiveShowCancle;
    private ImageView mActiveShowGoInto;

    public ActiveShowDialog(MainActivity mContext, ActiveMainShowDialog.DataBean.DialogBean infos) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.infos = infos;
        setContentView(R.layout.dialog_show_active);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mActiveShowCancle = (ImageView) findViewById(R.id.active_show_cancle);
        mActiveShowGoInto = (ImageView) findViewById(R.id.active_show_go_into);
    }

    private void initListener() {
        mActiveShowCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mActiveShowGoInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ACTIVE_TAKE_ME_OUT.equals(infos.getPage())) {
                    ActivityUtils.startActivity(mContext, ActiveRuleActivity.class);
                    dismiss();
                } else if (ACTIVE_SUPER_STAR.equals(infos.getPage())) {
                    Intent intent = new Intent(mContext, ActiveDateStarActivity.class);
                    intent.putExtra(ActiveDateStarActivity.DATE_STAR_TITLE, infos.getTitle());
                    mContext.startActivity(intent);
                    dismiss();
                }
            }
        });
    }

    private void initData() {
        if (!TextUtils.isEmpty(infos.getImage()))
            HttpLoader.getImageLoader().get(infos.getImage(),
                    ImageLoader.getImageListener(mActiveShowGoInto, R.drawable.picture_moren, R.drawable.picture_moren));
    }
}
