package work.innov8.todoplus.activity.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.innov8.todoplus.R;
import work.innov8.todoplus.data.TaskDBHelper;
import work.innov8.todoplus.model.Task;
import work.innov8.todoplus.util.Constant;
import work.innov8.todoplus.util.DateUtil;

public class ViewTaskActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitleTV;

    @BindView(R.id.tv_task_item_title)
    TextView taskTitleTV;

    @BindView(R.id.tv_task_item_desc)
    TextView taskDescriptionTV;

    @BindView(R.id.tv_task_item_date)
    TextView taskItemDateTV;

    @BindView(R.id.tv_task_item_date_created)
    TextView taskItemDateCreatedTV;

    @BindView(R.id.tv_task_item_status)
    TextView taskItemStatusTV;

    @BindView(R.id.cb_done_item)
    AppCompatCheckBox checkBox;

    private int task_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_view_task));
            toolbarTitleTV.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        this.task_id = intent.getIntExtra(Constant.KEY_ID, 0);
        updateFields();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
    }

    public void updateFields(){
        Task task = TaskDBHelper.getTaskDBHelperInstance(this).getTask(String.valueOf(this.task_id));

        taskTitleTV.setText(task.getTaskTitle());
        taskDescriptionTV.setText(task.getTaskDescription());
        if(task.getTaskDate() == null){
            taskItemDateTV.setVisibility(View.GONE);
        }else {
            taskItemDateTV.setVisibility(View.VISIBLE);
            taskItemDateTV.setText(this.getResources().getString(R.string.due_date) + new DateUtil(this).parse(task.getTaskDate()));
        }

        if(task.getTaskDateCreated() == null){
            taskItemDateCreatedTV.setVisibility(View.GONE);
        }else {
            taskItemDateCreatedTV.setVisibility(View.VISIBLE);
            taskItemDateCreatedTV.setText(this.getResources().getString(R.string.created_date) + task.getTaskDateCreated());
        }
        checkBox.setChecked(task.isTaskDone());
        if (task.isTaskDone()) {
            taskItemStatusTV.setText(this.getResources().getString(R.string.task_status_done));
        } else {
            taskItemStatusTV.setText(this.getResources().getString(R.string.task_status_pending));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if(this.task_id == 0) {
                Toast.makeText(this, this.getResources().getString(R.string.cannot_find_task), Toast.LENGTH_SHORT);
            }else{
                new AlertDialog.Builder(this)
                        .setTitle(this.getResources().getString(R.string.dialog_delete_task_title))
                        .setMessage(this.getResources().getString(R.string.dialog_delete_task_desc))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTask deleteTask = new DeleteTask(ViewTaskActivity.this);
                                deleteTask.execute(task_id);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        } else if (id == R.id.action_edit) {
            if(this.task_id == 0) {
                Toast.makeText(this, this.getResources().getString(R.string.cannot_find_task), Toast.LENGTH_SHORT);
            }else{
                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("id", String.valueOf(task_id));
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        private Activity activity;

        public DeleteTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            for(Integer id: integers){
                TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.activity);
                taskDBHelper.delete(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activity.finish();
            Toast.makeText(ViewTaskActivity.this, getResources().getString(R.string.delete_task_toast), Toast.LENGTH_SHORT).show();
        }
    }


}
