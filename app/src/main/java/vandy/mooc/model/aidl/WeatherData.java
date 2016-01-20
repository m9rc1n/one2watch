package vandy.mooc.model.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Parcelable {

    final public static String cod_JSON = "cod";
    final public static String name_JSON = "name";
    final public static String id_JSON = "id";
    final public static String dt_JSON = "dt";
    final public static String wind_JSON = "wind";
    final public static String main_JSON = "main";
    final public static String base_JSON = "base";
    final public static String weather_JSON = "weather";
    final public static String sys_JSON = "sys";
    final public static String message_JSON = "message";

    public static final Parcelable.Creator<WeatherData> CREATOR = new Parcelable.Creator<WeatherData>() {
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    private String mName;
    private long mDate;
    private long mCod;
    private List<Weather> mWeathers = new ArrayList<Weather>();
    private Sys mSys;
    private Main mMain;
    private Wind mWind;
    private String mMessage;

    public WeatherData(String name,
                       long date,
                       long cod,
                       Sys sys,
                       Main main,
                       Wind wind,
                       List<Weather> weathers) {
        mName = name;
        mDate = date;
        mCod = cod;
        mSys = sys;
        mMain = main;
        mWind = wind;
        mWeathers = weathers;
    }

    public WeatherData() {
    }

    private WeatherData(Parcel in) {
        mName = in.readString();
        mDate = in.readLong();
        mCod = in.readLong();

        mWeathers.add(new Weather(in.readLong(),
                in.readString(),
                in.readString(),
                in.readString()));

        mSys = new Sys(in.readLong(), in.readLong(), in.readString());

        mMain = new Main(in.readDouble(), in.readLong(), in.readDouble());

        mWind = new Wind(in.readDouble(), in.readDouble());
    }

    public Sys getSys() {
        return mSys;
    }

    public void setSys(Sys sys) {
        mSys = sys;
    }

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public long getCod() {
        return mCod;
    }

    public void setCod(long cod) {
        mCod = cod;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeLong(mDate);
        dest.writeLong(mCod);
        final Weather weather = mWeathers.get(0);
        dest.writeLong(weather.getId());
        dest.writeString(weather.getMain());
        dest.writeString(weather.getDescription());
        dest.writeString(weather.getIcon());
        dest.writeLong(mSys.getSunrise());
        dest.writeLong(mSys.getSunset());
        dest.writeString(mSys.getCountry());
        dest.writeDouble(mMain.getTemp());
        dest.writeLong(mMain.getHumidity());
        dest.writeDouble(mMain.getPressure());
        dest.writeDouble(mWind.getSpeed());
        dest.writeDouble(mWind.getDeg());
    }

    /*
     * BELOW THIS is related to Parcelable Interface.
     */

    public static class Weather {

        public final static String id_JSON = "id";
        public final static String main_JSON = "main";
        public final static String description_JSON = "description";
        public final static String icon_JSON = "icon";

        private long mId;
        private String mMain;
        private String mDescription;
        private String mIcon;

        public Weather(long id, String main, String description, String icon) {
            mId = id;
            mMain = main;
            mDescription = description;
            mIcon = icon;
        }

        public Weather() {
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public String getMain() {
            return mMain;
        }

        public void setMain(String main) {
            mMain = main;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }
    }

    public static class Sys {

        public final static String message_JSON = "message";
        public final static String country_JSON = "country";
        public final static String sunrise_JSON = "sunrise";
        public final static String sunset_JSON = "sunset";
        private long mSunrise;
        private long mSunset;
        private String mCountry;
        private double mMessage;

        public Sys(long sunrise, long sunset, String country) {
            mSunrise = sunrise;
            mSunset = sunset;
            mCountry = country;
        }

        public Sys() {
        }

        public long getSunrise() {
            return mSunrise;
        }

        public void setSunrise(long sunrise) {
            mSunrise = sunrise;
        }

        public long getSunset() {
            return mSunset;
        }

        public void setSunset(long sunset) {
            mSunset = sunset;
        }

        public String getCountry() {
            return mCountry;
        }

        public void setCountry(String country) {
            mCountry = country;
        }

        public double getMessage() {
            return mMessage;
        }

        public void setMessage(double message) {
            mMessage = message;
        }
    }

    public static class Main {
        public final static String temp_JSON = "temp";
        public final static String tempMin_JSON = "temp_min";
        public final static String tempMax_JSON = "temp_max";
        public final static String pressure_JSON = "pressure";
        public final static String seaLevel_JSON = "sea_level";
        public final static String grndLevel_JSON = "grnd_level";
        public final static String humidity_JSON = "humidity";
        private double mTemp;
        private long mHumidity;
        private double mPressure;

        public Main(double temp, long humidity, double pressure) {
            mTemp = temp;
            mHumidity = humidity;
            mPressure = pressure;
        }

        public Main() {
        }

        public double getPressure() {
            return mPressure;
        }

        public void setPressure(double pressure) {
            mPressure = pressure;
        }

        public double getTemp() {
            return mTemp;
        }

        public void setTemp(double temp) {
            mTemp = temp;
        }

        public long getHumidity() {
            return mHumidity;
        }

        public void setHumidity(long humidity) {
            mHumidity = humidity;
        }
    }

    public static class Wind {

        public final static String deg_JSON = "deg";
        public final static String speed_JSON = "speed";

        private double mSpeed;
        private double mDeg;

        public Wind(double speed, double deg) {
            mSpeed = speed;
            mDeg = deg;
        }

        public Wind() {
        }

        public double getSpeed() {
            return mSpeed;
        }

        public void setSpeed(double speed) {
            mSpeed = speed;
        }

        public double getDeg() {
            return mDeg;
        }

        public void setDeg(double deg) {
            mDeg = deg;
        }
    }
}

