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

            assertEquals(data_0.getId(), 3974);
            assertEquals(data_0.getTitle(), "Trailer");
            assertEquals(data_0.getImdbId(), "1646971");
            assertEquals(data_0.getType(), "trailer");
            assertEquals(data_0.getRating(), "Not Set");
            assertEquals(data_0.getDuration(), 118);
            assertEquals(data_0.getThumb().getSmall(),
                    "https://cdn2.themovieclips.com/images/1646971/trailer/small_screen_1.jpg");
            assertEquals(data_0.getThumb().getLarge(),
                    "https://cdn1.themovieclips.com/images/1646971/trailer/screen_1.jpg");
            assertEquals(data_0.getEmbed().getFlash(),
                    "https://www.themovieclips.com/mediafiles/player/v1/player.swf?v=3974");
            assertEquals(data_0.getEmbed().getIframe(),
                    "<iframe width='656' height='369' src=https://www.themovieclips.com/embed/3974' frameborder='0' allowfullscreen></iframe>");
            assertEquals(data_0.getEmbed().getHtml5().get360p(),
                    "https://stream.themovieclips.com/360/1646971/trailer/howtotrainyourdragon.mp4");
            assertEquals(data_0.getEmbed().getHtml5().get480p(),
                    "https://stream.themovieclips.com/480/1646971/trailer/howtotrainyourdragon.mp4");
            assertEquals(data_0.getEmbed().getHtml5().get720p(),
                    "https://stream.themovieclips.com/720/1646971/trailer/howtotrainyourdragon.mp4");
            assertEquals(data_0.getEmbed().getHtml5().get1080p(),
                    "https://stream.themovieclips.com/1080/1646971/trailer/howtotrainyourdragon.mp4");
            assertEquals(data_0.getLink(),
                    "https://www.themovieclips.com/trailer-how-to-train-your-dragon-2");
            assertEquals(data_0.getMovie().getId(), 10944);
            assertEquals(data_0.getMovie().getTitle(), "How to Train Your Dragon 2");
            assertEquals(data_0.getMovie().getPlot(),
                    "When Hiccup and Toothless discover an ice cave that is home to hundreds of new wild dragons and the mysterious Dragon Rider, the two friends find themselves at the center of a battle to protect the peace.");
            assertEquals(data_0.getMovie().getImdbId(), "1646971");
            assertEquals(data_0.getMovie().getTmdbId(), 82702);
            assertEquals(data_0.getMovie().getType(), "feature");
            assertEquals(data_0.getMovie().getYear(), 2014);
            assertEquals(data_0.getMovie().getRating(), "PG");
            assertEquals(data_0.getMovie().getImdbRating(), "7.9");
            assertEquals(data_0.getMovie().getGenre(), "Fantasy");
            assertEquals(data_0.getMovie().getPosters().getId(), 3746);
            assertEquals(data_0.getMovie().getPosters().getImdbId(), "1646971");
            assertEquals(data_0.getMovie().getPosters().getFullSize(),
                    "https://cdn2.themovieclips.com/posters/1646971/poster_1.jpg");
            assertEquals(data_0.getMovie().getPosters().getThumb(),
                    "https://cdn4.themovieclips.com/posters/1646971/poster_1_thumb.jpg");

            TrailerData data_1 = returnList.get(1);

            assertEquals(data_1.getId(), 4296);
            assertEquals(data_1.getTitle(), "Trailer");
            assertEquals(data_1.getImdbId(), "2004420");
            assertEquals(data_1.getType(), "trailer");
            assertEquals(data_1.getRating(), "Not Set");
            assertEquals(data_1.getDuration(), 101);
            assertEquals(data_1.getThumb().getSmall(),
                    "https://cdn1.themovieclips.com/images/2004420/trailer/small_screen_1.jpg");
            assertEquals(data_1.getThumb().getLarge(),
                    "https://cdn2.themovieclips.com/images/2004420/trailer/screen_1.jpg");
            assertEquals(data_1.getEmbed().getFlash(),
                    "https://www.themovieclips.com/mediafiles/player/v1/player.swf?v=4296");
            assertEquals(data_1.getEmbed().getIframe(),
                    "<iframe width='656' height='369' src=https://www.themovieclips.com/embed/4296' frameborder='0' allowfullscreen></iframe>");
            assertEquals(data_1.getEmbed().getHtml5().get360p(),
                    "https://stream.themovieclips.com/360/2004420/trailer/neighbors.mp4");
            assertEquals(data_1.getEmbed().getHtml5().get480p(),
                    "https://stream.themovieclips.com/480/2004420/trailer/neighbors.mp4");
            assertEquals(data_1.getEmbed().getHtml5().get720p(),
                    "https://stream.themovieclips.com/720/2004420/trailer/neighbors.mp4");
            assertEquals(data_1.getEmbed().getHtml5().get1080p(),
                    "https://stream.themovieclips.com/1080/2004420/trailer/neighbors.mp4");
            assertEquals(data_1.getLink(), "https://www.themovieclips.com/trailer-neighbors");
            assertEquals(data_1.getMovie().getId(), 11230);
            assertEquals(data_1.getMovie().getTitle(), "Neighbors");
            assertEquals(data_1.getMovie().getPlot(),
                    "After they are forced to live next to a fraternity house, a couple with a newborn baby do whatever they can to take them down.");
            assertEquals(data_1.getMovie().getImdbId(), "2004420");
            assertEquals(data_1.getMovie().getTmdbId(), 195589);
            assertEquals(data_1.getMovie().getType(), "feature");
            assertEquals(data_1.getMovie().getYear(), 2014);
            assertEquals(data_1.getMovie().getRating(), "R");
            assertEquals(data_1.getMovie().getImdbRating(), "6.4");
            assertEquals(data_1.getMovie().getGenre(), "Comedy");
            assertEquals(data_1.getMovie().getPosters().getId(), 3788);
            assertEquals(data_1.getMovie().getPosters().getImdbId(), "2004420");
            assertEquals(data_1.getMovie().getPosters().getFullSize(),
                    "https://cdn3.themovieclips.com/posters/2004420/poster_1.jpg");
            assertEquals(data_1.getMovie().getPosters().getThumb(),
                    "https://cdn0.themovieclips.com/posters/2004420/poster_1_thumb.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
