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
    private Boolean confirmed;

    @SerializedName("company") private String agency;
    @SerializedName("userType") private String role;

    protected User() {}

    // login
    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    // manager signup
    public User(String firstName, String lastName, String email, String pwd, String agency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.agency = agency;
    }

    // standard user signup
    public User(String firstName, String lastName, String email, String pwd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
    }

    // session constructor
    public User(String firstName, String lastName, String email, String pwd, String agency, String role, boolean confirmed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
        this.agency = agency;
        this.role = role;
        this.confirmed = confirmed;
    }
}
