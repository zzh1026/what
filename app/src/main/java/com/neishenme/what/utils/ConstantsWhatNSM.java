package com.neishenme.what.utils;

/**
 * 作者：zhaozh create on 2016/3/8 18:30
 * <p/>
 * 版权: 内什么
 * <p/>
 * =====================================
 * <p/>
 * 这是一个用来储存服务器地址以及请求不同数据的类
 * <p/>
 * 其作用是 :
 * 可以对不同数据的类进行统一管理.
 * <p/>
 * <p/>
 * warning: apil 需要携带token参数, apin 不需要
 * <p/>
 * 定义规则, 主页面 1000 , 邀请详情 2000  ,个人信息 3000 ,发起邀请 4000 , 那个什么 5000,( 2017/1/3 该规则已弃用....)
 * 二级子页面 : 1100,1200 ,2100...
 * 部分特殊界面如登录注册等页面 : 3位  100 ,200 ,
 */
public class ConstantsWhatNSM {

    /**
     * 要访问的服务器的前缀.  测试服
     */
//    public static final String URL_SERVER = "http://192.168.3.99:8788/nsmapi/";
    //姚
//    public static final String URL_SERVER = "http://192.168.3.199/nsmapi/";
    //张博
//    public static final String URL_SERVER = "http://192.168.3.200/nsmapi/";
//    正式服
//    public static final String URL_SERVER = "http://nsmapi.neishenme.com/";
    //正式服--测试环境
//    public static final String URL_SERVER = "http://checkapi.neishenme.com/";
    //外网地址
//    public static final String URL_SERVER = "http://testapi.neishenme.com/";
    //临时的地址
    public static final String URL_SERVER = "http://api.neishenme.cn/";

    //主机地址
    //姚
//    public static final String SOCKET_URL_SERVER_URL = "192.168.3.199";
    //测试服
//    public static final String SOCKET_URL_SERVER_URL = "192.168.3.99";
    //张博
//    public static final String SOCKET_URL_SERVER_URL = "192.168.3.200";
    //    正式服
//    public static final String SOCKET_URL_SERVER_URL = "socket.neishenme.com";
    //正式服--测试环境
//    public static final String SOCKET_URL_SERVER_URL = "checkapi.neishenme.com";
    //外网
//    public static final String SOCKET_URL_SERVER_URL = "testapi.neishenme.com";
    //临时的地址
    public static final String SOCKET_URL_SERVER_URL = "api.neishenme.cn";
    //端口
    public static final int SOCKET_URL_SERVER_PORT = 1984;

    /**
     * 版本提示更新的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_UPDATE_VERSION = URL_SERVER + "apin/version/getVersion";
    //请求码
    public static final int REQUEST_CODE_UPDATE_VERSION = 1;

    /**
     * 渠道打包的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_DOWN_QUDAO = URL_SERVER + "apin/appDown/notes";
    //请求码
    public static final int REQUEST_CODE_DOWN_QUDAO = 2;

    /**
     * 广告界面的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_AD = URL_SERVER + "apin/version/getAds";
    //请求码
    public static final int REQUEST_CODE_AD = 3;

    /**
     * 首页界面的请求地址以及请求参数
     */
    //请求地址
//    public static final String URL_HOME = URL_SERVER + "v3/apin/invite/nearInvites";
    //请求码
//    public static final int REQUEST_CODE_HOME = 1000;
    public static final String URL_HOME = URL_SERVER + "v3/apin/invite/list";
    public static final String URL_HOME_INVITE_LIST = URL_SERVER + "v3/apin/invite/list2";
    public static final int REQUEST_CODE_HOME = 1000;


    //public static final String V3_APIN_SERVICE_MAINPUSH = "v3/apin/service/mainpush";
    /**
     * 餐厅列表页下轮播图请求地址以及请求参数
     */
    //请求地址
    public static final String URL_RESTAURANT_MAINPUSH = URL_SERVER + "v3/apin/service/mainpush";
    //请求码
    public static final int REQUEST_CODE_RESTAURANT_MAINPUSH = 1100;

    /**
     * 餐厅列表页请求地址以及请求参数
     */
    //请求地址
    public static final String URL_RESTAURANT = URL_SERVER + "v3/apin/service/index";
    //请求码
    public static final int REQUEST_CODE_RESTAURANT = 1120;

    /**
     * 新版发布界面点击价位获取服务列表
     */
    //请求地址
//    public static final String URL_SERVICE_LIST = URL_SERVER + "v3/apin/service/list";
    //请求码
//    public static final int REQUEST_CODE_SERVICE_LIST = 1121;
    public static final String URL_SERVICE_LIST = URL_SERVER + "v3/apin/service/newlist";
    public static final int REQUEST_CODE_SERVICE_LIST = 1121;


    /**
     * 餐厅内容页请求地址以及请求参数
     */
    //请求地址
    public static final String URL_RESTAURANT_DETAIL = URL_SERVER + "v3/apin/service/servicedetail";
    //请求码
    public static final int REQUEST_CODE_RESTAURANT_DETAIL = 1130;


