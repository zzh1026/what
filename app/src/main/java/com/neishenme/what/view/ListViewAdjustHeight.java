package com.neishenme.what.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自适应高度listview
 */
public class ListViewAdjustHeight extends ListView {

  public ListViewAdjustHeight(Context context) {
    super(context);
  }

  public ListViewAdjustHeight(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ListViewAdjustHeight(Context context, AttributeSet attrs,
                              int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
