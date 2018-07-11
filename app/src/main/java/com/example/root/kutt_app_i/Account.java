package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class Account extends AppCompatActivity {
    Button sign_in;
    Switch notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        sign_in = findViewById(R.id.sign_in);
        notification = findViewById(R.id.notification);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account.this,Login.class);
                startActivity(i);
            }
        });
        SharedPreferences not = getSharedPreferences("notif",MODE_PRIVATE);
        final SharedPreferences.Editor editor = not.edit();
       /* notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMyServiceRunning(TheService.class) || isMyServiceRunning(SensorService.class)) {
                    stopService(new Intent(Account.this,SensorService.class));
                    stopService(new Intent(Account.this, TheService.class));
                    Toast.makeText(Account.this,"Service Stopped!",Toast.LENGTH_SHORT).show();
                }else {
                    getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
                }
            }
        });*/
        if(isMyServiceRunning(TheService.class) || isMyServiceRunning(SensorService.class)) {
            notification.setChecked(true);
        }else {
            notification.setChecked(false);
        }
       notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   editor.putInt("enable",1);
                   editor.apply();
                   getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
               }else {
                   editor.putInt("enable",0);
                   editor.apply();
                   stopService(new Intent(Account.this,SensorService.class));
                   stopService(new Intent(Account.this, TheService.class));
                   Toast.makeText(Account.this,"Service Stopped!",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
