package com.zen.android.puck.util;


import java.util.concurrent.Executor;

/**
 * CurrentThreadExecutor
 *
 * @author znyang 2016/11/29 0029
 */

public class CurrentThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        if (command == null) {
            return;
        }
        command.run();
    }
}
