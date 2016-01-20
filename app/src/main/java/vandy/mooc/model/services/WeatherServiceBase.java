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
import vandy.mooc.model.aidl.WeatherDataJsonParser;

/**
 * This is the super class for both WeatherServiceSync and
 * WeatherServiceAsync.  It factors out fields and methods that are
 * shared by both Service implementations.
 */
public class WeatherServiceBase extends LifecycleLoggingService {
    /**
     * Appid needed to access the service.  TODO -- fill in with your Appid.
     */
    private final String mAppid = "86b0bb5b91086e42057b9ba0d4b14a0f";

    /**
     * URL to the Weather Service web service.
     */
    private String mWeatherServiceURL = "http://api.openweathermap.org/data/2.5/weather?&APPID=" + mAppid + "&q=";

    /**
     * Default timeout is 10 seconds, after which the Cache data
     * expires. In a production app this value should be much higher
     * (e.g., 10 minutes) - we keep it small here to help with
     * testing.
     */
    private int DEFAULT_CACHE_TIMEOUT = 10;

    /**
     * Hook method called when the Service is created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO -- you fill in here.
        GenericSingleton.instance(WeatherCache.class).incrementRefCount();
    }

    /**
     * Hook method called when the last client unbinds from the
     * Service.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO -- you fill in here.
        if (GenericSingleton.instance(WeatherCache.class).decrementRefCount() == 0)
            GenericSingleton.remove(WeatherCache.class);
    }

    /**
     * Contitionally queries the Weather Service web service to obtain
     * a List of TrailerData corresponding to the @a location if it's
     * been more than 10 seconds since the last query to the Weather
     * Service. Otherwise, simply return the cached results.
     */
    protected List<TrailerData> getWeatherResults(String location) {
        Log.d(TAG, "Looking up results in the cache for " + location);
        // TODO -- you fill in here.
        WeatherCache cache = GenericSingleton.instance(WeatherCache.class);

        List<TrailerData> data = cache.get(location);

        if (data != null) {
            return data;
        } else {
            List<TrailerData> results = getResultsFromWeatherService(location);
            if (results != null) {
                cache.put(location, results, DEFAULT_CACHE_TIMEOUT);
            }
            return results;
        }

    }

    /**
     * Actually query the Weather Service web service to get the
     * current TrailerData. Usually only returns a single element in
     * the List, but can return multiple elements if they are sent
     * back from the Weather Service.
     */
    private List<TrailerData> getResultsFromWeatherService(String location) {
        // Create a List that will return the TrailerData obtained
        // from the Weather Service web service.
        List<TrailerData> returnList = null;

        try {
            // Create a URL that points to desired location the
            // Weather Service.
            URL url = new URL(mWeatherServiceURL + location);
            final URI uri = new URI(url.getProtocol(),
                    url.getUserInfo(),
                    url.getHost(),
                    url.getPort(),
                    url.getPath(),
                    url.getQuery(),
                    url.getRef());
            url = uri.toURL();

            // Opens a connection to the Weather Service.
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Sends the GET request and returns a stream containing
            // the Json results.
            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                // Create the parser.
                final WeatherDataJsonParser parser = new WeatherDataJsonParser();

                // Parse the Json results and create List of
                // TrailerData objects.
                returnList = parser.parseJsonStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // See if we parsed any valid data.
        if (returnList != null && returnList.size() > 0 && returnList.get(0).getMessage() == null) {

            // Return the List of TrailerData.
            return returnList;
        } else {
            Log.d(TAG, returnList.get(0).getMessage() + " \"" + location + "\"");

            return null;
        }
    }

    /**
     * Define a class that will cache the TrailerData since it doesn't
     * change rapidly. This class is passed to the
     * GenericSingleton.instance() method to retrieve the one and only
     * instance of the WeatherCache.
     */
    public static class WeatherCache
            extends ExecutorServiceTimeoutCache<String, List<TrailerData>> {
    }
}
