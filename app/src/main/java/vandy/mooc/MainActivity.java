package vandy.mooc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

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

    public static final String ACTION_RUN_BROWSER = "vandy.mooc.MainActivity:ACTION_RUN_BROWSER";
    public static final String EXTRA_URL = "vandy.mooc.MainActivity:EXTRA_URL";
    private static final String TAG = MainActivity.class.getName();
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private String mQuery;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;
    private BroadcastReceiver mReceiver;

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

        bindCustomTabsService();
        CustomTabsSession session = getSession();
        if (mClient != null) {
            mClient.warmup(0);
            session.mayLaunchUrl(Uri.parse("www.google.com"), null, null);
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_RUN_BROWSER.equals(intent.getAction())) {
                    //                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //                v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
                    int color = ContextCompat.getColor(context, R.color.colorPrimary);
                    builder.setToolbarColor(color).setShowTitle(true);
                    prepareMenuItems(builder);
                    builder.setStartAnimations(getActivity(),
                            R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    builder.setExitAnimations(getActivity(),
                            R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_arrow_back));
                    CustomTabsIntent customTabsIntent = builder.build();
                    CustomTabsHelper.addKeepAliveExtra(getActivity(), customTabsIntent.intent);
                    customTabsIntent.launchUrl(getActivity(),
                            Uri.parse(intent.getStringExtra(EXTRA_URL)));
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ACTION_RUN_BROWSER));
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

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy(isChangingConfigurations());
        unbindCustomTabsService();
        super.onDestroy();
    }

    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new CustomTabsCallback() {
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
                }
            });
        }
        return mCustomTabsSession;
    }

    private void bindCustomTabsService() {
        if (mClient != null) return;
        if (TextUtils.isEmpty(mPackageNameToBind)) {
            mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
            if (mPackageNameToBind == null) return;
        }
        mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };
    }

    private void unbindCustomTabsService() {
        if (mConnection == null) return;
        if (mClient != null) unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
    }

    private Activity getActivity() {
        return this;
    }

    private void prepareMenuItems(CustomTabsIntent.Builder builder) {
        Intent menuIntent = new Intent();
        menuIntent.setClass(getApplicationContext(), this.getClass());
        Bundle menuBundle = ActivityOptions.makeCustomAnimation(this,
                R.anim.slide_in_left,
                R.anim.slide_out_right).toBundle();
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0,
                menuIntent,
                0,
                menuBundle);
        builder.addMenuItem(getString(R.string.come_back), pi);
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