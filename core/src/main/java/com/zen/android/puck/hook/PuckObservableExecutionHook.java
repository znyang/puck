package com.zen.android.puck.hook;

import rx.Observable;
import rx.Subscription;
import rx.plugins.RxJavaObservableExecutionHook;

/**
 * PuckObservableExecutionHook
 *
 * @author znyang 2016/11/11 0011
 */
 class PuckObservableExecutionHook extends RxJavaObservableExecutionHook{

    @Override
    public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
        return super.onCreate(f);
    }

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance,
                                                          Observable.OnSubscribe<T> onSubscribe) {
        return super.onSubscribeStart(observableInstance, onSubscribe);
    }

    @Override
    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        return super.onSubscribeReturn(subscription);
    }
}
