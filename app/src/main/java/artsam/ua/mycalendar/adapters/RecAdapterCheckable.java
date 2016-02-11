package artsam.ua.mycalendar.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.R;
import artsam.ua.mycalendar.entity.Event;

public class RecAdapterCheckable extends RecyclerView.Adapter<RecAdapterCheckable.RecViewHolder> {

    private Context mContext;
    private ArrayList<Event> mEventsList;
    private ArrayList<Event> mCheckedEvents;

    public RecAdapterCheckable(Context mContext, ArrayList<Event> mEventsList) {
        this.mContext = mContext;
        this.mEventsList = mEventsList;
        this.mCheckedEvents = new ArrayList<>();
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(MainActivity.LOG_TAG, "RecAdapterCheckable: onCreateViewHolder");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_checkable, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewHolder holder, final int listPosition) {
//        Log.d(MainActivity.LOG_TAG, "RecAdapterCheckable: onBindViewHolder  " + listPosition);

        holder.setItemTag(mEventsList.get(listPosition));
        holder.mTextName.setText(mEventsList.get(listPosition).getText());
        Picasso.with(mContext)
                .load("file:///android_asset/images/"
                        + mEventsList.get(listPosition).getImagePath() + ".jpg")
                .resize(100, 100)
                .into(holder.mImageEvent);
    }

    public void swap() {
        List<Event> datas = MainActivity.db4oProvider.findAll(new Event());
        mEventsList.clear();
        mEventsList.addAll(datas);
        notifyDataSetChanged();
    }

    public ArrayList<Event> getCheckedEvents() {
        return mCheckedEvents;
    }

    public void setCheckedEvents(ArrayList<Event> checkedEvents) {
        this.mCheckedEvents = checkedEvents;
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

//==================================================================================================

    public class RecViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener,
            CompoundButton.OnCheckedChangeListener {

        private View mItemView;
        private ImageView mImageEvent;
        private TextView mTextName;
        private CheckBox mCheckBox;

        public RecViewHolder(View itemView) {
            super(itemView);

            this.mItemView = itemView;
            this.mTextName = (TextView) itemView.findViewById(R.id.tvEvent);
            this.mImageEvent = (ImageView) itemView.findViewById(R.id.imgEvent);
            this.mCheckBox = (CheckBox) itemView.findViewById(R.id.chbEvent);

            mCheckBox.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemTag(Event event) {
            mItemView.setTag(event);

            if (mCheckedEvents.size() != 0) {
                for (int i = 0; i < mCheckedEvents.size(); i++) {
                    if (mCheckedEvents.get(i).getText().equals(event.getText())) {
                        mCheckBox.setChecked(true);
                    }
                }
            }

        }

        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                Log.d(MainActivity.LOG_TAG, "RecViewHolder: onClick Teg == null");
            } else {
//                Log.d(MainActivity.LOG_TAG, "RecViewHolder: onClick");
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setChecked(true);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(MainActivity.LOG_TAG, "RecViewHolder: onLongClick");
            AlertDialog diaBox = ConfirmDeletion(v);
            diaBox.show();
            return false;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Event event = (Event) mItemView.getTag();
            if (isChecked) {
                if (!mCheckedEvents.contains(event)) {
                    mCheckedEvents.add(event);
                    Log.d(MainActivity.LOG_TAG,
                            "RecViewHolder: onCheckboxClicked " +
                                    event.getText() + " added");
                }
            } else {
                Log.d(MainActivity.LOG_TAG,
                        "RecViewHolder: onCheckboxClicked " +
                                event.getText() + " removed");
                mCheckedEvents.remove(event);
            }
        }

        private AlertDialog ConfirmDeletion(final View v) {

            return new AlertDialog.Builder(mContext)
                    //set message, title, and icon
                    .setTitle("Are you sure to delete")
                    .setMessage(" '" + ((Event) v.getTag()).getText() + "' event")
//                    .setIcon(R.drawable.delete)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //my deleting code
                            Log.d(MainActivity.LOG_TAG, "AlertDialog: Delete");

                            List<Event> events = MainActivity.db4oProvider
                                    .getRecord(new Event(((Event) v.getTag()).getText(), null));
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
