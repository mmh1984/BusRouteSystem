package com.example.bruneibus.busroutesystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnlogin=findViewById(R.id.btnLogin);
        Button btnsignup=findViewById(R.id.btnSignup);
        btnlogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
               final Dialog d=new Dialog(MainActivity.this);
               d.setContentView(R.layout.dlogin);

                final TextView txtemail=d.findViewById(R.id.txtemail);
                final TextView txtpass=d.findViewById(R.id.txtpassword);
                Button dbtnLogin=d.findViewById(R.id.btnlogin);
                Button dbtnCancel=d.findViewById(R.id.btncancel);

                d.show();

                dbtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                dbtnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(txtemail.getText().toString().isEmpty()){
                            txtemail.setError("Enter your email");

                        }
                        else if(txtpass.getText().toString().isEmpty()){
                            txtpass.setError("Enter your password");

                        }
                        else if(Patterns.EMAIL_ADDRESS.matcher(txtemail.getText()).matches()==false){

                            txtemail.setError("Invalid email");
                        }
                        else{

                           BackgroundWorker bw=new BackgroundWorker();
                           bw.execute(txtemail.getText().toString(),txtpass.getText().toString());
                        }
                    }
                });
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Register.class);

                startActivity(i);
            }
        });

    }
    public class BackgroundWorker extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        String result;
        String email;

        @Override
        protected Void doInBackground(String... strings) {
            //1: get the parameters
            email=strings[0].toString();
            String pass=strings[1].toString();
            ServerURL serverURL=new ServerURL();
            //2: set the url parameter
            String link=serverURL.getUrl() +"/bruneibus/android/login.php?email=" + email +"&password=" + pass;

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
            p=new ProgressDialog(MainActivity.this);
            p.setMessage("Logging in");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.equals("success")){
                Intent i=new Intent(getApplicationContext(),UserMenu.class);

                Session session=new Session(MainActivity.this);
                session.setusename(email);
                startActivity(i);
                finish();
                //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            p.dismiss();
        }
    }
}
