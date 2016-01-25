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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import vandy.mooc.R;
import vandy.mooc.model.aidl.TrailerData;

public class ComingSoonFragment extends Fragment {
    public static final String ACTION_DISPLAY = "vandy.mooc.view.ComingSoonFragment:ACTION_DISPLAY";
    public static final String ACTION_SYNC = "vandy.mooc.view.ComingSoonFragment:ACTION_SYNC";
    private final ImageLoader imageLoader;
    private RecyclerView rv;
    private BroadcastReceiver mReceiver;

    public ComingSoonFragment() {
        imageLoader = ImageLoader.getInstance();
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
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_DISPLAY:
                        ArrayList<TrailerData> trailers = intent.getParcelableArrayListExtra(
                                TrailerData.KEY_TRAILER_DATA);
                        RVAdapter adapter = new RVAdapter(trailers, imageLoader);
                        rv.setAdapter(adapter);
                        break;
                }
            }
        };
        getContext().registerReceiver(mReceiver, new IntentFilter(ACTION_DISPLAY));
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