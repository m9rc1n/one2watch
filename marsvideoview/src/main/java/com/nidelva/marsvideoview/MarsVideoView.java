package com.nidelva.marsvideoview;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MarsVideoView extends VideoView {

    public static final String ACTION_STOP = "ACTION_STOP";

    public MarsVideoView(Context context) {
        super(context);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_STOP.equals(intent.getAction())) {
                    if (isPlaying()) {
                        stopPlayback();
                    }
                }
            }
        }, new IntentFilter(ACTION_STOP));
    }

    public MarsVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarsVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarsVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void start() {
        super.start();
        getContext().sendBroadcast(new Intent(ACTION_STOP));
    }
}
