package com.neishenme.what.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.fragment.MyOrderingFragment;

/**
 * 我的订单界面, 现在已经弃用了
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {
    public static final int TO_TRIP_REQUEST_CODE = 1;

    private RelativeLayout titleBar;
    private ImageView ivIconBack;
    private RadioButton rbOrderIng;
    private RadioButton rbHistoryOrder;
    private FrameLayout fragmentContainer;
    private RadioGroup rgOrderMenus;
    private MyOrderingFragment orderingFragment;
    private MyOrderingFragment historyFragment;


    @Override
    protected int initContentView() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView() {
        titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        ivIconBack = (ImageView) findViewById(R.id.iv_icon_back);
        rbOrderIng = (RadioButton) findViewById(R.id.rb_order_ing);
        rbHistoryOrder = (RadioButton) findViewById(R.id.rb_history_order);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        rgOrderMenus = (RadioGroup) findViewById(R.id.rg_order_menus);
        rbOrderIng.setChecked(true);

        orderingFragment = MyOrderingFragment.newInstance("1");
        historyFragment = MyOrderingFragment.newInstance("2");
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, orderingFragment)
                .add(R.id.fragment_container, historyFragment).hide(historyFragment).commit();

    }

    @Override
    protected void initListener() {
        ivIconBack.setOnClickListener(this);
        rgOrderMenus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_order_ing:
                        rbHistoryOrder.setBackground(null);
                        rbOrderIng.setBackground(getResources().getDrawable(R.drawable.my_order_left));
                        getFragmentManager().beginTransaction().hide(historyFragment).show(orderingFragment).commit();
                        rbOrderIng.setTextColor(getResources().getColor(R.color.Whiteffffff));
                        rbHistoryOrder.setTextColor(getResources().getColor(R.color.Black3f3f3f));
                        break;
                    case R.id.rb_history_order:
                        rbHistoryOrder.setBackground(getResources().getDrawable(R.drawable.my_order_right));
                        rbOrderIng.setBackground(null);
                        getFragmentManager().beginTransaction().hide(orderingFragment).show(historyFragment).commit();
                        rbOrderIng.setTextColor(getResources().getColor(R.color.Black3f3f3f));
                        rbHistoryOrder.setTextColor(getResources().getColor(R.color.Whiteffffff));
                        break;
                }
            }
        });

    }

    public void toTripAct(String inviteId) {
        Intent intent = new Intent(this, MyTripActivity.class);
        intent.putExtra("data", inviteId);
        startActivityForResult(intent, TO_TRIP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_TRIP_REQUEST_CODE && resultCode == RESULT_OK) {
            rbHistoryOrder.setChecked(true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon_back:
                this.finish();
                break;
        }
    }
}
