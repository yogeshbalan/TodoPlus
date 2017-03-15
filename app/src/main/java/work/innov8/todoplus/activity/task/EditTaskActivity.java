package work.innov8.todoplus.activity.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.innov8.todoplus.R;
import work.innov8.todoplus.data.TaskDBHelper;
import work.innov8.todoplus.model.Task;
import work.innov8.todoplus.util.Constant;
import work.innov8.todoplus.util.DateUtil;

public class EditTaskActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NotEmpty(messageResId = R.string.field_empty)
    @BindView(R.id.ed_title)
    public EditText taskTitleEditText;

    @NotEmpty(messageResId = R.string.field_empty)
    @BindView(R.id.ed_description)
    public EditText taskDescriptionEditText;

    @BindView(R.id.date)
    EditText taskDateEditText;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitleTV;

    private Calendar calendar;
    private Validator validator;
    private int task_id = 0;
    public static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitleTV.setText(getResources().getString(R.string.title_activity_Edit_task));
        }
        Intent intent = getIntent();
        this.task_id = Integer.valueOf(intent.getStringExtra(Constant.KEY_ID));
        updateFields();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_task_confirm) {
            validator.validate();
            return true;
        } else if (id == R.id.action_discard_task) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateFields() {
        Task task = TaskDBHelper.getTaskDBHelperInstance(this).getTask(String.valueOf(this.task_id));
        taskTitleEditText.setText(task.getTaskTitle());
        taskDescriptionEditText.setText(task.getTaskDescription());
        if (task.getTaskDate() == null) {
            calendar = null;
        } else {
            calendar = Calendar.getInstance();
            calendar.setTime(task.getTaskDate());
        }
        updateTime();
    }


    public void showDatePickerDialog(View v) {
        Calendar calendarTemp;
        if (calendar == null) {
            calendarTemp = Calendar.getInstance();
        } else {
            calendarTemp = this.calendar;
        }
        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                }
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateTime();
                ImageButton button = (ImageButton) findViewById(R.id.button_remove_date);
                button.setVisibility(View.VISIBLE);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, calendarTemp.get(Calendar.YEAR), calendarTemp.get(Calendar.MONTH), calendarTemp.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void removeDate(View v) {
        ImageButton button = (ImageButton) findViewById(R.id.button_remove_date);
        button.setVisibility(View.GONE);
        calendar = null;
        updateTime();
    }

    public void showTimePickerDialog(View v) {
        Calendar calendarTemp;
        if (calendar == null) {
            calendarTemp = Calendar.getInstance();
        } else {
            calendarTemp = this.calendar;
        }
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                }
                calendar.set(Calendar.HOUR, h);
                calendar.set(Calendar.MINUTE, m);
                updateTime();
                ImageButton button = (ImageButton) findViewById(R.id.button_remove_date);
                button.setVisibility(View.VISIBLE);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, myTimeListener, calendarTemp.get(Calendar.HOUR), calendarTemp.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void updateTime() {
        if (calendar != null) {
            taskDateEditText.setText(new DateUtil(this).parse(calendar.getTime()));
        } else {
            taskDateEditText.setText("");
        }
    }

    @Override
    public void onValidationSucceeded() {
        Task task = new Task(taskTitleEditText.getText().toString(), taskDescriptionEditText.getText().toString(), null, null, false);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Date mDate = new Date(currentDateTimeString);
        if (calendar != null) {
            task.setTaskDate(calendar.getTime());
            task.setTaskDateCreated(mDate);
        }
        EditTaskActivity.UpdateTask updateTask = new UpdateTask(this);
        updateTask.execute(task);
        this.finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateTask extends AsyncTask<Task, Void, Void> {

        private Context context;

        public UpdateTask (Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(final Task... tasks) {
            for(Task task: tasks) {
                TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
                taskDBHelper.update(task);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            Toast.makeText(EditTaskActivity.this, context.getResources().getString(R.string.task_saved_toast), Toast.LENGTH_SHORT).show();
        }
    }


}