    /**
     * 编辑个人信息也请求地址及参数
     */

    //请求地址
    public static final String URL_EDIT_PEOPLE_INFO = URL_SERVER + "v3/apil/users/detail";
    //请求码
    public static final int REQUEST_CODE_EDIT_PEOPLE_INFO = 1140;


    /**
     * 获取兴趣列表
     */

    //请求地址
    public static final String URL_INTEREST_LIST = URL_SERVER + "v3/apil/interestinfo/list";
    //请求码
    public static final int REQUEST_CODE_INTEREST_LIST = 1150;

    /**
     * 提交兴趣爱好
     */

    //请求地址
    public static final String URL_POST_INTERSTED = URL_SERVER + "v3/apil/interest/update";
    //请求码
    public static final int REQUEST_CODE_POST_INTERSTED = 1160;

    //请求地址
    public static final String URL_POST_INTERSTEDS = URL_SERVER + "/v3/apil/interestinfo/add";
    //请求码
    public static final int REQUEST_CODE_POST_INTERSTEDS = 1161;
    /**
     * 关注的人
     */

    //请求地址
    public static final String URL_FOCUS_PEOPLE = URL_SERVER + "v3/apil/foucs/foucslist";
    //请求码
    public static final int REQUEST_CODE_FOCUS_PEOPLE = 1170;

    /**
     * 取消关注
     */

    //请求地址
    public static final String URL_CANCLE_FOUCS_PEOPLE = URL_SERVER + "v3/apil/foucs/delfoucs";
    //请求码
    public static final int REQUEST_CODE_CANCLE_FOUCS_PEOPLE = 1180;


    /**
     * 邀请详情界面发起者的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_INVITE_INVETER = URL_SERVER + "v3/apil/invitedetail/inviter";
    //请求码
    public static final int REQUEST_CODE_INVITE_INVITER = 2000;
    /**
     * 邀请详情界面加入者的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_INVITE_JOINER = URL_SERVER + "v3/apil/invitedetail/joiner";
    //请求码
    public static final int REQUEST_CODE_INVITE_JOINER = 2001;
    //邀请详情界面的加入者和发布者同一成该接口
    public static final String URL_INVITE_DETAIL = URL_SERVER + "v3/apil/invitedetail/detail";
    public static final int REQUEST_CODE_INVITE_DETAIL = 2000;

    /**
     * 邀请详情下申请加入的请求地址以及请求参数
     */
    //请求地址
    // v1, v2版本
    //public static final String URL_INVITE_JOIN = URL_SERVER + "apil/invite/joinInvite";
    //v3 版本
//    public static final String URL_INVITE_JOIN = URL_SERVER + "v3/apil/join/myjoin";
    //v4 版本 2017年1月4日 13:46:31
    public static final String URL_INVITE_JOIN = URL_SERVER + "v3/apil/join/applyjoin";
    //请求码
    public static final int REQUEST_CODE_INVITE_JOIN = 2110;

    /**
     * 邀请详情下增加浏览记录的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_ADD_PREVIEW = URL_SERVER + "v3/apil/invitedetail/preview";
    //请求码
    public static final int REQUEST_CODE_ADD_PREVIEW = 2220;

    /**
     * 邀请详情下添加屏蔽用户邀请的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_ADD_SHIELD = URL_SERVER + "v3/apil/invitedetail/shield";
    //请求码
    public static final int REQUEST_CODE_ADD_SHIELD = 2220;

    /**
     * 登录网址
     */
    public static final String URL_LOGIN = URL_SERVER + "apin/users/login";
    public static final int REQUEST_CODE_LOGIN = 110;

    /**
     * v3版本快速登录登录网址
     */
    public static final String URL_LOGIN_QUICK = URL_SERVER + "v3/apin/users/login";
    public static final int REQUEST_CODE_LOGIN_QUICK = 111;

    /**
     * v3版本通过token获取用户信息登录网址
     */
    public static final String URL_LOGIN_BYTOKEN = URL_SERVER + "v3/apil/users/loginByToken";
    public static final int REQUEST_CODE_LOGIN_BYTOKEN = 112;

    /**
     * v3版本用户详情网址
     */
    public static final String URL_USER_DETAIL = URL_SERVER + "v3/apil/users/detail";
    public static final int REQUEST_CODE_USER_DETAIL = 113;

    /**
     * 同意他的网址
     */
    //public static final String URL_INVITE_ACCEPTUSER = URL_SERVER + "apil/invite/acceptUser";
    public static final String URL_INVITE_ACCEPTUSER = URL_SERVER + "v3/apil/inviteAccept/accept";
    public static final int REQUEST_CODE_INVITE_ACCEPTUSER = 101;

