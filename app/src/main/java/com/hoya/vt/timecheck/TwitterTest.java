package com.hoya.vt.timecheck;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterTest {
    private static final String ACCESS_TOKEN = "4862469189-nTQrHeWycUUmNzHcKypYlVUigMGevWzoHbGQEKp";
    private static final String ACCESS_TOKEN_SECRET = "nMrqffb03wCcigJmPolqeilWsNutCOuBeMy7xx3m8IS5N\n";
    private static final String CONSUMER_KEY = "WKxzxQpFZNtbU4ti0N0Sxigbk";
    private static final String CONSUMER_SECRET = "P8ySJD1tpC6Mh8KfXvhppz2ioj8uuBs8Mrow2KDIaNr6ATdgu9";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthAccessToken(ACCESS_TOKEN);
        builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        builder.setOAuthConsumerKey(CONSUMER_KEY);
        builder.setOAuthConsumerSecret(CONSUMER_SECRET);
        OAuthAuthorization auth = new OAuthAuthorization(builder.build());
        Twitter twitter = new TwitterFactory().getInstance(auth);
        try {
            twitter.updateStatus("Hello World!");
        } catch (TwitterException e) {
            System.err.println("Error occurred while updating the status!");
            e.printStackTrace();
            return;
        }
        System.out.println("Successfully updated the status.");
    }
}