package com.example.android_project;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android_project.databinding.ActivityUserBinding;

import java.util.Arrays;

public class User{
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String pass;
    private String country;
    private String city;
    private int phone;
    private String user_type;
    private byte[] profileImage;

    public User(String firstName, String lastName, String gender, String email, String pass, String country, String city, int phone, String user_type, byte[] profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.pass = pass;
        this.country = country;
        this.city = city;
        this.phone = phone;
        this.user_type = user_type;
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", Gender='" + gender + '\'' +
                ", Email='" + email + '\'' +
                ", Password='" + pass + '\'' +
                ", Country='" + country + '\'' +
                ", City='" + city + '\'' +
                ", PhoneNumber='" + phone + '\'' +
                ", User Type='" + user_type + '\'' +
                ", ProfilePicture=" + Arrays.toString(profileImage) +
                '}';
    }
}