package com.lunatech.example.sietse.StackSearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.lunatech.example.sietse.StackSearch.model.User;

import java.util.Set;

public class UserHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StackSearch";
    public static final String TABLE_NAME = "Users";

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String create = "CREATE TABLE " + TABLE_NAME + " (" +
                "site TEXT, " +
                "id INTEGER, " +
                "displayName TEXT, " +
                "about TEXT, " +
                "reputation INTEGER, " +
                "PRIMARY KEY (site, id));";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method implementation intentionally left blank
    }

    public void saveUsers(Set<User> users) {
        final SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (User user : users) {
                final ContentValues values = new ContentValues();
                values.put("site", user.site.name());
                values.put("id", user.id);
                values.put("displayName", user.displayName);
                values.put("about", user.about);
                values.put("reputation", user.reputation);
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();

        } catch (SQLException se) {
            Log.e("UserOpenHelper", "SQL Exception", se);
        } finally {
            db.endTransaction();
        }

    }

    public long countUsers() {
        final SQLiteDatabase db = getReadableDatabase();
        final SQLiteStatement stmt = db.compileStatement("SELECT COUNT(*) FROM " + TABLE_NAME + ";");
        return stmt.simpleQueryForLong();
    }

    public Cursor query(String selection, String[] selectionArgs, String[] columns) {
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_NAME);

        final Cursor cursor = builder.query(getReadableDatabase(), columns, selection, selectionArgs, null, null, "displayName");

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return cursor;
    }
}
