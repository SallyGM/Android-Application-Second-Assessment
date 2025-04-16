package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentAddCard extends Fragment {

    TextInputEditText cardNumber;
    TextInputLayout lCardNumber;
    TextInputEditText fullName;
    TextInputLayout lFullName;
    TextInputEditText account;
    TextInputLayout lAccount;
    TextInputEditText sortCode;
    TextInputLayout lSortCode;
    TextInputEditText securityCode;
    TextInputLayout lSecurityCode;
    DatePicker expirationDate;
    TextInputEditText address;
    TextInputLayout lAddress;
    Button clear;
    Button submit;
    Button addAnother;
    ArrayList<Card> cards;
    User user;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_card, container, false);

        cards = new ArrayList<>();

        try{
            user = getArguments().getParcelable("User");
        }
        catch (Exception e){
            user = null;
        }

        context = getContext();

        //region Mapping variables to its components
        cardNumber = view.findViewById(R.id.cardNumber);
        lCardNumber = view.findViewById(R.id.lCardNumber);
        fullName = view.findViewById(R.id.fullName);
        lFullName = view.findViewById(R.id.lFullName);
        account = view.findViewById(R.id.account);
        lAccount = view.findViewById(R.id.lAccount);
        sortCode = view.findViewById(R.id.sortCode);
        lSortCode = view.findViewById(R.id.lSortCode);
        securityCode = view.findViewById(R.id.securityCode);
        lSecurityCode = view.findViewById(R.id.lSecurityCode);
        address = view.findViewById(R.id.address);
        lAddress = view.findViewById(R.id.lAddress);
        expirationDate = view.findViewById(R.id.expDate);
        clear = view.findViewById(R.id.clear);
        submit = view.findViewById(R.id.submit);
        addAnother = view.findViewById(R.id.addAnother);
        //endregion

        expirationDate.findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
        Date currentDate = Calendar.getInstance().getTime();
        long currentDateMillisecond = currentDate.getTime();

        expirationDate.setMinDate(currentDateMillisecond);

        resetDatePicker();

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!checkString(s.toString())){
                    lFullName.setError("Invalid name, it must not contain numbers or special characters");
                }
                else{
                    lFullName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clear.setOnClickListener(v -> {
            clearFields();
        });

        addAnother.setOnClickListener( v -> {
            LocalDate ldExpD = LocalDate.of(expirationDate.getYear(), expirationDate.getMonth()+1, expirationDate.getDayOfMonth());
            Card c = new Card(cardNumber.getText().toString(),fullName.getText().toString(), account.getText().toString(), sortCode.getText().toString(),
                    ldExpD.toString(), securityCode.getText().toString(), address.getText().toString());
            cards.add(c);

            clearFields();
        });

        submit.setOnClickListener(v -> {

            LocalDate ldExpD = LocalDate.of(expirationDate.getYear(), expirationDate.getMonth()+1, expirationDate.getDayOfMonth());

            Card c = new Card(cardNumber.getText().toString(),fullName.getText().toString(), account.getText().toString(), sortCode.getText().toString(),
                    ldExpD.toString(), securityCode.getText().toString(), address.getText().toString());

            cards.add(c);

            if(user != null){
                ((HomeActivity)context).cardManagement("Add_Cards", user, cards, callback -> {
                    ((HomeActivity)context).changeFragment("","Account");
                });
            }
            else{
                // insert database part
                 ((HomeActivity)context).createUser(null, cards , 2);

                // card details
                clearFields();
            }

        });

        return view;
    }

    private void clearFields(){
        fullName.setText("");
        lFullName.setError(null);
        cardNumber.setText("");
        lCardNumber.setError(null);
        account.setText("");
        lAccount.setError(null);
        sortCode.setText("");
        lSortCode.setError(null);
        securityCode.setText("");
        lSecurityCode.setError(null);
        address.setText("");
        lAddress.setError(null);
        resetDatePicker();
    }

    private void resetDatePicker(){
        Calendar c = Calendar.getInstance();
        expirationDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private boolean checkString(String s){

        if(s.length() < 6 || !s.matches("[a-zA-Z\\s]+")){
            return false;
        }

        return true;
    }
}