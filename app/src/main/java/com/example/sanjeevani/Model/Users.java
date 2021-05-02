package com.example.sanjeevani.Model;

public class Users {
    private String Name,Mobile,Password,Email;

    public Users()
    {

    }

    public Users(String name, String mobile, String password,String Email) {
        Name = name;
        Mobile = mobile;
        Password = password;
        this.Email=Email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
