package com.example.Adapter;


import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interface.AddRemoveCartListener;
import com.example.Interface.AddToCartListener;
import com.example.Interface.RemoveCart;
import com.example.Interface.UpdateItemPrice;
import com.example.Model.CartRVModel;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<CartRVModel> list;
    private Activity miContext;
    private SessionManager sessionManager;
    RemoveCart removeCart;
    AddToCartListener addToCartListener;
    AddRemoveCartListener addRemoveCartListener;

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;

    UpdateItemPrice updateItemPrice;


    public CartAdapter(Activity context, List<CartRVModel> cartProd) {
        this.list = cartProd;
        this.miContext = context;
        this.removeCart = (RemoveCart) context;
        this.addRemoveCartListener = (AddRemoveCartListener) context;

        sessionManager = new SessionManager(miContext);

        this.updateItemPrice = (UpdateItemPrice) miContext;

        newfeaturesList = sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);


    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items, viewGroup, false);
        return new MyViewHolder(item_view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.MyViewHolder myViewHolder, final int pos) {

        CartRVModel cartProductsArray = list.get(pos);

        myViewHolder.tvCartProductName.setText(cartProductsArray.getName());
        myViewHolder.tvCartProductPrice.setText("$ " + cartProductsArray.getPrice());
        myViewHolder.tvCartQuantity.setText(String.valueOf(cartProductsArray.getProdQty()));

        String img_url = miContext.getResources().getString(R.string.PROD_IMG_URL) + cartProductsArray.getLogo();

        Picasso.with(miContext)
                .load(img_url)
                .placeholder(R.drawable.loader)
                .fit().centerInside()
                .into(myViewHolder.ivCartProduct);


        myViewHolder.ivCartFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(miContext);
                if (wishlistProductLists != null && wishlistProductLists.size() != 0) {

                    for (int i = 0; i < wishlistProductLists.size(); i++) {

                        if (list.get(pos).getProductId().equalsIgnoreCase(wishlistProductLists.get(i).getProductId())) {

                            final int finalI = i;
                            new AlertDialog.Builder(miContext)
                                    .setTitle("Wishlist")
                                    .setMessage("Do you want to remove from Wishlist?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            sessionManager.removeProductFromWishlist(miContext, wishlistProductLists.get(finalI).getProductId());
                                            myViewHolder.ivCartFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(miContext, "Removed from  Wishlist", Toast.LENGTH_SHORT).show();
                                            // refreshHome.notifyHomeList();
                                            notifyDataSetChanged();
                                        }
                                    }).create().show();


                        } else {

                            // CosmeticArrivalModel model = list.get(pos);
                            CartRVModel model = list.get(pos);
                            myViewHolder.ivCartFav.setImageResource(R.drawable.ic_favorite_red_24dp);
                            WishlistRVModel wishlistProductList = new WishlistRVModel();
                            wishlistProductList.setPrice(model.getPrice());
                            wishlistProductList.setStatus("1");
                            wishlistProductList.setName(model.getName());
                            wishlistProductList.setId(model.getId());
                            wishlistProductList.setProductId(model.getProductId());
                            wishlistProductList.setLogo(model.getLogo());
                            wishlistProductList.setDescriptions(model.getDescriptions());
                            // wishlistProductList.setImagelogo(model.getSinglelogo());
                            // wishlistProductList.setUnit(model.getProdQty());
                            // wishlistProductList.setWeight(model.getWight());
                            sessionManager.addToWishlist(miContext, wishlistProductList);
                            // refreshHome.notifyHomeList();
                            notifyDataSetChanged();

                        }

                    }


                } else {
                    CartRVModel model = list.get(pos);
                    myViewHolder.ivCartFav.setImageResource(R.drawable.ic_favorite_red_24dp);
                    WishlistRVModel wishlistProductList = new WishlistRVModel();
                    wishlistProductList.setPrice(model.getPrice());
                    wishlistProductList.setStatus("1");
                    wishlistProductList.setDescriptions(model.getDescriptions());
                    wishlistProductList.setName(model.getName());
                    wishlistProductList.setId(model.getId());
                    wishlistProductList.setProductId(model.getProductId());
                    wishlistProductList.setLogo(model.getLogo());
                    //wishlistProductList.setImagelogo(model.getSinglelogo());
                    // wishlistProductList.setUnit(model.getProdQty());
                    // wishlistProductList.setWeight(model.getWight());
                    sessionManager.addToWishlist(miContext, wishlistProductList);
                    //refreshHome.notifyHomeList();
                    notifyDataSetChanged();
                }
            }
        });


        if (sessionManager.getAddToWishlist(miContext) != null) {
            ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(miContext);

            for (int k = 0; k < wishlistProductLists.size(); k++) {
                WishlistRVModel rvModel = wishlistProductLists.get(k);

                if (rvModel.getProductId() != null && rvModel.getProductId().equals(list.get(pos).getProductId())) {
                    myViewHolder.ivCartFav.setImageResource(R.drawable.ic_favorite_red_24dp);
                }
            }
        }

        if (listfeature.contains("Wishlist")) {
            myViewHolder.ivCartFav.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivCartFav.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCartProduct, ivCartFav;
        TextView tvCartProductName, tvCartProductPrice, tvCartQuantity, tvSubTotal;
        Button btnDecrease, btnIncrease, btnDeleteProd;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            ivCartProduct = itemView.findViewById(R.id.ivcartProd);
            tvCartProductName = itemView.findViewById(R.id.cartProdName);
            tvCartProductPrice = itemView.findViewById(R.id.cartProdPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnDeleteProd = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);
            ivCartFav = itemView.findViewById(R.id.ivcartfav);

            btnDeleteProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(miContext)
                            .setTitle("Cart")
                            .setMessage("Do you want to remove from cart?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    int pos = getAdapterPosition();
                                    sessionManager.removeProductFromCart(miContext, pos);
                                    list.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, list.size());
                                    notifyDataSetChanged();
                                    removeCart.removeCart();
                                }
                            }).create().show();

                }
            });


            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    CartRVModel cartRVModel = list.get(pos);

                    int val = cartRVModel.getProdQty() + 1;
                    tvCartQuantity.setText(String.valueOf(val));
                    cartRVModel.setProdQty(cartRVModel.getProdQty() + 1);
                    sessionManager.updateCart(miContext, 1, cartRVModel);
                    notifyItemRangeChanged(pos, list.size());
                    addRemoveCartListener.addRemoveCart();
                    buttonClickActions();
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    CartRVModel cartRVModel = list.get(pos);
                    if (!(cartRVModel.getProdQty() == 1)) {
                        int val = cartRVModel.getProdQty() - 1;
                        tvCartQuantity.setText(String.valueOf(val));
                        cartRVModel.setProdQty(cartRVModel.getProdQty() - 1);
                        sessionManager.updateCart(miContext, 0, cartRVModel);
                        notifyItemRangeChanged(pos, list.size());
                        addRemoveCartListener.addRemoveCart();
                        buttonClickActions();
                    }
                }
            });
        }
    }
    private void buttonClickActions() {

        sessionManager.getSubTotal(miContext);

        if (updateItemPrice != null) {
            updateItemPrice.onItemCountChanged(list.size(), sessionManager.getSubTotal(miContext));
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
