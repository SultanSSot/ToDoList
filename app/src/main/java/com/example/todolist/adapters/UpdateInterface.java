package com.example.todolist.adapters;

import android.database.Cursor;

import com.example.todolist.model.Task;

import java.util.ArrayList;

/**
 * Created by rodrigo on 09/09/16.
 */
public interface UpdateInterface {
    public void update(ArrayList<Task> tasks);
    public void update(Cursor cursor);
    public void update();
}
