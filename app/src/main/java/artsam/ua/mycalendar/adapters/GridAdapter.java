package artsam.ua.mycalendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.R;

public class GridAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;

    /**
     * Contains an array of objects that represent the data of this GridAdapter.
     */
    private Object[] mObjects;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param mObjects An array of objects for GridView;
     */
    public GridAdapter(Context context, Object[] mObjects) {
        this.mInflater = LayoutInflater.from(context);
        this.mObjects = mObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_griditem, parent, false);
            holder = new GridViewHolder();
            holder.setTextView((TextView) convertView.findViewById(R.id.tvDate));
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

        Date item = (Date) getItem(position);
        holder.setDate(item);
        holder.getTextView().setText(MainActivity.SDF_SHORT.format(item));

        convertView.setBackgroundResource(R.drawable.rect_dark);

        if (MainActivity.SDF_SHORT.format(new Date()).equals(MainActivity.SDF_SHORT.format(item))) {
            convertView.setBackgroundResource(R.drawable.rect_bright);
        } else if (position < 7 && Integer.valueOf(MainActivity.SDF_SHORT.format(item)) > 7) {
            convertView.setBackgroundResource(R.drawable.rect_light);
        } else if (position > 28 && Integer.valueOf(MainActivity.SDF_SHORT.format(item)) < 14) {
            convertView.setBackgroundResource(R.drawable.rect_light);
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mObjects[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return MainActivity.GRID_SIZE;
    }

    public static class GridViewHolder {

        private Date mDate;
        private TextView mTextView;

        public GridViewHolder() {
        }

        public Date getDate() {
            return mDate;
        }

        public void setDate(Date date) {
            this.mDate = date;
        }

        public TextView getTextView() {
            return mTextView;
        }

        public void setTextView(TextView tv) {
            this.mTextView = tv;
        }
    }
}
