package edu.sjsu.cmpe275.lab1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/***
 * Created by Chenglong Wei on 3/5/16.
 * Student ID: 010396464
 * This is the implementation of advice:
 * (1) Retry 3 up to times for network error.
 * (2) Log statistics of tweet and follow information and store in the map.
 */

public class RetryAndDoStats implements MethodInterceptor {
    // retry times because network error.
    private static final int RETRY_TIMES = 3;
    // The longest attempted tweet length.
    private static int longestAttemptedTweetLength;
    // The user and followees map.
    private static Map<String, Set<String>> userFollowee;
    // The user and Tweeter length map.
    private static Map<String, Integer> userTweeterLength;

    // This static block will be called when the class is loaded.
    // We use this static block to initialize the static members.
    static  {
        longestAttemptedTweetLength = 0;
        userFollowee = new HashMap<String, Set<String>>();
        userTweeterLength = new HashMap<String, Integer>();
    }

    /***
     * Following is the implementation of advice.
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Get arguments of tweet() and follow().
        String arg1 = (String) invocation.getArguments()[0];
        String arg2 = (String) invocation.getArguments()[1];

        // Current retry times.
        int numRetry = 0;
        // The exception of the invocation throws.
        IOException exception;
        do {
            try {
                // Proceed the method.
                Object retVal = invocation.proceed();

                // Here the follow/tweet success, do the log.
                if (isFollowMethod(invocation)) {
                    logFollow(arg1, arg2);
                } else if (isTweetMethod(invocation)) {
                    logTweet(arg1, arg2);
                }

                return retVal;

            } catch (IOException e) {
                // Catch network error, retry three times.
                exception = e;
                numRetry++;
            } catch (IllegalArgumentException e) {
                if (isTweetMethod(invocation)) {
                    logFailedTweet(arg1, arg2);
                }
                throw e;
            }
        } while (numRetry <= RETRY_TIMES);

        if (numRetry > RETRY_TIMES) {
            if (isTweetMethod(invocation)) {
                logFailedTweet(arg1, arg2);
            }
            throw exception;
        }

        return null;
    }

    private boolean isTweetMethod(MethodInvocation invocation) {
        return invocation.getMethod().getName().equals("tweet");
    }

    private boolean isFollowMethod(MethodInvocation invocation) {
        return invocation.getMethod().getName().equals("follow");
    }

    // Reset statistics.
    public static void resetStats() {
        longestAttemptedTweetLength = 0;
        userFollowee.clear();
        userTweeterLength.clear();
    }

    // Return the statics information of longest attempted tweet.
    public static int getLongestAttemptedTweetLength() {
        return longestAttemptedTweetLength;
    }

    // Return the statics information of follow.
    public static Map<String, Set<String>> getUserFollowee() {
        return userFollowee;
    }

    // Return the statics information of successful tweets length.
    public static Map<String, Integer> getUserTweeterLength() {
        return userTweeterLength;
    }

    // Log successful tweets.
    private void logTweet(String user, String message) {
        if (!userTweeterLength.containsKey(user)) {
            userTweeterLength.put(user, 0);
        }

        userTweeterLength.put(user, userTweeterLength.get(user) + message.length());
        updateLongestAttemptedTweetLength(message);
    }

    // Log failed tweets, we need to update longest attempted tweet.
    private void logFailedTweet(String user, String message) {
        updateLongestAttemptedTweetLength(message);
    }

    // Log successful follows.
    private void logFollow(String follower, String followee) {
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
