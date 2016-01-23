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
import vandy.mooc.model.aidl.TrailerType;
import vandy.mooc.presenter.TrailerPresenter;
import vandy.mooc.view.BoxOfficeFragment;
import vandy.mooc.view.ComingSoonFragment;
import vandy.mooc.view.PopularFragment;
import vandy.mooc.view.SearchFragment;

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
        adapter.addFragment(new BoxOfficeFragment(), "Box office");
        adapter.addFragment(new ComingSoonFragment(), "Coming Soon");
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy(isChangingConfigurations());
        super.onDestroy();
    }

    public void getTrailerSync(String search, TrailerType type) {
        if (search != null) {
            if (!getPresenter().getTrailersSync(search, type))
                Utils.showToast(getCurrentFocus(), "Call already in progress");
        }
    }

    public void getTrailerAsync(String search, TrailerType type) {
        if (search != null) {
            if (!getPresenter().getTrailersAsync(search, type)) {
                Utils.showToast(toolbar, "Call already in progress");
            }
        }
    }

    public void getTrailerSync(TrailerType type) {
        if (!getPresenter().getTrailersSync(type))
            Utils.showToast(toolbar, "Call already in progress");
    }

    public void getTrailerAsync(TrailerType type) {
        if (!getPresenter().getTrailersAsync(type)) {
            Utils.showToast(toolbar, "Call already in progress");
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
                getTrailerAsync(TrailerType.BOX_OFFICE);
                getTrailerAsync(TrailerType.COMING_SOON);
                getTrailerAsync(TrailerType.POPULAR);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void displayResults(List<TrailerData> trailerData,
                               String errorMessage,
                               TrailerType type) {
        if (trailerData == null) Utils.showToast(toolbar, errorMessage);
        else {
            switch (type) {
                case BOX_OFFICE:
                    sendBroadcast(BoxOfficeFragment.makeIntent(trailerData));
                    break;
                case POPULAR:
                    sendBroadcast(PopularFragment.makeIntent(trailerData));
                    break;
                case COMING_SOON:
                    sendBroadcast(ComingSoonFragment.makeIntent(trailerData));
                    break;
                case SEARCH:
                    sendBroadcast(SearchFragment.makeIntent(trailerData));
                    break;
            }
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