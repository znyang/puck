package com.zen.android.puck.test.library;

import com.zen.android.puck.runner.PuckRxJavaRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

/**
 * ObservablesTest
 *
 * @author znyang 2016/11/11 0011
 */
@RunWith(PuckRxJavaRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ObservablesTest {

    @Test
    public void testSimple() throws Exception {

        ObservablesFactory.getSimpleObservable().subscribe();
    }
}
