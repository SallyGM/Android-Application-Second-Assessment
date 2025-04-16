package com.example.assessment2;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductHolderBasket extends RecyclerView.ViewHolder {

    ImageButton recyclerFavouriteButton;
    TextView productId;
    TextView productName;
    TextView productAvailability;
    TextView productPrice;
    ImageButton productImage;
    Button productView;
    Button changeButton;
    Button removeButton;
    TextView recyclerQuantity;
    ImageButton recyclerMinusButton;
    ImageButton recyclerPlusButton;
    public int onCart;

    public ProductHolderBasket(@NonNull View itemView) {
        super(itemView);

        productId = itemView.findViewById(R.id.recyclerProductID);
        productName = itemView.findViewById(R.id.recyclerRef);
        productAvailability = itemView.findViewById(R.id.recyclerAvailability);
        productPrice = itemView.findViewById(R.id.recyclerPrice);
        productView = itemView.findViewById(R.id.recyclerViewButton);
        changeButton = itemView.findViewById(R.id.recyclerChangeButton);
        removeButton = itemView.findViewById(R.id.recyclerRemoveItemButton);
        productImage = itemView.findViewById(R.id.recyclerImage);
        recyclerQuantity = itemView.findViewById(R.id.recyclerQuantity);
        recyclerMinusButton = itemView.findViewById(R.id.recycler_minus);
        recyclerPlusButton = itemView.findViewById(R.id.recycler_plus);
        recyclerFavouriteButton = itemView.findViewById(R.id.recyclerFavourite);
    }

    public void setProductName(String productName) {
        this.productName.setText(productName);
    }

    public void setProductAvailability(String productAvailability) {
        this.productAvailability.setText(productAvailability);
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

    public String getProductAvailability() {
        return this.productAvailability.getText().toString();
    }

    public String getRecyclerQuantity() {
        return recyclerQuantity.getText().toString();
    }

    public void setRecyclerQuantity(String recyclerQuantity) {
        this.recyclerQuantity.setText(recyclerQuantity);;
    }

    public ImageButton getRecyclerFavouriteButton() {
        return recyclerFavouriteButton;
    }

    public void setRecyclerFavouriteButton(ImageButton recyclerFavouriteButton) {
        this.recyclerFavouriteButton = recyclerFavouriteButton;
    }
}
