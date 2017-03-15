package work.innov8.todoplus;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import work.innov8.todoplus.activity.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by yogesh on 16/3/17.
 */
@RunWith(AndroidJUnit4.class)
public class OpenSignUpInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void clickSignUpButton_opensSignUpScreen() {
        //locate and click on the login button
        onView(withId(R.id.btn_signup)).perform(click());

        //check if the sign up screen is displayed by asserting that the first name editText is displayed
        onView(withId(R.id.ed_email_sign_up)).check(matches(allOf(isDescendantOfA(withId(R.id.linear_layout_sign_up)), isDisplayed())));
    }

}
