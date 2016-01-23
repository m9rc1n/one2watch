package vandy.mooc.model.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import vandy.mooc.model.aidl.TrailerCall;
import vandy.mooc.model.aidl.TrailerData;

public class TrailerServiceSync extends TrailerServiceBase {

    private final TrailerCall.Stub mWeatherCallImpl = new TrailerCall.Stub() {

        @Override
        public List<TrailerData> getPopularTrailers() throws RemoteException {
            return getTrailerResults("popular");
        }

        @Override
        public List<TrailerData> getBoxOfficeTrailers() throws RemoteException {
            return getTrailerResults("/boxoffice");
        }

        @Override
        public List<TrailerData> getComingSoonTrailers() throws RemoteException {
            return getTrailerResults("/trailers");
        }

        @Override
        public List<TrailerData> getTrailers(String query) throws RemoteException {
            return getTrailerResults("/trailers?title=" + query);
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context, TrailerServiceSync.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }
}
