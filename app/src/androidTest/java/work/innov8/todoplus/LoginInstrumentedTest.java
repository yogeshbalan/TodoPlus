package work.innov8.todoplus;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import work.innov8.todoplus.activity.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yogesh on 16/3/17.
 */

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    private String testEmailString, testInvalidEmailString, testEmptyEmailString;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Before
    public void initValidEmailString() {
        // Specify a valid string.
        testEmailString = "yogeshbalan@gmail.com";
    }

    @Before
    public void initInValidEmailString() {
        // Specify a valid string.
        testInvalidEmailString = "yogeshbalan@gmail..com";
    }

    @Before
    public void initEmptyEmailString() {
        // Specify a valid string.
        testEmptyEmailString = "";
    }

    @Test
    public void changeText_Activity() {
        // Type text and then press the button.
        onView(withId(R.id.ed_email_login))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.ed_email_login))
                .perform(typeText(testInvalidEmailString), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.ed_email_login))
                .perform(typeText(testEmptyEmailString), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }

}
