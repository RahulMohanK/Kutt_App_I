package com.example.root.kutt_app_i;

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


        insertData();
        showData();





    }


    public void insertData(){

        String text = Clipboard_Utils.getDataFromClipboard(BackgroundActivity.this);
        String[] text1 = text.split(":");

        if (!text.equals("")){

            if(text1[0].equals("http") || text1[0].equals("https")  ) {


                boolean isInserted = myDb.insertData(text);


                if (isInserted == true) {

                    Toast.makeText(BackgroundActivity.this, "Data Inserted ", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(BackgroundActivity.this, "Not a legal link", Toast.LENGTH_SHORT).show();


                }


            }

            else
                Toast.makeText(BackgroundActivity.this, "Clipboard is empty.", Toast.LENGTH_SHORT).show();

        }

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
