package com.example.root.kutt_app_i;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(new Intent(getApplicationContext(), TheService.class));

            //start sensorService which inturn will start TheService
            getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
        }
        else {
//            getApplicationContext().startService(new Intent(getApplicationContext(), TheService.class));

            getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
        }
    }
}
