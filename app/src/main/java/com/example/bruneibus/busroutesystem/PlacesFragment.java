package com.example.bruneibus.busroutesystem;


import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment {
    double radius=0;
    String type="";
    Spinner spinner,spinner1;
    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_places, container, false);
        spinner=view.findViewById(R.id.spinnerdistance);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.radius,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner1=view.findViewById(R.id.spinnertype);
        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(getActivity(),R.array.category,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected=spinner.getSelectedItem().toString();
                radius=Double.parseDouble(selected);
                //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(radius),Toast.LENGTH_LONG).show();
                BackgroundWorker bw=new BackgroundWorker();
                bw.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type=spinner1.getSelectedItem().toString();

                //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(radius),Toast.LENGTH_LONG).show();
                BackgroundWorker bw=new BackgroundWorker();
                bw.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackgroundWorker bw=new BackgroundWorker();
        bw.execute();


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




                    GPSTracker gpsTracker=new GPSTracker(getActivity().getApplicationContext());
                    Location l=gpsTracker.getLocation();

                    if (l != null) {
                        double lat = l.getLatitude();
                        double lng = l.getLongitude();
                        double endlat = Double.parseDouble(j.getString("LAT"));
                        double endlng = Double.parseDouble(j.getString("LNG"));
                        String t=j.getString("TYPE");
                        Location to = new Location("Point B");
                        to.setLatitude(endlat);
                        to.setLongitude(endlng);
                        double distance;
                        distance = l.distanceTo(to) / 1000;

                        if(distance < radius && type.endsWith(t)){
                            Places p = new Places(j.getString("ID"), j.getString("LANDMARK"), j.getString("TYPE"), j.getString("AREA"), j.getString("LAT"), j.getString("LNG"));
                            places.add(p);

                        }

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
