package com.example.rafael.speed;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;


public class MainActivity extends ActionBarActivity {

    TextView ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ds = (TextView) findViewById(R.id.displaySpeed);
        ds.setText("DisplaySpeedHere");

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object... objects) {
                AutomotiveFactory.createAutomotiveManagerInstance(
                        new AutomotiveCertificate(new byte[0]),
                        new AutomotiveListener(){
                            @Override
                            public void receive(final AutomotiveSignal automotiveSignal) {
                                ds.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.setText(String.format("%.1f km/h", ((SCSFloat) automotiveSignal.getData()).getFloatValue()));

                                    }
                                });

                            }

                            @Override
                            public void timeout(int i) {

                            }

                            @Override
                            public void notAllowed(int i) {

                            }
                        },
                        new DriverDistractionListener() {
                            @Override
                            public void levelChanged(final DriverDistractionLevel driverDistractionLevel) {
                                ds.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ds.setTextSize(driverDistractionLevel.getLevel()*10.0F + 12.0F);
                                    }
                                });

                            }

                            @Override
                            public void lightModeChanged(LightMode lightMode) {

                            }

                            @Override
                            public void stealthModeChanged(StealthMode stealthMode) {

                            }
                        }
                ).register(AutomotiveSignalId.FMS_WHEEL_BASED_SPEED, AutomotiveSignalId.FMS_CURRENT_GEAR);
                return null;
            }
        }.execute();
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
}
