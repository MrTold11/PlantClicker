package com.example.plantclicker.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.example.plantclicker.Utils;

public class ShopItem {

    public final int price;
    private final int nameId, descriptionId;
    final String fileName;

    public final Object value;

    public ShopItem(int price, Object value, int nameId, int descriptionId, String fileName) {
        this.price = price;
        this.value = value;
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.fileName = fileName;
    }

    /**
     * Use this constructor if item's name and description
     * can be accessed from R.string (located in {@code strings.xml}).
     * Note that its name string id must be equal to its filename (without file extension)
     * and its description string id must be named {@code {name}_desc} as well
     * @param price item price
     * @param value custom value
     * @param name item's name and icon filename
     */
    public ShopItem(int price, Object value, String name) {
        this.price = price;
        this.value = value;
        this.fileName = name;
        this.nameId = 0;
        this.descriptionId = 0;
    }

    private int getNameId(Context c) {
        return nameId != 0 ? nameId
                : Utils.getResourceId(c, fileName, "string");
    }

    private int getDescriptionId(Context c) {
        return descriptionId != 0 ? descriptionId
                : Utils.getResourceId(c, fileName + "_desc", "string");
    }

    public Drawable getDrawable(Context c) {
        return Utils.getDrawable(c, fileName);
    }

    public String getName(Context c) {
        return Utils.getString(c, getNameId(c));
    }

    public String getId() {
        return fileName;
    }

    public String getDescription(Context c) {
        return Utils.getString(c, getDescriptionId(c));
    }

}
