package com.linda.test2;

import androidx.appcompat.app.AppCompatActivity;

/*import android.annotation.SuppressLint;*/
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView xTextView, yTextView, zTextView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable, itIsNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private float shakeThreshold = 5f;
    private Vibrator vibrator;
    private float angle = 0f;


    /*    @SuppressLint("ServiceCast")*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.println(Log.INFO, "Test", "Hej");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        zTextView = findViewById(R.id.zTextView);

        imageView = findViewById(R.id.imageView);

        imageView.setRotation(45);

/*        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);*/
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        } else {
            xTextView.setText("Accelerometer sensor is not available");
            isAccelerometerSensorAvailable = false;
        }
    }

    @Override
    public void onStart() {

        Log.println(Log.DEBUG, "Test", "onStart");
        Log.d("Test", "onStart");
        super.onStart();
    }

    @Override
    public void onBackPressed() {

        Log.d("Test", "onBackPressed");
        super.onBackPressed();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];
        xTextView.setText(currentX + "m/s2");
        yTextView.setText(currentY + "m/s2");
        zTextView.setText(currentZ + "m/s2");

        if (itIsNotFirstTime) {
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            if ((xDifference > shakeThreshold)||
                    (zDifference > shakeThreshold)||
                    (yDifference > shakeThreshold))
            {/*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                    //depricated is API 26
                }*/

                angle = angle + 10;
                imageView.setRotation(angle);
                Log.d("Test", "Rotate image");
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        itIsNotFirstTime = true;

    }

        @Override
        public void onAccuracyChanged(Sensor sensor,int i){

        }

        @Override
        protected void onResume() {
            super.onResume();

            if (isAccelerometerSensorAvailable)
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        protected void onPause() {
            super.onPause();
            if (isAccelerometerSensorAvailable)
                sensorManager.unregisterListener(this);
    }
}
