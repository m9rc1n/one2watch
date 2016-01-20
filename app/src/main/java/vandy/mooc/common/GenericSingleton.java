package vandy.mooc.common;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class GenericSingleton {

    protected final static String TAG = GenericSingleton.class.getCanonicalName();
    private static final GenericSingleton sInstance = new GenericSingleton();
    @SuppressWarnings("rawtypes")
    private Map<Class, Object> mMap = new HashMap<>();

    private GenericSingleton() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T instance(Class<T> classOf) {
        synchronized (sInstance) {
            T t = (T) sInstance.mMap.get(classOf);
            if (t == null) {
                try {
                    t = classOf.newInstance();
                } catch (Exception e) {
                    Log.d(TAG, "GenericSingleton.instance() " + e);
                    t = null;
                }
                sInstance.mMap.put(classOf, t);
            }
            return t;
        }
    }

    public static <T> boolean remove(Class<T> classOf) {
        synchronized (sInstance) {
            if (sInstance.mMap.get(classOf) != null) {
                sInstance.mMap.put(classOf, null);
                return true;
            } else return false;
        }
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
