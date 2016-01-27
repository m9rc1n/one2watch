package io.github.marcinn.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.github.marcinn.model.aidl.TrailerData;

public class PopularFragment extends TabFragment {
    public static final String ACTION_DISPLAY = "io.github.marcinn.view.PopularFragment:ACTION_DISPLAY";
    public static final String ACTION_SYNC = "io.github.marcinn.view.PopularFragment:ACTION_SYNC";

    public PopularFragment() {
    }

    public static Intent makeIntent(List<TrailerData> results) {
        ArrayList<TrailerData> arrayListResults = new ArrayList<>(results.size());
        arrayListResults.addAll(results);
        return new Intent(ACTION_DISPLAY).addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .putParcelableArrayListExtra(TrailerData.KEY_TRAILER_DATA, arrayListResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_DISPLAY:
                        updateAdapter(intent);
                        showTrailersView();
                        break;
                }
            }
        });
        getContext().registerReceiver(getReceiver(), new IntentFilter(ACTION_DISPLAY));
    }
}