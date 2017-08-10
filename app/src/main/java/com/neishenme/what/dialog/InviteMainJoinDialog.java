package com.neishenme.what.dialog;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditPayMoneyActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.fragment.HomeInviteFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class InviteMainJoinDialog extends BaseDialog {
    private MainActivity mContext;
    private HomeInviteFragment homeInviteFragment;
    private ArrayList<String> items;
    private int mCurrentPosition = 0;

    private WheelPicker mDialogReleasePickMoney;
    private TextView mDialogReleaseQuickEditMoney;
    private TextView mDialogReleaseQuickSave;

    private int MONEY_START = 0;
    private int MONEY_END = 1000;
    private int MONEY_BETWEEN = 50;

    public InviteMainJoinDialog(MainActivity mContext, HomeInviteFragment homeInviteFragment) {
        super(mContext, Gravity.BOTTOM, 0.0f, AnimationDirection.GROW, true, true);
        setContentView(R.layout.dialog_release_money_quick);
        this.homeInviteFragment = homeInviteFragment;
        this.mContext = mContext;
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
                homeInviteFragment.onMoneyEdit(items.get(mCurrentPosition));
                dismiss();
            }
        });
        mDialogReleaseQuickEditMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.isShouldNotRefresh = true;
                EditPayMoneyActivity.startEditPayMoneyForResult(mContext,
                        MainActivity.REQUEST_CODE_JOIN_INVITE_PRICE, EditPayMoneyActivity.PAY_MONEY_FLAG_JOIN);
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.blurCancel();
            }
        });
    }

    private void initData() {
        items = new ArrayList<>();
        for (int i = MONEY_START; i <= MONEY_END; i += MONEY_BETWEEN) {
            items.add(i + "");
        }

        mDialogReleasePickMoney.setData(items);
        mDialogReleasePickMoney.setSelectedItemPosition(items.size() >= 2 ? 2 : 0);
        mCurrentPosition = items.size() >= 2 ? 2 : 0;
    }
}
