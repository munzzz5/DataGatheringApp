package com.example.datagatheringapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SensorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SENSOR ON COMMAND","REACHED");
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        //stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d("SENSOR ON CREATE","REACHED");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("SENSOR ON DESTROY","REACHED");
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int i=0;
        while (i<2) {
            Log.d("SENSOR VALUES", getAccelerometer(sensorEvent));
            i++;
        }
        stopSelf();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private String getAccelerometer(SensorEvent sensorEvent) {
        float[] values=sensorEvent.values;


        float x=values[0];
        float y=values[1];
        float z=values[2];
        String s="";
        if((x>5||x<-5) && y<2 && z<2){
            s="Screen facing you and horizontal phone";
        }
        else if((y>5||y<-5) && x<2 && z<2)
        {
            s="Screen facing you and verticle phone";
        }
        else if((z>5||z<-5) && y<2 && x<2){
            s="screen bottom//screen top";
        }
        else if(y>5 && z>5 && x<2)
        {
            s="Viewing angle!";
        }
        else if(x>5 && z>5 && y<2)
        {
            s="Movie Angle!";
        }
        s+="\nX="+x+"\nY="+y+"\nZ="+z;




        return s;



    }
}
