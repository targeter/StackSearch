package com.lunatech.example.sietse.StackSearch;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import java.util.Arrays;

public class UserProvider extends ContentProvider {

    private UserHelper helper;

    public static String AUTHORITY = "com.lunatech.example.StackSearch.UserProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");

    public static final String USERS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.lunatech.example.stackSearch";

    private static final int SEARCH_USERS = 0;
    private static final int GET_USER = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final int REFRESH_SHORTCUT = 3;
    private static final UriMatcher sURIMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "users", SEARCH_USERS);
        matcher.addURI(AUTHORITY, "users/#", GET_USER);

        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", REFRESH_SHORTCUT);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        helper = new UserHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_USERS:
                if (selectionArgs == null) {
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the Uri: " + uri);
                }
                return searchUsers(selectionArgs, projection);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_USERS:
                return USERS_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Can't insert Users");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Can't delete Users");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Can't update Users");
    }

    private Cursor searchUsers(String[] args, String[] columns) {
        final String selection;
        final String[] selectionArgs;
        if(args.length > 0 && args[0] != null && args[0].length() > 0) {
           selection = UserHelper.TABLE_NAME + " MATCH ?";
           selectionArgs = new String[] { String.format("about:%s OR displayName:%1$s", args[0]) };
        } else {
           selection = null;
           selectionArgs = new String[] {};
        }

       final Cursor cursor = helper.query(selection, selectionArgs, columns);
       return new ScoringCursor(cursor);
    }

   private class ScoringCursor extends CursorWrapper {
      private String[] columnNamesCache;

      /**
       * Creates a cursor wrapper.
       *
       * @param cursor The underlying cursor to wrap.
       */
      public ScoringCursor(Cursor cursor) {
         super(cursor);
      }

      @Override
      public int getColumnCount() {
         return super.getColumnCount() + 1;
      }

      @Override
      public String[] getColumnNames() {
         if (this.columnNamesCache == null) {
            this.columnNamesCache = Arrays.copyOf(super.getColumnNames(), getColumnCount());
            this.columnNamesCache[getColumnCount() - 1] = "score";
         }

         return this.columnNamesCache;
      }

      @Override
      public int getColumnIndex(String columnName) {
         if ("score".equals(columnName))
            return getColumnCount() - 1;

         return super.getColumnIndex(columnName);
      }

      @Override
      public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
         final int columnIndex = getColumnIndex(columnName);

         if (columnIndex == -1)
            throw new IllegalArgumentException(String.format("column '%s' does not exist", columnName));

         return columnIndex;
      }

      @Override
      public String getColumnName(int columnIndex) {
         if (columnIndex == getColumnCount() - 1)
            return "score";

         return super.getColumnName(columnIndex);
      }

      @Override
      public String getString(int columnIndex) {
         if (columnIndex == getColumnCount() - 1) {
            final int offsetsIndex = getColumnIndex("offsets");

            final String offsets = super.getString(offsetsIndex);

            if ("".equals(offsets)) return "0";

            final String[] split = offsets.split("\\s+");
            final int[] count = new int[UserHelper.MAX_COL];

            for (int i = 0; i < split.length; i += 4)
               count[Integer.parseInt(split[i])]++;

            return String.format("%d (%s)", count[UserHelper.Column.DISPLAY_NAME.index] * 100 + count[UserHelper.Column.ABOUT.index] * 20, offsets);
         }

         return String.format("%s, %s, %d", super.getString(columnIndex), super.getString(0), getPosition());
      }


      @Override
      public int getInt(int columnIndex) {
         if (columnIndex == getColumnCount() - 1) {
            final int offsetsIndex = getColumnIndex("offsets");

            final String offsets = super.getString(offsetsIndex);

            if ("".equals(offsets)) return 0;

            final String[] split = offsets.split("\\s+");
            final int[] count = new int[UserHelper.MAX_COL];

            for (int i = 0; i < split.length; i += 4)
               count[Integer.parseInt(split[i])]++;

            return count[UserHelper.Column.DISPLAY_NAME.index] * 100 + count[UserHelper.Column.ABOUT.index] * 20;
         }

         return super.getInt(columnIndex);
      }

      @Override
      public boolean isNull(int columnIndex) {
         if (columnIndex == getColumnCount() - 1)
            return false;

         return super.isNull(columnIndex);
      }
   }
}
