package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/4/27 20:48
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class FillOrderTitleResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"titles":["一个人好无聊，我请你吃个饭吧","一起享受这顿美食吧","求个妹纸，我今天有时间","妹子有空没"]}
     */

    private int code;
    private String message;
    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private List<String> titles;

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public List<String> getTitles() {
            return titles;
        }
    }
}
