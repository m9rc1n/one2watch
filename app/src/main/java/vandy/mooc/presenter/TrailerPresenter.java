package vandy.mooc.presenter;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericAsyncTask;
import vandy.mooc.common.GenericAsyncTaskOps;
import vandy.mooc.common.GenericPresenter;
import vandy.mooc.model.TrailerModel;
import vandy.mooc.model.aidl.TrailerData;

public class TrailerPresenter
        extends GenericPresenter<MVP.RequiredPresenterOps, MVP.ProvidedModelOps, TrailerModel>
        implements GenericAsyncTaskOps<String, Void, TrailerData>,
        MVP.ProvidedPresenterOps,
        MVP.RequiredPresenterOps {

    protected final static String TAG = TrailerPresenter.class.getSimpleName();
    private final Handler mDisplayHandler = new Handler();
    protected WeakReference<MVP.RequiredViewOps> mView;
    private GenericAsyncTask<String, Void, TrailerData, TrailerPresenter> mAsyncTask;
    private String mLocation;
    public TrailerPresenter() {
    }

    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        super.onCreate(TrailerModel.class, this);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        getModel().onDestroy(isChangingConfigurations);
    }

    public boolean getWeatherAsync(String location) {
        return getModel().getWeatherAsync(location);
    }

    public boolean getWeatherSync(String location) {
        if (mAsyncTask != null) return false;
        else {
            mAsyncTask = new GenericAsyncTask<>(this);
            mAsyncTask.execute(location);
            return true;
        }
    }

    public TrailerData doInBackground(String... locations) {
        mLocation = locations[0];
        return getModel().getWeatherSync(mLocation);
    }

    public void onPostExecute(TrailerData trailerData) {
        mView.get()
                .displayResults(trailerData,
                        "No weather data for location \"" + mLocation + "\" found");
        mAsyncTask = null;
    }

    public void displayResults(final TrailerData trailerData, final String reason) {
        mDisplayHandler.post(new Runnable() {
            public void run() {
                mView.get().displayResults(trailerData, reason);
            }
        });
    }

    @Override
    public Context getActivityContext() {
        return mView.get().getActivityContext();
    }

    @Override
    public Context getApplicationContext() {
        return mView.get().getApplicationContext();
    }
}
