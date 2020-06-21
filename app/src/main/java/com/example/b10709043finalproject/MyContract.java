package com.example.b10709043finalproject;

import android.provider.BaseColumns;

public final class MyContract {
    public static final String AUTHORITY="com.example.b10709043finalproject";
    public static final String PATH_LOCATION="locations";
    public static final String PATH_LOCATION_WITH_ID="locations/#";
    public static final String PATH_NEARBY="nearby";
    public static final String PATH_NEARBY_WITH_ID="nearby/#";
    public MyContract(){}
    public static abstract class Location implements BaseColumns {
        public static final String TABLE_NAME="location";
        public static final String COLUMN_NAME_LONGITUDE="longitude";
        public static final String COLUMN_NAME_LATITUDE="latitude";
        public static final String COLUMN_NAME_NAME="name";
    }
}
