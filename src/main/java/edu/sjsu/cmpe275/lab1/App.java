package edu.sjsu.cmpe275.lab1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is the dummy implementation of App to demonstrate bean creation with Application context.
         * Students may alter the following code as required.
         */

        ApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetServiceProxy");
        TweetStats tweetStats = (TweetStats) ctx.getBean("tweetStats");

        try {
            tweeter.tweet("alex", "first tweet");
            tweeter.tweet("alex", "second tweet");

            tweeter.tweet("bob", "first tweet");
            tweeter.tweet("bob", "second tweet");
            tweeter.tweet("bob", "third tweet");

            tweeter.follow("alex", "bob");
            tweeter.follow("alex", "charles");
            tweeter.follow("alex", "david");

            tweeter.follow("david", "alex");
            tweeter.follow("bob", "alex");

            tweeter.follow("charles", "bob");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + tweetStats.getMostProductiveUser());
        System.out.println("Most followed user: " + tweetStats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + tweetStats.getLengthOfLongestTweetAttempted());
    }
}
