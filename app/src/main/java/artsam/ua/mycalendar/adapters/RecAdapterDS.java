package artsam.ua.mycalendar.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import artsam.ua.mycalendar.entity.Event;
import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.R;

public class RecAdapterDS extends RecyclerView.Adapter<RecAdapterDS.RecViewHolder> {

    public static final Event CLEAR_LIST_EVENT = new Event("No Events for Today", "cap_america");

    public Context mContext;
    private ArrayList<Event> eventsList;

    public RecAdapterDS(Context mContext, ArrayList<Event> eventsList) {
//        Log.d(MainActivity.LOG_TAG, "RecAdapterDS: Constructor");

        this.mContext = mContext;

        if (eventsList != null) {

            if (eventsList.size() == 0) {
                eventsList.add(CLEAR_LIST_EVENT);
            }

            this.eventsList = eventsList;
        } else {
            this.eventsList = new ArrayList<>();
            this.eventsList.add(CLEAR_LIST_EVENT);
        }
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(MainActivity.LOG_TAG, "RecAdapterDS: onCreateViewHolder");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewHolder holder, final int listPosition) {
//        Log.d(MainActivity.LOG_TAG, "RecAdapterDS: onBindViewHolder " + listPosition);

        if (eventsList.get(listPosition) != null) {
            holder.setItemTag(eventsList.get(listPosition).getText());
            holder.mTextName.setText(eventsList.get(listPosition).getText());
            Picasso.with(mContext)
                    .load("file:///android_asset/images/"
                            + eventsList.get(listPosition).getImagePath() + ".jpg")
                    .resize(100, 100)
                    .into(holder.mImageEvent);
        }
    }

    public void swap() {
        Log.d(MainActivity.LOG_TAG, "RecAdapterDS: swap");

        List<Event> datas = MainActivity.db4oProvider.findAll(new Event());
        eventsList.clear();
        eventsList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    //==================================================================================================
    public class RecViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private View mItemView;
        private TextView mTextName;
        private ImageView mImageEvent;

        public RecViewHolder(View itemView) {
            super(itemView);

            this.mItemView = itemView;
            this.mTextName = (TextView) itemView.findViewById(R.id.tvEvent);
            this.mImageEvent = (ImageView) itemView.findViewById(R.id.imgEvent);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                Log.d(MainActivity.LOG_TAG, "RecViewHolder: onClick  null");
            } else if (v.getTag().equals("Add New Event") || v.getTag().equals("No Events for Today")) {
                raiseNewEventActivity();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(MainActivity.LOG_TAG, "RecViewHolder: onLongClick");
            AlertDialog diaBox = AskOption(v);
            diaBox.show();
            return false;
        }

        private void raiseNewEventActivity() {
            Intent intent = new Intent("artsam.ua.mycalendar.intent.action.newevent");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((MainActivity) mContext)
                    .startActivityForResult(intent, MainActivity.REQUEST_CODE_NEWEVENT);
        }

        public void setItemTag(String itemTag) {
            mItemView.setTag(itemTag);
        }

        private AlertDialog AskOption(final View v) {

            return new AlertDialog.Builder(mContext)
                    //set message, title, and icon
                    .setTitle("Are you sure to delete")
                    .setMessage(" '" + v.getTag() + "' event")
//                    .setIcon(R.drawable.delete)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //my deleting code
                            Log.d(MainActivity.LOG_TAG, "AlertDialog: Delete");

                            List<Event> events = MainActivity.db4oProvider
                                    .getRecord(new Event(v.getTag().toString(), null));
                            MainActivity.db4oProvider.delete(events.get(0));
                            MainActivity.db4oProvider.commit();

                            swap();
                            dialog.dismiss();
                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(MainActivity.LOG_TAG, "AlertDialog: Cancel");
                            dialog.dismiss();
                        }
                    })

                    .create();
        }
    }
}
