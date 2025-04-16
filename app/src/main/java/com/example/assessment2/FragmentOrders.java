package com.example.assessment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentOrders extends Fragment {

    DBHelper helper;
    String userID;
    ProgressBar progressBar;
    private RecyclerView recycler;
    ArrayList<Order> order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        recycler = view.findViewById(R.id.recycleView);

        userID = getArguments().getString("UserID");
        helper = new DBHelper(getContext());

        loadOrder();

        return view;
    }

    public void loadProducts(){

        helper.getProducts(products -> {
            for(int i=0; i<order.size();i++){
                HashMap<String,String> productNames = new HashMap<>();
                for(String key : order.get(i).getItems().keySet()){
                    Product p = products.stream().filter(x -> x.getId().equals(key)).findFirst().get();
                    productNames.put(key, p.getName());
                }
                order.get(i).setProductNames(productNames);
            }

            RecyclerAdapterOrder adapter = new RecyclerAdapterOrder(order, getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        });
    }

    public void loadOrder(){
        helper.getOrders(userID, orders -> {
            if(orders.size() == 0){
                progressBar.setVisibility(View.GONE);
                //add message
            }
            else{
                order = orders;
                loadProducts();
            }
        });
    }
}