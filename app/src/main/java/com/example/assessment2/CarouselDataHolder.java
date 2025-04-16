package com.example.assessment2;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselDataHolder extends RecyclerView.ViewHolder {
    ImageView productImage;

    public CarouselDataHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.productImage);
    }

}
