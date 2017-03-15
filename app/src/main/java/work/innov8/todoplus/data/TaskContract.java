package work.innov8.todoplus.data;

import android.provider.BaseColumns;

/**
 * Created by yogesh on 11/3/17.
 */

public class TaskContract {

    // To prevent someone from accidentally instantiating the contract class,
    // we make the constructor private.
    private TaskContract() {}

    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRTIPTION = "descritption";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TASK_DATE_CREATED = "task_created_date";
        public static final String COLUMN_NAME_DONE = "done";
    }


}
