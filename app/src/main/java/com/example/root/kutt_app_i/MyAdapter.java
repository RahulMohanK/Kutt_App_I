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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

        holder.Name.setText(listen.getLink());
        holder.card.setVisibility(View.VISIBLE);

        holder.clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = listen.getLink();
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

        private TextView Name, Phone;
        public LinearLayout linearLayout,confirm;
        private ImageView clip,del;
        private Button cancel,delete;
        private CardView card;


        public ViewHolder(final View ItemView, Context context, List<ListenItem> listenItems) {
            super(ItemView);
            card = ItemView.findViewById(R.id.card);
            confirm = ItemView.findViewById(R.id.confirm);
            delete = ItemView.findViewById(R.id.delete);
            cancel = ItemView.findViewById(R.id.cancel);
            del = ItemView.findViewById(R.id.del);
            clip = ItemView.findViewById(R.id.clip);
            Name = (TextView) ItemView.findViewById(R.id.textViewName);

            linearLayout=(LinearLayout)ItemView.findViewById(R.id.linearlayout);
        }



    }
}