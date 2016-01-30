package io.github.marcinn;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.marcinn.model.aidl.TrailerData;
import io.github.marcinn.model.aidl.TrailerDataJsonParser;
import io.github.marcinn.view.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SampleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldTrailerJsonParserReturnCorrectStructure() {
        InputStream is = getInstrumentation().getTargetContext()
                .getResources()
                .openRawResource(R.raw.popular_trailers);
        final TrailerDataJsonParser parser = new TrailerDataJsonParser();
        try {
            List<TrailerData> returnList = parser.parseJsonStream(is);
            assertTrue(returnList.size() == 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
