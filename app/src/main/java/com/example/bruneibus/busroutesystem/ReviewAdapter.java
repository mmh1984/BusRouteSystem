package com.example.bruneibus.busroutesystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Mikail on 23/3/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {
    Context context;
    String user;
    ArrayList<ReviewClass> reviewClasses;


    public ReviewAdapter(Context context,ArrayList<ReviewClass> reviewClasses,String user){
        this.context=context;
        this.reviewClasses=reviewClasses;
        this.user=user;
    }
    @Override
    public ReviewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.CustomViewHolder holder, final int position) {

final ReviewClass reviewClass=reviewClasses.get(position);
int rating=Integer.parseInt(reviewClass.getRatings());
        holder.commentdate.setText(reviewClass.getCommentdate());
        holder.comments.setText(reviewClass.getComments());
        holder.reviewedby.setText(reviewClass.getEmail());
        holder.ratingBar.setRating(rating);
if(user.equals(reviewClass.getEmail())) {
    holder.btndelete.setVisibility(View.VISIBLE);
    holder.btndelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

           BackgroundWorker bw=new BackgroundWorker();
           bw.execute(reviewClass.getId().toString());
           // Toast.makeText(context,reviewClass.getId().toString(),Toast.LENGTH_LONG).show();

            reviewClasses.remove(position);
            notifyDataSetChanged();
        }
    });
    }
else{
    holder.btndelete.setVisibility(View.GONE);
}


    }

    @Override
    public int getItemCount() {
        return reviewClasses.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView commentdate,reviewedby,comments;
        RatingBar ratingBar;
        Button btndelete;


        public CustomViewHolder(View view) {
            super(view);
            commentdate=view.findViewById(R.id.tvcommentdate);
            reviewedby=view.findViewById(R.id.tvviewedby);
            comments=view.findViewById(R.id.tvcomments);
            ratingBar=view.findViewById(R.id.ratingBarreview);
            btndelete=view.findViewById(R.id.btndeletecomment);


        }
    }

    public class BackgroundWorker extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        String result;
        @Override
        protected Void doInBackground(String... strings) {

            try {
                //1:set the url link
                ServerURL serverURL=new ServerURL();
                String link=serverURL.getUrl()+"/bruneibus/android/deletecomment.php";
                //2:encode the POST data
                String data = URLEncoder.encode("id","UTF-8") +"=" + URLEncoder.encode(strings[0]);


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
            p=new ProgressDialog(context);
            p.setMessage("Deleting comment");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.contains("success")){
                Toast.makeText(context,"Comment deleted",Toast.LENGTH_SHORT).show();

            }


            p.dismiss();
        }
    }
}
