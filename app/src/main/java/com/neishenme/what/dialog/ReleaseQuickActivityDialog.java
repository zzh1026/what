package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditPayMoneyActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.ReleaseQuickActivity;
import com.neishenme.what.fragment.ReleaseQuickFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ReleaseQuickActivityDialog extends BaseDialog {
    private ReleaseQuickActivity context;
    private ArrayList<String> items;
    private ReleaseQuickActivity mReleaseQuickFragment;
    private int mCurrentPosition = 0;

    private WheelPicker mDialogReleasePickMoney;
    private TextView mDialogReleaseQuickEditMoney;
    private TextView mDialogReleaseQuickSave;

    public ReleaseQuickActivityDialog(Context context, ArrayList<String> items, ReleaseQuickActivity mReleaseQuickFragment) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.GROW, true, true);

        setContentView(R.layout.dialog_release_money_quick);
        this.context = (ReleaseQuickActivity) context;
        this.items = items;
        this.mReleaseQuickFragment = mReleaseQuickFragment;
        initView();
        initListeners();
        initData();
    }

    public void initView() {
        mDialogReleasePickMoney = (WheelPicker) findViewById(R.id.dialog_release_pick_money);
        mDialogReleaseQuickEditMoney = (TextView) findViewById(R.id.dialog_release_quick_edit_money);
        mDialogReleaseQuickSave = (TextView) findViewById(R.id.dialog_release_quick_save);
    }

    public void initListeners() {
        mDialogReleasePickMoney.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mCurrentPosition = position;
            }
        });
        mDialogReleaseQuickSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReleaseQuickFragment.onMoneyEdit(items.get(mCurrentPosition));
                dismiss();
            }
        });
        mDialogReleaseQuickEditMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPayMoneyActivity.startEditPayMoneyForResult(context, mReleaseQuickFragment.REQUEST_CODE_QUICK_RELEASE_PRICE, EditPayMoneyActivity.PAY_MONEY_FLAG_YUSUAN);
                dismiss();
            }
        });
    }

    private void initData() {
        mDialogReleasePickMoney.setData(items);
        mDialogReleasePickMoney.setSelectedItemPosition(items.size() >= 2 ? 2 : 0);
        mCurrentPosition = items.size() >= 2 ? 2 : 0;
    }
}
