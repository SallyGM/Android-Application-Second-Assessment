package com.example.assessment2;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductHolder extends RecyclerView.ViewHolder {

    TextView productId;
    TextView productName;
    TextView productAvailability;
    TextView productPrice;
    ImageButton productImage;
    Button productView;
    Button addToBasketButton;
    ImageButton recyclerMinusButton;
    TextView recyclerQuantity;
    ImageButton recyclerPlusButton;
    ImageButton recyclerFavouriteButton;

    public ProductHolder(@NonNull View itemView) {
        super(itemView);

        productId = itemView.findViewById(R.id.recyclerProductID);
        productName = itemView.findViewById(R.id.recyclerRef);
        productAvailability = itemView.findViewById(R.id.recyclerAvailability);
        productPrice = itemView.findViewById(R.id.recyclerPrice);
        productView = itemView.findViewById(R.id.recyclerViewButton);
        addToBasketButton = itemView.findViewById(R.id.recyclerAddToCartButton);
        productImage = itemView.findViewById(R.id.recyclerImage);
        recyclerMinusButton = itemView.findViewById(R.id.recycler_minus);
        recyclerQuantity = itemView.findViewById(R.id.recyclerQuantity);
        recyclerPlusButton = itemView.findViewById(R.id.recycler_plus);
        recyclerFavouriteButton = itemView.findViewById(R.id.recyclerFavourite);
    }

    public void setProductName(String productName) {
        this.productName.setText(productName);
    }

    public void setProductAvailability(String productAvailability) {
        this.productAvailability.setText(productAvailability);
    }
    public String getProductAvailability() {
        return this.productAvailability.getText().toString();
    }

    public void setPrice(String productPrice) {
        this.productPrice.setText(productPrice);
    }

    public void setProductId(String productId) {
        this.productId.setText(productId);
    }

    public String getProductId() {
        return productId.getText().toString();
    }

    public String getRecyclerQuantity() {
        return recyclerQuantity.getText().toString();
    }

    public void setRecyclerQuantity(String recyclerQuantity) {
        this.recyclerQuantity.setText(recyclerQuantity);
    }

    public ImageButton getRecyclerFavouriteButton() {
        return recyclerFavouriteButton;
    }

    public void setRecyclerFavouriteButton(ImageButton recyclerFavouriteButton) {
        this.recyclerFavouriteButton = recyclerFavouriteButton;
    }
}
