package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/20.
 */

public class InviteReasonListResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"reasons":["放我鸽子，再也不见","你我缘分已尽","咱们下次再约吧","无fuck可说，再见吧","诅咒你再也约不到人","迟到没关系，下次再约","你先忙，以后有的是机会","你可以去死了！","你的良心不会受到谴责吗","活动的小船说翻就翻"]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> reasons;

        public List<String> getReasons() {
            return reasons;
        }

        public void setReasons(List<String> reasons) {
            this.reasons = reasons;
        }
    }
}
