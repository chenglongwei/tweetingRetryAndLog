package edu.sjsu.cmpe275.lab1;

import java.util.Map;
import java.util.Set;

public class TweetStatsImpl implements TweetStats {
    // The user and followee number map.
    private Map<String, Set<String>> userFollowee;
    // The user and Tweeter length map.
    private Map<String, Integer> userTweeterLength;

    /***
     * Following is the dummy implementation of methods.
     * Students are expected to complete the actual implementation of these methods as part of lab completion.
     */

    public TweetStatsImpl() {
        userFollowee = Statics.getInstance().getUserFollowee();
        userTweeterLength = Statics.getInstance().getUserTweeterLength();
    }

    @Override
    public void resetStats() {
        Statics.getInstance().resetStats();
    }

    @Override
    public int getLengthOfLongestTweetAttempted() {
        return Statics.getInstance().getLongestAttemptedTweetLength();
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



