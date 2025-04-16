package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentViewProduct extends Fragment {

    ImageView ivImage1;
    ImageView ivImage2;
    ImageView ivImage3;
    ImageView ivImage4;
    FirebaseAuth fbAuth;
    FirebaseUser currentUser;
    TextView productName;
    TextView productRestriction;
    TextView productPrice;
    TextView productDetails;
    TextView productAvailability;
    TextView productGame;
    TextView productCategory;
    ImageButton minus;
    TextView quantityTV;
    ImageButton plus;
    ImageButton btnGoBack;
    Button addToBasket;
    DBHelper helper;
    Product product;

    ArrayList<Category> categoryList = new ArrayList<>();
    ArrayList<Game> gameList = new ArrayList<>();

    String productID ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);

        ivImage1 = view.findViewById(R.id.iv_Image1);
        ivImage2 = view.findViewById(R.id.iv_Image2);
        ivImage3 = view.findViewById(R.id.iv_Image3);
        ivImage4 = view.findViewById(R.id.iv_Image4);
        productName = view.findViewById(R.id.productName);
        productRestriction = view.findViewById(R.id.productRestriction);
        productPrice = view.findViewById(R.id.productPrice);
        productDetails = view.findViewById(R.id.productDetails);
        productAvailability = view.findViewById(R.id.productAvailability);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        minus = view.findViewById(R.id.recycler_minus);
        quantityTV = view.findViewById(R.id.recyclerQuantity);
        plus = view.findViewById(R.id.recycler_plus);
        addToBasket = view.findViewById(R.id.addToBasket);
        productGame = view.findViewById(R.id.productGame);
        productCategory = view.findViewById(R.id.productCategory);

        Context context = getContext();

        btnGoBack.setOnClickListener(v -> {
            ((HomeActivity) context).changeFragment("", "GoBack");
        });

        minus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTV.getText().toString());
            if(quantity == 0){
                addToBasket.setEnabled(false);
            }else if(quantity != 0){
                quantity -= 1;
                if(quantity == 0){
                    addToBasket.setEnabled(false);
                }
            }
            quantityTV.setText(String.valueOf(quantity));
        });

        // add items - also establish the maximum before letting the user add more to the basket
        plus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTV.getText().toString());
            if(productAvailability.equals("pre-order") && quantity == 2){
                Toast.makeText(context, "This is a pre-order, the maximum of items is 2", Toast.LENGTH_SHORT).show();
            }
            else if(quantity != 10){
                addToBasket.setEnabled(true);
                quantity += 1;
            }
            else{
                Toast.makeText(context, "There is a limit of 10 items per user!", Toast.LENGTH_SHORT).show();
            }
            quantityTV.setText(String.valueOf(quantity));
        });

        addToBasket.setOnClickListener(v -> {
            ((HomeActivity)context).basketActions(product.getId(), Integer.parseInt(quantityTV.getText().toString()), "add");
        });

        productID = getArguments().getString("productID");

        fbAuth = FirebaseAuth.getInstance();
        currentUser = fbAuth.getCurrentUser();
        helper = new DBHelper(getContext());

        loadOtherInformation();

        return view;
    }
    public void loadOtherInformation(){
        helper.getCategories(categories -> {
            categoryList = categories;
            helper.getGames(games -> {
                gameList = games;
                loadProductInformation(productID);
            });
        });
    }

    public void loadProductInformation(String id){
        helper.getProduct((id), products -> {
            if(products.size() > 0){
                Product p = products.get(0);
                product = p;

                productName.setText(p.getName());
                productPrice.setText("£" + p.getPrice());

                if(Integer.parseInt(p.getQuantity()) > 100){
                    productAvailability.setText("Available");
                }
                else if(Integer.parseInt(p.getQuantity()) > 50){
                    productAvailability.setText("Limited");
                }
                else if(Integer.parseInt(p.getQuantity()) > 0){
                    productAvailability.setText("Just a few left");
                }
                else if(p.getType().equals("pre-order")){
                    productAvailability.setText("Pre-order now");
                }
                else{
                    productAvailability.setText("Out of stock");
                    addToBasket.setEnabled(false);
                    minus.setEnabled(false);
                    plus.setEnabled(true);
                }

                for(int i=0; i<gameList.size();i++){
                    if(gameList.get(i).getId().equals(p.getGameID())){
                        productGame.setText("Game: " + gameList.get(i).getName());
                    }
                }

                for(int i=0; i<categoryList.size();i++){
                    if(categoryList.get(i).getId().equals(p.getCategoryID())){
                        productCategory.setText("Type: " + categoryList.get(i).getName());
                    }
                }


                for(int i=0;i<p.getDetails().size();i++){
                    productDetails.setText(productDetails.getText() + p.getDetails().get(i).toString() + "\n");
                }

                productDetails.setText(productDetails.getText() + p.getDimensions() + "\n");

                productRestriction.setText("Age restriction: " + p.getAgeRestriction());
                loadImages(p.getImages());
            }
        });
    }

    public void loadImages(ArrayList<String> urls){
        for(int i=0; i < urls.size(); i++){
            switch (i){
                case 0:
                    Picasso.get().load(urls.get(i)).into(ivImage1);
                    break;
                case 1:
                    Picasso.get().load(urls.get(i)).into(ivImage2);
                    break;
                case 2:
                    Picasso.get().load(urls.get(i)).into(ivImage3);
                    break;
                default:
                    Picasso.get().load(urls.get(i)).into(ivImage4);
                    break;
            }
        }
    }
}