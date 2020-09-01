package com.gepardec.mega.domain.model.monthlyreport;

import com.gepardec.mega.db.entity.State;

public class CommentDTO {
    private String message;
    private String author;
    private String creationDate;
    private State state;

    public CommentDTO() {
    }

    public CommentDTO(String message, String author, String creationDate, State state) {
        this.message = message;
        this.author = author;
        this.creationDate = creationDate;
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
