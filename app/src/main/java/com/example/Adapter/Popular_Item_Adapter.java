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
import com.example.Model.PopularProdModel;
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

public class Popular_Item_Adapter extends RecyclerView.Adapter<Popular_Item_Adapter.ItemViewHolder> {

    private Activity mContext;
    private List<PopularProdModel> list;
    private SessionManager sessionManager;
    AddToCartListener addToCartListener;
    RemoveCart removeCart;
    RefreshHome refreshHome;

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;
    ArrayList<CartRVModel> cartModelArrayList;

    // String user_id;

    public Popular_Item_Adapter(Activity context, List<PopularProdModel> list,RefreshHome refreshHome) {
        this.mContext = context;
        this.list = list;
        this.sessionManager = new SessionManager(mContext);
        this.addToCartListener=(AddToCartListener) context;
        this.removeCart=(RemoveCart) context;
        this.refreshHome=refreshHome;


        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView topmostImageView, ivTopMostWishlist, ivTopmostCart, ivPopularWishlistRed;
        TextView topmostName, topmostPrice, desc,tvCartQuantity;
        Button btnTopMostAdd, btnTopMostDecrease, btnTopMostIncrease;
        Button btnDecrease, btnIncrease;
        LinearLayout lvCartQuantity;
        RelativeLayout rlCartLayout;

        ItemViewHolder(final View itemView) {
            super(itemView);

            topmostImageView = itemView.findViewById(R.id.topmostImageView);
            ivTopMostWishlist = itemView.findViewById(R.id.ivPopularWishlist);
            topmostName = itemView.findViewById(R.id.topmostName);
            topmostPrice = itemView.findViewById(R.id.topmostPrice);
            ivTopmostCart = itemView.findViewById(R.id.ivTopCart);
            ivPopularWishlistRed = itemView.findViewById(R.id.ivPopularWishlistRed);
            desc = itemView.findViewById(R.id.tvDesc);

            lvCartQuantity=itemView.findViewById(R.id.lvCartQuantity);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            rlCartLayout=itemView.findViewById(R.id.rlCartLayout);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopularProdModel model=list.get(getAdapterPosition());
                    Log.e("id",model.getId());
                    Intent intent=new Intent(mContext, ProductDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("prodAutoId",model.getId());
                    intent.putExtra("prodId",model.getProductId());
                    intent.putExtra("Product_Logo",model.getProductLogo());
                    intent.putExtra("Product_Name",model.getProductNameEnglish());
                    intent.putExtra("Product_Price",model.getProductPrice());
                    intent.putExtra("Product_Qty",model.getProductQty());
                    intent.putExtra("Product_Unit",model.getProductWeight());
                    intent.putExtra("Product_Description",model.getDescriptionsEnglish());
                    mContext.startActivity(intent);
                }
            });


            ivTopmostCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivTopmostCart.setVisibility(View.GONE);
                    lvCartQuantity.setVisibility(View.VISIBLE);

                    PopularProdModel   current = list.get(getAdapterPosition());

                    // ivTopmostCart.setImageResource(R.drawable.ic_cart_grey_24dp);

                    CartRVModel cartProduct=new CartRVModel();

                    cartProduct.setPrice(current.getProductPrice());
                    cartProduct.setLogo(current.getProductLogo());
                    cartProduct.setName(current.getProductNameEnglish());
                    cartProduct.setId(current.getId());
                    cartProduct.setProductId(current.getProductId());
                    cartProduct.setProdQty(1);
                    cartProduct.setDescriptions(current.getDescriptionsEnglish());
                    sessionManager.addToCart(mContext,cartProduct);
                    addToCartListener.addToCart();

                }
            });

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    ArrayList<CartRVModel> lcartModelArrayList=sessionManager.getAddToCartList(mContext);
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
                    sessionManager.updateCart(mContext, 1, cartRVModel);
                    //notifyItemRangeChanged(pos, list.size());
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i;
                    int pos = getAdapterPosition();

                    ArrayList<CartRVModel> lcartModelArrayList=sessionManager.getAddToCartList(mContext);
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
                        sessionManager.updateCart(mContext, 0, cartRVModel);
                        // notifyItemRangeChanged(pos, list.size());
                    }else {
                        ivTopmostCart.setVisibility(View.VISIBLE);
                        lvCartQuantity.setVisibility(View.GONE);
                        sessionManager.removeProductFromCart(mContext, i);
                        removeCart.removeCart();
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_popular_item, viewGroup, false);

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {

        cartModelArrayList=new ArrayList<>();
        cartModelArrayList=sessionManager.getAddToCartList(mContext);


        final PopularProdModel current = list.get(position);


        holder.topmostName.setText(current.getProductNameEnglish());
        holder.topmostPrice.setText("$ "+current.getProductPrice());
        holder.desc.setText(current.getDescriptionsEnglish());

        String img_url=mContext.getResources().getString(R.string.PROD_IMG_URL)+current.getProductLogo();

        Picasso.with(mContext)
                .load(img_url)
                .placeholder(R.drawable.loader)
                .error(R.drawable.loader)
                .fit().centerInside()
                .into(holder.topmostImageView);



        holder.ivTopMostWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(mContext);
                if(wishlistProductLists!=null&&wishlistProductLists.size()!=0){

                    for(int i=0;i<wishlistProductLists.size();i++){

                        if(list.get(position).getProductId().equalsIgnoreCase(wishlistProductLists.get(i).getProductId())) {

                            final int finalI = i;
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Wishlist")
                                    .setMessage("Do you want to remove from Wishlist?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            sessionManager.removeProductFromWishlist(mContext,wishlistProductLists.get(finalI).getProductId());
                                            holder.ivTopMostWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(mContext, "Removed from  Wishlist", Toast.LENGTH_SHORT).show();
                                            refreshHome.notifyHomeList();
                                        }
                                    }).create().show();


                        }else {

                            // CosmeticArrivalModel model = list.get(pos);
                            PopularProdModel model = list.get(position);
                            holder.ivTopMostWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
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
                            sessionManager.addToWishlist(mContext, wishlistProductList);
                            refreshHome.notifyHomeList();


                        }

                    }


                }else{
                    PopularProdModel model = list.get(position);
                    holder.ivTopMostWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
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
                    sessionManager.addToWishlist(mContext, wishlistProductList);
                    refreshHome.notifyHomeList();
                }

            }

        });

        if (sessionManager.getAddToWishlist(mContext)!=null){
            ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(mContext);

            for (int k = 0; k < wishlistProductLists.size(); k++) {
                WishlistRVModel rvModel = wishlistProductLists.get(k);

               /* if (rvModel.getProductId()!=null && rvModel.getProductId().equals(list.get(position).getProductId())){
                    holder.ivTopMostWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);

                }*/
                if (rvModel.getProductId().equals(list.get(position).getProductId())){
                    holder.ivTopMostWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);

                }
            }
        }



        if(listfeature.contains("Wishlist"))
        {
            holder.ivTopMostWishlist.setVisibility(View.VISIBLE);
        }else {
            holder.ivTopMostWishlist.setVisibility(View.INVISIBLE);
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
                    holder.ivTopmostCart.setVisibility(View.GONE);
                    holder.lvCartQuantity.setVisibility(View.VISIBLE);
                    holder.tvCartQuantity.setText(""+cartModelArrayList.get(j).getProdQty());
                    break;
                }else {
                    holder.ivTopmostCart.setVisibility(View.VISIBLE);
                    holder.lvCartQuantity.setVisibility(View.GONE);
                }
            }
        }else {
            holder.ivTopmostCart.setVisibility(View.VISIBLE);
            holder.lvCartQuantity.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
