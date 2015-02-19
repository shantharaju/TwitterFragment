package com.codepath.apps.SJTweetsApp;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.SJTweetsApp.fragments.UserTimeLineFragment;
import com.codepath.apps.SJTweetsApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ProfileActivity extends ActionBarActivity {
    private TwitterClient client;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();

        setContentView(R.layout.activity_profile);

        verifyCredentials();

        // get the screen name
        String screenName = getIntent().getStringExtra("screen_name");

        // create the user timeline fragment
        UserTimeLineFragment userFragment = UserTimeLineFragment.newInstance(screenName);

        // Display user fragment within this activity dynamically
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.flContainer, userFragment)
                                   .commit();
    }

    private void verifyCredentials() {

        client.verifyCredentials( new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loggedInUser = User.fromJSON(response);
                // Toast.makeText(getBaseContext(), "User: " + loggedInUser.getName(), Toast.LENGTH_LONG).show();
                getSupportActionBar().setTitle("@" + loggedInUser.getScreenName());

                populateProfileHeader(loggedInUser);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Failed to get Logged in User info", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void populateProfileHeader(User loggedInUser) {
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        TextView tvName = (TextView)  findViewById(R.id.tvName);
        TextView tvTagLine = (TextView)  findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView)  findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)  findViewById(R.id.tvFollowing);

        tvName.setText(loggedInUser.getName());
        tvTagLine.setText(loggedInUser.getTagLine());
        tvFollowers.setText(loggedInUser.getFollowers() + " Followers");
        tvFollowers.setText(loggedInUser.getFollowing() + " Following");

        ivProfile.setImageResource(0);
        Picasso.with(this).load(loggedInUser.getProfileImageUrl()).into(ivProfile);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
