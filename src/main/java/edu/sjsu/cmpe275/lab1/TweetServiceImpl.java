package edu.sjsu.cmpe275.lab1;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {

    /***
     * Following is the dummy implementation of methods.
     * Students are expected to complete the actual implementation of these methods as part of lab completion.
     */

    public void tweet(String user, String message) throws IllegalArgumentException, IOException {
        if (message.contains("network error")) {
            throw new IOException("network error");
        }

        if (message.length() > 20) {
            throw new IllegalArgumentException("tweet length bigger than 140");
        }
    }

    static boolean flag = true;

    public void follow(String follower, String followee) throws IOException {
        if (flag && follower.equals("charles") && followee.equals("bob")) {
            flag = !flag;
            throw new IOException("network error");
        }

        if (follower.equals("bob") && followee.equals("alex")) {
            throw new IOException("network error");
        }
    }

}
