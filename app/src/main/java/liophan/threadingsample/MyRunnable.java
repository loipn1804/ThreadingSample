package liophan.threadingsample;

import android.util.Log;

/**
 * Copyright (c) 2017, Stacck Pte Ltd. All rights reserved.
 *
 * @author Lio <lphan@stacck.com>
 * @version 1.0
 * @since February 27, 2017
 */

public class MyRunnable implements Runnable {
    private volatile boolean isRunning = true;

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(PipeActivity.TAG, Thread.currentThread().getId() + " - " + Thread.currentThread().getName());
        }
    }
}
