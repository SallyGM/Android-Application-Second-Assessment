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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FragmentChangePassword extends Fragment {


    DBHelper helper;
    Context context;

    TextInputEditText oldPassword;
    TextInputLayout lOldPassword;
    TextInputEditText password;
    TextInputLayout lPassword;
    TextInputEditText cPassword;
    TextInputLayout lCPassword;
    Button change;
    Button goback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);

        context = getContext();
        helper = new DBHelper(context);

        oldPassword = view.findViewById(R.id.oldPassword);
        lOldPassword = view.findViewById(R.id.lOldPassword);
        password = view.findViewById(R.id.password);
        lPassword = view.findViewById(R.id.lPassword);
        cPassword = view.findViewById(R.id.cPassword);
        lCPassword = view.findViewById(R.id.lCPassword);
        change = view.findViewById(R.id.change);
        goback = view.findViewById(R.id.go_back);

        change.setEnabled(false);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8 || s.length() > 16){
                    lPassword.setError("Please limit the password to minimum of 8 character and maximum 16 characters");
                    change.setEnabled(false);
                }
                else if (!s.toString().matches("^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$%^&*-]).{8,16}$")){
                    lPassword.setError("Password must contain uppercaser, lowercase, numbers and special characters!");
                    change.setEnabled(false);
                }
                else{
                    lPassword.setError(null);
                    change.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                    change.setEnabled(false);
                }
                else{
                    lCPassword.setError(null);
                    change.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8 || s.length() > 16){
                    lOldPassword.setError("Please limit the password to minimum of 8 character and maximum 16 characters");
                    change.setEnabled(false);
                }
                else if (!s.toString().matches("^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$%^&*-]).{8,16}$")){
                    lOldPassword.setError("Password must contain uppercaser, lowercase, numbers and special characters!");
                    change.setEnabled(false);
                }
                else{
                    lOldPassword.setError(null);
                    change.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        change.setOnClickListener(v -> updatePassword());
        goback.setOnClickListener(v -> {((HomeActivity)context).changeFragment("", "Account");});

        return view;

    }

    public void updatePassword(){
        helper.updateUserPassword(password.getText().toString(), oldPassword.getText().toString(), callback ->{
            ((HomeActivity)context).changeFragment("", "Account");
        });
    }
}