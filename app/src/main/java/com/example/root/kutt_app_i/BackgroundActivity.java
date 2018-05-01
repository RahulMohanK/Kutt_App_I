package com.example.root.kutt_app_i;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BackgroundActivity extends AppCompatActivity {



    DatabaseHelper myDb;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;


    public static String s;
    private List<ListenItem> listenItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        myDb = new DatabaseHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));




       // progressBar = (ProgressBar) findViewById(R.id.progressBar);//

        listenItems = new ArrayList<>();
        showData();





    }





    public void showData( ){


        Cursor res = myDb.getAllData();


        /*if(res.getCount()==0)
        {
            showMessage("Error","Nothing Found");
            return;
        }*/

        while(res.moveToNext()){

            ListenItem item = new ListenItem(res.getString(1));
            listenItems.add(item);
            adapter = new MyAdapter(listenItems,getApplicationContext());
            recyclerView.setAdapter(adapter);

        }



    }





}
