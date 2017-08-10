package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ChatInfoTypeBean extends RBResponse {

    /**
     * phone : 06284
     * trueName : Jame
     */

    private VisitorEntity visitor;

    public void setVisitor(VisitorEntity visitor) {
        this.visitor = visitor;
    }

    public VisitorEntity getVisitor() {
        return visitor;
    }

    public static class VisitorEntity {
        private String phone;
        private String trueName;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getPhone() {
            return phone;
        }

        public String getTrueName() {
            return trueName;
        }
    }
}
