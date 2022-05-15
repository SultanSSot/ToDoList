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

import com.example.todolist.database.DBHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;

public class ViewActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar_toolbar);
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
        Task task = DBHelper.getInstance(this).getTaskbyID(String.valueOf(this.id));

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDate = findViewById(R.id.tv_datetime);
        TextView tvDesc = findViewById(R.id.tv_desc);

        tvTitle.setText(task.getTitle());
        tvDesc.setText(task.getDescription());
        if(task.getDate() == null){
            tvDate.setVisibility(View.GONE);
        }else {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(new SimpleDateFormat("EEE").format(task.getDate()) + ", " + new SimpleDateFormat().format(task.getDate()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        if (menuID == R.id.task_delete) {
            if(this.id == 0) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            }else{
                new AlertDialog.Builder(ViewActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_confirmation)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteEntry deleteEntry = new DeleteEntry(ViewActivity.this);
                                deleteEntry.execute(ViewActivity.this.id);
                                Toast.makeText(ViewActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.logo_inverse)
                        .show();
            }
            return true;
        }else if(menuID == R.id.task_edit){
            if(this.id == 0) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            }else{
                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("id", String.valueOf(id));
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class DeleteEntry extends AsyncTask<Integer, Void, Void>{

        private Activity activity;

        public DeleteEntry(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Integer... args) {
            for(Integer id: args){
                DBHelper helper = DBHelper.getInstance(this.activity);
                helper.deleteTaskbyID(id);
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
