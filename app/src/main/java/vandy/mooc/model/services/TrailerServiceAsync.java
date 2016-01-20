package vandy.mooc.model.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerRequest;
import vandy.mooc.model.aidl.TrailerResults;

public class TrailerServiceAsync extends TrailerServiceBase {

    private final TrailerRequest.Stub mWeatherRequestImpl = new TrailerRequest.Stub() {

        @Override
        public void getCurrentTrailer(final String location, final TrailerResults callback)
                throws RemoteException {
            List<TrailerData> results = getTrailerResults(location);

            if (results == null) {
                callback.sendError("Empty list");
                return;
            }

            for (TrailerData data : results) {
                if (data != null) {
                    callback.sendResults(data);
                    return;
                }
            }

            callback.sendError("Empty elements");
        }
    };

    private ExecutorService mExecutorService;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TrailerServiceAsync.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutorService = Executors.newCachedThreadPool();
    }

    @Override
    public void onDestroy() {
        mExecutorService.shutdownNow();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherRequestImpl;
    }
}
