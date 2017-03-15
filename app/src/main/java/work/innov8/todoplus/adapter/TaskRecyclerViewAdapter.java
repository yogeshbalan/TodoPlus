package work.innov8.todoplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.innov8.todoplus.R;
import work.innov8.todoplus.activity.task.ViewTaskActivity;
import work.innov8.todoplus.data.TaskDBHelper;
import work.innov8.todoplus.model.Task;
import work.innov8.todoplus.util.Constant;
import work.innov8.todoplus.util.DateUtil;

/**
 * Created by yogesh on 13/3/17.
 */

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> implements UpdateAdapter {

    private List<Task> tasks;
    private UpdateAdapter updateAdapter;
    private Context context;


    public TaskRecyclerViewAdapter(Context context, List<Task> tasks, UpdateAdapter updateAdapter) {
        this.context = context;
        this.tasks = tasks;
        this.updateAdapter = updateAdapter;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, final int position) {
        final Task task = tasks.get(position);
        holder.taskTitleTV.setText(task.getTaskTitle());
        if (task.getTaskDate() != null) {
            DateUtil dateUtil = new DateUtil(context);
            holder.taskItemDateTV.setText(dateUtil.parse(task.getTaskDate()));
            holder.taskItemDateTV.setVisibility(View.VISIBLE);
        } else {
            holder.taskItemDateTV.setText("");
            holder.taskItemDateTV.setVisibility(View.GONE);
        }
        if (task.getTaskDateCreated() != null) {
            DateUtil dateUtil = new DateUtil(context);
            holder.taskItemDateCreatedTV.setText(dateUtil.parse(task.getTaskDateCreated()));
            holder.taskItemDateCreatedTV.setVisibility(View.VISIBLE);
        } else {
            holder.taskItemDateCreatedTV.setText("");
            holder.taskItemDateCreatedTV.setVisibility(View.GONE);
        }

        if (task.isTaskDone()) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.md_green_50));
        }

        holder.checkBox.setChecked(task.isTaskDone());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                task.setTaskDone(!task.isTaskDone());
                UpdateTask updateTask = new UpdateTask(context, updateAdapter);
                updateTask.execute(task);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = tasks.get(position);
                Intent intent = new Intent(context, ViewTaskActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constant.KEY_ID, task.get_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public void updateTaskArrayAdapter(List<Task> tasks) {

    }

    @Override
    public void updateTaskArrayAdapter(Cursor cursor) {

    }

    @Override
    public void update() {

    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_task_item)
        CardView cardView;

        @BindView(R.id.tv_task_item_title)
        TextView taskTitleTV;

        @BindView(R.id.tv_task_item_date)
        TextView taskItemDateTV;

        @BindView(R.id.tv_task_item_date_created)
        TextView taskItemDateCreatedTV;

        @BindView(R.id.cb_done_item)
        AppCompatCheckBox checkBox;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class UpdateTask extends AsyncTask<Task, Void, Void> {
        private Context context;
        private UpdateAdapter updateAdapter;

        public UpdateTask(Context context, UpdateAdapter updateAdapter) {
            this.context = context;
            this.updateAdapter = updateAdapter;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            for (Task task : tasks) {
                TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
                taskDBHelper.update(task);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            updateAdapter.update();
        }
    }

}