    /**
     * 等待对方付款的时候重新选择的网址
     */
    public static final String URL_INVITE_RESTACCEPTUSER = URL_SERVER + "v3/apil/inviteAccept/restAccept";
    public static final int REQUEST_CODE_INVITE_RESTACCEPTUSER = 101;

    /**
     * 获得阿里签名网址
     */
    public static final String URL_ALIPAY_SIGN = URL_SERVER + "apil/trade/alipaySign";
    public static final int REQUEST_CODE_ALIPAY_SIGN = 200;

    /**
     * 验证支付是否成功网址
     */
    public static final String URL_PAY_TRADE_SUCCESS = URL_SERVER + "v3/apil/trade/payTrade";
    public static final int REQUEST_CODE_PAY_TRADE_SUCCESS = 210;

    /**
     * 需要短信验证短信网址 :　登录界面的快速登录获取验证码　，　注册界面的获取验证码　，忘记密码重置．获取验证码；
     */
    public static final String URL_SEND_NUM = URL_SERVER + "apin/authcodes/create";
    public static final int REQUEST_CODE_SEND_NUM = 120;

    /**
     * 重置支付密码短信验证短信网址
     */
    public static final String URL_SEND_NUM_PAY = URL_SERVER + "apil/payPassword/authcode";
    public static final int REQUEST_CODE_SEND_NUM_PAY = 121;

    /**
     * 重置密码网址和请求参数
     */
    //请求地址
    public static final String URL_RESET_PASSWORD = URL_SERVER + "apin/users/resetPassword";
    //请求码
    public static final int REQUEST_CODE_RESET_PASSWORD = 140;

    /**
     * 重置密码网址和请求参数
     */
    //请求地址
    public static final String URL_RESET_PAY_PASSWORD = URL_SERVER + "apil/payPassword/reset";
    //请求码
    public static final int REQUEST_CODE_RESET_PAY_PASSWORD = 141;

    /**
     * 用户注册网址和请求参数
     */
    //请求地址
    public static final String URL_REGEST = URL_SERVER + "apin/users/reg";
    //请求码
    public static final int REQUEST_CODE_REGEST = 130;

    /**
     * 用户注册页面2网址和请求参数
     */
    //请求地址
    public static final String URL_REGEST_UPDATE_BASEINFO = URL_SERVER + "apil/users/updateUserBaseInfo";
    //请求码
    public static final int REQUEST_CODE_REGEST_UPDATE_BASEINFO = 131;

    /**
     * 用户验证是否有支付密码网址和请求参数
     */
    //请求地址
    public static final String URL_USER_PAY_PASSWORD = URL_SERVER + "apil/payPassword/check";
    //请求码
    public static final int REQUEST_CODE_USER_PAY_PASSWORD = 150;

    /**
     * 用户提现网址和请求参数
     */
    //请求地址
    public static final String URL_USER_DRAWAL_MONEY = URL_SERVER + "apil/purse/withdrawals";
    //请求码
    public static final int REQUEST_CODE_USER_DRAWAL_MONEY = 151;

    /**
     * 验证支付密码是正确网址和请求参数
     */
    //请求地址
    public static final String URL_USER_PAY_PASSWORD_RIGHT = URL_SERVER + "apil/payPassword/validate";
    //请求码
    public static final int REQUEST_CODE_USER_PAY_PASSWORD_RIGHT = 152;

    /**
     * 用户提现网址和请求参数
     */
    //请求地址
    public static final String URL_USER_REGEST_PAY_PWD = URL_SERVER + "apil/payPassword/add";
    //请求码
    public static final int REQUEST_CODE_USER_REGEST_PAY_PWD = 160;

    /**
     * 首页新闻中心网址和请求参数
     */
    //请求地址
    public static final String URL_USER_HOME_NEWS = URL_SERVER + "apil/message/newmessagelist";
    //请求码
    public static final int REQUEST_CODE_USER_HOME_NEWS = 300;

    /**
     * 个人中心的请求地址以及请求参数
     */
    //请求地址
    //public static final String URL_PERSON = URL_SERVER + "person";
    //请求码
//    public static final int REQUEST_CODE_PERSON = 3000;

    /**
     * 个人中心的编辑信息请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_EDIT = URL_SERVER + "apin/userInterest/interests";
    //请求码
    public static final int REQUEST_CODE_PERSON_EDIT = 3100;

    /**
     * 个人中心的修改个人信息请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_EDIT_UPDATE_INFO = URL_SERVER + "apil/users/updateInfo";
    //请求码
    public static final int REQUEST_CODE_PERSON_EDIT_UPDATE_INFO = 3101;

    /**
     * 个人中心的检测是否有关键屏蔽词请求地址以及请求参数
     */
    //请求地址
//    public static final String URL_PERSON_EDIT_SENSITIVE_CHECK = URL_SERVER + "v3/apin/sensitive/check";
    public static final String URL_PERSON_EDIT_SENSITIVE_CHECK = URL_SERVER + "v3/apin/sensitive/checkReplace";
    //请求码
    public static final int REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK = 3103;

