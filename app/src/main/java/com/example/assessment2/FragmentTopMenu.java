package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentTopMenu extends Fragment {

    TextView tvUsername;
    ImageButton home;
    ImageButton account;
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_menu, container, false);

        tvUsername = view.findViewById(R.id.username);
        home = view.findViewById(R.id.home_button);
        account = view.findViewById(R.id.account_button);

        context = getContext();

        home.setOnClickListener(v -> ((HomeActivity)context).changeFragment("", "Home"));
        account.setOnClickListener(v -> ((HomeActivity)context).changeFragment("", "Account"));

        return view;
    }
}