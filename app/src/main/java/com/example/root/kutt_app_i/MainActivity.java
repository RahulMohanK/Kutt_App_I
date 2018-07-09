package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {


    DatabaseHelper myDb;
    TextView data,shlink,Bartitle;
    ImageView save,cut,copy,sharelink,profile;
    String text;
    LinearLayout got,sharel,star;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);
        String manufacturer = "xiaomi";
        /*if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            //this will open auto start screen where user can enable permission for your app2
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }*/

       //show_text = (Button) findViewById(R.id.show_text);//
       // Intent intent = new Intent(MainActivity.this,TheService.class);
       // startService(intent);
        Bartitle = findViewById(R.id.toolbar_title);
        profile = findViewById(R.id.profile);
        got = findViewById(R.id.button);
        sharel = findViewById(R.id.shortl);
        copy = findViewById(R.id.copy);
        progressBar = findViewById(R.id.progressBar);
        sharelink = findViewById(R.id.sharelink);
        data = findViewById(R.id.clipboard_data);
        save = findViewById(R.id.save);
        star = findViewById(R.id.star);
        save.setVisibility(View.GONE);
        shlink = findViewById(R.id.shortlink);
        sharel.setVisibility(View.GONE);
        cut = findViewById(R.id.cut);
        cut.setVisibility(View.GONE);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this,StarActivity.class);
                startActivity(j);
            }
        });
        sharelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = shlink.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clipboard_Utils.copyToClipboard(MainActivity.this,shlink.getText().toString());
            }
        });
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.length()>=27) {
                    if (!text.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        cut.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        cut.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        sharel.setVisibility(View.VISIBLE);
                                        shlink.setText(response);
                                        requestQueue.stop();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        cut.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Something went wrong,try again later", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();

                                        requestQueue.stop();
                                    }

                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("url", text);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(MainActivity.this, "This link can't be shortened further", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    cut.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    cut.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    sharel.setVisibility(View.VISIBLE);
                                    shlink.setText(response);
                                    requestQueue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    cut.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Something went wrong,try again later", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();

                                    requestQueue.stop();
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("url", text);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
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
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                text = Clipboard_Utils.getDataFromClipboard(MainActivity.this);
                if(text.length()>=27) {
                    if (!text.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        sharel.setVisibility(View.GONE);
                    }
                }else {
                    sharel.setVisibility(View.GONE);
                }
                get_data();

            }
        });

    }
    public void get_data(){
        text = Clipboard_Utils.getDataFromClipboard(MainActivity.this);
        String[] text1 = text.split(":");
        if (!text.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                if(text.length() > 62) {
                    data.setText(text.substring(0, 59) + "...");
                }else {
                    data.setText(text);
                }

                save.setVisibility(View.VISIBLE);
                cut.setVisibility(View.VISIBLE);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(isMyServiceRunning(TheService.class)) {
                stopService(new Intent(MainActivity.this, TheService.class));
                ed.putInt("mode",0);
                ed.apply();
                Toast.makeText(MainActivity.this,"Service Stopped!",Toast.LENGTH_SHORT).show();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getApplicationContext().startForegroundService(new Intent(getApplicationContext(), TheService.class));
                }
                else {
                    getApplicationContext().startService(new Intent(getApplicationContext(), TheService.class));
                }
                ed.putInt("mode",1);
                ed.apply();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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