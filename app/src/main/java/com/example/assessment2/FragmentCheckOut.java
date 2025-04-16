package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FragmentCheckOut extends Fragment {

    TextInputEditText name;
    TextInputLayout lName;
    TextInputEditText firstLine;
    TextInputLayout lFirstLine;
    TextInputEditText secondLine;
    TextInputLayout lSecondLine;
    TextInputEditText thirdLine;
    TextInputLayout lThirdLine;
    TextInputEditText postcode;
    TextInputLayout lPostcode;
    TextInputLayout lCountry;
    AutoCompleteTextView country;
    TextInputLayout lCity;
    AutoCompleteTextView city;
    TextInputLayout lCounty;
    AutoCompleteTextView county;
    TextView card;
    Button selectACard;
    Button pay;
    Button cancel;
    Button clear;
    Context context;
    ArrayList<Card> cardsList;
    ArrayList<Product> products;
    Basket basket;
    int currentCard = 0;

    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);


        userID = getArguments().getString("UserID");
        products = getArguments().getParcelableArrayList("Products");
        basket = getArguments().getParcelable("Basket");

        name = view.findViewById(R.id.name);
        lName = view.findViewById(R.id.lName);
        firstLine = view.findViewById(R.id.firstLine);
        lFirstLine = view.findViewById(R.id.lFirstLine);
        secondLine = view.findViewById(R.id.secondLine);
        lSecondLine = view.findViewById(R.id.lSecondLine);
        thirdLine = view.findViewById(R.id.thirdLine);
        lThirdLine = view.findViewById(R.id.lThirdLine);
        postcode = view.findViewById(R.id.postcode);
        lPostcode = view.findViewById(R.id.lPostcode);
        country = view.findViewById(R.id.country);
        lCountry = view.findViewById(R.id.lCountry);
        city = view.findViewById(R.id.city);
        lCity = view.findViewById(R.id.lCity);
        county =view.findViewById(R.id.county);
        lCounty = view.findViewById(R.id.lCounty);
        card = view.findViewById(R.id.card);
        selectACard = view.findViewById(R.id.selectACard);
        pay = view.findViewById(R.id.pay);
        cancel = view.findViewById(R.id.cancel);
        clear = view.findViewById(R.id.clear);

        context = getContext();

        country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayAdapter<String> adapterCity;
                ArrayAdapter<String> adapterCounty;
                String[] cityStringArray;
                String[] countyStringArray;

                switch (s.toString().toLowerCase()){
                    case "england":
                        cityStringArray = getResources().getStringArray(R.array.england_city);
                        countyStringArray = getResources().getStringArray(R.array.england_counties);
                        break;
                    case "wales":
                        cityStringArray = getResources().getStringArray(R.array.wales_cities);
                        countyStringArray = getResources().getStringArray(R.array.wales_counties);
                        break;
                    case "scotland":
                        cityStringArray = getResources().getStringArray(R.array.scotland_cities);
                        countyStringArray = getResources().getStringArray(R.array.scotland_counties);
                        break;
                    default:
                        cityStringArray = getResources().getStringArray(R.array.northern_ireland_cities);
                        countyStringArray = getResources().getStringArray(R.array.northern_ireland_counties);
                        break;
                }

                adapterCity = new ArrayAdapter<>(view.getContext(),
                        android.R.layout.simple_dropdown_item_1line, cityStringArray);
                city.setAdapter(adapterCity);
                lCity.setEnabled(true);
                adapterCounty = new ArrayAdapter<>(view.getContext(),
                        android.R.layout.simple_dropdown_item_1line, countyStringArray);
                county.setAdapter(adapterCounty);
                lCounty.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().matches("[a-zA-Z\\s]+")){
                    lName.setError("Invalid name, please insert only letters and space");
                }
                else if(s.toString().length() < 5){
                    lName.setError("Name is too short");
                }
                else{
                    lName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firstLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!checkString(s.toString())){
                    lFirstLine.setError("Please do not insert special characters");
                }
                else if(s.toString().length() == 0){
                    lFirstLine.setError("Please fill the first line of your address");
                }
                else{
                    lFirstLine.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 0 && !checkString(s.toString())){
                    lSecondLine.setError("Please do not insert special characters");
                }
                else{
                    lSecondLine.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        thirdLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 0 && !checkString(s.toString())){
                    lThirdLine.setError("Please do not insert special characters");
                }
                else{
                    lThirdLine.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<6){
                    lPostcode.setError("The post code has to be at least 6 characters, please do not insert a space");
                }
                else{
                    lPostcode.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pay.setOnClickListener(v -> {

            if(!validate()){
                Toast.makeText(context, "Please ensure obligatory fields are filled**", Toast.LENGTH_SHORT).show();
            }
            else{
                // showing popUpWindow
                createSecurityCodePopWindow(view, inflater);
            }
        });

        cancel.setOnClickListener(v -> ((HomeActivity)context).changeFragment("","Basket"));

        clear.setOnClickListener(v -> {
            name.setText("");
            lName.setError(null);
            firstLine.setText("");
            lFirstLine.setError(null);
            secondLine.setText("");
            lSecondLine.setError(null);
            thirdLine.setText("");
            lThirdLine.setError(null);
            postcode.setText("");
            lPostcode.setError(null);
        });

        getCard(userID);

        selectACard.setOnClickListener(v -> {
            // create a open PopUpWindow
            createPopWindow(view, inflater);
        });

        return view;
    }

    public void setCard(String cardN, Integer pos){
        currentCard = pos;
        card.setText(cardN);
    }

    public void getCard(String id){
        DBHelper helper = new DBHelper(context);

        helper.getCards(id, cards -> {

            this.cardsList = cards;
            String cardNumber = cards.get(0).getCardNumber();
            currentCard = 0;

            card.setText("**** **** ****" + cardNumber.substring(cardNumber.length()-5,cardNumber.length()-1));
        });
    }

    public boolean validate(){

        if(name.getText().length() == 0){
            lName.setError("Obligatory*");
            return false;
        }
        else if(postcode.getText().length() == 0){
            lPostcode.setError("Obligatory*");
            return false;
        }
        else if(firstLine.getText().length() == 0){
            lFirstLine.setError("Obligatory*");
            return false;
        }
        else if(city.getText().toString().isEmpty()){
            lCity.setError("Obligatory*");
            return false;

        }
        else if(country.getText().toString().isEmpty()){
            lCountry.setError("Obligatory*");
            return false;
        }
        else{
            lName.setError(null);
            lFirstLine.setError(null);
            lCity.setError(null);
            lCountry.setError(null);
            return true;
        }
    }

    public boolean checkString(String word){
        return word.matches("^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$");
    }

    public void doTransaction(View view,LayoutInflater inflater){
        View popUpWindow = inflater.inflate(R.layout.pop_window_processing_payment, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popUpWindow, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button next = popUpWindow.findViewById(R.id.next);
        ProgressBar progressBar = popUpWindow.findViewById(R.id.progress_bar);

        next.setOnClickListener(v1 -> {
            popupWindow.dismiss();
            ((HomeActivity)context).changeFragment("", "Orders");
        });

        // dismiss the popup window when touched
        popUpWindow.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            ((HomeActivity)context).changeFragment("", "Home");
            return true;
        });
        //endregion

        // do the checkout
        Address a = new Address(country.getText().toString(), county.getText().toString(), firstLine.getText().toString(),
                secondLine.getText().toString(), thirdLine.getText().toString(), postcode.getText().toString());

        // create date of the order and last update
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        double price = 0;
        ArrayList<String> productsID = new ArrayList<>();

        // get the price
        for(int i=0;i<products.size();i++){
            price += Double.parseDouble(products.get(i).getPrice()) * Integer.parseInt(String.valueOf(basket.getProducts().get(products.get(i).getId())));
            productsID.add(products.get(i).getId());
        }

        Order o = new Order(a, date, basket.getProducts(), date,"", String.valueOf(price),"pending",userID);

        ((HomeActivity)context).onClickCheckOut(o, result -> {
            if(result){
                next.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
            else{
                next.setVisibility(View.VISIBLE);
            }
        });
    }
    public void createSecurityCodePopWindow(View view, LayoutInflater inflater){
        View popUpWindow = inflater.inflate(R.layout.pop_window_security_code, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popUpWindow, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextInputLayout lSecurityCode = popUpWindow.findViewById(R.id.lSecurityCode);
        TextInputEditText securityCode = popUpWindow.findViewById(R.id.securityCode);
        ImageButton exit = popUpWindow.findViewById(R.id.exit);
        Button submit = popUpWindow.findViewById(R.id.submit);

        securityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 3){
                    lSecurityCode.setError(null);
                }
                else{
                    lSecurityCode.setError("Security code invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(v1 -> {
            Card test = cardsList.get(currentCard);
            if(test.getSecurityCode().equals(securityCode.getText().toString())){
                popupWindow.dismiss();
                doTransaction(view, inflater);
            }
            else{
                Toast.makeText(context, "Invalid security code", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        exit.setOnClickListener(v1 -> {
            popupWindow.dismiss();
        });

        // dismiss the popup window when touched
        popUpWindow.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    public void createPopWindow(View view, LayoutInflater inflater){
        View popUpWindow = inflater.inflate(R.layout.pop_window_select_card, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popUpWindow, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        RadioGroup radioGroup = popUpWindow.findViewById(R.id.rg_cards);
        Button submit = popUpWindow.findViewById(R.id.submit);
        Button clear = popUpWindow.findViewById(R.id.clear);
        ImageButton exit =  popUpWindow.findViewById(R.id.exit);

        HashMap<String, Integer> radioCheck = new HashMap<>();

        // create radio buttons for the available cards
        if(cardsList.size()>0){
            for(int i=0; i<cardsList.size();i++){
                RadioButton rb = new RadioButton(popUpWindow.getContext());
                rb.setText("**** **** **** " + cardsList.get(i).getCardNumber().substring(cardsList.get(i).getCardNumber().length()-5,cardsList.get(i).getCardNumber().length()-1));
                rb.setId(i);
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        radioCheck.put(buttonView.getText().toString(), rb.getId());
                    }
                    else{
                        radioCheck.remove(buttonView.getText().toString());
                    }
                });
                // add to the LinearLayout
                radioGroup.addView(rb);
            }
        }

        submit.setOnClickListener(v1 -> {
            if(radioCheck.size() > 0){
                for(String key : radioCheck.keySet()){
                    int pos =  radioCheck.get(key);
                    RadioButton rb = popUpWindow.findViewById(radioCheck.get(key));
                    rb.setChecked(false);
                    setCard(key, pos);

                    popupWindow.dismiss();
                }
            }
        });

        clear.setOnClickListener(v1 -> {
            if(radioCheck.size() > 0){
                for(String key : radioCheck.keySet()){
                    RadioButton rb = popUpWindow.findViewById(radioCheck.get(key));
                    rb.setChecked(false);
                }
            }
        });

        exit.setOnClickListener(v1 -> {
            popupWindow.dismiss();
        });

        // dismiss the popup window when touched
        popUpWindow.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
}