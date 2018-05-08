package com.example.root.kutt_app_i;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        final String title_T = listen.getTitle();
        final byte[] icon_T = listen.getIcon();
        if(listen.getStar()==0){
            holder.fav.setVisibility(View.GONE);
            holder.del.setVisibility(View.VISIBLE);
        }
        else {
            holder.fav.setVisibility(View.VISIBLE);
            holder.del.setVisibility(View.GONE);
        }
        if(link.length() > 40) {
            holder.linkc =link.substring(0, 37) + "...";
            holder.Name.setText(holder.linkc);
        }else {
            holder.Name.setText(link);
            holder.linkc =link;
        }
        if(title_T == null ||  icon_T == null || title_T.equals("Web page not available")) {
            holder.got_icon = false;
            holder.got_tittle = false;
            if (holder.mWebview != null) {
                holder.mWebview.setWebViewClient(new WebViewClient());
                holder.mWebview.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);

                        // Your custom code.
                    }

                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        if (title.length() > 30) {
                            holder.title.setText(title.substring(0, 27) + "...");
                        } else {
                            holder.title.setText(title);
                        }
                        new DatabaseHelper(context).add_title(title,link);
                        holder.got_tittle = true;
                        if (holder.got_tittle && holder.got_icon) {
                            holder.mWebview.setTag(null);
                            holder.mWebview.clearHistory();
                            holder.mWebview.removeAllViews();
                            holder.mWebview.clearView();
                            holder.mWebview.destroy();
                            holder.mWebview = null;
                        }
                    }

                    @Override
                    public void onReceivedIcon(WebView view, Bitmap icon) {
                        super.onReceivedIcon(view, icon);
                        holder.web.setImageBitmap(icon);
                        holder.got_icon = true;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        icon.compress(Bitmap.CompressFormat.PNG, 0, stream);
                        new DatabaseHelper(context).add_icon(stream.toByteArray(),link);
                        if (holder.got_tittle && holder.got_icon) {
                            holder.mWebview.setTag(null);
                            holder.mWebview.clearHistory();
                            holder.mWebview.removeAllViews();
                            holder.mWebview.clearView();
                            holder.mWebview.destroy();
                            holder.mWebview = null;
                        }
                    }
                });
                holder.mWebview.loadUrl(link);
            }
        }else {
            if (title_T.length() > 30) {
                holder.title.setText(title_T.substring(0, 27) + "...");
            } else {
                holder.title.setText(title_T);
            }
            if(icon_T != null) {
                holder.web.setImageBitmap(BitmapFactory.decodeByteArray(icon_T, 0, icon_T.length));
            }

        }
        holder.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MainWeb.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("link",listen.getLink());
                context.startActivity(i);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Clipboard_Utils.copyToClipboard(context,link);
                return false;
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                myDb.updateData(link);
                Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show();
                holder.del.setVisibility(View.GONE);
                holder.fav.setVisibility(View.VISIBLE);
            }
        });
       /*holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        });*/
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.share.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                if(!link.substring(0,26).equals("http://kutt.fossgect.club/")) {
                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent i = new Intent();
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setAction(Intent.ACTION_SEND);
                                    i.putExtra(Intent.EXTRA_TEXT, response);
                                    i.setType("text/plain");
                                    holder.share.setVisibility(View.VISIBLE);
                                    holder.progressBar.setVisibility(View.GONE);
                                    context.startActivity(i);
                                    Toast.makeText(context,"Sharing shortened link",Toast.LENGTH_SHORT).show();
                                    requestQueue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Intent i = new Intent();
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setAction(Intent.ACTION_SEND);
                                    i.putExtra(Intent.EXTRA_TEXT, link);
                                    i.setType("text/plain");
                                    holder.share.setVisibility(View.VISIBLE);
                                    holder.progressBar.setVisibility(View.GONE);
                                    context.startActivity(i);
                                    error.printStackTrace();
                                    requestQueue.stop();
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("url", link);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
                else {
                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setAction(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_TEXT, link);
                    i.setType("text/plain");
                    holder.share.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    context.startActivity(i);
                }

            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                myDb.updateNormal(link);
                holder.fav.setVisibility(View.GONE);
                holder.del.setVisibility(View.VISIBLE);
                Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show();
            }
        });
        /*holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                myDb.updateData(link);
                //Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                if(myDb.deletelink(link)){
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.confirm.setVisibility(View.GONE);
                    holder.card.setVisibility(View.GONE);
                    holder.exp.setVisibility(View.GONE);
                    Toast.makeText(context, "Link Removed Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.confirm.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        });
        /*holder.cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!link.substring(0,26).equals("http://kutt.fossgect.club/")) {
                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context,"Link shortened",Toast.LENGTH_SHORT).show();
                                    Clipboard_Utils.copyToClipboard(context,response);
                                    requestQueue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Something went wrong,try again later", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                    requestQueue.stop();
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("url", link);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
                else {
                    Toast.makeText(context,"This link can't be shortened further",Toast.LENGTH_SHORT).show();
                }
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return listenItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Name,title;
        public LinearLayout linearLayout,confirm,exp,card;
        private ImageView clip,del,share,web,fav;
        private Button cancel,delete;
        //private CardView card;
        private String linkc,link;
        private WebView mWebview;
        Boolean got_tittle,got_icon;
        ProgressBar progressBar;
        //final ListenItem listen = listenItems.get(position);

        public ViewHolder(final View ItemView, Context context, List<ListenItem> listenItems) {
            super(ItemView);
            card = ItemView.findViewById(R.id.card);
           // cut = ItemView.findViewById(R.id.cut);
            title = ItemView.findViewById(R.id.title);
            confirm = ItemView.findViewById(R.id.confirm);
            fav = ItemView.findViewById(R.id.fav);
            delete = ItemView.findViewById(R.id.delete);
            cancel = ItemView.findViewById(R.id.cancel);
            share = ItemView.findViewById(R.id.share);
            del = ItemView.findViewById(R.id.del);
            progressBar = ItemView.findViewById(R.id.progressBar);
            //clip = ItemView.findViewById(R.id.clip);
            Name = ItemView.findViewById(R.id.textViewName);
            //exp = ItemView.findViewById(R.id.expand);
            web = ItemView.findViewById(R.id.web);
            linearLayout=ItemView.findViewById(R.id.linearlayout);
            mWebview = ItemView.findViewById(R.id.webview);
            //mWebview = new WebView(context);
        }




    }
}