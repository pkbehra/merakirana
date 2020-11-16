package com.example.Adapter;


import android.content.Context;
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

import com.example.Interface.AddToCartListener;
import com.example.Interface.RemoveCart;
import com.example.Interface.RemoveWishlist;
import com.example.Model.CartRVModel;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.ProductDetailsActivity;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.Holder> {

    private Context mContext;
    private ArrayList<WishlistRVModel> list;
    private SessionManager sessionManager;
    RemoveWishlist wishlistRemove;
    RemoveCart removeCart;
    //MyCartInterface myCartInterface;
    AddToCartListener addToCartListener;
    RelativeLayout rlWishlist;

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;
    ArrayList<CartRVModel> cartModelArrayList;

    public WishlistAdapter(Context context, ArrayList<WishlistRVModel> wishlistRVModelArrayList) {
        this.mContext = context;
        this.list = wishlistRVModelArrayList;
        this.wishlistRemove= (RemoveWishlist) context;
        sessionManager = new SessionManager(context);
        this.addToCartListener = (AddToCartListener) context;
        this.removeCart=(RemoveCart)context;

        HashMap<String, String> userWishList = sessionManager.getUserDetails();

        try {
            this.addToCartListener = ((AddToCartListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_list_item, viewGroup, false);

        return new Holder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        cartModelArrayList=new ArrayList<>();
        cartModelArrayList=sessionManager.getAddToCartList(mContext);

        final WishlistRVModel current = list.get(position);

        holder.textName.setText(current.getName());
        holder.textPrice.setText("$ "+current.getPrice());

        String img_url=mContext.getResources().getString(R.string.PROD_IMG_URL)+current.getLogo();

        Picasso.with(mContext)
                .load(img_url)
                .placeholder(R.drawable.loader)
                .fit().centerInside()
                .into(holder.imageView);


        if(listfeature.contains("Cart")){
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textDesc,tvCartQuantity;
        ImageView imageView, ivdelete,ivWishlistAdd,ivCart;

        RelativeLayout rlWishlist;
        Button btnDecrease, btnIncrease;
        LinearLayout lvCartQuantity;
        RelativeLayout rlCartLayout;

        Holder(View itemview) {
            super(itemview);

            textName = itemview.findViewById(R.id.tvWishlistProductName);
            imageView = itemview.findViewById(R.id.ivWishlistProductImage);
            textDesc = itemview.findViewById(R.id.tvWishlistProductDescription);
            textPrice = itemview.findViewById(R.id.tvWishlistProductPrice);
            ivdelete = itemview.findViewById(R.id.ivWishlistDeleteProduct);
            ivWishlistAdd = itemview.findViewById(R.id.ivcartWishlist);
            ivCart=itemview.findViewById(R.id.ivcartWishlist);
            rlWishlist = itemview.findViewById(R.id.rlWishlist);

            lvCartQuantity=itemView.findViewById(R.id.lvCartQuantity);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            rlCartLayout=itemview.findViewById(R.id.rlCartLayout);


            ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //CartRVModel cartRVModel = arrayList.get(getAdapterPosition());

                    new AlertDialog.Builder(mContext)
                            .setTitle("Wishlist")
                            .setMessage("Do you want to remove from Wishlist?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    int pos = getAdapterPosition();
                                    sessionManager.removeProductFromWishlist(mContext, list.get(pos).getProductId());
                                    list.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, list.size());
                                    notifyDataSetChanged();
                                    wishlistRemove.removeWishlist();
                                }
                            }).create().show();
                }
            });

            ivCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ivCart.setVisibility(View.GONE);
                    lvCartQuantity.setVisibility(View.VISIBLE);

                    WishlistRVModel   current = list.get(getAdapterPosition());

                    CartRVModel cartProduct=new CartRVModel();

                    cartProduct.setPrice(current.getPrice());
                    cartProduct.setLogo(current.getLogo());
                    cartProduct.setName(current.getName());
                    cartProduct.setProductId(current.getProductId());
                    cartProduct.setId(current.getId());
                    cartProduct.setProdQty(1);
                    cartProduct.setDescriptions(current.getDescriptions());
                    sessionManager.addToCart(mContext,cartProduct);
                    addToCartListener.addToCart();

                }
            });

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WishlistRVModel model=list.get(getAdapterPosition());
                    Intent intent=new Intent(mContext, ProductDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("prodAutoId",model.getId());
                    intent.putExtra("prodId",model.getProductId());
                    intent.putExtra("Product_Logo",model.getLogo());
                    intent.putExtra("Product_Name",model.getName());
                    intent.putExtra("Product_Price",model.getPrice());
                    intent.putExtra("Product_Qty","");
                    intent.putExtra("Product_Unit","");
                    intent.putExtra("Product_Description",model.getDescriptions());
                    Log.d("product_auto_id",model.getId());
                    mContext.startActivity(intent);
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
                        ivCart.setVisibility(View.VISIBLE);
                        lvCartQuantity.setVisibility(View.GONE);
                        sessionManager.removeProductFromCart(mContext, i);
                        removeCart.removeCart();
                    }
                }
            });


        }
    }
}
