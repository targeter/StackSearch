package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class UserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_container);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.user_container, new UserListFragment()).commit();
        }
    }

    public void onUserSelected(long rowid) {
        final Bundle bundle = new Bundle();
        bundle.putLong(UserDetailFragment.USER_ID, rowid);

        final UserDetailFragment detailFragment = new UserDetailFragment();
        detailFragment.setArguments(bundle);

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.user_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
