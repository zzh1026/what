package com.neishenme.what.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neishenme.what.bean.HomeNewsInfoBean;
import com.neishenme.what.db.HomeNewsOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/15 13:59
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class HomeNewsDao {

    private static HomeNewsDao homeNewsDao;
    private final HomeNewsOpenHelper mHelper;

    private HomeNewsDao(Context context) {
        mHelper = new HomeNewsOpenHelper(context);
    }

    /**
     * 获取HomeNewsDao单例
     */
    public synchronized static HomeNewsDao getInstance(Context context) {
        if (homeNewsDao == null) {
            homeNewsDao = new HomeNewsDao(context);
        }
        return homeNewsDao;
    }

    public boolean add(String id, String context, String inviteId,
                       String joinerId, String userId, String endTime, String type, String link) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("context", context);
        values.put("inviteid", inviteId);
        values.put("joinerid", joinerId);
        values.put("userid", userId);
        values.put("endtime", endTime);
        values.put("type", type);
        values.put("link", link);
        long insert = db.insert("homenews", null, values);
        db.close();
        return insert != -1;
    }

    public boolean delete(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete("homenews", "id=?", new String[]{id});
        db.close();
        return delete > 0;
    }

    public List<HomeNewsInfoBean> findAllHN() {
        List<HomeNewsInfoBean> infos = new ArrayList<HomeNewsInfoBean>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("homenews", new String[]{"id", "context", "inviteid", "joinerid", "userid",
                "endtime", "type", "link"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            HomeNewsInfoBean info = new HomeNewsInfoBean();
            info.setId(cursor.getString(0));
            info.setContext(cursor.getString(1));
            info.setInviteid(cursor.getString(2));
            info.setJoinerid(cursor.getString(3));
            info.setUserid(cursor.getString(4));
            info.setEndtime(cursor.getString(5));
            info.setType(cursor.getString(6));
            info.setLink(cursor.getString(7));
            infos.add(info);
        }
        db.close();
        Collections.reverse(infos);
        return infos;
    }

    public boolean hasInfo() {
//        List<HomeNewsInfoBean> infos = new ArrayList<HomeNewsInfoBean>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("homenews", new String[]{"id"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
//        while (cursor.moveToNext()) {
//            HomeNewsInfoBean info = new HomeNewsInfoBean();
//            info.setId(cursor.getString(0));
//            ALog.i("info id的值为" + info.getId());
//            infos.add(info);
//        }
//        db.close();
//        return infos.size() != 0;
    }
}
