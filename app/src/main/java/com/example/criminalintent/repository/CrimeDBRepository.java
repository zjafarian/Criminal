package com.example.criminalintent.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import com.example.criminalintent.database.CrimeDAO;
import com.example.criminalintent.database.CrimeDatabase;
import com.example.criminalintent.database.UserDAO;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;

import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository {

    private static CrimeDBRepository sInstance;

    private SQLiteDatabase mDatabase;
    private CrimeDAO mCrimeDAO;
    private UserDAO mUserDAO;
    private Context mContext;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        mContext = context.getApplicationContext();
        //CrimeDBHelper crimeDBHelper = new CrimeDBHelper(mContext);

        //all 4 checks happens on getDataBase
        //mDatabase = crimeDBHelper.getWritableDatabase();

        CrimeDatabase crimeDatabase = Room.databaseBuilder(mContext,
                CrimeDatabase.class,
                "crime.db").allowMainThreadQueries()
                .build();

        mCrimeDAO = crimeDatabase.getCrimeTable();
        mUserDAO = crimeDatabase.getUserTable();
    }

    @Override
    public List<Crime> getCrimes() {
        return mCrimeDAO.getCrimes();
    }

    @Override
    public Crime getCrime(UUID crimeId) {
       return mCrimeDAO.getCrime(crimeId);
    }

    @Override
    public void insertCrime(Crime crime) {
       mCrimeDAO.insertCrime(crime);
    }

    @Override
    public void updateCrime(Crime crime) {
       mCrimeDAO.updateCrime(crime);
    }

    @Override
    public void deleteCrime(Crime crime) {
        mCrimeDAO.deleteCrime(crime);
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
        return mUserDAO.getUsers();
    }

    @Override
    public User getUser(UUID userId) {
       return mUserDAO.getUser(userId);
    }

    @Override
    public void insertUser(User user) {
       mUserDAO.insertUser(user);

    }

    @Override
    public void updateUser(User user) {
       mUserDAO.updateUser(user);

    }

    @Override
    public void deleteUser(User user) {
       mUserDAO.deleteUser(user);

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

}
