package com.example.plantclicker.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.example.plantclicker.R;
import com.example.plantclicker.Utils;

public enum ShopCategory {

    LAMPS(R.string.lamps, "lightbulb",
            new ShopItem(20, 1, "candle"),
            new ShopItem(50, 2, "oil_lamp"),
            new ShopItem(100, 5, "flashlight"),
            new ShopItem(500, 7, "lamp"),
            new ShopItem(2000, 10, R.string.ledlight, R.string.led_desc, "ledlight"),
            new ShopItem(5000, 15, "phytolamp"),
            new ShopItem(10000, 20, "spotlight")
    ),

    BOOSTERS(R.string.fertilizers, "fertilizer",
            new ShopItem(10, 2, "farm_fertilizer"),
            new ShopItem(50, 5, "growth_fertilizer"),
            new ShopItem(100, 7, "real_fertilizer"),
            new ShopItem(300, 10, "spray")
    );

    private final int nameId;
    public final ShopItem[] items;
    final String iconName;

    ShopCategory(int nameId, String iconName, ShopItem... items) {
        this.nameId = nameId;
        this.items = items;
        this.iconName = iconName;
    }

    public Drawable getDrawable(Context c) {
        return Utils.getDrawable(c, iconName);
    }

    public String getName(Context c) {
        return c.getResources().getString(nameId);
    }

    public String getId() {
        return iconName;
    }

}
