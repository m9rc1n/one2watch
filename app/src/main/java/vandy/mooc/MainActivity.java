package vandy.mooc;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import vandy.mooc.common.GenericActivity;
import vandy.mooc.common.Utils;
import vandy.mooc.model.aidl.TrailerData;
import vandy.mooc.presenter.TrailerPresenter;
import vandy.mooc.view.FourthFragment;
import vandy.mooc.view.OneFragment;
import vandy.mooc.view.SecondFragment;
import vandy.mooc.view.ThirdFragment;

public class MainActivity
        extends GenericActivity<MVP.RequiredViewOps, MVP.ProvidedPresenterOps, TrailerPresenter>
        implements MVP.RequiredViewOps {

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

    public void getTrailerSync(String search) {
        if (search != null) {
            if (!getPresenter().getWeatherSync(search))
                Utils.showToast(this, "Call already in progress");
        }
    }

    public void getTrailerAsync(String search) {
        if (search != null) {
            if (!getPresenter().getWeatherAsync(search))
                Utils.showToast(this, "Call already in progress");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getTrailerAsync(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void displayResults(List<TrailerData> trailerData, String errorMessage) {
        if (trailerData == null) Utils.showToast(this, errorMessage);
        else {
            sendBroadcast(OneFragment.makeIntent(trailerData));
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