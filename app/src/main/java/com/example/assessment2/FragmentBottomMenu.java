package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentBottomMenu extends Fragment {

    ImageButton search;
    ImageButton favourites;
    ImageButton cart;
    ImageButton orders;
    public TextView total;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_menu, container, false);

        search = view.findViewById(R.id.search);
        favourites = view.findViewById(R.id.favourite);
        cart = view.findViewById(R.id.cart);
        orders = view.findViewById(R.id.orders);
        total = view.findViewById(R.id.total);

        context = getContext();


        search.setOnClickListener(v -> {
            ((HomeActivity) context).changeFragment("", "Search");
        });

        favourites.setOnClickListener(v -> {
            ((HomeActivity) context).changeFragment("", "Favourites");
        });

        cart.setOnClickListener(v -> {
            ((HomeActivity) context).changeFragment("", "Basket");
        });

        orders.setOnClickListener(v -> {
            ((HomeActivity) context).changeFragment("", "Orders");
        });

        return view;
    }
}