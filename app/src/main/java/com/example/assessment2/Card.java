package com.example.assessment2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Card implements Parcelable {
    private String cardNumber;
    private String fullName;
    private String accountNumber;
    private String sortCode;
    private String expDate;
    private String securityCode;
    private String billAddress;


    public Card(HashMap<String, Object> card) {
        this.cardNumber = card.get("cardNumber").toString();
        this.fullName = card.get("fullName").toString();
        this.accountNumber = card.get("accountNumber").toString();
        this.sortCode = card.get("sortCode").toString();
        this.expDate = card.get("expDate").toString();
        this.securityCode = card.get("securityCode").toString();
        this.billAddress = card.get("billAddress").toString();
    }

    public Card(String cardNumber, String fullName, String accountNumber, String sortCode, String expDate, String securityCode, String billAddress) {
        this.cardNumber = cardNumber;
        this.fullName = fullName;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.expDate = expDate;
        this.securityCode = securityCode;
        this.billAddress = billAddress;
    }

    //region getters and setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(cardNumber);
        parcel.writeString(fullName);
        parcel.writeString(accountNumber);
        parcel.writeString(sortCode);
        parcel.writeString(expDate);
        parcel.writeString(securityCode);
    }

    public Card(Parcel in) {
        this.cardNumber = in.readString();
        this.fullName = in.readString();
        this.accountNumber = in.readString();
        this.sortCode = in.readString();
        this.expDate = in.readString();
        this.securityCode = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
