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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.Period;

public class FragmentAccount extends Fragment {

    TextInputEditText name;
    TextInputLayout lName;
    TextInputEditText middleName;
    TextInputLayout lMiddleName;
    TextInputEditText lastName;
    TextInputLayout lLastName;
    TextInputEditText email;
    TextInputLayout lEmail;
    TextInputEditText phone;
    TextInputLayout lPhone;
    DatePicker dob;
    Button save;
    Button changePassword;
    Button changeCard;
    Button signOut;
    Context context;
    RadioGroup radioGroup;
    TextInputEditText tiet_other;
    TextInputLayout lOther;
    RadioButton male;
    RadioButton female;
    RadioButton other;
    private String userID;
    private String userEmail;
    private DBHelper helper;
    String gender;

    User u;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        context = getContext();

        userID = getArguments().getString("UserID");
        userEmail = getArguments().getString("UserEmail");
        helper = new DBHelper(context);

        save = view.findViewById(R.id.save);
        changePassword = view.findViewById(R.id.changePassword);
        changeCard = view.findViewById(R.id.manageCards);
        name = view.findViewById(R.id.name);
        lName = view.findViewById(R.id.lName);
        middleName =view.findViewById(R.id.middleName);
        lMiddleName = view.findViewById(R.id.lMiddleName);
        lastName = view.findViewById(R.id.lastName);
        lLastName = view.findViewById(R.id.lLastName);
        email = view.findViewById(R.id.email);
        lEmail = view.findViewById(R.id.lEmail);
        phone = view.findViewById(R.id.phone);
        lPhone = view.findViewById(R.id.lPhone);
        dob = view.findViewById(R.id.dateOfBirth);
        signOut = view.findViewById(R.id.signOut);
        radioGroup = view.findViewById(R.id.rgGender);
        tiet_other = view.findViewById(R.id.tiet_other);
        lOther = view.findViewById(R.id.lOther);
        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);
        other = view.findViewById(R.id.other);

        tiet_other.setEnabled(false);

        signOut.setOnClickListener(v -> {
            helper.signOut();
            ((HomeActivity)context).changeFragment("", "Login");
            ((HomeActivity)context).reset();
        });

        save.setOnClickListener(v -> {
            if(save()){
                LocalDate ldDOB = LocalDate.of(dob.getYear(), dob.getMonth()+1, dob.getDayOfMonth());
                u.setPhone(phone.getText().toString());
                u.setName(name.getText().toString());
                u.setMiddleName(middleName.getText().toString());
                u.setLastName(lastName.getText().toString());
                u.setPhone(phone.getText().toString());
                u.setDateOfBirth(ldDOB.toString());
                u.setGender(gender);
                ((HomeActivity)context).onClickSaveUserInformation(u);
            }
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

        changeCard.setOnClickListener(v -> ((HomeActivity)context).onClickChangeUserFragment(u, "Account_Card_Management"));
        changePassword.setOnClickListener(v -> ((HomeActivity)context).onClickChangeUserFragment(u, "Change_Password"));

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!checkString(s.toString(),2)){
                    lName.setError("Invalid name, must not contain numbers or special characters");
                    save.setEnabled(false);
                }
                else{
                    lName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
            public void afterTextChanged(Editable s) {}
        });

        dob.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
            LocalDate ldDOB = LocalDate.of(year, monthOfYear+1, dayOfMonth);
            LocalDate ldCurr = LocalDate.now();

            Period difference = Period.between(ldDOB, ldCurr);

            if(difference.getYears() < 18){
                Toast.makeText(context, "You need to be at least 18 years old to register!", Toast.LENGTH_SHORT).show();
            }
        });

        userInfo();

        return view;
    }

    public boolean save(){
        LocalDate ldDOB = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDayOfMonth());
        LocalDate ldCurr = LocalDate.now();

        Period difference = Period.between(ldDOB, ldCurr);

        if(lName.getError() != null || lMiddleName.getError() != null || lLastName.getError() != null || lLastName.getError() != null ||
                lEmail.getError() != null || lPhone.getError() != null || radioGroup.getCheckedRadioButtonId() == -1 ||difference.getYears() < 18 ||
                radioGroup.getCheckedRadioButtonId() == -1 || (radioGroup.getCheckedRadioButtonId() == R.id.other && lOther.getError() != null)){
            save.setEnabled(false);
            return false;
        }

        save.setEnabled(true);

        return true;
    }

    public void userInfo(){
        helper.getUser(userID, user -> {
            u = user;
            u.setEmail(userEmail);

            name.setText(u.getName());
            middleName.setText(u.getMiddleName());
            lastName.setText(u.getLastName());
            email.setText(u.getEmail());
            phone.setText(u.getPhone());

            if(u.getGender().toLowerCase().equals("female")){
                female.setChecked(true);
            }
            else if(u.getGender().toLowerCase().equals("male")){
                male.setChecked(true);
            }
            else{
                other.setChecked(true);
                tiet_other.setText(u.getGender().toString());
            }

            String[] date = u.getDateOfBirth().split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);

            dob.init(year, month, day, null);
        });
    }

    private boolean checkString(String s, int min){
        if(s.length() <= min || !s.matches("[a-zA-Z]+")){
            return false;
        }
        return true;
    }
}