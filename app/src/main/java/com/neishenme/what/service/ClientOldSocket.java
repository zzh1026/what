package com.neishenme.what.service;

import android.os.SystemClock;

import com.neishenme.what.utils.ConstantsWhatNSM;

import org.seny.android.utils.ALog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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
@Deprecated
public class ClientOldSocket extends Thread {

    public static final long HEART_TIME = 1000 * 30;
    public static final long SEND_TIME = 1000;
    private static final String HEART_MSG = "heart";

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
     * 消息队列
     */
    private volatile List<String> mMsgQueue = null;

    /**
     * 接收到服务端返回信息回调接口
     */
    private OnSocketRecieveCallBack mOnSocketRecieveCallBack;

    private BufferedReader br;
    private PrintWriter ps;

    /**
     * 构造函数
     */
    public ClientOldSocket() {
        mMsgQueue = new LinkedList<String>();

        mReciverThread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    reciverMsgFromServer();
                }
            }
        };
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
        }
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
        try {
            mSocket = new Socket(InetAddress.getByName(
                    ConstantsWhatNSM.SOCKET_URL_SERVER_URL), ConstantsWhatNSM.SOCKET_URL_SERVER_PORT);
            mSocket.setKeepAlive(true);// 开启保持活动状态的套接字
            mSocket.setTcpNoDelay(true);
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
            mHeartTime = System.currentTimeMillis();
            // 轮询发送消息列表中的数据
            while (true) {
                // 判断是否要发送心跳包
                if (Math.abs(mHeartTime - System.currentTimeMillis()) > HEART_TIME) {
                    //sendHeart(HEART_MSG);
                    mSocket.sendUrgentData(0);
                }

                Thread.sleep(SEND_TIME);
                // 判断client socket 是否连接上Server
                if (mSocket.isConnected() && !mSocket.isClosed()) {
                    // 发送数据
                    if (!mSocket.isOutputShutdown() && !isMsgQueueEmpty()) {
                        String s = mMsgQueue.get(0);
                        ALog.i("本次发送的数据为 :" + s);
                        ps.println(s);
                        ALog.i("发送成功");
                        // 将发送过的数据移除消息列表
                        mMsgQueue.remove(0);
                        mHeartTime = System.currentTimeMillis();
                    }
                } else {
                    // 重建连接
                    if (!mSocket.isClosed()) {
                        mSocket.close();
                    }
                    mSocket = new Socket(InetAddress.getByName(
                            ConstantsWhatNSM.SOCKET_URL_SERVER_URL), ConstantsWhatNSM.SOCKET_URL_SERVER_PORT);
                    mSocket.setKeepAlive(true);// 开启保持活动状态的套接字
                    mSocket.setTcpNoDelay(true);
                    br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
                    mHeartTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            run();
        } finally {
            closeAll();
        }
    }

    public void closeAll() {
        if (br != null)
            try {
                br.close();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                //e.printStackTrace();
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

    /**
     * 接收到服务端返回信息回调接口
     *
     * @author Administrator
     */
    public interface OnSocketRecieveCallBack {
        public void OnRecieveFromServerMsg(String msg);
    }
}
