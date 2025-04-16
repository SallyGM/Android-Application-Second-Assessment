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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;


public class FragmentForgotPassword extends Fragment {

    Context context;
    TextInputEditText email;
    TextInputLayout lEmail;
    Button submit;
    Button goBack;

    TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        context = getContext();
        email = view.findViewById(R.id.email);
        lEmail = view.findViewById(R.id.lEmail);
        submit = view.findViewById(R.id.submit);
        goBack = view.findViewById(R.id.go_Back);
        message = view.findViewById(R.id.message);

        submit.setEnabled(false);
        message.setVisibility(View.GONE);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!email.getText().toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
                    lEmail.setError("Invalid email!");
                    submit.setEnabled(false);
                }
                else{
                    lEmail.setError(null);
                    email.setError(null);
                    submit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submit.setOnClickListener(v -> {
            DBHelper helper = new DBHelper(context);
            helper.recovery(email.getText().toString());
            submit.setEnabled(false);
            message.setVisibility(View.VISIBLE);
        });

        goBack.setOnClickListener(v-> ((HomeActivity)context).changeFragment("", "Login"));

        return view;
    }
}