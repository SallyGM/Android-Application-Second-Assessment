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

public class FragmentLogin extends Fragment{

    TextInputEditText email;
    TextInputEditText password;
    TextInputLayout lEmail;
    TextInputLayout lPassword;
    Button register;
    Button login;
    Button forgotPassword;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        lPassword = view.findViewById(R.id.lPassword);
        lEmail = view.findViewById(R.id.lEmail);
        register = view.findViewById(R.id.register);
        login = view.findViewById(R.id.login);
        forgotPassword = view.findViewById(R.id.forgotPassword);

        context = getContext();

        forgotPassword.setOnClickListener(v -> {
            ((HomeActivity)context).changeFragment("", "Forgot_Password");
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.length() < 8){
                    lPassword.setError("Password is invalid!");
                }
                else{
                    lPassword.setError(null);
                    password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!email.getText().toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
                    lEmail.setError("Invalid email!");
                }
                else{
                    lEmail.setError(null);
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        register.setOnClickListener(v -> {
            ((HomeActivity)context).changeFragment("", "Register");
        });

        login.setOnClickListener(v -> {
            // verify if login is valid
            DBHelper helper = new DBHelper(getContext());
            helper.signIn(email.getText().toString().toLowerCase(), password.getText().toString(), callback ->{
                ((HomeActivity)context).reset();
            });
        });

        // Inflate the layout for this fragment
        return view;
    }
}