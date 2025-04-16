package com.example.assessment2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardHolder extends RecyclerView.ViewHolder{

    TextView title;
    TextView expirationDate;
    String card;
    Button delete;

    public CardHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.recyclerRef);
        expirationDate = itemView.findViewById(R.id.expirationDate);
        delete = itemView.findViewById(R.id.recyclerRemoveCard);
    }

    //region Getters and Setters
    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getExpirationDate() {
        return expirationDate.getText().toString();
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate.setText(expirationDate);
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    //endregion
}
