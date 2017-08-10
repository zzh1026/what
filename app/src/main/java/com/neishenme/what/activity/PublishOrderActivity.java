package com.neishenme.what.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.adapter.MenuAdapter;
import com.neishenme.what.adapter.ServicePhotoAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RestaurantDetailResponse;
import com.neishenme.what.dialog.FillOrderDialog;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.SystemBarTintManager;
import com.neishenme.what.view.ListViewAdjustHeight;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.IndicatorLayout;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/5/5.
 * 餐厅详情
 *
 * 老的发布活动界面, 已经弃用
 */

@Deprecated
public class PublishOrderActivity extends BaseActivity implements View.OnClickListener, CustomScrollView.ScrollYChangedListener, HttpLoader.ResponseListener {

    private CustomScrollView customSV;

    private View titleBar;
//    private TextView tvDate;
    private ViewPager mVpCarousel;
    private IndicatorLayout mIndicateMain;
    private ImageView ivBack;
    private TextView tvRestaName;
    private TextView tvPrice;
    private LinearLayout mRestAddressMapLl;
    private TextView tvLocation;
    private TextView tvMiddle;
    private TextView tvMiddleTime;
    private TextView tvDinner;
    private TextView tvDinnerTime;
    private TextView tvAddress;
    private ImageView ivMoreAddress;
    private View mergeView;
    private ImageView ivDinner;
    private ListViewAdjustHeight lvMenu;
    private TextView mRestaurantDetailKefuTv;
    private TextView mRestaurantDetailTitleTv;
    private MenuAdapter menuAdapter;
    private List<RestaurantDetailResponse.DataEntity.ServicemenuEntity> menuList;
    private ArrayList<String> dinnerUrls;
    private int serviceId;
    private RestaurantDetailResponse restaurantDetailResponse;
    private double latitude;
    private double longitude;
    private String restName;

    private MyFriendsResponse.DataEntity.FriendsEntity friendsEntity;

    @Override
    protected int initContentView() {
        return R.layout.activity_restaurant;
    }

    @Override
    protected void initView() {
        customSV = (CustomScrollView) findViewById(R.id.custom_sv);
        titleBar = findViewById(R.id.title_bar);
        //tvDate = (TextView) findViewById(R.id.tv_date);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        mVpCarousel = (ViewPager) findViewById(R.id.vp_carousel);
        mIndicateMain = (IndicatorLayout) findViewById(R.id.indicate_main);
        mRestAddressMapLl = (LinearLayout) findViewById(R.id.rest_address_map_ll);

        tvRestaName = (TextView) findViewById(R.id.tv_resta_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        tvMiddleTime = (TextView) findViewById(R.id.tv_middle_time);
        tvDinner = (TextView) findViewById(R.id.tv_dinner);
        tvDinnerTime = (TextView) findViewById(R.id.tv_dinner_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        ivMoreAddress = (ImageView) findViewById(R.id.iv_more_address);
        mergeView = findViewById(R.id.merge_menu);
        ivDinner = (ImageView) mergeView.findViewById(R.id.iv_dinner);
        lvMenu = (ListViewAdjustHeight) mergeView.findViewById(R.id.lv_menu);

        mRestaurantDetailKefuTv = (TextView) findViewById(R.id.restaurant_detail_kefu_tv);
        mRestaurantDetailTitleTv = (TextView) findViewById(R.id.restaurant_detail_title_tv);

    }

    @Override
    protected void initListener() {
        customSV.setScrollYChangedListener(this);
        //tvDate.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivMoreAddress.setOnClickListener(this);
        ivDinner.setOnClickListener(this);
        mRestAddressMapLl.setOnClickListener(this);
        mRestaurantDetailKefuTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        loadingShow();

        Intent intent = getIntent();
        serviceId = intent.getIntExtra("serviceId", 0);
        if (serviceId == 0) {
            showToastWarning("登录出现问题,请重新登录");
            ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
            return;
        }

        titleBar.setAlpha(0);
        //设置状态栏颜色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.Black000000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getRestInfo();
    }

    private void getRestInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("serviceId", String.valueOf(serviceId));
        HttpLoader.get(ConstantsWhatNSM.URL_RESTAURANT_DETAIL, params, RestaurantDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_DETAIL, this).setTag(this);

        if (NSMTypeUtils.isLogin()) {
            HashMap paramss = new HashMap();
            paramss.put("token", NSMTypeUtils.getMyToken());
            paramss.put("page", "0");
            paramss.put("pageSize", "1");
            HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, paramss, MyFriendsResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, this, false).setTag(this);
        }
    }

