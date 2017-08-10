package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.galleryfinal.FunctionConfig;
import com.neishenme.what.galleryfinal.GalleryFinal;
import com.neishenme.what.utils.ConstanceRequest;

/**
 * Created by Administrator on 2016/5/27.
 */
public class PhotoWallDialog extends BaseDialog implements View.OnClickListener {
    private TextView tvDelete;
    private TextView tvChange;
    private FunctionConfig functionConfig;
    private GalleryFinal.OnHanlderResultCallback onHanlderResultCallback;
    private DeletePhotoListener deletePhotoListener;

    public interface DeletePhotoListener {
        void deleteListener();
    }


    public PhotoWallDialog(Context context, FunctionConfig functionConfig, GalleryFinal.OnHanlderResultCallback onHanlderResultCallback, DeletePhotoListener deletePhotoListener) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_photo_wall);
        this.functionConfig = functionConfig;
        this.onHanlderResultCallback = onHanlderResultCallback;
        this.deletePhotoListener = deletePhotoListener;
        initView();
        setListeners();
    }

    public void initView() {
        tvDelete = (TextView) findViewById(R.id.tv_cancle);
        tvChange = (TextView) findViewById(R.id.tv_change);
    }

    public void setListeners() {
        tvDelete.setOnClickListener(this);
        tvChange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //删除
            case R.id.tv_cancle:
                try {
                    deletePhotoListener.deleteListener();
                } catch (Exception e) {

                }

                break;
            //更换图片
            case R.id.tv_change:
                GalleryFinal.openGallerySingle(ConstanceRequest.REQUEST_CODE_PHOTO_WALL, functionConfig, onHanlderResultCallback);
                break;

        }

    }

}
