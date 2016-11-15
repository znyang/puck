package com.zen.android.puck.rx;

import com.zen.android.puck.hook.PuckSchedulerHook;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * @author zen
 * @version 2016/11/11
 */

public class PuckRxJavaRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return RxJavaStatement.wrap(RxAndroidStatement.wrap(base));
    }

    private static class RxJavaStatement extends Statement {

        private final Statement base;

        private static Statement wrap(Statement base) {
            return new RxJavaStatement(base);
        }

        private RxJavaStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            RxJavaPlugins.getInstance().reset();
            RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());

            base.evaluate();

            RxJavaPlugins.getInstance().reset();
        }
    }

    private static class RxAndroidStatement extends Statement {

        private final Statement base;

        private static Statement wrap(Statement base) {
            return new RxAndroidStatement(base);
        }

        private RxAndroidStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            RxAndroidPlugins.getInstance().reset();
            RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
                @Override
                public Scheduler getMainThreadScheduler() {
                    return Schedulers.immediate();
                }
            });

            base.evaluate();

            RxAndroidPlugins.getInstance().reset();
        }
    }

}