    /**
     * 个人中心的修改个人兴趣请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_EDIT_UPDATE_INFO_INTEREST = URL_SERVER + "apil/userInterest/updateInterests";
    //请求码
    public static final int REQUEST_CODE_PERSON_EDIT_UPDATE_INFO_INTEREST = 3102;

    /**
     * v3版本出发状态请求地址以及请求参数
     */
    //请求地址
    public static final String URL_INVITE_SETOUT = URL_SERVER + "v3/apil/invite/setOut";
    //请求码
    public static final int REQUEST_CODE_INVITE_SETOUT = 3111;

    /**
     * 确认见面请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_MAKE_METTING = URL_SERVER + "apil/invite/makeMeeting";
    //请求码
    public static final int REQUEST_CODE_PERSON_MAKE_METTING = 3113;

    /**
     * 确认见面扫码商家请求地址以及请求参数
     */
    //请求地址
    public static final String URL_STORE_MAKE_METTING = URL_SERVER + "v3/apil/makeMeeting/makemeeting";
    //请求码
    public static final int REQUEST_CODE_STORE_MAKE_METTING = 3114;

    /**
     * 他的照片的照片信息请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_OTHER_PHOTO = URL_SERVER + "apil/users/photos";
    //请求码
    public static final int REQUEST_CODE_PERSON_OTHER_PHOTO = 3200;

    /**
     * 个人中心的钱包信息请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_WALLET = URL_SERVER + "apil/users/getPurse";
    //请求码
    public static final int REQUEST_CODE_PERSON_WALLET = 3300;

    /**
     * 查询套餐最大可用活动金额
     */
//    public static final String URL_MAX_SERVICE_PRICE = URL_SERVER + "apil/trade/maxServiceprice";
    public static final String URL_MAX_SERVICE_PRICE = URL_SERVER + "v3/apil/payment/maxServiceprice";
    public static final int REQUEST_CODE_SERVICE_PRICE = 3500;

    /**
     * 活动金额支付
     */
//    public static final String URL_ACTIVITY_PRICE = URL_SERVER + "apil/trade/activityPrice";
    public static final String URL_ACTIVITY_PRICE = URL_SERVER + "v3/apil/payment/payway";
    public static final int REQUEST_CODE_ACTIVITY_PRICE = 3700;

    /**
     * v3版本请求消费记录的网址和请求参数
     */
    //请求地址
    public static final String URL_PERSON_WALLET_EXPENSE = URL_SERVER + "v3/apil/account/list";
    //请求码
    public static final int REQUEST_CODE_PERSON_WALLET_EXPENSE = 3310;

    /**
     * 个人中心的钱包信息提现最大金额请求地址以及请求参数
     */
    //请求地址
    public static final String URL_PERSON_WALLET_MAXPRICE = URL_SERVER + "apil/purse/priceMax";
    //请求码
    public static final int REQUEST_CODE_PERSON_WALLET_MAXPRICE = 3311;

    /**
     * 个人中心我认识的人(我的朋友)及请求参数
     */
    //请求地址
    public static final String URL_PERSON_MY_FRIENDS = URL_SERVER + "apil/userFriend/friends";
    //请求码
    public static final int REQUEST_CODE_PERSON_MY_FRIENDS = 3400;

    /**
     * v3版本我的当前活动
     */
    //请求地址
    public static final String URL_MY_ORDER = URL_SERVER + "v3/apil/orderrecord/myordering";
    //请求码
    public static final int REQUEST_CODE_MY_ORDER = 4100;

    /**
     * V3版本我的历史活动
     */
    //请求地址
    public static final String URL_MY_HISTORY_ORDER = URL_SERVER + "v3/apil/orderrecord/histroyorder";
    //请求码
    public static final int REQUEST_CODE_HISTORY_ORDER = 4200;

    /**
     * 我的活动我发布的取消我发布的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_MY_DATA_SEND_CANCELMYINVITE = URL_SERVER + "apil/invite/cancelMyInvite";
    //请求码
    public static final int REQUEST_CODE_MY_DATA_SEND_CANCENMYINVITE = 4210;

    /**
     * 我的活动我加入的请求地址以及请求参数
     */
    //请求地址
    public static final String URL_MY_DATA_ADD = URL_SERVER + "apil/invite/myJoinedInvites";
    //请求码
    public static final int REQUEST_CODE_MY_DATA_ADD = 4300;

