package com.example.assessment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentProduct extends Fragment {

    private RecyclerView recycler;
    private RecyclerAdapter adapter;
    private ProgressBar productProgressBar;
    private ArrayList<Product> productData = new ArrayList<>();

    private ImageButton ib_Filter;
    private TextInputEditText keyword;
    private ArrayList<Game> gamesList;
    private ArrayList<Category> categoriesList;
    private ArrayList<String> favouriteList;
    DBHelper helper;
    String userID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        userID = getArguments().getString("UserID");

        helper = new DBHelper(getContext());
        gamesList = new ArrayList<>();
        categoriesList = new ArrayList<>();

        ib_Filter = view.findViewById(R.id.button_filter);
        keyword = view.findViewById(R.id.search);
        productProgressBar = view.findViewById(R.id.product_progressbar);
        productProgressBar.setVisibility(View.VISIBLE);

        // send
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //adapter.getFilter().filter(s.toString());
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // load product, games, and categories
        if(!userID.equals("")){
            loadFavourites();
        }

        loadGames();
        loadCategory();
        loadProducts(view);

        //region popWindow
        ib_Filter.setOnClickListener(v -> {
            View popupWindow = inflater.inflate(R.layout.pop_window_filter, null);
            createPopWindow(view, popupWindow);
        });
        //endregion popWindow
        return view;
    }

    public void createPopWindow(View view, View popupWindowFilter){
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupWindowFilter, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        LinearLayout ll_game = popupWindowFilter.findViewById(R.id.ll_game);
        LinearLayout ll_category = popupWindowFilter.findViewById(R.id.ll_category);
        Button submit = popupWindowFilter.findViewById(R.id.submit);
        Button clear = popupWindowFilter.findViewById(R.id.clear);
        ImageButton exit =  popupWindowFilter.findViewById(R.id.exit);

        HashMap<String, Integer> gameCheckBox = new HashMap<>();
        HashMap<String, Integer> categoryCheckBox = new HashMap<>();

        // create checkbox for the games
        if(gamesList.size()>0){
            for(int i=0; i<gamesList.size();i++){
                CheckBox cb = new CheckBox(popupWindowFilter.getContext());
                cb.setText(gamesList.get(i).getName());
                cb.setId(i);
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        gameCheckBox.put(buttonView.getText().toString(), cb.getId());
                    }
                    else{
                        gameCheckBox.remove(buttonView.getText().toString());
                    }
                });

                // add to the LinearLayout
                ll_game.addView(cb);
            }
        }

        if(categoriesList.size()>0){
            for(int i=0; i<categoriesList.size();i++){
                CheckBox cb = new CheckBox(popupWindowFilter.getContext());
                cb.setText(categoriesList.get(i).getName());
                cb.setId(gamesList.size() + i);
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        categoryCheckBox.put(buttonView.getText().toString(), cb.getId());
                    }
                    else{
                        categoryCheckBox.remove(buttonView.getText().toString());
                    }
                });

                ll_category.addView(cb);
            }
        }

        submit.setOnClickListener(v1 -> {
            String filter = "";

            if(gameCheckBox.size() > 0){
                for(String key : gameCheckBox.keySet()){

                    int pos = 0;
                    for(int i=0; i<gamesList.size();i++){
                        if(gamesList.get(i).getName().equals(key)){
                            pos = i;
                        }
                    }

                    filter += gamesList.get(pos).getId() + "|";
                }
            }

            if(categoryCheckBox.size() > 0){
                filter += (gameCheckBox.size() > 0 ? "?" : "");

                for(String key : categoryCheckBox.keySet()){

                    int pos = 0;
                    for(int i=0; i<categoriesList.size();i++){
                        if(categoriesList.get(i).getName().equals(key)){
                            pos = i;
                        }
                    }

                    filter += categoriesList.get(pos).getId() + "|";
                }
            }

            if(gameCheckBox.size() == 0 && categoryCheckBox.size() == 0){
                filterList("");
            }
            else{
                filterList(filter);
            }
        });

        clear.setOnClickListener(v1 -> {
            if(gameCheckBox.size() > 0){
                for(String key : gameCheckBox.keySet()){
                    CheckBox cb = (CheckBox) popupWindowFilter.findViewById(gameCheckBox.get(key));
                    cb.setChecked(false);
                }
            }

            if(categoryCheckBox.size() > 0){
                for(String key : categoryCheckBox.keySet()){
                    CheckBox cb = (CheckBox) popupWindowFilter.findViewById(categoryCheckBox.get(key));
                    cb.setChecked(false);
                }
            }
        });

        exit.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return true;
        });

        // dismiss the popup window when touched
        popupWindowFilter.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    public void loadFavourites(){
        helper.retrieveFavourites(userID, favourites -> {
            if(favourites.size() > 0){
                favouriteList = favourites;
            }
        });
    }

    public void loadCategory(){
        helper.getCategories(categories -> {
            if(categories.size() > 0){
                for(int i=0; i<categories.size(); i++){
                    categoriesList.add(categories.get(i));
                }
            }
        });
    }

    public void loadGames(){
        helper.getGames(games -> {
            if(games.size() > 0){
                for(int i=0; i<games.size(); i++){
                    gamesList.add(games.get(i));
                }
            }
        });
    }
    public void loadProducts(View view){
        helper.getProducts(products -> {

            // clear list in case there is an update
            productData.clear();

            if(products.size() > 0){
                for(int i=0; i < products.size(); i++){
                    Product p = products.get(i);

                    Product pd = new Product(p.getId(),p.getName(), p.getPrice(), p.getDetails(), p.getDimensions(), p.getType(), p.getQuantity(), p.getAgeRestriction(), p.getGameID(), p.getCategoryID(), p.getImages());

                    productData.add(pd);
                }
            }

            recycler = view.findViewById(R.id.recycleView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            adapter = new RecyclerAdapter(productData,favouriteList,getContext());
            recycler.setAdapter(adapter);

            productProgressBar.setVisibility(View.GONE);
        });
    }

    public void filterList(String filter){
        // sends the filter word to the adapter
        adapter.getFilter().filter(filter);
    }
}

