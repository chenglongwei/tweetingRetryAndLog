package edu.sjsu.cmpe275.lab1;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {

    /***
     * Following is the dummy implementation of methods.
     * Students are expected to complete the actual implementation of these methods as part of lab completion.
     */

    static boolean tweet_flag = true;

    public void tweet(String user, String message) throws IllegalArgumentException, IOException {
        System.out.println("tweet(" + user + ", " + message + ")" + "called");

        if (message.equals("network error")) {
            throw new IOException("network error");
        }

        if (message.equals("network error with flag") && tweet_flag) {
            tweet_flag = false;
            throw new IOException("network error");
        }

        if (message.length() > 40) {
            throw new IllegalArgumentException("tweet length bigger than 140");
        }
    }

    static boolean flag = true;

    public void follow(String follower, String followee) throws IOException {
        System.out.println("follow(" + follower + ", " + followee + ")" + "called");

        if (flag && follower.equals("charles") && followee.equals("bob")) {
            flag = false;
            throw new IOException("network error");
        }

        if (follower.equals("bob") && followee.equals("alex")) {
            throw new IOException("network error");
        }
    }

}
