package com.neishenme.what.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditSelfInfoActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.seny.android.utils.ALog;

import java.util.HashMap;

import static com.neishenme.what.R.id.view;

/**
 * Created by xmuSistone on 2016/5/23.
 */
public class DraggableItemView extends FrameLayout implements HttpLoader.ResponseListener {

    public static final int STATUS_LEFT_TOP = 0;
    public static final int STATUS_RIGHT_TOP = 1;
    public static final int STATUS_RIGHT_TOP_new = 2;
    public static final int STATUS_RIGHT_MIDDLE = 3;
    public static final int STATUS_RIGHT_MIDDLE_new = 4;
    public static final int STATUS_LEFT_BOTTOM = 5;
    public static final int STATUS_MIDDLE_BOTTOM = 6;
    public static final int STATUS_RIGHT_BOTTOM = 7;
    public static final int STATUS_RIGHT_BOTTOM_new = 8;


    public static final int SCALE_LEVEL_1 = 1; // 最大状态，缩放比例是100%
    public static final int SCALE_LEVEL_2 = 2; // 中间状态，缩放比例scaleRate
    public static final int SCALE_LEVEL_3 = 3; // 最小状态，缩放比例是smallerRate

    private ImageView imageView;
    private View maskView;
    private int status;
    private float scaleRate = 0.5f;
    private float smallerRate = scaleRate * 0.9f;
    private Spring springX, springY;
    private ObjectAnimator scaleAnimator;
    private boolean hasSetCurrentSpringValue = false;
    private DraggableSquareView parentView;
    private SpringConfig springConfigCommon = SpringConfig.fromOrigamiTensionAndFriction(140, 7);
    private SpringConfig springConfigDragging = SpringConfig.fromOrigamiTensionAndFriction(300, 6);
    private int anchorX = Integer.MIN_VALUE, anchorY = Integer.MIN_VALUE;
    private OnClickListener dialogListener;

    private String imagePath;
    private View addView;
    private TextView mDragItemText;
    CustDialog dialog;

    public DraggableItemView(Context context) {
        this(context, null);
    }

    public DraggableItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.drag_item, this);

        imageView = (ImageView) findViewById(R.id.drag_item_imageview);
        maskView = findViewById(R.id.drag_item_mask_view);
        mDragItemText = (TextView) findViewById(R.id.drag_item_text);

        addView = findViewById(R.id.add_view);

        dialogListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.pick_image) {
                    // 从相册选择图片
//                    if (dialog.s) {
//                    pickImageAndDelete((int) DraggableItemView.this.getTag());
//                    App.EDIT.putString("deletePhotoId", DraggableItemView.this.getTag() + "").commit();
//                    } else {
                    //选择照片
                    pickImage();
//                    }
                } else if (v.getId() == R.id.delete) {
                    // 删除
                    DeleteImage();
                } else {

//                    if (dialog.s) {
//                    App.EDIT.putString("deletePhotoId", DraggableItemView.this.getTag() + "").commit();
//                    captureImageAndDelete();
//                    } else {
                    //照相机照片
                    captureImage();
//                    }
                }
            }
        };

        //点击变小处理,不需要这段代码
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!hasSetCurrentSpringValue) {
                    adjustImageView();
                    hasSetCurrentSpringValue = true;
                }
            }
        });

        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDraggable() || getStatus() == STATUS_LEFT_TOP) {
                    dialog = new CustDialog(getContext(), CustDialog.ShowType.SHOW_EXPECT_DELETE);
                } else {
                    dialog = new CustDialog(getContext(), CustDialog.ShowType.SHOW_DELETE_ONLY);
                }
                dialog.setClickListener(dialogListener);
                dialog.show();
            }
        });

        initSpring();
    }

    //删除
    public void DeleteImage() {
        if (null != DraggableItemView.this.getTag()) {

            HashMap<String, String> param = new HashMap<>();
            param.put("token", NSMTypeUtils.getMyToken());
            param.put("photoIds", (int) DraggableItemView.this.getTag() + "");
            HttpLoader.post(ConstantsWhatNSM.URL_DELETE_PHOTO, param, SendSuccessResponse.
                    class, ConstantsWhatNSM.REQUEST_CODE_DELETE_PHOTO, DraggableItemView.this).setTag(this);
        }
    }

    //图片添加
    private void pickImage() {
        EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
        mainActivity.pickImage(status, isDraggable());
    }

    //照相添加
    private void captureImage() {
        EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
        mainActivity.captureImage(status, isDraggable());
    }

    //如果有先删除再添加
//    private void pickImageAndDelete(int i) {
//        EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
//        mainActivity.pickImageAndDelete(status, isDraggable(), i);
//    }

    //如果有先删除再添加
