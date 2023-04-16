package com.jordanheim.jerseytrackerapp;

/**
 * Class for User object
 */

public class User
{

    int id;
    String user_username;
    String user_password;

    public User() {super();}

    public User(int i, String username, String password)
    {
        super();
        this.id = i;
        this.user_username = username;
        this.user_password = password;
    }

    // Constructor
    public User(String username, String password)
    {
        this.user_username = username;
        this.user_password = password;
    }

    // Getter methods
    public int getId() {return id;}
    public String getUsername() {return user_username;}
    public String getPassword() {return user_password;}

    // Setter methods
    public void setId(int id) {this.id = id;}
    public void setUsername(String username) {this.user_username = username;}
    public void setPassword(String password) {this.user_password = password;}
}
