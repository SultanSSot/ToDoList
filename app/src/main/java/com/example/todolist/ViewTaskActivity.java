package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.database.TaskDBHelper;
import com.example.todolist.model.Task;
import com.example.todolist.util.DateUtil;

public class ViewTaskActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        this.id = intent.getIntExtra("id", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
    }

    public void updateFields(){
        Task task = TaskDBHelper.getInstance(this).getTask(String.valueOf(this.id));

        TextView titulo = (TextView) findViewById(R.id.title_view);
        TextView data = (TextView) findViewById(R.id.date_view);
        TextView descricao = (TextView) findViewById(R.id.decription_view);

        titulo.setText(task.getTitle());
        descricao.setText(task.getDescription());
        if(task.getDate() == null){
            data.setVisibility(View.GONE);
        }else {
            data.setVisibility(View.VISIBLE);
            data.setText(new DateUtil(this).parse(task.getDate()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if (id_menu == R.id.action_delete) {
            if(this.id == 0) {
                Toast.makeText(this, "Task identifier unavaliable", Toast.LENGTH_SHORT);
            }else{
                new AlertDialog.Builder(ViewTaskActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_confirmation)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTask deleteTask = new DeleteTask(ViewTaskActivity.this);
                                deleteTask.execute(ViewTaskActivity.this.id);
                                Toast.makeText(ViewTaskActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.logo_inverse)
                        .show();
            }
            return true;
        }else if(id_menu == R.id.action_edit){
            if(this.id == 0) {
                Toast.makeText(this, "Task identifier unavaliable", Toast.LENGTH_SHORT);
            }else{
                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("id", String.valueOf(id));
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class DeleteTask extends AsyncTask<Integer, Void, Void>{

        private Activity activity;

        public DeleteTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            for(Integer id: integers){
                TaskDBHelper taskDBHelper = TaskDBHelper.getInstance(this.activity);
                taskDBHelper.delete(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activity.finish();
        }
    }
}
