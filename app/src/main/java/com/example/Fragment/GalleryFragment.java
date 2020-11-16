package com.example.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Adapter.GalleryAdapter;
import com.example.Model.GalleryRVModel;
import com.example.merakirana.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {


    RecyclerView recyclerView;
    int topProdImage[];
    GalleryAdapter galleryAdapter;

    private List<GalleryRVModel> galleryRVModelList = new ArrayList<>();



    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.rvGallery);

        galleryData();

        return view;

    }

    private void galleryData(){

        topProdImage = new int[]{R.drawable.ctire,
                R.drawable.engine,
                R.drawable.hub,
                R.drawable.winch};

        for (int i = 0; i < topProdImage.length; i++) {
            GalleryRVModel galleryRVModel = new GalleryRVModel();
            galleryRVModel.setProdImage(topProdImage[i]);
            galleryRVModelList.add(galleryRVModel);
        }
        galleryAdapter = new GalleryAdapter(getContext(), galleryRVModelList);

        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2,
                GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

}