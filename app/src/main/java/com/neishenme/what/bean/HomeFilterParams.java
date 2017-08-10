package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HomeFilterParams {
    private String gender;
    private String orderby;
    private String showVip;

    public String getShowVip() {
        return showVip;
    }

    public void setShowVip(String showVip) {
        this.showVip = showVip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "HomeFilterParams{" +
                "gender='" + gender + '\'' +
                ", showVip='" + showVip + '\'' +
                ", orderby='" + orderby + '\'' +
                '}';
    }


    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }
}
