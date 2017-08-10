package com.neishenme.what.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：zhaozh create on 2016/6/15 13:45
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class HomeNewsOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE_NAME_OLD = "HomeNews.db";
//    public static final String CREATE_TABLE_NAME = "HomeNewsVThree.db";
    public static final String CREATE_TABLE_NAME = "HomeNewsVFour.db";
    public static final String CREATE_HOMENEWS =
            "create table homenews (_id integer primary key autoincrement,id varchar(20)," +
                    "context varchar(100),inviteid varchar(10),joinerid varchar(10),userid varchar(10)," +
                    "endtime varchar(20),type varchar(20),link varchar(100))";

    public HomeNewsOpenHelper(Context context) {
        super(context, CREATE_TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOMENEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        ALog.e("运行到了升级方法 旧的version是 " + oldVersion + " 新的是" + newVersion);
//        if (oldVersion == 1) {
//            App.getApplication().deleteDatabase(CREATE_TABLE_NAME);
//            db.execSQL(CREATE_HOMENEWS);
//        }
    }
}
