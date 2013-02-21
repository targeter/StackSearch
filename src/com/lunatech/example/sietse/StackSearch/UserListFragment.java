package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    SimpleCursorAdapter mAdapter;
    CursorLoader cursorLoader = null;

   public String currentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("UserListFragment", "OnCreate");
    }


   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      Log.wtf("UserListFragment", "OnActivityCreated");

      final String[] fromColumns = {"displayName"};
      final int[] toViews = {android.R.id.text1};

      mAdapter = new SimpleCursorAdapter(getActivity(),
            android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);

      setListAdapter(mAdapter);

      getLoaderManager().initLoader(0, null, this);
   }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.wtf("UserListFragment", "OnCreateView");
        final View view = inflater.inflate(R.layout.user_list, container, false);
        ((EditText)view.findViewById(R.id.filter_box)).addTextChangedListener(this);
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

       final View view = getView();

       if (view != null)
          ((EditText)view.findViewById(R.id.filter_box)).removeTextChangedListener(this);
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
        Log.wtf("UserListFragment", "OnAttach");
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
        Log.wtf("UserListFragment", "onCreateLoader");
       this.cursorLoader = new CursorLoader(getActivity(), UserProvider.CONTENT_URI, PROJECTION, "", new String[]{this.currentFilter}, null);
       return this.cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.wtf("UserListFragment", "onLoaderReset");
        mAdapter.swapCursor(null);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e("UserListFragment", String.format("Click: %s, %s", position, id));
        ((UserActivity)getActivity()).onUserSelected(id);
    }

   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
   }

   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) {
      Log.wtf("UserListFragment", String.format("filtering on: [%s]", s.toString()));

      if (this.currentFilter == null && s == null)
         return;

      if (this.currentFilter != null && this.currentFilter.equals(s.toString()))
         return;

      this.currentFilter = s.toString();
      getLoaderManager().restartLoader(0, null, this);
   }

   @Override
   public void afterTextChanged(Editable s) {
   }

}