//    private void captureImageAndDelete() {
//        EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
//        mainActivity.captureImageAndDelete(status, isDraggable());
//
//    }

    /**
     * 初始化Spring相关
     */
    private void initSpring() {
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring();
        springY = mSpringSystem.createSpring();

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
            }
        });

        springX.setSpringConfig(springConfigCommon);
        springY.setSpringConfig(springConfigCommon);
    }

    /**
     * 调整ImageView的宽度和高度各为FrameLayout的一半
     */
    private void adjustImageView() {
        if (status != STATUS_LEFT_TOP) {
            imageView.setScaleX(scaleRate);
            imageView.setScaleY(scaleRate);

            maskView.setScaleX(scaleRate);
            maskView.setScaleY(scaleRate);
        }

        setCurrentSpringPos(getLeft(), getTop());
    }

    public void setScaleRate(float scaleRate) {
        this.scaleRate = scaleRate;
        this.smallerRate = scaleRate * 0.9f;
    }

    /**
     * 从一个状态切换到另一个状态
     */
    public void switchPosition(int toStatus) {
        if (this.status == toStatus) {
            throw new RuntimeException("程序错乱");
        }

        if (toStatus == STATUS_LEFT_TOP) {
            scaleSize(SCALE_LEVEL_1);
        } else if (this.status == STATUS_LEFT_TOP) {
            scaleSize(SCALE_LEVEL_2);
        }

        this.status = toStatus;
        Point point = parentView.getOriginViewPos(status);
        animTo(point.x, point.y);
    }

    public void animTo(int xPos, int yPos) {
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置缩放大小
     */
    public void scaleSize(int scaleLevel) {
        float rate = scaleRate;
        if (scaleLevel == SCALE_LEVEL_1) {
            rate = 1.0f;
        } else if (scaleLevel == SCALE_LEVEL_3) {
            rate = smallerRate;
        }

        if (scaleAnimator != null && scaleAnimator.isRunning()) {
            scaleAnimator.cancel();
        }

        scaleAnimator = ObjectAnimator
                .ofFloat(this, "custScale", imageView.getScaleX(), rate)
                .setDuration(200);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.start();
    }

    public void saveAnchorInfo(int downX, int downY) {
        int halfSide = getMeasuredWidth() / 2;
        anchorX = downX - halfSide;
        anchorY = downY - halfSide;
    }

    /**
     * 真正开始动画
     */
    public void startAnchorAnimation() {
        if (anchorX == Integer.MIN_VALUE || anchorY == Integer.MIN_VALUE) {
            return;
        }

        springX.setOvershootClampingEnabled(true);
        springY.setOvershootClampingEnabled(true);
        springX.setSpringConfig(springConfigDragging);
        springY.setSpringConfig(springConfigDragging);
        animTo(anchorX, anchorY);
        scaleSize(DraggableItemView.SCALE_LEVEL_3);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    /**
     * 设置当前spring位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setParentView(DraggableSquareView parentView) {
        this.parentView = parentView;
    }

    public void onDragRelease() {
        if (status == DraggableItemView.STATUS_LEFT_TOP) {
            scaleSize(DraggableItemView.SCALE_LEVEL_1);
        } else {
            scaleSize(DraggableItemView.SCALE_LEVEL_2);
        }

        springX.setOvershootClampingEnabled(false);
        springY.setOvershootClampingEnabled(false);
        springX.setSpringConfig(springConfigCommon);
        springY.setSpringConfig(springConfigCommon);

        Point point = parentView.getOriginViewPos(status);
        setCurrentSpringPos(getLeft(), getTop());
        animTo(point.x, point.y);
    }

    public void fillImageView(String imagePath) {
        this.imagePath = imagePath;
        addView.setVisibility(View.GONE);
        if (status == STATUS_LEFT_TOP) {
            mDragItemText.setVisibility(VISIBLE);
        } else {
            mDragItemText.setVisibility(INVISIBLE);
        }
        ImageLoader.getInstance().displayImage(imagePath, imageView);
    }

    public void fillImageView(String imagePath, final int degress) {
        this.imagePath = imagePath;
        addView.setVisibility(View.GONE);
        if (status == STATUS_LEFT_TOP) {
            mDragItemText.setVisibility(VISIBLE);
        } else {
            mDragItemText.setVisibility(INVISIBLE);
        }
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(imagePath, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Matrix m = new Matrix();
                if (degress != 0) {
                    m.setRotate(degress);
                } else {
                    m = null;
                }
                int width = loadedImage.getWidth();
                int height = loadedImage.getHeight();
                loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, height, m, true);
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                failReason.getCause();
                Log.d("editself", " failReason.getCause()" + failReason.toString());
            }
        });
    }

    // 以下两个get、set方法是为自定义的属性动画CustScale服务，不能删
    public void setCustScale(float scale) {
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        maskView.setScaleX(scale);
        maskView.setScaleY(scale);
    }

    public float getCustScale() {
        return imageView.getScaleX();
    }

    public void updateEndSpringX(int dx) {
        springX.setEndValue(springX.getEndValue() + dx);
    }

    public void updateEndSpringY(int dy) {
        springY.setEndValue(springY.getEndValue() + dy);
    }

    public boolean isDraggable() {
        return imagePath != null;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_DELETE_PHOTO && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                imagePath = null;
                imageView.setImageBitmap(null);
                addView.setVisibility(View.VISIBLE);
                parentView.onDedeleteImage(DraggableItemView.this);
                EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
                mainActivity.showToastSuccess("操作成功");
                setTag(null);
            } else {
                EditSelfInfoActivity mainActivity = (EditSelfInfoActivity) getContext();
                mainActivity.showToastError("操作失败,请重试");
            }

            for (int i = 0; i < 9; i++) {
                ALog.i("第" + i + "个图片的tag为" + parentView.getChildAt(i).getTag() + "");
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}

