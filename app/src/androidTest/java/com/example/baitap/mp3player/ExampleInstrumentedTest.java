package com.example.baitap.mp3player;

import static junit.framework.Assert.assertEquals;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.testng.annotations.Test;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Instrumentation InstrumentationRegistry;
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.baitap.mp3player", appContext.getPackageName());
    }


}
