package com.neishenme.what.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.neishenme.what.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/**
 * 这个界面我也不知道是什么玩意, 完全没印象 ,, 不是我写的,应该没有用
 */
public class TestActivity extends AppCompatActivity {
    TextView tv_devices;
    private BroadcastReceiver receiver;
    private BluetoothAdapter mBluetoothAdapter;
    private final UUID MY_UUID = UUID.fromString("abcd1234-ab12-ab12-ab12-abcdef123456");//随便定义一个UUID
    private BluetoothDevice device; //创建客户端Socket
    private BluetoothSocket clienSocket;//开始连接蓝牙，如果没有配对，则弹出对话框进行配对
    private OutputStream os;//获得输入流（客户端指向服务端的文本）
    private IntentFilter filter;
    String str = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv_devices = (TextView) findViewById(R.id.tv_devices);

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
                tv_devices.setText(str);
            }
        }

        //定义广播接收器
        //设置广播过了器
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//没搜索到一个设备就发送一个广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完发送该广播
        filter.setPriority(Integer.MAX_VALUE);//设置优先级
        zhuceguangbo();
        this.registerReceiver(receiver, filter);

        aaa();
    }

    private  final int REQUEST_BLUETOOTH_PERMISSION = 10;
    private void shezhiquanxian() {
        //判断系统版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有否个权限
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            //判断这个权限是否已经授权过
            if (checkSelfPermission != PackageManager.PERMISSION_DENIED) {
                //判断是否需要 向用户解释 ，为什么申请此权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_BLUETOOTH_PERMISSION);
                    return;
                }

            }


        }
    }

    private void zhuceguangbo() {
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
                        tv_devices.setText(str);
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //搜索完成
                }
            }
        };
    }

    //点击事件发信息
    public void syncDataItem(View v) {
        //如果当前在搜索就取消
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //开启搜索
        mBluetoothAdapter.startDiscovery();
//        zhuceguangbo();
//        aaa();
    }
    private void aaa(){
        //获取蓝牙设备
//        String s = tv_devices.getText().toString();
        String address = str.substring(str.indexOf(":") + 1).trim();
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if(device==null) {
            //获取远程设备
            device =  mBluetoothAdapter.getRemoteDevice(address);
        }
        if(clienSocket == null) {
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

        if(os!=null) {
            //往服务端写信息
            try {
                os.write("蓝牙信息来了fff132".getBytes("utf-8"));   //传过去的数据
                Log.e("adsasdas","chaungfuoq22222222222u");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解绑
//        unregisterReceiver(receiver,filter);

    }
}