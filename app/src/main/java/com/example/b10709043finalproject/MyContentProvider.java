package com.example.b10709043finalproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    private MyDBHelper myDBHelper;
    private UriMatcher uriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        System.out.println("in provider onCreate");
        myDBHelper = new MyDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = uriMatcher.match(uri);
        System.out.println("my output : query");
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor ;
        switch (match){
            case LOCATION:
                cursor = db.rawQuery("select * from location",null);
                return cursor;
            case NEARBY_WITH_ID:
                String id = uri.getLastPathSegment();
                System.out.println("nearby");
                System.out.println("my output : id :"+id);
                cursor = db.rawQuery("select * from location where _id = ?",new String[]{id});
                if(cursor.getCount() == 0) {
                    return cursor;
                }
                cursor.moveToNext();
                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                Location l = new Location("");
                l.setLongitude(longitude);
                l.setLatitude(latitude);
                System.out.println("my longitude:"+longitude);
                System.out.println("my latitude:"+latitude);


                cursor = db.rawQuery("select * from location where _id != ?", new String[]{id});
                if(cursor.getCount() == 0){
                    return cursor;
                }

                Location destination = new Location("");
                double min = Double.POSITIVE_INFINITY;
                int position = -1;
                while(cursor.moveToNext()){
                    destination.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                    destination.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                    double distance = l.distanceTo(destination);
                    if(distance < min){
                        min = distance;
                        position = cursor.getPosition();
                    }
                }
                cursor.moveToPosition(position);
                System.out.println("near by : " + cursor.getDouble(1));
                System.out.println("near by : " + cursor.getDouble(2));
                return cursor;
            default:
                System.out.println("no match");
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        System.out.println("insert");
        return Uri.withAppendedPath(uri, db.insert("location",null,values)+"");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        System.out.println("delete");
        return db.delete("location", "_id = ?",selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static final int LOCATION = 100;
    private static final int LOCATION_WITH_ID = 101;
    private static final int NEARBY_WITH_ID = 201;
    private UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyContract.AUTHORITY,MyContract.PATH_LOCATION, LOCATION);
        uriMatcher.addURI(MyContract.AUTHORITY,MyContract.PATH_LOCATION_WITH_ID, LOCATION_WITH_ID);
        uriMatcher.addURI(MyContract.AUTHORITY,MyContract.PATH_NEARBY_WITH_ID, NEARBY_WITH_ID);
        return uriMatcher;
    }
}
