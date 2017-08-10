package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.view.loopview.LoopView;
import com.neishenme.what.view.loopview.OnItemSelectedListener;

import org.seny.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by gzx
 */
public class MyDateReleaseQuickPickerDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private LoopView monthLoop, dayLoop;
    private int currMonthIndex, currDayIndex;
    private TextView cancel, commit;
    private TextView tv_title;
    private String dialogTitle;
    private int defalutMonthIndex = 0, defaultDayIndex = 0;
    private String timeLimit;
    private int mSelectHour = 0;

    public MyDateReleaseQuickPickerDialog(Context context, String timeLimit, String title) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_release_quick_data_picker);
        this.context = context;
        dialogTitle = title;

        this.timeLimit = timeLimit;
        bindView();
        initData();
        setListeners();
    }

    public void bindView() {
        tv_title = (TextView) findViewById(R.id.wheel_dialog_title);
        monthLoop = (LoopView) findViewById(R.id.loopview_month);
        dayLoop = (LoopView) findViewById(R.id.loopview_day);
        cancel = (TextView) findViewById(R.id.cancel);
        commit = (TextView) findViewById(R.id.commit);
    }

    OnConfirmListeners mOnConfirmListeners;

    public void setOnConfirmListeners(OnConfirmListeners onConfirmListeners) {
        mOnConfirmListeners = onConfirmListeners;
    }

    public interface OnConfirmListeners {
        void onConfirmClicked(int monthSelectedIndex, int daySelectedIndex);
    }

    public void setListeners() {
        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        monthLoop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currMonthIndex = index;
            }
        });
        dayLoop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currDayIndex = index;
            }
        });

    }

    public void initData() {
        //设置字体大小
        monthLoop.setTextSize(15);
        dayLoop.setTextSize(15);

        tv_title.setText(dialogTitle);
        String[] split = timeLimit.split(",");
        int nowHour = DateUtils.formatHour(System.currentTimeMillis());
        List hours = getHourList(split, nowHour);
        monthLoop.setItems(hours);
        ArrayList minute = new ArrayList();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minute.add("0" + i);
            } else {
                minute.add("" + i);
            }
        }
        dayLoop.setItems(minute);

        //设置初始位置
        currMonthIndex = defalutMonthIndex;
        monthLoop.setInitPosition(defalutMonthIndex);
        currDayIndex = defaultDayIndex;
        dayLoop.setInitPosition(defaultDayIndex);
    }

    private List getHourList(String[] split, int nowHour) {
        ArrayList<String> hourList = new ArrayList();
        for (String hour : split) {
            if (Integer.parseInt(hour) >= nowHour) {
                hourList.add(hour);
            }
        }
        if (hourList.size() == 0) {
            hourList.add(split[split.length - 1]);
        }
        mSelectHour = split.length - hourList.size();
        return hourList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确定
            case R.id.commit:
                mOnConfirmListeners.onConfirmClicked(currMonthIndex + mSelectHour, currDayIndex);
                this.dismiss();
                break;

            //取消
            case R.id.cancel:
                this.dismiss();
                break;
        }

    }
}
