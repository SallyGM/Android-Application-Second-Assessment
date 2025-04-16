package com.example.assessment2;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class HomeActivity extends AppCompatActivity {
    FragmentTopMenu fragmentTopMenu;
    FragmentManager fm;
    FirebaseAuth fbAuth;
    FirebaseUser currentUser;
    FragmentBottomMenu fragmentBottomMenu;
    DBHelper helper;
    Basket basket = new Basket();
    HashMap<String, Integer> basketGuest = new HashMap<>();
    HashMap<String, Integer> basketUser = new HashMap<>();
    private User guestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fm = this.getSupportFragmentManager();
        helper = new DBHelper(this);

        fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
        fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

        fbAuth = FirebaseAuth.getInstance();
        currentUser = fbAuth.getCurrentUser();

        // user should not access this page without login
        if(currentUser == null) {
            changeFragment("", "Login");
        }
        else{
            changeFragment("", "Home");
            loadUserInfo();
            getBasket();
        }
    }

    public void reset(){
        currentUser = fbAuth.getCurrentUser();
        basket = new Basket();
        basketGuest = new HashMap<>();
        basketUser = new HashMap<>();

        fragmentBottomMenu.total.setText("0");
        fragmentTopMenu.tvUsername.setText("Welcome!\n");

        if(currentUser != null){
            changeFragment("", "Home");
            loadUserInfo();
            getBasket();
        }
    }

    public void getBasket(){
        helper.getBasket((currentUser.getUid()), baskets -> {
            if(baskets.size() > 0){
                if(baskets.get(0).getProducts().size()>0){
                    basket = baskets.get(0);
                    basketUser = basket.getProducts();
                    fragmentBottomMenu.total.setText(String.valueOf(basketSize()));
                }
            }
        });
    }

    public void loadUserInfo(){
        DBHelper helper = new DBHelper(this);
        helper.getUser((currentUser.getUid()), user -> {
            fragmentTopMenu.tvUsername.setText("Welcome!\n" + user.getLastName() + ", " + user.getName());
        });
    }

    public int basketSize(){
        int size = 0;
        if(currentUser!=null) {
            if(basketUser.size() == 0){
                return size;
            }

            for(String key : basketUser.keySet()){
                size += Integer.parseInt(String.valueOf(basketUser.get(key)));
            }
        }
        else{
            if(basketGuest.size() == 0){
                return size;
            }

            for(String key : basketGuest.keySet()){
                size += basketGuest.get(key);
            }
        }
        return size;
    }

    public void basketActions(String productID, int quantity, String action){
        // if the item is already on the basket
        fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

        if(currentUser!=null){
            boolean addOnTop = basket.contains(productID);

            // remove product from the basket
            if(quantity == 0){
                helper.removeProductFromBasket(currentUser.getUid(), productID, result -> {
                    changeFragment(currentUser.getUid(), "Basket");
                    basketUser.remove(productID);
                    fragmentBottomMenu.total.setText(String.valueOf(basketSize()));
                });
            }
            // clear basket
            else if(productID.equals("") && quantity == 0){
                helper.clearBasket(currentUser.getUid(), result -> {
                    changeFragment(currentUser.getUid(), "Basket");
                    basketUser.clear();
                    fragmentBottomMenu.total.setText("0");
                });
            }
            // add to basket
            else{
                helper.addProductToBasket(currentUser.getUid(), productID, action, quantity);

                if(addOnTop && action.equals("add")){
                    basketUser.replace(productID, Integer.parseInt(String.valueOf(basket.getProducts().get(productID))) + quantity);
                }
                else{
                    basketUser.put(productID, quantity);
                }

                fragmentBottomMenu.total.setText(String.valueOf(basketSize()));
            }
        }
        else{
            boolean addOnTop = basketGuest.containsKey(productID);

            if(quantity == 0){
                basketGuest.remove(productID);
            }
            else if(productID.equals("") && quantity == 0){
                basketGuest.clear();
            }
            else{
                if(addOnTop){
                    basketGuest.replace(productID,basketGuest.get(productID) + quantity);
                }
                else{
                    basketGuest.put(productID, quantity);
                }
            }

            fragmentBottomMenu.total.setText(basketGuest.size());
            Toast.makeText(this, "Basket updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickToCheckOut(ArrayList<Product> products, Basket basket){

        Bundle bundle = new Bundle();
        bundle.putString("UserID", currentUser.getUid());
        bundle.putParcelable("Basket", basket);
        bundle.putParcelableArrayList("Products", products);

        fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
        fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

        fm.beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.main_container, FragmentCheckOut.class, bundle, "home_container")
                .commit();
    }

    public void onClickFavourites(String productID, OnDataReceivedCallBackFavourites callBack){
        helper.addProductToFavourite(currentUser.getUid(), productID, callBack::onDataReceived);
    }

    public void onClickSaveUserInformation(User user) {
        helper.updateUser(user);
    }

    public void onClickCheckOut(Order order, OnDataReceivedCallBack callBack) {
        HashMap<String, Integer> items = order.getItems();

        for(String key : items.keySet()){
            helper.changeProductQuantity(key, Integer.parseInt(String.valueOf(order.getItems().get(key))));
        }

        helper.placeAnOrder(currentUser.getUid(), order, result -> {
            if (result) {
                helper.clearBasket(currentUser.getUid(), result1 -> {
                    fragmentBottomMenu.total.setText("0");
                    callBack.onDataReceived(result1);
                });
            }
            else{
                callBack.onDataReceived(false);
            }
        });
    }

    public void cardManagement(String action, User u,  ArrayList<Card> c, OnDataReceivedCallBack callBack){
        if(action.equals("Remove_Card")){
            helper.removeACard(currentUser.getUid(),c.get(0), result -> {
                callBack.onDataReceived(result);
            });
        }
        else if(action.equals("Add_Cards")){
            helper.addCards(u, c, result -> {
                callBack.onDataReceived(result);
            });
        }
    }

    public void onClickChangeUserFragment(User u, String action){
        Bundle bundle = new Bundle();
        bundle.putParcelable("User", u);
        if(action.equals("Account_Card_Management")){

            fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
            fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentCardManagement.class, bundle, "home_container")
                    .commit();
        }
        else if(action.equals("Add_Card")){

            fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
            fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentAddCard.class, bundle, "home_container")
                    .commit();
        }
        else if(action.equals("Change_Password")){

            fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
            fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentChangePassword.class, bundle, "home_container")
                    .commit();
        }
    }

    public void createUser(User nu, ArrayList<Card> cards, int part){

        if(part == 1){
            guestUser = nu;
        }
        if(part == 2){
            DBHelper helper = new DBHelper(this);
            guestUser.setCard(cards);
            helper.addUser(guestUser, result -> {
                currentUser = fbAuth.getCurrentUser();

                if(basketGuest.size() > 0){
                    helper.creatingBasketForNewUser(currentUser.getUid(), basketGuest);
                }

                changeFragment("", "Home");

                guestUser = null;
            });


        }
    }

    //change fragment
    public void changeFragment(String id, String action) {
        Bundle bundle = new Bundle();

        if(action.equals("Register")){
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentCreateNewAccount.class, null, "main_container")
                    .commit();
        }
        else if(action.equals("Login")){
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentLogin.class, null, "main_container")
                    .commit();
        }
        else if(action.equals("Card_Details")){
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentAddCard.class, null, "main_container")
                    .commit();
        }
        else if(action.equals("Validate Account")){

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentCreateNewAccount.class, null, "main_container")
                    .commit();
        }
        else if(action.equals("Forgot_Password")){

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentForgotPassword.class, null, "main_container")
                    .commit();
        }
        else if(action.equals("View")){
            bundle.putString("productID", id);

            fragmentTopMenu = (FragmentTopMenu) fm.findFragmentById(R.id.top_menu_container);
            fragmentBottomMenu = (FragmentBottomMenu) fm.findFragmentById(R.id.bottom_menu_container);
            Fragment home_container = fm.findFragmentById(R.id.main_container);

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.view_product_container, FragmentViewProduct.class, bundle, "view_product_container")
                    .hide(fragmentTopMenu)
                    .hide(fragmentBottomMenu)
                    .hide(home_container)
                    .commit();

        }
        if(action.equals("Search")){

            if(currentUser != null){
                bundle.putString("UserID", currentUser.getUid());
            }
            else{
                bundle.putString("UserID", "");
            }

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentProduct.class, bundle, "main_container")
                    .commit();
        }
        if(action.equals("Home")){
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentHome.class, null, "main_container")
                    .commit();

        }
        else if(action.equals("Account")){
            if(currentUser == null){
                changeFragment("", "Login");
            }
            else{
                bundle.putString("UserID", currentUser.getUid());
                bundle.putString("UserEmail", currentUser.getEmail());

                fm.beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_container, FragmentAccount.class, bundle, "main_container")
                        .commit();
            }

        }
        else if(action.equals("Favourites")){
            if(currentUser == null){
                Toast.makeText(this, "You need to log in to access this functionality!", Toast.LENGTH_SHORT).show();
            }
            else{
                bundle.putString("UserID", currentUser.getUid());

                fm.beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_container, FragmentFavourites.class, bundle, "main_container")
                        .commit();
            }
        }
        else if(action.equals("Orders")){
            if(currentUser == null){
                Toast.makeText(this, "You need to log in to see your orders!", Toast.LENGTH_SHORT).show();
            }
            else{
                bundle.putString("UserID", currentUser.getUid());

                fm.beginTransaction()
                        .setReorderingAllowed(true)
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_container, FragmentOrders.class, bundle, "main_container")
                        .commit();
            }
        }
        else if(action.equals("Basket")){
            if(currentUser != null){
                bundle.putString("UserID", currentUser.getUid());
                bundle.putParcelable("Basket", basket);
            }
            else{
                bundle.putString("UserID", "");
                bundle.putSerializable("BasketGuest", basketGuest);
            }

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_container, FragmentBasket.class, bundle, "main_container")
                    .commit();

        }
        else if(action.equals("GoBack")){
            if(currentUser != null){
                bundle.putString("UserID", currentUser.getUid());
            }
            else{
                bundle.putString("UserID", "");
            }

            Fragment viewProduct = fm.findFragmentByTag("view_product_container");
            Fragment main_fragment = fm.findFragmentByTag("main_container");
            Fragment top_fragment = fm.findFragmentByTag("top_menu_container");
            Fragment bottom_fragment = fm.findFragmentByTag("bottom_menu_container");

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .remove(viewProduct)
                    .add(R.id.bottom_menu_container, bottom_fragment.getClass(), bundle, "bottom_menu_container")
                    .add(R.id.top_menu_container, top_fragment.getClass(), bundle, "top_menu_container")
                    .add(R.id.main_container, main_fragment.getClass(), bundle, "main_container")
                    .commit();


        }
    }

}
