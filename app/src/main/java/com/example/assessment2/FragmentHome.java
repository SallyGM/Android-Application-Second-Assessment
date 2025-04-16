package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.carousel.CarouselLayoutManager;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    Context context;
    DBHelper helper;
    ArrayList<Product> products;
    RecyclerView recycler;
    ProgressBar progressBar;
    Button browse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
        helper = new DBHelper(context);
        products = new ArrayList<>();

        recycler = view.findViewById(R.id.recycler_home);
        progressBar = view.findViewById(R.id.progress_bar);
        browse = view.findViewById(R.id.browse);

        loadProducts();

        browse.setOnClickListener(v -> {((HomeActivity)context).changeFragment("", "Search");});

        return view;
    }

    public void loadProducts(){
        helper.getLastProducts(p -> {
            if(p.size() > 0){
                products = p;
            }

            RecyclerAdapterCarousel adapter = new RecyclerAdapterCarousel(products, getContext());
            CarouselLayoutManager layoutManager = new CarouselLayoutManager();
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        });
    }


}