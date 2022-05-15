package com.example.todolist.model;

import java.util.Date;

/**
 * Created by rodrigo on 06/09/16.
 */
public class Task {
    private int _id;
    private String title;
    private String description;
    private Date date;
    private boolean isDone;

    public Task(){}

    public Task(String title, String description, Date date, boolean isDone) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.isDone = isDone;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }
}
