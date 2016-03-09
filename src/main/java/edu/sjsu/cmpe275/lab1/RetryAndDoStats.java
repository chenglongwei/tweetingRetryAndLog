package edu.sjsu.cmpe275.lab1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

/***
 * Created by Chenglong Wei on 3/5/16.
 * Student ID: 010396464
 *
 * Use ApplicationContextAware to inject singleton bean tweetStats.
 * This is the implementation of advice.
 * (1) Retry 3 up to times for network error.
 * (2) Log events of tweet and follow.
 */

public class RetryAndDoStats implements MethodInterceptor, ApplicationContextAware {
    // Retry times because of network error.
    private static final int RETRY_TIMES = 3;
    // TweetStatsImpl which has api to log tweet and follow events.
    private TweetStatsImpl tweetStats;

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
                    tweetStats.logFollow(arg1, arg2);
                } else if (isTweetMethod(invocation)) {
                    tweetStats.logTweet(arg1, arg2);
                }

                return retVal;

            } catch (IOException e) {
                // Catch network error, retry three times.
                exception = e;
                numRetry++;
            } catch (IllegalArgumentException e) {
                // If tweeter is more than 140, log the max length.
                if (isTweetMethod(invocation)) {
                    tweetStats.logFailedTweet(arg1, arg2);
                }
                throw e;
            }
        } while (numRetry <= RETRY_TIMES);

        // Only retry fails arrive here, log the failed tweeter.
        if (numRetry > RETRY_TIMES) {
            if (isTweetMethod(invocation)) {
                tweetStats.logFailedTweet(arg1, arg2);
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

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Use the applicationContext to initialize tweetStats.
        tweetStats = (TweetStatsImpl) applicationContext.getBean("tweetStats");
    }
}
