package vandy.mooc.presenter;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.List;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericAsyncTaskOps;
import vandy.mooc.common.GenericPresenter;
import vandy.mooc.model.TrailerAsyncTask;
import vandy.mooc.model.TrailerModel;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerType;

public class TrailerPresenter
        extends GenericPresenter<MVP.RequiredPresenterOps, MVP.ProvidedModelOps, TrailerModel>
        implements GenericAsyncTaskOps<String, Void, List<TrailerData>>,
        MVP.ProvidedPresenterOps,
        MVP.RequiredPresenterOps {

    protected final static String TAG = TrailerPresenter.class.getSimpleName();
    private final Handler mDisplayHandler = new Handler();
    protected WeakReference<MVP.RequiredViewOps> mView;
    private TrailerAsyncTask<String, Void, List<TrailerData>, TrailerPresenter> mAsyncTask;
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

    public List<TrailerData> doInBackground(String... locations) {
        if (locations == null || locations.length == 0) {
            return getModel().getTrailersSync(mAsyncTask.getType());
        } else {
            mLocation = locations[0];
            return getModel().getTrailersSync(mLocation, mAsyncTask.getType());
        }
    }

    public void onPostExecute(List<TrailerData> results) {
        mView.get()
                .displayResults(results,
                        "No weather data for location found",
                        mAsyncTask.getType());
        mAsyncTask = null;
    }

    @Override
    public Context getActivityContext() {
        return mView.get().getActivityContext();
    }

    @Override
    public Context getApplicationContext() {
        return mView.get().getApplicationContext();
    }

    @Override
    public boolean getTrailersAsync(String query, TrailerType type) {
        return getModel().getTrailersAsync(query, type);
    }

    @Override
    public boolean getTrailersAsync(TrailerType type) {
        return getModel().getTrailersAsync(type);
    }

    @Override
    public boolean getTrailersSync(String query, TrailerType type) {

        if (mAsyncTask != null) return false;
        else {
            mAsyncTask = new TrailerAsyncTask<>(this, type);
            mAsyncTask.execute(query);
            return true;
        }
    }

    @Override
    public boolean getTrailersSync(TrailerType type) {

        if (mAsyncTask != null) return false;
        else {
            mAsyncTask = new TrailerAsyncTask<>(this, type);
            mAsyncTask.execute();
            return true;
        }
    }

    @Override
    public void displayResults(final List<TrailerData> results,
                               final String reason,
                               final TrailerType type) {
        mDisplayHandler.post(new Runnable() {
            public void run() {
                mView.get().displayResults(results, reason, type);
            }
        });
    }
}
