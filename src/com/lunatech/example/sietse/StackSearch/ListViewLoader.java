package com.lunatech.example.sietse.StackSearch;

import android.R;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class ListViewLoader extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] fromColumns = {"displayName"};
        final int[] toViews = {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.simple_list_item_1, null, fromColumns, toViews, 0);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UserProvider.CONTENT_URI, PROJECTION, "", new String[]{}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
