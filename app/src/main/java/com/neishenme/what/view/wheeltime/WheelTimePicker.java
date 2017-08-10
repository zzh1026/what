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

import org.seny.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/2/21.
 */

public class WheelTimePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener,
        IWheelPicker, IWheelTimePicker, View.OnClickListener {

    private TextView mPickerTitle;
    private WheelPicker mPickerHour;
    private WheelPicker mPickerMinute;
    private TextView mPickerCancel;
    private TextView mPickerCommit;

    private OnTimeConformListener mListener;

    private String mTimeData;   //记录当前的时间 年-月-日

    private int mHour, mMinute; //记录当前选中的小时和分钟

    private List<String> mHourData;     //可选择的小时集合
    private List<String> mMinuteNormal; //普通的可选择的分钟
    private List<String> mMinuteFirst;  //第一波的可选择的分钟
    private int lastSelectedPosition = 0;//上一次选中的position,用来判断数据是否需要更新

    private long mStartTime = System.currentTimeMillis() + (80 * 1000);    //起始的时间,默认80m之后

    public WheelTimePicker(Context context) {
        this(context, null);
    }

    public WheelTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_wheel_time_picker, this);

        mPickerHour = (WheelPicker) findViewById(R.id.wheel_time_picker_hour);
        mPickerMinute = (WheelPicker) findViewById(R.id.wheel_time_picker_minute);
        mPickerHour.setOnItemSelectedListener(this);
        mPickerMinute.setOnItemSelectedListener(this);

        mPickerHour.setMaximumWidthText("00");
        mPickerMinute.setMaximumWidthText("00");

        mPickerTitle = (TextView) findViewById(R.id.wheel_time_picker_title);
        mPickerCancel = (TextView) findViewById(R.id.wheel_time_picker_cancel);
        mPickerCommit = (TextView) findViewById(R.id.wheel_time_picker_commit);

        mPickerTitle.setText("时间");
        mPickerCancel.setOnClickListener(this);
        mPickerCommit.setOnClickListener(this);

        initMinuteNormal();
//        mHour = mPickerHour.getCurrentItemPosition();
//        mMinute = mPickerMinute.getCurrentItemPosition();
    }

    private void initTime() {
        initStartTime();
        initHour();
        initMinuteFirst();
    }

    private void initStartTime() {
        mTimeData = DateUtils.formatDate(mStartTime);
        mHour = DateUtils.formatHour(mStartTime);
        mMinute = DateUtils.formatMinute(mStartTime);
    }

    private void initHour() {
        mHourData = new ArrayList<>();
        for (int i = mHour; i <= 23; i++) {
            if ((i + "").length() == 1)
                mHourData.add("0" + i);
            else
                mHourData.add(i + "");
        }
        mPickerHour.setData(mHourData);
        mPickerHour.setSelectedItemPosition(0);
        lastSelectedPosition = 0;
    }

    private void initMinuteFirst() {
        mMinuteFirst = new ArrayList<>();
        for (int i = mMinute; i <= 59; i++) {
            if ((i + "").length() == 1)
                mMinuteFirst.add("0" + i);
            else
                mMinuteFirst.add(i + "");
        }
        mPickerMinute.setData(mMinuteFirst);
        mPickerMinute.setSelectedItemPosition(0);
    }

    private void initMinuteNormal() {
        mMinuteNormal = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if ((i + "").length() == 1)
                mMinuteNormal.add("0" + i);
            else
                mMinuteNormal.add(i + "");
        }
    }

    private void updateMinuteDate(int position) {
        int currentPosition = mPickerMinute.getCurrentItemPosition();
        if (position == 0) {
            if (mMinuteFirst != null) {
                mPickerMinute.setData(mMinuteFirst);
                mPickerMinute.setSelectedItemPosition(currentPosition - (60 - mMinuteFirst.size()));
            } else {
                initMinuteFirst();
            }
            mMinute = Integer.parseInt(mMinuteFirst.get(mPickerMinute.getSelectedItemPosition()));
        } else {
            if (mMinuteNormal == null) {
                initMinuteNormal();
            }
            mPickerMinute.setData(mMinuteNormal);
            mPickerMinute.setSelectedItemPosition(currentPosition + (60 - mMinuteFirst.size()));
            mMinute = Integer.parseInt(mMinuteNormal.get(mPickerMinute.getSelectedItemPosition()));
        }
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        if (picker.getId() == R.id.wheel_time_picker_hour) {
            mHour = Integer.valueOf(String.valueOf(data));
            if ((position == 0 && lastSelectedPosition != 0) || (position != 0 && lastSelectedPosition == 0)) {
                lastSelectedPosition = position;
                updateMinuteDate(position);
            }
        } else if (picker.getId() == R.id.wheel_time_picker_minute) {
            mMinute = Integer.valueOf(String.valueOf(data));
        }
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
    public void setSelectedTime(int position) {
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
            mListener.onTimeSelected(this, getCurrentTime());
        }
    }

    public interface OnTimeConformListener {
        void onTimeSelected(WheelTimePicker picker, long time);

        void onCancel();
    }
}
