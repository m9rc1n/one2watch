package vandy.mooc.model;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericServiceConnection;
import vandy.mooc.model.aidl.TrailerCall;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerRequest;
import vandy.mooc.model.aidl.TrailerResults;
import vandy.mooc.model.services.TrailerServiceAsync;
import vandy.mooc.model.services.TrailerServiceSync;

public class TrailerModel implements MVP.ProvidedModelOps {

    protected final static String TAG = TrailerModel.class.getSimpleName();
    protected WeakReference<MVP.RequiredPresenterOps> mPresenter;

    private final TrailerResults.Stub mWeatherResults = new TrailerResults.Stub() {

        @Override
        public void sendResults(final TrailerData weatherResults) throws RemoteException {
            mPresenter.get().displayResults(weatherResults, null);
        }

        @Override
        public void sendError(final String reason) throws RemoteException {
            mPresenter.get().displayResults(null, reason);
        }
    };

    private GenericServiceConnection<TrailerRequest> mTrailerServAsyncConn;

    private GenericServiceConnection<TrailerCall> mTrailerServSyncConn;

    private String mLocation;

    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        mPresenter = new WeakReference<>(presenter);
        mTrailerServAsyncConn = new GenericServiceConnection<>(TrailerRequest.class);
        mTrailerServSyncConn = new GenericServiceConnection<>(TrailerCall.class);
        bindService();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        if (isChangingConfigurations)
            Log.d(TAG, "Simply changing configurations, no need to destroy the Service");
        else unbindService();
    }

    private void bindService() {
        Log.d(TAG, "calling bindService()");
        if (mTrailerServSyncConn.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(TrailerServiceSync.makeIntent(mPresenter.get()
                                    .getActivityContext()),
                            mTrailerServSyncConn,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on TrailerServiceAsync");
        }
        if (mTrailerServAsyncConn.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(TrailerServiceAsync.makeIntent(mPresenter.get()
                                    .getActivityContext()),
                            mTrailerServAsyncConn,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on TrailerServiceAsync");
        }
    }

    private void unbindService() {
        Log.d(TAG, "calling unbindService()");
        if (mTrailerServSyncConn.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mTrailerServSyncConn);
        if (mTrailerServAsyncConn.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mTrailerServAsyncConn);
    }

    public boolean getWeatherAsync(String location) {
        try {
            mTrailerServAsyncConn.getInterface().getCurrentTrailer(location, mWeatherResults);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;

    }

    public TrailerData getWeatherSync(String location) {
        try {
            for (TrailerData data : mTrailerServSyncConn.getInterface()
                    .getCurrentTrailer(location)) {
                if (data != null) {
                    return data;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
