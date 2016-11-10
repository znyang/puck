package com.zen.android.puck.rule;

import com.zen.android.puck.scheduler.PuckSchedulerHook;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.plugins.RxJavaPlugins;

/**
 * @author zen
 * @version 2016/11/11
 */

public class PuckRxJavaRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new PuckStatement(base);
    }

    private static class PuckStatement extends Statement {

        private final Statement base;

        public PuckStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            RxJavaPlugins.getInstance().reset();
            RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());

            base.evaluate();
        }
    }

}
