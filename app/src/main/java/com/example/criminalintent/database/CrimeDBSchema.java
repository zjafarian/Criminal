package com.example.criminalintent.database;

public class CrimeDBSchema {
    public static final String NAME = "crime.db";
    public static final Integer VERSION = 1;

    public static final class CrimeTable {
        public static final String NAME = "crimeTable";

        public static final class Cols {
            //this column in only for database
            public static final String ID = "id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }

    public static final class UserTable {
        public static final String Name="userTable";

        public  static final class ColsUser {
            public static final String ID="idUser";
            public static final String UUIDUser="uuidUser";
            public static final String USERNAME ="username";
            public static final String Password="password";
        }
    }
}
