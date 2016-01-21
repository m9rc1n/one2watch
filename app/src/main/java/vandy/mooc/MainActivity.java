package vandy.mooc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import vandy.mooc.common.GenericActivity;
import vandy.mooc.common.Utils;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.presenter.TrailerPresenter;
import vandy.mooc.view.DisplayTrailerActivity;

public class MainActivity
        extends GenericActivity<MVP.RequiredViewOps, MVP.ProvidedPresenterOps, TrailerPresenter>
        implements MVP.RequiredViewOps {

    protected EditText mEditText;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mEditText = ((EditText) findViewById(R.id.searchEditText));
        super.onCreate(TrailerPresenter.class, this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new SecondFragment(), "TWO");
        adapter.addFragment(new ThirdFragment(), "THREE");
        adapter.addFragment(new FourthFragment(), "FOUR");
        viewPager.setAdapter(adapter);
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
            if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
            else Utils.showToast(this, "No Activity found to display Weather Data");
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}