package com.example.b10709043finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView r;
    public  EditText et_longitude;
    public  EditText et_latitude;
    public  EditText et_name;
    public  Button btn_add;
    public ContentResolver cr;
    public MyAdapter ad;
    public Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r = findViewById(R.id.list);
        et_latitude = findViewById(R.id.latitude);
        et_longitude = findViewById(R.id.longitude);
        et_name = findViewById(R.id.name);
        btn_add = findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add)
                    btn_add_OnClick();
            }
        });

        cr = getContentResolver();
        uri = Uri.parse("content://com.example.b10709043finalproject/locations");
        Cursor c = cr.query(uri,null,null,null,null);
        if(c == null){
            System.out.println("cursor null");
            return;}
        ad = new MyAdapter(c, getApplicationContext(), cr);

        r.setAdapter(ad);

        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String id = (String)viewHolder.itemView.getTag();
                cr.delete(Uri.parse("content://"+MyContract.AUTHORITY+"/location"),null,new String[]{id});
                System.out.println(id);
                ad.refresh();
            }


        }).attachToRecyclerView(r);
    }
    public void btn_add_OnClick(){
        Uri uri = Uri.parse("content://com.example.b10709043finalproject/locations");
        ContentValues cv = new ContentValues();
        cv.put("longitude",et_longitude.getText().toString());
        cv.put("latitude",et_latitude.getText().toString());
        cv.put("name",et_name.getText().toString());
        Uri returnUri = this.cr.insert(uri, cv);
        System.out.println(returnUri.toString());
        this.ad.refresh();
        Toast.makeText(getApplicationContext(), returnUri.toString(), Toast.LENGTH_SHORT).show();
    }
}
