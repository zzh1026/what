package com.neishenme.what.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.net.HttpLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarouselViewPager extends FrameLayout {
    float xDistance, yDistance, xLast, yLast;

    private boolean canPlayNext = true;
    private static final boolean isAutoPlay = true;    // 自动轮播启用开关
    private ViewPager viewPager;
    private int currentItem = 0;    // 当前轮播页
    private ScheduledExecutorService scheduledExecutorService;    // 定时任务
    private Context context;
    private List<String> url_list;
    private List<ImageView> imageViewsList;    // 放轮播图片的ImageView 的list
    private List<View> dotViewsList;    // 放圆点的View的list

    private boolean mTimer;
    private HomeInviteFragment mRestaurantFragment;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public CarouselViewPager(Context context) {
        this(context, null);
        this.context = context;
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CarouselViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void setBannerData(HomeInviteFragment mRestaurantFragment, List<String> urlList) {
        if (url_list == null && urlList != null && urlList.size() != 0) {
            url_list = urlList;
            mTimer = true;
            getHttpDataWithImageUrl();
        }
        this.mRestaurantFragment = mRestaurantFragment;
    }

    //当不需要定时轮播时调用
    public void setViewPagerData(List<String> bannerData, boolean timer) {
        if (url_list == null && bannerData != null && bannerData.size() != 0) {
            url_list = bannerData;
            mTimer = timer;
            initUI(context);
        }
    }


    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                if (canPlayNext) {
                    currentItem = (currentItem + 1);
                    handler.obtainMessage().sendToTarget();
                }
            }
        }
    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        //当需要定时轮播
        if (mTimer && url_list.size() != 1) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            SlideShowTask slideShowTask = new SlideShowTask();
            scheduledExecutorService.scheduleAtFixedRate(slideShowTask, 5, 5, TimeUnit.SECONDS);
        }

    }

    /**
     * 停止轮播图切换
     */
    public void stopPlay() {
        if (mTimer && null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }


    }


    /**
     * 初始化Views等UI
     */
    private void initUI(final Context context) {
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
        if (url_list == null || url_list.size() == 0)
            return;
        LayoutInflater.from(context).inflate(R.layout.view_carousel_view_pager, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();


        for (int i = 0; i < url_list.size(); i++) {
            final int currentPage = i;
            ImageView view = new ImageView(context);
            HttpLoader.getImageLoader().get(url_list.get(i), ImageLoader.getImageListener(
                    view, R.drawable.picture_moren, R.drawable.picture_moren));

            if (mTimer) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRestaurantFragment.dispathPageInfo(currentPage);
                    }
                });
            }

            view.setScaleType(ScaleType.CENTER_CROP);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 12;
//            params.rightMargin = 12;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
//        if (url_list.size() == 1) {
        currentItem = 100 * imageViewsList.size();
//        } else {
//            currentItem = 0;
//        }
        viewPager.setCurrentItem(currentItem);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = (position + 3) % imageViewsList.size();
            if (position < 0) {
                position = imageViewsList.size() + position;
            }
            ImageView view;
            if (imageViewsList.size() == 1) {
                view = imageViewsList.get(0);
            } else {
                view = imageViewsList.get(position);
            }
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            ((ViewPager) container).addView(view);
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            stopPlay();
                            break;
                        case MotionEvent.ACTION_UP:
                            startPlay();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            startPlay();
                            break;
                    }
                    return false;
                }
            });
            return view;
        }


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    /**
     * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    canPlayNext = false;

                    break;
                case 2:// 界面切换中
                    canPlayNext = false;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    canPlayNext = true;
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                   /* if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1
                            && !canPlayNext) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !canPlayNext) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }*/
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos % imageViewsList.size()) {
                    ((View) dotViewsList.get(pos % imageViewsList.size())).setBackgroundResource(R.drawable.home_invite_banner_selected);//dot_focus
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.home_invite_banner_normal);//dot_blur
                }
            }
        }
    }


    /**
     * 异步任务,获取数据
     */
    public void getHttpDataWithImageUrl() {
        initUI(context);
        if (isAutoPlay) {
            startPlay();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {//当横向滑动我才处理
                    //父类拦截了我就先请求父类不拦截,然后自己拦截子类的事件,由我自己处理
                    //父类不拦截我也不拦截
                    boolean ret = super.dispatchTouchEvent(ev);
                    Log.e("MyViewPager", ret + "");
                    if (ret) {
                        requestDisallowInterceptTouchEvent(true);
                    }
                    return ret;
                }

        }
        return super.dispatchTouchEvent(ev);
    }


}
