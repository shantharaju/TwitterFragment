package com.codepath.apps.SJTweetsApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.SJTweetsApp.R;
import com.codepath.apps.SJTweetsApp.TwitterApp;
import com.codepath.apps.SJTweetsApp.TwitterClient;
import com.codepath.apps.SJTweetsApp.interfaces.EndlessScrollListener;
import com.codepath.apps.SJTweetsApp.models.Tweet;
import com.codepath.apps.SJTweetsApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sjayanna on 2/17/15.
 */
public class UserTimeLineFragment extends TweetsListFragment {
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private User loggedInUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();

        // Get info about logged in user
        // verifyCredentials();
        populateTimeline();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);
        getViews(v);

        return v;
    }

    private void getViews(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // your code to refresh the list here
                //  Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(1);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // Code to enable Infinite scrolling
        super.getListViewTweets().setOnScrollListener(new EndlessScrollListener() {
            @Override
            protected void onLoadMore(int page, int totalItemCount) {
                customLoadMoreDataFromAPI(page);
            }
        });
    }

    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment userFragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
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
                Toast.makeText(getActivity(), "Failed to get Logged in User info", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void fetchTimelineAsync(int page) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                clear();

                // Clear db
                // TODO: Delete only tweets from given user
                // new Delete().from(Tweet.class).execute();
                // new Delete().from(User.class).execute();

                // This would have created db again
                addAll(Tweet.fromJSONArray(response));

                swipeContainer.setRefreshing(false);
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void customLoadMoreDataFromAPI(int page) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                addAll(Tweet.fromJSONArray(response));
                // Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {
        String screenName = getArguments().getString("screen_name");
        // Load from the very beginning
        client.getUserTimeline(1, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                addAll(Tweet.fromJSONArray(response));
                // Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
