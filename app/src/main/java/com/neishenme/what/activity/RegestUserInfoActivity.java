package com.neishenme.what.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.LoginQuickSuccessResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.UpPhotoBean;
import com.neishenme.what.dialog.SelectHeadPicDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnBirthdayTimeSelect;
import com.neishenme.what.nsminterface.UpLoadResponseListener;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.FileUtil;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.SoftInputUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.utils.UpdataPersonInfo;
import com.neishenme.what.view.CircleImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.soundcloud.android.crop.Crop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;
import org.seny.android.utils.MyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：zhaozh create on 2016/5/13 12:18
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 注册帐号以后初始化个人信息界面, 因为用户注册完毕后必须有性别昵称等信息,所以跳转这个界面
 * .
 * 其作用是 :
 */
public class RegestUserInfoActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_LOCAL = 2;
    public static final int REQUEST_CODE_CROP = 3;

    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private CircleImageView mRegestUserHead;
    private EditText mRegestUserName;
    private EditText mRegestUserSign;
    private TextView mRegestUserBirthday;
    private RadioGroup mRgGenderType;
    private Button mRegestUserinfoBtn;

    String birthday = null;
    private String token;
    private String userName;
    private String userSign;
    private String userBirthday;
    private SelectHeadPicDialog selectHeadPicDialog;

    private int oriention;
    private String logo = null;

    private boolean hasPhoto, hasUsername, hasSign, hasBirthday, hasGender = false;

    @Override
    protected int initContentView() {
        return R.layout.activity_regestuserinfo;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);

        mRegestUserHead = (CircleImageView) findViewById(R.id.regest_user_head);

        mRegestUserName = (EditText) findViewById(R.id.regest_user_name);
        mRegestUserSign = (EditText) findViewById(R.id.regest_user_sign);
        mRegestUserBirthday = (TextView) findViewById(R.id.regest_user_birthday);

        mRgGenderType = (RadioGroup) findViewById(R.id.rg_gender_type);

        mRegestUserinfoBtn = (Button) findViewById(R.id.regest_userinfo_btn);
    }

    @Override
    protected void initListener() {
        //mIvActionbarLeft.setOnClickListener(this);
        mRegestUserHead.setOnClickListener(this);
        mRegestUserBirthday.setOnClickListener(this);
        mRegestUserinfoBtn.setOnClickListener(this);
        mRgGenderType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hasGender = true;
                refreshSubmitBtn();
            }
        });

        mRegestUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasUsername = !TextUtils.isEmpty(s.toString());
                refreshSubmitBtn();
            }
        });

        mRegestUserSign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasSign = !TextUtils.isEmpty(s.toString());
                refreshSubmitBtn();
            }
        });
    }

    @Override
    protected void initData() {
        mIvActionbarLeft.setVisibility(View.INVISIBLE);
        mTvActionbarMiddle.setText("填写个人资料");
        token = getIntent().getStringExtra("data");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regest_user_head: //点击头像
                requestPermission();
                break;
            case R.id.regest_user_birthday: //点击出生日期
                editBirthday();
                break;
            case R.id.regest_userinfo_btn:  //点击完成
                trySuccess();
                break;
            case R.id.iv_actionbar_left:    //点击返回
                finish();
                break;
        }
    }

    private void requestPermission() {
        if (AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
            showSelectPic();
        } else {
            AndPermission.with(this)
                    .requestCode(MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO)
                    .permission(Manifest.permission.CAMERA)
                    .rationale(mRationaleListener)
                    .send();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO:
                    AndPermission.rationaleDialog(RegestUserInfoActivity.this, rationale).show();
                    break;
            }
        }
    };

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO) {
                showSelectPic();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            if (requestCode == MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO) {
                MPermissionManager.showToSetting(RegestUserInfoActivity.this,
                        MPermissionManager.REQUEST_CODE_CAMERA_HEADER_PHOTO, new MPermissionManager.OnNegativeListner() {
                            @Override
                            public void onNegative() {

                            }
                        });
            }
        }
    };

    private void showSelectPic() {
        selectHeadPicDialog = new SelectHeadPicDialog(this);
        selectHeadPicDialog.show();
    }

    public void editBirthday() {
        long time;
        final String birthdayTime = mRegestUserBirthday.getText().toString().trim();
        time = DateUtils.formatToLong(birthdayTime, "yyyy-MM-dd");
        if (0 >= time) {
            time = DateUtils.formatToLong("1995-10-10", "yyyy-MM-dd");
        }
        TimeUtils.setBirthdayTime(this, time, new OnBirthdayTimeSelect() {
            @Override
            public void onTimeSelect(long time) {
                birthday = DateUtils.formatDate(time);
                mRegestUserBirthday.setText(birthday);
                hasBirthday = true;
                refreshSubmitBtn();
            }
        });
    }

    //尝试完成;
    private void trySuccess() {
        setButtonState("正在完成", false);
        userName = mRegestUserName.getText().toString().trim();
        userSign = mRegestUserSign.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userSign)) {
            setButtonState("完成", true);
            return;
        }

        if (birthday == null) {
            userBirthday = String.valueOf(System.currentTimeMillis() - (long) 20 * 365 * 24 * 60 * 60 * 1000);
        } else {
            userBirthday = String.valueOf(DateUtils.formatToLong(birthday, "yyyy-MM-dd"));
        }

        if ("3".equals(getTarget())) {
            showToastWarning("请选择性别");
            setButtonState("完成", true);
            return;
        }

        //头像使用缩略图
        if (TextUtils.isEmpty(logo)) {
            showToastInfo("上传头像");
            setButtonState("完成", true);
            return;
        }

        saveUserInfo();
    }

    private void saveUserInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("birthDay", userBirthday);
        params.put("name", userName);
        params.put("gender", getTarget());
        params.put("sign", userSign);

        HttpLoader.post(ConstantsWhatNSM.URL_REGEST_UPDATE_BASEINFO, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_REGEST_UPDATE_BASEINFO, this, false).setTag(this);
    }

    private void refreshSubmitBtn() {
        mRegestUserinfoBtn.setEnabled(hasPhoto && hasUsername && hasSign && hasBirthday && hasGender);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_REGEST_UPDATE_BASEINFO
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse successResponse = (SendSuccessResponse) response;
            int code = successResponse.getCode();
            if (1 == code) {
                loginNet();

                //完成这里代表首次注册成功
//                App.EDIT.putBoolean("is_regest_user", true).commit();
            } else {
                MyToast.showConterToast(this, successResponse.getMessage());
                setButtonState("完成", true);
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_LOGIN_BYTOKEN
                && response instanceof LoginQuickSuccessResponse) {
            LoginQuickSuccessResponse loginResponse = (LoginQuickSuccessResponse) response;
            int code = loginResponse.getCode();
            if (1 == code) {
                LoginQuickSuccessResponse.DataEntity.UserEntity user = loginResponse.getData().getUser();
                if ("1".equals(user.getGender()) || "2".equals(user.getGender())) {
                    user.setToken(token);
                    //将数据进行更新.
                    UpdataPersonInfo.updatePersonInfoByToken(loginResponse.getData());
                    ActivityUtils.startActivityAndFinish(this, MainActivity.class);
                } else {
                    showToastError("更新信息失败,可能是网络缓慢,请重试");
                    setButtonState("完成", true);
                }
            } else {
                showToastError("自动登录失败,请自行登录");
                ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_REGEST_UPDATE_BASEINFO) {
            showToastError("网络连接失败");
            setButtonState("完成", true);
        }
    }

    //注册成功登录
    private void loginNet() {
        SoftInputUtils.UpSoftInputUtils();
        if (TextUtils.isEmpty(token)) {
            ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("platform", "2");
        params.put("identifier", AppManager.getIMEI());
        params.put("version", PackageVersion.getPackageVersion(this));
        HttpLoader.get(ConstantsWhatNSM.URL_LOGIN_BYTOKEN, params, LoginQuickSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_LOGIN_BYTOKEN, this, false).setTag(this);
    }


    private String getTarget() {
        String target = "3";
        switch (mRgGenderType.getCheckedRadioButtonId()) {
            case R.id.rb_gender_male:
                target = "1";
                break;
            case R.id.rb_gender_femal:
                target = "2";
                break;
            default:
                target = "3";
                break;
        }
        return target;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectHeadPicDialog.dismiss();
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    beginCrop(Uri.fromFile(Crop.file), REQUEST_CODE_CROP);
                }
                break;
            case REQUEST_CODE_CROP:
                handleCrop(resultCode, data);
                break;
            case REQUEST_CODE_LOCAL:
                if (resultCode == Activity.RESULT_OK) {
                    //tryCropImg(data.getData(), photoFile);
                    beginCrop(data.getData(), REQUEST_CODE_CROP);
                }
                break;
            default:
                break;
        }
    }

    private void tryUpHeadPic(Uri photoFile) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoFile));
            ALog.w("bitmap 的大小为: " + bitmap.getRowBytes() + " * " + bitmap.getHeight()
                    + " = " + (bitmap.getRowBytes() * bitmap.getHeight()));
            mRegestUserHead.setImageBitmap(bitmap);
            showToastSuccess("获取成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToastSuccess("获取失败");
        }
    }

    private void tryCropImg(Uri photoFile) {
        tryCropImg(photoFile, photoFile);
    }

    private void tryCropImg(Uri data, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    //设置按钮状态和显示信息
    private void setButtonState(String text, boolean clickable) {
        mRegestUserinfoBtn.setText(text);
        mRegestUserinfoBtn.setClickable(clickable);
    }

    public void pickImage() {
        Crop.pickImage(this, REQUEST_CODE_LOCAL);
    }

    public void captureImage() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Crop.captureImage(this, REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    private void beginCrop(Uri source, int resultCode) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        String imagePath = FileUtil.getRealFilePath(this, source);
        if (null == imagePath) {
            imagePath = source.getPath();
        }
        oriention = getBitmapDegree(imagePath);
        Crop.of(source, destination).asSquare(oriention).start(this, resultCode);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();

            final HashMap<String, String> map_file = new HashMap<>();
            map_file.put("logo", imagePath.replace("file://", ""));

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(imagePath, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Matrix m = new Matrix();
                    if (oriention != 0) {
                        m.setRotate(oriention);
                    } else {
                        m = null;
                    }
                    int width = loadedImage.getWidth();
                    int height = loadedImage.getHeight();
                    loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, height, m, true);
                    Log.d("editself", "loadedImage.getHeight()" + loadedImage.getHeight() + "");
                    mRegestUserHead.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    failReason.getCause();
                    Log.d("editself", " failReason.getCause()" + failReason.toString());
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", token);
                    map.put("photoState", "0");
                    httpRequestPhoto(ConstantsWhatNSM.URL_UPDATE_LOGO, map, map_file, oriention);
                }
            }).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public String httpRequestPhoto(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, int oriention) {
        return HttpLoader.upLoadPic(urlStr, textMap, fileMap, oriention, new UpLoadResponseListener() {
            @Override
            public void onResponseSuccess(String requestString) {
                Gson gson = new Gson();
                UpPhotoBean upPhotoBean = gson.fromJson(requestString, UpPhotoBean.class);
                if (upPhotoBean.getCode() == 1) {
                    showToastSuccess("成功");
                    logo = upPhotoBean.getData().getLogo();
                    hasPhoto = true;
                    refreshSubmitBtn();
                } else {
                    showToastError("上传失败,请重试");
                    mRegestUserHead.setImageResource(R.drawable.picture_moren);
                }
            }

            @Override
            public void onResponseError(Exception e) {
                showToastError("上传失败,请重试");
                mRegestUserHead.setImageResource(R.drawable.picture_moren);
                e.printStackTrace();
            }
        });
    }
}
