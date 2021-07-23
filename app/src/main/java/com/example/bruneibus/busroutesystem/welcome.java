package com.example.bruneibus.busroutesystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView logo=findViewById(R.id.ivLogo);
        TextView tvtitle=findViewById(R.id.tvtitle);
        TextView download=findViewById(R.id.tvDownload);
        TextView start=findViewById(R.id.tvStart);
        download.setMovementMethod(LinkMovementMethod.getInstance());
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.blink);
        Animation anim2= AnimationUtils.loadAnimation(this,R.anim.slideup);
        final Animation anim3= AnimationUtils.loadAnimation(this,R.anim.blink2);
        final TextView tvconnecting=findViewById(R.id.tvconnecting);
        tvconnecting.setVisibility(View.GONE);
        logo.startAnimation(anim);
        tvtitle.startAnimation(anim2);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerURL serverURL=new ServerURL();

                String link=serverURL.getUrl()+"/assets/files/New Bus Route.pdf";
                Intent browser=new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browser);
            }
        });
        final Handler h=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        };
start.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        tvconnecting.setVisibility(View.VISIBLE);
        tvconnecting.startAnimation(anim3);
        h.postDelayed(runnable,3000);
    }
});




    }
}
