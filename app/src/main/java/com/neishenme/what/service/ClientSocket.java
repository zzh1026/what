package com.neishenme.what.service;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.neishenme.what.bean.SocketLoginBean;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/14 15:01
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class ClientSocket extends Thread {

    public static final long HEART_TIME = 1000 * 30;
    public static final long SEND_TIME = 1000;
    private static final String HEART_MSG = "heart";

    private static Gson gson = new Gson();

    /**
     * 接收线程
     */
    private Thread mReciverThread = null;

    /**
     * 心跳时间
     */
    private long mHeartTime = 0;

    /**
     * client socket
     */
    private Socket mSocket = null;

    /**
     * 是否应该连接,当主动关闭的时候应该关闭连接
     */
    private boolean isSocketClose = false;

    /**
     * 消息队列
     */
    private static volatile List<String> mMsgQueue = null;

    /**
     * 接收到服务端返回信息回调接口
     */
    private OnSocketRecieveCallBack mOnSocketRecieveCallBack;

    /**
     * 监听socket创建异常
     */
    private OnSocketStateListener mOnSocketStateListener;

    /**
     * 重试机制的第几次
     */
    private int mTryCurrentTime = 1;

    private BufferedReader br;
    private PrintWriter ps;

    /**
     * 构造函数
     */
    public ClientSocket() {
        mMsgQueue = new ArrayList<>();

        mReciverThread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    reciverMsgFromServer();
                }
            }
        };
    }

    public void startReciveMsg() {
        mReciverThread.start();
    }

    private void reciverMsgFromServer() {
        // 接收数据
        if (mSocket != null && mSocket.isConnected() && !mSocket.isClosed() && !mSocket.isInputShutdown() && br != null) {
            try {
                String recieveMsg = readFromInputStream(br);
                if (mOnSocketRecieveCallBack != null) {
                    mOnSocketRecieveCallBack.OnRecieveFromServerMsg(recieveMsg);
                }
            } catch (Exception e) {
                SystemClock.sleep(1000);
                //reciverMsgFromServer();
            }
        } else {
            SystemClock.sleep(1500);
        }
        Log.i("hehe", "运行了接收数据");
    }

    public String readFromInputStream(BufferedReader in) {
        try {
            char[] sn = new char[1024];
            int len;
            len = br.read(sn);
            return new String(sn, 0, len);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        mHeartTime = System.currentTimeMillis();
        mTryCurrentTime = 1;
        // 轮询发送消息列表中的数据
        while (mTryCurrentTime <= 1000) {
            isSocketClose = isSocketClose();
            Log.i("hehe", "socket 的连接状态为: " + isSocketClose);
            if (isSocketClose) {
                while (isSocketClose && mTryCurrentTime < 1000) {
                    try {
                        Log.i("hehe", "socket 断开连接,需要重新连接");
                        // 重建连接
                        if (mSocket != null && !mSocket.isClosed()) {
                            mSocket.close();
                        }
                        if (NSMTypeUtils.isLogin()) {
                            mSocket = new Socket(InetAddress.getByName(
                                    ConstantsWhatNSM.SOCKET_URL_SERVER_URL), ConstantsWhatNSM.SOCKET_URL_SERVER_PORT);
                            mSocket.setKeepAlive(true);// 开启保持活动状态的套接字
                            mSocket.setTcpNoDelay(true);
                            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
//                            ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
                            ps = new PrintWriter(mSocket.getOutputStream(), true);
                            //重新连接
                            mHeartTime = System.currentTimeMillis();
                            isSocketClose = false;
                            Log.i("hehe", "socket 重新连接成功,连接数据");
                            mTryCurrentTime = 1;
                            sendLoginMessage();
                        }
                    } catch (Exception e) {
                        mTryCurrentTime++;
                        isSocketClose = true;
                        Log.i("hehe", "socket 连接失败!!!");
                    }

                    if (NSMTypeUtils.isLogin()) {
                        try {
                            Thread.sleep(SEND_TIME * mTryCurrentTime * 2);
                            Log.i("hehe", "socket 连接失败!!! 睡眠了: " + SEND_TIME * mTryCurrentTime * 2);
                        } catch (Exception e1) {
                        }
                    } else {
                        mTryCurrentTime = 1001;
                    }
                }
            } else {
                Log.i("hehe", "socket 连接成功!!! ");
                // 判断是否要发送心跳包
                try {
                    if (mMsgQueue != null && mMsgQueue.size() > 0) {
                        Thread.sleep(SEND_TIME);
                    } else {
                        Thread.sleep(SEND_TIME * 5);
                    }

                    // 发送数据
                    if (!mSocket.isOutputShutdown() && !isMsgQueueEmpty()) {
                        String s = mMsgQueue.get(0);
                        if (!TextUtils.isEmpty(s)) {
                            ps.println(s);
                            Log.i("hehe", "本次发送的数据为 :" + s + " 其长度是: " + s.getBytes().length);
                        }
                        // 将发送过的数据移除消息列表
                        mMsgQueue.remove(0);
                        mHeartTime = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                    Log.i("hehe", "socket 捕获到异常");
                    isSocketClose = true;
                }
            }
        }
        if (mOnSocketStateListener != null) {
            mOnSocketStateListener.onSocketStateException();
            mTryCurrentTime = 1;
            Log.i("hehe", "socket 失败返回!!! ");
        }
        closeAll();
    }

    public void sendLoginMessage() {
//        CityLocationConfig.getLocationUtil(new CityLocationConfig.OnCitySuccess() {
//            @Override
//            public void onCitySuccess() {
//                SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(
//                        CityLocationConfig.cityLatitude, CityLocationConfig.cityLonggitude);
//                addSendMsgToQueue(gson.toJson(socketLoginBean));
//            }
//
//            @Override
//            public void onCityError() {
//                SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(
//                        CityLocationConfig.cityLatitude, CityLocationConfig.cityLonggitude);
//                addSendMsgToQueue(gson.toJson(socketLoginBean));
//            }
//        });
    }

    private boolean isSocketClose() {
        if (mSocket == null) {
            return true;
        }

        if (mSocket.isConnected() && !mSocket.isClosed()) {
            try {
                mSocket.sendUrgentData(0);  // 发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
                return false;
            } catch (Exception se) {
                return true;
            }
        } else {
            return true;
        }
    }

    public void closeAll() {
        if (br != null)
            try {
                br.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mReciverThread != null && !mReciverThread.isInterrupted()) {
            mReciverThread.interrupt();
            mReciverThread = null;
        }

        if (mSocket != null && !mSocket.isClosed()) {
            mSocket.close();
        }
    }

    /**
     * 发送心跳包
     */
    public void sendHeart(String msgHeart) {
        ((LinkedList<String>) mMsgQueue).addFirst(msgHeart);
    }

    /**
     * socket 是否有断开连接
     *
     * @return
     */
    public boolean isSocketConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    /**
     * 添加发送消息到队列
     *
     * @param msg
     */
    public void addSendMsgToQueue(String msg) {
        mMsgQueue.add(msg);
    }

    /**
     * 判断消息队列是否为空
     *
     * @return
     */
    public boolean isMsgQueueEmpty() {
        return mMsgQueue.isEmpty();
    }


    //get set 方法
    public Socket getSocket() {
        return mSocket;
    }

    public String getServerUrl() {
        return ConstantsWhatNSM.SOCKET_URL_SERVER_URL;
    }

    public int getServerPort() {
        return ConstantsWhatNSM.SOCKET_URL_SERVER_PORT;
    }

    public List<String> getMsgQueue() {
        return mMsgQueue;
    }

    public void setMsgQueue(List<String> mMsgQueue) {
        this.mMsgQueue = mMsgQueue;
    }

    public OnSocketRecieveCallBack getOnSocketRecieveCallBack() {
        return mOnSocketRecieveCallBack;
    }

    public void setOnSocketRecieveCallBack(OnSocketRecieveCallBack mOnSocketRecieveCallBack) {
        this.mOnSocketRecieveCallBack = mOnSocketRecieveCallBack;
    }

    public void setOnSocketStateListener(OnSocketStateListener mOnSocketStateListener) {
        this.mOnSocketStateListener = mOnSocketStateListener;
    }

    /**
     * 接收到服务端返回信息回调接口
     *
     * @author Administrator
     */
    public interface OnSocketRecieveCallBack {
        public void OnRecieveFromServerMsg(String msg);
    }

    public interface OnSocketStateListener {
        public void onSocketStateException();

        public void onSocketStateSuccess();
    }
}
