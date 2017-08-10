package com.neishenme.what.activity;

import android.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.fragment.ActiveMyAddFragment;
import com.neishenme.what.fragment.ActiveMyVotedFragment;

/**
 * 我的活动界面,一元购活动界面
 */
public class MyActiveActivity extends BaseActivity {

    private ImageView mActiveMyBack;
    private RadioGroup mActiveMyRg;
    private FrameLayout mFragmentContainer;
    private RadioButton mActiveMyAdd;
    private RadioButton mActiveMyVoted;

    private ActiveMyAddFragment myAddFragment;      //我加入的
    private ActiveMyVotedFragment myVotedFragment;  //我投票的

    private FragmentManager fragmentManager;        //同步

    @Override
    protected int initContentView() {
        return R.layout.activity_my_active;
    }

    @Override
    protected void initView() {
        mActiveMyBack = (ImageView) findViewById(R.id.active_my_back);
        mActiveMyRg = (RadioGroup) findViewById(R.id.active_my_rg);
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        mActiveMyAdd = (RadioButton) findViewById(R.id.active_my_add);
        mActiveMyVoted = (RadioButton) findViewById(R.id.active_my_voted);
    }

    @Override
    protected void initListener() {
        mActiveMyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActiveMyRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.active_my_add:
                        mActiveMyAdd.setBackground(getResources().getDrawable(R.drawable.my_order_left));
                        mActiveMyAdd.setTextColor(getResources().getColor(R.color.Whiteffffff));
                        mActiveMyVoted.setBackground(null);
                        mActiveMyVoted.setTextColor(getResources().getColor(R.color.Black3f3f3f));
                        if (myVotedFragment != null && myAddFragment != null) {
                            fragmentManager.beginTransaction().hide(myVotedFragment).show(myAddFragment).commit();
                        }
                        break;
                    case R.id.active_my_voted:
                        mActiveMyAdd.setBackground(null);
                        mActiveMyAdd.setTextColor(getResources().getColor(R.color.Black3f3f3f));
                        mActiveMyVoted.setBackground(getResources().getDrawable(R.drawable.my_order_right));
                        mActiveMyVoted.setTextColor(getResources().getColor(R.color.Whiteffffff));
                        if (myVotedFragment != null && myAddFragment != null) {
                            fragmentManager.beginTransaction().hide(myAddFragment).show(myVotedFragment).commit();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        myAddFragment = ActiveMyAddFragment.newInstance();
        myVotedFragment = ActiveMyVotedFragment.newInstance();
        fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, myAddFragment)
                .add(R.id.fragment_container, myVotedFragment).hide(myVotedFragment).show(myAddFragment)
                .commit();
    }
}
