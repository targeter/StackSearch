package com.lunatech.example.sietse.StackSearch;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class ScoringAdapter extends SimpleCursorAdapter {
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
