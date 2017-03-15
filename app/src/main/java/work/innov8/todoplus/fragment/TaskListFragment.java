package work.innov8.todoplus.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.innov8.todoplus.R;
import work.innov8.todoplus.adapter.TaskRecyclerViewAdapter;
import work.innov8.todoplus.adapter.UpdateAdapter;
import work.innov8.todoplus.data.TaskContract;
import work.innov8.todoplus.data.TaskDBHelper;
import work.innov8.todoplus.model.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment implements UpdateAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "typelist";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int typeList;
    private String mParam2;
    private List<Task> taskList = new ArrayList<>();
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    private UpdateAdapter updateAdapter;

    @BindView(R.id.toDoRecyclerView)
    RecyclerView TodoRecyclerView;

    @BindView(R.id.toDoEmptyView)
    LinearLayout emptyView;


    public TaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param typelist Parameter 1.
     * @param param2   Parameter 2.
     * @return A new instance of fragment TaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(int typelist, String param2) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typelist);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeList = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, view);
        TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(getContext());
        if (taskDBHelper != null) {
            taskList = taskDBHelper.getAllTask();
        }
        if (taskList.size() == 0) {
            TodoRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            TodoRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(getContext(), taskList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            TodoRecyclerView.setLayoutManager(mLayoutManager);
            TodoRecyclerView.setItemAnimator(new DefaultItemAnimator());
            TodoRecyclerView.setAdapter(taskRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void updateTaskArrayAdapter(List<Task> tasks) {
        this.taskList.clear();
        taskList.addAll(tasks);
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(getContext(), taskList, this);
        TodoRecyclerView.setAdapter(taskRecyclerViewAdapter);
    }

    @Override
    public void updateTaskArrayAdapter(Cursor cursor) {
        this.taskList.clear();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Task task = new Task();
            task.set_id(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry._ID)));
            task.setTaskTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
            task.setTaskDescription(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION)));
            Long valueDate = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DATE));
            if (valueDate != 0) {
                task.setTaskDate(new Date(valueDate));
            }
            Long valueTaskDateCreated = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED));
            if (valueTaskDateCreated != 0) {
                task.setTaskDateCreated(new Date(valueTaskDateCreated));
            }
            task.setTaskDone(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DONE)) > 0);
            this.taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(getContext(), taskList, this);
        TodoRecyclerView.setAdapter(taskRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.update();
    }

    @Override
    public void update() {
        switch (typeList) {
            case 0:
                GetPendingTasks getPendingTasks = new GetPendingTasks(getContext(), this);
                getPendingTasks.execute();
                break;
            case 1:
                GetDoneTasks getDoneTasks = new GetDoneTasks(getContext(), this);
                getDoneTasks.execute();
                break;
            case 2:
                GetAllTasks getAllTasks = new GetAllTasks(getContext(), this);
                getAllTasks.execute();
                break;
        }
    }


    private class GetAllTasks extends AsyncTask<Void, Void, Cursor> {

        private UpdateAdapter updateAdapter;
        private Context context;

        public GetAllTasks(Context context, UpdateAdapter updateAdapter) {
            this.updateAdapter = updateAdapter;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
            return taskDBHelper.getAll();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.updateTaskArrayAdapter(cursor);
        }
    }

    private class GetDoneTasks extends AsyncTask<Void, Void, Cursor> {

        private UpdateAdapter updateAdapter;
        private Context context;

        public GetDoneTasks(Context context, UpdateAdapter updateAdapter) {
            this.updateAdapter = updateAdapter;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
            return taskDBHelper.getDone();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.updateTaskArrayAdapter(cursor);
        }
    }

    private class GetPendingTasks extends AsyncTask<Void, Void, Cursor> {

        private UpdateAdapter updateAdapter;
        private Context context;

        public GetPendingTasks(Context context, UpdateAdapter updateAdapter) {
            this.updateAdapter = updateAdapter;
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
            return taskDBHelper.getPending();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            updateAdapter.updateTaskArrayAdapter(cursor);
        }
    }

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        private UpdateAdapter updateAdapter;
        private Context context;

        public DeleteTask(Context context, UpdateAdapter updateAdapter) {
            this.updateAdapter = updateAdapter;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            for (Integer id : integers) {
                TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(this.context);
                taskDBHelper.delete(id);
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
