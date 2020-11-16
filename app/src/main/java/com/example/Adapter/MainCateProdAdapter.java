package com.example.Adapter;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interface.AddToCartListener;
import com.example.Interface.RefreshHome;
import com.example.Interface.RemoveCart;
import com.example.Model.CartRVModel;
import com.example.Model.MainCategoryProductsModel;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.ProductDetailsActivity;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class MainCateProdAdapter extends RecyclerView.Adapter<MainCateProdAdapter.Holder> {

    List<MainCategoryProductsModel> list;
    Activity activity;
    SessionManager sessionManager;
    AddToCartListener addToCartListener;
    RemoveCart removeCart;
    RefreshHome refreshHome;

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;
    ArrayList<CartRVModel> cartModelArrayList;


    public MainCateProdAdapter(List<MainCategoryProductsModel> list, Activity activity,RefreshHome refreshHome) {
        this.list = list;
        this.activity = activity;
        sessionManager=new SessionManager(activity);
        addToCartListener= (AddToCartListener) activity;
        removeCart=(RemoveCart) activity;
        this.refreshHome=refreshHome;

        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View v=layoutInflater.inflate(R.layout.list_auto_access,null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        cartModelArrayList=new ArrayList<>();
        cartModelArrayList=sessionManager.getAddToCartList(activity);


        MainCategoryProductsModel current= list.get(position);

        holder.tvProdName.setText(current.getProductNameEnglish());
        holder.tvPrice.setText("$ "+current.getProductPrice());
        holder.tvDesc.setText(current.getDescriptionsEnglish());

        String img_url=activity.getResources().getString(R.string.PROD_IMG_URL)+current.getProductLogo();

        Picasso.with(activity)
                .load(img_url)
                .placeholder(R.drawable.loader)
                .error(R.drawable.loader)
                .fit().centerInside()
                .into(holder.ivproductImage);


        holder.ivWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(activity);
                if(wishlistProductLists!=null&&wishlistProductLists.size()!=0){

                    for(int i=0;i<wishlistProductLists.size();i++){

                        if(list.get(position).getProductId().equalsIgnoreCase(wishlistProductLists.get(i).getProductId())) {

                            final int finalI = i;
                            new AlertDialog.Builder(activity)
                                    .setTitle("Wishlist")
                                    .setMessage("Do you want to remove from Wishlist?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            sessionManager.removeProductFromWishlist(activity,wishlistProductLists.get(finalI).getProductId());
                                            holder.ivWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(activity, "Removed from  Wishlist", Toast.LENGTH_SHORT).show();
                                            refreshHome.notifyHomeList();
                                        }
                                    }).create().show();


                        }else {

                            // CosmeticArrivalModel model = list.get(pos);
                            MainCategoryProductsModel model = list.get(position);
                            holder.ivWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                            WishlistRVModel wishlistProductList = new WishlistRVModel();
                            wishlistProductList.setPrice(model.getProductPrice());
                            wishlistProductList.setStatus("1");
                            wishlistProductList.setName(model.getProductNameEnglish());
                            wishlistProductList.setId(model.getId());
                            wishlistProductList.setProductId(model.getProductId());
                            wishlistProductList.setLogo(model.getProductLogo());
                            wishlistProductList.setDescriptions(model.getDescriptionsEnglish());
                            // wishlistProductList.setImagelogo(model.getSinglelogo());
                            // wishlistProductList.setUnit(model.getProdQty());
                            // wishlistProductList.setWeight(model.getWight());
                            sessionManager.addToWishlist(activity, wishlistProductList);
                            refreshHome.notifyHomeList();

                        }

                    }


                }else{
                    MainCategoryProductsModel model = list.get(position);
                    holder.ivWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                    WishlistRVModel wishlistProductList = new WishlistRVModel();
                    wishlistProductList.setPrice(model.getProductPrice());
                    wishlistProductList.setStatus("1");
                    wishlistProductList.setDescriptions(model.getDescriptionsEnglish());
                    wishlistProductList.setName(model.getProductNameEnglish());
                    wishlistProductList.setId(model.getId());
                    wishlistProductList.setProductId(model.getProductId());
                    wishlistProductList.setLogo(model.getProductLogo());
                    //wishlistProductList.setImagelogo(model.getSinglelogo());
                    // wishlistProductList.setUnit(model.getProdQty());
                    // wishlistProductList.setWeight(model.getWight());
                    sessionManager.addToWishlist(activity, wishlistProductList);
                    refreshHome.notifyHomeList();
                }

            }

        });

        if (sessionManager.getAddToWishlist(activity)!=null){
            ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(activity);

            for (int k = 0; k < wishlistProductLists.size(); k++) {
                WishlistRVModel rvModel = wishlistProductLists.get(k);

                if (rvModel.getProductId()!=null && rvModel.getProductId().equals(list.get(position).getProductId())){
                    holder.ivWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);

                }
            }
        }


        if(listfeature.contains("Wishlist"))
        {
            holder.ivWishlist.setVisibility(View.VISIBLE);
        }else {
            holder.ivWishlist.setVisibility(View.INVISIBLE);
        }

        if(listfeature.contains("Cart"))
        {
            holder.rlCartLayout.setVisibility(View.VISIBLE);
        }else {
            holder.rlCartLayout.setVisibility(View.INVISIBLE);
        }

        //check if item is already added to cart
        if(cartModelArrayList!=null&&cartModelArrayList.size()!=0){

            for(int j=0;j<cartModelArrayList.size();j++){
                if(current.getProductId().equalsIgnoreCase(cartModelArrayList.get(j).getProductId())) {
                    holder.ivCart.setVisibility(View.GONE);
                    holder.lvCartQuantity.setVisibility(View.VISIBLE);
                    holder.tvCartQuantity.setText(""+cartModelArrayList.get(j).getProdQty());
                    break;
                }else {
                    holder.ivCart.setVisibility(View.VISIBLE);
                    holder.lvCartQuantity.setVisibility(View.GONE);
                }
            }
        }else {
            holder.ivCart.setVisibility(View.VISIBLE);
            holder.lvCartQuantity.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvProdName, tvPrice,tvDesc,tvCartQuantity;
        ImageView ivproductImage,ivCart,ivWishlist;
        Button btnDecrease, btnIncrease;
        LinearLayout lvCartQuantity;
        RelativeLayout rlCartLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvProdName=(TextView)itemView.findViewById(R.id.tvProdName);
            tvPrice=(TextView)itemView.findViewById(R.id.tvProdPrice);
            tvDesc=(TextView)itemView.findViewById(R.id.tvProdDesc);
            ivproductImage=(ImageView)itemView.findViewById(R.id.ivProdImage);
            ivCart=(ImageView)itemView.findViewById(R.id.ivCart);
            ivWishlist=(ImageView)itemView.findViewById(R.id.ivWishlist);

            lvCartQuantity=itemView.findViewById(R.id.lvCartQuantity);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            rlCartLayout=itemView.findViewById(R.id.rlCartLayout);


            ivCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivCart.setVisibility(View.GONE);
                    lvCartQuantity.setVisibility(View.VISIBLE);

                    MainCategoryProductsModel   current = list.get(getAdapterPosition());

                    // ivCart.setImageResource(R.drawable.ic_cart_grey_24dp);

                    CartRVModel cartProduct=new CartRVModel();

                    cartProduct.setPrice(current.getProductPrice());
                    cartProduct.setLogo(current.getProductLogo());
                    cartProduct.setName(current.getProductNameEnglish());
                    cartProduct.setId(current.getId());
                    cartProduct.setProductId(current.getProductId());
                    cartProduct.setProdQty(1);
                    cartProduct.setDescriptions(current.getDescriptionsEnglish());
                    sessionManager.addToCart(activity,cartProduct);
                    addToCartListener.addToCart();

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainCategoryProductsModel model=list.get(getAdapterPosition());
                    Intent intent=new Intent(activity, ProductDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("prodAutoId",model.getId());
                    intent.putExtra("prodId",model.getProductId());
                    intent.putExtra("Product_Logo",model.getProductLogo());
                    intent.putExtra("Product_Name",model.getProductNameEnglish());
                    intent.putExtra("Product_Price",model.getProductPrice());
                    intent.putExtra("Product_Qty",model.getProductQty());
                    intent.putExtra("Product_Unit",model.getProductWeight());
                    intent.putExtra("Product_Description",model.getDescriptionsEnglish());
                    Log.d("product_auto_id",model.getId());
                    activity.startActivity(intent);
                }
            });

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    ArrayList<CartRVModel> lcartModelArrayList=sessionManager.getAddToCartList(activity);
                    CartRVModel cartRVModel=null;

                    for(int i=0;i<lcartModelArrayList.size();i++)
                    {
                        if(lcartModelArrayList.get(i).getProductId().equals(list.get(pos).getProductId()))
                        {
                            cartRVModel = lcartModelArrayList.get(i);
                            break;
                        }
                    }


                    int val = cartRVModel.getProdQty() + 1;
                    tvCartQuantity.setText(String.valueOf(val));
                    cartRVModel.setProdQty(cartRVModel.getProdQty() + 1);
                    sessionManager.updateCart(activity, 1, cartRVModel);
                    //notifyItemRangeChanged(pos, list.size());
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i;
                    int pos = getAdapterPosition();

                    ArrayList<CartRVModel> lcartModelArrayList=sessionManager.getAddToCartList(activity);
                    CartRVModel cartRVModel=null;

                    for(i=0;i<lcartModelArrayList.size();i++)
                    {
                        if(lcartModelArrayList.get(i).getProductId().equals(list.get(pos).getProductId()))
                        {
                            cartRVModel = lcartModelArrayList.get(i);
                            break;
                        }
                    }

                    if (cartRVModel.getProdQty() > 1) {
                        int val = cartRVModel.getProdQty() - 1;
                        tvCartQuantity.setText(String.valueOf(val));
                        cartRVModel.setProdQty(cartRVModel.getProdQty() - 1);
                        sessionManager.updateCart(activity, 0, cartRVModel);
                        // notifyItemRangeChanged(pos, list.size());
                    }else {
                        ivCart.setVisibility(View.VISIBLE);
                        lvCartQuantity.setVisibility(View.GONE);
                        sessionManager.removeProductFromCart(activity, i);
                        removeCart.removeCart();
                    }
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
