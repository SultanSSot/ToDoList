package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todolist.adapters.TasksAdapter;
import com.example.todolist.adapters.UpdateInterface;
import com.example.todolist.database.DBHelper;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.Date;

public class ListFragment extends Fragment implements UpdateInterface {

    private TasksAdapter arrayAdapter;
    private int taskType;
    public ListFragment() { }

    public static ListFragment newInstance(int type) {
        ListFragment fragment = new ListFragment();

        Bundle argumentss = new Bundle();
        argumentss.putInt("typeList", type);
        fragment.setArguments(argumentss);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Store the listType
        if(savedInstanceState == null){
            taskType = getArguments().getInt("typeList", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View root =  layoutInflater.inflate(R.layout.fragment_list, parent, false);
        final ArrayList<Task> tasks = new ArrayList<Task>();
        arrayAdapter = new TasksAdapter(getActivity(), tasks, this);
        ListView tasksListView = (ListView) root.findViewById(R.id.list_tasks);
        tasksListView.setAdapter(arrayAdapter);

        //Open Task
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = arrayAdapter.getItem(position);
                Intent i = new Intent(getActivity(), ViewActivity.class);
                i.putExtra("id", task.get_id());
                startActivity(i);
            }
        });

        //Context Menu
        registerForContextMenu(tasksListView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.update();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Task task = arrayAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.task_delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_confirmation)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTaskByID deleteTaskByID = new DeleteTaskByID(getContext(), ListFragment.this);
                                deleteTaskByID.execute(task.get_id());
                                Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.logo_inverse)
                        .show();
                return true;
            case R.id.task_edit:
                Intent intentEdit = new Intent(getContext(), EditActivity.class);
                intentEdit.putExtra("id", String.valueOf(task.get_id()));
                startActivity(intentEdit);
                return true;
            case R.id.show_task:
                Intent intentShow = new Intent(getContext(), ViewActivity.class);
                intentShow.putExtra("id", task.get_id());
                startActivity(intentShow);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void update(ArrayList<Task> tasks) {
        this.arrayAdapter.clear();
        arrayAdapter.addAll(tasks);
    }

    @Override
    public void update(Cursor cursor) {
        this.arrayAdapter.clear();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Task task = new Task();
            task.set_id( cursor.getInt( cursor.getColumnIndex( DBHelper._ID )));
            task.setTitle( cursor.getString(cursor.getColumnIndex( DBHelper.COLUMN_TITLE)) );
            task.setDescription( cursor.getString(cursor.getColumnIndex( DBHelper.COLUMN_DECRTIPTION)) );
            Long timestamp = cursor.getLong(cursor.getColumnIndex( DBHelper.COLUMN_DATE));
            if(timestamp != 0){
                task.setDate( new Date( timestamp ));
            }
            task.setDone( cursor.getInt(cursor.getColumnIndex( DBHelper.COLUMN_DONE)) > 0);
            this.arrayAdapter.add(task);
            cursor.moveToNext();
        }
        cursor.close();
    }

    @Override
    public void update() {
        switch (taskType){
            case 0:
                PendingTasks pendingTasks = new PendingTasks(getContext(), this);
                pendingTasks.execute();
                break;
            case 1:
                DoneTasks doneTasks = new DoneTasks(getContext(), this);
                doneTasks.execute();
                break;
            case 2:
                AllTasks allTasks = new AllTasks(getContext(), this);
                allTasks.execute();
                break;
        }
    }

    private class AllTasks extends AsyncTask<Void, Void, Cursor>{

        private UpdateInterface updateAdapter;
        private Context context;

        public AllTasks(Context context, UpdateInterface updateInterface) {
            this.updateAdapter = updateInterface;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            DBHelper taskDBHelper = DBHelper.getInstance(this.context);
            return taskDBHelper.getAllTask();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.update(cursor);
        }
    }

    private class DoneTasks extends AsyncTask<Void, Void, Cursor>{

        private UpdateInterface updateAdapter;
        private Context context;

        public DoneTasks(Context context, UpdateInterface updateInterface) {
            this.updateAdapter = updateInterface;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            DBHelper taskDBHelper = DBHelper.getInstance(this.context);
            return taskDBHelper.getDoneTask();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.update(cursor);
        }
    }

    private class PendingTasks extends AsyncTask<Void, Void, Cursor>{

        private UpdateInterface updateAdapter;
        private Context context;

        public PendingTasks(Context context, UpdateInterface updateInterface) {
            this.updateAdapter = updateInterface;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            DBHelper taskDBHelper = DBHelper.getInstance(this.context);
            return taskDBHelper.getPendingTask();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.update(cursor);
        }
    }

    private class DeleteTaskByID extends AsyncTask<Integer, Void, Void>{

        private UpdateInterface updateAdapter;
        private Context context;

        public DeleteTaskByID(Context context, UpdateInterface updateInterface) {
            this.updateAdapter = updateInterface;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            for(Integer id: integers){
                DBHelper taskDBHelper = DBHelper.getInstance(this.context);
                taskDBHelper.deleteTaskbyID(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateAdapter.update();
        }
    }
}
