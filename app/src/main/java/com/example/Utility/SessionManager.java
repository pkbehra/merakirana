package com.example.Utility;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.Model.CartRVModel;
import com.example.Model.WishlistRVModel;
import com.example.merakirana.HomeActivity;
import com.example.merakirana.LoginActivity;
import com.example.merakirana.WelcomeActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 30-10-2017.
 */

public class SessionManager {

    private static final String ADD_TO_CART = "AddToCart";
    private static final String ADD_TO_WISHLIST = "AddToWishlist";

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context mContext;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LearnEarnPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //FCM Token
    public static final String KEY_FCM_TOKEN = "token";

    // ID (make variable public to access from outside)
    public static final String KEY_ID = "uid";

    //Referrer ID
    public static final String REFERRER_ID = "referrer_id";


    //address session
    public static final String KEY_PIN_CODE = "pin-code";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_AREA = "area";
    public static final String IS_ADDRESS = "is_address";


    // Constructor
    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //manage login user data
    public void createLoginSession(String id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing ID in pref
        editor.putString(KEY_ID, id);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user ID
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {

            // user is logged in redirect him to Main Activity
            Intent i = new Intent(mContext, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            mContext.startActivity(i);

        } else {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(mContext, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            mContext.startActivity(i);
        }

    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN, false);
        editor.remove(KEY_ID);
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(mContext, WelcomeActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
    }

    public void storeRegIdInPref(String token) {
        editor.putString(KEY_FCM_TOKEN, token);
        editor.commit();
    }

    //manage wishlist session data
    private void saveToWishlist(Context context, List<WishlistRVModel> favorites) {

        Gson gson = new Gson();

        String jsonFavorites = gson.toJson(favorites);

        editor.putString(ADD_TO_WISHLIST, jsonFavorites);

        editor.commit();
    }

    public void addToWishlist(Context context,WishlistRVModel wishlistProductList){

        boolean isValid = false;

        ArrayList<WishlistRVModel> wishlistProductLists = getAddToWishlist(context);
        if (wishlistProductLists == null)
            wishlistProductLists = new ArrayList<WishlistRVModel>();

        for (int i = 0; i < wishlistProductLists.size(); i++) {

            WishlistRVModel rvModel = wishlistProductLists.get(i);


            if (rvModel.getProductId()!=null && rvModel.getProductId().equals(wishlistProductList.getProductId())){
                // Toast.makeText(context,"Already added in wishlist",Toast.LENGTH_LONG).show();
                isValid=true;
            }
            else {
                // Toast.makeText(context, "Added in wishlist", Toast.LENGTH_SHORT).show();
            }
        }

        if (!isValid) {
            wishlistProductLists.add(wishlistProductList);
            saveToWishlist(context, wishlistProductLists);
            Toast.makeText(context, "Added in wishlist", Toast.LENGTH_SHORT).show();
        }
    }

   /* public void removeProductFromWishlist(Context context, int position) {
        ArrayList<WishlistRVModel> wishlistProductLists = getAddToWishlist(context);

        if (wishlistProductLists != null) {
            wishlistProductLists.remove(position);
            saveToWishlist(context, wishlistProductLists);


        }
    }*/

    public void removeProductFromWishlist(Context context,String id) {
        ArrayList<WishlistRVModel> wishlistProductLists = getAddToWishlist(context);

        for(int i=0;i<wishlistProductLists.size();i++){

            if(wishlistProductLists.get(i).getProductId().equalsIgnoreCase(id)){
                if (wishlistProductLists != null) {
                    wishlistProductLists.remove(i);
                    saveToWishlist(context, wishlistProductLists);
                }
            }
        }
    }

    public ArrayList<WishlistRVModel> getAddToWishlist(Context context) {
        List<WishlistRVModel> wishModelList;

        if (pref.contains(ADD_TO_WISHLIST)) {
            String jsonAddToCart = pref.getString(ADD_TO_WISHLIST, null);
            Gson gson = new Gson();
            WishlistRVModel[] wishlistprodarray = gson.fromJson(jsonAddToCart, WishlistRVModel[].class);

            wishModelList = Arrays.asList(wishlistprodarray);
            wishModelList = new ArrayList<WishlistRVModel>(wishModelList);
        } else
            return null;

        return (ArrayList<WishlistRVModel>) wishModelList;
    }

    //manage cart session data
    public void addToCart(Context context,CartRVModel cartRVModel){
        boolean isValid = false;
        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);
        if (cartProdArray == null)
            cartProdArray = new ArrayList<CartRVModel>();

        for (int i = 0; i < cartProdArray.size(); i++) {
            CartRVModel rvModel = cartProdArray.get(i);

            //  if (rvModel.getId()!=null && rvModel.getId().equals(cartRVModel.getId())){
            if (rvModel.getProductId().equals(cartRVModel.getProductId())){
                Toast.makeText(context,"Already added to cart",Toast.LENGTH_LONG).show();
                isValid=true;
            }
            /*else {
               // Toast.makeText(context, "Added in cart", Toast.LENGTH_SHORT).show();
            }*/
        }

        if (!isValid) {
            cartProdArray.add(cartRVModel);
            saveAddToCart(context, cartProdArray);
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAddToCart(Context context, List<CartRVModel> favorites) {
        Gson gson = new Gson();

        String jsonFavorites = gson.toJson(favorites);

        editor.putString(ADD_TO_CART, jsonFavorites);

        editor.commit();
    }

    public void removeProductFromCart(Context context, int position) {
        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);

        if (cartProdArray != null) {
            cartProdArray.remove(position);
            saveAddToCart(context, cartProdArray);
        }
    }

    public void updateCart(Context context, int value, CartRVModel cartProductsArray) {
        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);

        for (int i = 0; i < cartProdArray.size(); i++) {

            CartRVModel cardModel = cartProdArray.get(i);
            if (value == 1) {
                if (cardModel.getProductId() != null && cardModel.getProductId().equals(cartProductsArray.getProductId())) {
                    int val = cardModel.getProdQty();
                    //  Log.v("qty", String.valueOf(val));
                    //  Toast.makeText(context,String.valueOf(val),Toast.LENGTH_SHORT).show();
                    // Log.v("qtynew", String.valueOf(Integer.parseInt(val) + 1));
                    cardModel.setProdQty(val+ 1);
                }
            } else {
                if (cardModel.getProductId() != null && cardModel.getProductId().equals(cartProductsArray.getProductId())) {
                    int val = cardModel.getProdQty();
                    //Log.v("qty", String.valueOf(val));
                    //Toast.makeText(context,String.valueOf(val),Toast.LENGTH_SHORT).show();
                    // Log.v("qty", val);
                    // Log.v("qtynew", String.valueOf(Integer.parseInt(val) - 1));
                    cardModel.setProdQty(val - 1);
                }
            }
        }
        saveAddToCart(context, cartProdArray);
    }

    public ArrayList<CartRVModel> getAddToCartList(Context context) {
        List<CartRVModel> cartModelList;

        if (pref.contains(ADD_TO_CART)) {
            String jsonAddToCart = pref.getString(ADD_TO_CART, null);
            Gson gson = new Gson();
            CartRVModel[] cartProdArray = gson.fromJson(jsonAddToCart, CartRVModel[].class);

            cartModelList = Arrays.asList(cartProdArray);
            cartModelList = new ArrayList<CartRVModel>(cartModelList);
        } else
            return null;

        return (ArrayList<CartRVModel>) cartModelList;
    }

    public void deleteCartList() {
        pref.edit().remove(ADD_TO_CART).commit();
    }


    //get Subtotal amount
    public Integer getSubTotal(Context context) {
        int subtotal = 0;
        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);

        if (cartProdArray != null) {
            for (int i = 0; i < cartProdArray.size(); i++) {
                CartRVModel cardModel = cartProdArray.get(i);
                subtotal +=(cardModel.getProdQty()) * Integer.parseInt(cardModel.getPrice());
            }
        }
        return subtotal;
    }

