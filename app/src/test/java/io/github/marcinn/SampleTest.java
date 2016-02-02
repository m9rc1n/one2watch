package io.github.marcinn;

import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Test;

import java.net.URL;

@LargeTest
public class SampleTest {

    @Test
    public void testSample() {
        Assert.assertEquals(1, 1);
        URL url = this.getClass().getClassLoader().getResource("popular_trailers.json");
    }
}
