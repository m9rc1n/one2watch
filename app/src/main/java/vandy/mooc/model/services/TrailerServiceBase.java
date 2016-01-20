package vandy.mooc.model.services;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import vandy.mooc.common.ExecutorServiceTimeoutCache;
import vandy.mooc.common.GenericSingleton;
import vandy.mooc.common.LifecycleLoggingService;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.model.aidl.TrailerDataJsonParser;

public class TrailerServiceBase extends LifecycleLoggingService {

    private final String mAppId = "kEm5QgSMTNmshZsKR2thZixJaPNxp1JfEsojsnojdBmMWBRgyD";

    private String mTrailerServiceURL = "https://themovieclips.p.mashape.com/popular";

    private int DEFAULT_CACHE_TIMEOUT = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        GenericSingleton.instance(TrailerCache.class).incrementRefCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (GenericSingleton.instance(TrailerCache.class).decrementRefCount() == 0)
            GenericSingleton.remove(TrailerCache.class);
    }

    protected List<TrailerData> getTrailerResults(String location) {
        Log.d(TAG, "Looking up results in the cache for " + location);
        TrailerCache cache = GenericSingleton.instance(TrailerCache.class);
        List<TrailerData> data = cache.get(location);
        if (data != null) {
            return data;
        } else {
            List<TrailerData> results = getResultsFromTrailerService(location);
            if (results != null) {
                cache.put(location, results, DEFAULT_CACHE_TIMEOUT);
            }
            return results;
        }
    }

    private List<TrailerData> getResultsFromTrailerService(String location) {
        List<TrailerData> returnList = null;
        try {
            URL url = new URL(mTrailerServiceURL);
            final URI uri = new URI(url.getProtocol(),
                    url.getUserInfo(),
                    url.getHost(),
                    url.getPort(),
                    url.getPath(),
                    url.getQuery(),
                    url.getRef());
            url = uri.toURL();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Mashape-Key", mAppId);
            urlConnection.setRequestProperty("Accept", "application/json");
            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                final TrailerDataJsonParser parser = new TrailerDataJsonParser();
                returnList = parser.parseJsonStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static class TrailerCache
            extends ExecutorServiceTimeoutCache<String, List<TrailerData>> {
    }
}
