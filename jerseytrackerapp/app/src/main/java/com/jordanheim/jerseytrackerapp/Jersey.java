package com.jordanheim.jerseytrackerapp;

/**
 * Class for Jersey object
 */

public class Jersey
{
    int id;
    String user_username;
    String jersey_name;
    String jersey_type;
    String jersey_qty;

    public Jersey(){super();}

    public Jersey(int i, String username, String jerseyName, String jerseyType, String jerseyQuantity)
    {
        super();
        this.id = i;
        this.user_username = username;
        this.jersey_name = jerseyName;
        this.jersey_type = jerseyType;
        this.jersey_qty = jerseyQuantity;
    }

    // Constructor
    public Jersey(String username, String jerseyName, String jerseyType, String jerseyQuantity)
    {
        this.user_username = username;
        this.jersey_name = jerseyName;
        this.jersey_type = jerseyType;
        this.jersey_qty = jerseyQuantity;
    }

    // Getter methods
    public int getId() {return id;}
    public String getUsername() {return user_username;}
    public String getJerseyName() {return jersey_name;}
    public String getJerseyType() {return jersey_type;}
    public String getJerseyQty(){return jersey_qty;}

    // Setter methods
    public void setId(int id) {this.id = id;}
    public void setUsername(String username) {this.user_username = username;}
    public void setJerseyName(String jerseyName) {this.jersey_name = jerseyName;}
    public void setJerseyType(String jerseyType) {this.jersey_type = jerseyType;}
    public void setJerseyQty(String jerseyQty) {this.jersey_qty = jerseyQty;}



}
