package com.neishenme.what.dialog;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.neishenme.what.R;
import com.neishenme.what.activity.RegestUserInfoActivity;

import java.io.File;

/**
 * 作者：zhaozh create on 2016/6/9 15:48
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SelectHeadPicDialog extends BaseDialog implements View.OnClickListener {
    private RegestUserInfoActivity mActivity;
    private Button mPickerFromAlbumBtn;
    private Button mPickerFromCameraBtn;
    private Button mPickerFromCancelBtn;
    private File file;

    public SelectHeadPicDialog(RegestUserInfoActivity mActivity) {
        super(mActivity, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, false, false);
        setContentView(R.layout.dialog_picker_head_pics);
        this.mActivity = mActivity;
        initView();
        initListener();
    }

    private void initView() {
        mPickerFromAlbumBtn = (Button) findViewById(R.id.picker_from_album_btn);
        mPickerFromCameraBtn = (Button) findViewById(R.id.picker_from_camera_btn);
        mPickerFromCancelBtn = (Button) findViewById(R.id.picker_from_cancel_btn);

    }

    private void initListener() {
        mPickerFromAlbumBtn.setOnClickListener(this);
        mPickerFromCameraBtn.setOnClickListener(this);
        mPickerFromCancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //相册
            case R.id.picker_from_album_btn:
                //openAlbum();
                pickImage();
                break;
            //照相机
            case R.id.picker_from_camera_btn:
                //openCamera();
                captureImage();
                break;
            //取消
            case R.id.picker_from_cancel_btn:
                dismiss();
                break;
        }
    }

    //图片添加
    private void pickImage() {
        mActivity.pickImage();
    }

    //照相添加
    private void captureImage() {
        mActivity.captureImage();
    }

    private void openAlbum() {
        File file = getFile();
        if (file == null) {
            mActivity.showToastError("获取错误");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//
//        mActivity.startActivityForResult(intent, RegestUserInfoActivity.REQUEST_CODE_LOCAL);

//        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//        getAlbum.setType("image/*");
        //mActivity.startActivityForResult(intent, RegestUserInfoActivity.REQUEST_CODE_CROP);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
//        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        //不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        mActivity.startActivityForResult(intent, RegestUserInfoActivity.REQUEST_CODE_CROP);

    }

    private void openCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            mActivity.showToastWarning("SD卡不存在，不能拍照");
            return;
        }
        File file = getFile();
        if (file == null) {
            mActivity.showToastError("获取错误");
        }
        mActivity.startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)),
                RegestUserInfoActivity.REQUEST_CODE_CAMERA);
    }

    public Uri getPhotoFile() {
        return Uri.fromFile(file);
    }

    private File getFile() {
        file = new File(Environment.getExternalStorageDirectory() + "/NSM_Photo/", "headerPhoto.jpg");
        if (file.exists()) {
            file.delete();
        }
        //try {
        file.getParentFile().mkdirs();
        //file.createNewFile();
        //} catch (IOException e) {
        //e.printStackTrace();
        //file = null;
        //}
        return file;
    }
}
