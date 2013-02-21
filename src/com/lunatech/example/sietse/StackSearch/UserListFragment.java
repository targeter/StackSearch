package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    SimpleCursorAdapter mAdapter;
    CursorLoader cursorLoader = null;

   static final String FILTER = "FILTER";
   public String currentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("UserListFragment", "OnCreate");

       if (savedInstanceState != null)
          this.currentFilter = savedInstanceState.getString(FILTER);
    }


   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      Log.wtf("UserListFragment", "OnActivityCreated");

      setHasOptionsMenu(true);

      final String[] fromColumns = {"displayName"};
      final int[] toViews = {android.R.id.text1};

      mAdapter = new SimpleCursorAdapter(getActivity(),
            android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);

      setListAdapter(mAdapter);

      getLoaderManager().initLoader(0, null, this);
   }

//   @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.wtf("UserListFragment", "OnCreateView");
//      return inflater.inflate(R.layout.user_list, container, false);
//    }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      Log.wtf("UserListFragment", "onCreateOptionsMenu");
      final MenuItem item = menu.add("Search");
      item.setIcon(android.R.drawable.ic_menu_search);
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      SearchView sv = new SearchView(getActivity());
      sv.setOnQueryTextListener(this);
      sv.setQuery(this.currentFilter, true);
      item.setActionView(sv);
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
       Log.wtf("UserListFragment", "onDestroy");
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

       outState.putString(FILTER, this.currentFilter);
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
   public boolean onQueryTextSubmit(String query) {
      final InputMethodManager systemService = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      systemService.hideSoftInputFromWindow(getView().getWindowToken(), 0);
      return true;
   }

   @Override
   public boolean onQueryTextChange(String newText) {
      if (this.currentFilter == null && (newText == null || newText.length() == 0))
         return true;

      if (this.currentFilter != null && this.currentFilter.equals(newText))
         return true;

      Log.wtf("UserListFragment", "Actually filtering");
      this.currentFilter = newText;
      getLoaderManager().restartLoader(0, null, this);

      return true;
   }
}