    /**
     * 发起活动头部轮播图请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_HOT = URL_SERVER + "apin/service/hot";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_HOT = 5100;

    /**
     * 发起活动热门餐厅请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_INDEX = URL_SERVER + "apin/service/index";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_INDEX = 5200;

    /**
     * 发起活动填写活动请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_RES_PHOTO = URL_SERVER + "apin/service/photos";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_RES_PHOTO = 5110;

    /**
     * 发起活动填写活动商家详情菜单请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_RES_RVICEMENU = URL_SERVER + "apin/service/serviceMenu";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_RES_RVICEMENU = 5111;

    /**
     * 发起活动填写商家详情图片请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_RES_RVICEMENU_PHOTOS = URL_SERVER + "apin/stores/storesPhoto";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_RES_RVICEMENU_PHOTOS = 5112;

    /**
     * 发起活动填写活动主题请求地址以及请求参数
     */
    //请求地址
    public static final String URL_SEND_DATA_RES_TITLE = URL_SERVER + "apin/inviteTitle/selectedlist";
    //    public static final String URL_SEND_DATA_RES_TITLE = URL_SERVER + "apin/inviteTitle/list";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_RES_TITLE = 5120;

    /**
     * 发起活动时请求地址以及请求参数
     */
    //请求地址
    //public static final String URL_SEND_DATA_SEND = URL_SERVER + "apil/invite/publishInvite";
//    public static final String URL_SEND_DATA_SEND = URL_SERVER + "v3/apil/push/mypush";
    public static final String URL_SEND_DATA_SEND = URL_SERVER + "v3/apil/push/submitpush";
    //请求码
    public static final int REQUEST_CODE_SEND_DATA_SEND = 5210;


    /**
     * 获取用户照片
     */
    public static final String URL_GEt_PHOTOS = URL_SERVER + "apil/users/photos";

    public static final int REQUEST_CODE_GET_PHOTOS = 8701;
    /**
     * 上传用户照片
     */
    public static final String URL_UPLOAD_PHOTOS = URL_SERVER + "apil/userPhoto/addPhoto";

    public static final int REQUEST_CODE_UPLOAD_PHOTOS = 8702;

    /**
     * 上传用户背景照片
     */
    public static final String URL_UPLOAD_BACKGROUND_PHOTOS = URL_SERVER + "v3/apil/users/background";

    public static final int REQUEST_CODE_UPLOAD_BACKGROUND_PHOTOS = 8706;

    /**
     * 上传用户音频
     */
    public static final String URL_UPLOAD_AUDIO = URL_SERVER + "apil/users/updateAudio";

    public static final int REQUEST_CODE_UPLOAD_AUDIO = 8703;

    /**
     * 删除用户音频
     */
    public static final String URL_DELETE_AUDIO = URL_SERVER + "v3/apil/users/deleteAudio";

    public static final int REQUEST_CODE_DELETE_AUDIO = 8704;

    /**
     * 删除照片
     */
    public static final String URL_DELETE_PHOTO = URL_SERVER + "apil/userPhoto/delPhotos";

    public static final int REQUEST_CODE_DELETE_PHOTO = 8705;

    /**
     * 添加视频
     */
    public static final String URL_ADD_VIDEO = URL_SERVER + "apil/users/updateVideo";
    /**
     * 申请退款
     */
    public static final String URL_REFUNDS = URL_SERVER + "apil/purse/applyRefund";
    public static final int REQUEST_CODE_AREFUNDS = 6000;
    public static final int REQUEST_CODE_ADD_VIDEO = 8707;

    /**
     * 删除视频
     */
    public static final String URL_DELETE_VIDEO = URL_SERVER + "v3/apil/users/deleteVideo";

    public static final int REQUEST_CODE_DELETE_VIDEO = 8708;

    /**
     * 更新logo
     */
    public static final String URL_UPDATE_LOGO = URL_SERVER + "apil/users/updateLogo";

    /**
     * 微信支付失败处理
     */
//    public static final String URL_TENPAY_ERROR = URL_SERVER + "apil/trade/tempayPayError";
    public static final String URL_TENPAY_ERROR = URL_SERVER + "v3/apil/tempay/tempayPayError";
    public static final int REQUEST_CODE_TENPAY_ERROR = 297;

    /**
     * 支付宝支付失败处理
     */
    public static final String URL_ALIPAY_SIGN_ERROR = URL_SERVER + "v3/apil/alipay/alipayPayError";
    public static final int REQUEST_CODE_ALIPAY_SIGN_ERROR = 296;

    /**
     * 附近的人
     */
    public static final String URL_NEARLY_USER = URL_SERVER + "v3/apin/nearlyuser/list";
    public static final int REQUEST_CODE_URL_NEARLY_USER = 300;

    /**
     * 认识的人
     */
    public static final String URL_RECOGNIZED_PEOPLE = URL_SERVER + "apil/userFriend/friends";
    public static final int REQUEST_CODE_RECOGNIZED_PEOPLE = 301;

    /**
     * 屏蔽认识的人
     */
    public static final String URL_RECOGNIZED_SCREEEN = URL_SERVER + "apil/userFriend/screening";
    public static final int REQUEST_CODE_RECOGNIZED_SCREEEN = 302;
    /**
     * 添加关注
     */
    public static final String URL_ADDFOCUS = URL_SERVER + "v3/apil/foucs/addfoucs";
    public static final int REQUEST_CODE_ADDFOCUS = 303;

