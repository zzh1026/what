package com.neishenme.what.view.refreshview;

import android.content.Context;
import android.util.AttributeSet;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.neishenme.what.view.refreshview.loadingview.CustomRefreshFooterView;
import com.neishenme.what.view.refreshview.loadingview.CustomRefreshHeaderView;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/13.
 */

public class CustomRefreshLayout extends TwinklingRefreshLayout {
    public CustomRefreshLayout(Context context) {
        super(context);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEnableRefresh(true);    //默认允许下拉刷新
        setEnableLoadmore(false);   //默认允许加载更多
        setOverScrollRefreshShow(false);    //默认越界不显示控件
//        setOverScrollTopShow(false);    //默认越界的时候不显示刷新控件
//        setOverScrollBottomShow(false); //默认越界不显示加载更多

        setAutoLoadMore(false); //默认不自动加载更多

        setHeaderView(new CustomRefreshHeaderView(context));

        setBottomView(new CustomRefreshFooterView(context));
    }


}
