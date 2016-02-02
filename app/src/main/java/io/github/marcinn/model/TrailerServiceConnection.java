package io.github.marcinn.model;

import android.content.ComponentName;
import android.os.IBinder;

import java.lang.ref.WeakReference;

import io.github.marcinn.MVP;
import io.github.marcinn.common.GenericServiceConnection;

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
