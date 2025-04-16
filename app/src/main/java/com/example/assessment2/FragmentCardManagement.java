package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class FragmentCardManagement extends Fragment {

    User user;
    ProgressBar progressBar;
    RecyclerView recycler;
    Button addCard;
    Button goBack;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_management, container, false);

        context = getContext();

        user = getArguments().getParcelable("User");
        progressBar = view.findViewById(R.id.progress_bar);
        recycler = view.findViewById(R.id.recycleView);
        addCard = view.findViewById(R.id.addANewCard);
        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(v -> ((HomeActivity)context).changeFragment("", "Account"));
        addCard.setOnClickListener(v -> ((HomeActivity)context).onClickChangeUserFragment(user, "Add_Card"));
        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {
        RecyclerAdapterCard adapter = new RecyclerAdapterCard(user.getCard(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

    }


}
