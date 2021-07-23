package com.example.bruneibus.busroutesystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class BusAdapter extends RecyclerView.Adapter<BusAdapter.CustomViewHolder> {

    Context context;
    ArrayList<BusClass> busClasses;
    String[] busimg;
    int[] busimgsrc;

    public BusAdapter(Context context, ArrayList<BusClass> busClasses) {
        this.context = context;
        this.busClasses = busClasses;
        initialize_array();
    }

    public void initialize_array(){
        busimg=new String[27];
        busimg[0]="01A";
        busimg[1]="01C";
        busimg[2]="20";
        busimg[3]="21";
        busimg[4]="22";
        busimg[5]="23";
        busimg[6]="24";
        busimg[7]="33";
        busimg[8]="34";
        busimg[9]="35";
        busimg[10]="36";
        busimg[11]="37";
        busimg[12]="38";
        busimg[13]="39";
        busimg[14]="42";
        busimg[15]="43";
        busimg[16]="44";
        busimg[17]="45";
        busimg[18]="46";
        busimg[19]="47";
        busimg[20]="48";
        busimg[21]="49";
        busimg[22]="55";
        busimg[23]="56";
        busimg[24]="57";
        busimg[25]="58";
        busimg[26]="59";

        busimgsrc=new int[27];
        busimgsrc[0]=R.drawable.b01a;
        busimgsrc[1]=R.drawable.b01c;
        busimgsrc[2]=R.drawable.b20;
        busimgsrc[3]=R.drawable.b21;
        busimgsrc[4]=R.drawable.b22;
        busimgsrc[5]=R.drawable.b23;
        busimgsrc[6]=R.drawable.b24;
        busimgsrc[7]=R.drawable.b33;
        busimgsrc[8]=R.drawable.b34;
        busimgsrc[9]=R.drawable.b35;
        busimgsrc[10]=R.drawable.b36;
        busimgsrc[11]=R.drawable.b37;
        busimgsrc[12]=R.drawable.b38;
        busimgsrc[13]=R.drawable.b39;
        busimgsrc[14]=R.drawable.b42;
        busimgsrc[15]=R.drawable.b43;
        busimgsrc[16]=R.drawable.b44;
        busimgsrc[17]=R.drawable.b45;
        busimgsrc[18]=R.drawable.b46;
        busimgsrc[19]=R.drawable.b47;
        busimgsrc[20]=R.drawable.b48;
        busimgsrc[21]=R.drawable.b49;
        busimgsrc[22]=R.drawable.b55;
        busimgsrc[23]=R.drawable.b56;
        busimgsrc[24]=R.drawable.b57;
        busimgsrc[25]=R.drawable.b58;
        busimgsrc[26]=R.drawable.b59;




    }
    @Override
    public BusAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mainlayout,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusAdapter.CustomViewHolder holder, int position) {
        BusClass busClass=busClasses.get(position);
        int img=0;
        int index=0;
      String busno=busClass.getBusno();
        for(int x =0;x<27;x++){
            if (busimg[x].equals(busno)){
                img=busimgsrc[x];
                holder.imgbus.setImageResource(img);
                holder.routes.setText("Routes: " + busClass.getRoutes());

            }
        }
    }

    @Override
    public int getItemCount() {
        return busClasses.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView imgbus;
        TextView routes;
        public CustomViewHolder(View view) {
            super(view);
            imgbus=view.findViewById(R.id.imgbus);
            routes=view.findViewById(R.id.tvroutes);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    BusClass busitem=busClasses.get(pos);
                   Intent i=new Intent(context,busroutes.class);
                   i.putExtra("busno",busitem.getBusno());
                   context.startActivity(i);
                }
            });
        }
    }
}
