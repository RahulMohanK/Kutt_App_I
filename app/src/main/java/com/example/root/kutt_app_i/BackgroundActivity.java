package com.example.root.kutt_app_i;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BackgroundActivity extends AppCompatActivity {



    DatabaseHelper myDb;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    ImageView favorite,nfavorite,plus,profile,close,search;
    TextView title_text,nomatch;
    LinearLayout default_title,search_title;
    EditText search_box;
    int fa,dd,sa;


    public static String s;
    private List<ListenItem> listenItems;
    private List<ListenItem> SearchItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_background);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        favorite = findViewById(R.id.added);
        title_text = findViewById(R.id.toolbar_title);
        nfavorite = findViewById(R.id.notadded);
        plus = findViewById(R.id.plus);
        profile = findViewById(R.id.profile);
        close = findViewById(R.id.close);
        search = findViewById(R.id.search);
        default_title = findViewById(R.id.normal);
        search_title = findViewById(R.id.search_title);
        search_box = findViewById(R.id.search_bar);
        nomatch = findViewById(R.id.nomatch);
        listenItems = new ArrayList<>();
        SearchItems = new ArrayList<>();
        fa=0;sa=0;
        myDb = new DatabaseHelper(this);

        recyclerView =  findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sa=1;
                search_box.setText("");
                default_title.setVisibility(View.GONE);
                search_title.setVisibility(View.VISIBLE);
                search_box.requestFocus();
                if(fa==1) {
                    adapter = new MyAdapter(SearchItems, getApplicationContext());
                }else {
                    adapter = new MyAdapter(SearchItems, BackgroundActivity.this);
                }
                recyclerView.setAdapter(adapter);
            }
        });
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchItems.clear();
                for(ListenItem l: listenItems){
                    if(l.getLink().contains(s)){
                        SearchItems.add(l);
                    }
                }
                adapter.notifyDataSetChanged();
                if(SearchItems.isEmpty()){
                    nomatch.setVisibility(View.VISIBLE);
                }else {
                    nomatch.setVisibility(View.GONE);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sa=0;
                if(fa==1) {
                    adapter = new MyAdapter(listenItems, getApplicationContext());
                }else {
                    adapter = new MyAdapter(listenItems, BackgroundActivity.this);
                }
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                default_title.setVisibility(View.VISIBLE);
                search_title.setVisibility(View.GONE);
                nomatch.setVisibility(View.GONE);
                search_box.setText("");
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BackgroundActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BackgroundActivity.this,Login.class);
                startActivity(i);
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favdata();
                fa=1;
                favorite.setVisibility(View.GONE);
                nfavorite.setVisibility(View.VISIBLE);
                title_text.setText("Favorites");

            }
        });
        nfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
                fa=0;
               // adapter.notifyDataSetChanged();
                favorite.setVisibility(View.VISIBLE);
                nfavorite.setVisibility(View.GONE);
                title_text.setText("Saved Links");
            }
        });



        // progressBar = (ProgressBar) findViewById(R.id.progressBar);//


        showData();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final RecyclerView.ViewHolder holder = viewHolder;
                dd=0;
                String message;
                if(fa==0){
                    message="link deleted!";
                }else {
                    message="link removed from favorites!";
                }
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.layout), message, Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dd=1;
                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.layout), "Link restored!", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                                adapter.notifyItemChanged(holder.getAdapterPosition());
                            }
                        });

                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT && dd==0) {
                            diss(holder.getAdapterPosition());
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                    }
                });


            }
        }).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ListenItem listen = listenItems.get(viewHolder.getAdapterPosition());
                Uri uri = Uri.parse(listen.getLink());


                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

                intentBuilder.setShowTitle(true);
                intentBuilder.setToolbarColor(ContextCompat.getColor(BackgroundActivity.this, R.color.white));
                intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(BackgroundActivity.this, R.color.white));

                intentBuilder.setStartAnimations(BackgroundActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
                intentBuilder.setExitAnimations(BackgroundActivity.this, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(BackgroundActivity.this, uri);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);




    }



    public void favdata(){



        Cursor res = myDb.getUpdateData();
        adapter = new MyAdapter(listenItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        listenItems.clear();

        while(res.moveToNext()){
            ListenItem item = new ListenItem(res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
            listenItems.add(item);
        }
        adapter.notifyDataSetChanged();



    }

    public void showData(){


        Cursor res = myDb.getAllData();
        adapter = new MyAdapter(listenItems,BackgroundActivity.this);
        recyclerView.setAdapter(adapter);

        listenItems.clear();
        while(res.moveToNext()){

            ListenItem item = new ListenItem(res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
            listenItems.add(item);

        }
        adapter.notifyDataSetChanged();

    }
    public void diss(int pos)
    {
        final ListenItem listen = listenItems.get(pos);

        myDb = new DatabaseHelper(this);
        if(fa==0){
            myDb.deletelink(listen.getLink());
           // Toast.makeText(this,"Link deleted!!",Toast.LENGTH_LONG).show();
        }else {
            myDb.updateNormal(listen.getLink());
            //Toast.makeText(this,"Removed from favorites",Toast.LENGTH_LONG).show();
        }

        listenItems.remove(pos);
        adapter.notifyItemRemoved(pos);




    }
    @Override
     public void onResume(){
        super.onResume();
        if (fa == 1) {
            Cursor res = myDb.getUpdateData();
            listenItems.clear();
            while(res.moveToNext()){

                ListenItem item = new ListenItem(res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
                listenItems.add(item);

            }
        } else {
            Cursor res = myDb.getAllData();
            listenItems.clear();
            while(res.moveToNext()){

                ListenItem item = new ListenItem(res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
                listenItems.add(item);

            }
        }
        if(sa==0){
            adapter.notifyDataSetChanged();
        }



    }




}
