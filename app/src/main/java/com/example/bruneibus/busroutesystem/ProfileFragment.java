package com.example.bruneibus.busroutesystem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        Session user=new Session(getActivity().getApplicationContext());

        //Toast.makeText(getActivity().getApplicationContext(),"hello " + user.getusename(),Toast.LENGTH_SHORT).show();
        BackgroundWorker bw= new BackgroundWorker();
        bw.execute(user.getusename());


        return view;

    }
    public class BackgroundWorker extends AsyncTask<String,Void,Void> {
        ProgressDialog p;
        String result;



        @Override
        protected Void doInBackground(String...strings) {
            //1: get the parameters

            //2: set the url parameter
            ServerURL serverURL=new ServerURL();
            String link=serverURL.getUrl()+"/bruneibus/android/loadprofile.php?email="+strings[0];

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
             p.setMessage("Fetching Profile");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray data=new JSONArray(result);


               final TextView fullname=getView().findViewById(R.id.tvfullname);
             final  TextView email=getView().findViewById(R.id.tvemail);
              TextView datejoined=getView().findViewById(R.id.tvdatejoined);
                Button btnedit=getView().findViewById(R.id.btneditprofile);
                String id="";
                for (int i = 0; i < data.length(); i++) {
                    JSONObject j = data.getJSONObject(i);

                    id=j.getString("id");
                  fullname.setText(j.getString("fullname"));
                 email.setText(j.getString("email"));
                  datejoined.setText(j.getString("datejoined"));




                }
                final String userid=id;
                btnedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity().getApplicationContext(),EditProfile.class);
                        i.putExtra("fullname",fullname.getText());
                        i.putExtra("email",email.getText());
                        i.putExtra("id",userid);
                        startActivity(i);
                        getActivity().finish();
                    }
                });

                // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(radius),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            p.dismiss();
        }
    }
}
