package work.innov8.todoplus.data;

import android.provider.BaseColumns;

/**
 * Created by yogesh on 14/3/17.
 */

public class LoginContract {
    public LoginContract() {
    }
    public static abstract class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
}
