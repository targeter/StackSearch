package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, FilterQueryProvider {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("UserListFragment", "OnCreate");

        if(savedInstanceState == null) {
            final String[] fromColumns = {"displayName"};
            final int[] toViews = {android.R.id.text1};

            mAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);

            setListAdapter(mAdapter);
            mAdapter.setFilterQueryProvider(this);


            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.wtf("UserListFragment", "OnCreateView");
        final View view = inflater.inflate(R.layout.user_list, container, false);
        ((EditText)view.findViewById(R.id.filter_box)).addTextChangedListener(filterTextWatcher);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.wtf("UserListFragment", "OnStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.wtf("UserListFragment", "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.wtf("UserListFragment", "OnPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.wtf("UserListFragment", "OnStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((EditText)getView().findViewById(R.id.filter_box)).removeTextChangedListener(filterTextWatcher);
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
        Log.wtf("UserListFragment", "OnAttach");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.wtf("UserListFragment", "OnActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.wtf("UserListFragment", "OnDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.wtf("UserListFragment", "OnDetach");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.wtf("UserListFragment", "onSaveInstanceState");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.wtf("ULF", "onCreateLoader");
        return new CursorLoader(getActivity(), UserProvider.CONTENT_URI, PROJECTION, "", new String[]{}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.wtf("ULF", "onLoadFinished " + data.hashCode());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.wtf("ULF", "onLoaderReset");
        mAdapter.swapCursor(null);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e("LVL", String.format("Click: %s, %s", position, id));
        ((UserActivity)getActivity()).onUserSelected(id);
    }

    @Override
    public Cursor runQuery(CharSequence constraint) {
        return getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI,
                PROJECTION,
                "",
                new String[]{constraint.toString()},
                "displayName");
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
