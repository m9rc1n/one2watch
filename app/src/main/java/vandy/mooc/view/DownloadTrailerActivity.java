package vandy.mooc.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import vandy.mooc.MVP;
import vandy.mooc.R;
import vandy.mooc.common.GenericActivity;
import vandy.mooc.common.Utils;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.presenter.TrailerPresenter;

public class DownloadTrailerActivity
        extends GenericActivity<MVP.RequiredViewOps, MVP.ProvidedPresenterOps, TrailerPresenter>
        implements MVP.RequiredViewOps {

    protected EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_weather_activity);
        mEditText = ((EditText) findViewById(R.id.locationQuery));
        super.onCreate(TrailerPresenter.class, this);
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy(isChangingConfigurations());
        super.onDestroy();
    }

    public void getTrailerSync(View v) {
        Utils.hideKeyboard(this, mEditText.getWindowToken());
        final String location = Utils.uppercaseInput(this,
                mEditText.getText().toString().trim(),
                true);
        if (location != null) {
            if (!getPresenter().getWeatherSync(location))
                Utils.showToast(this, "Call already in progress");
            mEditText.requestFocus();
            mEditText.selectAll();
        }
    }

    public void getTrailerAsync(View v) {
        Utils.hideKeyboard(this, mEditText.getWindowToken());
        final String location = Utils.uppercaseInput(this,
                mEditText.getText().toString().trim(),
                true);
        if (location != null) {
            if (!getPresenter().getWeatherAsync(location))
                Utils.showToast(this, "Call already in progress");
            mEditText.requestFocus();
            mEditText.selectAll();
        }
    }

    public void displayResults(TrailerData trailerData, String errorMessage) {
        if (trailerData == null) Utils.showToast(this, errorMessage);
        else {
            final Intent intent = DisplayTrailerActivity.makeIntent(trailerData);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
            else
                Utils.showToast(this, "No Activity found to display Weather Data");
        }
    }
}
