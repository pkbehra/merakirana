package com.example.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Adapter.WishlistAdapter;
import com.example.Interface.AddToCartListener;
import com.example.Interface.RemoveCart;
import com.example.Interface.RemoveWishlist;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.HomeActivity;
import com.example.merakirana.R;
import com.example.service.BlockStatusService;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment implements RemoveWishlist, AddToCartListener, RemoveCart {

    private View view;
    private RecyclerView recyclerViewWishlist;
    private WishlistAdapter wishlistAdapter;
    private ArrayList<WishlistRVModel> wishlistProductLists = new ArrayList<>();
    private TextView tvNoProductWishlistMsg, tvWishlistLogin, tvWishlistSignup,tvWishlistEmpty;
    private LinearLayout llWishlistEmpty;
    private SessionManager sessionManager;

    public static WishlistFragment newInstance() {
        WishlistFragment fragment = new WishlistFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        init();
        showWishlist();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    return true;

                }

                return false;
            }
        });

        return view;

    }

    public void init() {

        recyclerViewWishlist = (RecyclerView) view.findViewById(R.id.rvWishlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewWishlist.setLayoutManager(linearLayoutManager);
        recyclerViewWishlist.setHasFixedSize(true);
        recyclerViewWishlist.setItemAnimator(new DefaultItemAnimator());
        tvWishlistEmpty=(TextView)view.findViewById(R.id.tvWishlistEmpty);

        sessionManager = new SessionManager(getContext());
        llWishlistEmpty = view.findViewById(R.id.llWishlistEmpty);

    }


    public void showWishlist(){
        ArrayList<WishlistRVModel> arrayList = sessionManager.getAddToWishlist(getContext());
        if ((arrayList == null) || (arrayList.size() == 0)) {

            recyclerViewWishlist.setVisibility(View.GONE);
            llWishlistEmpty.setVisibility(View.VISIBLE);
        }

        else {

            wishlistAdapter = new WishlistAdapter(getContext(), arrayList);
            wishlistAdapter.notifyDataSetChanged();
            recyclerViewWishlist.setAdapter(wishlistAdapter);
            llWishlistEmpty.setVisibility(View.GONE);
            recyclerViewWishlist.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void addToCart() {
        ArrayList<WishlistRVModel> arrayList = sessionManager.getAddToWishlist(getContext());

        wishlistAdapter = new WishlistAdapter(getContext(), arrayList);
        recyclerViewWishlist.setAdapter(wishlistAdapter);

    }

    @Override
    public void removeWishlist() {
        showWishlist();
    }


    @Override
    public void onResume() {
        super.onResume();

        //to check block status register broadcast
        Intent background = new Intent(getActivity(), BlockStatusService.class);
        getActivity().startService(background);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));
    }

    @Override
    public void onPause() {
        super.onPause();

        //to check block status Unregister broadcast
        Intent background = new Intent(getActivity(), BlockStatusService.class);
        getActivity().stopService(background);
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("efunhub.com.automobile.closeactivity")) {
                getActivity().finish();
            }
        }
    };

    @Override
    public void removeCart() {

    }
}
