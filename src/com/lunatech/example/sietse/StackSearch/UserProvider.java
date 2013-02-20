package com.lunatech.example.sietse.StackSearch;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

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
        final String selection = "displayName LIKE ?";
        final String[] selectionArgs;
        if(args.length > 0) {
            selectionArgs = new String[]{args[0] + "%"};
        } else {
            selectionArgs = new String[]{"%"};
        }

        return helper.query(selection, selectionArgs, columns);
    }

}
