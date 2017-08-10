package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/3/28 00:28
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class HomeNewsResponse extends RBResponse {


    /**
     * code : 1
     * data : {"messages":[{"context":"青巷旧街 申请了你发起的 给你一件事 邀请 快去同意他吧","createTime":1469440740213,"data":{"inviteId":"58159","joinerId":"275883","userId":"666"},"endTime":1469449380213,"id":272443,"map":{},"mark":"joinInvite#inviteId=58159#joinerId=275883#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469440740213,"useJPsh":1},{"context":"⒈朵小奇葩° 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469440440217,"data":{"inviteId":"58158","joinerId":"275882","userId":"666"},"endTime":1469449080217,"id":272442,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275882#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469440440217,"useJPsh":1},{"context":"话说起溺 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469439600135,"data":{"inviteId":"58158","joinerId":"275881","userId":"666"},"endTime":1469448240135,"id":272441,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275881#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469439600135,"useJPsh":1},{"context":"孤毒、 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469438100188,"data":{"inviteId":"58158","joinerId":"275875","userId":"666"},"endTime":1469446740188,"id":272429,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275875#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469438100188,"useJPsh":1},{"context":"口口口 申请了你发起的 给你一件事 邀请 快去同意他吧","createTime":1469438006177,"data":{"inviteId":"58159","joinerId":"275874","userId":"666"},"endTime":1469446646177,"id":272428,"map":{},"mark":"joinInvite#inviteId=58159#joinerId=275874#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469438006177,"useJPsh":1},{"context":"一二三四五六七八 申请了你发起的 给你一件事 邀请 快去同意他吧","createTime":1469437982091,"data":{"inviteId":"58159","joinerId":"275873","userId":"666"},"endTime":1469446622091,"id":272427,"map":{},"mark":"joinInvite#inviteId=58159#joinerId=275873#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469437982091,"useJPsh":1},{"context":"敌于眉间 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469436420133,"data":{"inviteId":"58158","joinerId":"275858","userId":"666"},"endTime":1469445060133,"id":272367,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275858#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469436420133,"useJPsh":1},{"context":"碎花裙 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469436301654,"data":{"inviteId":"58158","joinerId":"275857","userId":"666"},"endTime":1469444941654,"id":272366,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275857#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469436301654,"useJPsh":1},{"context":"孤人孤巷 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469435941067,"data":{"inviteId":"58158","joinerId":"275850","userId":"666"},"endTime":1469444581067,"id":272358,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275850#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469435941067,"useJPsh":1},{"context":"夜夜做新郎 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469435713891,"data":{"inviteId":"58158","joinerId":"275847","userId":"666"},"endTime":1469444353891,"id":272355,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275847#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469435713891,"useJPsh":1},{"context":"离人毁心 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469435587473,"data":{"inviteId":"58158","joinerId":"275844","userId":"666"},"endTime":1469444227473,"id":272352,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275844#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469435587473,"useJPsh":1},{"context":"大眼怪囡囡 申请了你发起的 名字叫 邀请 快去同意他吧","createTime":1469435586165,"data":{"inviteId":"58158","joinerId":"275843","userId":"666"},"endTime":1469444226165,"id":272351,"map":{},"mark":"joinInvite#inviteId=58158#joinerId=275843#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1469435586165,"useJPsh":1},{"context":"旅进 申请了你发起的 您贵姓 邀请 快去同意他吧","createTime":1465375652112,"data":{"inviteId":"42233","joinerId":"202976","userId":"666"},"endTime":1465384292112,"id":199274,"map":{},"mark":"joinInvite#inviteId=42233#joinerId=202976#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465375652112,"useJPsh":1},{"context":"bl文科目送5尊在哪啊军部我来咯马基雅维利同意了你的申请 快去准备吧","createTime":1465372843112,"data":{"inviteId":"42229","userId":"670"},"endTime":1465381483112,"id":199270,"map":{},"mark":"invite#inviteId=42229#userId=670","sender":-1,"sendto":666,"title":"你成功申请了一个邀约 快去准备吧","type":"invite","updateTime":1465372843112,"useJPsh":1},{"context":"嘻嘻嘻 申请了你发起的 额红mix额(⊙o⊙)\u2026 邀请 快去同意他吧","createTime":1465289805099,"data":{"inviteId":"41977","joinerId":"201179","userId":"666"},"endTime":1465298445099,"id":197470,"map":{},"mark":"joinInvite#inviteId=41977#joinerId=201179#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465289805099,"useJPsh":1},{"context":"勿忘我 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282860197,"data":{"inviteId":"41975","joinerId":"201177","userId":"666"},"endTime":1465291500197,"id":197457,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201177#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282860197,"useJPsh":1},{"context":"西巷姑娘 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282800901,"data":{"inviteId":"41975","joinerId":"201176","userId":"666"},"endTime":1465291440901,"id":197456,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201176#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282800901,"useJPsh":1},{"context":"亡与栀枯  申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282800549,"data":{"inviteId":"41975","joinerId":"201175","userId":"666"},"endTime":1465291440549,"id":197455,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201175#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282800549,"useJPsh":1},{"context":"喜你已久 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282800205,"data":{"inviteId":"41975","joinerId":"201174","userId":"666"},"endTime":1465291440205,"id":197454,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201174#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282800205,"useJPsh":1},{"context":"孤毒、 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282620370,"data":{"inviteId":"41975","joinerId":"201173","userId":"666"},"endTime":1465291260370,"id":197453,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201173#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282620370,"useJPsh":1},{"context":"离人毁心 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282560552,"data":{"inviteId":"41975","joinerId":"201172","userId":"666"},"endTime":1465291200552,"id":197452,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201172#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282560552,"useJPsh":1},{"context":"且贪欢 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282560209,"data":{"inviteId":"41975","joinerId":"201171","userId":"666"},"endTime":1465291200209,"id":197451,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201171#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282560209,"useJPsh":1},{"context":"伪坚强 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282500203,"data":{"inviteId":"41975","joinerId":"201170","userId":"666"},"endTime":1465291140203,"id":197450,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201170#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282500203,"useJPsh":1},{"context":"嘻嘻嘻 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282456092,"data":{"inviteId":"41975","joinerId":"201169","userId":"666"},"endTime":1465291096092,"id":197449,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201169#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282456092,"useJPsh":1},{"context":"小馒头 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282440213,"data":{"inviteId":"41975","joinerId":"201168","userId":"666"},"endTime":1465291080213,"id":197448,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201168#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282440213,"useJPsh":1},{"context":"小说情节 申请了你发起的 呵呵呵 邀请 快去同意他吧","createTime":1465282380312,"data":{"inviteId":"41975","joinerId":"201167","userId":"666"},"endTime":1465291020312,"id":197447,"map":{},"mark":"joinInvite#inviteId=41975#joinerId=201167#userId=666","sender":-1,"sendto":666,"title":"你有一个新的邀约 快去同意TA吧","type":"joinInvite","updateTime":1465282380312,"useJPsh":1}]}
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
         * context : 青巷旧街 申请了你发起的 给你一件事 邀请 快去同意他吧
         * createTime : 1469440740213
         * data : {"inviteId":"58159","joinerId":"275883","userId":"666"}
         * endTime : 1469449380213
         * id : 272443
         * map : {}
         * mark : joinInvite#inviteId=58159#joinerId=275883#userId=666
         * sender : -1
         * sendto : 666
         * title : 你有一个新的邀约 快去同意TA吧
         * type : joinInvite
         * updateTime : 1469440740213
         * useJPsh : 1
         */

        private List<MessagesEntity> messages;

        public void setMessages(List<MessagesEntity> messages) {
            this.messages = messages;
        }

        public List<MessagesEntity> getMessages() {
            return messages;
        }

        public static class MessagesEntity {
            private String context;
            private long createTime;
            /**
             * inviteId : 58159
             * joinerId : 275883
             * userId : 666
             */

            private DatasEntity data;
            private long endTime;
            private int id;
            private MapEntity map;
            private String mark;
            private int sender;
            private int sendto;
            private String title;
            private String type;
            private long updateTime;
            private int useJPsh;

            public void setContext(String context) {
                this.context = context;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setData(DatasEntity data) {
                this.data = data;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setMap(MapEntity map) {
                this.map = map;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public void setSender(int sender) {
                this.sender = sender;
            }

            public void setSendto(int sendto) {
                this.sendto = sendto;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public void setUseJPsh(int useJPsh) {
                this.useJPsh = useJPsh;
            }

            public String getContext() {
                return context;
            }

            public long getCreateTime() {
                return createTime;
            }

            public DatasEntity getData() {
                return data;
            }

            public long getEndTime() {
                return endTime;
            }

            public int getId() {
                return id;
            }

            public MapEntity getMap() {
                return map;
            }

            public String getMark() {
                return mark;
            }

            public int getSender() {
                return sender;
            }

            public int getSendto() {
                return sendto;
            }

            public String getTitle() {
                return title;
            }

            public String getType() {
                return type;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public int getUseJPsh() {
                return useJPsh;
            }

            public static class DatasEntity {
                private String inviteId;
                private String joinerId;
                private String userId;
                private String link;

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }


                public void setInviteId(String inviteId) {
                    this.inviteId = inviteId;
                }

                public void setJoinerId(String joinerId) {
                    this.joinerId = joinerId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getInviteId() {
                    return inviteId;
                }

                public String getJoinerId() {
                    return joinerId;
                }

                public String getUserId() {
                    return userId;
                }
            }

            public static class MapEntity {
            }
        }
    }
}
