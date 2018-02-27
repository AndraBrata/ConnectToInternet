package com.app.andra.connecttointernet;
/**
 * Created by Komang Candra Brata on 10/2/2017.
 */

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsynctaskActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    private String webcontent;
    private TextView textView;
    private Button b1,b2;
    private ImageView img;
    private HttpURLConnection httpConn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        img = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadImage("http://1.bp.blogspot.com/-r4_dSLP80g0/Ty3lIThZCWI/AAAAAAAAAhs/_U5_k2a73Tw/s1600/dota+2+logo+white.png");
            }
        });



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadWebContent("http://plantplaces.com/perl/mobile/viewplantsjson.pl?Combined_Name=Merlot");

            }
        });


    }
    private void downloadImage(String urlStr) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connMgr =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check for network connections

        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data

            new DownloadImageTask().execute( urlStr);
        } else {
            textView.setText("No network connection available.");
        }


    }


    private void downloadWebContent(String urlStr){

        ConnectivityManager connMgr =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check for network connections

        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data

            new DownloadWebTask().execute( urlStr);
        } else {
            textView.setText("No network connection available.");
        }


    }




    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AsynctaskActivity.this) ;
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }


        @Override
        protected Bitmap doInBackground(String... params) {


            String url = params[0];
            InputStream in = null;

            try {
                in = openHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            img.setImageBitmap(result);
            httpConn.disconnect();
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        private InputStream openHttpConnection(String urlStr) {
            InputStream in = null;
            int resCode = -1;

            try {
                URL url = new URL(urlStr);

                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setReadTimeout(10000 /* milliseconds */);
                httpConn.setConnectTimeout(15000 /* milliseconds */);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return in;
        }

    }






    public class DownloadWebTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AsynctaskActivity.this) ;
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {


            String url = params[0];
            InputStream in = null;

            try {
                StringBuilder sb = new StringBuilder();
                in = openHttpConnection(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                    webcontent = sb.toString();
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", "doInBackground: Input stream process" );
            }

            return webcontent;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            textView.setText(result);
            httpConn.disconnect();
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        private InputStream openHttpConnection(String urlStr) {
            InputStream in = null;
            int resCode = -1;

            try {
                URL url = new URL(urlStr);

                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setReadTimeout(10000 /* milliseconds */);
                httpConn.setConnectTimeout(15000 /* milliseconds */);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();

                }
                else
                {
                    Log.e ("HTTP", "openHttpConnection: Host not reached" );
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("Error", "openHttpConnection: URL not correct",e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", "openHttpConnection: host not reach",e);
            }
            return in;
        }


    }

}
