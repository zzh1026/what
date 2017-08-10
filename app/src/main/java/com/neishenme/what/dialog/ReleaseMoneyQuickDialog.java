package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditPayMoneyActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.fragment.ReleaseQuickFragment;

import org.seny.android.utils.ALog;

import java.util.ArrayList;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/15.
 */

public class ReleaseMoneyQuickDialog extends BaseDialog {
    private MainActivity context;
    private ArrayList<String> items;
    private ReleaseQuickFragment mReleaseQuickFragment;
    private int mCurrentPosition = 0;

    private WheelPicker mDialogReleasePickMoney;
    private TextView mDialogReleaseQuickEditMoney;
    private TextView mDialogReleaseQuickSave;

    public ReleaseMoneyQuickDialog(Context context, ArrayList<String> items, ReleaseQuickFragment mReleaseQuickFragment) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.GROW, true, true);

        setContentView(R.layout.dialog_release_money_quick);
        this.context = (MainActivity) context;
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
//                EditPayMoneyActivity.startEditPayMoneyForResult(context, MainActivity.REQUEST_CODE_QUICK_RELEASE_PRICE, EditPayMoneyActivity.PAY_MONEY_FLAG_YUSUAN);
                dismiss();
            }
        });
    }

    private void initData() {
        mDialogReleasePickMoney.setData(items);
    }
}
