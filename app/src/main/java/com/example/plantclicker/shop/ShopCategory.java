package com.example.plantclicker.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.example.plantclicker.Utils;

public enum ShopCategory {

    LAMPS("Лампы", new ShopItem[] {
            new ShopItem(20, "Свеча", "Призраков будет видно", "candle"),
            new ShopItem(50, "Масляная лампа", "Лучше чем ничего", "oil_lamp"),
            new ShopItem(100, "Фонарик", "С таким охранники ходят", "flashlight"),
            new ShopItem(500, "Настольная лампа", "Приятный теплый свет 😊", "lamp"),
            new ShopItem(2000, "LED", "Профессиональое освещение, ярче солнца", "ledlight"),
            new ShopItem(5000, "Фитолампа", "Световой спектр лампы не содержит зелёного света, специально для растений", "ultraviolet"),
            new ShopItem(10000, "Прожектор", "Осторожно, горячо!", "spotlight")
    }, "lightbulb"),

    BOOSTERS("Удобрения", new ShopItem[] {
            new ShopItem(10, "Какое-то удобрение с фермы", "Лучше чем ничего - избитая фраза", "fertilizerleon"),
            new ShopItem(50, "Удобрение роста", "Оно растет, ура!", "fertilizergrow"),
            new ShopItem(100, "Настоящее удобренеие", "Это что, рашн кампост?", "fertilizerusual"),
            new ShopItem(300, "Пшикалка", "Пш, пш, прикольно", "fertilizerpshik"),
    }, "fertilizer");

    public final String name;
    public final ShopItem[] items;
    final String iconName;

    ShopCategory(String name, ShopItem[] items, String iconName) {
        this.name = name;
        this.items = items;
        this.iconName = iconName;
    }

    public Drawable getDrawable(Context c) {
        return Utils.getDrawable(c, iconName);
    }

}
