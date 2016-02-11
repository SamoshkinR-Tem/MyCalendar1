package artsam.ua.mycalendar.db;

import android.content.Context;
import android.util.Log;

import com.db4o.ext.ObjectNotStorableException;

import java.util.Date;
import java.util.List;

import artsam.ua.mycalendar.MainActivity;
import artsam.ua.mycalendar.entity.Day;
import artsam.ua.mycalendar.entity.DeletedEvent;
import artsam.ua.mycalendar.entity.Event;

public class Db4oProvider extends Db4oHelper {

    private static Db4oProvider provider = null;

    /**
     * @param context It`s an object that provides access to the basic functions of the
     *                application such as:
     *                access to resources, to the file system, calling Activity, etc.
     */
    public Db4oProvider(Context context) {
        super(context);
    }

    public static Db4oProvider getInstance(Context ctx) {
        if (provider == null)
            provider = new Db4oProvider(ctx);
        return provider;
    }

    //This method is used to store the object into the database.
    public void store(Object o) {
        if (db().queryByExample(o).size() == 0) {
            try {
                db().store(o);
            } catch (ObjectNotStorableException e) {
                Log.d(MainActivity.LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //This method is used to delete the object into the database.
    public void delete(Object o) {
        db().delete(o);
    }

    //This method is used to retrive all object from database.
    public List<Event> findAll(Event event) {
        return db().query(Event.class);
    }

    public List<Day> findAll(Day day) {
        return db().query(Day.class);
    }

    //This method is used to retrive matched object from database.
    public List<Event> getRecord(Event event) {
        return db().queryByExample(event);
    }

    public List<Day> getRecord(Day day) {
        return db().queryByExample(day);
    }

    public void commit() {
        db().commit();
    }

    public void close() {
        if (db() != null) {
            db().close();
        }
    }
/*
    public ObjectSet<Event> getAllData() {
        Event proto = new Event(null, null);
        return db().queryByExample(proto);
    }
*/

}
