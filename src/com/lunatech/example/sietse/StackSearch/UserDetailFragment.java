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

    private Long userId;
    private UserHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong(USER_ID);
        helper = new UserHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.user_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final User user = helper.findUserByRowid(userId);
        if(user == null) {
            Log.e("UserDetailFragment", "User at rowid " + userId + " not found!");
        } else {
            ((WebView)getActivity().findViewById(R.id.about_value)).loadData(user.about, "text/html", "utf-8");
            ((TextView)getActivity().findViewById(R.id.displayName_value)).setText(user.displayName);
            ((TextView)getActivity().findViewById(R.id.reputation_value)).setText(user.reputation.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(USER_ID, userId);
    }
}
