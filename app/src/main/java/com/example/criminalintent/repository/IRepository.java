package com.example.criminalintent.repository;

import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface IRepository extends Serializable {
    List<Crime> getCrimes();
    Crime getCrime(UUID crimeId);
    void insertCrime(Crime crime);
    void updateCrime(Crime crime);
    void deleteCrime(Crime crime);
    int getPosition(UUID crimeId);
    List<User> getUsers();
    User getUser(UUID userId);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    int getPositionUser(UUID userId);
}