    /**
     * 检测vip
     */
    public static final String URL_CHECK_VIP = URL_SERVER + "apil/vip/check";
    public static final int REQUEST_CHECK_VIP = 304;

    /**
     * 会员卡特权购买方案
     */
    public static final String URL_VIP_SCHEME = URL_SERVER + "apil/cheme/vipCard";
    public static final int REQUEST_VIP_SCHEME = 305;

    /**
     * 生成会员购买活动
     */
    public static final String URL_VIP_TRADE = URL_SERVER + "apil/vip/trade";
    public static final int REQUEST_CODE_VIP_TRADE = 306;

    /**
     * 取消我发起的邀请活动
     */
    public static final String URL_CANCEL_MYINVITE = URL_SERVER + "apil/invite/cancelMyInvite";
    public static final int REQUEST_CODE_CANCEL_MYINVITE = 307;

    /**
     * 取消我的参与
     */
    public static final String URL_CANCEL_MYJOIN = URL_SERVER + "apil/invite/cancelMyJoin";
    public static final int REQUEST_CODE_CANCEL_MYJOIN = 308;

    /**
     * 删除我的参与
     */
    public static final String URL_DELETE_MYJOIN = URL_SERVER + "apil/invite/deleteMyJoin";
    public static final int REQUEST_CODE_DELETE_MYJOIN = 309;

    /**
     * 删除我的邀请
     */
    public static final String URL_DELETE_MYINVITE = URL_SERVER + "apil/invite/deleteMyInvite";
    public static final int REQUEST_CODE_DELETE_MYINVITE = 310;
    /**
     * 微信支付
     */
    public static final String URL_WEIXIN_PAY = URL_SERVER + "v3/apil/tempay/unifiedorder";
    //    public static final String URL_WEIXIN_PAY = URL_SERVER + "v3/apil/tempay/weChatUnifiedorder";
    public static final int REQUEST_CODE_WEIXIN_PAY = 311;

    /**
     * 支付宝
     */
    public static final String URL_ZHIFUBAO_PAY = URL_SERVER + "v3/apil/alipay/sign";
    //    public static final String URL_ZHIFUBAO_PAY = URL_SERVER + "v3/apil/alipay/aliSign";
    public static final int REQUEST_CODE_ZHIFUBAO_PAY = 312;

    /* ***************    2016活动所有请求地址和请求码          ********** */
    //参与列表
    public static final String URL_ACTIVE_TACKMEOUT_LIST = URL_SERVER + "v3/apin/takemeout/list";
    public static final int REQUEST_CODE_ACTIVE_TACKMEOUT_LIST = 66;

    //我要报名
    public static final String URL_ACTIVE_TACKMEOUT_ADD = URL_SERVER + "v3/apil/takemeout/add";
    public static final int REQUEST_CODE_ACTIVE_TACKMEOUT_ADD = 67;

    //发送短信
    public static final String URL_ACTIVE_GET_AUTOCODE = URL_SERVER + "apil/authcodes/create";
    public static final int REQUEST_CODE_ACTIVE_GET_AUTOCODE = 68;

    //参与投票
    public static final String URL_ACTIVE_TACK_JOIN = URL_SERVER + "v3/apil/takemeout/join";
    public static final int REQUEST_CODE_ACTIVE_TACK_JOIN = 69;

    //我参与的
    public static final String URL_ACTIVE_MY_TAKE = URL_SERVER + "v3/apil/takemeout/mytakemeout";
    public static final int REQUEST_CODE_ACTIVE_MY_TAKE = 70;

    //我加入的
    public static final String URL_ACTIVE_MY_JOIN_LIST = URL_SERVER + "v3/apil/takemeout/myjoinlist";
    public static final int REQUEST_CODE_ACTIVE_MY_JOINLIST = 71;

    //用户信息下的加入列表
    public static final String URL_ACTIVE_USER_JOIN = URL_SERVER + "v3/apil/takemeout/takemeoutjoinlist";
    public static final int REQUEST_CODE_ACTIVE_USER_JOIN = 72;

    //主界面访问是否弹出活动广告
    public static final String URL_ACTIVE_SHOW_DIALOG = URL_SERVER + "v3/apin/dialog/list";
    public static final int REQUEST_CODE_ACTIVE_SHOW_DIALOG = 73;

    //活动主体界面头部信息
    public static final String URL_ACTIVE_ACTIVE_INFOS = URL_SERVER + "v3/apin/takemeout/statistics";
    public static final int REQUEST_CODE_ACTIVE_ACTIVE_INFOS = 74;

    //主页面活动显示方式
    public static final String URL_ACTIVE_MENU_LIST = URL_SERVER + "v3/apin/active/list";
    public static final int REQUEST_CODE_ACTIVE_MENU_LIST = 75;

    //主页面活动显示方式
    public static final String URL_ACTIVE_GET_CURRENT_SHARED = URL_SERVER + "v3/apin/active/current";
    public static final int REQUEST_CODE_ACTIVE_GET_CURRENT_SHARED = 76;

