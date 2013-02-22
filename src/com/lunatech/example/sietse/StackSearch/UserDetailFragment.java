package com.lunatech.example.sietse.StackSearch;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import com.lunatech.example.sietse.StackSearch.model.User;

public class UserDetailFragment extends Fragment {

    public static final String USER_ID = "rowid";

    private long userId;
    private UserHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
           this.userId = savedInstanceState.getLong(USER_ID);

        if (this.userId == 0)
           this.userId = getActivity().getIntent().getLongExtra(USER_ID, 0);

        helper = new UserHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.user_detail_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

       switchUser(this.userId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(USER_ID, userId);
    }

   public void switchUser(long id) {
      this.userId = id;

      if (id == 0) {
         getActivity().findViewById(R.id.user_detail_view).setVisibility(View.GONE);
         getActivity().findViewById(R.id.user_detail_layout_empty).setVisibility(View.VISIBLE);
         getActivity().findViewById(R.id.user_detail_layout_notfound).setVisibility(View.GONE);
      } else {
         getActivity().findViewById(R.id.user_detail_layout_empty).setVisibility(View.GONE);

         final User user = helper.findUserByRowid(userId);
         if(user == null) {
            Log.e("UserDetailFragment", "User at rowid " + userId + " not found!");
            getActivity().findViewById(R.id.user_detail_view).setVisibility(View.GONE);
            getActivity().findViewById(R.id.user_detail_layout_notfound).setVisibility(View.VISIBLE);
         } else {
            ((WebView)getActivity().findViewById(R.id.about_value)).loadData(user.about, "text/html", "utf-8");
            ((TextView)getActivity().findViewById(R.id.displayName_value)).setText(user.displayName);
            ((TextView)getActivity().findViewById(R.id.reputation_value)).setText(user.reputation.toString());

            getActivity().findViewById(R.id.user_detail_view).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.user_detail_layout_notfound).setVisibility(View.GONE);
         }
      }
   }

   public void switchUserIfNone(long itemId) {
      if (this.userId == 0)
         switchUser(itemId);
   }
}
