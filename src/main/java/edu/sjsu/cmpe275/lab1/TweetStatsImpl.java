package edu.sjsu.cmpe275.lab1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/***
 * Created by Chenglong Wei on 3/5/16.
 * Student ID: 010396464
 * This is the implementation of Tweet statistics:
 * (1) resetStats
 * (2) getLengthOfLongestTweetAttempted
 * (3) getMostFollowedUser
 * (4) getMostProductiveUser
 */

public class TweetStatsImpl implements TweetStats {
    // The longest attempted tweet length.
    private int longestAttemptedTweetLength;
    // The user and followees map, value is a Set to remove duplicate follow.
    private Map<String, Set<String>> userFollowee;
    // The user and total Tweeter length map.
    private Map<String, Integer> userTweeterLength;

    public TweetStatsImpl() {
        longestAttemptedTweetLength = 0;
        userFollowee = new HashMap<String, Set<String>>();
        userTweeterLength = new HashMap<String, Integer>();
    }

    @Override
    public void resetStats() {
        longestAttemptedTweetLength = 0;
        userFollowee.clear();
        userTweeterLength.clear();
    }

    @Override
    public int getLengthOfLongestTweetAttempted() {
        return longestAttemptedTweetLength;
    }

    @Override
    public String getMostFollowedUser() {
        // If no follow operation, return null.
        if (userFollowee.size() == 0) {
            return null;
        }

        String mostFollowedUser = "";
        // Most tweet length number.
        int mostFollowedeNumber = 0;

        // If user's followee number bigger than mostFollowedeNumber or equal
        // but user name is alphabetical less, update mostFollowedUser.
        for (Map.Entry<String, Set<String>> entry : userFollowee.entrySet()) {
            if (entry.getValue().size() > mostFollowedeNumber) {
                mostFollowedUser = entry.getKey();
                mostFollowedeNumber = entry.getValue().size();
            } else if (entry.getValue().size() == mostFollowedeNumber &&
                    entry.getKey().compareTo(mostFollowedUser) < 0) {
                mostFollowedUser = entry.getKey();
            }
        }

        return mostFollowedUser;
    }

    @Override
    public String getMostProductiveUser() {
        // If no tweet operation, return null.
        if (userTweeterLength.size() == 0) {
            return null;
        }

        String mostProductiveUser = "";
        // Most tweet length number.
        int mostProductiveNumber = 0;

        // If user's tweet length number bigger than mostProductiveNumber or equal
        // but user name is alphabetical less, update mostProductiveUser.
        for (Map.Entry<String, Integer> entry : userTweeterLength.entrySet()) {
            if (entry.getValue() > mostProductiveNumber) {
                mostProductiveUser = entry.getKey();
                mostProductiveNumber = entry.getValue();
            } else if (entry.getValue() == mostProductiveNumber &&
                    entry.getKey().compareTo(mostProductiveUser) < 0) {
                mostProductiveUser = entry.getKey();
            }
        }
        return mostProductiveUser;
    }

    // Log successful tweets.
    public void logTweet(String user, String message) {
        if (!userTweeterLength.containsKey(user)) {
            userTweeterLength.put(user, 0);
        }

        userTweeterLength.put(user, userTweeterLength.get(user) + message.length());
        updateLongestAttemptedTweetLength(message);
    }

    // Log failed tweets, we need to update longest attempted tweet.
    public void logFailedTweet(String user, String message) {
        updateLongestAttemptedTweetLength(message);
    }

    // Log successful follows.
    public void logFollow(String follower, String followee) {
        if (!userFollowee.containsKey(followee)) {
            userFollowee.put(followee, new HashSet<String>());
        }

        userFollowee.get(followee).add(follower);
    }

    private void updateLongestAttemptedTweetLength(String message) {
        if (message.length() > longestAttemptedTweetLength) {
            longestAttemptedTweetLength = message.length();
        }
    }
}



