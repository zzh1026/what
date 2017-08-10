package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.fragment.RestaurantFragment;

/**
 * Created by Administrator on 2016/5/5.
 */
public class MenuFiltrateDialog extends BaseDialog implements View.OnClickListener {
    public static final int SORT_BY_BOY_LOVE = 1;
    public static final int SORT_BY_GIRL_LOVE = 2;
    public static final int SORT_BY_PRICE = 3;
    public static final int SORT_BY_TIME = 4;
    public static final int SORT_BY_DISTENCE = 5;

    private RestaurantFragment restaurantFragment;

    private Context context;
    private ImageView ivCancle;
    private ImageView ivChoose;
    private ImageView tvManLike, tvWomanLike, tvPrice, tvTime, tvDistance;

    public MenuFiltrateDialog(Context context, RestaurantFragment restaurantFragment) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, false);
        setContentView(R.layout.dialog_filtrate);
        this.context = context;
        this.restaurantFragment = restaurantFragment;
        bindView();
        setListener();
    }

    public void bindView() {
        ivCancle = (ImageView) findViewById(R.id.iv_dialog_cancle);
        ivChoose = (ImageView) findViewById(R.id.iv_dialog_choose);

        tvManLike = (ImageView) findViewById(R.id.tv_man_like);
        tvWomanLike = (ImageView) findViewById(R.id.tv_woman_like);
        tvPrice = (ImageView) findViewById(R.id.tv_price);
        tvTime = (ImageView) findViewById(R.id.tv_time);
        tvDistance = (ImageView) findViewById(R.id.tv_distance);

    }

    public void setListener() {
        ivCancle.setOnClickListener(this);
        ivChoose.setOnClickListener(this);

        tvManLike.setOnClickListener(this);
        tvWomanLike.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvDistance.setOnClickListener(this);
    }

    public interface OnFiltrateSelectedListener {

        void filtrateSelected(String str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_cancle:
                break;
            case R.id.iv_dialog_choose:
                cancel();
                break;

            case R.id.tv_man_like:
                restaurantFragment.requestFilterSort(SORT_BY_BOY_LOVE);
                cancel();
                break;
            case R.id.tv_woman_like:
                restaurantFragment.requestFilterSort(SORT_BY_GIRL_LOVE);
                cancel();
                break;
            case R.id.tv_price:
                restaurantFragment.requestFilterSort(SORT_BY_PRICE);
                cancel();
                break;
            case R.id.tv_time:
                restaurantFragment.requestFilterSort(SORT_BY_TIME);
                cancel();
                break;
            case R.id.tv_distance:
                restaurantFragment.requestFilterSort(SORT_BY_DISTENCE);
                cancel();
                break;
        }
    }
}