    //delete cart list after making giving order

    public void deleteWishList() {
        pref.edit().remove(ADD_TO_WISHLIST).commit();
    }

    //get Wishlist product list

    public HashMap<String, String> getRegIdPref() {
        HashMap<String, String> token = new HashMap<String, String>();

        // token
        token.put(KEY_FCM_TOKEN, pref.getString(KEY_FCM_TOKEN, null));

        return token;
    }

    /*To store referrer id*/
    public void storeReferrerIdInPref(String refId) {
        editor.putString(REFERRER_ID, refId);
        editor.commit();
    }

    /*To get referrer id while registering new user*/
    public HashMap<String, String> getReferIdPref() {
        HashMap<String, String> referrId = new HashMap<String, String>();

        // referrId
        referrId.put(REFERRER_ID, pref.getString(REFERRER_ID, ""));

        return referrId;
    }

    public void saveFeaturesArray(String[] filterlis) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filterlis.length; i++) {
            sb.append(filterlis[i]).append(",");
        }

        editor.putString("FEATURESARRAY", sb.toString());

        editor.commit();
    }

    public String getFeaturesArray() {
        return pref.getString("FEATURESARRAY", "Login");
    }




    public void setBusinessName(String id) {
        // Storing login value as TRUE


        // Storing ID in pref
        editor.putString("BUSINESSNAME", id);

        // commit changes
        editor.commit();
    }

    public String getBusinessName() {
        // Storing login value as TRUE
        return pref.getString("BUSINESSNAME", "");

    }

    public void setFeatureLogo(String id) {
        // Storing login value as TRU
        // Storing ID in pref
        editor.putString("MYLOGO", id);
        // commit changes
        editor.commit();
    }

    public String getFeatureLogo() {
        // Storing login value as TRUE
        return pref.getString("MYLOGO", "");

    }

    public void setCurrentDate(String currentDate) {
        // Storing login value as TRU
        // Storing ID in pref
        editor.putString("CURRENTDATE", currentDate);
        // commit changes
        editor.commit();
    }

    public String getCurrentDate() {
        // Storing login value as TRUE
        return pref.getString("CURRENTDATE", "");

    }
    //user address
    public void saveDeliveryAddress(String address,String city,String area,String pincode){
        editor.putBoolean(IS_ADDRESS, true);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_AREA, area);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PIN_CODE, pincode);

        editor.commit();

    }

    public HashMap<String, String> getAddressDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user ID
        user.put(KEY_PIN_CODE, pref.getString(KEY_PIN_CODE, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_AREA, pref.getString(KEY_AREA, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        return user;
    }

    public boolean isAddressSaved(){
        return pref.getBoolean(IS_ADDRESS, false);
    }

    //Save FCM  token
    public void onSetToken(String token) {
        // SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public  String onGetToken() {
        return pref.getString("token", "");

    }


   /* public void onSetCartStatus(Boolean cartStatus) {
        // SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("CARTSTATUS", cartStatus);
        editor.commit();
    }

    public  Boolean onGetCartSatus() {
        return pref.getBoolean("CARTSTATUS", false);

    }*/
}
