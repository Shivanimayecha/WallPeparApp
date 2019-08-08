package com.hd.nature.ghkwallpaper.comman;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorite.db";
    private static final int DB_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create  Table
        String wallpeparTable = "CREATE TABLE wallpaper (id TEXT , img TEXT )";
        sqLiteDatabase.execSQL(wallpeparTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
