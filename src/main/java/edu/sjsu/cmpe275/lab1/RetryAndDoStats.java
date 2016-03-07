package edu.sjsu.cmpe275.lab1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RetryAndDoStats implements MethodInterceptor {
    // retry times because network error.
    private static int count = 0;
    private static final int RETRY_TIMES = 3;

    /***
     * Following is the dummy implementation of advice.
     * Students are expected to complete the required implementation as part of lab completion.
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String arg1 = (String) invocation.getArguments()[0];
        String arg2 = (String) invocation.getArguments()[1];
        System.out.println("Method " + invocation.getMethod().getName() +
                "(" + arg1 + ", " + arg2 + ") is called");

        try {
            Object retVal = invocation.proceed();

            if (isFollowMethod(invocation)) {
                Statics.getInstance().logFollow(arg1, arg2);
            } else if (isTweetMethod(invocation)) {
                Statics.getInstance().logTweet(arg1, arg2);
            }

            return retVal;

        } catch (IOException ioe) {
            // retry 3 times, count is already retry times
            if (count < RETRY_TIMES) {
                count++;
                invoke(invocation);
            } else {
                // retry over 3 times
                count = 0;
                if (isTweetMethod(invocation)) {
                    Statics.getInstance().logFailedTweet(arg1, arg2);
                }
                throw ioe;
            }
        } catch (IllegalArgumentException e) {
            if (isTweetMethod(invocation)) {
                Statics.getInstance().logFailedTweet(arg1, arg2);
            }
            throw e;
        }

        return null;
    }

    private boolean isTweetMethod(MethodInvocation invocation) {
        return invocation.getMethod().getName().equals("tweet");
    }

    private boolean isFollowMethod(MethodInvocation invocation) {
        return invocation.getMethod().getName().equals("follow");
    }
}

class Statics {
    // The longest attempted tweet length.
    private int longestAttemptedTweetLength;
    // The user and followee number map.
    private Map<String, Set<String>> userFollowee;
    // The user and Tweeter length map.
    private Map<String, Integer> userTweeterLength;

    //SingleTon
    private Statics() {
        longestAttemptedTweetLength = 0;
        userFollowee = new HashMap<String, Set<String>>();
        userTweeterLength = new HashMap<String, Integer>();
    }

    private static Statics instance = null;

    public static Statics getInstance() {
        if (instance == null) {
            instance = new Statics();
        }
        return instance;
    }

    public void resetStats() {
        longestAttemptedTweetLength = 0;
        userFollowee.clear();
        userTweeterLength.clear();
    }

    public void logTweet(String user, String message) {
        if (!userTweeterLength.containsKey(user)) {
            userTweeterLength.put(user, 0);
        }

        userTweeterLength.put(user, userTweeterLength.get(user) + message.length());
        updateLongestAttemptedTweetLength(message);
    }

    public void logFailedTweet(String user, String message) {
        updateLongestAttemptedTweetLength(message);
    }

    public void logFollow(String follower, String followee) {
        if (!userFollowee.containsKey(followee)) {
            userFollowee.put(followee, new HashSet<String>());
        }

        userFollowee.get(followee).add(follower);
    }

    public int getLongestAttemptedTweetLength() {
        return longestAttemptedTweetLength;
    }

    public Map<String, Set<String>> getUserFollowee() {
        return userFollowee;
    }

    public Map<String, Integer> getUserTweeterLength() {
        return userTweeterLength;
    }

    private void updateLongestAttemptedTweetLength(String message) {
        if (message.length() > longestAttemptedTweetLength) {
            longestAttemptedTweetLength = message.length();
        }
    }
}
