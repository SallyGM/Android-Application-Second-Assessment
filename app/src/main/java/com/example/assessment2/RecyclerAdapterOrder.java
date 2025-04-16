package com.example.assessment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterOrder extends RecyclerView.Adapter<OrderHolder> implements Filterable {

    private ArrayList<Order> orderList;
    private ArrayList<Order> orderListFilter;
    private Context context;
    public RecyclerAdapterOrder(ArrayList<Order> list, Context c){
        this.orderList = list;
        this.context = c;
        this.orderListFilter = list;
    }
    
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_view_order, parent, false);

        final OrderHolder holder = new OrderHolder(view);

        //holder.view.setOnClickListener( v -> { });

        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {

        final Order order = orderList.get(position);
        // Set the data to the views here

        holder.setTitle("Order reference: " + order.getId());
        holder.setStatus("Status: " + order.getStatus());
        holder.setTotal("£"+ order.getPrice());
        holder.setDate("Order placed: " + order.getDate().toString());
        holder.setUpdate("Last Update: " + order.getLastUpdate().toString());

        String items = "";
        for(String key : order.getProductNames().keySet()){
            items += order.getProductNames().get(key) + " - Quantity: " + order.getItems().get(key) + "\n";
        }

        holder.setItems(items);
    }

    public interface OnClickListener {
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
