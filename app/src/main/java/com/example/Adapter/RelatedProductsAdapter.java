package com.example.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.Interface.AddToCartListener;
import com.example.Model.AutoAccessRVModel;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.R;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RelatedProductsAdapter extends RecyclerView.Adapter<RelatedProductsAdapter.MyViewHolder> {


    Fragment fragment;
    private List<AutoAccessRVModel> autoAccessRVModelList;
    private List<WishlistRVModel> wishlistRVModelList;

    private Context miContext;
    private SessionManager sessionManager;
    PopupWindow popupWindow;
    AddToCartListener addToCartListener;
    private String user_id;

    public RelatedProductsAdapter(Context miContext, List<AutoAccessRVModel> autoAccessRVModelList) {
        this.autoAccessRVModelList = autoAccessRVModelList;
        this.miContext = miContext;
        this.sessionManager=new SessionManager(miContext);
        this.addToCartListener= (AddToCartListener) miContext;

        HashMap<String, String> userWishList = sessionManager.getUserDetails();
        user_id = userWishList.get(sessionManager.KEY_ID);

        try {
            this.addToCartListener = ((AddToCartListener) miContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvProdName, tvPrice,tvDesc;
        ImageView ivproductImage,ivCart,ivWishlist;

        public MyViewHolder(@NonNull final View itemView) {

            super(itemView);
            tvProdName=(TextView)itemView.findViewById(R.id.tvProdName);
            tvPrice=(TextView)itemView.findViewById(R.id.tvProdPrice);
            tvDesc=(TextView)itemView.findViewById(R.id.tvProdDesc);
            ivproductImage=(ImageView)itemView.findViewById(R.id.ivProdImage);
            ivCart=(ImageView)itemView.findViewById(R.id.ivCart);
            ivWishlist=(ImageView)itemView.findViewById(R.id.ivWishlist);

            ivproductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    AutoAccessRVModel   autoProducts = autoAccessRVModelList.get(pos);

                    tvProdName.setText(autoProducts.getTitle());
                    tvPrice.setText(autoProducts.getPrice());
                    ivproductImage.setImageResource(autoProducts.getImg());


                    LayoutInflater factory = LayoutInflater.from(miContext);
/*

                    final View dialogView = factory.inflate(R.layout.popup_window, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(miContext).create();
                    alertDialog.setView(dialogView);
                    TextView tvProductName = dialogView.findViewById(R.id.tvProdName);
                    tvProductName.setText(mkproducts.getTitle());

                    final TextView tvPrice = dialogView.findViewById(R.id.tvPrice);
                    tvPrice.setText(mkproducts.getPrice());

                    ImageView ivProductImage = dialogView.findViewById(R.id.ivproduct);
                    ivProductImage.setImageResource(mkproducts.getImg());

                    dialogView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //your business logic
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
*/

                }
            });

/*
            ivCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AutoAccessRVModel   current = autoAccessRVModelList.get(getAdapterPosition());

                    CartRVModel cartProduct=new CartRVModel();

                    cartProduct.setProdPrice(current.price);
                    cartProduct.setProdImage(current.img);
                    cartProduct.setProdName(current.title);
                    cartProduct.setProductID(current.id);
                    cartProduct.setProdQty(1);
                    sessionManager.addToCart(miContext,cartProduct);
                    addToCartListener.addToCart();

                }
            });
*/

/*
            ivWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AutoAccessRVModel   current = autoAccessRVModelList.get(getAdapterPosition());

                    ivWishlist.setVisibility(View.GONE);
                    ivOffersWishlistRed.setVisibility(View.VISIBLE);

                    WishlistRVModel   wishlistProductList = new WishlistRVModel();

                    wishlistProductList.setProduct_price(current.price);
                    wishlistProductList.setProdImage(current.img);
                    wishlistProductList.setProduct_name(current.title);
                    wishlistProductList.setProduct_id(current.id);
                    sessionManager.addToWishlist(miContext,wishlistProductList);
                    Toast.makeText(miContext, "Added in Wishlist", Toast.LENGTH_SHORT).show();

                }
            });
*/

        }
    }


    @NonNull
    @Override
    public RelatedProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_auto_access,viewGroup,false);

        return new RelatedProductsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedProductsAdapter.MyViewHolder viewHolder, int pos) {

        AutoAccessRVModel make= autoAccessRVModelList.get(pos);

        viewHolder.tvProdName.setText(make.getTitle());
        viewHolder.tvPrice.setText(make.getPrice());
        viewHolder.ivproductImage.setImageResource(make.getImg());
        viewHolder.tvDesc.setText(make.getDesp());


    }

    @Override
    public int getItemCount() {
        return autoAccessRVModelList.size();
    }
}
