package com.example.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeDBHelper;
import com.example.criminalintent.database.CrimeDBSchema;
import com.example.criminalintent.model.Crime;

import static com.example.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository {

    private static CrimeDBRepository sInstance;

    private SQLiteDatabase mDatabase;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    public CrimeDBRepository(Context context) {
        CrimeDBHelper crimeDBHelper = new CrimeDBHelper(context);

        //all 4 checks happens on getDataBase
        mDatabase = crimeDBHelper.getWritableDatabase();
    }

    @Override
    public List<Crime> getCrimes() {
        return null;
    }

    @Override
    public Crime getCrime(UUID crimeId) {
        return null;
    }

    @Override
    public void insertCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, values);
    }

    @Override
    public void updateCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.update(CrimeDBSchema.CrimeTable.NAME, values, whereClause, whereArgs);
    }

    @Override
    public void deleteCrime(Crime crime) {
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.delete(CrimeDBSchema.CrimeTable.NAME, whereClause, whereArgs);
    }

    @Override
    public int getPosition(UUID crimeId) {
        return 0;
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }
}
