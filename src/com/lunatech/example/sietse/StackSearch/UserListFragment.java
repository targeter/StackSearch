package com.lunatech.example.sietse.StackSearch;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName"};

    CursorAdapter mAdapter;
    CursorLoader cursorLoader = null;

   static final String FILTER = "FILTER";
   public String currentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("UserListFragment", "OnCreate");

       if (savedInstanceState != null)
          this.currentFilter = savedInstanceState.getString(FILTER);

       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      final View view = inflater.inflate(R.layout.user_list_fragment, container, false);
      final EditText filterBox = (EditText)view.findViewById(R.id.filter_box);
      filterBox.addTextChangedListener(this);
      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      Log.wtf("UserListFragment", "OnActivityCreated");

      final String[] fromColumns = {"displayName", "id"};
      final int[] toViews = {android.R.id.text1, android.R.id.text2};

//      mAdapter = new ScoringAdapter(getActivity(),
//            android.R.layout.simple_list_item_2, null, fromColumns, toViews, 0);
//      this.mAdapter = new ScoringAdapter(getActivity());
      mAdapter = new SimpleCursorAdapter(getActivity(),
            android.R.layout.simple_list_item_2, null, fromColumns, toViews, 0);

      setListAdapter(mAdapter);

      getLoaderManager().initLoader(0, null, this);
   }

   @Override
    public void onStart() {
        super.onStart();
//      getListView().setFastScrollEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       Log.wtf("UserListFragment", "onDestroy");

       final View view = getView();
       if (view != null)
          ((EditText)view.findViewById(R.id.filter_box)).removeTextChangedListener(this);
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
       getActivity().findViewById(R.id.user_list_empty).setVisibility(View.VISIBLE);
       getActivity().findViewById(R.id.user_list_progressBar).setVisibility(View.GONE);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.wtf("UserListFragment", "onLoaderReset");
       getActivity().findViewById(R.id.user_list_empty).setVisibility(View.GONE);
       getActivity().findViewById(R.id.user_list_progressBar).setVisibility(View.VISIBLE);
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e("UserListFragment", String.format("Click: %s, %s", position, id));
        ((Callbacks)getActivity()).onUserSelected(id);
    }

   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
   }

   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (this.currentFilter == null && s == null)
         return;

      if (this.currentFilter != null && this.currentFilter.equals(s.toString()))
         return;

      this.currentFilter = s.toString();

      if (this.cursorLoader != null) {
         Log.wtf("ULF", "We have a loader");
         this.cursorLoader.setSelectionArgs(new String[]{s.toString()});

         if (this.cursorLoader.isStarted()) {
            Log.wtf("ULF", "The loader is started");
            this.cursorLoader.forceLoad();
         }
      }
   }

   @Override
   public void afterTextChanged(Editable s) {
   }

   public interface Callbacks {
      void onUserSelected(long id);
   }

}
