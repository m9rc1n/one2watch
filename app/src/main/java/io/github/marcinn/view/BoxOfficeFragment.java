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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class BoxOfficeFragment extends Fragment {
    public static final String ACTION_DISPLAY = "io.github.marcinn.view.BoxOfficeFragment:ACTION_DISPLAY";
    public static final String ACTION_SYNC = "io.github.marcinn.view.BoxOfficeFragment:ACTION_SYNC";
    private RecyclerView mRecycleView;
    private TrailersAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    private ProgressBar mProgressBar;
    private TextView mProgressText;

    public BoxOfficeFragment() {
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
                        updateAdapter(intent);
                        showTrailersView();
                        break;
                }
            }
        };
        getContext().registerReceiver(mReceiver, new IntentFilter(ACTION_DISPLAY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        findViews(view);
        prepareRecycleView(view);
        showLoadingView();
        return view;
    }

    private void updateAdapter(Intent intent) {
        ArrayList<TrailerData> trailers = intent.getParcelableArrayListExtra(TrailerData.KEY_TRAILER_DATA);
        mAdapter.setTrailers(trailers);
    }

    private void showTrailersView() {
        mRecycleView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mProgressText.setVisibility(View.GONE);
    }

    private void findViews(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressText = (TextView) view.findViewById(R.id.progressText);
    }

    private void prepareRecycleView(View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv);
        mAdapter = new TrailersAdapter(new ArrayList<TrailerData>());
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showLoadingView() {
        mRecycleView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
    }
}