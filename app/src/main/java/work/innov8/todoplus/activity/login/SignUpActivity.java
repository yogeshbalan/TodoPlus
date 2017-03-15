package work.innov8.todoplus.activity.login;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import work.innov8.todoplus.R;
import work.innov8.todoplus.activity.MainActivity;
import work.innov8.todoplus.data.LoginDBHelper;
import work.innov8.todoplus.model.User;
import work.innov8.todoplus.util.Constant;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty(messageResId = R.string.field_empty)
    @Email(messageResId = R.string.email_field)
    @BindView(R.id.ed_email_sign_up)
    public EditText inputEmailED;

    @NotEmpty(messageResId = R.string.field_empty)
    @Password(messageResId = R.string.password_field)
    @BindView(R.id.ed_password_sign_up)
    public EditText inputPasswordED;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.sign_up_button)
    public Button btnSignup;

    @BindView(R.id.sign_in_button)
    public Button btnSignin;

    @OnClick(R.id.sign_up_button)
    public void openMainActivity() {
        validator.validate();
    }

    @OnClick(R.id.sign_in_button)
    public void openLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
        finish();
    }

    private Validator validator;
    private SharedPreferences sharedPreferences;
    public static final String TAG = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        String email = inputEmailED.getText().toString();
        String password = inputPasswordED.getText().toString();
        User user = new User(email, password);
        sharedPreferences = getSharedPreferences(Constant.MyPREFS, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.EMAIL_KEY, email);
        editor.putString(Constant.PASSWORD_KEY, password);
        editor.putBoolean(Constant.isLogin, true);
        editor.commit();
        CreateUser createUser = new CreateUser(this);
        createUser.execute(user);
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
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

    public class CreateUser extends AsyncTask<User, Void, Boolean> {

        private Context context;

        public CreateUser (Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            int rowInserted = 0;
            for (User user : users) {
                LoginDBHelper loginDBHelper = LoginDBHelper.getLoginDBHelperInstance(SignUpActivity.this);
                Long newRowId = loginDBHelper.insert(user);
                Log.d(TAG, "doInBackground: " + String.valueOf(newRowId));
                rowInserted += newRowId;
            }
            return rowInserted > 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(context, "User Created Succesfully", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

}
