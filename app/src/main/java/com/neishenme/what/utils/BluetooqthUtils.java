package com.neishenme.what.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/3.
 */

public class BluetooqthUtils {
    public BroadcastReceiver receiver;
    private BluetoothAdapter mBluetoothAdapter;
    private final UUID MY_UUID = UUID.fromString("abcd1234-ab12-ab12-ab12-abcdef123456");//定义一个UUID
    private BluetoothDevice device; //创建客户端Socket
    private BluetoothSocket clienSocket;//开始连接蓝牙，如果没有配对，则弹出对话框进行配对
    private OutputStream os;//获得输入流（客户端指向服务端的文本）
    private IntentFilter filter;
    private Context mContext;
    private String str;
    private int areaId;  //ID
    private double latitude;//经纬度
    private double longitude;

    //    public Handler SocketHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    setDate(); // 发送数据
////                    msg = SocketHandler.obtainMessage(1);
////                    SocketHandler.sendMessageDelayed(msg,3000);
//                    break;
//            }
//        }
//    };
    public BluetooqthUtils(Context context) {
        mContext = context;
        start();
    }

    public BluetooqthUtils(Context applicationContext, int showCityAreaId, double latitude, double longitude) {
        mContext = applicationContext;
        areaId = showCityAreaId;
        this.latitude = latitude;
        this.longitude = longitude;
        start();
    }

    public void start() {
        //设置权限
        shezhiquanxian();

        //创建BluetoothAdapter对象
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.enable();
        //我们先获取并显示一下已经配对的蓝牙设备列表
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
//                tv_devices.append(device.getName() + ":" + device.getAddress());
                String str2 = device.getName() + ":" + device.getAddress();
                str = str + str2;
            }
        }
        register();
        //定义广播接收器
        //设置广播过了器
        //没搜索到一个设备就发送一个广播
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(receiver, filter);
        //当全部搜索完发送该广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        filter.setPriority(Integer.MAX_VALUE);//设置优先级
        mContext.registerReceiver(receiver, filter);

        setDate();
        setDate();

        //发Handler实现无限循环， 去无限发送消息和搜索设备。
//        Message message = SocketHandler.obtainMessage(1);
//        SocketHandler.sendMessageDelayed(message, 3000);
    }

    private final int REQUEST_BLUETOOTH_PERMISSION = 10;

    private void shezhiquanxian() {
//        //判断系统版本
//        if (Build.VERSION.SDK_INT >= 23) {
//            //检测当前app是否拥有否个权限
//            int checkSelfPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION);
//            //判断这个权限是否已经授权过
//            if (checkSelfPermission != PackageManager.PERMISSION_DENIED) {
//                //判断是否需要 向用户解释 ，为什么申请此权限
//                if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity) mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
//
//                    ActivityCompat.requestPermissions((MainActivity) mContext, new String[]
//                            {android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_BLUETOOTH_PERMISSION);
//                    return;
//                }
//
//            }
//        }
    }

    private void register() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                        tv_devices.append(device.getName() + ":" + device.getAddress());
                        String str2 = device.getName() + ":" + device.getAddress();
                        str = str + str2;
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //搜索完成
                }
            }
        };
    }


    //    //点击事件发信息
//    public void syncDataItem(View v) {
//        //如果当前在搜索就取消
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
//        //开启搜索
//        mBluetoothAdapter.startDiscovery();
////        zhuceguangbo();
////        setDate();
//    }
    private String date;

    public void setDate() {
        boolean login = NSMTypeUtils.isLogin();
        if (login) {
            String myToken = NSMTypeUtils.getMyToken();
            String userId = NSMTypeUtils.getMyUserId();
            date = userId + " " + areaId + " " + latitude + " " + longitude + " " + myToken;
        } else {
            date = "no";
        }
        if (str == null) {
            return;
        }
        //获取蓝牙设备
//        String s = tv_devices.getText().toString();
        String address = str.substring(str.indexOf(":") + 1).trim();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if (device == null) {
            //获取远程设备
            try {
                device = mBluetoothAdapter.getRemoteDevice(address);
            } catch (Exception e) {
                return;
            }
        }
        if (clienSocket == null) {
            try {
                //创建客户端Socket
                clienSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                //开始连接蓝牙，如果没有配对，则弹出对话框进行配对
                clienSocket.connect();
                //获得输入流（客户端指向服务端的文本）
                os = clienSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (os != null) {
            //往服务端写信息
            try {
                os.write(date.getBytes("utf-8"));
                Log.e("aaaaa", "蓝牙信息来了 " + date);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //开启搜索
        mBluetoothAdapter.startDiscovery();
    }

}