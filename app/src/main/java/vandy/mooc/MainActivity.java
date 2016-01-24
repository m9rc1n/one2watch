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
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(TrailerPresenter.class, this);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BoxOfficeFragment(), getString(R.string.box_office));
        adapter.addFragment(new ComingSoonFragment(), getString(R.string.coming_soon));
        adapter.addFragment(new PopularFragment(), getString(R.string.popular));
        adapter.addFragment(new SearchFragment(), getString(R.string.search));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String current = adapter.getPageTitle(position).toString();
                if (current.equals(getString(R.string.box_office))) {
                    getTrailerAsync(TrailerType.BOX_OFFICE);
                } else if (current.equals(getString(R.string.coming_soon))) {
                    getTrailerAsync(TrailerType.COMING_SOON);
                } else if (current.equals(getString(R.string.popular))) {
                    getTrailerAsync(TrailerType.POPULAR);
                } else if (current.equals(getString(R.string.search))) {
                    getTrailerAsync(mQuery);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy(isChangingConfigurations());
        super.onDestroy();
    }

    public void getTrailerSync(String search) {
        if (search != null) {
            if (!getPresenter().getTrailersSync(search))
                Utils.showToast(getCurrentFocus(), "Call already in progress");
        }
    }

    public void getTrailerAsync(String search) {
        if (search != null) {
            if (!getPresenter().getTrailersAsync(search)) {
                Utils.showToast(mToolbar, "Call already in progress");
            }
        }
    }

    public void getTrailerSync(TrailerType type) {
        if (!getPresenter().getTrailersSync(type))
            Utils.showToast(mToolbar, "Call already in progress");
    }

    public void getTrailerAsync(TrailerType type) {
        if (!getPresenter().getTrailersAsync(type)) {
            Utils.showToast(mToolbar, "Call already in progress");
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
                mQuery = query;
                getTrailerAsync(mQuery);
                mViewPager.setCurrentItem(3);
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
        if (trailerData == null) Utils.showToast(mToolbar, errorMessage);
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

    @Override
    public void synchronizeAll() {
        getTrailerAsync(TrailerType.BOX_OFFICE);
        getTrailerAsync(TrailerType.COMING_SOON);
        getTrailerAsync(TrailerType.POPULAR);
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