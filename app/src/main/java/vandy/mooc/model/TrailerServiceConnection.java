package vandy.mooc.model;

import android.content.ComponentName;
import android.os.IBinder;

import java.lang.ref.WeakReference;

import vandy.mooc.MVP;
import vandy.mooc.common.GenericServiceConnection;

public class TrailerServiceConnection<AIDLInterface extends android.os.IInterface>
        extends GenericServiceConnection<AIDLInterface> {

    private final WeakReference<MVP.RequiredPresenterOps> mPresenter;

    public TrailerServiceConnection(Class<AIDLInterface> aidl,
                                    WeakReference<MVP.RequiredPresenterOps> presenter) {
        super(aidl);
        mPresenter = presenter;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mPresenter.get().synchronizeAll();

    }
}
