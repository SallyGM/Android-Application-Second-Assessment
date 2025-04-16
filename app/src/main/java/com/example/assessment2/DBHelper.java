package com.example.assessment2;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.compose.ui.node.ObserverNodeKt;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DBHelper {

    private DatabaseReference db;
    private FirebaseAuth auth;
    private Context context;

    public DBHelper(Context context){
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        this.context = context;
    }

    //region User
    public void signIn(String email, String password, OnDataReceivedCallBack callBack) {
        try{
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Log in successful", Toast.LENGTH_SHORT).show();
                            callBack.onDataReceived(true);

                        } else {
                            Toast.makeText(context, "Error on log in", Toast.LENGTH_SHORT).show();
                            callBack.onDataReceived(false);
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(context, "Error on sign in", Toast.LENGTH_SHORT).show();
            callBack.onDataReceived(false);
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void recovery(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "E-mail sent!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void addUser(User u, OnDataReceivedCallBack callBack) {

        try{
            // removing the password, email --> as it is in authentication
            User newUser = new User(u.getName(), u.getMiddleName(), u.getLastName(), u.getPhone(), u.getDateOfBirth(), u.getCard(), u.getGender());
            // creating authentication
            auth.createUserWithEmailAndPassword(u.getEmail(), u.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUser fbNewUser = auth.getCurrentUser();

                            // add information to the node
                            db.child("User").child(fbNewUser.getUid()).setValue(newUser).addOnCompleteListener(task3 -> {
                                if(!task3.isSuccessful()){
                                    Toast.makeText(context, "Error creating account", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(false);
                                }
                                else{
                                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(true);
                                }
                            });
                        } else {
                            Toast.makeText(context, "Error creating the user", Toast.LENGTH_SHORT).show();
                            callBack.onDataReceived(false);
                        }
                    });

        }
        catch (Exception e){
            Toast.makeText(context, "Error creating the user", Toast.LENGTH_SHORT).show();
            callBack.onDataReceived(false);
        }
    }

    public void updateUserPassword(String newPassword, String oldPassword, OnDataReceivedCallBack callBack){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if(!task1.isSuccessful()){
                       Toast.makeText(context, "Password updated!", Toast.LENGTH_SHORT).show();
                       callBack.onDataReceived(true);
                    }else {
                        Toast.makeText(context, "Error updating password", Toast.LENGTH_SHORT).show();
                        callBack.onDataReceived(false);
                    }
                });
            }else {
                Toast.makeText(context, "Error updating password", Toast.LENGTH_SHORT).show();
                callBack.onDataReceived(false);
            }
        });
    }

    public void updateUser(User u){

        try{

            Map<String, Object> data = new HashMap<>();

            data.put("name", u.getName());
            data.put("middleName", u.getMiddleName());
            data.put("lastName", u.getLastName());
            data.put("email", u.getEmail());
            data.put("phone", u.getPhone());
            data.put("dateOfBirth", u.getDateOfBirth());
            data.put("card", u.getCard());
            data.put("gender", u.getGender());

            final Task<Void> r = FirebaseDatabase.getInstance().getReference().child("User").child(u.getId()).updateChildren(data)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(context, "User information updated!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Error could not update list", Toast.LENGTH_SHORT).show();
                    }

            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not update user information", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUser(String id, OnDataReceivedCallBackUsers callBack){

        try{
            ArrayList<User> user = new ArrayList<>();

            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("User").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    Map<String, Object> data = (HashMap<String, Object>) snapshot.getValue();

                    if(snapshot.getValue() != null){
                        ArrayList<HashMap<String,Object>> card = (ArrayList<HashMap<String, Object>>) data.get("card");

                        ArrayList<Card> cardList = new ArrayList<>();

                        // trying to fix card type being cast into a hashmap
                        for(int i=0; i<card.size();i++){
                            HashMap<String, Object> cardHashMap = (HashMap<String, Object>) card.get(i);
                            Card c = new Card(cardHashMap);

                            cardList.add(c);
                        }

                        User u = new User(snapshot.getKey(), data.get("name").toString(), data.get("middleName").toString(), data.get("lastName").toString(),
                                data.get("phone").toString(), data.get("dateOfBirth").toString(), cardList, data.get("gender").toString());

                        user.add(u);

                        callBack.onDataReceived(user.get(0));
                    }
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "An error has occurred, user data could not be retrieved", Toast.LENGTH_SHORT).show();
        }
    }

    //region Cards
    public void addCards(User user, ArrayList<Card> cards, OnDataReceivedCallBack callBack){

        try{

            final Task<DataSnapshot> r = FirebaseDatabase.getInstance().getReference().child("User").child(user.getId()).child("card").get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    Map<String, Object> newData = new HashMap<>();

                    String lastkey= "";
                    for(DataSnapshot ss : snapshot.getChildren()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) ss.getValue();
                        lastkey = ss.getKey();
                        newData.put(ss.getKey(), data);
                    }

                    for(int i=0; i<cards.size();i++){
                        Map<String, Object> newCard = new HashMap<>();
                        newCard.put("cardNumber", cards.get(i).getCardNumber());
                        newCard.put("fullName", cards.get(i).getFullName());
                        newCard.put("accountNumber", cards.get(i).getAccountNumber());
                        newCard.put("sortCode", cards.get(i).getSortCode());
                        newCard.put("expDate", cards.get(i).getExpDate());
                        newCard.put("securityCode", cards.get(i).getSecurityCode());
                        newCard.put("billAddress", cards.get(i).getBillAddress());

                        int newPosition = Integer.parseInt(lastkey) + 1;
                        lastkey = String.valueOf(newPosition);
                        newData.put(lastkey, newCard);
                    }

                    snapshot.getRef().updateChildren(newData).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(context, "Cards updated!", Toast.LENGTH_SHORT).show();
                            callBack.onDataReceived(true);
                        }
                        else{
                            Toast.makeText(context, "Could not update cards", Toast.LENGTH_SHORT).show();
                            callBack.onDataReceived(false);
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                    callBack.onDataReceived(false);
                }
            });

        }
        catch (Exception e){
            Toast.makeText(context, "An error has occurred, user data could not be retrieved", Toast.LENGTH_SHORT).show();
            callBack.onDataReceived(false);
        }
    }

    public void removeACard(String userId, Card c, OnDataReceivedCallBack callBack){

        try{
            final Task<DataSnapshot> r = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("card").get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    DataSnapshot snapshot = task.getResult();

                    for(DataSnapshot ss : snapshot.getChildren()){

                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();
                        if(data.get("cardNumber").toString().equals(c.getCardNumber())){
                            snapshot.getRef().child(ss.getKey()).removeValue().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Toast.makeText(context, "Cards updated", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(true);
                                }
                                else{
                                    Toast.makeText(context, "Could not remove card", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(false);
                                }
                            });
                        }
                    }

                    if(snapshot.getValue() != null){

                    }

                    Toast.makeText(context, "Cards updated!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(context, "An error has occurred, user data could not be retrieved", Toast.LENGTH_SHORT).show();
        }

    }

    public void getCards(String id, OnDataReceivedCallBackCards callBack){

        try{
            ArrayList<Card> card = new ArrayList<>();

            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("User").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    Map<String, Object> data = (HashMap<String, Object>) snapshot.getValue();

                    ArrayList<Card> cards = (ArrayList<Card>) data.get("card");

                    for(int i=0; i<cards.size();i++){
                        Object obj = cards.get(i);
                        HashMap<String, Object> crd = (HashMap<String, Object>) obj;
                        Card c = new Card(crd);

                        card.add(c);
                    }

                    callBack.onDataReceived(card);
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(context, "An error has occurred, user data could not be retrieved", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion Cards
    //endregion User

    //region Product retrieval related code
    public void getProduct(String id, OnDataReceivedCallBackProducts callBack){

        ArrayList<Product> products = new ArrayList<>();

        final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Product").child(id);
        r.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DataSnapshot snapshot = task.getResult();

                Map<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                ArrayList<String> details = (ArrayList<String>) data.get("details");
                ArrayList<String> images = (ArrayList<String>) data.get("images");

                Product p = new Product(snapshot.getKey(),data.get("name").toString(), data.get("price").toString(), details, data.get("dimensions").toString(),data.get("type").toString(),
                        data.get("quantity").toString(),data.get("ageRestriction").toString(),data.get("gameID").toString(), data.get("categoryID").toString(), images);

                products.add(p);

                callBack.onDataReceived(products);
            }
            else{
                Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProducts(OnDataReceivedCallBackProducts callBack){

        ArrayList<Product> products = new ArrayList<>();

        try{
            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Product");
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    for(DataSnapshot ss : snapshot.getChildren()){

                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();
                        ArrayList<String> details = (ArrayList<String>) data.get("details");
                        ArrayList<String> images = (ArrayList<String>) data.get("images");

                        Product p = new Product(ss.getKey(),data.get("name").toString(), data.get("price").toString(), details, data.get("dimensions").toString(),data.get("type").toString(),
                                data.get("quantity").toString(),data.get("ageRestriction").toString(),data.get("gameID").toString(), data.get("categoryID").toString(), images);

                        products.add(p);

                    }

                    callBack.onDataReceived(products);
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLastProducts(OnDataReceivedCallBackProducts callBack){

        ArrayList<Product> products = new ArrayList<>();

        try{
            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Product");
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    for(DataSnapshot ss : snapshot.getChildren()){

                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();
                        ArrayList<String> details = (ArrayList<String>) data.get("details");
                        ArrayList<String> images = (ArrayList<String>) data.get("images");

                        String[] d = data.get("dateAdded").toString().split("-");

                        LocalDate date = LocalDate.of(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));

                        Product p = new Product(ss.getKey(),data.get("name").toString(), data.get("price").toString(), details, data.get("dimensions").toString(),data.get("type").toString(),
                                data.get("quantity").toString(),data.get("ageRestriction").toString(),data.get("gameID").toString(), data.get("categoryID").toString(), images, date);

                        products.add(p);

                    }

                    // Comparing in descending order
                    Comparator<Product> comparator = (p1, p2) -> p2.getDateAdded().compareTo(p1.getDateAdded());
                    Collections.sort(products, comparator);

                    callBack.onDataReceived(new ArrayList<>(products.subList(0,7)));
                }
                else{
                    Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSpecificProducts(ArrayList<String> productIDs,OnDataReceivedCallBackProducts callBack){

        ArrayList<Product> products = new ArrayList<>();

        final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Product");
        r.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DataSnapshot snapshot = task.getResult();
                for(DataSnapshot ss : snapshot.getChildren()){

                    if(productIDs.contains(ss.getKey())){
                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();
                        ArrayList<String> details = (ArrayList<String>) data.get("details");
                        ArrayList<String> images = (ArrayList<String>) data.get("images");

                        Product p = new Product(ss.getKey(),data.get("name").toString(), data.get("price").toString(), details, data.get("dimensions").toString(),data.get("type").toString(),
                                data.get("quantity").toString(),data.get("ageRestriction").toString(),data.get("gameID").toString(), data.get("categoryID").toString(), images);

                        products.add(p);
                    }
                }

                callBack.onDataReceived(products);
            }
            else{
                Toast.makeText(context, "Could not retrieve the list", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region Basket
    public void getBasket(String id, OnDataReceivedCallBackBasket callback){
        try{
            ArrayList<Basket> baskets = new ArrayList<>();

            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Basket").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    if(snapshot.getValue() != null){
                        Map<String, Object> data = (HashMap<String, Object>) snapshot.getValue();

                        Map<String, Integer> item = (HashMap<String, Integer>) data.get("items");

                        Basket b = new Basket(snapshot.getKey(), (HashMap<String, Integer>) item);
                        baskets.add(b);

                        callback.onDataReceived(baskets);
                    }
                    else{
                        callback.onDataReceived(baskets);
                    }
                }
                else{
                    Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeProductFromBasket(String id, String productId, OnDataReceivedCallBack callBack){

        try{
            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Basket").child(id).child("items");
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // in case basket is empty already
                    if(snapshot.getValue() != null){
                        snapshot.getRef().child(productId).removeValue().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Basket updated!", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(true);
                            }
                            else{
                                Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(false);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void addProductToBasket(String id, String productId, String action, int quantity){

        try{
            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Basket").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // in case there is no node for the data
                    if(snapshot.getValue() == null){
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> item = new HashMap<>();

                        item.put(productId, quantity);

                        data.put(id,item);

                        snapshot.getRef().child("items").updateChildren(item).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Basket updated!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Map<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                        Map<String, Object> item = (HashMap<String, Object>) data.get("items");

                        if(action.equals("change")){
                            if(item.containsKey(productId)){
                                item.replace(productId, quantity);
                            }
                        }
                        else if(!item.containsKey(productId)) {
                            item.put(productId, quantity);
                        }
                        else if(action.equals("add")) {
                            for(String key : item.keySet()){
                                if(productId.equals(key)){
                                    item.replace(key, Integer.parseInt(item.get(key).toString()) + quantity);
                                }
                            }
                        }

                        // add amount on top of old value

                        snapshot.getRef().child("items").updateChildren(item).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Basket updated!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void creatingBasketForNewUser(String id, HashMap<String, Integer> basket){

        try{
            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Basket").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // in case there is no node for the data
                    if(snapshot.getValue() == null){
                        Map<String, Object> data = new HashMap<>(basket);
                        Map<String, Object> item = new HashMap<>();

                        data.put(id,item);

                        snapshot.getRef().child("items").updateChildren(item).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Basket updated!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearBasket(String id, OnDataReceivedCallBack callBack){
        try{
            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Basket").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // if it finds values under the node
                    if(snapshot.getValue() != null){
                        snapshot.getRef().child("items").removeValue().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Basket cleared!", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(true);
                            }
                            else{
                                Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(true);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    public void changeProductQuantity(String productId, int quantityDecrease){
        final DatabaseReference r1 = FirebaseDatabase.getInstance().getReference().child("Product").child(productId);
        r1.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DataSnapshot snapshot = task.getResult();
                Map<String, Object> product_data = (HashMap<String, Object>) snapshot.getValue();
                ArrayList<String> details = (ArrayList<String>) product_data.get("details");
                ArrayList<String> images = (ArrayList<String>) product_data.get("images");

                Product p = new Product(snapshot.getKey(),product_data.get("name").toString(), product_data.get("price").toString(), details, product_data.get("dimensions").toString(),product_data.get("type").toString(),
                        product_data.get("quantity").toString(),product_data.get("ageRestriction").toString(),product_data.get("gameID").toString(), product_data.get("categoryID").toString(), images);

                int quantity = Integer.parseInt(p.getQuantity());
                quantity = quantity - quantityDecrease;

                p.setQuantity(String.valueOf(quantity));

                snapshot.getRef().child("quantity").setValue(quantity).addOnCompleteListener(task2 -> {
                    if(!task2.isSuccessful()){
                        Toast.makeText(context, "Could not update product", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(context, "Could not find the product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOrders(String id, OnDataReceivedCallOrders callback){
        try{
            ArrayList<Order> orders = new ArrayList<>();

            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Order");
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    if(snapshot.getValue() != null){
                        for(DataSnapshot ss : snapshot.getChildren()) {

                            Map<String, Object> data = (HashMap<String, Object>) ss.getValue();
                            HashMap<String, Integer> item = (HashMap<String, Integer>) data.get("items");

                            if (data.get("userID").toString().equals(id)) {
                                Map<String, Object> address_map = (Map<String, Object>) data.get("address");

                                Address address = new Address((address_map.get("country") == null ? "": address_map.get("country").toString()),
                                        (address_map.get("county") == null ? "": address_map.get("county").toString()),
                                        (address_map.get("lineOne") == null ? "": address_map.get("lineOne").toString()),
                                        (address_map.get("lineTwo") == null ? "": address_map.get("lineTwo").toString()),
                                        (address_map.get("lineThree") == null ? "": address_map.get("lineThree").toString()),
                                        (address_map.get("postcode") == null ? "": address_map.get("postcode").toString()));

                                Order order = new Order(ss.getKey(), address, data.get("date").toString(), item, data.get("lastUpdate").toString(),
                                        data.get("notes").toString(), data.get("price").toString(), data.get("status").toString(), data.get("userID").toString());

                                orders.add(order);

                            }
                        }

                        callback.onDataReceived(orders);
                    }
                    else{
                        callback.onDataReceived(orders);
                    }
                }
                else{
                    Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void placeAnOrder(String id, Order order, OnDataReceivedCallBack callBack){

        try{
            final DatabaseReference ra =  FirebaseDatabase.getInstance().getReference().child("Order").push();

            ra.get().addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()){
                    DataSnapshot snapshot = task1.getResult();

                    // in case there is no node for the data
                    if(snapshot.getValue() == null){
                        Map<String, Object> node = new HashMap<>();
                        Map<String, Object> children = new HashMap<>();

                        children.put("address", order.getAddress());
                        children.put("date", order.getDate());
                        children.put("items", order.getItems());
                        children.put("lastUpdate", order.getLastUpdate());
                        children.put("notes", order.getNotes());
                        children.put("price", order.getPrice());
                        children.put("status", order.getStatus());
                        children.put("userID", order.getUserID());
                        node.put(snapshot.getKey(),children);

                        snapshot.getRef().setValue(children).addOnCompleteListener(task2 -> {
                            if(task2.isSuccessful()){
                                Toast.makeText(context, "Order placed", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(true);
                            }
                            else{
                                Toast.makeText(context, "Could not place order", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(false);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(context, "Could not place order", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    //region Favourite
    public void addProductToFavourite(String id, String productID, OnDataReceivedCallBackFavourites callBack){
        try{
            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Favourite").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // in case there is no node for the data
                    if(snapshot.getValue() == null){
                        Map<String, Object> data = new HashMap<>();

                        data.put(productID, 0);

                        snapshot.getRef().updateChildren(data).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "Product added to favourites", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(1);
                            }
                            else{
                                Toast.makeText(context, "Could not add to favourites", Toast.LENGTH_SHORT).show();
                                callBack.onDataReceived(0);
                            }
                        });
                    }
                    else{
                        HashMap<String,Object> data = (HashMap<String,Object>) snapshot.getValue();

                        // removes if it's there
                        if(data.containsKey(productID)){
                            snapshot.getRef().child(productID).removeValue().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Toast.makeText(context, "Favourites changed", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(2);
                                }
                                else{
                                    Toast.makeText(context, "Could not add to favourites", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(0);
                                }
                            });
                        }
                        // add if it's not there
                        else{
                            data.put(productID,"");
                            snapshot.getRef().updateChildren(data).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Toast.makeText(context, "Favourites changed", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(1);
                                }
                                else{
                                    Toast.makeText(context, "Could not add to favourites", Toast.LENGTH_SHORT).show();
                                    callBack.onDataReceived(0);
                                }
                            });
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Could not update basket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void retrieveFavourites(String id, OnDataReceivedCallBackArrayList callback){
        try{
            ArrayList<String> favourites = new ArrayList<>();

            final DatabaseReference r =  FirebaseDatabase.getInstance().getReference().child("Favourite").child(id);
            r.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    // in case there is no node for the data
                    if(snapshot.getValue() == null){
                        callback.onDataReceived(favourites);
                    }
                    else{
                        HashMap<String, Object> data = ( HashMap<String, Object>) snapshot.getValue();
                        for(String key : data.keySet()){
                            favourites.add(key);
                        }

                        callback.onDataReceived(favourites);
                    }
                }
                else{
                    Toast.makeText(context, "Could not retrieve favourites list", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve favourites list", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Category
    public void getCategories(OnDataReceivedCallBackCategories callBack){

        try {
            ArrayList<Category> categories = new ArrayList<>();

            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Category");
            r.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    for(DataSnapshot ss : snapshot.getChildren()){

                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();

                        Category c = new Category(ss.getKey(),data.get("name").toString());
                        categories.add(c);

                    }
                    callBack.onDataReceived(categories);

                }
                else{
                    Toast.makeText(context, "Could not retrieve information, please wait and try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve information, please wait and try again", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Games
    public void getGames(OnDataReceivedCallBackGames callBack){
        try {
            ArrayList<Game> games = new ArrayList<>();

            final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Game");
            r.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for(DataSnapshot ss : snapshot.getChildren()){

                        Map<String, Object> data = (HashMap<String, Object>) ss.getValue();

                        Game g = new Game(ss.getKey(),data.get("name").toString());

                        games.add(g);
                    }
                    callBack.onDataReceived(games);
                }
                else {
                    Toast.makeText(context, "Could not retrieve information, please wait and try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Could not retrieve information, please wait and try again", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion
}