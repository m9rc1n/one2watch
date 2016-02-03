package com.nidelva.marsvideoview;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.util.UUID;

public class MarsVideoView extends VideoView {

    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String EXTRA_UUID = "EXTRA_UUID";
    private UUID mUUID;

    public MarsVideoView(Context context) {
        super(context);
        registerReceiver(context);
    }

    public MarsVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerReceiver(context);
    }

    public MarsVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerReceiver(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarsVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        registerReceiver(context);
    }

    private void registerReceiver(Context context) {
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_STOP.equals(intent.getAction())) {
                    if (intent.getStringExtra(EXTRA_UUID).equals(mUUID.toString())) return;
                    if (isPlaying()) {
                        pause();
                    }
                }
            }
        }, new IntentFilter(ACTION_STOP));
        mUUID = UUID.randomUUID();
    }

    @Override
    public void start() {
        super.start();
        Intent intent = new Intent(ACTION_STOP);
        intent.putExtra(EXTRA_UUID, mUUID.toString());
        getContext().sendBroadcast(intent);
    }
}
