package com.lunatech.example.sietse.StackSearch;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
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
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class UserListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {

    static final String[] PROJECTION = new String[]{"rowid AS _id", "id", "displayName", String.format("offsets(%s) as offsets", UserHelper.TABLE_NAME)};

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

      final String[] fromColumns = {"displayName", "score"};
      final int[] toViews = {android.R.id.text1, android.R.id.text2};

      mAdapter = new ScoringAdapter(getActivity(),
            android.R.layout.simple_list_item_2, null, fromColumns, toViews, 0);
//      mAdapter = new SimpleCursorAdapter(getActivity(),
//            android.R.layout.simple_list_item_2, null, fromColumns, toViews, 0);
//      this.mAdapter = new ScoringAdapter(getActivity());

      setListAdapter(mAdapter);

      getLoaderManager().initLoader(0, null, this);
   }

   @Override
    public void onStart() {
        super.onStart();
      getListView().setFastScrollEnabled(true);
      getListView().setFastScrollAlwaysVisible(true);
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

   private class ScoringAdapter extends SimpleCursorAdapter {
      private Map<Integer, Integer> positionMap;

      public ScoringAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
         super(context, layout, c, from, to, flags);

         initMap(c);
      }

      @Override
      public Cursor swapCursor(Cursor c) {
         initMap(c);
         return super.swapCursor(c);
      }

      private void initMap(Cursor cursor) {
         if (cursor == null || !cursor.moveToFirst()) return;

         if (cursor.getInt(4) == 0) {
            this.positionMap = null;
            return;
         }

         final Set<MapItem> set = new TreeSet<MapItem>();
         do {
            set.add(new MapItem(cursor.getInt(4), cursor.getPosition()));
         } while (cursor.moveToNext());

         this.positionMap = new HashMap<Integer, Integer>();

         int newPosition = 0;
         for (MapItem mapItem : set) {
            this.positionMap.put(newPosition, mapItem.originalPosition);
            newPosition++;
         }

         cursor.moveToFirst();
      }

      private class MapItem implements Comparable<MapItem> {
         final public Integer score;
         final public Integer originalPosition;

         private MapItem(Integer score, Integer originalPosition) {
            this.score = score;
            this.originalPosition = originalPosition;
         }

         @Override
         public int compareTo(MapItem another) {
            final int result = this.score.compareTo(another.score);

            if (result != 0) return result * -1;

            return this.originalPosition.compareTo(another.originalPosition);
         }
      }

      private int getNewPosition(int position) {
         if (this.positionMap == null) return position;

         final Integer newPosition = this.positionMap.get(position);
         if (newPosition == null) return position;

         return newPosition;
      }
      @Override
      public Object getItem(int position) {
         return super.getItem(getNewPosition(position));
      }

      @Override
      public long getItemId(int position) {
         return super.getItemId(getNewPosition(position));
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         return super.getView(getNewPosition(position), convertView, parent);
      }
   }
}
