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
        public void getPopularTrailers(TrailerResults callback) throws RemoteException {
            List<TrailerData> results = getTrailerResults("/popular");
            if (results == null) {
                callback.sendError("Empty list");
            } else {
                callback.sendPopularTrailersResults(results);
            }
        }

        @Override
        public void getBoxOfficeTrailers(TrailerResults callback) throws RemoteException {
            List<TrailerData> results = getTrailerResults("/boxoffice");
            if (results == null) {
                callback.sendError("Empty list");
            } else {
                callback.sendBoxOfficeTrailersResults(results);
            }
        }

        @Override
        public void getComingSoonTrailers(TrailerResults callback) throws RemoteException {
            List<TrailerData> results = getTrailerResults("/trailers");
            if (results == null) {
                callback.sendError("Empty list");
            } else {
                callback.sendComingSoonTrailersResults(results);
            }
        }

        @Override
        public void getTrailers(String query, TrailerResults callback) throws RemoteException {
            List<TrailerData> results = getTrailerResults("/trailers?title=" + query);
            if (results == null) {
                callback.sendError("Empty list");
            } else {
                callback.sendTrailersResults(results);
            }
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
