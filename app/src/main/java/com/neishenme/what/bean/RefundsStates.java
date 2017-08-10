package com.neishenme.what.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/24.
 */

public class RefundsStates implements Serializable {
    private String applyTime;  //提交时间
    private String applyPassTime;  //申请通过时间
    private String refundInTime;  //进行中时间
    private String finishTime;  //完成时间

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyPassTime() {
        return applyPassTime;
    }

    public void setApplyPassTime(String applyPassTime) {
        this.applyPassTime = applyPassTime;
    }

    public String getRefundInTime() {
        return refundInTime;
    }

    public void setRefundInTime(String refundInTime) {
        this.refundInTime = refundInTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
