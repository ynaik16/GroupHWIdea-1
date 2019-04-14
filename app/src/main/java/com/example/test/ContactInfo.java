package com.example.test;

//Will hold all the info for each Contact
public class ContactInfo {
    private String name, phone, email, address;

    //basic constructor
    public ContactInfo(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    /* getters */

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress(){ return address; }
}
