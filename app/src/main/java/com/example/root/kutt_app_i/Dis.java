package com.example.root.kutt_app_i;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Dis extends AppCompatActivity{


    private String[] data,data1;
    ViewPager viewPager;
    private List<ListenItem> listenItems;
    DatabaseHelper myDb;
    String pos;
    int value =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis);
        Intent i = getIntent();
        pos = i.getStringExtra("link");
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        viewPager = (ViewPager) findViewById(R.id.vertical_viewPager);
        listenItems = new ArrayList<>();
        myDb = new DatabaseHelper(this);
        initData();
        initView();
        Selected(pos);
        }

    private void initData() {
        Cursor res = myDb.getAllData();
        while (res.moveToNext()) {
            ListenItem item = new ListenItem(res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
            listenItems.add(item);
        }
        this.data = new String[listenItems.size()];
        this.data1 = new String[1];
    }
    private void initView() {
        for (int i = 0; i < listenItems.size(); i++) {


            this.data[i]=listenItems.get(i).getLink();
           /* PageAdapter viewPageAdapter = new PageAdapter(getSupportFragmentManager(), data);
            viewPager.setAdapter(viewPageAdapter);*/
        }

    }
    private void Selected(String value){

        data1[0] = value;
        PageAdapter viewPageAdapter = new PageAdapter(getSupportFragmentManager(),data1);
        viewPager.setAdapter(viewPageAdapter);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

    }

}


