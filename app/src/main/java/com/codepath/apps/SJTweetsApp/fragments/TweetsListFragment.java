package com.codepath.apps.SJTweetsApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.SJTweetsApp.R;
import com.codepath.apps.SJTweetsApp.TweetsArrayAdapter;
import com.codepath.apps.SJTweetsApp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjayanna on 2/17/15.
 */
public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        // getViews(v);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        // hook up adapter to ListView
        lvTweets.setAdapter(aTweets);
        return v;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clear() {
        aTweets.clear();
    }

    // creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create datasource
        tweets = new ArrayList<Tweet>();

        // create Arrayadapter
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

    }

    public ListView getListViewTweets() {
        return lvTweets;
    }

    public void addTweet(Tweet tweet) {
        aTweets.insert(tweet, 0);
    }
}
