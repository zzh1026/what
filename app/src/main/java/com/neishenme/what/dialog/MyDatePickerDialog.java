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
public class MyDatePickerDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private ArrayList<String> items;
    private LoopView yearLoop, monthLoop, dayLoop;
    private int currYearIndex, currMonthIndex, currDayIndex;
    private TextView cancel, commit;
    private TextView tv_title;
    private String dialogTitle;
    private int defalutYearIndex, defalutMonthIndex, defaultDayIndex;
    public static int DEFAULT_YEAR = 1990;
    public boolean yearIsExist = true;
    private RelativeLayout rlYear;
    private String timeLimit;
    private int mSelectHour = 0;

    public MyDatePickerDialog(Context context, ArrayList<String> items, String title, int defaultYear, int defaultMonth, int defaultDay) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_data_picker);
        this.context = context;
        this.items = items;
        dialogTitle = title;
       /* if(defaultYear<Integer.parseInt(items.get(0))){
            throw new IllegalArgumentException("默认显示年份"+defaultYear+"不在"+items.get(0)+"-"+Calendar.getInstance().get(Calendar.YEAR)+"范围内");
        }

         if(defaultMonth<1||defaultMonth>12){
            throw new IllegalArgumentException("默认显示月份必须在1-12之间");
        }

        Calendar instance = Calendar.getInstance();
        instance.se
        int actualMaximum = getActualMaximum(Calendar.DAY_OF_MONTH);
        if(defaultDay<1||defaultDay>actualMaximum){
            throw new IllegalArgumentException(defaultMonth+"月的天数必须在1到31之间");
        }
        */
        this.defalutMonthIndex = defaultMonth - 1;
        this.defalutYearIndex = defaultYear - Integer.parseInt(items.get(0));
        this.defaultDayIndex = defaultDay - 1;
        bindView();
        initData();
        setListeners();
    }


    public MyDatePickerDialog(Context context, ArrayList<String> items, String title) {
        this(context, items, title, DEFAULT_YEAR, 1, 1);
    }

    public MyDatePickerDialog(Context context, String timeLimit, String title) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_data_picker);
        yearIsExist = false;
        this.context = context;
        dialogTitle = title;

        this.timeLimit = timeLimit;
        bindView();
        initData();
        setListeners();
    }

    ;

    public void bindView() {
        tv_title = (TextView) findViewById(R.id.wheel_dialog_title);
        yearLoop = (LoopView) findViewById(R.id.loopview_year);
        monthLoop = (LoopView) findViewById(R.id.loopview_month);
        dayLoop = (LoopView) findViewById(R.id.loopview_day);
        cancel = (TextView) findViewById(R.id.cancel);
        commit = (TextView) findViewById(R.id.commit);
        if (!yearIsExist) {
            rlYear = (RelativeLayout) findViewById(R.id.rl_year);
            rlYear.setVisibility(View.GONE);
        }
    }

    OnConfirmListeners mOnConfirmListeners;

    public void setOnConfirmListeners(OnConfirmListeners onConfirmListeners) {
        mOnConfirmListeners = onConfirmListeners;
    }

    public interface OnConfirmListeners {
        void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex);
    }

    public void setListeners() {
        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        yearLoop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currYearIndex = index;
                setDayLoopData();
            }
        });
        monthLoop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currMonthIndex = index;
                if (yearIsExist) {
                    setDayLoopData();
                }

            }
        });
        dayLoop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currDayIndex = index;
            }
        });

    }

    public void setYearExist(boolean exist) {
        this.yearIsExist = exist;
    }

    private int getMaxDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // Log.e("getMaxDaysInMonth", year + "/" + month + "/最大天数" + maxDaysInMonth);
        return maxDaysInMonth;
    }

    private void setDayLoopData() {
        List days = new ArrayList();
        int maxDaysInMonth = getMaxDaysInMonth(Integer.parseInt(items.get(currYearIndex)), currMonthIndex + 1);
        for (int i = 1; i <= maxDaysInMonth; i++) {
            if (i < 10) {
                days.add("0" + i);
            } else {
                days.add("" + i);
            }
        }
        dayLoop.setItems(days);

    }

    public void initData() {
        //设置字体大小
        yearLoop.setTextSize(15);
        monthLoop.setTextSize(15);
        dayLoop.setTextSize(15);

        tv_title.setText(dialogTitle);
        if (yearIsExist) {
            List months = new ArrayList();
            for (int i = 1; i <= 12; i++) {
                if (i < 10) {
                    months.add("0" + i);
                } else {
                    months.add("" + "" + i);
                }
            }

            yearLoop.setItems(items);
            monthLoop.setItems(months);
        } else {
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
        }

        if (yearIsExist) {
            //设置初始位置
            currYearIndex = defalutYearIndex;
            yearLoop.setInitPosition(defalutYearIndex);
            currMonthIndex = defalutMonthIndex;
            monthLoop.setInitPosition(defalutMonthIndex);
            currDayIndex = defaultDayIndex;
            setDayLoopData();
            dayLoop.setInitPosition(defaultDayIndex);
        } else {
            currMonthIndex = defalutMonthIndex;
            monthLoop.setInitPosition(defalutMonthIndex);
            currDayIndex = defaultDayIndex;
            dayLoop.setInitPosition(defaultDayIndex);
        }


    }

    private List getHourList(String[] split, int nowHour) {
        ArrayList<String> hourList = new ArrayList();
        for (String hour : split) {
            if (Integer.parseInt(hour) > nowHour) {
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
                if (yearIsExist) {
                    mOnConfirmListeners.onConfirmClicked(currYearIndex, currMonthIndex, currDayIndex);
                } else {
                    mOnConfirmListeners.onConfirmClicked(currYearIndex, currMonthIndex + mSelectHour, currDayIndex);
                }
                this.dismiss();
                break;

            //取消
            case R.id.cancel:
                this.dismiss();
                break;
        }

    }
}
