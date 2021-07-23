package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class WriteReviews extends AppCompatActivity {
String user,busno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_reviews);
        Bundle b=getIntent().getExtras();
        busno=b.getString("busno");
        user=b.getString("user");
        //user="maynard@kemuda.com";
        //Toast.makeText(WriteReviews.this,busno,Toast.LENGTH_LONG).show();
        final EditText txtcomment=findViewById(R.id.txtcomment);
        final RatingBar ratingBar=findViewById(R.id.ratingBar);
        Button btnsave=findViewById(R.id.btnsavereview);
        Button btnclose=findViewById(R.id.btnclosereview);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtcomment.getText().length()==0){
                    txtcomment.setError("Please enter your comment");
                }
                else{
                    BackgroundWorker bw=new BackgroundWorker();
                    bw.execute(txtcomment.getText().toString(),String.valueOf(ratingBar.getRating()),user,busno,"save");
                }
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class BackgroundWorker extends AsyncTask<String,Void,Void> {
        ProgressDialog p;
        String result;
        @Override
        protected Void doInBackground(String... strings) {

            try {
                //1:set the url link
                ServerURL serverURL=new ServerURL();
                String link=serverURL.getUrl()+"/bruneibus/android/comments.php";
                //2:encode the POST data
                String data = URLEncoder.encode("comment","UTF-8") +"=" + URLEncoder.encode(strings[0]);
                data+="&" +URLEncoder.encode("ratings","UTF-8") +"=" + URLEncoder.encode(strings[1]);
                data+="&" +URLEncoder.encode("email","UTF-8") +"=" + URLEncoder.encode(strings[2]);
                data+="&" +URLEncoder.encode("busno","UTF-8") +"=" + URLEncoder.encode(strings[3]);
                data+="&" +URLEncoder.encode("operation","UTF-8") +"=" + URLEncoder.encode(strings[4]);

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
             p=new ProgressDialog(WriteReviews.this);
            p.setMessage("Saving your comment");
             p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(result.contains("success")){
                Toast.makeText(WriteReviews.this,"Review Successfully Added",Toast.LENGTH_LONG).show();
                finish();

            }
            else{
                Toast.makeText(WriteReviews.this,"Error saving Reviews " + result.toString(),Toast.LENGTH_LONG).show();

            }


             p.dismiss();


        }

    }
}
