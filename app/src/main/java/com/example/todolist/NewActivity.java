package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

public class NewActivity extends AppCompatActivity{

    private Calendar calendar;
    private EditText titleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = findViewById(R.id.actionbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleTask = findViewById(R.id.et_title);
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
            TextView title = findViewById(R.id.et_title);
            TextView description = findViewById(R.id.et_description);
            Task task = new Task(title.getText().toString(),description.getText().toString(),null,false);
            if( calendar != null ){
                task.setDate(calendar.getTime());
            }
            SaveEntry saveEntry = new SaveEntry(this);
            saveEntry.execute(task);
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
                if(NewActivity.this.calendar == null){
                    NewActivity.this.calendar = Calendar.getInstance();
                }
                NewActivity.this.calendar.set(Calendar.YEAR, year);
                NewActivity.this.calendar.set(Calendar.MONTH, month);
                NewActivity.this.calendar.set(Calendar.DAY_OF_MONTH, day);
                updateTime();
                ImageButton button = findViewById(R.id.remove_date_button);
                button.setVisibility(View.VISIBLE);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void removeDateButton(View v) {
        ImageButton button = findViewById(R.id.remove_date_button);
        button.setVisibility(View.GONE);
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
                if(NewActivity.this.calendar == null){
                    NewActivity.this.calendar = Calendar.getInstance();
                }
                NewActivity.this.calendar.set(Calendar.HOUR_OF_DAY, h);
                NewActivity.this.calendar.set(Calendar.MINUTE, m);
                updateTime();
                ImageButton imageButton = findViewById(R.id.remove_date_button);
                imageButton.setVisibility(View.VISIBLE);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void updateTime(){
        TextView tvDate = findViewById(R.id.tv_date);
        if(calendar != null){
            tvDate.setText(new SimpleDateFormat("EEE").format(calendar.getTime()) + ", " + new SimpleDateFormat().format(calendar.getTime()));
        }else{
            tvDate.setText("");
        }
    }


    private class SaveEntry extends AsyncTask<Task, Void, Boolean> {

        private Context context;

        public SaveEntry(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(final Task... tasks) {
            int rowIns = 0;
            for(Task task: tasks) {
                DBHelper taskDBHelper = DBHelper.getInstance(this.context);
                Long newRowId = taskDBHelper.insertTask(task);
                rowIns += newRowId;
            }
            return rowIns > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
