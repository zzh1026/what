package com.neishenme.what.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/27.
 */
public class CustDialogBg extends AlertDialog {

    private View.OnClickListener btnListener;
    private View.OnClickListener innerListener;


    public CustDialogBg(Context context) {
        super(context);
    }

    public CustDialogBg(Context context, int theme) {
        super(context, theme);
    }

    public CustDialogBg(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickListener(View.OnClickListener btnListener) {
        this.btnListener = btnListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_dialog_bg);

        innerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnListener.onClick(v);
                dismiss();
            }
        };

        View pickImage = findViewById(R.id.pick_image);
        View capture = findViewById(R.id.capture_image);
        Button mPickCacel = (Button) findViewById(R.id.pick_cacel);


        capture.setOnClickListener(innerListener);
        pickImage.setOnClickListener(innerListener);
        mPickCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
