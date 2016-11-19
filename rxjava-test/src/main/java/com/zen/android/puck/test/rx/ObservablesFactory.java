package com.zen.android.puck.test.rx;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * ObservablesFactory
 *
 * @author znyang 2016/11/11 0011
 */

public class ObservablesFactory {

    public static Observable<String> getSimpleObservable() {
        return Observable.create(subscriber -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                subscriber.onError(e);
            }
            subscriber.onNext("1");
            subscriber.onNext("2");
            subscriber.onNext("3");
            subscriber.onCompleted();
        });
    }

    public static Observable<List<String>> getTransformedObservable() {
        return getSimpleObservable().toList();
    }

}
