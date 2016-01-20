package vandy.mooc.common;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class RefCounted {

    protected final String TAG = getClass().getSimpleName();
    protected final AtomicInteger mRefcount = new AtomicInteger();
    public int getRefcount() {
        return mRefcount.get();
    }

    public final RefCounted incrementRefCount() {
        mRefcount.incrementAndGet();
        return this;
    }

    public int decrementRefCount() {
        int count = mRefcount.decrementAndGet();
        if (count == 0) close();
        return count;
    }

    protected abstract void close();
}
