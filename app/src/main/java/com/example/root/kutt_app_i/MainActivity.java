package com.example.root.kutt_app_i;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {


    DatabaseHelper myDb;
    TextView data;
    ImageView save;
    String text;
    LinearLayout got;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

       //show_text = (Button) findViewById(R.id.show_text);//

        got = findViewById(R.id.button);
        data = findViewById(R.id.clipboard_data);
        save = findViewById(R.id.save);
        save.setVisibility(View.GONE);
       //clipboardData = (TextView) findViewById(R.id.clipboard_data);//
       got.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent ii = new Intent(MainActivity.this,BackgroundActivity.class);
               startActivity(ii);
           }
       });
       get_data();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });




    }
    @Override
    public void onResume(){
        super.onResume();
        get_data();
    }
    public void get_data(){
        text = Clipboard_Utils.getDataFromClipboard(MainActivity.this);
        String[] text1 = text.split(":");
        if (!text.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                data.setText(text);
                save.setVisibility(View.VISIBLE);
            } else {
                data.setText("Clipboard doesn't contain a valid link");
            }
        }


    }
    public void insertData(){
        String[] text1 = text.split(":");
        if (!text.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                boolean isInserted = myDb.insertData(text);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Link saved", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Not a legal link", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(MainActivity.this, "Clipboard is empty.", Toast.LENGTH_SHORT).show();
        }
    }





   /* public void showData( ){

        show_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor res = myDb.getAllData();

                if(res.getCount()==0)
                {
                    showMessage("Error","Nothing Found");
                    return;
                }

                StringBuffer stringBuffer =new StringBuffer();

                while(res.moveToNext()){

                    stringBuffer.append("Link : "+ res.getString(1)+"\n\n\n");

                }

                showMessage("Data",stringBuffer.toString());
            }
        });

    }*/



   /* public void showMessage(String title,String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.show();



    }*/




}