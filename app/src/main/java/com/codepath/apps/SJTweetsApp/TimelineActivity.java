package com.codepath.apps.SJTweetsApp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.SJTweetsApp.fragments.HomeTimeLineFragment;
import com.codepath.apps.SJTweetsApp.fragments.MentionsTimeLineFragment;
import com.codepath.apps.SJTweetsApp.models.Tweet;
import com.codepath.apps.SJTweetsApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class TimelineActivity extends ActionBarActivity {

    private static final int UPDATE_STATUS = 1;
    private HomeTimeLineFragment homeFragment;
    private MentionsTimeLineFragment mentionsFragment;
    private User loggedInUser;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();


        // Get the viewpager
        // Set the viewpager adapter for the pager
        // find the sliding tabstrip
        // Attach the tabstrip to the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        verifyCredentials();
        homeFragment = new HomeTimeLineFragment();
        mentionsFragment = new MentionsTimeLineFragment();
    }

    private void verifyCredentials() {

        client.verifyCredentials( new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loggedInUser = User.fromJSON(response);
                // Toast.makeText(getBaseContext(), "User: " + loggedInUser.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Failed to get Logged in User info", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            // Toast.makeText(getBaseContext(), "Compose Clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("loggedInUser", loggedInUser);
            startActivityForResult(i, UPDATE_STATUS);
        }

        if(id == R.id.miProfile) {
            // Toast.makeText(getBaseContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();
            // onProfileClick
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UPDATE_STATUS) {
            if(RESULT_OK == resultCode) {
                Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
                homeFragment.addTweet(tweet);
            }
            else if(RESULT_CANCELED == resultCode) {
                // Do nothing here
            }
            else {
                Toast.makeText(getBaseContext(), "Unknown return code", Toast.LENGTH_SHORT).show();
                Log.e("COMPOSE", "Unknown return code: " + resultCode);
            }
        }
    }

    // return order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                // return new HomeTimeLineFragment();
                return homeFragment;
            }
            else if(position == 1) {
                // return new MentionsTimeLineFragment();
                return mentionsFragment;
            } else {
                return null;
            }
        }

        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
