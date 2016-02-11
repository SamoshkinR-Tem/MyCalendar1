package artsam.ua.mycalendar.db;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import artsam.ua.mycalendar.entity.Day;
import artsam.ua.mycalendar.entity.Event;
import artsam.ua.mycalendar.entity.DeletedEvent;

public class Db4oHelper {

    private static final String DATABASE_NAME = "myDatabase.db4o";
    private static final int DATABASE_MODE = 0;
    private static ObjectContainer oc = null;
    private Context context;

    /**
     * @param context   It`s an object that provides access to the basic functions of the
     *                  application such as:
     *                  access to resources, to the file system, calling Activity, etc.
     */
    public Db4oHelper(Context context) {
        this.context = context;
    }

    /**
     * Create, open and close the database
     */
    public ObjectContainer db() {

        try {
            if (oc == null || oc.ext().isClosed()) {
                oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
            }

            return oc;

        } catch (Exception ie) {
            Log.e(Db4oHelper.class.getName(), ie.toString());
            return null;
        }
    }

    /**
     * Configure the behavior of the database
     */
    private EmbeddedConfiguration dbConfig() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().objectClass(Day.class).objectField("mDate").indexed(true);
        configuration.common().objectClass(Day.class).cascadeOnUpdate(true);
        configuration.common().objectClass(Day.class).cascadeOnActivate(true);
        configuration.common().objectClass(Day.class).cascadeOnDelete(false);
        configuration.common().objectClass(Event.class).objectField("mText").indexed(true);
        configuration.common().objectClass(Event.class).cascadeOnUpdate(true);
        configuration.common().objectClass(Event.class).cascadeOnActivate(true);
        configuration.common().objectClass(Event.class).cascadeOnDelete(false);
        configuration.common().objectClass(DeletedEvent.class).objectField("eventDel").indexed(true);
        configuration.common().objectClass(DeletedEvent.class).cascadeOnUpdate(true);
        configuration.common().objectClass(DeletedEvent.class).cascadeOnActivate(true);
        configuration.common().objectClass(DeletedEvent.class).cascadeOnDelete(false);
        return configuration;
    }
//    private static Configuration configure(){
//
//        dbConfiguration = Db4o.newConfiguration();
//
//        dbConfiguration.objectClass(Trip.class).objectField(Trip.ID).indexed(true);
//        dbConfiguration.objectClass(Trip.class).cascadeOnUpdate(true);
//        dbConfiguration.objectClass(Trip.class).cascadeOnDelete(true);
//        dbConfiguration.objectClass(Location.class).objectField(Location.GEORSSPNT).indexed(true);
//        dbConfiguration.objectClass(Location.class).cascadeOnDelete(true);
//        dbConfiguration.objectClass(Location.class).cascadeOnUpdate(true);
//        dbConfiguration.objectClass(Person.class).objectField(Person.USERNAME).indexed(true);
//        dbConfiguration.objectClass(Person.class).cascadeOnUpdate(true);
//        dbConfiguration.objectClass(Person.class).cascadeOnDelete(true);
//        //[...]
//        dbConfiguration.objectClass(ActiveTrip.class).objectField(ActiveTrip.ID).indexed(true);
//        dbConfiguration.objectClass(ActiveTrip.class).cascadeOnUpdate(true);
//        dbConfiguration.objectClass(ActiveTrip.class).cascadeOnDelete(true);
//        dbConfiguration.objectClass(Route.class).cascadeOnUpdate(true);
//        dbConfiguration.objectClass(Route.class).cascadeOnDelete(true);
//        dbConfiguration.lockDatabaseFile(false);
//        dbConfiguration.messageLevel(2);
//
//        return dbConfiguration;
//    }

    /**
     * Returns the path for the database location
     */
    private String db4oDBFullPath(Context context) {
//        System.out.println(context.getDir("data", DATABASE_MODE) + "/" + DATABASE_NAME);
        return context.getDir("data", DATABASE_MODE) + "/" + DATABASE_NAME;
    }

    /**
     * Closes the database
     */
    public void close() {
        if (oc != null)
            oc.close();
    }
}