package work.innov8.todoplus.adapter;

import android.database.Cursor;

import java.util.List;

import work.innov8.todoplus.model.Task;

/**
 * Created by yogesh on 13/3/17.
 */

public interface UpdateAdapter {
    public void updateTaskArrayAdapter(List<Task> tasks);
    public void updateTaskArrayAdapter(Cursor cursor);
    public void update();
}
