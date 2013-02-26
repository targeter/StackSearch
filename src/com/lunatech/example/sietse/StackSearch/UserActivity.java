package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UserActivity extends Activity implements UserListFragment.Callbacks {

    private UserPagerAdapter userPagerAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        userPagerAdapter = new UserPagerAdapter(getFragmentManager());
        pager = (ViewPager) findViewById(R.id.user_detail_pager);
        if(pager != null)
            pager.setAdapter(userPagerAdapter);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onUserSelected(int position, long rowid) {
        Log.wtf("UserActivity", String.format("onUserSelected(%s, %s)", position, rowid));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.user_detail_pager);

        if (viewPager != null)
           viewPager.setCurrentItem(position);
       else {
          final Intent intent = new Intent(this, UserDetailActivity.class);
          intent.putExtra(UserDetailActivity.USER_ID, rowid);
          startActivity(intent);
       }
    }

    @Override
    public void onUserListChanged() {
        Log.wtf("UserActivity","onUserListChanged");
        userPagerAdapter.notifyDataSetChanged();
    }

    private UserDetailFragment getDetailFragment() {
      return (UserDetailFragment) getFragmentManager().findFragmentById(R.id.user_detail_container);
   }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);

      Log.wtf("UserActivity", "onSaveInstanceState");
   }



   @Override
   protected void onDestroy() {
      super.onDestroy();

      Log.wtf("UserActivity", "onDestroy");
   }

    public class UserPagerAdapter extends FragmentStatePagerAdapter {

        public UserPagerAdapter(android.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int i) {
            Log.wtf("UserPagerAdapter", "getItem: " + i);
            final long itemId = getListAdapter().getItemId(i);
            Log.wtf("UserPagerAdapter", "itemId: " + itemId);
            final UserDetailFragment userDetailFragment = new UserDetailFragment(itemId);
            return userDetailFragment;
//            Fragment fragment = new DemoObjectFragment();
//            Bundle args = new Bundle();
//            args.putInt(DemoObjectFragment.ARG_OBJECT, i);
//            fragment.setArguments(args);
//            return fragment;
        }

        @Override
        public int getCount() {
            return getListAdapter().getCount();
        }

        private ListAdapter getListAdapter() {
            final UserListFragment userListFragment = (UserListFragment) getFragmentManager()
                    .findFragmentById(R.id.user_list_container);
            return userListFragment.getListAdapter();
        }
    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    android.R.layout.simple_list_item_1, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }

}
