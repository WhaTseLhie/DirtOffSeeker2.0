package com.example.jayvee.dirtoffseeker;

public class Feedback {

    String feedback_comment, feedback_id, feedback_rating, feedback_seekerId, feedback_workerId;

    public Feedback(String feedback_comment, String feedback_id, String feedback_rating, String feedback_seekerId, String feedback_workerId) {
        this.feedback_comment = feedback_comment;
        this.feedback_id = feedback_id;
        this.feedback_rating = feedback_rating;
        this.feedback_seekerId = feedback_seekerId;
        this.feedback_workerId = feedback_workerId;
    }

    public String getFeedback_comment() {
        return feedback_comment;
    }

    public void setFeedback_comment(String feedback_comment) {
        this.feedback_comment = feedback_comment;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFeedback_rating() {
        return feedback_rating;
    }

    public void setFeedback_rating(String feedback_rating) {
        this.feedback_rating = feedback_rating;
    }

    public String getFeedback_seekerId() {
        return feedback_seekerId;
    }

    public void setFeedback_seekerId(String feedback_seekerId) {
        this.feedback_seekerId = feedback_seekerId;
    }

    public String getFeedback_workerId() {
        return feedback_workerId;
    }

    public void setFeedback_workerId(String feedback_workerId) {
        this.feedback_workerId = feedback_workerId;
    }
}