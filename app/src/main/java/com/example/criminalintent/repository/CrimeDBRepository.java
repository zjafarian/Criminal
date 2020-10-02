package com.example.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeDBHelper;
import com.example.criminalintent.database.CrimeDBSchema;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;

import static com.example.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository {

    private static CrimeDBRepository sInstance;

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        mContext = context.getApplicationContext();
        CrimeDBHelper crimeDBHelper = new CrimeDBHelper(mContext);

        //all 4 checks happens on getDataBase
        mDatabase = crimeDBHelper.getWritableDatabase();
    }

    @Override
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper crimeCursorWrapper = queryCrimeCursor(null, null);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return crimes;

        try {
            crimeCursorWrapper.moveToFirst();

            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;
    }

    @Override
    public Crime getCrime(UUID crimeId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crimeId.toString()};

        CrimeCursorWrapper crimeCursorWrapper = queryCrimeCursor(where, whereArgs);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return null;

        try {
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        } finally {
            crimeCursorWrapper.close();
        }
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
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId))
                return i;
        }
        return -1;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryUserCursor(null, null);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return users;

        try {
            crimeCursorWrapper.moveToFirst();

            while (!crimeCursorWrapper.isAfterLast()) {
                users.add(crimeCursorWrapper.getUser());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return users;
    }

    @Override
    public User getUser(UUID userId) {
        String where = CrimeDBSchema.UserTable.ColsUser.UUIDUser + " = ?";
        String[] whereArgs = new String[]{userId.toString()};


        CrimeCursorWrapper crimeCursorWrapper = queryUserCursor(where, whereArgs);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return null;

        try {
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getUser();
        } finally {
            crimeCursorWrapper.close();
        }
    }

    @Override
    public void insertUser(User user) {
        ContentValues values = getContentValuesUser(user);
        mDatabase.insert(CrimeDBSchema.UserTable.Name, null, values);

    }

    @Override
    public void updateUser(User user) {
        ContentValues values = getContentValuesUser(user);
        String whereClause = CrimeDBSchema.UserTable.ColsUser.UUIDUser + " = ?";
        String[] whereArgs = new String[]{user.getIdUser().toString()};
        mDatabase.update(CrimeDBSchema.UserTable.Name, values, whereClause, whereArgs);

    }

    @Override
    public void deleteUser(User user) {
        String whereClause = CrimeDBSchema.UserTable.ColsUser.UUIDUser + " = ?";
        String[] whereArgs = new String[]{user.getIdUser().toString()};
        mDatabase.delete(CrimeDBSchema.UserTable.Name, whereClause, whereArgs);

    }

    @Override
    public int getPositionUser(UUID userId) {
        List<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIdUser().equals(userId))
                return i;
        }
        return -1;
    }



    private CrimeCursorWrapper queryCrimeCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        CrimeCursorWrapper crimeCursorWrapper = new CrimeCursorWrapper(cursor);
        return crimeCursorWrapper;
    }

    private CrimeCursorWrapper queryUserCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDBSchema.UserTable.Name,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        CrimeCursorWrapper crimeCursorWrapper = new CrimeCursorWrapper(cursor);
        return crimeCursorWrapper;
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private ContentValues getContentValuesUser(User user) {
        ContentValues values = new ContentValues();
        values.put(CrimeDBSchema.UserTable.ColsUser.UUIDUser, user.getIdUser().toString());
        values.put(CrimeDBSchema.UserTable.ColsUser.USERNAME, user.getName());
        values.put(CrimeDBSchema.UserTable.ColsUser.Password, user.getPassword());
        return values;
    }


}
