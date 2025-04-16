package com.example.assessment2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String id;
    private String name;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String dateOfBirth;
    private String gender;
    private ArrayList<Card> card;

    public User() {

    }

    // to save on the db - 6
    public User(String name, String middleName, String lastName, String phone,  String dateOfBirth, ArrayList<Card> card, String gender) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.card = card;
        this.gender = gender;
    }

    public User(String name, String middleName, String lastName, String email, String phone, String dateOfBirth, String password, ArrayList<Card> card, String gender) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.card = card;
        this.gender = gender;
    }

    // for other purposes - 7
    public User(String id, String name, String middleName, String lastName, String phone, String dateOfBirth, ArrayList<Card> card, String gender) {
        this.id = id;
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.card = card;
        this.gender = gender;
    }

    // Complete obj - 9
    public User(String id, String name, String middleName, String lastName, String email, String phone, String password, String dateOfBirth, ArrayList<Card> card, String gender) {
        this.id = id;
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.card = card;
        this.gender = gender;
    }

    //region getters and setters
    public ArrayList<Card> getCard() {
        return card;
    }

    public void setCard(ArrayList<Card> card) {
        this.card = card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(middleName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(dateOfBirth);
        parcel.writeValue(card);
    }

    public User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.middleName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.password = in.readString();
        this.dateOfBirth = in.readString();
        this.card = (ArrayList<Card>) in.readValue(Card.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


