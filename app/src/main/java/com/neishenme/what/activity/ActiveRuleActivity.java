package com.neishenme.what.activity;

import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;

import org.seny.android.utils.ActivityUtils;

/**
 * 一元购活动 的规则界面
 * 由主界面广告  特定 type和 我的一元购活动详情界面点击规则进入
 */
public class ActiveRuleActivity extends BaseActivity {
    private ImageView mIvBack;
    private ImageView mActiveRuleToActiveBg;

    @Override
    protected int initContentView() {
        return R.layout.activity_activite_rule;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mActiveRuleToActiveBg = (ImageView) findViewById(R.id.active_rule_to_active_bg);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActiveRuleToActiveBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(ActiveRuleActivity.this, ActiveActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
    }
}
