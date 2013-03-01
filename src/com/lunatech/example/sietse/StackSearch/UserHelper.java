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
import com.almworks.sqlite4java.Nasty;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.lunatech.example.sietse.StackSearch.model.StackSite;
import com.lunatech.example.sietse.StackSearch.model.User;

import java.io.File;
import java.util.Set;

public class UserHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StackSearch";
    public static final String TABLE_NAME = "Users";

   public static final int MAX_COL = 4;

   private final Context context;

   public enum Column {
      SITE(0), ID(1), DISPLAY_NAME(2), ABOUT(3), REPUTATION(4);

      public final int index;

      private Column(final int index) {
         this.index = index;
      }
   }

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String create = "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING FTS3 (" +
                "site TEXT, " +
                "id INTEGER, " +
                "displayName TEXT, " +
                "about TEXT, " +
                "reputation INTEGER, " +
                "PRIMARY KEY (site, id));";
        db.execSQL(create);
    }

   @Override
   public SQLiteDatabase getReadableDatabase() {
      return super.getReadableDatabase();
   }

   @Override
   public SQLiteDatabase getWritableDatabase() {
      return super.getWritableDatabase();
   }

   /* load our native library */
    static {
       System.loadLibrary("sqlite4java-android-armv7");
       System.loadLibrary("VINDRank");
    }

    public int testCreateFunction() {

       final SQLiteConnection connection = new SQLiteConnection(new File(this.context.getFilesDir(), "foo.sqlite"));
       try {
          connection.open();
       } catch (SQLiteException e) {
          Log.e("UserHelper", "Failed to open database", e);
       }

       final VINDRank vindRank = new VINDRank();
       int result = vindRank.attachVINDRank(Nasty.getDbPointer(connection));
       if (result > 0) {
          Log.wtf("UserHelper", "Attaching VINDRank function fail with status: "+result);
       } else {
          Log.wtf("UserHelper", "Attaching VINDRank function succeeded!");
       }

       return result;
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

    public User findUserByRowid(Long rowid) {
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db
                .query(TABLE_NAME, new String[]{"site", "id", "displayName", "about", "reputation"}, "rowid = ?",
                        new String[]{rowid.toString()}, null, null, null);

        if (cursor == null) {
            return null;
        }

        try {
            if (!cursor.moveToFirst()) {
                return null;
            }
            StackSite site = StackSite.valueOf(cursor.getString(0));
            Long id = cursor.getLong(1);
            String displayName = cursor.getString(2);
            String about = cursor.getString(3);
            Integer reputation = cursor.getInt(4);

            return new User(site, id, displayName, about, reputation);
        } finally {
            cursor.close();
            db.close();
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

        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = builder.query(db, columns, selection, selectionArgs, null, null, null);

       Log.wtf("UserListFragment", "before");
        if (cursor == null) {
            return null;
        }

        Log.wtf("UserListFragment", "after");

        return cursor;
    }

}
