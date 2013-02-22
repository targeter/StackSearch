package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class UserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_container);

        getActionBar().setDisplayHomeAsUpEnabled(true);

       final FragmentManager fragmentManager = getFragmentManager();
       if (fragmentManager.findFragmentById(R.id.user_container) == null) {
          fragmentManager
                .beginTransaction()
                .add(R.id.user_container, new UserListFragment(), "user-list-fragment")
                .commit();
       }
    }

    public void onUserSelected(long rowid) {
        final Bundle bundle = new Bundle();
        bundle.putLong(UserDetailFragment.USER_ID, rowid);

        final UserDetailFragment detailFragment = new UserDetailFragment();
        detailFragment.setArguments(bundle);

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.user_container, detailFragment, "user-detail-fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
