package vandy.mooc.model.aidl;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vandy.mooc.model.aidl.WeatherData.Main;
import vandy.mooc.model.aidl.WeatherData.Sys;
import vandy.mooc.model.aidl.WeatherData.Weather;
import vandy.mooc.model.aidl.WeatherData.Wind;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of WeatherData objects that contain this data.
 */
public class WeatherDataJsonParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<WeatherData> parseJsonStream(InputStream inputStream) throws IOException {
        // TODO -- you fill in here.
        // Create a JsonReader for the inputStream.
        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
            // Log.d(TAG, "Parsing the results returned as an array");
            return parseJsonWeatherDataArray(reader);
        }
    }

    /**
     * Parse a Json stream and convert it into a List of WeatherData
     * objects.
     */
    public List<WeatherData> parseJsonWeatherDataArray(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        final List<WeatherData> list = new ArrayList<>();
        list.add(parseJsonWeatherData(reader));
        return list;
    }

    /**
     * Parse a Json stream and return a WeatherData object.
     */
    public WeatherData parseJsonWeatherData(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        final WeatherData data = new WeatherData();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case WeatherData.main_JSON:
                    data.setMain(parseMain(reader));
                    break;
                case WeatherData.sys_JSON:
                    data.setSys(parseSys(reader));
                    break;
                case WeatherData.wind_JSON:
                    data.setWind(parseWind(reader));
                    break;
                case WeatherData.cod_JSON:
                    data.setCod(reader.nextLong());
                    break;
                case WeatherData.dt_JSON:
                    data.setDate(reader.nextLong());
                    break;
                case WeatherData.message_JSON:
                    data.setMessage(reader.nextString());
                    break;
                case WeatherData.name_JSON:
                    data.setName(reader.nextString());
                    break;
                case WeatherData.weather_JSON:
                    if (reader.peek() == JsonToken.BEGIN_ARRAY)
                        data.setWeathers(parseWeathers(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return data;
    }

    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginArray();
        final List<Weather> list = new ArrayList<>();
        while (reader.hasNext()) list.add(parseWeather(reader));
        reader.endArray();
        return list;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        final Weather weather = new Weather();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Weather.icon_JSON:
                    weather.setIcon(reader.nextString());
                    break;
                case Weather.id_JSON:
                    weather.setId(reader.nextLong());
                    break;
                case Weather.description_JSON:
                    weather.setDescription(reader.nextString());
                    break;
                case Weather.main_JSON:
                    weather.setMain(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return weather;
    }

    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        final Main main = new Main();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Main.humidity_JSON:
                    main.setHumidity(reader.nextLong());
                    break;
                case Main.pressure_JSON:
                    main.setPressure(reader.nextDouble());
                    break;
                case Main.temp_JSON:
                    main.setTemp(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        final Wind wind = new Wind();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Wind.deg_JSON:
                    wind.setDeg(reader.nextDouble());
                    break;
                case Wind.speed_JSON:
                    wind.setSpeed(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        final Sys sys = new Sys();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Sys.country_JSON:
                    sys.setCountry(reader.nextString());
                    break;
                case Sys.message_JSON:
                    sys.setMessage(reader.nextDouble());
                    break;
                case Sys.sunrise_JSON:
                    sys.setSunrise(reader.nextLong());
                    break;
                case Sys.sunset_JSON:
                    sys.setSunset(reader.nextLong());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return sys;
    }
}
