package com.neishenme.what.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.view.loopview.LoopView;
import com.neishenme.what.view.loopview.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Created by gengxin on 2015/11/19.
 */
public class WheelDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private ArrayList<String> items;
    private int number;
    private LoopView loop;
    private ImageView cancel,commit;
    private TextView wheelDialogTitle;
    private String dialogTitle;

    public WheelDialog(Context context, ArrayList<String> items, String title) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE,true,true);
        setContentView(R.layout.dialog_wheel);
        this.context = context;
        this.items = items;
        dialogTitle=title;
        bindView();
        initData();
        setListeners();

    }

    public void bindView() {
        wheelDialogTitle= (TextView) findViewById(R.id.wheel_dialog_title);
        loop = (LoopView) findViewById(R.id.loopview);
        cancel = (ImageView) findViewById(R.id.cancel);
        commit = (ImageView) findViewById(R.id.commit);
    }

    OnWheelSelecteListeners onwheelSelecteListeners;
    public final void setWheelListeners(OnWheelSelecteListeners wheelListeners ) {
        onwheelSelecteListeners = wheelListeners;
    }

    public interface OnWheelSelecteListeners {
        void onWheelSelected(int index);
    }

    public void setListeners() {
        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        loop.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "Item " + index);
                number=index;
            }
        });
    }

    public void initData() {
        //设置dialog标题
        wheelDialogTitle.setText(dialogTitle);
        //设置原始数据
        loop.setItems(items);
        //设置初始位置
        loop.setInitPosition(0);
        //设置字体大小
        loop.setTextSize(15);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确定
            case R.id.commit:
                onwheelSelecteListeners.onWheelSelected(number);
                this.dismiss();
                break;

            //取消
            case R.id.cancel:
                this.dismiss();
                break;
        }

    }


}
