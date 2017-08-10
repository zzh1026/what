package com.neishenme.what.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.ErrorResponse;

import org.json.JSONException;
import org.seny.android.utils.ALog;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义Request, 通过gson把json格式的response解析成bean对象，另外请求自带缓存功能
 * <p/>
 * Created by zzh.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<? extends T> clazz;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;
    private boolean isCache;

    public String cookieFromResponse;
    private String mHeader;

    /**
     * 初始化
     *
     * @param method        请求方式
     * @param url           请求地址
     * @param params        请求参数，可以为null
     * @param clazz         Clazz类型，用于GSON解析json字符串封装数据
     * @param listener      处理响应的监听器
     * @param errorListener 处理错误信息的监听器
     */
    public GsonRequest(int method, String url, Map<String, String> params, Class<? extends T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, boolean isCache) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        this.isCache = isCache;
    }

    public Gson getGson() {
        return gson;
    }

    public Class<? extends T> getClazz() {
        return clazz;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        try {
            listener.onResponse(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            ALog.d("" + json);

            mHeader = response.headers.toString();
            Pattern pattern = Pattern.compile("Set-Cookie.*?;");
            Matcher m = pattern.matcher(mHeader);
            if (m.find()) {
                cookieFromResponse = m.group();
            }
            //判断,如果length结尾就去掉,去掉cookie末尾的分号
            if (cookieFromResponse != null && cookieFromResponse.length() > 11) {
                cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
                App.cookie = cookieFromResponse;
            }

//            //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(json);
//                jsonObject.put("Cookie", cookieFromResponse);
//
//                ALog.i("Cookie : " + cookieFromResponse);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            //*********************************************************

            T result = null;
            try {
                result = gson.fromJson(json, clazz);    //解析json
                ALog.i("result 的值为" + result);
                if (isCache) {
                    //如果解析成功，并且需要缓存则将json字符串缓存到本地
                    ALog.i("Save response to local shibai !");
                    //FileCopyUtils.copy(response.data, new File(App.getApplication().getCacheDir(), "" + MD5Utils.encode(getUrl())));
                }
            } catch (JsonSyntaxException e) {
                result = (T) gson.fromJson(json, ErrorResponse.class);//解析失败，按规范错误响应解析
                ALog.e(e.getMessage());
            }
            //如果缓存可能会出现这个错误
//            catch (IOException e) {
//                ALog.e(e.getMessage());
//            }
            return Response.success(
                    result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            ALog.e(e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            ALog.e(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }
}