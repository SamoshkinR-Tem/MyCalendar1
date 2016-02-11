package artsam.ua.mycalendar.fragment;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import artsam.ua.mycalendar.adapters.RecAdapterCheckable;
import artsam.ua.mycalendar.adapters.RecAdapterDS;
import artsam.ua.mycalendar.entity.Day;
import artsam.ua.mycalendar.entity.Event;
import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.R;

public class EventsDialog extends DialogFragment implements View.OnClickListener {

    private String mDate;
    private Day day;
    private ArrayList<Day> mDays;
    private ArrayList<Event> mEvents;
    private RecAdapterCheckable mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "EventsDialog: onCreateView");

        mDate = getArguments().getString(MainActivity.ARG_DATE);
        day = new Day(mDate, null);
        mDays = new ArrayList<>();
        mEvents = new ArrayList<>();

        for (Day d : MainActivity.db4oProvider.getRecord(day)) {
            mDays.add(d);
        }

        switch (mDays.size()) {
            case 0:
                Log.d(MainActivity.LOG_TAG, "EventsDialog: mDays.size() == 0");
                break;
            case 1:
                Log.d(MainActivity.LOG_TAG, "EventsDialog: mDays.size() != 0");
                day = mDays.get(0);
                if (day.getEvents().contains(null)) {
                    for (Event e : day.getEvents()) {
                        if (e == null) {
                            day.getEvents().remove(e);
                            MainActivity.db4oProvider.delete(e);
                            MainActivity.db4oProvider.commit();
                        }
                    }
                }
                break;
            default:
                Log.d(MainActivity.LOG_TAG, "MainActivity: fetchEvents() CHECK DB!!!");
                break;
        }

        getDialog().setTitle("Events Dialog!");
        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if (textView != null) {
            textView.setGravity(Gravity.CENTER);
        }

        View v = inflater.inflate(R.layout.fragment_addeventdialog, null);

        v.findViewById(R.id.btnNewEvent).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerAddEvents);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fetchEvents();
        mAdapter = new RecAdapterCheckable(getActivity(), mEvents);
        spotCheckedEvents();
        recyclerView.setAdapter(mAdapter);

        v.findViewById(R.id.btnOk).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);

        return v;
    }

    private void fetchEvents() {
        for (Event e : MainActivity.db4oProvider.findAll(new Event())) {
            mEvents.add(e);
        }
    }

    private void spotCheckedEvents() {
        if (mDays.size() != 0) {
            mAdapter.setCheckedEvents(day.getEvents());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewEvent:
                raiseNewEventActivity();
                break;
            case R.id.btnOk:
//                Log.d(MainActivity.LOG_TAG, "EventsDialog: Button " + ((Button) v).getText() + " pressed");
                storeDay();
                ((MainActivity) getActivity()).updateRecView(mDate);
                dismiss();
                break;
            case R.id.btnCancel:
//                Log.d(MainActivity.LOG_TAG, "EventsDialog: Button " + ((Button) v).getText() + " pressed");
                for (Day d : MainActivity.db4oProvider.findAll(new Day())) {
                    MainActivity.db4oProvider.delete(d);
                }
                MainActivity.db4oProvider.commit();
                dismiss();
                break;
        }
    }

    private void storeDay() {

        day.setDate(mDate);
        mEvents = mAdapter.getCheckedEvents();
        if (mEvents.size() == 0) {
            MainActivity.db4oProvider.delete(day);
        } else {
            if (mEvents.contains(RecAdapterDS.CLEAR_LIST_EVENT)) {
                mEvents.remove(RecAdapterDS.CLEAR_LIST_EVENT);
            }

            day.setEvents(mEvents);
            MainActivity.db4oProvider.store(day);
            MainActivity.db4oProvider.commit();

            for (Day d : MainActivity.db4oProvider.getRecord(new Day(mDate, null))) {
                Log.d(MainActivity.LOG_TAG, "EventsDialog: storeDay " +
                        "Date = " + d.getDate() +
                        ", Events = " + d.getEvents());
            }
        }
    }

    private void raiseNewEventActivity() {
        Intent intent = new Intent("artsam.ua.mycalendar.intent.action.newevent");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_NEWEVENT);
    }

    public RecAdapterCheckable getRecAdapter() {
        return mAdapter;
    }

    /**
     * Method onDismiss activates every time when the the dialog is closed
     *
     * @param dialog It is the dialog interface.
     */
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(MainActivity.LOG_TAG, "EventsDialog: onDismiss");
    }

    /**
     * Method onCancel activates when the dialogue canceled whith the Back button.
     *
     * @param dialog It is the dialog interface.
     */
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(MainActivity.LOG_TAG, "EventsDialog: onCancel");
    }
}
