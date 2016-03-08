package edu.sjsu.cmpe275.lab1;

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
    // The user and followees map.
    private Map<String, Set<String>> userFollowee;
    // The user and Tweeter length map.
    private Map<String, Integer> userTweeterLength;

    // Get the statistics information from RetryAndDoStats.
    public TweetStatsImpl() {
        userFollowee = RetryAndDoStats.getUserFollowee();
        userTweeterLength = RetryAndDoStats.getUserTweeterLength();
    }

    @Override
    public void resetStats() {
        RetryAndDoStats.resetStats();
    }

    @Override
    public int getLengthOfLongestTweetAttempted() {
        return RetryAndDoStats.getLongestAttemptedTweetLength();
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
}



