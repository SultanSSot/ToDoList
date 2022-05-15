package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.todolist.database.DBHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity{

    private Calendar calendar;
    private EditText titleTaskET;
    private EditText descriptionTaskET;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String tastId = i.getStringExtra("id");
        task = DBHelper.getInstance(this).getTaskbyID(tastId);

        titleTaskET = findViewById(R.id.et_title);
        descriptionTaskET = findViewById(R.id.et_description);

        titleTaskET.setText(task.getTitle());
        descriptionTaskET.setText(task.getDescription());
        if(task.getDate() == null){
            calendar = null;
        }else{
            calendar = Calendar.getInstance();
            calendar.setTime(task.getDate());
        }

        titleTaskET = findViewById(R.id.et_title);
        updateTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.task_cancel) {
            this.finish();
            return true;
        }else if(id == R.id.task_save){
            task.setTitle(titleTaskET.getText().toString());
            task.setDescription(descriptionTaskET.getText().toString());
            if( calendar != null ){
                task.setDate(calendar.getTime());
            }
            UpdateEntry updateEntry = new UpdateEntry(this);
            updateEntry.execute(task);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        Calendar calendar;
        if(this.calendar == null) {
            calendar = Calendar.getInstance();
        }else{
            calendar = this.calendar;
        }
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                if(EditActivity.this.calendar == null){
                    EditActivity.this.calendar = Calendar.getInstance();
                }
                EditActivity.this.calendar.set(Calendar.YEAR, year);
                EditActivity.this.calendar.set(Calendar.MONTH, month);
                EditActivity.this.calendar.set(Calendar.DAY_OF_MONTH, day);
                updateTime();
                ImageButton imageButton = findViewById(R.id.remove_date_button);
                imageButton.setVisibility(View.VISIBLE);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void removeDateButton(View v) {
        ImageButton imageButton = findViewById(R.id.remove_date_button);
        imageButton.setVisibility(View.GONE);
        calendar = null;
        updateTime();
    }

    public void showTimePickerDialog(View v) {
        Calendar calendar;
        if(this.calendar == null) {
            calendar = Calendar.getInstance();
        }else{
            calendar = this.calendar;
        }
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                if(EditActivity.this.calendar == null){
                    EditActivity.this.calendar = Calendar.getInstance();
                }
                EditActivity.this.calendar.set(Calendar.HOUR_OF_DAY, h);
                EditActivity.this.calendar.set(Calendar.MINUTE, m);
                updateTime();
                ImageButton button = (ImageButton) findViewById(R.id.remove_date_button);
                button.setVisibility(View.VISIBLE);
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(this, listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    public void updateTime(){
        TextView textViewDate = findViewById(R.id.tv_date);
        if(calendar != null){
            textViewDate.setText(new SimpleDateFormat("EEE").format(calendar.getTime()) + ", " + new SimpleDateFormat().format(calendar.getTime()));
        }else{
            textViewDate.setText("");
        }
    }


    private class UpdateEntry extends AsyncTask<Task, Void, Void> {

        private Context context;

        public UpdateEntry(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(final Task... tasks) {
            for(Task task: tasks) {
                DBHelper taskDBHelper = DBHelper.getInstance(this.context);
                taskDBHelper.updateTask(task);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
        }
    }
}
