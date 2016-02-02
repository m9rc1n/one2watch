package io.github.marcinn;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.marcinn.model.aidl.TrailerData;
import io.github.marcinn.model.aidl.TrailerDataJsonParser;
import io.github.marcinn.view.MainActivity;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrailerDataJsonParserTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldTrailerJsonParserReturnCorrectStructure() {
        InputStream stream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("popular_trailers.json");

        final TrailerDataJsonParser parser = new TrailerDataJsonParser();
        try {
            List<TrailerData> returnList = parser.parseJsonStream(stream);
            assertEquals(2, returnList.size());

            TrailerData data_0 = returnList.get(0);

            assertEquals(3974, data_0.getId());
            assertEquals("Trailer", data_0.getTitle());
            assertEquals("1646971", data_0.getImdbId());
            assertEquals("trailer", data_0.getType());
            assertEquals("Not Set", data_0.getRating());
            assertEquals(118, data_0.getDuration());
            assertEquals("https://cdn2.themovieclips.com/images/1646971/trailer/small_screen_1.jpg",
                    data_0.getThumb().getSmall());
            assertEquals("https://cdn1.themovieclips.com/images/1646971/trailer/screen_1.jpg",
                    data_0.getThumb().getLarge());
            assertEquals("https://www.themovieclips.com/mediafiles/player/v1/player.swf?v=3974",
                    data_0.getEmbed().getFlash());
            assertEquals(
                    "<iframe width='656' height='369' src=https://www.themovieclips.com/embed/3974' frameborder='0' allowfullscreen></iframe>",
                    data_0.getEmbed().getIframe());
            assertEquals(
                    "https://stream.themovieclips.com/360/1646971/trailer/howtotrainyourdragon.mp4",
                    data_0.getEmbed().getHtml5().get360p());
            assertEquals(
                    "https://stream.themovieclips.com/480/1646971/trailer/howtotrainyourdragon.mp4",
                    data_0.getEmbed().getHtml5().get480p());
            assertEquals(
                    "https://stream.themovieclips.com/720/1646971/trailer/howtotrainyourdragon.mp4",
                    data_0.getEmbed().getHtml5().get720p());
            assertEquals(
                    "https://stream.themovieclips.com/1080/1646971/trailer/howtotrainyourdragon.mp4",
                    data_0.getEmbed().getHtml5().get1080p());
            assertEquals("https://www.themovieclips.com/trailer-how-to-train-your-dragon-2",
                    data_0.getLink());
            assertEquals(10944, data_0.getMovie().getId());
            assertEquals("How to Train Your Dragon 2", data_0.getMovie().getTitle());
            assertEquals(
                    "When Hiccup and Toothless discover an ice cave that is home to hundreds of new wild dragons and the mysterious Dragon Rider, the two friends find themselves at the center of a battle to protect the peace.",
                    data_0.getMovie().getPlot());
            assertEquals("1646971", data_0.getMovie().getImdbId());
            assertEquals(82702, data_0.getMovie().getTmdbId());
            assertEquals("feature", data_0.getMovie().getType());
            assertEquals(2014, data_0.getMovie().getYear());
            assertEquals("PG", data_0.getMovie().getRating());
            assertEquals("7.9", data_0.getMovie().getImdbRating());
            assertEquals("Fantasy", data_0.getMovie().getGenre());
            assertEquals(3746, data_0.getMovie().getPosters().getId());
            assertEquals("1646971", data_0.getMovie().getPosters().getImdbId());
            assertEquals("https://cdn2.themovieclips.com/posters/1646971/poster_1.jpg",
                    data_0.getMovie().getPosters().getFullSize());
            assertEquals("https://cdn4.themovieclips.com/posters/1646971/poster_1_thumb.jpg",
                    data_0.getMovie().getPosters().getThumb());

            TrailerData data_1 = returnList.get(1);

            assertEquals(4296, data_1.getId());
            assertEquals("Trailer", data_1.getTitle());
            assertEquals("2004420", data_1.getImdbId());
            assertEquals("trailer", data_1.getType());
            assertEquals("Not Set", data_1.getRating());
            assertEquals(101, data_1.getDuration());
            assertEquals("https://cdn1.themovieclips.com/images/2004420/trailer/small_screen_1.jpg",
                    data_1.getThumb().getSmall());
            assertEquals("https://cdn2.themovieclips.com/images/2004420/trailer/screen_1.jpg",
                    data_1.getThumb().getLarge());
            assertEquals("https://www.themovieclips.com/mediafiles/player/v1/player.swf?v=4296",
                    data_1.getEmbed().getFlash());
            assertEquals(
                    "<iframe width='656' height='369' src=https://www.themovieclips.com/embed/4296' frameborder='0' allowfullscreen></iframe>",
                    data_1.getEmbed().getIframe());
            assertEquals("https://stream.themovieclips.com/360/2004420/trailer/neighbors.mp4",
                    data_1.getEmbed().getHtml5().get360p());
            assertEquals("https://stream.themovieclips.com/480/2004420/trailer/neighbors.mp4",
                    data_1.getEmbed().getHtml5().get480p());
            assertEquals("https://stream.themovieclips.com/720/2004420/trailer/neighbors.mp4",
                    data_1.getEmbed().getHtml5().get720p());
            assertEquals("https://stream.themovieclips.com/1080/2004420/trailer/neighbors.mp4",
                    data_1.getEmbed().getHtml5().get1080p());
            assertEquals("https://www.themovieclips.com/trailer-neighbors", data_1.getLink());
            assertEquals(11230, data_1.getMovie().getId());
            assertEquals("Neighbors", data_1.getMovie().getTitle());
            assertEquals(
                    "After they are forced to live next to a fraternity house, a couple with a newborn baby do whatever they can to take them down.",
                    data_1.getMovie().getPlot());
            assertEquals("2004420", data_1.getMovie().getImdbId());
            assertEquals(195589, data_1.getMovie().getTmdbId());
            assertEquals("feature", data_1.getMovie().getType());
            assertEquals(2014, data_1.getMovie().getYear());
            assertEquals("R", data_1.getMovie().getRating());
            assertEquals("6.4", data_1.getMovie().getImdbRating());
            assertEquals("Comedy", data_1.getMovie().getGenre());
            assertEquals(3788, data_1.getMovie().getPosters().getId());
            assertEquals("2004420", data_1.getMovie().getPosters().getImdbId());
            assertEquals("https://cdn3.themovieclips.com/posters/2004420/poster_1.jpg",
                    data_1.getMovie().getPosters().getFullSize());
            assertEquals("https://cdn0.themovieclips.com/posters/2004420/poster_1_thumb.jpg",
                    data_1.getMovie().getPosters().getThumb());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
