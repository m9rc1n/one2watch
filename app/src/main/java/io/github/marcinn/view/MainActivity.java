package io.github.marcinn.view;

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
import android.os.Environment;
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

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.marcinn.MVP;
import io.github.marcinn.R;
import io.github.marcinn.common.GenericActivity;
import io.github.marcinn.common.Utils;
import io.github.marcinn.model.aidl.TrailerData;
import io.github.marcinn.model.aidl.TrailerType;
import io.github.marcinn.presenter.TrailerPresenter;

public class MainActivity
        extends GenericActivity<MVP.RequiredViewOps, MVP.ProvidedPresenterOps, TrailerPresenter>
        implements MVP.RequiredViewOps {

    public static final String ACTION_RUN_BROWSER = "io.github.marcinn.view.MainActivity:ACTION_RUN_BROWSER";
    public static final String ACTION_SEND_TO_MESSENGER = "io.github.marcinn.view.MainActivity:ACTION_SEND_TO_MESSENGER";
    public static final String EXTRA_URL = "io.github.marcinn.view.MainActivity:EXTRA_URL";
    public static final String HOST_GOOGLE = "www.google.com";
    public static final int SHARE_TO_MESSENGER_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getName();
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private String mQuery;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;
    private BroadcastReceiver mReceiver;
    private boolean mPicking;
    private MessengerThreadParams mThreadParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(TrailerPresenter.class, this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.ic_logo);
        mToolbar.setSubtitle(R.string.quickly_share_movies);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        bindCustomTabsService();
        CustomTabsSession session = getSession();
        if (mClient != null) {
            mClient.warmup(0);
            session.mayLaunchUrl(Uri.parse(HOST_GOOGLE), null, null);
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_RUN_BROWSER.equals(intent.getAction())) {
                    //                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //                                    v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
                    int color = ContextCompat.getColor(context, R.color.colorPrimary);
                    builder.setToolbarColor(color).setShowTitle(true);
                    prepareMenuItems(builder);
                    builder.setStartAnimations(getBaseContext(),
                            R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    builder.setExitAnimations(getBaseContext(),
                            R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_arrow_back));
                    CustomTabsIntent customTabsIntent = builder.build();
                    CustomTabsHelper.addKeepAliveExtra(getBaseContext(), customTabsIntent.intent);
                    customTabsIntent.launchUrl(MainActivity.this,
                            Uri.parse(intent.getStringExtra(EXTRA_URL)));
                } else if (ACTION_SEND_TO_MESSENGER.equals(intent.getAction())) {
                    //                    Uri uri = Uri.parse(intent.getStringExtra(EXTRA_URL));
                    Uri uri = Uri.parse(intent.getStringExtra(EXTRA_URL));
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                            "a.mp4");
                    ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(
                            Uri.fromFile(file),
                            "video/mp4")
                            .setExternalUri(uri)
                            .setMetaData("{ \"image\" : \"trailers\" }")
                            .build();

                    if (mPicking) {
                        MessengerUtils.finishShareToMessenger(MainActivity.this,
                                shareToMessengerParams);
                    } else {
                        MessengerUtils.shareToMessenger(MainActivity.this,
                                SHARE_TO_MESSENGER_REQUEST_CODE,
                                shareToMessengerParams);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(ACTION_RUN_BROWSER);
        intentFilter.addAction(ACTION_SEND_TO_MESSENGER);
        registerReceiver(mReceiver, intentFilter);
        pickMessengerIntent();
    }

    private void pickMessengerIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mPicking = true;
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
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
        unregisterReceiver(mReceiver);
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

    private void prepareMenuItems(CustomTabsIntent.Builder builder) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), this.getClass());
        Bundle bundle = ActivityOptions.makeCustomAnimation(this,
                R.anim.slide_in_left,
                R.anim.slide_out_right).toBundle();
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0, bundle);
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