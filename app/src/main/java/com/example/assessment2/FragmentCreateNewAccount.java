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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class FragmentCreateNewAccount extends Fragment {

    TextInputEditText name;
    TextInputLayout lName;
    TextInputEditText middleName;
    TextInputLayout lMiddleName;
    TextInputEditText lastName;
    TextInputLayout lLastName;
    TextInputEditText email;
    TextInputLayout lEmail;
    TextInputEditText cEmail;
    TextInputLayout lCEmail;
    TextInputEditText phone;
    TextInputLayout lPhone;
    DatePicker dob;
    TextInputEditText password;
    TextInputLayout lPassword;
    TextInputEditText cPassword;
    TextInputLayout lCPassword;
    RadioGroup radioGroup;
    TextInputEditText tiet_other;
    TextInputLayout lOther;
    RadioButton male;
    RadioButton female;
    RadioButton other;
    Button next;
    Button clear;
    Context context;
    String gender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);

        //region mapping variables to components in the layout
        name = view.findViewById(R.id.name);
        lName = view.findViewById(R.id.lName);
        middleName =view.findViewById(R.id.middleName);
        lMiddleName = view.findViewById(R.id.lMiddleName);
        lastName = view.findViewById(R.id.lastName);
        lLastName = view.findViewById(R.id.lLastName);
        email = view.findViewById(R.id.email);
        lEmail = view.findViewById(R.id.lEmail);
        cEmail =view.findViewById(R.id.cEmail);
        lCEmail = view.findViewById(R.id.lCEmail);
        phone = view.findViewById(R.id.phone);
        lPhone = view.findViewById(R.id.lPhone);
        dob = view.findViewById(R.id.dateOfBirth);
        password = view.findViewById(R.id.password);
        lPassword = view.findViewById(R.id.lPassword);
        cPassword = view.findViewById(R.id.cPassword);
        lCPassword = view.findViewById(R.id.lCPassword);
        next = view.findViewById(R.id.next);
        clear = view.findViewById(R.id.clear);
        radioGroup = view.findViewById(R.id.rgGender);
        tiet_other = view.findViewById(R.id.tiet_other);
        lOther = view.findViewById(R.id.lOther);
        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);
        other = view.findViewById(R.id.other);
        //endregion

        tiet_other.setEnabled(false);
        context = getContext();

        // setting max date of birth to today - for convenience
        Date currentDate = Calendar.getInstance().getTime();
        long currentDateMillisecond = currentDate.getTime();

        dob.setMaxDate(currentDateMillisecond);

        // making today's date as the default
        resetDatePicker();

        // does not let the user progress to the next page
        // until the form is complete
        next.setEnabled(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!checkString(s.toString(),2)){
                    lName.setError("Invalid name, must not contain numbers or special characters");
                }
                else{
                    lName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!checkString(s.toString(),2)){
                    lLastName.setError("Invalid last name, must not contain numbers or special characters");
                }
                else{
                    lLastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        middleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // middle name can be empty
                if(s.length() != 0 && !checkString(s.toString(), 0)){
                    lMiddleName.setError("Invalid middle name, must not contain numbers or special characters");
                }
                else{
                    lMiddleName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!emailValidation(s.toString())){
                    lEmail.setError("Please insert a valid email, for example: example@example.com");
                }
                else{
                    lEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        cEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(email.getText().toString())){
                    lCEmail.setError("Email does not match");
                }
                else{
                    lCEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 11){
                    lPhone.setError("Phone number should have 11 characters");
                }
                else{
                    lPhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8 || s.length() > 16){
                    lPassword.setError("Please limit the password to minimum of 8 character and maximum 16 characters");
                }
                else if (!s.toString().matches("^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$%^&*-]).{8,16}$")){
                    lPassword.setError("Password must contain uppercaser, lowercase, numbers and special characters!");
                }
                else{
                    lPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(password.getText().toString())){
                    lCPassword.setError("Passwords do not match");
                }
                else{
                    lCPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyForm();
            }
        });

        next.setOnClickListener(v -> {
            LocalDate ldDOB = LocalDate.of(dob.getYear(), dob.getMonth()+1, dob.getDayOfMonth());

            User u = new User(name.getText().toString(), middleName.getText().toString(), lastName.getText().toString(),
                    email.getText().toString().toLowerCase(), phone.getText().toString(), ldDOB.toString(), password.getText().toString(),null, gender);

            ((HomeActivity)context).createUser(u, null, 1);
            ((HomeActivity)context).changeFragment("", "Card_Details");
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            gender = selected.getText().toString();
            if(checkedId == other.getId()){
                tiet_other.setEnabled(true);
            }
            else{
                tiet_other.setEnabled(false);
            }
        });

        tiet_other.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2){
                    gender = s.toString();
                    lOther.setError(null);
                }
                else{
                    lOther.setError("Gender is too short");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        clear.setOnClickListener(v -> {
            name.setText("");
            lName.setError(null);
            lastName.setText("");
            lLastName.setError(null);
            email.setText("");
            lEmail.setError(null);
            cEmail.setText("");
            lCEmail.setError(null);
            password.setText("");
            lPassword.setError(null);
            cPassword.setText("");
            lCPassword.setError(null);
            resetDatePicker();
            next.setEnabled(false);
            radioGroup.clearCheck();
        });

        // Inflate the layout for this fragment
        return view;
    }

    // check if the form is valid
    private boolean verifyForm(){

        LocalDate ldDOB = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDayOfMonth());
        LocalDate ldCurr = LocalDate.now();

        Period difference = Period.between(ldDOB, ldCurr);

        if(lName.getError() != null || lMiddleName.getError() != null || lLastName.getError() != null || lLastName.getError() != null ||
                lEmail.getError() != null || lCEmail.getError() != null || lPassword.getError() != null || lCPassword.getError() != null ||
                lPhone.getError() != null || radioGroup.getCheckedRadioButtonId() == -1 || difference.getYears() < 18 ||
                (radioGroup.getCheckedRadioButtonId() == R.id.other && lOther.getError() != null)){
            next.setEnabled(false);
            return false;
        }

        next.setEnabled(true);
        return true;
    }

    private boolean checkString(String s, int min){

        if(s.length() <= min || !s.matches("[a-zA-Z]+")){
            return false;
        }

        return true;
    }

    private boolean emailValidation(String e){
        if (!e.toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
           return false;
        }

        return true;
    }

    private void resetDatePicker(){
        Calendar c = Calendar.getInstance();
        dob.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }
}