package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import androidx.annotation.Nullable;

public class CrimeDBHelper extends SQLiteOpenHelper {

    public CrimeDBHelper(@Nullable Context context) {
        super(context, CrimeDBSchema.NAME, null, CrimeDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("CREATE TABLE " + CrimeDBSchema.CrimeTable.NAME + " (");
        sbQuery.append(Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery.append(Cols.UUID + " TEXT NOT NULL,");
        sbQuery.append(Cols.TITLE + " TEXT,");
        sbQuery.append(Cols.DATE + " TEXT,");
        sbQuery.append(Cols.SOLVED + " INTEGER");
        sbQuery.append(");");
        StringBuilder dbQuery = new StringBuilder();
        dbQuery.append("CREATE TABLE " + CrimeDBSchema.CrimeTable.NAME + " (");
        dbQuery.append(Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        dbQuery.append(Cols.UUID + " TEXT NOT NULL,");
        dbQuery.append(Cols.TITLE + " TEXT,");
        dbQuery.append(Cols.DATE + " TEXT,");
        dbQuery.append(Cols.SOLVED + " INTEGER");
        dbQuery.append(");");
        db.execSQL(sbQuery.toString());
        db.execSQL(dbQuery.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
