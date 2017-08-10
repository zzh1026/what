package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.ReleaseQuickActivity;
import com.neishenme.what.view.loopview.LoopView;
import com.neishenme.what.view.loopview.OnItemSelectedListener;
import com.neishenme.what.view.wheeltime.WheelTime24Picker;
import com.neishenme.what.view.wheeltime.WheelTimePicker;

import org.seny.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.dialogTitle;
import static com.neishenme.what.R.id.commit;
import static com.neishenme.what.R.id.tv_title;
import static com.neishenme.what.dialog.MyDatePickerDialog.DEFAULT_YEAR;

/**
 * Created by zzh
 */
public class ReleaseTimePickerDialog extends BaseDialog {
    public static final int RELEASE_QUICK = 1;
    public static final int RELEASE_NORMAL = 2;
    private int mReleaseType = RELEASE_QUICK;
    private OnConfirmListeners mOnConfirmListeners;
    private WheelTime24Picker mWheelTimePicker;
    private ReleaseQuickActivity mainActivity;
    private long mStartTime;

    public ReleaseTimePickerDialog(Context context, long startTime, int releaseType, OnConfirmListeners onConfirmListeners) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_release_time);
        mReleaseType = releaseType;
        mainActivity = (ReleaseQuickActivity) context;
        mStartTime = startTime;
        mOnConfirmListeners = onConfirmListeners;
        bindView();
        initData();
    }

    public void bindView() {
        mWheelTimePicker = (WheelTime24Picker) findViewById(R.id.wheel_time_picker);
    }

    public void initData() {
        mWheelTimePicker.setOnTimeConformListener(new WheelTime24Picker.OnTimeConformListener() {
            @Override
            public void onTimeSelected(WheelTime24Picker picker, long time) {
                if (mOnConfirmListeners != null) {
                    if (mReleaseType == RELEASE_QUICK) {
                        if (time < (System.currentTimeMillis() + 1000 * 60)) {
                            mainActivity.showToast("时间须至少在一分钟之后");
                            return;
                        }
                    } else {
                        if (time < (System.currentTimeMillis() + 1000 * 60 * 60)) {
                            mainActivity.showToast("时间须至少在一小时之后");
                            return;
                        }
                    }
                    if (time > (System.currentTimeMillis() + 1000 * 60 * 60 * 24)) {
                        mainActivity.showToast("时间须在24小时之内");
                        return;
                    }
                    mOnConfirmListeners.onConfirmClicked(time);
                    dismiss();
                }
            }

            @Override
            public void onCancel() {
                dismiss();
            }
        });
        mWheelTimePicker.setStartTime(mStartTime);
    }

    public void setSelectedTime(long time) {
        mWheelTimePicker.setSelectedTime(time);
    }

    public interface OnConfirmListeners {
        void onConfirmClicked(long selectedTime);
    }
}
