package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText txtfullname=findViewById(R.id.etfullname);
        final EditText txtemail=findViewById(R.id.etemail);
        final EditText txtpassword=findViewById(R.id.etpassword);
        txtfullname.setText(getIntent().getExtras().get("fullname").toString());
        txtemail.setText(getIntent().getExtras().get("email").toString());
        final String id=getIntent().getExtras().getString("id");
        Button btnupdateprofile=findViewById(R.id.btnsaveprofile);
        Button btncancel=findViewById(R.id.btncancelprofile);
        btnupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (txtfullname.getText().toString().isEmpty()){
                    txtemail.setError("Enter your fullname");
                }

                else if (txtemail.getText().toString().isEmpty()){
                    txtemail.setError("Enter your email");
                }

                 else if (txtpassword.getText().toString().isEmpty()){
                     txtpassword.setError("Enter your email");
                 }
                 else{
                    BackgroundWorker bw=new BackgroundWorker();
                    Session session=new Session(getApplicationContext());

                    bw.execute(txtfullname.getText().toString(),txtemail.getText().toString(),txtpassword.getText().toString(),id,session.getusename());
                 }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this,UserMenu.class));
                finish();
            }
        });

    }

    public class BackgroundWorker extends AsyncTask<String,Void,Void> {
        ProgressDialog p;
        String result;
        String newname="";
        @Override
        protected Void doInBackground(String... strings) {

            try {
                //1:set the url link
                ServerURL serverURL=new ServerURL();
                String link=serverURL.getUrl()+"/bruneibus/android/updateprofile.php";
                //2:encode the POST data
                String data = URLEncoder.encode("fullname","UTF-8") +"=" + URLEncoder.encode(strings[0]);
                data+="&" +URLEncoder.encode("email","UTF-8") +"=" + URLEncoder.encode(strings[1]);
                data+="&" +URLEncoder.encode("password","UTF-8") +"=" + URLEncoder.encode(strings[2]);
                data+="&" +URLEncoder.encode("id","UTF-8") +"=" + URLEncoder.encode(strings[3]);
                data+="&" +URLEncoder.encode("old","UTF-8") +"=" + URLEncoder.encode(strings[4]);

                newname=strings[0];
                //3:set the url link and open the connection
                URL url = new URL(link);
                URLConnection conn=url.openConnection();
                //4:receive the output
                conn.setDoOutput(true);
                //5:pass the data to the connection
                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                //6:receive server response
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                //7:create string builder to convert data to string

                String line="";

                //create a loop to recieve the output
                while((line=br.readLine())!=null){
                    result+=line;
                    break;
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(EditProfile.this);
            p.setMessage("Registering new user");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(result.contains("success")){
                Toast.makeText(getApplicationContext(),"Profile updated",Toast.LENGTH_SHORT).show();
                Session session=new Session(getApplicationContext());
                session.setusename(newname);
                startActivity(new Intent(EditProfile.this,MainActivity.class));
                finish();


            }
            else {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();


            }


            p.dismiss();
        }
    }
}
