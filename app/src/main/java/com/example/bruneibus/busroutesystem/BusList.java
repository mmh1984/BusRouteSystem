package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class BusList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        TextView tvarea=findViewById(R.id.tvArea);

        String placeid=getIntent().getExtras().getString("placeid");
        String placename=getIntent().getExtras().getString("name");
        tvarea.setText("Buses in " + placename);
        BackgroundWorker bw=new BackgroundWorker();
        bw.execute(placeid);
    }

    public class BackgroundWorker extends AsyncTask<String, Void, Void> {

        String result="nothing";
        ProgressDialog p;
        ArrayList<BusClassCustom> busClassCustoms;

        @Override
        protected Void doInBackground(String... strings) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link = serverURL.getUrl()+"/bruneibus/android/loadbus2.php?placeid=" + strings[0];

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
            p = new ProgressDialog(BusList.this);
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
                busClassCustoms= new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);


                    BusClassCustom b = new BusClassCustom(object.getString("busno"));
                   busClassCustoms.add(b);


                }

                if(result!=null) {
                    // Toast.makeText(getApplicationContext(), String.valueOf(routes.size()),Toast.LENGTH_LONG).show();
                    RecyclerView rw = findViewById(R.id.recyclerview);
                    rw.setLayoutManager(new LinearLayoutManager(BusList.this));

                    BusAdapterCustom adapter = new BusAdapterCustom(BusList.this, busClassCustoms);
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
}
