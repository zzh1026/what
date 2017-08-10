package com.neishenme.what.view.wheeltime;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/2/21.
 */

public interface IWheelTime24Picker {
    void setOnTimeConformListener(WheelTime24Picker.OnTimeConformListener listener);

    long getSelectedTime();

    void setSelectedTime(long selectedTime);

    //设置起始时间和终止时间,暂时用不到,所以不提供
//    void setWheelTimes(long timeStart, Long timeEnd);

    void setStartTime(long startTime);

    //用不到
//    void setEndTime(long endTime);

    long getStartTime();

//    long getEndTime();

    long getCurrentTime();  //获取最终选中的时间

    int getCurrentSelectedHourPosition();   //获取当前选中的小时的position
}
