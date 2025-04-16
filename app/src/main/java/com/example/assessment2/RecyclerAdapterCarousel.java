package com.example.assessment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterCarousel extends RecyclerView.Adapter<CarouselDataHolder> {

    private ArrayList<Product> productList;
    private Context context;

    public RecyclerAdapterCarousel(ArrayList<Product> list, Context c){
        this.productList = list;
        this.context = c;
    }

    @NonNull
    @Override
    public CarouselDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_view_home, parent, false);

        final CarouselDataHolder holder = new CarouselDataHolder(view);


        return new CarouselDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselDataHolder holder, int position) {
        final Product pd = productList.get(position);
        // Set the data to the views here

        // load first picture on the layout
        Picasso.get().load(pd.getImages().get(0)).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList == null? 0: productList.size();
    }
}
