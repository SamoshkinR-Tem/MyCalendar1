package artsam.ua.mycalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import artsam.ua.mycalendar.entity.Event;

public class NewEventActivity extends Activity implements View.OnClickListener {

    private Event mEvent;
    private EditText mEtEvent;
    private String mImgPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onCreate()");

        findViewById(R.id.imgEvent).setOnClickListener(this);
        mEtEvent = (EditText) findViewById(R.id.etEvent);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEvent:
                Log.d(MainActivity.LOG_TAG, "NewEventActivity: onClick(imgEvent)");

                break;
            case R.id.btnOk:
                Log.d(MainActivity.LOG_TAG, "NewEventActivity: onClick(btnOk)");

                initEvent();
                if(mEvent != null){
                    saveToDb();
                    super.onBackPressed();
                }
                break;
            case R.id.btnCancel:
                Log.d(MainActivity.LOG_TAG, "NewEventActivity: onClick(btnCancel)");
                super.onBackPressed();
                break;
        }
    }

    private void initEvent() {
        if(!mEtEvent.getText().toString().equals("")){
            mEvent = new Event(mEtEvent.getText().toString(), "raven");
        } else {
            Toast.makeText(getBaseContext(), R.string.enter_event_text, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToDb() {
        MainActivity.db4oProvider.store(mEvent);
        MainActivity.db4oProvider.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.LOG_TAG, "NewEventActivity: onDestroy()");
    }
}
