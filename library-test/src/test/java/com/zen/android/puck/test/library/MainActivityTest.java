package com.zen.android.puck.test.library;

import com.zen.android.puck.runner.PuckDebugTestRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;


@RunWith(PuckDebugTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity mMainActivity;

    @Before
    public void setUp() throws Exception {
        mMainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testStart() throws Exception {
        Assert.assertNotNull(mMainActivity.mTvContent);
        Assert.assertSame(mMainActivity.mTvContent.getText(), "");
    }

}
