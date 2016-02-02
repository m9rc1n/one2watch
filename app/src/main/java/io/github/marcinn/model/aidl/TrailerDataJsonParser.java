package io.github.marcinn.model.aidl;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.github.marcinn.model.aidl.TrailerData.Embed;
import io.github.marcinn.model.aidl.TrailerData.Movie;
import io.github.marcinn.model.aidl.TrailerData.Thumb;

import static io.github.marcinn.model.aidl.TrailerData.Html5;
import static io.github.marcinn.model.aidl.TrailerData.duration_JSON;
import static io.github.marcinn.model.aidl.TrailerData.embed_JSON;
import static io.github.marcinn.model.aidl.TrailerData.id_JSON;
import static io.github.marcinn.model.aidl.TrailerData.imdb_id_JSON;
import static io.github.marcinn.model.aidl.TrailerData.link_JSON;
import static io.github.marcinn.model.aidl.TrailerData.movie_JSON;
import static io.github.marcinn.model.aidl.TrailerData.rating_JSON;
import static io.github.marcinn.model.aidl.TrailerData.thumb_JSON;
import static io.github.marcinn.model.aidl.TrailerData.title_JSON;
import static io.github.marcinn.model.aidl.TrailerData.type_JSON;

public class TrailerDataJsonParser {

    private final String TAG = this.getClass().getCanonicalName();

    public List<TrailerData> parseJsonStream(InputStream inputStream) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            return parseJsonTrailerDataArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<TrailerData> parseJsonTrailerDataArray(JsonReader reader) throws IOException {
        final List<TrailerData> list = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                TrailerData object = parseJsonTrailerData(reader);
                list.add(object);
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private TrailerData parseJsonTrailerData(JsonReader reader) throws IOException {
        reader.beginObject();
        final TrailerData data = new TrailerData();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case id_JSON:
                    data.setId(reader.nextLong());
                    break;
                case embed_JSON:
                    data.setEmbed(parseEmbed(reader));
                    break;
                case movie_JSON:
                    data.setMovie(parseMovie(reader));
                    break;
                case thumb_JSON:
                    data.setThumb(parseThumb(reader));
                    break;
                case title_JSON:
                    data.setTitle(reader.nextString());
                    break;
                case type_JSON:
                    data.setType(reader.nextString());
                    break;
                case duration_JSON:
                    data.setDuration(reader.nextLong());
                    break;
                case imdb_id_JSON:
                    data.setImdbId(reader.nextString());
                    break;
                case rating_JSON:
                    data.setRating(reader.nextString());
                    break;
                case link_JSON:
                    data.setLink(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return data;
    }

    private Embed parseEmbed(JsonReader reader) throws IOException {
        reader.beginObject();
        final Embed embed = new Embed();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Embed.flash_JSON:
                    embed.setFlash(reader.nextString());
                    break;
                case Embed.iframe_JSON:
                    embed.setIframe(reader.nextString());
                    break;
                case Embed.html5_JSON:
                    embed.setHtml5(parseHtml5(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return embed;
    }

    private Html5 parseHtml5(JsonReader reader) throws IOException {
        reader.beginObject();
        final Html5 html5 = new Html5();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Html5._360p_JSON:
                    html5.set360p(reader.nextString());
                    break;
                case Html5._480p_JSON:
                    html5.set480p(reader.nextString());
                    break;
                case Html5._720p_JSON:
                    html5.set720p(reader.nextString());
                    break;
                case Html5._1080p_JSON:
                    html5.set1080p(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return html5;
    }

    private Thumb parseThumb(JsonReader reader) throws IOException {
        reader.beginObject();
        final Thumb thumb = new Thumb();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Thumb.small_JSON:
                    thumb.setSmall(reader.nextString());
                    break;
                case Thumb.large_JSON:
                    thumb.setLarge(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return thumb;
    }

    private Movie parseMovie(JsonReader reader) throws IOException {
        reader.beginObject();
        final Movie movie = new Movie();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Movie.id_JSON:
                    movie.setId(reader.nextLong());
                    break;
                case Movie.title_JSON:
                    movie.setTitle(reader.nextString());
                    break;
                case Movie.plot_JSON:
                    movie.setPlot(reader.nextString());
                    break;
                case Movie.imdbId_JSON:
                    movie.setImdbId(reader.nextString());
                    break;
                case Movie.tmdbId_JSON:
                    movie.setTmdbId(reader.nextLong());
                    break;
                case Movie.type_JSON:
                    movie.setType(reader.nextString());
                    break;
                case Movie.year_JSON:
                    movie.setYear(reader.nextInt());
                    break;
                case Movie.rating_JSON:
                    movie.setType(reader.nextString());
                    break;
                case Movie.imdbRating_JSON:
                    movie.setType(reader.nextString());
                    break;
                case Movie.genre_JSON:
                    movie.setGenre(reader.nextString());
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
