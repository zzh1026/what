package com.neishenme.what.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.galleryfinal.FunctionConfig;
import com.neishenme.what.galleryfinal.GalleryFinal;
import com.neishenme.what.utils.ConstanceRequest;

import java.util.ArrayList;

/**
 * Created by gengxin on 2015/11/19.
 */
public class PickerPicsDialog extends BaseDialog implements View.OnClickListener {
    private Activity activity;
    private ArrayList<String> photoPaths;
    private int requestCode;
    private TextView cameraChoice;
    private TextView ablumChoice;
    private TextView cancleChoice;

    private FunctionConfig functionConfig;
    private GalleryFinal.OnHanlderResultCallback onHanlderResultCallback;

    public PickerPicsDialog(Activity activity,FunctionConfig functionConfig, GalleryFinal.OnHanlderResultCallback onHanlderResultCallback) {
        super(activity, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_picker_pic);
        this.activity = activity;
        this.functionConfig = functionConfig;
        this.onHanlderResultCallback = onHanlderResultCallback;
        bindView();
        setListeners();
        initData();
    }

    public void bindView() {
        cameraChoice = (TextView) findViewById(R.id.pic_camera);
        ablumChoice = (TextView) findViewById(R.id.pic_album);
        cancleChoice = (TextView) findViewById(R.id.pic_cancle);
    }

    public void setListeners() {
        cameraChoice.setOnClickListener(this);
        ablumChoice.setOnClickListener(this);
        cancleChoice.setOnClickListener(this);
    }

    public void initData() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //照相
            case R.id.pic_camera:
                GalleryFinal.openCamera(ConstanceRequest.REQUEST_CODE_BACKGROUND, functionConfig, onHanlderResultCallback);
                break;
            //相册
            case R.id.pic_album:
                GalleryFinal.openGallerySingle(ConstanceRequest.REQUEST_CODE_BACKGROUND, functionConfig, onHanlderResultCallback);
                break;
            //取消
            case R.id.pic_cancle:
                this.dismiss();
                break;
        }

    }
}
