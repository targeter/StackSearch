package com.lunatech.example.sietse.StackSearch;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] fromColumns = {"displayName"};
        final int[] toViews = {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), UserProvider.CONTENT_URI, PROJECTION, "", new String[]{}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e("LVL", String.format("Click: %s, %s", position, id));
        ((UserActivity)getActivity()).onUserSelected(id);
    }
}
