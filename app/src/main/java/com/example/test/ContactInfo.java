package com.example.test;

//Will hold all the info for each Contact
public class ContactInfo {
    private String name, phone, email;

    public ContactInfo(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
