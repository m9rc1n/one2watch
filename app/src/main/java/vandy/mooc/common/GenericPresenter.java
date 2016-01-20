package vandy.mooc.common;

import android.util.Log;

public class GenericPresenter<RequiredPresenterOps, ProvidedModelOps, ModelType extends ModelOps<RequiredPresenterOps>> {

    protected final String TAG = getClass().getSimpleName();

    private ModelType mOpsInstance;

    public void onCreate(Class<ModelType> opsType, RequiredPresenterOps presenter) {
        try {
            initialize(opsType, presenter);
        } catch (InstantiationException
                | IllegalAccessException e) {
            Log.d(TAG, "handleConfiguration " + e);
            throw new RuntimeException(e);
        }
    }

    private void initialize(Class<ModelType> opsType, RequiredPresenterOps presenter)
            throws InstantiationException, IllegalAccessException {
        mOpsInstance = opsType.newInstance();
        mOpsInstance.onCreate(presenter);
    }

    @SuppressWarnings("unchecked")
    public ProvidedModelOps getModel() {
        return (ProvidedModelOps) mOpsInstance;
    }
}

