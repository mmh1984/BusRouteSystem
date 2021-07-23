package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btncancel=findViewById(R.id.btncancel);
        Button btnsignup=findViewById(R.id.btnsignup);




        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtfname=findViewById(R.id.txtfullname);
                TextView txtemail=findViewById(R.id.txtemail);
                TextView txtpass=findViewById(R.id.txtpassword);
                if(txtfname.getText().toString().isEmpty()){
                    txtfname.setError("Enter your fullname");

                }
                else if(txtemail.getText().toString().isEmpty()){
                    txtemail.setError("Enter your email");

                }
                else if(txtpass.getText().toString().isEmpty()){
                    txtpass.setError("Enter your password");

                }
                else if(Patterns.EMAIL_ADDRESS.matcher(txtemail.getText()).matches()==false){
                    txtemail.setError("Enter a valid email");

                }
                else{
                    String pass=txtpass.getText().toString();
                    String name=txtfname.getText().toString();
                    String email=txtemail.getText().toString();

                    BackgroundWorker bw=new BackgroundWorker();
                    bw.execute(pass,name,email);
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public class BackgroundWorker extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        String result;
        @Override
        protected Void doInBackground(String... strings) {

            try {
                //1:set the url link
                ServerURL serverURL=new ServerURL();
                String link=serverURL.getUrl()+"/bruneibus/android/register.php";
                //2:encode the POST data
                String data = URLEncoder.encode("userpass","UTF-8") +"=" + URLEncoder.encode(strings[0]);
                data+="&" +URLEncoder.encode("fullname","UTF-8") +"=" + URLEncoder.encode(strings[1]);
                data+="&" +URLEncoder.encode("email","UTF-8") +"=" + URLEncoder.encode(strings[2]);

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
            p=new ProgressDialog(Register.this);
            p.setMessage("Registering new user");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(result.contains("emailexists")){
                Toast.makeText(getApplicationContext(),"Error, this email is already taken",Toast.LENGTH_SHORT).show();
                TextView txtemail=findViewById(R.id.txtemail);
                txtemail.setError("Error!, this email is already taken");
            }
            else if(result.contains("success")){
                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                finish();

            }
            else{
                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();

            }

            p.dismiss();
        }
    }
}
