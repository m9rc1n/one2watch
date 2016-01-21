package vandy.mooc.common;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public abstract class GenericActivity<RequiredViewOps, ProvidedPresenterOps, PresenterType extends PresenterOps<RequiredViewOps>>
        extends AppCompatActivity implements ContextView {

    private static final String TAG = GenericActivity.class.getName();

    private final RetainedFragmentManager mRetainedFragmentManager = new RetainedFragmentManager(
            this.getFragmentManager(),
            TAG);

    private PresenterType mPresenterInstance;

    public void onCreate(Class<PresenterType> opsType, RequiredViewOps view) {

        try {
            if (mRetainedFragmentManager.firstTimeIn()) {
                initialize(opsType, view);
            } else {
                reinitialize(opsType, view);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public ProvidedPresenterOps getPresenter() {
        return (ProvidedPresenterOps) mPresenterInstance;
    }

    public RetainedFragmentManager getRetainedFragmentManager() {
        return mRetainedFragmentManager;
    }

    private void initialize(Class<PresenterType> opsType, RequiredViewOps view)
            throws InstantiationException, IllegalAccessException {
        mPresenterInstance = opsType.newInstance();
        mRetainedFragmentManager.put(opsType.getSimpleName(), mPresenterInstance);
        mPresenterInstance.onCreate(view);
    }

    private void reinitialize(Class<PresenterType> opsType, RequiredViewOps view)
            throws InstantiationException, IllegalAccessException {
        mPresenterInstance = mRetainedFragmentManager.get(opsType.getSimpleName());
        if (mPresenterInstance == null) initialize(opsType, view);
        else mPresenterInstance.onConfigurationChange(view);
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}

