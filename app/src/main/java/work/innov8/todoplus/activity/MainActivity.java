package work.innov8.todoplus.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.innov8.todoplus.R;
import work.innov8.todoplus.activity.login.LoginActivity;
import work.innov8.todoplus.activity.task.AddTaskActivity;
import work.innov8.todoplus.data.TaskDBHelper;
import work.innov8.todoplus.fragment.TaskListFragment;
import work.innov8.todoplus.util.Constant;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_pending:
                        selectedFragment = TaskListFragment.newInstance(0, "");
                        break;
                    case R.id.navigation_done:
                        selectedFragment = TaskListFragment.newInstance(1, "");
                        break;
                    case R.id.navigation_all:
                        selectedFragment = TaskListFragment.newInstance(2, "");
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, selectedFragment);
                fragmentTransaction.commit();
                return true;
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, TaskListFragment.newInstance(2, ""));
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, TaskListFragment.newInstance(2, ""));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_task) {
            startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        sharedPreferences = getSharedPreferences(Constant.MyPREFS, this.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        new AlertDialog.Builder(this)
                .setTitle(this.getResources().getString(R.string.dialog_logout_title))
                .setMessage(this.getResources().getString(R.string.dialog_logout_desc))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear();
                        editor.commit();
                        TaskDBHelper taskDBHelper = TaskDBHelper.getTaskDBHelperInstance(MainActivity.this);
                        taskDBHelper.deleteTable();
                        // After logout redirect user to Loing Activity
                        Intent startLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                        // Closing all the Activities
                        startLoginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        startLoginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        // Staring Login Activity
                        startActivity(startLoginActivityIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
