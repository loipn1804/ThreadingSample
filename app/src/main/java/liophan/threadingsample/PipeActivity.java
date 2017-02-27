package liophan.threadingsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017, Stacck Pte Ltd. All rights reserved.
 *
 * @author Lio <lphan@stacck.com>
 * @version 1.0
 * @since February 27, 2017
 */

public class PipeActivity extends AppCompatActivity {

    public static final String TAG = "PipeActivity";

    @BindView(R.id.edtPipe)
    EditText edtPipe;
    @BindView(R.id.btnStop)
    Button btnStop;

    private PipedReader mReader;
    private PipedWriter mWriter;

    private Thread mThread;
    private PipeHandlerTask mPipeHandlerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipe);
        ButterKnife.bind(this);

        setupThread();
    }

    private void setupThread() {
        mReader = new PipedReader();
        mWriter = new PipedWriter();

        try {
            mWriter.connect(mReader);
        } catch (IOException e) {
            Log.e(PipeActivity.TAG, e.getMessage());
        }

        edtPipe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > before) {
                    Log.e(PipeActivity.TAG, s.subSequence(before, count).toString());
                    try {
                        mWriter.write(s.subSequence(before, count).toString());
//                        mWriter.flush();
                    } catch (IOException e) {
                        Log.e(PipeActivity.TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPipeHandlerTask = new PipeHandlerTask(mReader);
        mThread = new Thread(mPipeHandlerTask);
        mThread.start();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    private void stop() {
        mPipeHandlerTask.stop();
        try {
            if (mReader != null) {
                mReader.close();
            }
            if (mWriter != null) {
                mWriter.close();
            }
        } catch (IOException e) {
            Log.e(PipeActivity.TAG, e.getMessage());
        }
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
    }
}
