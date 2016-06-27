package com.example.username.petly;

import java.util.Calendar;
import java.util.Date;

/**
 * Class: Associated with the Post objects used to contain information of posts in the newsfeed.
 */
public class Post implements Comparable<Post> {
    private String postString;
    private Calendar postTime;
    private String posterUsername;
    private String posterPicture;

    /**
     * Constructor: Used to instantiate the Post object.
     * @param postString: Assumes it is a non-null String object.
     * @param posterUsername: Assumes it is a non-null String object.
     * @param posterPicture: Assumes it is a non-null String object (Encoded in Base64).
     */
    public Post(String postString, String posterUsername, String posterPicture) {
        this.postString = postString;
        this.postTime = Calendar.getInstance();
        this.posterUsername = posterUsername;
        this.posterPicture = posterPicture;
    }

    /**
     * Method: Returns the time of the post.
     * @return: Returns a Date object.
     */
    public Date getDateTime() {
        return postTime.getTime();
    }

    /**
     * Method: Returns the post's status.
     * @return: Returns the String object.
     */
    public String getStatus() {
        return postString;
    }

    /**
     * Method: Used to compare the post objects according to the dates of the objects.
     * @param post1: Assumes it is a non-null Post object.
     * @return: boolean according to comparison of the date.
     */
    @Override
    public int compareTo(Post post1) {
        return getDateTime().compareTo(post1.getDateTime());
    }

    /**
     * Method: Used to get the poster's picture in string format (Base64 encoded).
     * @return: Returns the String format picture (Base64 encoded).
     */
    public String getPosterPicture() {
        return posterPicture;
    }

    /**
     * Method: Used to get the post's user's username.
     * @return: Returns username in String format.
     */
    public String getPosterUsername() {
        return posterUsername;
    }
}
