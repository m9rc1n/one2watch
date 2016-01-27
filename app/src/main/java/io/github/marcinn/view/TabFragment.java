package io.github.marcinn.view;

import android.content.BroadcastReceiver;
import android.content.Intent;
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

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class TabFragment extends Fragment {
    private RecyclerView mRecycleView;
    private TrailersAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    private ProgressBar mProgressBar;
    private TextView mProgressText;

    public TabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        findViews(view);
        prepareRecycleView(view);
        showLoadingView();
        return view;
    }

    protected void updateAdapter(Intent intent) {
        ArrayList<TrailerData> trailers = intent.getParcelableArrayListExtra(TrailerData.KEY_TRAILER_DATA);
        mAdapter.setTrailers(trailers);
    }

    protected void showTrailersView() {
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

    public BroadcastReceiver getReceiver() {
        return mReceiver;
    }

    public void setReceiver(BroadcastReceiver mReceiver) {
        this.mReceiver = mReceiver;
    }

    public TextView getProgressText() {
        return mProgressText;
    }
}