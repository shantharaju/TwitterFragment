package com.codepath.apps.SJTweetsApp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sjayanna on 2/8/15.
 */
@Table(name = "Users")
public class User extends Model implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "uid")
    private long uid;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "tagLine")
    private String tagLine;

    @Column(name = "followers")
    private long followers;

    @Column(name = "following")
    private long following;


    public User() {
        super();
    }

    public User(String name, long uid, String screenName, String profileImageUrl) {
        super();
        this.name = name;
        this.uid = uid;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagLine() {return tagLine;}

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagLine = jsonObject.getString("description");
            user.followers = jsonObject.getLong("followers_count");
            user.following = jsonObject.getLong("friends_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // user.save();
        return user;
    }


}
