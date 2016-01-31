package com.hoya.vt.timecheck;

import android.app.ListActivity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import twitter4j.TwitterException;

public class DriverActivity extends ListActivity {

    Toolbar toolbar;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "75YiknXrR89hvbnIsqK3HUipp";
    private static final String TWITTER_SECRET = "lmUpO2m5m2Rd08klkPWPey4TDQAV9fKpwz7l13yfXrTIvoftcA";

    private static final String SEARCH_QUERY = "#PASSENGERALERT411";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu_settings);
        toolbar.setTitle("Passenger Wait Requests");
        toolbar.setLogo(R.drawable.driver_icon);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(SEARCH_QUERY)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();
        setListAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(com.twitter.sdk.android.core.TwitterException e) {

                    }
                });
            }
        });

        new CountDownTimer(100000000, 10000) {

            public void onTick(long millisUntilFinished) {
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(com.twitter.sdk.android.core.TwitterException e) {

                    }
                });
            }

            public void onFinish() {

            }
        }.start();
    }

    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settingsMenu) {
            launchSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
