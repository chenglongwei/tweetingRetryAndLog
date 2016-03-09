package edu.sjsu.cmpe275.lab1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.ExceptionDepthComparator;

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
            tweetStats.resetStats();
            test(tweetStats);

            tweeter.tweet("b", "bbbccc");
            test(tweetStats);
            tweeter.tweet("a", "bbbccc");
            test(tweetStats);
            tweeter.tweet("b", "dddddddddd");
            test(tweetStats);

            tweeter.follow("b", "c");
            tweeter.follow("b", "c");
            test(tweetStats);
            tweeter.follow("a", "b");
            test(tweetStats);
            tweeter.follow("a", "c");
            test(tweetStats);

            tweetStats.resetStats();

            try {
                tweeter.tweet("foo", "network error");
            } catch (Exception e) {

            }

            try {
                tweeter.tweet("foo", "network error with flag");
            } catch (Exception e) {

            }
            test(tweetStats);

            tweeter.follow("alex", "bob");
            test(tweetStats);

            try {
                tweeter.follow("charles", "bob");
            } catch (Exception e) {

            }
            try {
                tweeter.follow("charles", "bob");
            } catch (Exception e) {

            }

            try {
                tweeter.follow("bob", "alex");
            } catch (Exception e) {

            }
            test(tweetStats);

            tweetStats.resetStats();
            tweeter.tweet("alex", "first tweet");
            tweeter.tweet("alex", "second tweet");

            tweeter.tweet("bob", "first tweet");
            tweeter.tweet("bob", "second tweet");
            tweeter.tweet("bob", "third tweet");

            test(tweetStats);

            try {
                tweeter.tweet("charles", "sa;kdjfksdfdfdfdfdfdfdfjfdkfjkdjfkdjfkdjfkdjkfjdkfjkdjfkdjflkdjfkljdkdjfksdfdfdfdfdfdfdfjfdkfjkdjfkdjfkdjfkdjkfjdkfjkdjfkdjflkdjfkljdkk");
            } catch (Exception e) {

            }
            test(tweetStats);

            tweeter.follow("alex", "charles");
            tweeter.follow("alex", "david");

            tweeter.follow("david", "alex");


            tweeter.follow("bob", "charles");

            tweetStats.resetStats();
            tweeter.follow("alex", "david");
            tweeter.follow("david", "alex");

        } catch (Exception e) {
//            e.printStackTrace();
        }

        test(tweetStats);
    }

    private static void test(TweetStats tweetStats) {
        System.out.println("Most productive user: " + tweetStats.getMostProductiveUser());
        System.out.println("Most followed user: " + tweetStats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + tweetStats.getLengthOfLongestTweetAttempted());
    }
}
