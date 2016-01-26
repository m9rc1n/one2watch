package io.github.marcinn.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTimeoutCache<K, V> extends RefCounted implements TimeoutCache<K, V> {

    protected final String TAG = getClass().getSimpleName();
    private ConcurrentHashMap<K, CacheValues> mResults = new ConcurrentHashMap<>();
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void put(final K key, V value, int timeout) {
        final CacheValues cacheValues = new CacheValues(value);
        final Runnable cleanupCacheRunnable = new Runnable() {
            @Override
            public void run() {
                mResults.remove(key, cacheValues);
            }
        };

        CacheValues prevCacheValues = mResults.put(key, cacheValues);
        if (prevCacheValues != null) prevCacheValues.mFuture.cancel(true);
        ScheduledFuture<?> future = mScheduledExecutorService.schedule(cleanupCacheRunnable,
                timeout,
                TimeUnit.SECONDS);
        cacheValues.setFuture(future);
    }

    @Override
    public final V get(K key) {
        CacheValues cacheValues = mResults.get(key);
        return cacheValues != null ? cacheValues.mValue : null;
    }

    @Override
    public void remove(K key) {
        mResults.remove(key);
    }

    @Override
    public final int size() {
        return mResults.size();
    }

    @Override
    protected void close() {
        for (CacheValues cvs : mResults.values())
            if (cvs.mFuture != null) cvs.mFuture.cancel(true);
        mScheduledExecutorService.shutdownNow();
    }

    class CacheValues {

        final public V mValue;
        public ScheduledFuture<?> mFuture = null;

        public CacheValues(V value) {
            mValue = value;
        }

        public void setFuture(ScheduledFuture<?> future) {
            mFuture = future;
        }
    }
}
