package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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

import com.github.wihoho.jama.Matrix;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.example.pepperapp28aprile.Globals.myAppID;
import static com.example.pepperapp28aprile.Globals.receiveCallID;
import static com.example.pepperapp28aprile.MainMenuFragment.downloadFile;

public class WebActivityRecognition extends AppCompatActivity {
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

    private Matrix vectorize(Matrix input) {
        int m = input.getRowDimension();
        int n = input.getColumnDimension();

        Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;
    }

    private void riconosci(View view){

        String name = getIntent().getExtras().getString("name");
        String surname = getIntent().getExtras().getString("surname");
            try {
                downloadFile(new URL("https://bettercallpepper.altervista.org/Riconoscimento/login.pgm"),Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/faces/login.pgm");
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/faces/login.pgm"); //WebRiconoscimento
            String labelAttesa = name + "_" + surname.replace(" ", "_");
            String labelFound = null;
            try {
                labelFound = MyListAdapter.trainer.recognize(vectorize(MyFileManager.convertPGMtoMatrix(file.getPath())));
                System.out.println("riconosco "+ labelFound);
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            System.out.println("label attesa: " + labelAttesa);
            if(labelAttesa.equals(labelFound))
                startProfile(view,name);
            else{
                //Context context = getApplicationContext();
                CharSequence text = "Volto non riconosciuto";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(this, text, duration);
                toast.show();
            }
    }


    public void startProfile(View view, String name) {
        Intent intent = new Intent(this, ProfileActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        System.out.println("start profile");
        ProfileActivity.name = name;
        this.startActivity(intent);
    }

    public void startMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
         this.startActivity(intent);
    }


    private void init(){
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("http://exitme")) {
                    riconosci(view);
                    view.loadUrl("");
                    finish();  // close activity
                }
                else if (url.contains("http://exit2me")){
                    startMenu(view);
                    view.loadUrl("");
                    finish();
                }
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
                Toast.makeText(WebActivityRecognition.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
//		String url = "<html><frameset cols=\"25%,25%\" FRAMEBORDER=NO FRAMESPACING=0 BORDER=0><frame src=\"https://titansteam.altervista.org/VideoChat/?room=nomeroom&mode=c><frame src=\"file:///sdcard/left.htm\" /> </frameset></html>";
//
//		mWebView.loadDataWithBaseURL("", url, "text/html", "utf-8", "");

        String[] permissions =
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                };

        ActivityCompat.requestPermissions(
                this,
                permissions,
                1);

     //   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);
        String url;
        /*
        if(getIntent().getExtras().getInt("type") != 0)
            sURL = "https://bettercallpepper.altervista.org/api/addParentCall.php?eldid=" + myAppID + "&parid=" + getIntent().getExtras().getInt("id");
        else
            sURL = "https://bettercallpepper.altervista.org/api/updateCall.php?parid=" + receiveCallID + "&eldid=" + myAppID+"&status=-";
        // Connect to the URL using java's native library

        //sURL = "https://bettercallpepper.altervista.org/Riconoscimento";
        URL addurl = null;
        try {
            addurl = new URL(sURL);
            URLConnection request = addurl.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        } catch (Exception e) { }
*/

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

       /* if(getIntent().getExtras().getInt("type") != 0)
            url = "https://bettercallpepper.altervista.org/VideoChat/?room="+myAppID + "_" + getIntent().getExtras().getInt("id") +"&mode=c";
        else
            url = "https://bettercallpepper.altervista.org/VideoChat/?room="+receiveCallID + "_" + myAppID +"&mode=j";
*/

        url = "https://bettercallpepper.altervista.org/Riconoscimento/";
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

    /*
    @Override
    public void onBackPressed() {
        int close = 0;
        Intent setIntent = new Intent(this, ProfileActivity.class);
        Log.d("CDA", "onBackPressed Called");
        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivityRecognition.this);
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

     */

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
        new AlertDialog.Builder(WebActivityRecognition.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

