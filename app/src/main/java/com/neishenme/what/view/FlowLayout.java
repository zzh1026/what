package com.neishenme.what.view;

/*
 * File Name: MyFlowLayout.java
 * History:
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.neishenme.what.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    public static final int DEFAULT_SPACING = 20;
    /**
     * 横向间隔
     */
    private int mHorizontalSpacing = DEFAULT_SPACING;
    /**
     * 纵向间隔
     */
    private int mVerticalSpacing = DEFAULT_SPACING;
    /**
     * 是否需要布局，只用于第一次
     */
    boolean mNeedLayout = true;
    /**
     * 当前行已用的宽度，由子View宽度加上横向间隔
     */
    private int mUsedWidth = 0;
    /**
     * 代表每一行的集合
     */
    private final List<Line> mLines = new ArrayList<Line>();
    private Line mLine = null;
    private int mMaxLinesCount = Integer.MAX_VALUE;

    public FlowLayout(Context context) {
        super(context);
    }

    public void setHorizontalSpacing(int spacing) {
        if (mHorizontalSpacing != spacing) {
            mHorizontalSpacing = spacing;
            requestLayoutInner();
        }
    }

    public void setVerticalSpacing(int spacing) {
        if (mVerticalSpacing != spacing) {
            mVerticalSpacing = spacing;
            requestLayoutInner();
        }
    }

    public void setMaxLines(int count) {
        if (mMaxLinesCount != count) {
            mMaxLinesCount = count;
            requestLayoutInner();
        }
    }

    private void requestLayoutInner() {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = child.getMeasuredWidth();
            mUsedWidth += childWidth;
            if (mUsedWidth <= sizeWidth) {
                mLine.addView(child);// 添加child
                mUsedWidth += mHorizontalSpacing;// 加上间隔
                if (mUsedWidth >= sizeWidth) {// 加上间隔后如果大于等于总宽度，需要换行
                    if (!newLine()) {
                        break;
                    }
                }
            } else {
                if (mLine.getViewCount() == 0) {
                    mLine.addView(child);// 添加child
                    if (!newLine()) {
                        break;
                    }
                } else {
                    if (!newLine()) {
                        break;
                    }
                    mLine.addView(child);
                    mUsedWidth += childWidth + mHorizontalSpacing;
                }
            }
        }

        if (mLine != null && mLine.getViewCount() > 0 && !mLines.contains(mLine)) {
            mLines.add(mLine);
        }

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int linesCount = mLines.size();
        for (int i = 0; i < linesCount; i++) {// 加上所有行的高度
            totalHeight += mLines.get(i).mHeight;
        }
        totalHeight += mVerticalSpacing * (linesCount - 1);
        totalHeight += getPaddingTop() + getPaddingBottom();// 加上padding
        setMeasuredDimension(totalWidth, resolveSize(totalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!mNeedLayout || changed) {//没有发生改变就不重新布局
            mNeedLayout = false;
            int left = getPaddingLeft();
            int top = getPaddingTop();
            final int linesCount = mLines.size();
            for (int i = 0; i < linesCount; i++) {
                final Line oneLine = mLines.get(i);
                oneLine.layoutView(left, top);//布局每一行
                top += oneLine.mHeight + mVerticalSpacing;//为下一行的top赋值
            }
        }
    }

    /**
     * 还原所有数据
     */
    private void restoreLine() {
        mLines.clear();
        mLine = new Line();
        mUsedWidth = 0;
    }

    /**
     * 新增加一行
     */
    private boolean newLine() {
        mLines.add(mLine);
        if (mLines.size() < mMaxLinesCount) {
            mLine = new Line();
            mUsedWidth = 0;
            return true;
        }
        return false;
    }

    class Line {
        int mWidth = 0;
        int mHeight = 0;// 该行中所有的子View中高度的那个子View的高度
        List<View> views = new ArrayList<View>();

        public void addView(View view) {// 往该行中添加一个
            views.add(view);
            mWidth += view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            mHeight = mHeight < childHeight ? childHeight : mHeight;//高度等于一行中最高的View
        }

        public int getViewCount() {
            return views.size();
        }

        public void layoutView(int l, int t) {// 布局
            int left = l;
            int top = t;
            int count = getViewCount();
            //总宽度
            int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int surplusWidth = layoutWidth - mWidth - mHorizontalSpacing * (count - 1);
            if (surplusWidth >= 0) {// 剩余空间
                int splitSpacing = (int) (surplusWidth / count + 0.5);
                for (int i = 0; i < count; i++) {
                    final View view = views.get(i);
                    int childWidth = view.getMeasuredWidth();
                    int childHeight = view.getMeasuredHeight();
                    int topOffset = (int) ((mHeight - childHeight) / 2.0 + 0.5);
                    if (topOffset < 0) {
                        topOffset = 0;
                    }
                    //把剩余空间平均到每个View上
//					childWidth = childWidth + splitSpacing;
//					view.getLayoutParams().width = childWidth;
//					if (splitSpacing > 0) {
//						int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
//						int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
//						view.measure(widthMeasureSpec, heightMeasureSpec);
//					}
                    view.layout(left, top + topOffset, left + childWidth, top + topOffset + childHeight);
                    left += childWidth + mHorizontalSpacing;
                }
            } else {
                if (count == 1) {
                    View view = views.get(0);
                    view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
                } else {

                }
            }
        }
    }
}
