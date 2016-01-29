package io.github.marcinn;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.marcinn.view.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SampleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sample_sameActivity() {
    }

}
