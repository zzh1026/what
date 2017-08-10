package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/16 13:34
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class InviteDetailResponse extends RBResponse {

    /**
     * code : 1
     * data : {"invite":{"audioDuration":1,"audioFile":"http://192.168.3.99:8888/audios/76bc1048508f495a8995695f776ac00d.m4a","createTime":1462927083770,"id":33735,"newstatus":100,"payType":1,"reception":0,"serviceId":52,"signing":0,"sort":0,"status":100,"storeLatitude":116.461528,"storeLongitude":116.461528,"target":1,"time":1462976103664,"title":"一起吃饭呗。有好事关照你哦。","userId":146},"joiners":[{"acceptTime":1462927205408,"acceptType":1,"createTime":1462927205408,"id":152452,"logo":"http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source.jpg","logoState":1,"name":"郎心如铁","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source_100x100.jpg","userId":485},{"acceptTime":1462927205408,"acceptType":2,"createTime":1462927205732,"id":152453,"logo":"http://192.168.3.99:8888/images/ad9f5e4df370404f82688af4652ae72c/source.jpg","logoState":1,"name":"玫兰枢","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ad9f5e4df370404f82688af4652ae72c/source_100x100.jpg","userId":489},{"acceptTime":0,"acceptType":0,"createTime":1462927206066,"id":152454,"logo":"http://192.168.3.99:8888/images/67d1aafa061047b8a8c034c26d3286c6/source.jpg","logoState":1,"name":"夜子轩牧","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/67d1aafa061047b8a8c034c26d3286c6/source_100x100.jpg","userId":507},{"acceptTime":0,"acceptType":0,"createTime":1462927322338,"id":152461,"logo":"http://192.168.3.99:8888/images/a0dbfddbd6b0469a924c40f4b389d3f9/source.jpg","logoState":1,"name":"宫尚名","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/a0dbfddbd6b0469a924c40f4b389d3f9/source_100x100.jpg","userId":474},{"acceptTime":0,"acceptType":0,"createTime":1462927322672,"id":152462,"logo":"http://192.168.3.99:8888/images/c3f289fa634c47d58694bca3d80cb5c1/source.jpg","logoState":1,"name":"成龙","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/c3f289fa634c47d58694bca3d80cb5c1/source_100x100.jpg","userId":459},{"acceptTime":0,"acceptType":0,"createTime":1462927323023,"id":152463,"logo":"http://192.168.3.99:8888/images/25d92b9c811e43d3b43fc3ed91ddeca9/source.jpg","logoState":1,"name":"沐剑枫","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/25d92b9c811e43d3b43fc3ed91ddeca9/source_100x100.jpg","userId":496},{"acceptTime":0,"acceptType":0,"createTime":1462927443831,"id":152477,"logo":"http://192.168.3.99:8888/images/02875812d3244d33a94ac8363898f50d/source.jpg","logoState":1,"name":"才哲","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/02875812d3244d33a94ac8363898f50d/source_100x100.jpg","userId":465},{"acceptTime":0,"acceptType":0,"createTime":1462927444413,"id":152478,"logo":"http://192.168.3.99:8888/images/b9baee223566413ca135d29787132292/source.jpg","logoState":1,"name":"小民","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/b9baee223566413ca135d29787132292/source_100x100.jpg","userId":504},{"acceptTime":0,"acceptType":0,"createTime":1462927572186,"id":152487,"logo":"http://192.168.3.99:8888/images/3a5cc561353c4774a80dd2fa83152609/source.jpg","logoState":1,"name":"南人","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/3a5cc561353c4774a80dd2fa83152609/source_100x100.jpg","userId":512},{"acceptTime":0,"acceptType":0,"createTime":1462927572503,"id":152488,"logo":"http://192.168.3.99:8888/images/2b020cb15f0a4b28b99ea8cd9158dc22/source.jpg","logoState":1,"name":"借个火","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/2b020cb15f0a4b28b99ea8cd9158dc22/source_100x100.jpg","userId":482},{"acceptTime":0,"acceptType":0,"createTime":1462927572820,"id":152489,"logo":"http://192.168.3.99:8888/images/8e77f48007d94057875ca182dc60c034/source.jpg","logoState":1,"name":"博瀚","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/8e77f48007d94057875ca182dc60c034/source_100x100.jpg","userId":457},{"acceptTime":0,"acceptType":0,"createTime":1462927680821,"id":152502,"logo":"http://192.168.3.99:8888/images/94ae1e3c8bc84a45b1090961225c1d64/source.jpg","logoState":1,"name":"斌斌","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/94ae1e3c8bc84a45b1090961225c1d64/source_100x100.jpg","userId":455},{"acceptTime":0,"acceptType":0,"createTime":1462927681173,"id":152503,"logo":"http://192.168.3.99:8888/images/dbf0e16dcc654aad8c8da324533a8337/source.jpg","logoState":1,"name":"少爷","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/dbf0e16dcc654aad8c8da324533a8337/source_100x100.jpg","userId":502},{"acceptTime":0,"acceptType":0,"createTime":1462927681524,"id":152504,"logo":"http://192.168.3.99:8888/images/ed053e22b21a403f8bec4efbfdf7640e/source.jpg","logoState":1,"name":"和4G","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ed053e22b21a403f8bec4efbfdf7640e/source_100x100.jpg","userId":477},{"acceptTime":0,"acceptType":0,"createTime":1462927800447,"id":152514,"logo":"http://192.168.3.99:8888/images/39fb948ac91d43ccbfbd85623a8327f7/source.jpg","logoState":1,"name":"飞羽","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/39fb948ac91d43ccbfbd85623a8327f7/source_100x100.jpg","userId":466},{"acceptTime":0,"acceptType":0,"createTime":1462927800797,"id":152515,"logo":"http://192.168.3.99:8888/images/6d4810ce65de4e23a37f880a6d58c511/source.jpg","logoState":1,"name":"随风","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/6d4810ce65de4e23a37f880a6d58c511/source_100x100.jpg","userId":444},{"acceptTime":0,"acceptType":0,"createTime":1462927801148,"id":152516,"logo":"http://192.168.3.99:8888/images/d77018073b924d179f08a18d09012654/source.jpg","logoState":1,"name":"轩辕","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/d77018073b924d179f08a18d09012654/source_100x100.jpg","userId":505},{"acceptTime":0,"acceptType":0,"createTime":1462970124165,"id":153562,"logo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source.jpg","logoState":0,"name":"ellzu1","newstatus":100,"phone":"06786","signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source_100x100.jpg","userId":46}],"servicephotos":[{"id":617,"photo":"http://192.168.3.99:8888/images/ac9c313b5e624e4fbe82f7ce5e567865/source.jpg","sort":0},{"id":618,"photo":"http://192.168.3.99:8888/images/c36089a77d294d3da93fc342befec683/scale_974x550.jpg","sort":0},{"id":619,"photo":"http://192.168.3.99:8888/images/cff96329a6f8492fa1f7bef46c7ad313/scale_984x527.jpg","sort":0},{"id":620,"photo":"http://192.168.3.99:8888/images/dc90f4be0cbf4a90a82b6b648f7ee240/scale_986x588.jpg","sort":0},{"id":621,"photo":"http://192.168.3.99:8888/images/e8ac26e42402496b909b9c68ea886af3/scale_989x578.jpg","sort":0},{"id":622,"photo":"http://192.168.3.99:8888/images/0b166c57c39c49faa8d5f07dd3784cac/scale_994x573.jpg","sort":0},{"id":623,"photo":"http://192.168.3.99:8888/images/a39a8164701e4fe084cbfe6c80d5095a/scale_993x616.jpg","sort":0},{"id":624,"photo":"http://192.168.3.99:8888/images/6602a3776a424fe9bce875d2ae41959b/scale_996x605.jpg","sort":0},{"photo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","sort":0,"thumbnails":"images/aebfb11102da43b19f43fa637454f315/source.jpg"}],"services":{"content":"凉菜:  鲜辣冷汁炝元贝，砂礓葱香去骨鸡，冰糖甜酒糯米枣，杭州素鹅。 热菜:  清汤极品佛跳墙 龙井虾仁配芦笋 低温慢烤菲力牛排 罗汉四季 素斋包蒸笋壳  时蔬拼盘 主食: 桂圆紫米粥配铁锅生煎包 鲜果拼盘","dinner":"16:00-22:00","id":52,"logo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","lunch":"11:00-13:00","name":"静享食光","price":2000,"sort":0,"status":0,"storeId":23,"storeLatitude":116.461528,"storeLongitude":116.461528,"timeArray":"11,12,13,16,17,18,19,20,21,22"},"store":{"addr":"朝阳区 工人体育场西路西门院内北侧","addrDetail":"朝阳区工人体育场西路","city":"北京","contact":"010-65518812 010-65518813 ","id":23,"latitude":116.451178,"logo":"http://192.168.3.99:8888/stores/23/46b01566-f0f4-4e98-b353-cb3e6af5b506.jpg","longitude":39.937356,"name":"许仙楼","status":1},"trade":{"activitypurse":0,"id":177471,"jobId":153562,"jobType":2,"leftmoney":0,"paymentStatus":100,"price":0,"remark":"","tradeNum":"nsm20160511083524720298","userId":46,"userpurse":0},"user":{"account":"rot146tst","audioDuration":0,"audioFile":"http://192.168.3.99:8888/0","birthday":799516800000,"gender":2,"id":146,"logo":"http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47.jpg","logoState":1,"name":"清声扬","sign":"遇到喜欢的人一定要表白，你丑没关系，万一他瞎呢","thumbnailslogo":"http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47_100x100.jpg","videoDuration":0},"userphones":[{"id":446,"photo":"http://192.168.3.99:8888/users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552.jpg","photoState":1,"thumbnails":"users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_57600.jpg","thumbnailsPhoto":"users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_100x100.jpg"},{"id":447,"photo":"http://192.168.3.99:8888/users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4.jpg","photoState":1,"thumbnails":"users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4_57600.jpg","thumbnailsPhoto":"users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4_100x100.jpg"},{"id":448,"photo":"http://192.168.3.99:8888/users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10.jpg","photoState":1,"thumbnails":"users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10_57600.jpg","thumbnailsPhoto":"users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10_100x100.jpg"}]}
     * message : success
     */

    private int code;
    /**
     * invite : {"audioDuration":1,"audioFile":"http://192.168.3.99:8888/audios/76bc1048508f495a8995695f776ac00d.m4a","createTime":1462927083770,"id":33735,"newstatus":100,"payType":1,"reception":0,"serviceId":52,"signing":0,"sort":0,"status":100,"storeLatitude":116.461528,"storeLongitude":116.461528,"target":1,"time":1462976103664,"title":"一起吃饭呗。有好事关照你哦。","userId":146}
     * joiners : [{"acceptTime":1462927205408,"acceptType":1,"createTime":1462927205408,"id":152452,"logo":"http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source.jpg","logoState":1,"name":"郎心如铁","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source_100x100.jpg","userId":485},{"acceptTime":1462927205408,"acceptType":2,"createTime":1462927205732,"id":152453,"logo":"http://192.168.3.99:8888/images/ad9f5e4df370404f82688af4652ae72c/source.jpg","logoState":1,"name":"玫兰枢","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ad9f5e4df370404f82688af4652ae72c/source_100x100.jpg","userId":489},{"acceptTime":0,"acceptType":0,"createTime":1462927206066,"id":152454,"logo":"http://192.168.3.99:8888/images/67d1aafa061047b8a8c034c26d3286c6/source.jpg","logoState":1,"name":"夜子轩牧","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/67d1aafa061047b8a8c034c26d3286c6/source_100x100.jpg","userId":507},{"acceptTime":0,"acceptType":0,"createTime":1462927322338,"id":152461,"logo":"http://192.168.3.99:8888/images/a0dbfddbd6b0469a924c40f4b389d3f9/source.jpg","logoState":1,"name":"宫尚名","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/a0dbfddbd6b0469a924c40f4b389d3f9/source_100x100.jpg","userId":474},{"acceptTime":0,"acceptType":0,"createTime":1462927322672,"id":152462,"logo":"http://192.168.3.99:8888/images/c3f289fa634c47d58694bca3d80cb5c1/source.jpg","logoState":1,"name":"成龙","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/c3f289fa634c47d58694bca3d80cb5c1/source_100x100.jpg","userId":459},{"acceptTime":0,"acceptType":0,"createTime":1462927323023,"id":152463,"logo":"http://192.168.3.99:8888/images/25d92b9c811e43d3b43fc3ed91ddeca9/source.jpg","logoState":1,"name":"沐剑枫","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/25d92b9c811e43d3b43fc3ed91ddeca9/source_100x100.jpg","userId":496},{"acceptTime":0,"acceptType":0,"createTime":1462927443831,"id":152477,"logo":"http://192.168.3.99:8888/images/02875812d3244d33a94ac8363898f50d/source.jpg","logoState":1,"name":"才哲","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/02875812d3244d33a94ac8363898f50d/source_100x100.jpg","userId":465},{"acceptTime":0,"acceptType":0,"createTime":1462927444413,"id":152478,"logo":"http://192.168.3.99:8888/images/b9baee223566413ca135d29787132292/source.jpg","logoState":1,"name":"小民","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/b9baee223566413ca135d29787132292/source_100x100.jpg","userId":504},{"acceptTime":0,"acceptType":0,"createTime":1462927572186,"id":152487,"logo":"http://192.168.3.99:8888/images/3a5cc561353c4774a80dd2fa83152609/source.jpg","logoState":1,"name":"南人","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/3a5cc561353c4774a80dd2fa83152609/source_100x100.jpg","userId":512},{"acceptTime":0,"acceptType":0,"createTime":1462927572503,"id":152488,"logo":"http://192.168.3.99:8888/images/2b020cb15f0a4b28b99ea8cd9158dc22/source.jpg","logoState":1,"name":"借个火","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/2b020cb15f0a4b28b99ea8cd9158dc22/source_100x100.jpg","userId":482},{"acceptTime":0,"acceptType":0,"createTime":1462927572820,"id":152489,"logo":"http://192.168.3.99:8888/images/8e77f48007d94057875ca182dc60c034/source.jpg","logoState":1,"name":"博瀚","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/8e77f48007d94057875ca182dc60c034/source_100x100.jpg","userId":457},{"acceptTime":0,"acceptType":0,"createTime":1462927680821,"id":152502,"logo":"http://192.168.3.99:8888/images/94ae1e3c8bc84a45b1090961225c1d64/source.jpg","logoState":1,"name":"斌斌","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/94ae1e3c8bc84a45b1090961225c1d64/source_100x100.jpg","userId":455},{"acceptTime":0,"acceptType":0,"createTime":1462927681173,"id":152503,"logo":"http://192.168.3.99:8888/images/dbf0e16dcc654aad8c8da324533a8337/source.jpg","logoState":1,"name":"少爷","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/dbf0e16dcc654aad8c8da324533a8337/source_100x100.jpg","userId":502},{"acceptTime":0,"acceptType":0,"createTime":1462927681524,"id":152504,"logo":"http://192.168.3.99:8888/images/ed053e22b21a403f8bec4efbfdf7640e/source.jpg","logoState":1,"name":"和4G","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ed053e22b21a403f8bec4efbfdf7640e/source_100x100.jpg","userId":477},{"acceptTime":0,"acceptType":0,"createTime":1462927800447,"id":152514,"logo":"http://192.168.3.99:8888/images/39fb948ac91d43ccbfbd85623a8327f7/source.jpg","logoState":1,"name":"飞羽","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/39fb948ac91d43ccbfbd85623a8327f7/source_100x100.jpg","userId":466},{"acceptTime":0,"acceptType":0,"createTime":1462927800797,"id":152515,"logo":"http://192.168.3.99:8888/images/6d4810ce65de4e23a37f880a6d58c511/source.jpg","logoState":1,"name":"随风","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/6d4810ce65de4e23a37f880a6d58c511/source_100x100.jpg","userId":444},{"acceptTime":0,"acceptType":0,"createTime":1462927801148,"id":152516,"logo":"http://192.168.3.99:8888/images/d77018073b924d179f08a18d09012654/source.jpg","logoState":1,"name":"轩辕","newstatus":100,"signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/d77018073b924d179f08a18d09012654/source_100x100.jpg","userId":505},{"acceptTime":0,"acceptType":0,"createTime":1462970124165,"id":153562,"logo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source.jpg","logoState":0,"name":"ellzu1","newstatus":100,"phone":"06786","signing":0,"status":100,"thumbnailslogo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source_100x100.jpg","userId":46}]
     * servicephotos : [{"id":617,"photo":"http://192.168.3.99:8888/images/ac9c313b5e624e4fbe82f7ce5e567865/source.jpg","sort":0},{"id":618,"photo":"http://192.168.3.99:8888/images/c36089a77d294d3da93fc342befec683/scale_974x550.jpg","sort":0},{"id":619,"photo":"http://192.168.3.99:8888/images/cff96329a6f8492fa1f7bef46c7ad313/scale_984x527.jpg","sort":0},{"id":620,"photo":"http://192.168.3.99:8888/images/dc90f4be0cbf4a90a82b6b648f7ee240/scale_986x588.jpg","sort":0},{"id":621,"photo":"http://192.168.3.99:8888/images/e8ac26e42402496b909b9c68ea886af3/scale_989x578.jpg","sort":0},{"id":622,"photo":"http://192.168.3.99:8888/images/0b166c57c39c49faa8d5f07dd3784cac/scale_994x573.jpg","sort":0},{"id":623,"photo":"http://192.168.3.99:8888/images/a39a8164701e4fe084cbfe6c80d5095a/scale_993x616.jpg","sort":0},{"id":624,"photo":"http://192.168.3.99:8888/images/6602a3776a424fe9bce875d2ae41959b/scale_996x605.jpg","sort":0},{"photo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","sort":0,"thumbnails":"images/aebfb11102da43b19f43fa637454f315/source.jpg"}]
     * services : {"content":"凉菜:  鲜辣冷汁炝元贝，砂礓葱香去骨鸡，冰糖甜酒糯米枣，杭州素鹅。 热菜:  清汤极品佛跳墙 龙井虾仁配芦笋 低温慢烤菲力牛排 罗汉四季 素斋包蒸笋壳  时蔬拼盘 主食: 桂圆紫米粥配铁锅生煎包 鲜果拼盘","dinner":"16:00-22:00","id":52,"logo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","lunch":"11:00-13:00","name":"静享食光","price":2000,"sort":0,"status":0,"storeId":23,"storeLatitude":116.461528,"storeLongitude":116.461528,"timeArray":"11,12,13,16,17,18,19,20,21,22"}
     * store : {"addr":"朝阳区 工人体育场西路西门院内北侧","addrDetail":"朝阳区工人体育场西路","city":"北京","contact":"010-65518812 010-65518813 ","id":23,"latitude":116.451178,"logo":"http://192.168.3.99:8888/stores/23/46b01566-f0f4-4e98-b353-cb3e6af5b506.jpg","longitude":39.937356,"name":"许仙楼","status":1}
     * trade : {"activitypurse":0,"id":177471,"jobId":153562,"jobType":2,"leftmoney":0,"paymentStatus":100,"price":0,"remark":"","tradeNum":"nsm20160511083524720298","userId":46,"userpurse":0}
     * user : {"account":"rot146tst","audioDuration":0,"audioFile":"http://192.168.3.99:8888/0","birthday":799516800000,"gender":2,"id":146,"logo":"http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47.jpg","logoState":1,"name":"清声扬","sign":"遇到喜欢的人一定要表白，你丑没关系，万一他瞎呢","thumbnailslogo":"http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47_100x100.jpg","videoDuration":0}
     * userphones : [{"id":446,"photo":"http://192.168.3.99:8888/users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552.jpg","photoState":1,"thumbnails":"users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_57600.jpg","thumbnailsPhoto":"users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_100x100.jpg"},{"id":447,"photo":"http://192.168.3.99:8888/users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4.jpg","photoState":1,"thumbnails":"users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4_57600.jpg","thumbnailsPhoto":"users/146/photos/1218d2b4-e02b-4d72-9fc4-af94aa1dd1b4_100x100.jpg"},{"id":448,"photo":"http://192.168.3.99:8888/users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10.jpg","photoState":1,"thumbnails":"users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10_57600.jpg","thumbnailsPhoto":"users/146/photos/f646c384-9c93-4c07-8851-d8352d9fdd10_100x100.jpg"}]
     */

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
        private int showMap;

        /**
         * audioDuration : 1
         * audioFile : http://192.168.3.99:8888/audios/76bc1048508f495a8995695f776ac00d.m4a
         * createTime : 1462927083770
         * id : 33735
         * newstatus : 100
         * payType : 1
         * reception : 0
         * serviceId : 52
         * signing : 0
         * sort : 0
         * status : 100
         * storeLatitude : 116.461528
         * storeLongitude : 116.461528
         * target : 1
         * time : 1462976103664
         * title : 一起吃饭呗。有好事关照你哦。
         * userId : 146
         */

        private InviteEntity invite;
        /**
         * content : 凉菜:  鲜辣冷汁炝元贝，砂礓葱香去骨鸡，冰糖甜酒糯米枣，杭州素鹅。 热菜:  清汤极品佛跳墙 龙井虾仁配芦笋 低温慢烤菲力牛排 罗汉四季 素斋包蒸笋壳  时蔬拼盘 主食: 桂圆紫米粥配铁锅生煎包 鲜果拼盘
         * dinner : 16:00-22:00
         * id : 52
         * logo : http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg
         * lunch : 11:00-13:00
         * name : 静享食光
         * price : 2000
         * sort : 0
         * status : 0
         * storeId : 23
         * storeLatitude : 116.461528
         * storeLongitude : 116.461528
         * timeArray : 11,12,13,16,17,18,19,20,21,22
         */

        private ServicesEntity services;
        /**
         * addr : 朝阳区 工人体育场西路西门院内北侧
         * addrDetail : 朝阳区工人体育场西路
         * city : 北京
         * contact : 010-65518812 010-65518813
         * id : 23
         * latitude : 116.451178
         * logo : http://192.168.3.99:8888/stores/23/46b01566-f0f4-4e98-b353-cb3e6af5b506.jpg
         * longitude : 39.937356
         * name : 许仙楼
         * status : 1
         */

        private StoreEntity store;
        /**
         * activitypurse : 0
         * id : 177471
         * jobId : 153562
         * jobType : 2
         * leftmoney : 0
         * paymentStatus : 100
         * price : 0
         * remark :
         * tradeNum : nsm20160511083524720298
         * userId : 46
         * userpurse : 0
         */

        private TradeEntity trade;
        /**
         * account : rot146tst
         * audioDuration : 0
         * audioFile : http://192.168.3.99:8888/0
         * birthday : 799516800000
         * gender : 2
         * id : 146
         * logo : http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47.jpg
         * logoState : 1
         * name : 清声扬
         * sign : 遇到喜欢的人一定要表白，你丑没关系，万一他瞎呢
         * thumbnailslogo : http://192.168.3.99:8888/users/146/a04bcb92-d0ee-4371-bdec-87d42ffe2b47_100x100.jpg
         * videoDuration : 0
         */

        private UserEntity user;
        /**
         * acceptTime : 1462927205408
         * acceptType : 1
         * createTime : 1462927205408
         * id : 152452
         * logo : http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source.jpg
         * logoState : 1
         * name : 郎心如铁
         * newstatus : 100
         * signing : 0
         * status : 100
         * thumbnailslogo : http://192.168.3.99:8888/images/83f966d269fa4313a1b19a8aed9fb4b7/source_100x100.jpg
         * userId : 485
         */

        private List<JoinersEntity> joiners;
        /**
         * id : 617
         * photo : http://192.168.3.99:8888/images/ac9c313b5e624e4fbe82f7ce5e567865/source.jpg
         * sort : 0
         */

        private List<ServicephotosEntity> servicephotos;
        /**
         * id : 446
         * photo : http://192.168.3.99:8888/users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552.jpg
         * photoState : 1
         * thumbnails : users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_57600.jpg
         * thumbnailsPhoto : users/146/photos/0b7ddca9-171f-4bf7-b6b3-3bf909706552_100x100.jpg
         */

        private List<UserphonesEntity> userphones;

        public void setInvite(InviteEntity invite) {
            this.invite = invite;
        }

        public void setServices(ServicesEntity services) {
            this.services = services;
        }

        public void setStore(StoreEntity store) {
            this.store = store;
        }

        public void setTrade(TradeEntity trade) {
            this.trade = trade;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setJoiners(List<JoinersEntity> joiners) {
            this.joiners = joiners;
        }

        public void setServicephotos(List<ServicephotosEntity> servicephotos) {
            this.servicephotos = servicephotos;
        }

        public void setUserphones(List<UserphonesEntity> userphones) {
            this.userphones = userphones;
        }

        public int getShowMap() {
            return showMap;
        }

        public void setShowMap(int showMap) {
            this.showMap = showMap;
        }

        public InviteEntity getInvite() {
            return invite;
        }

        public ServicesEntity getServices() {
            return services;
        }

        public StoreEntity getStore() {
            return store;
        }

        public TradeEntity getTrade() {
            return trade;
        }

        public UserEntity getUser() {
            return user;
        }

        public List<JoinersEntity> getJoiners() {
            return joiners;
        }

        public List<ServicephotosEntity> getServicephotos() {
            return servicephotos;
        }

        public List<UserphonesEntity> getUserphones() {
            return userphones;
        }

        public static class InviteEntity {
            private int audioDuration;
            private String audioFile;
            private long createTime;
            private int id;
            private int newstatus;
            private int payType;
            private int reception;
            private int serviceId;
            private int setout;
            private int signing;
            private int sort;
            private int status;
            private double storeLatitude;
            private double storeLongitude;
            private int target;
            private long time;
            private String title;
            private int userId;

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public void setReception(int reception) {
                this.reception = reception;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
            }

            public void setSigning(int signing) {
                this.signing = signing;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setStoreLatitude(double storeLatitude) {
                this.storeLatitude = storeLatitude;
            }

            public void setStoreLongitude(double storeLongitude) {
                this.storeLongitude = storeLongitude;
            }

            public void setTarget(int target) {
                this.target = target;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public long getCreateTime() {
                return createTime;
            }

            public int getId() {
                return id;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public int getPayType() {
                return payType;
            }

            public int getReception() {
                return reception;
            }

            public int getServiceId() {
                return serviceId;
            }

            public int getSigning() {
                return signing;
            }

            public int getSetout() {
                return setout;
            }

            public int getSort() {
                return sort;
            }

            public int getStatus() {
                return status;
            }

            public double getStoreLatitude() {
                return storeLatitude;
            }

            public double getStoreLongitude() {
                return storeLongitude;
            }

            public int getTarget() {
                return target;
            }

            public long getTime() {
                return time;
            }

            public String getTitle() {
                return title;
            }

            public int getUserId() {
                return userId;
            }
        }

        public static class ServicesEntity {
            private String content;
            private String dinner;
            private int id;
            private String logo;
            private String lunch;
            private String name;
            private int price;
            private int sort;
            private int status;
            private int storeId;
            private double storeLatitude;
            private double storeLongitude;
            private String timeArray;

            public void setContent(String content) {
                this.content = content;
            }

            public void setDinner(String dinner) {
                this.dinner = dinner;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLunch(String lunch) {
                this.lunch = lunch;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public void setStoreLatitude(double storeLatitude) {
                this.storeLatitude = storeLatitude;
            }

            public void setStoreLongitude(double storeLongitude) {
                this.storeLongitude = storeLongitude;
            }

            public void setTimeArray(String timeArray) {
                this.timeArray = timeArray;
            }

            public String getContent() {
                return content;
            }

            public String getDinner() {
                return dinner;
            }

            public int getId() {
                return id;
            }

            public String getLogo() {
                return logo;
            }

            public String getLunch() {
                return lunch;
            }

            public String getName() {
                return name;
            }

            public int getPrice() {
                return price;
            }

            public int getSort() {
                return sort;
            }

            public int getStatus() {
                return status;
            }

            public int getStoreId() {
                return storeId;
            }

            public double getStoreLatitude() {
                return storeLatitude;
            }

            public double getStoreLongitude() {
                return storeLongitude;
            }

            public String getTimeArray() {
                return timeArray;
            }
        }

        public static class StoreEntity {
            private String addr;
            private String addrDetail;
            private String city;
            private String contact;
            private int id;
            private double latitude;
            private String logo;
            private double longitude;
            private String name;
            private int status;

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public void setAddrDetail(String addrDetail) {
                this.addrDetail = addrDetail;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAddr() {
                return addr;
            }

            public String getAddrDetail() {
                return addrDetail;
            }

            public String getCity() {
                return city;
            }

            public String getContact() {
                return contact;
            }

            public int getId() {
                return id;
            }

            public double getLatitude() {
                return latitude;
            }

            public String getLogo() {
                return logo;
            }

            public double getLongitude() {
                return longitude;
            }

            public String getName() {
                return name;
            }

            public int getStatus() {
                return status;
            }
        }

        public static class TradeEntity {
            private double activitypurse;
            private int id;
            private int jobId;
            private int jobType;
            private double leftmoney;
            private int paymentStatus;
            private double price;
            private String remark;
            private String tradeNum;
            private int userId;
            private double userpurse;

            public void setActivitypurse(double activitypurse) {
                this.activitypurse = activitypurse;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setJobId(int jobId) {
                this.jobId = jobId;
            }

            public void setJobType(int jobType) {
                this.jobType = jobType;
            }

            public void setLeftmoney(double leftmoney) {
                this.leftmoney = leftmoney;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setTradeNum(String tradeNum) {
                this.tradeNum = tradeNum;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setUserpurse(double userpurse) {
                this.userpurse = userpurse;
            }

            public double getActivitypurse() {
                return activitypurse;
            }

            public int getId() {
                return id;
            }

            public int getJobId() {
                return jobId;
            }

            public int getJobType() {
                return jobType;
            }

            public double getLeftmoney() {
                return leftmoney;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public double getPrice() {
                return price;
            }

            public String getRemark() {
                return remark;
            }

            public String getTradeNum() {
                return tradeNum;
            }

            public int getUserId() {
                return userId;
            }

            public double getUserpurse() {
                return userpurse;
            }
        }

        public static class UserEntity {
            private String account;
            private int audioDuration;
            private String audioFile;
            private String hxUserName;
            private long birthday;
            private int gender;
            private int id;
            private String logo;
            private int logoState;
            private String name;
            private String sign;
            private String thumbnailslogo;
            private int videoDuration;

            public void setAccount(String account) {
                this.account = account;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public String getAccount() {
                return account;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public long getBirthday() {
                return birthday;
            }

            public int getGender() {
                return gender;
            }

            public int getId() {
                return id;
            }

            public String getLogo() {
                return logo;
            }

            public int getLogoState() {
                return logoState;
            }

            public String getName() {
                return name;
            }

            public String getSign() {
                return sign;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public int getVideoDuration() {
                return videoDuration;
            }
        }

        public static class JoinersEntity {
            private long acceptTime;
            private int acceptType;
            private long createTime;
            private int isfranchise;
            private String phone;
            private int id;
            private String logo;
            private int logoState;
            private int setout;
            private String name;
            private int newstatus;
            private int signing;
            private int status;
            private String thumbnailslogo;
            private int userId;

            public void setAcceptTime(long acceptTime) {
                this.acceptTime = acceptTime;
            }

            public void setAcceptType(int acceptType) {
                this.acceptType = acceptType;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setIsfranchise(int isfranchise) {
                this.isfranchise = isfranchise;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public void setSigning(int signing) {
                this.signing = signing;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public long getAcceptTime() {
                return acceptTime;
            }

            public int getAcceptType() {
                return acceptType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public int getIsfranchise() {
                return isfranchise;
            }

            public String getPhone() {
                return phone;
            }

            public int getId() {
                return id;
            }

            public String getLogo() {
                return logo;
            }

            public int getLogoState() {
                return logoState;
            }

            public int getSetout() {
                return setout;
            }

            public String getName() {
                return name;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public int getSigning() {
                return signing;
            }

            public int getStatus() {
                return status;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public int getUserId() {
                return userId;
            }
        }

        public static class ServicephotosEntity {
            private int id;
            private String photo;
            private int sort;

            public void setId(int id) {
                this.id = id;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getId() {
                return id;
            }

            public String getPhoto() {
                return photo;
            }

            public int getSort() {
                return sort;
            }
        }

        public static class UserphonesEntity {
            private int id;
            private String photo;
            private int photoState;
            private String thumbnails;
            private String thumbnailsPhoto;

            public void setId(int id) {
                this.id = id;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public void setPhotoState(int photoState) {
                this.photoState = photoState;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public void setThumbnailsPhoto(String thumbnailsPhoto) {
                this.thumbnailsPhoto = thumbnailsPhoto;
            }

            public int getId() {
                return id;
            }

            public String getPhoto() {
                return photo;
            }

            public int getPhotoState() {
                return photoState;
            }

            public String getThumbnails() {
                return thumbnails;
            }

            public String getThumbnailsPhoto() {
                return thumbnailsPhoto;
            }
        }
    }
}
