package com.example.criminalintent.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.criminalintent.model.Crime;

import java.util.List;
import java.util.UUID;

@Dao
public interface CrimeDAO {
    @Insert
    void insertCrime(Crime crime);

    @Update
    void updateCrime(Crime crime);

    @Delete
    void deleteCrime(Crime crime);

    @Query("SELECT * FROM crimeTable WHERE uuid=:crimeId")
    Crime getCrime(UUID crimeId);

    @Query("SELECT * FROM crimeTable")
    List<Crime> getCrimes();


}
