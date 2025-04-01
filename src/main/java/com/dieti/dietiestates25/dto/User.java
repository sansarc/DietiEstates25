package com.dieti.dietiestates25.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String pwd;

    @SerializedName("company")
    private String agency;

    protected User() {}

    // login
    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    // signup
    public User(String firstName, String lastName, String email, String pwd, String agency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
        this.agency = agency;
    }

    // session constructor
    public User(String firstName, String lastName, String email, String pwd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
    }
}
