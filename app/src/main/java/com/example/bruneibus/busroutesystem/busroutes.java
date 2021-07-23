package com.example.bruneibus.busroutesystem;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class busroutes extends AppCompatActivity {
String user;
    String busno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busroutes);
        Bundle b=getIntent().getExtras();
        busno=b.getString("busno");

        Session session=new Session(busroutes.this);
        user=session.getusename();

        //Toast.makeText(busroutes.this,user,Toast.LENGTH_LONG).show();

       // user="maynard@kemuda.com";
        TextView tvbusno=findViewById(R.id.tvbusno);
        tvbusno.setText("Routes for Bus No: " + busno);
        ActivityCompat.requestPermissions(busroutes.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        BackgroundWorker bw=new BackgroundWorker();
        bw.execute(busno);


    }

    public class BackgroundWorker extends AsyncTask<String, Void, Void> {

        String result="nothing";
        ProgressDialog p;
        ArrayList<RouteClass> routes;

        @Override
        protected Void doInBackground(String... strings) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link = serverURL.getUrl()+"/bruneibus/android/loadroutes.php?busno=" + strings[0];

            //3:set the url link and open the connection

            try {
                //4:URL settings
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);

                //5:Bufferedreader
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(busroutes.this);
            p.setMessage("Loading places");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            JSONArray data = null;
            try {
                data = new JSONArray(result);
                // Toast.makeText(getApplicationContext(),String.valueOf(result),Toast.LENGTH_LONG).show();
                routes = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);


                    RouteClass r = new RouteClass(object.getString("id"), object.getString("name"), object.getString("type"), object.getString("area"), object.getString("lat"), object.getString("lng"));

                    routes.add(r);


                }

                if(result!=null) {
                    // Toast.makeText(getApplicationContext(), String.valueOf(routes.size()),Toast.LENGTH_LONG).show();
                    RecyclerView rw = findViewById(R.id.recyclerview);
                    rw.setLayoutManager(new LinearLayoutManager(busroutes.this));

                    RouteAdapter adapter = new RouteAdapter(busroutes.this, routes);
                    rw.setAdapter(adapter);
                }
                else {

                    Toast.makeText(getApplicationContext(),"No Results",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            p.dismiss();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.busmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
       switch(item.getItemId()){
           case R.id.writereview:
               i=new Intent(busroutes.this,WriteReviews.class);
               i.putExtra("busno",busno);
               i.putExtra("user",user);
               startActivity(i);

               break;

           case R.id.viewreview:

               i=new Intent(busroutes.this,Reviews.class);
               i.putExtra("busno",busno);
               i.putExtra("user",user);
               startActivity(i);
               break;


       }
        return true;
    }





}
