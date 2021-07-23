package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class SearchFragment extends Fragment {


    // TODO: Rename and change types of parameters
String place;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        final EditText txtsearch=view.findViewById(R.id.txtsearch);

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                place=txtsearch.getText().toString();
                place=place.toUpperCase();
                if(place.length()!=0) {
                    BackgroundWorker bw = new BackgroundWorker();
                    bw.execute();
                }
                else{
                   place="NOTHING";
                    BackgroundWorker bw = new BackgroundWorker();
                    bw.execute();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;


    }

    public class BackgroundWorker extends AsyncTask<Void,Void,Void> {
        ProgressDialog p;
        String result;
        ArrayList<Places> places;


        @Override
        protected Void doInBackground(Void... Voids) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link=serverURL.getUrl()+"/bruneibus/android/loadallplaces.php";

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
//            p=new ProgressDialog(getActivity().getApplicationContext());
            //   p.setMessage("Logging in");
            // p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray data=new JSONArray(result);

                places = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject j = data.getJSONObject(i);


                        String area=j.getString("AREA");
                        String name=j.getString("LANDMARK");


                        if(name.contains(place) || area.contains(place)){
                            Places p = new Places(j.getString("ID"), j.getString("LANDMARK"), j.getString("TYPE"), j.getString("AREA"), j.getString("LAT"), j.getString("LNG"));
                            places.add(p);

                        }

                    }


                // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(radius),Toast.LENGTH_LONG).show();
                if(places.size()>0) {
                    RecyclerView rw = getActivity().findViewById(R.id.recyclerview);
                    rw.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                    PlacesAdapter adapter = new PlacesAdapter(getActivity().getApplicationContext(),places);
                    rw.setAdapter(adapter);
                }
                else {
                    RecyclerView rw = getActivity().findViewById(R.id.recyclerview);
                    rw.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                    PlacesAdapter adapter = new PlacesAdapter(getActivity().getApplicationContext(),places);
                    rw.setAdapter(adapter);
                    Toast.makeText(getActivity(),"No Results",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            p.dismiss();
        }
    }


}
