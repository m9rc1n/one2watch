package vandy.mooc.view;

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

import java.util.ArrayList;
import java.util.List;

import vandy.mooc.R;
import vandy.mooc.model.aidl.TrailerData;

public class BoxOfficeFragment extends Fragment {

    public static final String ACTION_DISPLAY_TRAILER = "vandy.mooc.intent.action.BoxOfficeFragment";
    public static final String TYPE_TRAILER = "parcelable/trailer";
    public static final String KEY_TRAILER_DATA = "trailerList";
    private RecyclerView rv;
    private BroadcastReceiver mReceiver;

    public BoxOfficeFragment() {
    }

    public static Intent makeIntent(List<TrailerData> results) {
        ArrayList<TrailerData> arrayListResults = new ArrayList<>(results.size());
        arrayListResults.addAll(results);
        return new Intent(ACTION_DISPLAY_TRAILER).addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .putParcelableArrayListExtra(KEY_TRAILER_DATA, arrayListResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                RVAdapter adapter = new RVAdapter(intent.<TrailerData>getParcelableArrayListExtra(
                        KEY_TRAILER_DATA));
                rv.setAdapter(adapter);
            }
        };
        getContext().registerReceiver(mReceiver, new IntentFilter(ACTION_DISPLAY_TRAILER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
    }
}