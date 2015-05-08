package com.example.rafael.speed;

import android.os.AsyncTask;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.widget.TextView;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

/**
 * Created by Rafael on 24/04/2015.
 */
public class doServerStuff extends AsyncTask {

    TextView ds;

    protected doServerStuff(TextView ds) {

        this.ds = ds;

        doInBackground();

    }


    protected Object doInBackground(Object... objects) {
        AutomotiveFactory.createAutomotiveManagerInstance(
                new AutomotiveCertificate(new byte[0]),
                new AutomotiveListener() {
                    @Override
                         public void receive(final AutomotiveSignal automotiveSignal) {
                         ds.post(new Runnable() {
                                    @Override
                                    public void run() {
                                 ds.setText(String.format("%.1f km/h", ((SCSFloat) automotiveSignal.getData()).getFloatValue()));
                                        try {
                                            Thread.sleep(18);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
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
                                        ds.setTextSize(driverDistractionLevel.getLevel() * 10.0F + 12.0F);
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
        }//.execute();

