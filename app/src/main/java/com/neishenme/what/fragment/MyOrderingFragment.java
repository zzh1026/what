package com.neishenme.what.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.ItemHistoryOrderAdapter;
import com.neishenme.what.adapter.OrderIngAdapter;
import com.neishenme.what.bean.HistoryOrderResponse;
import com.neishenme.what.bean.MyOrderResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyOrderingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderingFragment extends Fragment implements HttpLoader.ResponseListener {
    private static final String ARG_PARAM1 = "param1";

    private PullToRefreshListView lv;
    private OrderIngAdapter orderIngAdapter;
    private ItemHistoryOrderAdapter itemHistoryOrderAdapter;

    private MyOrderResponse orderResponse;
    private HistoryOrderResponse historyOrderResponse;
    private int page = 1;

    private String mParam1;
    private TextView tvEmpty;
    private boolean isOrderHasMore;


    public MyOrderingFragment() {
    }

    public static MyOrderingFragment newInstance(String param1) {
        MyOrderingFragment fragment = new MyOrderingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ordering, container, false);
        initView(view);
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        lv = (PullToRefreshListView) view.findViewById(R.id.lv);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
    }

    private void initListener() {
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                itemHistoryOrderAdapter = null;
                orderIngAdapter = null;
                page = 1;
                if ("1".equals(mParam1)) {
                    requestOrderIng();
                } else {
                    requestOrderHistory();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isOrderHasMore) {
                    page++;
                    if ("1".equals(mParam1)) {
                        requestOrderIng();
                    } else {
                        requestOrderHistory();
                    }
                } else {
                    lv.onRefreshComplete();
                }
            }
        });
    }

    private void initData() {
        lv.setMode(PullToRefreshBase.Mode.BOTH);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        page = 1;
        if ("1".equals(mParam1)) {
            orderIngAdapter = null;
            requestOrderIng();
        } else {
            itemHistoryOrderAdapter = null;
            requestOrderHistory();
        }
    }

    private void requestOrderIng() {
        HashMap<String, String> params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("areaId", CityLocationConfig.cityLocationId + "");
        params.put("pageSize", "10");
        HttpLoader.get(ConstantsWhatNSM.URL_MY_ORDER, params,
                MyOrderResponse.class, ConstantsWhatNSM.REQUEST_CODE_MY_ORDER, this).setTag(this);
    }

    private void requestOrderHistory() {
        HashMap<String, String> params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("areaId", CityLocationConfig.cityLocationId + "");
        params.put("pageSize", "10");
        HttpLoader.get(ConstantsWhatNSM.URL_MY_HISTORY_ORDER, params,
                HistoryOrderResponse.class, ConstantsWhatNSM.REQUEST_CODE_HISTORY_ORDER, this).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        lv.onRefreshComplete();
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_MY_ORDER
                && response instanceof MyOrderResponse) {
            MyOrderResponse myOrderResponse_down = (MyOrderResponse) response;
            if (myOrderResponse_down.getCode() == 1) {
                isOrderHasMore = myOrderResponse_down.getData().isHasMore();
                if (!isOrderHasMore) {
                    lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    lv.setMode(PullToRefreshBase.Mode.BOTH);
                }
                if (null == orderIngAdapter) {
                    orderResponse = myOrderResponse_down;
                    orderIngAdapter = new OrderIngAdapter(getActivity(), orderResponse);
                    lv.setAdapter(orderIngAdapter);
                } else {
                    orderResponse.getData().getOrdering().addAll(myOrderResponse_down.getData().getOrdering());
                    orderIngAdapter.notifyDataSetChanged();
                }
                if (orderResponse.getData().getOrdering().size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HISTORY_ORDER && response instanceof HistoryOrderResponse) {
            HistoryOrderResponse historyOrderResponse_down = (HistoryOrderResponse) response;
            if (historyOrderResponse_down.getCode() == 1) {
                isOrderHasMore = historyOrderResponse_down.getData().isHasMore();
                if (!isOrderHasMore) {
                    lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    lv.setMode(PullToRefreshBase.Mode.BOTH);
                }
                if (null == itemHistoryOrderAdapter) {
                    historyOrderResponse = historyOrderResponse_down;
                    itemHistoryOrderAdapter = new ItemHistoryOrderAdapter(getActivity(), historyOrderResponse);
                    lv.setAdapter(itemHistoryOrderAdapter);

                } else {
                    historyOrderResponse.getData().getHistroyorder().addAll(historyOrderResponse_down.getData().getHistroyorder());
                    itemHistoryOrderAdapter.notifyDataSetChanged();

                }
                if (historyOrderResponse.getData().getHistroyorder().size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
