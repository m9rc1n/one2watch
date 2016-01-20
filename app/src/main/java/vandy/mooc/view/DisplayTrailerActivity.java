package vandy.mooc.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import vandy.mooc.R;
import vandy.mooc.common.LifecycleLoggingActivity;
import vandy.mooc.common.Utils;
import vandy.mooc.model.aidl.TrailerData;

public class DisplayTrailerActivity extends LifecycleLoggingActivity {

    public static final String ACTION_DISPLAY_TRAILER = "vandy.mooc.intent.action.DISPLAY_TRAILER";
    public static final String TYPE_TRAILER = "parcelable/trailer";
    public static final String KEY_TRAILER_DATA = "trailerList";

    TextView mDateView;
    TextView mFriendlyDateView;
    TextView mLocationName;
    TextView mDescriptionView;
    TextView mCelsiusTempView;
    TextView mFarhenheitTempView;
    TextView mHumidityView;
    TextView mWindView;
    TextView mSunriseView;
    TextView mSunsetView;
    ImageView mIconView;

    public static Intent makeIntent(TrailerData trailerData) {
        return new Intent(ACTION_DISPLAY_TRAILER)
                .setType(TYPE_TRAILER)
                .putExtra(KEY_TRAILER_DATA, trailerData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_trailer_activity);
        initializeViewFields();
        final Intent intent = getIntent();
        if (intent.getType().equals(TYPE_TRAILER)) {
            final TrailerData trailerData = intent.getParcelableExtra(KEY_TRAILER_DATA);
            setViewFields(trailerData);
        } else {
            Utils.showToast(this, "Incorrect Data");
        }
    }

    private void initializeViewFields() {
        mIconView = (ImageView) findViewById(R.id.detail_icon);
        mDateView = (TextView) findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) findViewById(R.id.detail_day_textview);
        mLocationName = (TextView) findViewById(R.id.detail_locationName);
        mDescriptionView = (TextView) findViewById(R.id.detail_forecast_textview);
        mCelsiusTempView = (TextView) findViewById(R.id.detail_high_textview);
        mFarhenheitTempView = (TextView) findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) findViewById(R.id.detail_wind_textview);
        mSunriseView = (TextView) findViewById(R.id.detail_sunrise_textview);
        mSunsetView = (TextView) findViewById(R.id.detail_sunset_textview);
    }

    private void setViewFields(TrailerData trailerData) {
        final String locationName = trailerData.getMovie().getTitle() + ", " + trailerData.getMovie().getGenre();
        mLocationName.setText(locationName);
    }
}
