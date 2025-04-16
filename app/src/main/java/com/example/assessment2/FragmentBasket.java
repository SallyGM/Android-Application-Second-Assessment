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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentBasket extends Fragment {

    Context context;
    DBHelper helper;
    LinearLayout ll_items;
    ArrayList<Product> productData;
    private RecyclerView recycler;
    private RecyclerAdapterBasket adapter;
    private ProgressBar productProgressBar;

    private ArrayList<String> favouriteList;
    private TextView totalInBasket;
    private Button clearBasket;
    private Button checkOut;
    Basket basket = new Basket();
    HashMap<String, Integer> basketGuest = new HashMap<>();
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        context = getContext();

        ll_items = view.findViewById(R.id.ll_items);
        totalInBasket = view.findViewById(R.id.total);
        clearBasket = view.findViewById(R.id.clearBasket);
        checkOut = view.findViewById(R.id.checkOut);
        recycler = view.findViewById(R.id.recycleView);

        // clear basket
        clearBasket.setOnClickListener(v -> ((HomeActivity)context).basketActions("",0,""));
        checkOut.setOnClickListener(v ->
        {
            if(userID.equals("")){
                Toast.makeText(context, "You need an account to checkout!",  Toast.LENGTH_SHORT).show();
                ((HomeActivity)context).changeFragment("", "Login");
            }
            else{
                if(basket.getProducts() != null){
                    ((HomeActivity)context).onClickToCheckOut(productData, basket);
                }
                else{
                    Toast.makeText(context, "Please add items to your basket to access the CheckOut",  Toast.LENGTH_SHORT).show();
                }
            }
        });

        productProgressBar = view.findViewById(R.id.product_progressbar);
        productProgressBar.setVisibility(View.VISIBLE);

        helper = new DBHelper(getContext());

        userID = getArguments().getString("UserID");
        if(userID.equals("")){
            basketGuest = (HashMap<String, Integer>) getArguments().getSerializable("BasketGuest");
        }

        if(!userID.equals("")){
            loadFavourites();
        }

        loadBasket();

        return view;
    }


    public void loadFavourites(){
        helper.retrieveFavourites(userID, favourites -> {
            if(favourites.size() > 0){
                favouriteList = favourites;
            }
        });
    }

    public void loadProducts(HashMap<String, Integer> productsID){
        ArrayList<String> keys = new ArrayList<>(productsID.keySet());
        productData = new ArrayList<>();

        helper.getSpecificProducts(keys, products -> {
            if(products.size() > 0){
                for(int i=0; i<products.size();i++){
                    // Add the amount of items added to the cart
                    int value = Integer.parseInt(String.valueOf((productsID.get(products.get(i).getId()))));

                    products.get(i).setOnCart(value);
                    productData.add(products.get(i));
                }
            }

            // Adding adapter to recycler view to display items on the basket
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            adapter = new RecyclerAdapterBasket(productData, favouriteList, getContext());
            recycler.setAdapter(adapter);

            productProgressBar.setVisibility(View.GONE);
            // updating value of the basket
            updateBasketValue();
        });
    }

    public void updateBasketValue(){
        // in case basket is empty
        double total = 0;

        if(productData.size()>0){
            for(int i=0; i<productData.size();i++){
                total += Double.parseDouble(productData.get(i).getPrice()) * productData.get(i).getOnCart();
            }

            totalInBasket.setText("£"+ total);
        }
    }

    public void loadBasket(){

        if(userID.equals("")){
            if(basketGuest != null){
                if(basketGuest.size()>0){
                    loadProducts(basketGuest);
                }
                else{
                    // if the basket is empty just display a message
                    displayNoItems ();
                    productProgressBar.setVisibility(View.GONE);
                }
            }
            else{
                // if the basket is empty just display a message
                displayNoItems ();
                productProgressBar.setVisibility(View.GONE);
            }
        }
        else{
            helper.getBasket(userID, baskets ->{
                if(baskets.size() > 0){
                    basket = baskets.get(0);
                    if(basket.getId() != null && basket.getProducts().size()>0){
                        loadProducts(basket.getProducts());
                    }
                    else{
                        // if the basket is empty just display a message
                        displayNoItems ();
                        productProgressBar.setVisibility(View.GONE);
                    }
                }
                else{
                    // if the basket is empty just display a message
                    displayNoItems ();
                    productProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void displayNoItems (){
        TextView tv = new TextView(getContext());
        tv.setText("Your basket is empty!");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ll_items.addView(tv);
    }
}