package liophan.threadingsample;

import android.util.Log;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Copyright (c) 2017, Stacck Pte Ltd. All rights reserved.
 *
 * @author Lio <lphan@stacck.com>
 * @version 1.0
 * @since February 27, 2017
 */

public class PipeHandlerTask implements Runnable {

    private volatile boolean isRunning;

    private PipedReader mReader;

    public PipeHandlerTask(PipedReader reader) {
        isRunning = true;
        mReader = reader;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            int i;
            try {
                while (isRunning && (i = mReader.read()) != -1) {
                    char c = (char) i;
                    Log.e(PipeActivity.TAG, Thread.currentThread().getName() + " - c:" + c + " - i:" + i);
                }
            } catch (IOException e) {
                Log.e(PipeActivity.TAG, Log.getStackTraceString(e));
            }
        }
    }
}
