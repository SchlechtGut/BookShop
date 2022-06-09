package com.example.MyBookShopApp.api.request;

public class ProfileRequest {
    private String name;
    private String mail;
    private String phone;
    private String password;
    private String passwordReply;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordReply() {
        return passwordReply;
    }

    public void setPasswordReply(String passwordReply) {
        this.passwordReply = passwordReply;
    }

    @Override
    public String toString() {
        return "ProfileRequest{" +
                "name='" + name + '\'' +
                ", email='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", passwordReply='" + passwordReply + '\'' +
                '}';
    }
}
