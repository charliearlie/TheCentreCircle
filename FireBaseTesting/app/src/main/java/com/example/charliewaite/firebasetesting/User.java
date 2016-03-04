package com.example.charliewaite.firebasetesting;

/**
 * Created by charliewaite on 08/02/2016.
 */
public class User {

    private int birthYear;
    private String fullName;
    public User() {}

    public User(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getFullName() {
        return fullName;
    }
}
