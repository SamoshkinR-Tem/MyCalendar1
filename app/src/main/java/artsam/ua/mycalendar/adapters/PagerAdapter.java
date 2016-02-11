package artsam.ua.mycalendar.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.R;
import artsam.ua.mycalendar.fragment.EventsDialog;
import artsam.ua.mycalendar.utils.CalendarUtil;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PagerPlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 70 total pages.
        return 70;
    }

    public static class PagerPlaceholderFragment extends Fragment
            implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        MainActivity mMainActivity;
        GridAdapter mGridAdapter;
        GridView mGridView;
        CalendarUtil mCalUtil;

        public PagerPlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PagerPlaceholderFragment newInstance(int sectionNumber) {
            PagerPlaceholderFragment fragment = new PagerPlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View mRootView = inflater.inflate(R.layout.fragment_pageritem, container, false);

            mCalUtil = new CalendarUtil(getArguments().getInt(ARG_SECTION_NUMBER));
            mMainActivity = (MainActivity) getActivity();

            mGridView = (GridView) mRootView.findViewById(R.id.gvMonth);
            mGridAdapter = new GridAdapter(getActivity(), mCalUtil.getDates());
            mGridView.setAdapter(mGridAdapter);
            mGridView.setOnItemClickListener(this);
            mGridView.setOnItemLongClickListener(this);

            return mRootView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(MainActivity.LOG_TAG, "mGridView: onItem " +
                    MainActivity.SDF_LONG.format(mCalUtil.obtCurrDate(position)) + " Click");
            mMainActivity.updateRecView(MainActivity.SDF_LONG.format(mCalUtil.obtCurrDate(position)));
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(MainActivity.LOG_TAG, "mGridView: onItem " +
                    MainActivity.SDF_LONG.format(mCalUtil.obtCurrDate(position)) + " LongClick");

            GridAdapter.GridViewHolder holder = (GridAdapter.GridViewHolder) view.getTag();

            EventsDialog ed = new EventsDialog();
            Bundle args = new Bundle();
            args.putString(MainActivity.ARG_DATE, MainActivity.SDF_LONG.format(holder.getDate()));
            ed.setArguments(args);
            ed.show(getFragmentManager(), "addEventDlg");

            mGridAdapter.notifyDataSetChanged();
            return true;
        }
    }
}