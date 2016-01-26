package io.github.marcinn.common;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class RefCounted {

    protected final String TAG = getClass().getSimpleName();
    protected final AtomicInteger mRefCount = new AtomicInteger();
    public int getRefcount() {
        return mRefCount.get();
    }

    public final RefCounted incrementRefCount() {
        mRefCount.incrementAndGet();
        return this;
    }

    public int decrementRefCount() {
        int count = mRefCount.decrementAndGet();
        if (count == 0) close();
        return count;
    }

    protected abstract void close();
}