    @Override
    public void scrollYChange(int y) {

        int alpha;
        //获取滚动距离设置titlebar透明度
        if (y < 0) {
            y = 0;
        }
        alpha = (int) (y * (0.005));
        titleBar.setAlpha(alpha >= 1 ? 1 : alpha);
        mRestaurantDetailTitleTv.setVisibility(alpha >= 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_DETAIL
                && response instanceof RestaurantDetailResponse) {
            restaurantDetailResponse = (RestaurantDetailResponse) response;
            if (1 != restaurantDetailResponse.getCode()) {
                showToastInfo(restaurantDetailResponse.getMessage());
                return;
            }

            //分发界面信息数据
            disPathInfoData(restaurantDetailResponse.getData().getService(), restaurantDetailResponse.getData().getStore());

            //分发商店信息数据
            List<RestaurantDetailResponse.DataEntity.StorephotosEntity> storephotos = restaurantDetailResponse.getData().getStorephotos();
            if (null != storephotos && 0 != storephotos.size()) {
                disPathServicePhotoData(storephotos);
            } else {
                //如果没有图片怎么办;
            }

            //分发菜品照片数据
            List<RestaurantDetailResponse.DataEntity.ServicephotosEntity> servicePhotos = restaurantDetailResponse.getData().getServicephotos();
            if (null != servicePhotos && 0 != servicePhotos.size()) {
                disPathStorePhotoData(servicePhotos);
            } else {
                ivDinner.setImageResource(R.drawable.picture_moren);
            }

            //分发服务菜单数据
            List<RestaurantDetailResponse.DataEntity.ServicemenuEntity> servicemenus = restaurantDetailResponse.getData().getServicemenu();
            if (null != servicemenus && 0 != servicemenus.size()) {
                disPathMenuData(servicemenus);
            }

            loadingDismiss();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE
                && response instanceof MyFriendsResponse) {
            MyFriendsResponse myFriendsResponse = (MyFriendsResponse) response;
            int code = myFriendsResponse.getCode();
            if (1 == code) {
                friendsEntity = myFriendsResponse.getData().getFriends().get(0);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络连接失败,请检查网络连接");
        loadingDismiss();
        finish();
    }

    private void disPathInfoData(RestaurantDetailResponse.DataEntity.ServiceEntity service,
                                 RestaurantDetailResponse.DataEntity.StoreEntity store) {
        tvRestaName.setText(service.getName());
        tvPrice.setText(String.valueOf(((int) service.getPrice())));
        tvLocation.setText(store.getName());
        tvMiddleTime.setText(service.getLunch());
        tvDinnerTime.setText(service.getDinner());
        tvAddress.setText(store.getAddr());

        restName = store.getAddr();
        latitude = store.getLatitude();
        longitude = store.getLongitude();
    }

    private void disPathServicePhotoData(List<RestaurantDetailResponse.DataEntity.StorephotosEntity> servicePhotos) {
        mVpCarousel.setAdapter(new ServicePhotoAdapter(getServicePhotos(servicePhotos)));
        mIndicateMain.setViewPage(mVpCarousel);
    }

    private void disPathStorePhotoData(List<RestaurantDetailResponse.DataEntity.ServicephotosEntity> storephotos) {
        String photo = storephotos.get(0).getPhoto();
        if (!TextUtils.isEmpty(photo)) {
            HttpLoader.getImageLoader().get(photo,
                    ImageLoader.getImageListener(ivDinner, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            ivDinner.setImageResource(R.drawable.picture_moren);
        }
        dinnerUrls = new ArrayList<>();
        String photoUrl;
        for (RestaurantDetailResponse.DataEntity.ServicephotosEntity storephoto : storephotos) {
            photoUrl = storephoto.getPhoto();
            if (!TextUtils.isEmpty(photoUrl)) {
                dinnerUrls.add(photoUrl);
            }
        }
    }

    private void disPathMenuData(List<RestaurantDetailResponse.DataEntity.ServicemenuEntity> servicemenus) {
        menuList = servicemenus;
        menuAdapter = new MenuAdapter(PublishOrderActivity.this, menuList);
        lvMenu.setAdapter(menuAdapter);
    }

    private List<String> getServicePhotos(List<RestaurantDetailResponse.DataEntity.StorephotosEntity> servicephotos) {
        List<String> lists = new ArrayList<>();
        for (RestaurantDetailResponse.DataEntity.StorephotosEntity photosEntity : servicephotos) {
            lists.add(photosEntity.getPhoto());
        }
        return lists;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
//                if (!NSMTypeUtils.isLogin()) {
//                    showToast("您尚未登录,请登录后重试");
//                    ActivityUtils.startActivity(this, LoginActivity.class);
//                    return;
//                }
//                FillOrderDialog fillOrderDialog = new FillOrderDialog(
//                        PublishOrderActivity.this, serviceId,
//                        restaurantDetailResponse.getData().getService().getTimeArray(),
//                        restaurantDetailResponse.getData().getService());
//                fillOrderDialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rest_address_map_ll:
                //RestaurantLocationActivity.startRestLocationAct(this, latitude, longitude, restName);
                break;
            case R.id.iv_dinner:
                if (dinnerUrls != null && dinnerUrls.size() != 0) {
                    ActivityUtils.startActivityForListData(this, PhotoViewActivity.class, dinnerUrls, 0);
                }
                break;
            case R.id.restaurant_detail_kefu_tv:
                if (!NSMTypeUtils.isLogin()) {
                    showToastInfo(getResources().getString(R.string.unlogin));
                    return;
                }
                if (null != friendsEntity) {
                    ChatInfoBean chatInfoBean = new ChatInfoBean(
                            friendsEntity.getLogo(),
                            String.valueOf(friendsEntity.getId()),
                            friendsEntity.getName());
                    EventBus.getDefault().postSticky(chatInfoBean);
                    startActivity(new Intent(this, ChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, friendsEntity.getHxUserName()));
                } else {
                    showToastError("连接失败");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        loadingDismiss();
        finish();
        super.onBackPressed();
    }

    /**
     * 开餐厅详情界面方法,
     *
     * @param context   上下文
     * @param serviceId 服务id
     */
    public static void startRestDetailAct(Context context, int serviceId) {
        Intent intent = new Intent(context, PublishOrderActivity.class);
        intent.putExtra("serviceId", serviceId);
        context.startActivity(intent);
    }
}
