package com.example.plantclicker.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.example.plantclicker.Utils;

public class ShopItem {

    public final int price;
    public final String name, description;
    final String fileName;

    public ShopItem(int price, String name, String description, String fileName) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.fileName = fileName;
    }

    public Drawable getDrawable(Context c) {
        return Utils.getDrawable(c, fileName);
    }
}
