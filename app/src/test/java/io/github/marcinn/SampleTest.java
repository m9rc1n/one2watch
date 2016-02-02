package io.github.marcinn;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.JsonReader;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.marcinn.model.aidl.TrailerData;
import io.github.marcinn.model.aidl.TrailerDataJsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@LargeTest
public class SampleTest {

    @Test
    public void testSample() {
        // JsonReader cannot be tested locally -> it belongs to android.jar
        InputStream stream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("popular_trailers.json");
    }
}
