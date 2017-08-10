package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.neishenme.what.R;
import com.neishenme.what.fragment.ReleaseNormalFragment;

import org.seny.android.utils.ALog;

import java.util.ArrayList;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/15.
 */

public class ReleaseMoneyNormalDialog extends BaseDialog {
    private Context context;
    private ArrayList<String> items;
    private ReleaseNormalFragment releaseNormalFragment;

    private WheelPicker mDialogReleasePickMoney;
    private TextView mDialogReleaseQuickSave;

    private int mCurrentSelect = 0;

    public ReleaseMoneyNormalDialog(Context context, ArrayList<String> items, ReleaseNormalFragment releaseNormalFragment) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.GROW, true, true);

        setContentView(R.layout.dialog_release_money_normal);
        this.context = context;
        this.releaseNormalFragment = releaseNormalFragment;
        this.items = items;
        initView();
        initListeners();
        initData();
    }

    public void initView() {
        mDialogReleasePickMoney = (WheelPicker) findViewById(R.id.dialog_release_pick_money);
        mDialogReleaseQuickSave = (TextView) findViewById(R.id.dialog_release_quick_save);
    }

    public void initListeners() {
        mDialogReleasePickMoney.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                ALog.i("选中的position为:" + position + "\n"
                        + "选中的数据为:" + data.toString() + "\n"
                        + "滚动的pick对象为::" + picker.toString() + "\n"
                        + "界面中的pick对象为::" + mDialogReleasePickMoney.toString() + "\n"
                );
                mCurrentSelect = position;
            }
        });
        mDialogReleaseQuickSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = items.get(mCurrentSelect);
                releaseNormalFragment.onMoneyEdit(s);
                dismiss();
            }
        });
    }

    private void initData() {
        mDialogReleasePickMoney.setData(items);
    }
}
