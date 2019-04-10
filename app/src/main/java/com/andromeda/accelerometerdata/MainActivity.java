package com.andromeda.accelerometerdata;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public BluetoothAdapter bluetoothAdapter;
    public int REQUEST_ENABLE_BT = 1;
    public boolean bluetoothIsEnabled = false;

    private SensorManager sensorManager;
    private Sensor sensor;
    TextView textView1, textView2, textView3;

    float gravity[] = {0, 0, 0};
    float linear_acceleration[] = {0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBluetooth();

        textView1 = findViewById(R.id.text_view_1);
        textView2 = findViewById(R.id.text_view_2);
        textView3 = findViewById(R.id.text_view_3);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        textView1.setText(Float.toString(linear_acceleration[0]));
        textView2.setText(Float.toString(linear_acceleration[1]));
        textView3.setText(Float.toString(linear_acceleration[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bluetoothIsEnabled = true;
                getPairedDevicesList();
            }
            if (requestCode == RESULT_CANCELED) {
                bluetoothIsEnabled = false;
            }
        }
    }


    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

        }
    }

    void getPairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                Log.i("Name", deviceName);
                Log.i("id", deviceHardwareAddress);
            }
        }

    }
    

}
