package vandy.mooc.presenter;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.List;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericAsyncTask;
import vandy.mooc.common.GenericAsyncTaskOps;
import vandy.mooc.common.GenericPresenter;
import vandy.mooc.model.TrailerModel;
import vandy.mooc.model.aidl.TrailerData;

public class TrailerPresenter
        extends GenericPresenter<MVP.RequiredPresenterOps, MVP.ProvidedModelOps, TrailerModel>
        implements GenericAsyncTaskOps<String, Void, List<TrailerData>>,
        MVP.ProvidedPresenterOps,
        MVP.RequiredPresenterOps {

    protected final static String TAG = TrailerPresenter.class.getSimpleName();
    private final Handler mDisplayHandler = new Handler();
    protected WeakReference<MVP.RequiredViewOps> mView;
    private GenericAsyncTask<String, Void, List<TrailerData>, TrailerPresenter> mAsyncTask;
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

    public List<TrailerData> doInBackground(String... locations) {
        mLocation = locations[0];
        return getModel().getWeatherSync(mLocation);
    }

    public void onPostExecute(List<TrailerData> results) {
        mView.get().displayResults(results, "No weather data for location found");
        mAsyncTask = null;
    }

    public void displayResults(final List<TrailerData> results, final String reason) {
        mDisplayHandler.post(new Runnable() {
            public void run() {
                mView.get().displayResults(results, reason);
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
