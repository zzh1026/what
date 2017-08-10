package com.neishenme.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.adapter.PhotoViewAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.fragment.PhotoViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 *  照片查看界面, 进入方式是邀请详情中或者个人信息界面中点击图片进入 该界面进行查看所有的照片数据
 */
public class PhotoViewActivity extends BaseActivity {
    private ViewPager mViewPager;
    private List<String> imgUrls;
    private int mSelectPosition;
    private TextView mTvCurrentPage;
    private TextView mTvAllPage;

    private static final String IMG_URLS = "data";
    private static final String IMG_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTvCurrentPage = (TextView) findViewById(R.id.tv_current_page);
        mTvAllPage = (TextView) findViewById(R.id.tv_all_page);
    }

    @Override
    protected void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvCurrentPage.setText(position + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        imgUrls = intent.getStringArrayListExtra(IMG_URLS);
        mSelectPosition = intent.getIntExtra(IMG_POSITION, 0);
        if (imgUrls == null) {
            return;
        }

        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < imgUrls.size(); i++) {
            list.add(PhotoViewFragment.newInstence(imgUrls.get(i)));
        }


        PhotoViewAdapter photoViewAdapter = new PhotoViewAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(photoViewAdapter);
        mViewPager.setCurrentItem(mSelectPosition, false);

        mTvCurrentPage.setText(mSelectPosition + 1 + "");
        mTvAllPage.setText(imgUrls.size() + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(0);
        System.gc();
    }
}
