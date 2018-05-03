package com.example.root.kutt_app_i;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by RahulKMohan on 07-01-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<ListenItem> listenItems;


    Context context;
    int lastPosition = -1;
    int ex=1;


    public MyAdapter(List<ListenItem> listenItems, Context context) {
        this.listenItems = listenItems;
        this.context = context;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listen, parent, false);
        return new ViewHolder(v, context, listenItems);
    }


    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {

        final ListenItem listen = listenItems.get(position);
        final String link = listen.getLink();
        if(link.length() > 50) {
            holder.linkc =link.substring(0, 47) + "...";
            holder.Name.setText(holder.linkc);
        }else {
            holder.Name.setText(link);
            holder.linkc =link;
        }

        /*holder.card.setVisibility(View.VISIBLE);
        holder.mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    holder.Name.setText(title);
                }
            }
        });
        holder.mWebView.loadUrl(link);*/
        holder.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MainWeb.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("link",listen.getLink());
                context.startActivity(i);
            }
        });
        holder.clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clipboard_Utils.copyToClipboard(context,link);
                //Toast.makeText(context,link,Toast.LENGTH_LONG).show();
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.setVisibility(View.GONE);
                holder.confirm.setVisibility(View.VISIBLE);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ex==1) {
                    holder.exp.setVisibility(View.VISIBLE);
                    holder.Name.setText(link);
                    ex=0;
                }else {
                    holder.exp.setVisibility(View.GONE);
                    holder.Name.setText(holder.linkc);
                    ex=1;
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, link);
                i.setType("text/plain");
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                //Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                if(myDb.deletelink(link)){
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.confirm.setVisibility(View.GONE);
                    holder.card.setVisibility(View.GONE);
                    holder.exp.setVisibility(View.GONE);
                    Toast.makeText(context, "Link Removed Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.confirm.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        });



        /*if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.item_animation_fall_down);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }*/

    }

    @Override
    public int getItemCount() {
        return listenItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView Name;
        public LinearLayout linearLayout,confirm,exp;
        private ImageView clip,del,share,web;
        private Button cancel,delete;
        private CardView card;
        public String linkc;


        public ViewHolder(final View ItemView, Context context, List<ListenItem> listenItems) {
            super(ItemView);
            card = ItemView.findViewById(R.id.card);
            confirm = ItemView.findViewById(R.id.confirm);
            delete = ItemView.findViewById(R.id.delete);
            cancel = ItemView.findViewById(R.id.cancel);
            share = ItemView.findViewById(R.id.share);
            del = ItemView.findViewById(R.id.del);
            clip = ItemView.findViewById(R.id.clip);
            Name = ItemView.findViewById(R.id.textViewName);
            exp = ItemView.findViewById(R.id.expand);
            web = ItemView.findViewById(R.id.web);


            linearLayout=(LinearLayout)ItemView.findViewById(R.id.linearlayout);
        }



    }
}