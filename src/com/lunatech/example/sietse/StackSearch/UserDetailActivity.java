package com.lunatech.example.sietse.StackSearch;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class UserDetailActivity extends FragmentActivity {
   public static final String USER_ID = "USER_ID";

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.user_detail);
//      getActionBar().setDisplayHomeAsUpEnabled(true);
   }

   @Override
   protected void onStart() {
      super.onStart();

      final UserDetailFragment detailFragment = (UserDetailFragment)getSupportFragmentManager().findFragmentById(R.id.user_detail_container);
      detailFragment.switchUser(getIntent().getLongExtra(USER_ID, 0));
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            finish();
            return true;
      }

      return super.onOptionsItemSelected(item);
   }
}
