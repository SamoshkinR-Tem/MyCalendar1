package artsam.ua.mycalendar;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import artsam.ua.mycalendar.adapters.PagerAdapter;
import artsam.ua.mycalendar.adapters.RecAdapterDS;
import artsam.ua.mycalendar.db.Db4oProvider;
import artsam.ua.mycalendar.entity.Day;
import artsam.ua.mycalendar.entity.Event;
import artsam.ua.mycalendar.fragment.EventsDialog;
import artsam.ua.mycalendar.utils.DateUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = "myLogs";
    public static final int REQUEST_CODE_NEWEVENT = 1;
    public static final int START_PAGER_POSITION = 37;
    public static final int GRID_SIZE = 42;
    public static final Date TODAY_DATE = new Date();
    public static final String ARG_DATE = "grid_item_date";
    public static final SimpleDateFormat SDF_LONG = new SimpleDateFormat("dd/MMM/yyyy");
    public static final SimpleDateFormat SDF_MIDDLE = new SimpleDateFormat("MMMM yyyy");
    public static final SimpleDateFormat SDF_SHORT = new SimpleDateFormat("d");

    public static Db4oProvider db4oProvider;

    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "MainActivity: onCreate ");

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//--------------------------------------------------------------------------------------------------

        final TextView tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvMonth.setText(SDF_MIDDLE.format(TODAY_DATE));

        // create an object to create and manage database
        db4oProvider = new Db4oProvider(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        // To create a representation of infinity we'll start from 37th item.
        mViewPager.setCurrentItem(START_PAGER_POSITION);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Date date = DateUtil.addMonth(TODAY_DATE, position - START_PAGER_POSITION);
                tvMonth.setText(SDF_MIDDLE.format(date));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);

        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rec_show_events);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        updateRecView(SDF_LONG.format(TODAY_DATE));
//--------------------------------------------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void updateRecView(String currDate) {
        ArrayList<Event> events = null;
        ArrayList<Day> days = new ArrayList<>();

        for (Day d : db4oProvider.getRecord(new Day(currDate, null))) {
            days.add(d);
            Log.d(LOG_TAG, "MainActivity: updateRecView " + d.toString());
        }

        if (days.size() > 1) {
            Log.d(LOG_TAG, "MainActivity: updateRecView() CHECK DB!!!");
        }

        try {
            events = days.get(0).getEvents();
        } catch (IndexOutOfBoundsException e) {
            Log.d(LOG_TAG, "MainActivity: " + e.getMessage());
        }

        mRecyclerView.setAdapter(new RecAdapterDS(this, events));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                break;
            case R.id.btnNext:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "MainActivity: onActivityResult");

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.get(fragments.size() - 1) instanceof EventsDialog) {
            EventsDialog ed = (EventsDialog) fragments.get(fragments.size() - 1);
            ed.getRecAdapter().swap();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Close database before exiting the application
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
        db4oProvider.close();
    }
}