    /* ***************   2016活动所有请求地址和请求码完毕        ********** */

    /* ****************  v4版本新接口  ****************** */
    //主界面城市列表
    public static final String URL_ACTIVE_GET_CITY_LIST = URL_SERVER + "v3/apin/dictionary/citylist";
    public static final int REQUEST_CODE_ACTIVE_GET_CITY_LIST = 80;

    //搜索城市
    public static final String URL_ACTIVE_GET_CITY_SEARCH = URL_SERVER + "v3/apin/dictionary/citysearch";
    public static final int REQUEST_CODE_ACTIVE_GET_CITY_SEARCH = 81;

    //启动app的定位城市
    public static final String URL_ACTIVE_GET_CITY_LOCATION = URL_SERVER + "v3/apin/dictionary/verifyposition";
    public static final int REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION = 82;

    //获取优惠券的list
    public static final String URL_ACTIVE_GET_COUPONS = URL_SERVER + "v3/apil/coupon/list";
    public static final int REQUEST_CODE_ACTIVE_GET_COUPONS = 83;

    //获取用户是否为会员
    public static final String URL_USER_IS_VIP = URL_SERVER + "apil/vip/isvip";
    public static final int REQUEST_CODE_USER_IS_VIP = 84;

    //获取内谁界面信息, 即附近的用户
    public static final String URL_HOME_PERSONS = URL_SERVER + "v3/apin/nearlyuser/listbyarea";
    public static final int REQUEST_CODE_HOME_PERSONS = 85;

    //主界面点击加入按钮显示的三个图片
    public static final String URL_HOME_INVITE_LOGOLIST = URL_SERVER + "v3/apil/invite/logolist";
    public static final int REQUEST_CODE_HOME_INVITE_LOGOLIST = 86;

    //邀请详情界面的提前聊天接口
    public static final String URL_INVITE_CHAT = URL_SERVER + "v3/apil/invite/chat";
    public static final int REQUEST_CODE_INVITE_CHAT = 87;

    //会员中心抽奖借口
    public static final String URL_VIP_ACTIVE_NUM = URL_SERVER + "v3/apil/lottery/trial";
    public static final int REQUEST_CODE_VIP_ACTIVE_NUM = 88;

    //会员中心获取活动图片接口
    public static final String URL_VIP_ACTIVE_IMG = URL_SERVER + "v3/apil/lottery/vipImgUrl";
    public static final int REQUEST_CODE_VIP_ACTIVE_IMG = 89;

    //邀请详情界面用户爽约接口
    public static final String URL_INVITE_USER_MISSMEETING = URL_SERVER + "v3/apil/missmeet/dispose";
    public static final int REQUEST_CODE_INVITE_USER_MISSMEETING = 91;


    //申请退款接口
    public static final String URL_APPLYY_MONEY = URL_SERVER + "apil/purse/scheduleSearch";
    public static final int URL_APPLYY_MONEY_STATE = 6001;

    //取消申请接口
    public static final String URL_CANCEL_REFUNDS = URL_SERVER + "apil/purse/cancelapply";
    public static final int URL_CANCEL_REFUNDS_STATE = 6002;


    /* ****************  v4版本新接口结束  ****************** */

    /* ****************  v5版本新接口开始  ****************** */
    //我的行程接口
    public static final String URL_MY_TRIP = URL_SERVER + "v3/apil/inivtejourney/myjourney";
    public static final int REQUEST_CODE_MY_TRIP = 7001;

    //我的行程接提交评分
    public static final String URL_SUBMIT_GRADELEVEL = URL_SERVER + "v3/apil/inivtejourney/gradelevel";
    public static final int REQUEST_CODE_SUBMIT_GRADELEVEL = 7002;

    //附近的邀请banner图中点击广告 type=6 可以分享获取数据的接口
    public static final String URL_AD_SHARED = URL_SERVER + "v3/apil/userinvitedetail/shareurl";
    public static final int REQUEST_CODE_SAD_SHARED = 7003;

    //发布页面财富值接口
    public static final String RELEASEMONEY = URL_SERVER + "v3/apil/users/userCash";
    public static final int RELEASEMONEY_STATE = 6003;

    //发布页面财富值接口
    public static final String RELESEQUCIKURL = URL_SERVER + "v3/apil/push/push";
    public static final int RELESEQUCIKURL_STATE = 6004;

    //超时30分钟未完成获取未完成原因的接口
    public static final String URL_INVITE_REASON_LIST = URL_SERVER + "v3/apin/invite/reasonlist";
    public static final int REQUEST_CODE_INVITE_REASON_LIST = 7004;

    //超时30分钟未完成取消我发布的活动接口
    public static final String URL_INVITE_TIMEOUT_INVITE = URL_SERVER + "v3/apil/invite/cancelInvite";
    public static final int REQUEST_CODE_INVITE_TIMEOUT_INVITE = 7005;

