package com.example.assessment2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView items;
    TextView date;
    TextView update;
    TextView status;
    TextView total;
    Button view;

    public OrderHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.recyclerRef);
        items = itemView.findViewById(R.id.recyclerItem);
        status = itemView.findViewById(R.id.recyclerStatus);
        total = itemView.findViewById(R.id.recyclerTotal);
        view = itemView.findViewById(R.id.recyclerViewButton);
        date = itemView.findViewById(R.id.recyclerDate);
        update = itemView.findViewById(R.id.recyclerUpdate);
    }

    //region Getters and Setters
    public TextView getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public TextView getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items.setText(items);
    }

    public TextView getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public TextView getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total.setText(total);;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.setText(date);
    }

    public TextView getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update.setText(update);
    }
    //endregion
}
