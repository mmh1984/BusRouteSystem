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

public class Reviews extends AppCompatActivity {
String busno,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Bundle b=getIntent().getExtras();
        busno=b.getString("busno");
        user=b.getString("user");
        //user="maynard@kemuda.com";

        TextView tvBus=findViewById(R.id.tvArea);
        tvBus.setText("Reviews for Bus no: " + busno);
        BackgroundWorker bw=new BackgroundWorker();
        bw.execute(busno);
    }

    public class BackgroundWorker extends AsyncTask<String, Void, Void> {

        String result="nothing";
        ProgressDialog p;
        ArrayList<ReviewClass> reviewClasses;

        @Override
        protected Void doInBackground(String... strings) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link = serverURL.getUrl()+"/bruneibus/android/viewcomments.php?busno=" + strings[0];

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
            p = new ProgressDialog(Reviews.this);
            p.setMessage("Loading Reviews");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            JSONArray data = null;
            try {
                data = new JSONArray(result);
               //Toast.makeText(getApplicationContext(),String.valueOf(result),Toast.LENGTH_LONG).show();
               reviewClasses= new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);

                    String id=object.getString("id");
                    String email=object.getString("email");
                    String comments=object.getString("comments");
                     String rating=object.getString("ratings");
                    String date=object.getString("date");

                    ReviewClass b = new ReviewClass(id,busno,email,comments,date,rating);
                   reviewClasses.add(b);


                }

                if(result!=null) {

                    RecyclerView rw = findViewById(R.id.recyclerviewreviews);
                    rw.setLayoutManager(new LinearLayoutManager(Reviews.this));

                   ReviewAdapter adapter = new ReviewAdapter(Reviews.this, reviewClasses,user);
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
    public void onBackPressed() {
        finish();
    }
}
