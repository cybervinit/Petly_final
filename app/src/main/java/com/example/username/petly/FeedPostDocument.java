package com.example.username.petly;

import java.util.ArrayList;

/**
 * Class: Associated with FeedPostDocument objects used to transfer user's posts to and from
 * Cloudant server to application.
 */
public class FeedPostDocument {
    String _id;
    String _rev;
    ArrayList<Post> posts;

    /**
     * Constructor: Instantiates the FeedPostDocument
     * @param username: Assumes it is a non-null String object.
     */
    FeedPostDocument(String username) {
        this._id = username;
        this.posts = new ArrayList<>(0);
    }

    /**
     * Method: Used to add a Post object to the ArrayList of Posts part of this object.
     * @param newPost: Assumes it is a non-null Post object.
     */
    public void addPost(Post newPost) {
        try {
            posts.add(posts.size(), newPost);
        } catch (NullPointerException f) {
            posts = new ArrayList<>(0);
            posts.add(posts.size(), newPost);
        }
    }

    /**
     * Method: Used to get the user's posts in ArrayList format.
     * @return: ArrayList of Post objects.
     */
    public ArrayList<Post> getPosts() {
        return posts;
    }

    /**
     * Method: Returns the user's ID.
     * @return: User's ID in String format.
     */
    public String getID() {
        return _id;
    }

    /**
     * Method: Used to check if this object equals another FeedPostDocument object.
     * @param object: Assumes it is a non-null object. (Should be a FeedPostDocument object).
     * @return: Boolean according to the comparison of both the objects.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof FeedPostDocument) {
            FeedPostDocument obj = (FeedPostDocument) object;
            obj.getID().equals(this._id);
            return true;
        } else {
            return false;
        }
    }
}
