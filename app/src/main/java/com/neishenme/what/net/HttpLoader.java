package com.neishenme.what.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.ErrorResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.nsminterface.UpLoadResponseListener;
import com.neishenme.what.utils.UIUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MD5Utils;
import org.seny.android.utils.MyToast;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 网络请求核心类，负责get,post请求，初始化RequestQueue和ImageLoader
 * <p/>
 * Created by zzh.
 */
public class HttpLoader {

    /**
     * 过滤重复请求(正在飞的请求集合)，保存当前正在消息队列中执行的request，其中key为requestCode.
     */
    private static final HashMap<Integer, Request> mInFlightRequests = new HashMap<>();

    /**
     * 消息队列，全局使用一个
     */
    private static RequestQueue mRequestQueue = Volley.newRequestQueue(App.getApplication());

    /**
     * 图片加载工具，使用自定义的二级缓存机制
     */
    private static ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new LevelTwoLruBitmapCache(App.getApplication()));


    /**
     * 以get方式发送gsonRequest请求，默认缓存请求结果
     *
     * @return Request对象，方便链式编程，比如说接着设置Tag
     */
    public static Request get(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener) {
        return request(Request.Method.GET, url, params, clazz, requestCode, listener, true);
    }


    /**
     * 以get方式发送gsonRequest请求,自定义缓存请求结果.
     */
    public static Request get(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener, boolean isCache) {
        return request(Request.Method.GET, url, params, clazz, requestCode, listener, isCache);
    }


    /**
     * 发送post方式的GsonRequest请求，默认缓存请求结果
     */
    public static Request post(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener) {
        return request(Request.Method.POST, url, params, clazz, requestCode, listener, true);
    }

    /**
     * 发送post方式的GsonRequest请求
     *
     * @param isCache 是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public static Request post(String url, Map<String, String> params, Class<? extends RBResponse> clazz, final int requestCode, final ResponseListener listener, boolean isCache) {
        return request(Request.Method.POST, url, params, clazz, requestCode, listener, isCache);
    }

    /**
     * 发送gsonRequest请求
     *
     * @param isCache 是否需要缓冲本次响应结果
     * @return
     */
    private static Request request(int method, String url, Map<String, String> params, Class<? extends RBResponse> clazz, int requestCode, ResponseListener listener, boolean isCache) {
        Request request = mInFlightRequests.get(requestCode);
        if (request == null) {
            if (Request.Method.GET == method) {
                request = makeGsonRequest(method, url + buildGetParam(params, url), null, clazz, requestCode, listener, isCache);
            } else if (Request.Method.POST == method) {
                request = makeGsonRequest(method, url, params, clazz, requestCode, listener, isCache);
            }
            //首先尝试解析本地缓存供界面显示，然后再发起网络请求

            //TODO 读取缓存
            //tryLoadCacheResponse(request, requestCode, listener);

            ALog.d("Handle request by network!");
            return addRequest(request, requestCode);
        } else {
            ALog.i("Hi guy,the request (RequestCode is " + requestCode + ")  is already anim_fade_in-flight , So Ignore!");
            return request;
        }
    }

    public static String upLoadPic(String urlNet, Map<String, String> params, Map<String, String> fileMap, int oriention, final UpLoadResponseListener upLoadResponseListener) {
        String res = null;
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlNet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (params != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    out.write(bitmapToby(inputValue, oriention).toByteArray());
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            ALog.i("res is : " + res);
            final String finalRes = res;
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    upLoadResponseListener.onResponseSuccess(finalRes);
                }
            });
            reader.close();
        } catch (final Exception e) {
            System.out.println("发送POST请求出错。" + urlNet);
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    upLoadResponseListener.onResponseError(e);
                }
            });
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 上传mp4 的方法
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @return
     */
    public static String httpRequestMp4(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String mp4RealPath, final UpLoadResponseListener upLoadResponseListener) {
        String res = null;
        HttpURLConnection conn = null;
        String filename = null;
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        String mp4Path = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (Build.VERSION.SDK_INT > 13) {
                conn.setRequestProperty("Connection", "close");
            }
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();

                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    mp4Path = inputValue;
                    File file = new File(inputValue);
                    filename = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + "video/mp4" + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            if (fileMap != null && !TextUtils.isEmpty(mp4Path)) {
                String inputName = "thumbfile";
                filename = filename.replace(".mp4", ".jpg");
                ALog.d("filename" + filename);
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                        + "\"\r\n");
                strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");
                out.write(strBuf.toString().getBytes());
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(mp4Path);


                    Bitmap bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    out.write(Bitmap2Bytes(bitmap));
                } catch (Exception e) {
                    ALog.i("封面获取失败");
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            ALog.d("res" + res);
            final String finalRes = res;
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    upLoadResponseListener.onResponseSuccess(finalRes);
                }
            });
            reader.close();
            reader = null;
        } catch (final Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);

            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    upLoadResponseListener.onResponseError(e);
                }
            });
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把请求添加到请求队列（相当于发起了网络请求）
     *
     * @param requestCode 请求码，请求的唯一标识
     * @return request，方便链式编程
     */
    private static Request addRequest(Request<?> request, int requestCode) {
        if (mRequestQueue != null && request != null) {
            mRequestQueue.add(request);
        }
        mInFlightRequests.put(requestCode, request);
        return request;
    }

    /**
     * 取消请求
     *
     * @param tag 请求TAG
     */
    public static void cancelRequest(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);//从请求队列中取消对应的任务
        }
        //同时在mInFlightRequests删除保存所有TAG匹配的Request
        Iterator<Map.Entry<Integer, Request>> it = mInFlightRequests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Request> entry = it.next();
            Object rTag = entry.getValue().getTag();
            if (rTag != null && rTag.equals(tag)) {
                it.remove();
            }
        }
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 尝试从缓存中读取json数据
     *
     * @param request     请求
     * @param requestCode 请求码
     * @param listener    结果响应监听
     */
    private static void tryLoadCacheResponse(Request request, int requestCode, ResponseListener listener) {
        ALog.d("Try to  load cache response first !");
        if (listener != null && request != null) {
            try {
                //获取缓存文件
                File cacheFile = new File(App.getApplication().getCacheDir(), "" + MD5Utils.encode(request.getUrl()));
                StringWriter sw = new StringWriter();
                //读取缓存文件
                FileCopyUtils.copy(new FileReader(cacheFile), sw);
                if (request instanceof GsonRequest) {
                    //如果是GsonRequest，那么解析出本地缓存的json数据为GsonRequest
                    GsonRequest gr = (GsonRequest) request;

                    RBResponse response = (RBResponse) gr.getGson().fromJson(sw.toString(), gr.getClazz());
                    //传给onResponse，让前面的人用缓存数据
                    listener.onGetResponseSuccess(requestCode, response);
                    ALog.d("Load cache response success !");
                }
            } catch (Exception e) {
                ALog.w("No cache response ! " + e.getMessage());
            }
        }
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @param oriention
     * @return
     */
    private static ByteArrayOutputStream bitmapToby(String filePath, int oriention) {

        Bitmap bm = getSmallBitmap(filePath);
        Matrix m = new Matrix();
        if (oriention != 0) {
            m.setRotate(oriention);
            int width = bm.getWidth();
            int height = bm.getHeight();
            bm = Bitmap.createBitmap(bm, 0, 0, width, height, m, true);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        return baos;
    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    private static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 640, 960);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in, null, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 遍历Map集合元素，构建一个get请求参数字符串
     *
     * @param params get请求map集合
     * @param url    要拼接的url地址,用来判断怎么添加
     * @return get请求的字符串结构
     */
    public static String buildGetParam(Map<String, String> params, String url) {
        StringBuilder buffer = new StringBuilder();
        if (params != null) {
            if (url.contains("?")) {    //如果有?表示已经有参数, 在后面加&
                buffer.append("&");
            } else {
                buffer.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    buffer.append(URLEncoder.encode(key, "UTF-8"));
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(value, "UTF-8"));
                    buffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        String str = buffer.toString();
        //去掉最后的&
        if (str.length() > 1 && str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 构建一个GsonRequest
     */
    private static Request makeGsonRequest(int method, String url, Map<String, String> params, Class<? extends RBResponse> clazz, int requestCode, ResponseListener listener, boolean isCache) {
        RequestListener httpListener = new RequestListener(listener, requestCode);
        GsonRequest gsonRequest = new GsonRequest<RBResponse>(method, url, params, clazz, httpListener, httpListener, isCache) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //TODO 默认处理，如需自定义header，可重写
                return generateHeaders();
            }
        };
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy());//设置默认的重试机制，超时时间，重试次数，重试因子等
        return gsonRequest;
    }

    /**
     * 生成公共Header头信息
     *
     * @return
     */
    private static Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        //        appkey        软件身份key
        //        udid          手机客户端的唯一标识
        //        os            操作系统名称
        //        osversion     操作系统版本
        //        appversion    APP版本
        //        sourceid      推广ID
        //        ver           通讯协议版本
        //        userid        用户ID
        //        usersession   登陆后得到的用户唯一性标识
        //        unique        激活后得到的设备唯一性标识

        if (App.cookie == null) {
            App.cookie = "JSESSIONID=" + System.currentTimeMillis() + "";
        }
        headers.put("Cookie", App.cookie);
        return headers;
    }

    /**
     * 成功获取到服务器响应结果的监听，供UI层调用
     */
    public interface ResponseListener {

        /**
         * 成功获取到服务器响应数据的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onGetResponseSuccess(int requestCode, RBResponse response);

        /**
         * 网络获取失败，(做一些释放性的操作，比如关闭对话框)
         *
         * @param requestCode 请求码
         * @param error       错误信息
         */
        void onGetResponseError(int requestCode, VolleyError error);
    }

    /**
     * 对Volley请求的两种监听的封装，并执行一些默认操作，结果抛给供UI层注册的ResponseListener
     */
    private static class RequestListener implements Response.Listener<RBResponse>, Response.ErrorListener {

        private ResponseListener listener;
        private int requestCode;

        public RequestListener(ResponseListener listener, int requestCode) {
            this.listener = listener;
            this.requestCode = requestCode;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            ALog.w("----Request error from network!");
            error.printStackTrace();

            //请求错误，从正在飞的集合中删除该请求
            mInFlightRequests.remove(requestCode);
            if (listener != null) {
                listener.onGetResponseError(requestCode, error);
            }
        }

        @Override
        public void onResponse(RBResponse response) {
            ALog.w("----onResponse from network!");
            mInFlightRequests.remove(requestCode);
            if (response != null) {
                //执行通用处理，如果是服务器返回的ErrorResponse，直接提示错误信息并返回
                if ("error".equals(response.getResponse()) && response instanceof ErrorResponse) {
                    ErrorResponse errorResponse = (ErrorResponse) response;
                    MyToast.show(App.getApplication(), errorResponse.getError().getText());
                    return;
                }
                if (listener != null) {
                    listener.onGetResponseSuccess(requestCode, response);
                }
            }
        }
    }

}
