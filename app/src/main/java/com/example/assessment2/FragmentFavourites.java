package com.example.assessment2;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentFavourites extends Fragment {


    private RecyclerView recycler;
    private RecyclerAdapter adapter;
    private ProgressBar progressBar;
    private TextView message;
    DBHelper helper;
    String userID;
    ArrayList<String> favouriteList;
    ArrayList<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        userID = getArguments().getString("UserID");

        progressBar = view.findViewById(R.id.progressBar);
        recycler = view.findViewById(R.id.recycleView);
        message = view.findViewById(R.id.message);
        helper = new DBHelper(getContext());

        loadFavourites();

        return view;
    }

    public void loadProducts(){

        helper.getSpecificProducts(favouriteList, products -> {

            productList = products;

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            adapter = new RecyclerAdapter(productList,favouriteList, getContext());
            recycler.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        });
    }

    public void loadFavourites(){

        helper.retrieveFavourites(userID, favourites -> {
            if(favourites.size() > 0){
                favouriteList = favourites;

                loadProducts();
            }
            else{
                progressBar.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
            }
        });
    }
}