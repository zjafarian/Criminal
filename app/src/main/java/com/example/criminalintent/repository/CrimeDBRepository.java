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

public class CrimeDBRepository implements IRepository, UserRepository {

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

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return crimes;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Crime crime = extractCrimeFromCursor(cursor);
                crimes.add(crime);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    @Override
    public Crime getCrime(UUID crimeId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crimeId.toString()};

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            Crime crime = extractCrimeFromCursor(cursor);

            return crime;
        } finally {
            cursor.close();
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

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.UserTable.Name,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return users;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                User user = extractUserFromCursor(cursor);
                users.add(user);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return users;
    }

    @Override
    public User getUser(UUID userId) {
        String where = CrimeDBSchema.UserTable.ColsUser.UUIDUser + " = ?";
        String[] whereArgs = new String[]{userId.toString()};

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.UserTable.Name,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            User user = extractUserFromCursor(cursor);

            return user;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void insertUser(User user) {
        ContentValues values = getContentValuesUser(user);
        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, values);

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

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private Crime extractCrimeFromCursor(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.UUID)));
        String title = cursor.getString(cursor.getColumnIndex(Cols.TITLE));
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(Cols.DATE)));
        boolean solved = cursor.getInt(cursor.getColumnIndex(Cols.SOLVED)) == 0 ? false : true;

        return new Crime(uuid, title, date, solved);
    }

    private ContentValues getContentValuesUser(User user) {
        ContentValues values = new ContentValues();
        values.put(CrimeDBSchema.UserTable.ColsUser.UUIDUser, user.getIdUser().toString());
        values.put(CrimeDBSchema.UserTable.ColsUser.USERNAME, user.getName());
        values.put(CrimeDBSchema.UserTable.ColsUser.Password, user.getPassword());
        return values;
    }

    private User extractUserFromCursor(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(CrimeDBSchema.UserTable.ColsUser.UUIDUser)));
        String name = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.UserTable.ColsUser.USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.UserTable.ColsUser.Password));

        return new User(uuid, name, password);
    }


}
