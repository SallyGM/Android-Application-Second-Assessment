package com.example.assessment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecyclerAdapterCard extends RecyclerView.Adapter<CardHolder> {

    private ArrayList<Card> cardList;
    private Context context;


    public RecyclerAdapterCard(ArrayList<Card> list, Context c){

        this.cardList = list;
        this.context = c;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_view_card, parent, false);
        final CardHolder holder = new CardHolder(view);

        holder.delete.setOnClickListener(v -> {
            if(cardList.size()>1){
                Card card = cardList.stream().filter(c-> c.getCardNumber().equals(holder.getCard())).findFirst().get();
                ArrayList<Card> list = new ArrayList<>();
                list.add(card);
                ((HomeActivity)context).cardManagement("Remove_Card", null, list, callback -> {
                    if(callback){
                        cardList.remove(card);
                        notifyDataSetChanged();
                    }
                });
            }
            else{
                Toast.makeText(context, "You need at least one card attached to your account", Toast.LENGTH_SHORT).show();
            }
        });

        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        final Card card = cardList.get(position);
        // Set the data to the views here

        holder.setCard(card.getCardNumber());
        holder.setTitle("XXXX XXXX XXXX "+ card.getCardNumber().substring(card.getCardNumber().length()-5, card.getCardNumber().length()-1));

        String[] date = card.getExpDate().toString().split("-");
        holder.setExpirationDate("Expiration date: " + date[0] + "/" + date[1]);

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
}
