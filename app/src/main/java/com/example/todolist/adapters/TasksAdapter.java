package com.example.todolist.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.database.DBHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 06/09/16.
 */
public class TasksAdapter extends ArrayAdapter<Task>{
    private List<Task> tasks;
    private UpdateInterface updateInterface;

    public TasksAdapter(Context context, ArrayList<Task> taskArrayList, UpdateInterface updateInterface) {
        super(context, 0, taskArrayList);
        taskArrayList = new ArrayList<Task>();
        this.updateInterface = updateInterface;
    }

    @Override
    public View getView(final int position, View itemView, final ViewGroup parentvIEW) {
        final Task task = getItem(position);
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.task_row_item, parentvIEW, false);
        }
        CheckBox doneCheckBox = itemView.findViewById(R.id.done_item);
        doneCheckBox.setChecked(task.isDone());
        TextView titleTV = itemView.findViewById(R.id.title_item);
        TextView dateTV = itemView.findViewById(R.id.date_item);

        titleTV.setText(task.getTitle());
        if( task.getDate() != null){
            dateTV.setText(new SimpleDateFormat("EEE").format(task.getDate()) + ", " + new SimpleDateFormat().format(task.getDate()));
            dateTV.setVisibility(View.VISIBLE);
        }else{
            dateTV.setText("");
            dateTV.setVisibility(View.GONE);
        }
        doneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                task.setDone(!task.isDone());
                UpdateItem updateTask = new UpdateItem(getContext(), updateInterface);
                updateTask.execute(task);
            }
        });

        return itemView;
    }

    private class UpdateItem extends AsyncTask<Task, Void, Void>{
        private Context context;
        private UpdateInterface updateInter;

        public UpdateItem(Context context, UpdateInterface updateAdapter){
            this.context = context;
            this.updateInter = updateAdapter;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            for(Task task : tasks) {
                DBHelper _dbHelper = DBHelper.getInstance(this.context);
                _dbHelper.updateTask(task);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            updateInter.update();
        }
    }
}
