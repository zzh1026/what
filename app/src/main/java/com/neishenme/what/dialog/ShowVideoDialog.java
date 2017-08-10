package com.neishenme.what.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditSelfInfoActivity;
import com.neishenme.what.activity.PlayVideoActivity;
import com.neishenme.what.activity.ReleaseQuickActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.FileUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ShowVideoDialog extends BaseDialog implements View.OnClickListener {
    private EditSelfInfoActivity context;
    private ReleaseQuickActivity context2;
    private String videoUrl;
    private String videoThumb;

    private ImageView mVideoThumbIv;

    private LinearLayout mLoadingProcress;
    private ImageView mIvProgressBar;
    private TextView mTvProgressBar;

    private Button mBtnVideoDelete;
    private Button mBtnVideoComplete;

    private File sdVideoDir = FileUtil.getSDVideoDir();
    private File file;
    private Bitmap bitmap;

    public ShowVideoDialog(EditSelfInfoActivity context, String videoUrl, String videoThumb) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.pop_video);
        this.context = context;
        this.videoUrl = videoUrl;
        this.videoThumb = videoThumb;
        initView();
        initListener();
        initData();
    }
    public ShowVideoDialog(ReleaseQuickActivity context) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.pop_video);
        this.context2 = context;
        initView();
        initListener();
        initData();
    }

    public void initView() {
        mVideoThumbIv = (ImageView) findViewById(R.id.video_thumb_iv);

        mLoadingProcress = (LinearLayout) findViewById(R.id.loading_procress);
        mIvProgressBar = (ImageView) findViewById(R.id.iv_progress_bar);
        mTvProgressBar = (TextView) findViewById(R.id.tv_progress_bar);

        mBtnVideoDelete = (Button) findViewById(R.id.btn_video_delete);
        mBtnVideoComplete = (Button) findViewById(R.id.btn_video_complete);
    }

    private void initListener() {
        mVideoThumbIv.setOnClickListener(this);

        mBtnVideoDelete.setOnClickListener(this);

        mBtnVideoComplete.setOnClickListener(this);
    }

    private void initData() {
        file = new File(sdVideoDir, "myVideo.mp4");
        if (file.exists()) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file.getAbsolutePath());
            bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if (null != bitmap) {
                mVideoThumbIv.setImageBitmap(bitmap);
            }
        } else {
            if(videoThumb!=null) {
                HttpLoader.getImageLoader().get(videoThumb,
                        ImageLoader.getImageListener(mVideoThumbIv, R.color.no_color, R.color.no_color));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_thumb_iv:
                Intent intent = new Intent(context2, PlayVideoActivity.class);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, "1");//播放自己的视频
                context2.startActivity(intent);
                break;
            case R.id.btn_video_delete:
                context2.deleteVideo();
                dismiss();
                break;
            case R.id.btn_video_complete:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mVideoThumbIv.setImageBitmap(null);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
