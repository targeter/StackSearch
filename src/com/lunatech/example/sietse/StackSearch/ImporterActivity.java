package com.lunatech.example.sietse.StackSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.lunatech.example.sietse.StackSearch.model.StackSite;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImporterActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importer);
    }

    public void doImport(View view) {
        ((Button) view).setText("Importing...");
//        new ImporterTask().execute(StackSite.SERVERFAULT, StackSite.SUPERUSER, StackSite.STACKOVERFLOW);
        new ImporterTask(this).execute(StackSite.SERVERFAULT);
    }

    public void listUsers(View view) {
        startActivity(new Intent(this, UserActivity.class));
    }

    public void countUsers(View view) {
        final UserHelper helper = new UserHelper(this);
        final long result = helper.countUsers();
        ((Button)view).setText("Users: " + result);

    }

    public void testCreateFunction(View view) {
       final UserHelper userHelper = new UserHelper(this);
       final int result = userHelper.testCreateFunction();
       ((Button)view).setText(String.format("Got result: %d (%s)", result, result == 0 ? "success" : "failure"));
    }
    private class ImporterTask extends AsyncTask<StackSite, Void, Integer> {
        private static final String TAG = "ImporterTask";
        private final Context ctx;


        private ImporterTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected Integer doInBackground(StackSite... sites) {
            int userCount = 0;
            final UserHelper userHelper = new UserHelper(ctx);
            for (StackSite site : sites) {
                final UserParser parser = new UserParser(site, userHelper);
                Log.d(TAG, "Start download and parse for " + site);
                try {
                    final InputStream stream = download(site.url);
                    final int siteUserCount = parser.parse(stream);
                    userCount += siteUserCount;
                } catch (IOException ioe) {
                    Log.e(TAG, "Connection error", ioe);
                } catch (XmlPullParserException xppe) {
                    Log.e(TAG, "Parser error", xppe);
                }
            }

            return userCount;
        }

        @Override
        protected void onPostExecute(Integer count) {
            // Displays the HTML string in the UI via a WebView
            final Button button = (Button) findViewById(R.id.button_import);
            button.setText(String.format("Parsed %s users", count));
        }

        private InputStream download(String urlString) throws IOException {
            final URL url = new URL(urlString);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getInputStream();
        }
    }
}
