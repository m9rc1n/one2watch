package vandy.mooc.model.aidl;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vandy.mooc.model.aidl.TrailerData.Embed;
import vandy.mooc.model.aidl.TrailerData.Movie;
import vandy.mooc.model.aidl.TrailerData.Weather;
import vandy.mooc.model.aidl.TrailerData.Thumb;

public class WeatherDataJsonParser {

    private final String TAG = this.getClass().getCanonicalName();

    public List<TrailerData> parseJsonStream(InputStream inputStream) throws IOException {

        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
            return parseJsonWeatherDataArray(reader);
        }
    }

    public List<TrailerData> parseJsonWeatherDataArray(JsonReader reader) throws IOException {
        final List<TrailerData> list = new ArrayList<>();
        list.add(parseJsonWeatherData(reader));
        return list;
    }

    public TrailerData parseJsonWeatherData(JsonReader reader) throws IOException {
        reader.beginObject();
        final TrailerData data = new TrailerData();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case TrailerData.embed_JSON:
                    data.setEmbed(parseMain(reader));
                    break;
                case TrailerData.movie_JSON:
                    data.setMovie(parseSys(reader));
                    break;
                case TrailerData.rating_JSON:
                    data.setThumb(parseWind(reader));
                    break;
                case TrailerData.title_JSON:
                    data.setCod(reader.nextLong());
                    break;
                case TrailerData.type_JSON:
                    data.setDate(reader.nextLong());
                    break;
                case TrailerData.message_JSON:
                    data.setMessage(reader.nextString());
                    break;
                case TrailerData.imdb_id_JSON:
                    data.setTitle(reader.nextString());
                    break;
                case TrailerData.weather_JSON:
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

    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        reader.beginArray();
        final List<Weather> list = new ArrayList<>();
        while (reader.hasNext()) list.add(parseWeather(reader));
        reader.endArray();
        return list;
    }

    public Weather parseWeather(JsonReader reader) throws IOException {
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

    public Embed parseMain(JsonReader reader) throws IOException {
        reader.beginObject();
        final Embed embed = new Embed();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Embed.humidity_JSON:
                    embed.setHumidity(reader.nextLong());
                    break;
                case Embed.pressure_JSON:
                    embed.setPressure(reader.nextDouble());
                    break;
                case TrailerData.Embed.flash_JSON:
                    embed.setTemp(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return embed;
    }

    public TrailerData.Thumb parseWind(JsonReader reader) throws IOException {
        reader.beginObject();
        final Thumb thumb = new TrailerData.Thumb();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case TrailerData.Thumb.small_JSON:
                    thumb.setDeg(reader.nextDouble());
                    break;
                case TrailerData.Thumb.large_JSON:
                    thumb.setSpeed(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return thumb;
    }

    public Movie parseSys(JsonReader reader) throws IOException {
        reader.beginObject();
        final Movie movie = new TrailerData.Movie();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case TrailerData.Movie.plot_JSON:
                    movie.setCountry(reader.nextString());
                    break;
                case Movie.title_JSON:
                    movie.setMessage(reader.nextDouble());
                    break;
                case TrailerData.Movie.imdbId_JSON:
                    movie.setSunrise(reader.nextLong());
                    break;
                case TrailerData.Movie.type_JSON:
                    movie.setSunset(reader.nextLong());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return movie;
    }
}
