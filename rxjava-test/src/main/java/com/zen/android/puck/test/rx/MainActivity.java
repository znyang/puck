package com.zen.android.puck.test.rx;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    @VisibleForTesting
    TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvContent = (TextView) findViewById(R.id.tv_content);
    }

    void loadData() {
        Observable.defer(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Observable.just("ok");
        })
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    mTvContent.setText(result);
                });
    }

}
