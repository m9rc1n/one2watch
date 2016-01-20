package vandy.mooc.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericServiceConnection<AIDLInterface extends android.os.IInterface>
        implements ServiceConnection {

    private static final String STUB = "Stub";
    private static final String AS_INTERFACE = "asInterface";
    private static final Class<?>[] AI_PARAMS = {IBinder.class};
    private final Class<?> mStub;
    private final Method mAsInterface;
    private AIDLInterface mInterface;

    public GenericServiceConnection(final Class<AIDLInterface> aidl) {
        Class<?> stub = null;
        Method method = null;
        for (final Class<?> c : aidl.getDeclaredClasses()) {
            if (c.getSimpleName().equals(STUB)) {
                try {
                    stub = c;
                    method = stub.getMethod(AS_INTERFACE, AI_PARAMS);
                    break;
                } catch (final NoSuchMethodException e) { // Should not be possible
                    e.printStackTrace();
                }
            }
        }
        mStub = stub;
        mAsInterface = method;
    }

    public AIDLInterface getInterface() {
        return mInterface;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            mInterface = (AIDLInterface) mAsInterface.invoke(mStub, service);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mInterface = null;
    }
}
