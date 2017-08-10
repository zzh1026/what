package com.neishenme.what.view.wheeltime;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.IWheelPicker;
import com.aigestudio.wheelpicker.WheelPicker;
import com.neishenme.what.R;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;

import org.seny.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/2/21.
 */

public class WheelTime24Picker extends LinearLayout implements WheelPicker.OnItemSelectedListener,
        IWheelPicker, IWheelTime24Picker, View.OnClickListener {

    private TextView mPickerTitle;
    private WheelPicker mPickerHour;
    private WheelPicker mPickerMinute;
    private WheelPicker mPickerDay;
    private TextView mPickerCancel;
    private TextView mPickerCommit;

    private OnTimeConformListener mListener;

    private String mTimeData;   //记录当前的时间 年-月-日

    private int mHour, mMinute; //记录当前选中的小时和分钟
    private boolean isToday = true; //动态记录选择的时间是否是今天的

    private List<String> mHourTodayData;     //今天可选择的小时集合
    private List<String> mHourTomorrowData;     //明天可选择的小时集合
    private List<String> mDayData;     //选择今天或者明天
    private List<String> mMinuteData;   //可选择的分钟集合

    private long mStartTime = System.currentTimeMillis() + (120 * 1000);    //起始的时间,默认80m之后

    public WheelTime24Picker(Context context) {
        this(context, null);
    }

    public WheelTime24Picker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_wheel_time_picker_24, this);

        mPickerDay = (WheelPicker) findViewById(R.id.wheel_time_picker_day);
        mPickerHour = (WheelPicker) findViewById(R.id.wheel_time_picker_hour);
        mPickerMinute = (WheelPicker) findViewById(R.id.wheel_time_picker_minute);
        mPickerDay.setOnItemSelectedListener(this);
        mPickerHour.setOnItemSelectedListener(this);
        mPickerMinute.setOnItemSelectedListener(this);

        mPickerDay.setMaximumWidthText("今天");
        mPickerHour.setMaximumWidthText("00");
        mPickerMinute.setMaximumWidthText("00");

        mPickerTitle = (TextView) findViewById(R.id.wheel_time_picker_title);
        mPickerCancel = (TextView) findViewById(R.id.wheel_time_picker_cancel);
        mPickerCommit = (TextView) findViewById(R.id.wheel_time_picker_commit);

        mPickerTitle.setText("时间");
        mPickerCancel.setOnClickListener(this);
        mPickerCommit.setOnClickListener(this);

        initDays();
        initMinutes();
    }

    private void initTime() {
        initStartTime();
        initTodayHours();
        initTomorrowHours();
    }

    private void initStartTime() {
        mTimeData = DateUtils.formatDate(mStartTime);
        mHour = DateUtils.formatHour(mStartTime);
        mMinute = DateUtils.formatMinute(mStartTime);

        mPickerDay.setSelectedItemPosition(0);
        mPickerMinute.setSelectedItemPosition(mMinute);
    }

    private void initDays() {
        mDayData = new ArrayList<>();
        mDayData.add("今天");
        mDayData.add("明天");
        mPickerDay.setData(mDayData);
        isToday = true;
    }

    private void initTodayHours() {
        mHourTodayData = new ArrayList<>();
        for (int i = mHour; i <= 23; i++) {
            if ((i + "").length() == 1)
                mHourTodayData.add("0" + i);
            else
                mHourTodayData.add(i + "");
        }
        mPickerHour.setData(mHourTodayData);
        mPickerHour.setSelectedItemPosition(0);
    }

    private void initTomorrowHours() {
        mHourTomorrowData = new ArrayList<>();
        for (int i = 0; i <= mHour; i++) {
            if ((i + "").length() == 1)
                mHourTomorrowData.add("0" + i);
            else
                mHourTomorrowData.add(i + "");
        }
    }

    private void initMinutes() {
        mMinuteData = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if ((i + "").length() == 1)
                mMinuteData.add("0" + i);
            else
                mMinuteData.add(i + "");
        }
        mPickerMinute.setData(mMinuteData);
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        if (picker.getId() == R.id.wheel_time_picker_hour) {
            mHour = Integer.valueOf(String.valueOf(data));
        } else if (picker.getId() == R.id.wheel_time_picker_minute) {
            mMinute = Integer.valueOf(String.valueOf(data));
        } else if (picker.getId() == R.id.wheel_time_picker_day) {
            upDataFormPosition(position);   //根据position更新数据
        }
    }

    private void upDataFormPosition(int position) {
        if (position == 0) {    //今天
            isToday = true;
            mPickerHour.setData(mHourTodayData);
            mHour = Integer.valueOf(mHourTodayData.get(0));
            mMinute = DateUtils.formatMinute(mStartTime);
            mPickerMinute.setSelectedItemPosition(mMinute);
        } else {
            isToday = false;
            mPickerHour.setData(mHourTomorrowData);
            mHour = Integer.valueOf(mHourTomorrowData.get(0));
            mMinute = Integer.valueOf(mMinuteData.get(0));
            mPickerMinute.setSelectedItemPosition(0);
        }
        mPickerHour.setSelectedItemPosition(0);
    }

    @Override
    public int getVisibleItemCount() {
        if (mPickerHour.getVisibleItemCount() == mPickerMinute.getVisibleItemCount())
            return mPickerHour.getVisibleItemCount();
        return 0;
    }

    @Override
    public void setVisibleItemCount(int count) {
        mPickerHour.setVisibleItemCount(count);
        mPickerMinute.setVisibleItemCount(count);
    }

    @Override
    public boolean isCyclic() {
        return mPickerHour.isCyclic() && mPickerMinute.isCyclic();
    }

    @Override
    public void setCyclic(boolean isCyclic) {
        mPickerHour.setCyclic(isCyclic);
        mPickerMinute.setCyclic(isCyclic);
    }

    @Override
    public void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener listener) {
        //设置条目选中的监听, 这个事linearlayout,所以不做处理
    }

    //获取选中条目的position,这里不作处理
    @Override
    public int getSelectedItemPosition() {
        return 0;
    }

    @Override
    public void setSelectedItemPosition(int position) {
    }

    @Override
    public int getCurrentItemPosition() {
        return 0;
    }

    @Override
    public List getData() {
        return null;
    }

    @Override
    public void setData(List data) {
    }

    @Override
    public void setSameWidth(boolean hasSameSize) {
    }

    @Override
    public boolean hasSameWidth() {
        return false;
    }

    @Override
    public void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener listener) {
    }

    @Override
    public String getMaximumWidthText() {
        return null;
    }

    @Override
    public void setMaximumWidthText(String text) {
    }

    @Override
    public int getMaximumWidthTextPosition() {
        return 0;
    }

    @Override
    public void setMaximumWidthTextPosition(int position) {
    }

    @Override
    public int getSelectedItemTextColor() {
        if (mPickerHour.getSelectedItemTextColor() == mPickerMinute.getSelectedItemTextColor())
            return mPickerHour.getSelectedItemTextColor();
        return 0;
    }

    @Override
    public void setSelectedItemTextColor(int color) {
        mPickerHour.setSelectedItemTextColor(color);
        mPickerMinute.setSelectedItemTextColor(color);
    }

    @Override
    public int getItemTextColor() {
        if (mPickerHour.getItemTextColor() == mPickerMinute.getItemTextColor())
            return mPickerHour.getItemTextColor();
        return 0;
    }

    @Override
    public void setItemTextColor(int color) {
        mPickerHour.setItemTextColor(color);
        mPickerMinute.setItemTextColor(color);
    }

    @Override
    public int getItemTextSize() {
        if (mPickerHour.getItemTextSize() == mPickerMinute.getItemTextSize())
            return mPickerHour.getItemTextSize();
        return 0;
    }

    @Override
    public void setItemTextSize(int size) {
        mPickerHour.setItemTextSize(size);
        mPickerMinute.setItemTextSize(size);
    }

    @Override
    public int getItemSpace() {
        if (mPickerHour.getItemSpace() == mPickerMinute.getItemSpace())
            return mPickerHour.getItemSpace();
        return 0;
    }

    @Override
    public void setItemSpace(int space) {
        mPickerHour.setItemSpace(space);
        mPickerMinute.setItemSpace(space);
    }

    @Override
    public void setIndicator(boolean hasIndicator) {
        mPickerHour.setIndicator(hasIndicator);
        mPickerMinute.setIndicator(hasIndicator);
    }

    @Override
    public boolean hasIndicator() {
        return mPickerHour.hasIndicator() && mPickerMinute.hasIndicator();
    }

    @Override
    public int getIndicatorSize() {
        if (mPickerHour.getIndicatorSize() == mPickerMinute.getIndicatorSize())
            return mPickerHour.getIndicatorSize();
        return 0;
    }

    @Override
    public void setIndicatorSize(int size) {
        mPickerHour.setIndicatorSize(size);
        mPickerMinute.setIndicatorSize(size);
    }

    @Override
    public int getIndicatorColor() {
        if (mPickerHour.getIndicatorColor() == mPickerMinute.getIndicatorColor())
            return mPickerHour.getIndicatorColor();
        return 0;
    }

    @Override
    public void setIndicatorColor(int color) {
        mPickerHour.setIndicatorColor(color);
        mPickerMinute.setIndicatorColor(color);
    }

    @Override
    public void setCurtain(boolean hasCurtain) {
        mPickerHour.setCurtain(hasCurtain);
        mPickerMinute.setCurtain(hasCurtain);
    }

    @Override
    public boolean hasCurtain() {
        return mPickerHour.hasCurtain() && mPickerMinute.hasCurtain();
    }

    @Override
    public int getCurtainColor() {
        if (mPickerHour.getCurtainColor() == mPickerMinute.getCurtainColor())
            return mPickerHour.getCurtainColor();
        return 0;
    }

    @Override
    public void setCurtainColor(int color) {
        mPickerHour.setCurtainColor(color);
        mPickerMinute.setCurtainColor(color);
    }

    @Override
    public void setAtmospheric(boolean hasAtmospheric) {
        mPickerHour.setAtmospheric(hasAtmospheric);
        mPickerMinute.setAtmospheric(hasAtmospheric);
    }

    @Override
    public boolean hasAtmospheric() {
        return mPickerHour.hasAtmospheric() && mPickerMinute.hasAtmospheric();
    }

    @Override
    public boolean isCurved() {
        return mPickerHour.isCurved() && mPickerMinute.isCurved();
    }

    @Override
    public void setCurved(boolean isCurved) {
        mPickerHour.setCurved(isCurved);
        mPickerMinute.setCurved(isCurved);
    }

    @Override
    public int getItemAlign() {
        return 0;
    }

    @Override
    public void setItemAlign(int align) {
    }

    @Override
    public Typeface getTypeface() {
        if (mPickerHour.getTypeface().equals(mPickerMinute.getTypeface()))
            return mPickerHour.getTypeface();
        return null;
    }

    @Override
    public void setTypeface(Typeface tf) {
        mPickerHour.setTypeface(tf);
        mPickerMinute.setTypeface(tf);
    }

    @Override
    public void setOnTimeConformListener(OnTimeConformListener listener) {
        mListener = listener;
    }

    @Override
    public long getSelectedTime() {
        return 0;
    }

    @Override
    public void setSelectedTime(long selectedTime) {
        if (selectedTime <= 0) {
            return;
        }
        mHour = DateUtils.formatHour(selectedTime);
        if (TimeUtils.isToday(selectedTime)) {  //是今天, 直接显示进去
            isToday = true;
            mPickerHour.setData(mHourTodayData);
            int selectedHour = mHourTodayData.size() + mHour - 24;
            if (selectedHour > 0) { //说明可以显示这个时间
                mPickerHour.setSelectedItemPosition(selectedHour);
            } else {    //不能显示 , 重置为当前时间
                mPickerHour.setSelectedItemPosition(0);
                mHour = Integer.valueOf(mHourTodayData.get(0));
            }
            mPickerDay.setSelectedItemPosition(0);
        } else {
            isToday = false;
            mPickerHour.setData(mHourTomorrowData);
            mPickerHour.setSelectedItemPosition(mHour);
            mPickerDay.setSelectedItemPosition(1);
        }
        mMinute = DateUtils.formatMinute(selectedTime);
        mPickerMinute.setSelectedItemPosition(mMinute);
    }

    @Override
    public void setStartTime(long startTime) {
        mStartTime = startTime;
        initTime();
    }

    @Override
    public long getStartTime() {
        return mStartTime;
    }

    @Override
    public long getCurrentTime() {
        return DateUtils.formatToLong(mTimeData + " " + mHour + ":" + mMinute, "yyyy-MM-dd HH:mm");
    }

    @Override
    public int getCurrentSelectedHourPosition() {
        return mPickerHour.getCurrentItemPosition();
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        if (v.getId() == R.id.wheel_time_picker_cancel) {   //取消
            mListener.onCancel();
        } else if (v.getId() == R.id.wheel_time_picker_commit) {    //确定
            //这里需要对这个时间的合法性进行判断如果大于开始的时间则直接使用,否则则加24个小时为明天
            if (isToday) {    //大于
                mListener.onTimeSelected(this, getCurrentTime());
            } else {
                mListener.onTimeSelected(this, getCurrentTime() + 1000 * 60 * 60 * 24);
            }
        }
    }

    public interface OnTimeConformListener {
        void onTimeSelected(WheelTime24Picker picker, long time);

        void onCancel();
    }
}
