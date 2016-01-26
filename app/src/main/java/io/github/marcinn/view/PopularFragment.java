package io.github.marcinn.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class PopularFragment extends Fragment {
    public static final String ACTION_DISPLAY = "io.github.marcinn.view.PopularFragment:ACTION_DISPLAY";
    public static final String ACTION_SYNC = "io.github.marcinn.view.PopularFragment:ACTION_SYNC";
    private BroadcastReceiver mReceiver;
    private RecyclerView mRecycleView;
    private ProgressBar mProgressBar;

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
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_DISPLAY:
                        ArrayList<TrailerData> trailers = intent.getParcelableArrayListExtra(
                                TrailerData.KEY_TRAILER_DATA);
                        RVAdapter adapter = new RVAdapter(trailers, context);
                        mRecycleView.setAdapter(adapter);
                        mRecycleView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };
        getContext().registerReceiver(mReceiver, new IntentFilter(ACTION_DISPLAY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
    }
}