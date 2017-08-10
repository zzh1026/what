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
public class CustDialog extends AlertDialog {

    private Button button1, button2;
    private View.OnClickListener btnListener;
    private View.OnClickListener innerListener;
    public ShowType showType;

    protected CustDialog(Context context) {
        super(context);
        showType = ShowType.SHOW_DELETE_ONLY;
    }

    public CustDialog(Context context, ShowType showType) {
        super(context);
        this.showType = showType;
    }

    public CustDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickListener(View.OnClickListener btnListener) {
        this.btnListener = btnListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_dialog);

        innerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnListener.onClick(v);
                dismiss();
            }
        };

        View pickImage = findViewById(R.id.pick_image);
        View delete = findViewById(R.id.delete);
        View capture = findViewById(R.id.capture_image);

        capture.setOnClickListener(innerListener);
        delete.setOnClickListener(innerListener);
        pickImage.setOnClickListener(innerListener);

        if (showType == ShowType.SHOW_EXPECT_DELETE) {
            pickImage.setVisibility(View.VISIBLE);
            capture.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        } else {
            pickImage.setVisibility(View.GONE);
            capture.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }
    }

    public enum ShowType {
        SHOW_DELETE_ONLY,
        SHOW_EXPECT_DELETE;
    }
}