    //超时30分钟未完成取消我发布的活动接口
    public static final String URL_INVITE_TIMEOUT_JOIN = URL_SERVER + "v3/apil/invite/cancelJoin";
    public static final int REQUEST_CODE_INVITE_TIMEOUT_JOIN = 7005;

    //我的所有的行程接口
    public static final String URL_MY_ORDER_LIST = URL_SERVER + "v3/apil/orderrecord/allJourney";
    public static final int REQUEST_CODE_MY_ORDER_LIST = 7006;

    public static final String URL_HOME_INVITE_JOIN = URL_SERVER + "v3/apil/join/whetherjoin";
    public static final int REQUEST_CODE_HOME_INVITE_JOIN = 7007;

    //内什么小助手
    public static final String URL_GET_NSM_SERVER_INFO = URL_SERVER + "apil/userFriend/nsmassistant";
    public static final int REQUEST_CODE_GET_NSM_SERVER_INFO = 7008;

    //网页普通活动是否加入
    public static final String URL_GET_ACTIVE_ISJOIN = URL_SERVER + "v3/apil/activitygroup/isjoin";
    public static final int REQUEST_CODE_GET_ACTIVE_ISJOIN = 7009;

    //网页普通活动申请加入
    public static final String URL_GET_ACTIVE_JOIN = URL_SERVER + "v3/apil/activitygroup/join";
    public static final int REQUEST_CODE_GET_ACTIVE_JOIN = 7010;

    //获取活动iphone7的弹幕信息
    public static final String URL_GET_DANMU_INFO = URL_SERVER + "v3/apin/active/iphoneStatistics";
    public static final int REQUEST_CODE_GET_DANMU_INFO = 7011;

    /* ****************  v5版本新接口开始  ****************** */

    //获取明星排行数据列表
    public static final String URL_GET_SUPER_STAR_LIST = URL_SERVER + "v3/apin/superStar/listStars";
    public static final int REQUEST_CODE_GET_SUPER_STAR_LIST = 7012;

    //明星立即约她 投票获取订单信息
    public static final String URL_VOTE_SUPER_STAR = URL_SERVER + "v3/apil/superStar/vote";
    public static final int REQUEST_CODE_VOTE_SUPER_STAR = 7013;

    //单个明星的 投票详情
    public static final String URL_SUPER_STAR_VOTE_DETAIL = URL_SERVER + "v3/apil/superStar/listStarDetail";
    public static final int REQUEST_CODE_SUPER_STAR_VOTE_DETAIL = 7014;

    //获取约会女明星活动中 我的投票详情 .
    public static final String URL_GET_STAR_MY_VOTE = URL_SERVER + "v3/apil/superStar/myTickets";
    public static final int REQUEST_CODE_GET_STAR_MY_VOTE = 7015;

    //获取约会女明星活动中 投票成功后排名 变化的接口
    public static final String URL_GET_STAR_MY_RANK_NUMBER = URL_SERVER + "v3/apil/superStar/rankIncreased";
    public static final int REQUEST_CODE_GET_STAR_MY_RANK_NUMBER = 7016;

    //获取约会女明星活动中 付款成功后抽奖后领奖
    public static final String URL_ACCETP_STAR_SHARED_PRIZE = URL_SERVER + "v3/apil/superStar/acceptPrize";
    public static final int REQUEST_CODE_ACCETP_STAR_SHARED_PRIZE = 7017;

    //兑换会员界面获取兑换的结果
    public static final String URL_CONVERT_VIP_VIPCARD = URL_SERVER + "v3/apil/vip/vipcard";
    public static final int REQUEST_CODE_CONVERT_VIP_VIPCARD = 7018;

    //兑换会员界面点击兑换获取结果
    public static final String URL_CONVERT_VIP_TRADE = URL_SERVER + "v3/apil/vip/trade";
    public static final int REQUEST_CODE_CONVERT_VIP_TRADE = 7019;

    //提现界面获取信息接口
    public static final String URL_PICK_MONEY_INFO = URL_SERVER + "v3/apil/purse/index";
    public static final int REQUEST_CODE_PICK_MONEY_INFO = 7020;

    //提现界面获取具体退款方案
    public static final String URL_PICK_MONEY_SCHEME = URL_SERVER + "v3/apil/purse/scheme";
    public static final int REQUEST_CODE_PICK_MONEY_SCHEME = 7021;

    //提现界面点击确认退款接口
    public static final String URL_PICK_MONEY_REFUND = URL_SERVER + "v3/apil/purse/refund";
    public static final int REQUEST_CODE_PICK_MONEY_REFUND = 7022;

    //提现界面点击取消退款接口
    public static final String URL_PICK_MONEY_CANCEL_REFUND = URL_SERVER + "v3/apil/purse/cancel";
    public static final int REQUEST_CODE_PICK_MONEY_CANCEL_REFUND = 7023;

}
