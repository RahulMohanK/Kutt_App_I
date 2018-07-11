package com.example.root.kutt_app_i;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Dis extends AppCompatActivity {

    TextView sample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis);
        sample = findViewById(R.id.sample);
        String link = getIntent().getStringExtra("link");
        sample.setText(link);
    }
}
