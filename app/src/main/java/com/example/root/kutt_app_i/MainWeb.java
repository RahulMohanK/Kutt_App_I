package com.example.root.kutt_app_i;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainWeb extends AppCompatActivity {

    WebView mWebview;
    String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web_view);
        mWebview = new WebView(this);
        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        MainWeb.this.setTitle(link);
        getSupportActionBar().setElevation(0);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setSupportZoom(true);
        //mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.getSettings().setLoadWithOverviewMode(true);// enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                MainWeb.this.setTitle(view.getTitle());
            }
        });
        mWebview.loadUrl(link);
        setContentView(mWebview);

        //mWebview = findViewById(R.id.activity_main_webview);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mWebview.getUrl()));
            startActivity(intent);
            return true;
        }else if(id == R.id.copy){
            ClipboardManager clipboard = (ClipboardManager) MainWeb.this
                    .getSystemService(Context.CLIPBOARD_SERVICE);//Get Clipboard Manager
            ClipData clip = ClipData.newPlainText(
                    "clipboard data ",mWebview.getUrl() );//Save plain text data to clip data
            clipboard.setPrimaryClip(clip);//set clip data as primary clip
            Toast.makeText(MainWeb.this,"Link Copied to Clipboard",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}