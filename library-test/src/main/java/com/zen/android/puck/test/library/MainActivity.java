package com.zen.android.puck.test.library;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.widget.TextView;

public class MainActivity extends Activity {

    @VisibleForTesting
    TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvContent = (TextView) findViewById(R.id.tv_content);
    }

}
