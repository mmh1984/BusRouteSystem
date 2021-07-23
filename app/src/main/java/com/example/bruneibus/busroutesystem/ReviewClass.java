package com.example.bruneibus.busroutesystem;

/**
 * Created by Mikail on 23/3/2018.
 */

public class ReviewClass {

    String id;
    String busno,email,comments,commentdate;
    String ratings;

    public ReviewClass(String id,String busno,String email,String comments,String commentdate,String ratings){
    this.id=id;
    this.busno=busno;
    this.email=email;
    this.comments=comments;
    this.commentdate=commentdate;
    this.ratings=ratings;

    }

    public String getId() {
        return id;
    }

    public String getBusno() {
        return busno;
    }

    public String getEmail() {
        return email;
    }

    public String getComments() {
        return comments;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public String getRatings() {
        return ratings;
    }
}
