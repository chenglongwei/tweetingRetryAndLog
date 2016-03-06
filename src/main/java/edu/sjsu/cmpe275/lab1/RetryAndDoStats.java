package edu.sjsu.cmpe275.lab1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

public class RetryAndDoStats implements MethodInterceptor, ApplicationContextAware {
    private TweetStatsImpl tweetStats;
    // retry times because network error.
    private static int count = 0;

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
                tweetStats.logFollow(arg1, arg2);
            } else if (isTweetMethod(invocation)) {
                tweetStats.logTweet(arg1, arg2);
            }

            return retVal;

        } catch (IOException ioe) {
            // retry 3 times
            if (count <= 3) {
                invoke(invocation);
                count++;
            } else {
                // retry over 3 times
                count = 0;
                if (isTweetMethod(invocation)) {
                    tweetStats.logFailedTweet(arg1, arg2);
                }
                throw ioe;
            }
        } catch (IllegalArgumentException e) {
            if (isTweetMethod(invocation)) {
                tweetStats.logFailedTweet(arg1, arg2);
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

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        tweetStats = (TweetStatsImpl) applicationContext.getBean("tweetStats");
    }
}
