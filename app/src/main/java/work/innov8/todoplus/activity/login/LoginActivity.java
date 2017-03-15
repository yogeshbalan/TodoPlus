package work.innov8.todoplus.activity.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import work.innov8.todoplus.R;
import work.innov8.todoplus.activity.MainActivity;
import work.innov8.todoplus.data.LoginDBHelper;
import work.innov8.todoplus.util.Constant;

public class LoginActivity extends AppCompatActivity {

    @NotEmpty(messageResId = R.string.email_field)
    @BindView(R.id.ed_email_login)
    public EditText inputEmailED;

    @NotEmpty(messageResId = R.string.email_field)
    @BindView(R.id.ed_password_login)
    public EditText inputPasswordED;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.btn_signup)
    public Button btnSignup;

    @BindView(R.id.btn_login)
    public Button btnLogin;

    @OnClick(R.id.btn_signup)
    public void openSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.btn_login)
    public void openMainActivity() {
        progressBar.setVisibility(View.VISIBLE);
        String email = inputEmailED.getText().toString();
        String password = inputPasswordED.getText().toString();
        LoginDBHelper loginDBHelper = LoginDBHelper.getLoginDBHelperInstance(LoginActivity.this);
        String storedPassword = loginDBHelper.getPasswordEntry(email);
        if (password.equals(storedPassword)) {
            sharedPreferences = getSharedPreferences(Constant.MyPREFS, this.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constant.EMAIL_KEY, email);
            editor.putString(Constant.PASSWORD_KEY, password);
            editor.putBoolean(Constant.isLogin, true);
            editor.commit();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Hello " + email.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            inputEmailED.setText("");
            inputPasswordED.setText("");
            inputEmailED.requestFocus();
            Toast.makeText(this, this.getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
        }

    }

    public static final String TAG = "login";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        LoginDBHelper loginDBHelper = LoginDBHelper.getLoginDBHelperInstance(this);
        //List<User> userList = loginDBHelper.getAllUser();
        //Log.d(TAG, "onCreate user list email and password: " + userList.get(0).getEmail() + " : " + userList.get(0).getPassword());
        sharedPreferences = getSharedPreferences(Constant.MyPREFS, this.MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences.getBoolean(Constant.isLogin, false);
        if (isLoggedIn) {
            String email = sharedPreferences.getString(Constant.EMAIL_KEY, "user");
            Toast.makeText(this, "Hello " + email, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}
