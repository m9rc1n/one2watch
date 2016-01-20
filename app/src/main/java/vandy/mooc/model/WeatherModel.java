package vandy.mooc.model;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericServiceConnection;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.WeatherCall;
import vandy.mooc.model.aidl.WeatherRequest;
import vandy.mooc.model.aidl.WeatherResults;
import vandy.mooc.model.services.WeatherServiceAsync;
import vandy.mooc.model.services.WeatherServiceSync;

/**
 * This class plays the "Model" role in the Model-View-Presenter (MVP)
 * pattern by defining an interface for providing data that will be
 * acted upon by the "Presenter" and "View" layers in the MVP pattern.
 * It implements the MVP.ProvidedModelOps so it can be created/managed
 * by the GenericPresenter framework.
 */
public class WeatherModel implements MVP.ProvidedModelOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final static String TAG = WeatherModel.class.getSimpleName();

    /**
     * A WeakReference used to access methods in the Presenter layer.
     * The WeakReference enables garbage collection.
     */
    protected WeakReference<MVP.RequiredPresenterOps> mPresenter;
    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.getCurrentWeather() method.
     * <p/>
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private final WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
        /**
         * This method is invoked by the WeatherServiceAsync to
         * return the results back.
         */
        @Override
        public void sendResults(final TrailerData weatherResults) throws RemoteException {
            // Pass the results back to the Presenter's
            // displayResults() method.
            // TODO -- you fill in here.
            mPresenter.get().displayResults(weatherResults, null);
        }

        /**
         * This method is invoked by the WeatherServiceAsync to
         * return error results back.
         */
        @Override
        public void sendError(final String reason) throws RemoteException {
            // Pass the results back to the Presenter's
            // displayResults() method.
            // TODO -- you fill in here.
            mPresenter.get().displayResults(null, reason);
        }
    };

    // TODO -- define ServiceConnetions to connect to the
    // WeatherServiceSync and WeatherServiceAsync.

    private GenericServiceConnection<WeatherRequest> mWeatherServiceAsyncConnection;

    private GenericServiceConnection<WeatherCall> mWeatherServiceSyncConnection;

    /**
     * Location we're trying to get current weather for.
     */
    private String mLocation;


    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        // Set the WeakReference.
        mPresenter = new WeakReference<>(presenter);

        // TODO -- you fill in here to initialize the WeatherService*.
        mWeatherServiceAsyncConnection = new GenericServiceConnection<>(WeatherRequest.class);
        mWeatherServiceSyncConnection = new GenericServiceConnection<>(WeatherCall.class);

        // Bind to the services.
        bindService();
    }

    /**
     * Hook method called to shutdown the Presenter layer.
     */
    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        // Don't bother unbinding the service if we're simply changing
        // configurations.
        if (isChangingConfigurations)
            Log.d(TAG, "Simply changing configurations, no need to destroy the Service");
        else unbindService();
    }

    /**
     * Initiate the service binding protocol.
     */
    private void bindService() {
        Log.d(TAG, "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.

        // TODO -- you fill in here.

        if (mWeatherServiceSyncConnection.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(WeatherServiceSync.makeIntent(mPresenter.get()
                                    .getActivityContext()),
                            mWeatherServiceSyncConnection,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on WeatherServiceAsync");
        }
        if (mWeatherServiceAsyncConnection.getInterface() == null) {
            mPresenter.get()
                    .getApplicationContext()
                    .bindService(WeatherServiceAsync.makeIntent(mPresenter.get()
                                    .getActivityContext()),
                            mWeatherServiceAsyncConnection,
                            Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Calling bindService() on WeatherServiceAsync");
        }
    }

    /**
     * Initiate the service unbinding protocol.
     */
    private void unbindService() {
        Log.d(TAG, "calling unbindService()");
        // TODO -- you fill in here to unbind from the WeatherService*.
        if (mWeatherServiceSyncConnection.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mWeatherServiceSyncConnection);
        if (mWeatherServiceAsyncConnection.getInterface() != null)
            mPresenter.get().getApplicationContext().unbindService(mWeatherServiceAsyncConnection);
    }

    /**
     * Initiate the asynchronous weather lookup.
     */
    public boolean getWeatherAsync(String location) {
        // TODO -- you fill in here.
        try {
            mWeatherServiceAsyncConnection.getInterface().getCurrentWeather(location, mWeatherResults);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Initiate the synchronous weather lookup.
     */
    public TrailerData getWeatherSync(String location) {
        // TODO -- you fill in here.
        try {
            for (TrailerData data : mWeatherServiceSyncConnection.getInterface()
                    .getCurrentWeather(location)) {
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
