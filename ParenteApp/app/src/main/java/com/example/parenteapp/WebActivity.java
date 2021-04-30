package com.example.parenteapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.example.parenteapp.Globals.myAppID;
import static com.example.parenteapp.Globals.receiveCallID;

public class WebActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }
        init();
    }

    private void init(){
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("http://exitme")) {
                    String sURL = "https://bettercallpepper.altervista.org/api/updateCallStatus.php?parid="+myAppID+"&eldid="+getIntent().getExtras().getInt("id")+"&status=0";

                    // Connect to the URL using java's native library
                    URL uurl = null;
                    try {
                        uurl = new URL(sURL);
                        URLConnection request = uurl.openConnection();
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(request.getInputStream()));
                        String inputLine;
                        StringBuffer content = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        System.out.println("Url opened");
                        //request.connect();
                    } catch (Exception e) {
                        System.out.println("Erroreeeee");
                        e.printStackTrace();
                    }
                    view.loadUrl("");
                    finish();  // close activity
                }
                else
                    view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // there is a page called back when jumping
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // callback after the end of the page jump
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
//		String url = "<html><frameset cols=\"25%,25%\" FRAMEBORDER=NO FRAMESPACING=0 BORDER=0><frame src=\"https://titansteam.altervista.org/VideoChat/?room=nomeroom&mode=c><frame src=\"file:///sdcard/left.htm\" /> </frameset></html>";
//
//		mWebView.loadDataWithBaseURL("", url, "text/html", "utf-8", "");

        String[] permissions =
                {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA};

        ActivityCompat.requestPermissions(
                this,
                permissions,
                1010);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String sURL;
        String url;
        System.out.println("Type: " + getIntent().getExtras().getInt("type"));
        if(getIntent().getExtras().getInt("type") != 0)
            sURL = "https://bettercallpepper.altervista.org/api/addParentCall.php?parid=" + myAppID + "&eldid=" + getIntent().getExtras().getInt("id");
        else
            sURL = "https://bettercallpepper.altervista.org/api/updateCall.php?parid=" + myAppID + "&eldid=" + receiveCallID+"&status=-";
        // Connect to the URL using java's native library
        URL addurl = null;
        try {
            addurl = new URL(sURL);
            URLConnection request = addurl.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        } catch (Exception e) { }


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBlockNetworkLoads(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_NO_CACHE );
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");

        if(getIntent().getExtras().getInt("type") != 0)
            url = "https://bettercallpepper.altervista.org/VideoChat/?room="+myAppID + "_" + getIntent().getExtras().getInt("id") +"&mode=c";
        else
            url = "https://bettercallpepper.altervista.org/VideoChat/?room="+myAppID + "_" + receiveCallID +"&mode=j";

        System.out.println(url);

        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
        noCacheHeaders.put("Pragma", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-cache");
        mWebView.loadUrl(url, noCacheHeaders);

    }
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        int close = 0;
        Intent setIntent = new Intent(this, MainActivity.class);
        Log.d("CDA", "onBackPressed Called");
        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
        builder.setTitle("Stai per chiudere la chiamata. Sei sicuro?");

        //if the response is positive in the alert
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mWebView.loadUrl("");
                startActivity(setIntent);
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WebActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

