package com.example.datagatheringapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

public class SensorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    long startMillis;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SENSOR ON COMMAND","REACHED");
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        startMillis=System.currentTimeMillis();





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

        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            StringBuilder sb=new StringBuilder();
            Log.d("SENSOR VALUES", getAccelerometer(sensorEvent));
            sb.append(getAccelerometer(sensorEvent));
            sb.append("\n-----------------------------------\n");
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("OUTPUT FOR STORAGE",sb.toString());
            saveData(sb.toString());


        }




        else if(sensorEvent.sensor.getType()==Sensor.TYPE_MOTION_DETECT){
            StringBuilder sb=new StringBuilder();

                    sb.append("\n------------------MOTION DETECTOR-------------------------\n"+sensorEvent.values[0]);
                    Log.d("STEPS",String.valueOf(sensorEvent.values[0]));
                }

        if(System.currentTimeMillis()-startMillis>5000)
        {
            sensorManager.unregisterListener(this);
            stopSelf();
        }



        }




    private void saveData(String accelData) {
        try {
            FileOutputStream fout=openFileOutput("SensorDataKomplete.txt",MODE_APPEND);
            Date d= Calendar.getInstance().getTime();
            OutputStreamWriter osw=new OutputStreamWriter(fout);
            osw.write(d.toString()+"\n------------------------------\n"+accelData);
            osw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
