package com.smappdevelopers.smapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("NADA", key +" : "+ value);
            }
        }

        String url = "https://www.smapp.com.ar/?from_app";
        webview = (WebView) this.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUserAgentString("com.smappdevelopers.smapp:1");
        if (isConnected()) {
            webview.loadUrl(url);
            webview.setWebViewClient(new MyWebViewClient());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                HomeActivity.this.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
                return true;
            } else if (url.startsWith("mailto:")) {
                url = url.substring(7);
                Intent mail = new Intent("android.intent.action.SEND");
                mail.setType("application/octet-stream");
                mail.putExtra("android.intent.extra.EMAIL", new String[]{url});
                mail.putExtra("android.intent.extra.SUBJECT", "");
                mail.putExtra("android.intent.extra.TEXT", "");
                HomeActivity.this.startActivity(mail);
                return true;
            } else if (url.startsWith("share:")) {
                url = url.substring(6);
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("text/plain");
                i.putExtra("android.intent.extra.SUBJECT", "Sharing URL");
                i.putExtra("android.intent.extra.TEXT", url);
                HomeActivity.this.startActivity(Intent.createChooser(i, "Share URL"));
                return true;
            } else if (url.startsWith("geo:")) {
                HomeActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.com/maps?z=8&q=" + url.substring(4))));
                return true;
            } else if (url.startsWith("route:")) {
                HomeActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + url.substring(6))));
                return true;
            } else if (!Uri.parse(url).getHost().equals("www.smapp.com.ar")) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse(url));
                HomeActivity.this.startActivity(i);
                return true;
            } else if (HomeActivity.this.isConnected()) {
                return false;
            } else {
                Toast.makeText(HomeActivity.this.getApplicationContext(), "Sin conexión a internet", 1).show();
                return true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    }
                    else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
