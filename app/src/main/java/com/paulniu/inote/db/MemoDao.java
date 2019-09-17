package com.paulniu.inote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.niupuyue.mylibrary.utils.TimeUtility;

/**
 * Coder: niupuyue
 * Date: 2019/8/16
 * Time: 10:25
 * Desc: 备忘录数据库操作工具类
 * Version:
 */
public class MemoDao {

    private DBHelper helper;

    public MemoDao(Context mContext) {
        helper = new DBHelper(mContext);
    }

    /**
     * 查询某个文件夹中有多少个备忘录
     */
    public int getMemoCountByFolder(int folderId) {
        if (folderId < 0) return 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        String sql = "select count(*) from i_memo where folder_id =" + folderId + " order by memo_create_date desc";
        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }

    /**
     * 新建一个文件
     */
    public long insertMemo(String fileTitle, String fileContent, int folderId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into i_memo(memo_title,memo_content,folder_id,memo_create_date) values (?,?,?,?)";
        long ret = 0;
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {
            statement.bindString(1, fileTitle);
            statement.bindString(2, fileContent);
            statement.bindLong(3, folderId);
            statement.bindString(4, TimeUtility.convertToString(System.currentTimeMillis(), TimeUtility.TIME_FORMAT));
            ret = statement.executeInsert();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }

    /**
     * 删除一个文件
     */
    public int deleteMemo(int fileId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            ret = db.delete("i_memo", "memo_id=?", new String[]{String.valueOf(fileId)});
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != db) {
                db.close();
            }
        }
        return ret;
    }

}
