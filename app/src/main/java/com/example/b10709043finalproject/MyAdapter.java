package com.example.b10709043finalproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Cursor c;
    ContentResolver cr;
    Context context;

    public MyAdapter(Cursor c, Context context, ContentResolver cr){
        this.c = c;
        this.cr = cr;
        this.context = context;
    }

    public void refresh(){
        this.c = cr.query(Uri.parse("content://"+MyContract.AUTHORITY+"/locations"),null,null,null,null);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        this.c.moveToPosition(position);
        holder.tv_id.setText(this.c.getString(this.c.getColumnIndex("_id")));
        holder.tv_longitude.setText(this.c.getString(this.c.getColumnIndex("longitude")));
        holder.tv_latitude.setText(this.c.getString(this.c.getColumnIndex("latitude")));
        holder.tv_name.setText(this.c.getString(this.c.getColumnIndex("name")));
        holder.itemView.setTag(holder.tv_id.getText().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cc = cr.query(Uri.parse("content://"+MyContract.AUTHORITY+"/nearby/"+holder.tv_id.getText().toString()),
                        null,null,null,null);
                if (cc.getCount() == 0){
                    System.out.println("cc count 0");
                    Toast.makeText(context, "NA", Toast.LENGTH_SHORT).show();

                    return;
                }
                Toast.makeText(context,cc.getString(cc.getColumnIndex("_id")),Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                double longitude = Double.parseDouble(holder.tv_longitude.getText().toString());
                double latitude = Double.parseDouble(holder.tv_latitude.getText().toString());
                System.out.println(longitude);
                System.out.println(latitude);
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("geo:0.0?q="+latitude+","+longitude);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mapIntent);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return c.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_id;
        TextView tv_longitude;
        TextView tv_latitude;
        TextView tv_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.id);
            tv_longitude = itemView.findViewById(R.id.tv_longitude);
            tv_latitude = itemView.findViewById(R.id.tv_latitude);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }
}
