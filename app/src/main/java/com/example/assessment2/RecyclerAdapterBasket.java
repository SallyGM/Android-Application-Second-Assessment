package com.example.assessment2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerAdapterBasket extends RecyclerView.Adapter<ProductHolderBasket> implements Filterable {

    private ArrayList<Product> productList;
    private ArrayList<Product> productListFilter;
    private ArrayList<String> favouriteList;
    private Context context;

    public RecyclerAdapterBasket(ArrayList<Product> list, ArrayList<String> favourites,  Context c){
        this.productList = list;
        this.context = c;
        this.favouriteList = favourites;
        this.productListFilter = list;
    }

    @NonNull
    @Override
    public ProductHolderBasket onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_view_basket_layout, parent, false);

        final ProductHolderBasket holder = new ProductHolderBasket(view);

        holder.productView.setOnClickListener(v -> ((HomeActivity)context).changeFragment(holder.productId.getText().toString(), "View"));
        holder.changeButton.setOnClickListener(v -> {((HomeActivity)context).basketActions(holder.getProductId(), Integer.parseInt(holder.getRecyclerQuantity()), "change");});

        holder.removeButton.setOnClickListener(v -> ((HomeActivity)context).basketActions(holder.getProductId(), 0, ""));

        // remove items on the button
        holder.recyclerMinusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.recyclerQuantity.getText().toString());
            if(quantity != 0){
                quantity -= 1;
            }
            holder.recyclerQuantity.setText(String.valueOf(quantity));
        });

        // add items - also establish the maximum before letting the user add more to the basket
        holder.recyclerPlusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.recyclerQuantity.getText().toString());
            if(holder.getProductAvailability().equals("pre-order") && quantity == 2){
                Toast.makeText(context, "This is a pre-order, the maximum of items is 2", Toast.LENGTH_SHORT).show();
            }
            else if(quantity != 10){
                quantity += 1;
            }
            else{
                Toast.makeText(context, "There is a limit of 10 items per user!", Toast.LENGTH_SHORT).show();
            }
            holder.recyclerQuantity.setText(String.valueOf(quantity));
        });

        holder.recyclerFavouriteButton.setOnClickListener(v -> {
            ((HomeActivity)context).onClickFavourites(holder.productId.getText().toString(), result -> {
                if(result == 1){
                    holder.recyclerFavouriteButton.setImageResource(R.drawable.baseline_favorite_24);
                    holder.recyclerFavouriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bittersweet)));
                }
                else if(result == 2){
                    holder.recyclerFavouriteButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    holder.recyclerFavouriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
                }
            });
        });

        return new ProductHolderBasket(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductHolderBasket holder, int position) {
        final Product pd = productList.get(position);
        // Set the data to the views here

        holder.setProductId(pd.getId());
        holder.setProductName(pd.getName());

        // in case the list is empty
        if(favouriteList != null){
            if(favouriteList.contains(pd.getId())){
                holder.recyclerFavouriteButton.setImageResource(R.drawable.baseline_favorite_24);
                holder.recyclerFavouriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bittersweet)));
            }
            else{
                holder.recyclerFavouriteButton.setImageResource(R.drawable.baseline_favorite_border_24);
                holder.recyclerFavouriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
            }
        }

        if(Integer.parseInt(pd.getQuantity()) > 100){
            holder.setProductAvailability("Available");
        }
        else if(Integer.parseInt(pd.getQuantity()) > 50){
            holder.setProductAvailability("Limited");
        }
        else if(Integer.parseInt(pd.getQuantity()) > 0){
            holder.setProductAvailability("Just a few left");
        }
        else if(pd.getType().equals("pre-order")){
            holder.setProductAvailability("Pre-order now");
        }
        else{
            holder.setProductAvailability("Out of stock");
        }

        holder.setPrice("£"+ pd.getPrice());
        holder.setRecyclerQuantity(String.valueOf(pd.getOnCart()));
        holder.onCart = pd.getOnCart();

        // load first picture on the layout
        Picasso.get().load(pd.getImages().get(0)).into(holder.productImage);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                String keyword = constraint.toString();

                // if there are filters being applied to the search
                if(constraint == null || constraint.length() == 0 || constraint.equals("")){
                    results.values = productListFilter;
                    results.count = productListFilter.size();
                }
                // filter for both game and category
                else if(keyword.contains("?")){
                    ArrayList<String> filter = new ArrayList<>(Arrays.asList(keyword.split("\\?")));
                    ArrayList<String> games = new ArrayList<>(Arrays.asList(filter.get(0).split("\\|")));
                    ArrayList<String> category = new ArrayList<>(Arrays.asList(filter.get(1).split("\\|")));

                    List<Product> products = new ArrayList<>();

                    for(Product p : productListFilter){
                        if(games.contains(p.getGameID()) && category.contains(p.getCategoryID())){
                            products.add(p);
                        }
                    }

                    results.values = products;
                    results.count = products.size();
                }
                // only for game or only for category
                else if(keyword.contains("|")){
                    ArrayList<String> filter = new ArrayList<>(Arrays.asList(keyword.split("\\|")));
                    List<Product> products = new ArrayList<>();
                    for(Product p : productListFilter){
                        if(filter.contains(p.getGameID()) || filter.contains(p.getCategoryID())){
                            products.add(p);
                        }
                    }

                    results.values = products;
                    results.count = products.size();
                }
                // general search filter
                else{
                    keyword = keyword.toLowerCase();
                    List<Product> products = new ArrayList<>();
                    for(Product p : productListFilter){
                        if(p.getName().toLowerCase().contains(keyword)){
                            products.add(p);
                        }
                    }

                    results.values = products;
                    results.count = products.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public interface OnClickListener {
    }

    @Override
    public int getItemCount() {
        return productList == null? 0: productList.size();
    }
}
