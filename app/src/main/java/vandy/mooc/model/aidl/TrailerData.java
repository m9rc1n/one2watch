package vandy.mooc.model.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerData implements Parcelable {

    public static final String TYPE_TRAILER = "parcelable/trailer";
    public static final String KEY_TRAILER_DATA = "trailerList";
    public static final String id_JSON = "id";
    public static final String title_JSON = "title";
    public static final String imdb_id_JSON = "imdb_id";
    public static final String type_JSON = "type";
    public static final String rating_JSON = "rating";
    public static final String duration_JSON = "duration";
    public static final String thumb_JSON = "thumb";
    public static final String embed_JSON = "embed";
    public static final String link_JSON = "link";
    public static final String movie_JSON = "movie";

    public static final Parcelable.Creator<TrailerData> CREATOR = new Parcelable.Creator<TrailerData>() {
        public TrailerData createFromParcel(Parcel in) {
            return new TrailerData(in);
        }

        public TrailerData[] newArray(int size) {
            return new TrailerData[size];
        }
    };

    private long mId;
    private String mTitle;
    private String mImdbId;
    private String mType;
    private String mRating;
    private long mDuration;
    private Thumb mThumb;
    private Embed mEmbed;
    private String mLink;
    private Movie mMovie;

    public TrailerData(long mId,
                       String mTitle,
                       String mImdbId,
                       String mType,
                       String mRating,
                       long mDuration,
                       Thumb mThumb,
                       Embed mEmbed,
                       String mLink,
                       Movie mMovie) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImdbId = mImdbId;
        this.mType = mType;
        this.mRating = mRating;
        this.mDuration = mDuration;
        this.mThumb = mThumb;
        this.mEmbed = mEmbed;
        this.mLink = mLink;
        this.mMovie = mMovie;
    }

    public TrailerData() {
    }

    private TrailerData(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mImdbId = in.readString();
        mType = in.readString();
        mRating = in.readString();
        mDuration = in.readLong();
        mThumb = new Thumb(in.readString(), in.readString());
        mEmbed = new Embed(in.readString(),
                in.readString(),
                new Html5(in.readString(), in.readString(), in.readString(), in.readString()));
        mLink = in.readString();
        mMovie = new Movie(in.readLong(),
                in.readString(),
                in.readString(),
                in.readString(),
                in.readLong(),
                in.readString(),
                in.readInt(),
                in.readString(),
                in.readString(),
                in.readString());
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getImdbId() {
        return mImdbId;
    }

    public void setImdbId(String mImdbId) {
        this.mImdbId = mImdbId;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String mRating) {
        this.mRating = mRating;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public Thumb getThumb() {
        return mThumb;
    }

    public void setThumb(Thumb mThumb) {
        this.mThumb = mThumb;
    }

    public Embed getEmbed() {
        return mEmbed;
    }

    public void setEmbed(Embed mEmbed) {
        this.mEmbed = mEmbed;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie mMovie) {
        this.mMovie = mMovie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mImdbId);
        dest.writeString(mType);
        dest.writeString(mRating);
        dest.writeLong(mDuration);
        dest.writeString(mThumb.getSmall());
        dest.writeString(mThumb.getLarge());
        dest.writeString(mEmbed.getFlash());
        dest.writeString(mEmbed.getIframe());
        dest.writeString(mEmbed.getHtml5().get360p());
        dest.writeString(mEmbed.getHtml5().get480p());
        dest.writeString(mEmbed.getHtml5().get720p());
        dest.writeString(mEmbed.getHtml5().get1080p());
        dest.writeString(mLink);
        dest.writeLong(mMovie.getId());
        dest.writeString(mMovie.getTitle());
        dest.writeString(mMovie.getPlot());
        dest.writeString(mMovie.getImdbId());
        dest.writeLong(mMovie.getTmdbId());
        dest.writeString(mMovie.getType());
        dest.writeInt(mMovie.getYear());
        dest.writeString(mMovie.getRating());
        dest.writeString(mMovie.getImdbRating());
        dest.writeString(mMovie.getGenre());
    }

    public static class Movie {

        public final static String id_JSON = "id";
        public final static String title_JSON = "title";
        public final static String plot_JSON = "plot";
        public final static String imdbId_JSON = "imdbId";
        public final static String tmdbId_JSON = "tmdbId";
        public final static String type_JSON = "type";
        public final static String year_JSON = "year";
        public final static String rating_JSON = "rating";
        public final static String imdbRating_JSON = "imdbRating";
        public final static String genre_JSON = "genre";

        private long mId;
        private String mTitle;
        private String mPlot;
        private String mImdbId;
        private long mTmdbId;
        private String mType;
        private int mYear;
        private String mRating;
        private String mImdbRating;
        private String mGenre;

        public Movie(long mId,
                     String mTitle,
                     String mPlot,
                     String mImdbId,
                     long mTmdbId,
                     String mType,
                     int mYear,
                     String mRating,
                     String mImdbRating,
                     String mGenre) {
            this.mId = mId;
            this.mTitle = mTitle;
            this.mPlot = mPlot;
            this.mImdbId = mImdbId;
            this.mTmdbId = mTmdbId;
            this.mType = mType;
            this.mYear = mYear;
            this.mRating = mRating;
            this.mImdbRating = mImdbRating;
            this.mGenre = mGenre;
        }

        public Movie() {
        }


        public long getId() {
            return mId;
        }

        public void setId(long mId) {
            this.mId = mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getPlot() {
            return mPlot;
        }

        public void setPlot(String mPlot) {
            this.mPlot = mPlot;
        }

        public String getImdbId() {
            return mImdbId;
        }

        public void setImdbId(String mImdbId) {
            this.mImdbId = mImdbId;
        }

        public long getTmdbId() {
            return mTmdbId;
        }

        public void setTmdbId(long mTmdbId) {
            this.mTmdbId = mTmdbId;
        }

        public String getType() {
            return mType;
        }

        public void setType(String mType) {
            this.mType = mType;
        }

        public int getYear() {
            return mYear;
        }

        public void setYear(int mYear) {
            this.mYear = mYear;
        }

        public String getRating() {
            return mRating;
        }

        public void setRating(String mRating) {
            this.mRating = mRating;
        }

        public String getImdbRating() {
            return mImdbRating;
        }

        public void setImdbRating(String mImdbRating) {
            this.mImdbRating = mImdbRating;
        }

        public String getGenre() {
            return mGenre;
        }

        public void setGenre(String mGenre) {
            this.mGenre = mGenre;
        }
    }

    public static class Embed {
        public final static String flash_JSON = "flash";
        public final static String iframe_JSON = "iframe";
        public final static String html5_JSON = "html5";

        private String mFlash;
        private String mIframe;
        private Html5 mHtml5;

        public Embed(String mFlash, String mIframe, Html5 mHtml5) {
            this.mFlash = mFlash;
            this.mIframe = mIframe;
            this.mHtml5 = mHtml5;
        }

        public Embed() {
        }

        public String getFlash() {
            return mFlash;
        }

        public void setFlash(String mFlash) {
            this.mFlash = mFlash;
        }

        public String getIframe() {
            return mIframe;
        }

        public void setIframe(String mIframe) {
            this.mIframe = mIframe;
        }

        public Html5 getHtml5() {
            return mHtml5;
        }

        public void setHtml5(Html5 mHtml5) {
            this.mHtml5 = mHtml5;
        }
    }

    public static class Html5 {

        public final static String _360p_JSON = "360p";
        public final static String _480p_JSON = "480p";
        public final static String _720p_JSON = "720p";
        public final static String _1080p_JSON = "1080p";

        private String m360p;
        private String m480p;
        private String m720p;
        private String m1080p;

        public Html5(String m360p, String m480p, String m720p, String m1080p) {
            this.m360p = m360p;
            this.m480p = m480p;
            this.m720p = m720p;
            this.m1080p = m1080p;
        }

        public Html5() {
        }

        public String get360p() {
            return m360p;
        }

        public void set360p(String m360p) {
            this.m360p = m360p;
        }

        public String get480p() {
            return m480p;
        }

        public void set480p(String m480p) {
            this.m480p = m480p;
        }

        public String get720p() {
            return m720p;
        }

        public void set720p(String m720p) {
            this.m720p = m720p;
        }

        public String get1080p() {
            return m1080p;
        }

        public void set1080p(String m1080p) {
            this.m1080p = m1080p;
        }
    }

    public static class Thumb {

        public final static String small_JSON = "small";
        public final static String large_JSON = "large";

        private String mSmall;
        private String mLarge;

        public Thumb(String mSmall, String mLarge) {
            this.mSmall = mSmall;
            this.mLarge = mLarge;
        }

        public Thumb() {
        }

        public String getSmall() {
            return mSmall;
        }

        public void setSmall(String mSmall) {
            this.mSmall = mSmall;
        }

        public String getLarge() {
            return mLarge;
        }

        public void setLarge(String mLarge) {
            this.mLarge = mLarge;
        }
    }
}

