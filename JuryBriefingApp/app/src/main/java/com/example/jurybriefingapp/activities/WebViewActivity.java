package com.example.jurybriefingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.jurybriefingapp.R;

import java.io.File;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mBar = (ProgressBar) findViewById(R.id.progressBar);
        //mWebView.clearCache(true);
        //mWebView.clearHistory();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookies(null);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        Log.e("CACHE", String.valueOf(isCacheAvailable()));
        mWebView.loadUrl("http://score.wine-trophy.com:51573");
        mWebView.setWebViewClient(new DwmWebClient());
    }


    private int isCacheAvailable(){
        File dir  = getApplicationContext().getCacheDir();
        if (dir.exists())
            return dir.listFiles().length;
        else
            return 0;
    };

    public class DwmWebClient extends WebViewClient {

        //private boolean mLoaded = false;
        //ProgressDialog progressDialog;
        private boolean _showInternalErrorPage = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
            view.loadUrl(url.getUrl().toString());
            return super.shouldOverrideUrlLoading(view, url);
            /*
            if(mLoaded){ // open in new browser window only if page already finished
                try
                {
                    mWebView.loadUrl(url);
                    //Uri uri = Uri.parse(url);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //view.getContext().startActivity(intent);
                }
                catch (Exception e)
                {
                    Log.d("webview", e.getMessage());
                }
                return true;
            }
            else
            {
                return false;
            }*/
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            /*if (progressDialog == null) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }*/
            super.onPageStarted(view, url, favicon);
            //mBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String url)
        {
            if(url.contains("/Account/Welcome")) {
                _showInternalErrorPage = false;
            }
            Log.e("CACHE", String.valueOf(isCacheAvailable()));

            /*

            if (progressDialog != null) {
                try {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }*/
            super.onPageFinished(webView, url);
            mBar.setVisibility(View.GONE);
            //mLoaded = true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if(_showInternalErrorPage) {
                mWebView.loadUrl("file:///android_asset/error.html");
            } else {
                super.onReceivedError(view, request, error);
            }
            //mBar.setVisibility(View.GONE);
            //super.onReceivedError(view, request, error);
            //loadErrorPage(view);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        /*
        public void loadErrorPage(WebView webview){
            if(webview!=null){
                String description = "desc";
                String failingUrl = "failingUrl";

                String htmlData ="<html><body><div align=\"center\" >This is the description for the load fail : "+description+"\nThe failed url is : "+failingUrl+"\n</div></body>";

                webview.loadUrl("about:blank");
                webview.loadDataWithBaseURL(null,htmlData, "text/html", "UTF-8",null);
                webview.invalidate();

            } */
    }
}
