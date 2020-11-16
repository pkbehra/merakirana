package com.example.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class WishlistRVModel implements Parcelable {

    private String id,productId,mainCategoryAutoId,mainCategoryId,subCategoryAutoId,subCategoryId,name,logo,price,descriptions,status;

    public WishlistRVModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMainCategoryAutoId() {
        return mainCategoryAutoId;
    }

    public void setMainCategoryAutoId(String mainCategoryAutoId) {
        this.mainCategoryAutoId = mainCategoryAutoId;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getSubCategoryAutoId() {
        return subCategoryAutoId;
    }

    public void setSubCategoryAutoId(String subCategoryAutoId) {
        this.subCategoryAutoId = subCategoryAutoId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Creator<WishlistRVModel> getCREATOR() {
        return CREATOR;
    }

    public WishlistRVModel(Parcel in)
    {
        id = in.readString();
        productId = in.readString();
        mainCategoryAutoId = in.readString();
        mainCategoryId = in.readString();
        subCategoryAutoId = in.readString();
        subCategoryId = in.readString();
        name = in.readString();
        logo = in.readString();
        price = in.readString();
        descriptions = in.readString();
        status = in.readString();
    }

    public static final Creator<WishlistRVModel> CREATOR = new Creator<WishlistRVModel>()
    {
        @Override
        public WishlistRVModel createFromParcel(Parcel in)
        {
            return new WishlistRVModel(in);
        }

        @Override
        public WishlistRVModel[] newArray(int size)
        {
            return new WishlistRVModel[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(productId);
        parcel.writeString(mainCategoryAutoId);
        parcel.writeString(mainCategoryId);
        parcel.writeString(subCategoryAutoId);
        parcel.writeString(subCategoryId);
        parcel.writeString(name);
        parcel.writeString(logo);
        parcel.writeString(price);
        parcel.writeString(descriptions);
        parcel.writeString(status);
    }


}
