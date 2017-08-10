package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/2 13:50
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class RecordsListResponse extends RBResponse {

    /**
     * code : 1
     * data : {"accounts":[{"createTime":1464845556354,"money":10,"origin":210,"purse":50,"remark":"37237:哈哈哈哈 活动退款","type":1,"userId":666},{"createTime":1464845556354,"money":5,"origin":210,"purse":40,"remark":"37237:哈哈哈哈 活动退款","type":1,"userId":666},{"createTime":1464845556354,"money":5,"origin":110,"purse":35,"remark":"37237:哈哈哈哈 参与邀请","type":0,"userId":666},{"createTime":1464845556354,"money":10,"origin":120,"purse":40,"remark":"37237:哈哈哈哈 发布邀请","type":0,"userId":666},{"createTime":1464845556354,"money":50,"origin":130,"purse":50,"remark":"提现金额 50","type":0,"userId":666},{"createTime":1464845556354,"money":30,"origin":140,"purse":100,"remark":"充值会员 30","type":0,"userId":666},{"createTime":1464845556354,"money":130,"origin":220,"purse":130,"remark":"支付失败退款","type":1,"userId":666}]}
     * message : success
     */

    private int code;
    private DataEntity data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        /**
         * createTime : 1464845556354
         * money : 10.0
         * origin : 210
         * purse : 50.0
         * remark : 37237:哈哈哈哈 活动退款
         * type : 1
         * userId : 666
         */

        private List<AccountsEntity> accounts;

        public void setAccounts(List<AccountsEntity> accounts) {
            this.accounts = accounts;
        }

        public List<AccountsEntity> getAccounts() {
            return accounts;
        }

        public static class AccountsEntity {
            private long createTime;
            private double money;
            private int origin;
            private double purse;
            private String remark;
            private int type;
            private int userId;

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public void setOrigin(int origin) {
                this.origin = origin;
            }

            public void setPurse(double purse) {
                this.purse = purse;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setType(int type) {
                this.type = type;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public double getMoney() {
                return money;
            }

            public int getOrigin() {
                return origin;
            }

            public double getPurse() {
                return purse;
            }

            public String getRemark() {
                return remark;
            }

            public int getType() {
                return type;
            }

            public int getUserId() {
                return userId;
            }
        }
    }
}
