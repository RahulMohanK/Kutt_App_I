package com.example.root.kutt_app_i;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainWeb extends AppCompatActivity {

    WebView mWebview;
    String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main2);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView tit = findViewById(R.id.toolbar_title);
        TextView url = findViewById(R.id.url);
        LinearLayout head = findViewById(R.id.header);
        ImageView open = findViewById(R.id.open_in_browser);
        ImageView back = findViewById(R.id.back);
        tit.setText("Loading...");
        getSupportActionBar().setTitle("");
        mWebview = findViewById(R.id.webview);
        mWebview.setWebViewClient(new WebViewClient());
        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        //MainWeb.this.setTitle(link);
        if (link.length() > 50) {
            url.setText(link.substring(0, 47) + "...");
        } else {
            url.setText(link);
        }
        getSupportActionBar().setElevation(0);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setSupportZoom(true);
        //mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.getSettings().setLoadWithOverviewMode(true);// enable javascript

        final Activity activity = this;
        head.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) MainWeb.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);//Get Clipboard Manager
                ClipData clip = ClipData.newPlainText(
                        "clipboard data ",mWebview.getUrl() );//Save plain text data to clip data
                clipboard.setPrimaryClip(clip);//set clip data as primary clip
                Toast.makeText(MainWeb.this,"Link Copied to Clipboard",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mWebview.getUrl()));
                startActivity(intent);
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.length() > 30) {
                    tit.setText(title.substring(0, 27) + "...");
                } else {
                    tit.setText(title);
                }
            }

        });
        mWebview.loadUrl(link);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWebview != null){
                    mWebview.setTag(null);
                    mWebview.clearHistory();
                    mWebview.removeAllViews();
                    mWebview.clearView();
                    mWebview.destroy();
                    mWebview = null;
                }
                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(mWebview != null){
            mWebview.setTag(null);
            mWebview.clearHistory();
            mWebview.removeAllViews();
            mWebview.clearView();
            mWebview.destroy();
            mWebview = null;
        }
        finish();
    }

}