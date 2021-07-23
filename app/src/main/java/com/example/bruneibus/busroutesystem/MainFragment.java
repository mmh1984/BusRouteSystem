package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackgroundWorker bw=new BackgroundWorker();
        bw.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public class BackgroundWorker extends AsyncTask<Void,Void,Void> {
        ProgressDialog p;
        String result;
        ArrayList<BusClass> buses;


        @Override
        protected Void doInBackground(Void... Voids) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link=serverURL.getUrl()+"/bruneibus/android/loadbus.php";

            //3:set the url link and open the connection

            try {
                //4:URL settings
                URL url = new URL(link);
                HttpClient client=new DefaultHttpClient();
                HttpGet request=new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response=client.execute(request);

                //5:Bufferedreader
                BufferedReader in=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb=new StringBuffer("");
                String line="";

                while((line=in.readLine())!=null){
                    sb.append(line);
                }
                result=sb.toString();






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
      p=new ProgressDialog(getActivity());
           p.setMessage("Fetching Bus Details");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray data=new JSONArray(result);

                    buses = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject j = data.getJSONObject(i);
                        BusClass b = new BusClass(j.getString("busno"), j.getString("routes"));
                        buses.add(b);


                    }

                    if(buses.size()>0) {
                        RecyclerView rw = getActivity().findViewById(R.id.recyclerview);
                        rw.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                        BusAdapter adapter = new BusAdapter(getActivity().getApplicationContext(), buses);
                        rw.setAdapter(adapter);
                    }
                    else {

                        Toast.makeText(getActivity(),"No Results",Toast.LENGTH_LONG).show();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           p.dismiss();
        }
    }

}
