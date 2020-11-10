package com.example.Model;


public class MenuModel  {

    String menuName,auto_id;
    int img;
    boolean isGroup,isChild;


    public MenuModel(String menuName, String auto_id, int img, boolean isGroup, boolean isChild) {
        this.menuName = menuName;
        this.auto_id=auto_id;
        this.img = img;
        this.isGroup = isGroup;
        this.isChild = isChild;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getImg() {
        return img;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setChild(boolean child) {
        isChild = child;
    }
}
