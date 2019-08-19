package com.hd.nature.ghkwallpaper.comman;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;
import com.hd.nature.ghkwallpaper.data_models.fvrt_model;

import static android.provider.BlockedNumberContract.BlockedNumbers.COLUMN_ID;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorite.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "wallpaper";
    public static final String COLUMN_ID = "ID";
    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create  Table
        String wallpeparTable = "CREATE TABLE " + TABLE_NAME + "(id TEXT , img TEXT)";
        sqLiteDatabase.execSQL(wallpeparTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

  /*  public void deleteRecord(WallPepars_Model model) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{model.getId()});
        database.close();
    }*/
}
