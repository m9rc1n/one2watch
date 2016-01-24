package vandy.mooc.model;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericServiceConnection;
import vandy.mooc.model.aidl.TrailerCall;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerRequest;
import vandy.mooc.model.aidl.TrailerResults;
import vandy.mooc.model.aidl.TrailerType;
import vandy.mooc.model.services.TrailerServiceAsync;
import vandy.mooc.model.services.TrailerServiceSync;

public class TrailerModel implements MVP.ProvidedModelOps {

    protected final static String TAG = TrailerModel.class.getSimpleName();
    protected WeakReference<MVP.RequiredPresenterOps> mPresenter;

    private final TrailerResults.Stub mResults = new TrailerResults.Stub() {

        @Override
        public void sendPopularTrailersResults(List<TrailerData> results) throws RemoteException {
            mPresenter.get().displayResults(results, null, TrailerType.POPULAR);
        }

        @Override
        public void sendBoxOfficeTrailersResults(List<TrailerData> results) throws RemoteException {
            mPresenter.get().displayResults(results, null, TrailerType.BOX_OFFICE);
        }

        @Override
        public void sendComingSoonTrailersResults(List<TrailerData> results)
                throws RemoteException {
            mPresenter.get().displayResults(results, null, TrailerType.COMING_SOON);
        }

        @Override
        public void sendTrailersResults(List<TrailerData> results) throws RemoteException {
            mPresenter.get().displayResults(results, null, TrailerType.SEARCH);
        }

        @Override
        public void sendError(final String reason) throws RemoteException {
            mPresenter.get().displayResults(null, reason, TrailerType.ERROR);
        }
    };

    private GenericServiceConnection<TrailerRequest> mTrailerAsyncConn;

    private GenericServiceConnection<TrailerCall> mTrailerSyncConn;

    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        mPresenter = new WeakReference<>(presenter);
        mTrailerAsyncConn = new TrailerServiceConnection<>(TrailerRequest.class, mPresenter);
        mTrailerSyncConn = new TrailerServiceConnection<>(TrailerCall.class, mPresenter);
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
        if (mTrailerSyncConn.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(TrailerServiceSync.makeIntent(mPresenter.get()
                                    .getActivityContext()), mTrailerSyncConn,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on TrailerServiceAsync");
        }
        if (mTrailerAsyncConn.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(TrailerServiceAsync.makeIntent(mPresenter.get()
                                    .getActivityContext()), mTrailerAsyncConn,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on TrailerServiceAsync");
        }
    }

    private void unbindService() {
        Log.d(TAG, "calling unbindService()");
        if (mTrailerSyncConn.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mTrailerSyncConn);
        if (mTrailerAsyncConn.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mTrailerAsyncConn);
    }

    public boolean getTrailersAsync(TrailerType type) {
        try {
            switch (type) {
                case BOX_OFFICE:
                    mTrailerAsyncConn.getInterface().getBoxOfficeTrailers(mResults);
                    break;
                case POPULAR:
                    mTrailerAsyncConn.getInterface().getPopularTrailers(mResults);
                    break;
                case COMING_SOON:
                    mTrailerAsyncConn.getInterface().getComingSoonTrailers(mResults);
                    break;
                case SEARCH:
                    mTrailerAsyncConn.getInterface().getTrailers("", mResults);
                    break;
                case ERROR:
                    return false;
            }
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getTrailersAsync(String query, TrailerType type) {
        try {
            mTrailerAsyncConn.getInterface().getTrailers(query, mResults);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getTypePath(TrailerType type) {
        switch (type) {
            case BOX_OFFICE:
                return "boxoffice";
            case POPULAR:
                return "popular";
            case COMING_SOON:
                return "trailers";
            case SEARCH:
                return "trailers";
        }
        return null;
    }

    public List<TrailerData> getTrailersSync(TrailerType type) {
        try {
            switch (type) {
                case BOX_OFFICE:
                    return mTrailerSyncConn.getInterface().getBoxOfficeTrailers();
                case POPULAR:
                    return mTrailerSyncConn.getInterface().getPopularTrailers();
                case COMING_SOON:
                    return mTrailerSyncConn.getInterface().getComingSoonTrailers();
                case SEARCH:
                    return mTrailerSyncConn.getInterface().getTrailers("");
                case ERROR:
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TrailerData> getTrailersSync(String query, TrailerType type) {
        try {
            return mTrailerSyncConn.getInterface().getTrailers(query);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
